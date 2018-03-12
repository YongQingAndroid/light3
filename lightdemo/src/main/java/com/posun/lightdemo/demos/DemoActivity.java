package com.posun.lightdemo.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.posun.lightdemo.MainActivity;
import com.posun.lightdemo.R;

import thereisnospon.codeview.CodeView;
import thereisnospon.codeview.CodeViewTheme;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);
        initUi();
    }

    CodeView codeView = null;

    private void initUi() {
        findViewById(R.id.execut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.lightExecute.execute(DemoActivity.this);
            }
        });
        codeView = findViewById(R.id.codeview);
        codeView.setVisibility(View.VISIBLE);
        codeView.setTheme(CodeViewTheme.ATELIER_CAVE_LIGHT).fillColor();
        new android.os.Handler().post(new Runnable() {
            @Override
            public void run() {
                codeView.showCode(MainActivity.lightExecute.getMsg());
            }
        });
    }
}
