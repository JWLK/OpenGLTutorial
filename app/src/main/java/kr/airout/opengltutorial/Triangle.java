package kr.airout.opengltutorial;

import android.opengl.GLES31;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {

    private FloatBuffer vertexBuffer;
    private final int mProgram;

    private static final String vertexShaderCode =
            "#version 300 es\n" +
                    "in vec4 vPosition;\n" +
                    "void main() {\n" +
                    "    gl_Position = vPosition;\n" +
                    "}";

    private static final String fragmentShaderCode =
            "#version 300 es\n" +
                    "precision mediump float;\n" +
                    "uniform vec4 uColor;\n" +
                    "out vec4 fragColor;\n" +
                    "void main() {\n" +
                    "    fragColor = uColor;\n" + //  "fragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                    "}";

    private int mColorHandle;

    private float[] triangleCoords;
    private int screenWidth;
    private int screenHeight;

    private float convertX(float xPixel) {
        return -1 + (2.0f / screenWidth) * xPixel;
    }

    private float convertY(float yPixel) {
        return 1 - (2.0f / screenHeight) * yPixel;
    }
    public Triangle(float sideLength, float xPixel, float yPixel, float scaleFactor, int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        float xCoord = convertX(xPixel);
        float yCoord = convertY(yPixel);
        float h = (float) (Math.sqrt(3) * sideLength / 2);

        triangleCoords = new float[]{
                xCoord, yCoord, 0.0f,
                xCoord - (sideLength / 2) * scaleFactor, yCoord - h * scaleFactor, 0.0f,
                xCoord + (sideLength / 2) * scaleFactor, yCoord - h * scaleFactor, 0.0f
        };

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES31.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES31.glCreateProgram();
        GLES31.glAttachShader(mProgram, vertexShader);
        GLES31.glAttachShader(mProgram, fragmentShader);
        GLES31.glLinkProgram(mProgram);

        // Check linking status
        final int[] linkStatus = new int[1];
        GLES31.glGetProgramiv(mProgram, GLES31.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            String error = GLES31.glGetProgramInfoLog(mProgram);
            GLES31.glDeleteProgram(mProgram);
            throw new RuntimeException("Program link failed with: " + error);
        }
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES31.glCreateShader(type);
        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        // Check compile status
        final int[] compileStatus = new int[1];
        GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            String error = GLES31.glGetShaderInfoLog(shader);
            GLES31.glDeleteShader(shader);
            throw new RuntimeException("Shader compilation failed with: " + error);
        }

        return shader;
    }

    public void draw(int argbColor) {
        GLES31.glUseProgram(mProgram);

        int positionHandle = GLES31.glGetAttribLocation(mProgram, "vPosition");
        GLES31.glEnableVertexAttribArray(positionHandle);
        GLES31.glVertexAttribPointer(positionHandle, 3, GLES31.GL_FLOAT, false, 0, vertexBuffer);

        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, 3);

        GLES31.glDisableVertexAttribArray(positionHandle);


        float alpha = ((argbColor >> 24) & 0xFF) / 255.0f;
        float red = ((argbColor >> 16) & 0xFF) / 255.0f;
        float green = ((argbColor >> 8) & 0xFF) / 255.0f;
        float blue = (argbColor & 0xFF) / 255.0f;
        mColorHandle = GLES31.glGetUniformLocation(mProgram, "uColor");
        GLES31.glUniform4f(mColorHandle, red, green, blue, alpha);

    }

}
