package kr.airout.opengltutorial;

import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    public static int GLViewWidth = 32;
    public static int GLViewHeight = 1024;
    private Triangle mTriangle;
    private Square mSquare;
    
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // 배경색 설정

        Log.d("GLViewWidth", GLViewWidth+"");
        Log.d("GLViewHeight", GLViewHeight+"");
        mTriangle = new Triangle(1.0f, 16, 500, 0.1f, GLViewWidth, GLViewHeight);
        mSquare = new Square(1, 16, 50, 1,1, GLViewWidth, GLViewHeight);
        // 3x3 사각형 행렬 생성
//        mSquareMatrix = new SquareMatrix(3, 500, 1.0f, 2, 1, 1, 1, GLViewWidth, GLViewHeight);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        int nextValue = 255;
        int nextArgbValue = 0xFF << 24 | nextValue << 16 | nextValue << 8 | nextValue;
        mTriangle.draw(nextArgbValue);
        mSquare.draw(nextArgbValue);

        // 3x3 사각형 행렬 그리기
        int[][] colors = new int[3][1000];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 1000; j++) {
                colors[i][j] = nextArgbValue;
            }
        }
//        mSquareMatrix.draw(colors);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
    }
}
