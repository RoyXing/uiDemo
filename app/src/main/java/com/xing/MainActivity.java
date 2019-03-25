package com.xing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xing.xfermode滤镜.XfermodeEaserView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new XfermodeEaserView(this));
    }
}
