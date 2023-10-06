package kr.airout.opengltutorial;

import android.opengl.GLES31;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Square {

    private FloatBuffer vertexBuffer;
    private final int mProgram;
    private final int mVBO[] = new int[1];  // Vertex Buffer Object (VBO) 배열

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
                    "    fragColor = uColor;\n" +
                    "}";

    private int mColorHandle;
    private float[] squareCoords;

    private int screenWidth;
    private int screenHeight;

    private float convertX(float xPixel) {
        return -1 + (2.0f / screenWidth) * xPixel;
    }

    private float convertY(float yPixel) {
        return 1 - (2.0f / screenHeight) * yPixel;
    }

    public Square(float sideLength, float xPixel, float yPixel, float scaleFactorX, float scaleFactorY, int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        float xCoord = convertX(xPixel);
        float yCoord = convertY(yPixel);
//        float halfWidthScaled = (sideLength / 2) * scaleFactorX;
//        float halfHeightScaled = (sideLength / 2) * scaleFactorY;

        float halfWidthScaled = sideLength / screenWidth;
        float halfHeightScaled = sideLength / screenHeight;

        squareCoords = new float[]{
                xCoord - halfWidthScaled, yCoord + halfHeightScaled, 0.0f,
                xCoord - halfWidthScaled, yCoord - halfHeightScaled, 0.0f,
                xCoord + halfWidthScaled, yCoord + halfHeightScaled, 0.0f,
                xCoord + halfWidthScaled, yCoord - halfHeightScaled, 0.0f
        };

        // FloatBuffer 초기화
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(squareCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // VBO 생성 및 데이터 설정
        GLES31.glGenBuffers(1, mVBO, 0);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, mVBO[0]);
        GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES31.GL_STATIC_DRAW);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);

        int vertexShader = loadShader(GLES31.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES31.glCreateProgram();
        GLES31.glAttachShader(mProgram, vertexShader);
        GLES31.glAttachShader(mProgram, fragmentShader);
        GLES31.glLinkProgram(mProgram);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES31.glCreateShader(type);
        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        // 컴파일 상태 확인
        final int[] compileStatus = new int[1];
        GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            GLES31.glDeleteShader(shader);
            throw new RuntimeException("Shader compilation failed.");
        }

        return shader;
    }

    public void draw(int argbColor) {
        GLES31.glUseProgram(mProgram);

        // VBO를 사용하여 그리기
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, mVBO[0]);
        int positionHandle = GLES31.glGetAttribLocation(mProgram, "vPosition");
        GLES31.glEnableVertexAttribArray(positionHandle);
        GLES31.glVertexAttribPointer(positionHandle, 3, GLES31.GL_FLOAT, false, 0, 0);

        float alpha = ((argbColor >> 24) & 0xFF) / 255.0f;
        float red = ((argbColor >> 16) & 0xFF) / 255.0f;
        float green = ((argbColor >> 8) & 0xFF) / 255.0f;
        float blue = (argbColor & 0xFF) / 255.0f;

        mColorHandle = GLES31.glGetUniformLocation(mProgram, "uColor");
        GLES31.glUniform4f(mColorHandle, red, green, blue, alpha);

        GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4);

        GLES31.glDisableVertexAttribArray(positionHandle);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);  // VBO 언바인딩
    }
}
