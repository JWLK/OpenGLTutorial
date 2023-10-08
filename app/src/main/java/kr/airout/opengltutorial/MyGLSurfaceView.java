package kr.airout.opengltutorial;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // OpenGL ES 3.0 context를 설정합니다.
        setEGLContextClientVersion(3);

        renderer = new MyGLRenderer();

        // Renderer를 이 GLSurfaceView에 설정합니다.
        setRenderer(renderer);

//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // 화면을 그릴 내용이 변경되었을 때만 화면을 다시 그리도록 설정합니다.
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY); // 가능한 한 빠르게 지속적으로 다시 그림
    }
}

