package com.xing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xing.canvas.split.SplitView;
import com.xing.xfermode滤镜.XfermodeEaserView;
import com.xing.贝塞尔曲线.DragBubbleView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
