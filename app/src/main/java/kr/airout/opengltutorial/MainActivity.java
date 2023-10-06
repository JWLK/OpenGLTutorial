package kr.airout.opengltutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private MyGLSurfaceView mGLView;
    private LinearLayout openGLView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openGLView = findViewById(R.id.openGLView);

        mGLView = new MyGLSurfaceView(this);
        openGLView.addView(mGLView);


    }
}