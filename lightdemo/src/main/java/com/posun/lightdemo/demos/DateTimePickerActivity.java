package com.posun.lightdemo.demos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.posun.lightdemo.MainActivity;
import com.posun.lightdemo.R;

public class DateTimePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);
        initUi();
    }


    private void initUi() {
        findViewById(R.id.execut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.lightExecute.execute(DateTimePickerActivity.this);
            }
        });
        TextView textView = findViewById(R.id.msg);
        textView.setVisibility(View.VISIBLE);
        textView.setText(MainActivity.lightExecute.getMsg());
    }

    public interface LightExecute {
        void execute(Activity activity);

        String getMsg();
    }
}
