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
    }
}

