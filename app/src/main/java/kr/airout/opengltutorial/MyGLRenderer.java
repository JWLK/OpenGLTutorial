package kr.airout.opengltutorial;

import android.graphics.Color;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Random random = new Random();

    public static int GLViewWidth = 32;
    public static int GLViewHeight = 1024;
    private Triangle mTriangle;
    private Square mSquare;
    private SquareMatrix mSquareMatrix;
    int matrixRow = 1024;
    int matrixCol = 33;


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // 배경색 설정

        Log.d("GLViewWidth", GLViewWidth+"");
        Log.d("GLViewHeight", GLViewHeight+"");
        mTriangle = new Triangle(1.0f, 16, 500, 0.1f, GLViewWidth, GLViewHeight);

        // 초기 색상을 흰색으로 설정합니다.
        int initialColor = 0xFFFFFFFF; // ARGB format: White color
        mSquare = new Square(1, 0, 20, 1,1, GLViewWidth, GLViewHeight, initialColor);

        // 초기 색상 배열을 생성합니다.
        int[][] initialColors = new int[matrixRow][matrixCol];
        for (int i = 0; i < matrixRow; i++) {
            for (int j = 0; j < matrixCol; j++) {
                // 무작위 색상을 생성합니다.
                initialColors[i][j] = Color.argb(
                        255, // Alpha
                        random.nextInt(256), // Red
                        random.nextInt(256), // Green
                        random.nextInt(256)  // Blue
                );
            }
        }
        // 3x3 사각형 행렬 생성
        mSquareMatrix = new SquareMatrix(matrixRow, matrixCol, GLViewWidth, GLViewHeight, initialColors);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        int nextValue = 255;
        int nextArgbValue = 0xFF << 24 | nextValue << 16 | nextValue << 8 | nextValue;
        mTriangle.draw(nextArgbValue);

        // mSquare의 위치를 무작위로 변경
        float randomX = random.nextFloat() * GLViewWidth;
        float randomY = random.nextFloat() * GLViewHeight;
        int randomColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        mSquare.updateVertices(randomX, randomY, 1);  // 위치 업데이트
        mSquare.updateColor(randomColor);  // 색상 업데이트
        mSquare.draw();  // 그리기

        int[][] colors = new int[matrixRow][matrixCol];
        for (int i = 0; i < matrixRow; i++) {
            for (int j = 0; j < matrixCol; j++) {
                colors[i][j] = Color.argb(
                        255, // Alpha
                        random.nextInt(256), // Red
                        random.nextInt(256), // Green
                        random.nextInt(256)  // Blue
                );
            }
        }
        mSquareMatrix.updateColors(colors); // 색상 업데이트
        mSquareMatrix.draw();
    }


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
    }


}
