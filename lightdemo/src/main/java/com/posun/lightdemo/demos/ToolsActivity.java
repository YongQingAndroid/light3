package com.posun.lightdemo.demos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.posun.lightdemo.R;

public class ToolsActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout main_layout;
    private String[] menus = new String[]{"startActivityForResult"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
    }

    private void initUi() {
        main_layout = findViewById(R.id.main_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (String item : menus) {
            Button button = new Button(this);
            button.setText(item);
            button.setOnClickListener(this);
            main_layout.addView(button);
        }
    }

    public static DateTimePickerActivity.LightExecute lightExecute = null;

    @Override
    public void onClick(View view) {
        lightExecute = null;
        String msg = ((TextView) view).getText().toString();
        switch (msg) {
            case "startActivityForResult":
                startActivityForResultCall();
                break;
        }
        if (lightExecute == null) {
            Toast.makeText(this, "暂时还没有demo", Toast.LENGTH_SHORT).show();
            return;
        }
        new DemoDialog(this, lightExecute).show();
    }

    @Override
    protected void onDestroy() {
        lightExecute = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startActivityForResultCall() {
        lightExecute = new DateTimePickerActivity.LightExecute() {
            @Override
            public void execute(Activity activity) {
                Toast.makeText(activity, "没有示例", Toast.LENGTH_SHORT).show();
            }

            @Override
            public String getMsg() {
                return getString(R.string.lightActivity);
            }
        };
    }
}
