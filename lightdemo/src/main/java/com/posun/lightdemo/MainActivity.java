package com.posun.lightdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.posun.lightdemo.demos.DateTimePickerActivity;
import com.posun.lightdemo.demos.DemoDialog;
import com.posun.lightdemo.demos.SqliteOrmDemo;
import com.posun.lightdemo.demos.ToolsActivity;
import com.posun.lightui.citypicker.LightDialog;
import com.posun.lightui.citypicker.MeterialCityDialog;
import com.posun.lightui.timePicker.MaterialTimePickerLayout;
import com.posun.lightui.timePicker.calender.LightCalenderView;

import org.joda.time.DateTime;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout main_layout;
    private String[] menus = new String[]{"DateTimePicker", "cityPicker", "OrmSqlLite", "OrmSqlLite(加密)", "lightHttp", "LightUI", "常用工具", "calendarUi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @SuppressLint("WrongViewCast")
    private void initUI() {
        main_layout = findViewById(R.id.main_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (String item : menus) {
            Button button = new Button(this);
            button.setText(item);
            button.setOnClickListener(this);
            main_layout.addView(button);
        }
    }

    @Override
    public void onClick(View view) {
        lightExecute = null;
        String msg = ((TextView) view).getText().toString();
        switch (msg) {
            case "DateTimePicker":
                dateTimePicker();
                break;
            case "cityPicker":
                cityPicker();
                break;
            case "lightHttp":
                lightHttp();
                break;
            case "OrmSqlLite":
                lightORm();
                break;
            case "OrmSqlLite(加密)":
                lightORm();
                break;
            case "calendarUi":
                calendarUi();
                break;
            case "常用工具":
                tools();
                break;

        }
        if (lightExecute == null) {
            Toast.makeText(this, "暂时还没有demo", Toast.LENGTH_SHORT).show();
            return;
        }
        new DemoDialog(this).show();
    }

    private void tools() {
        lightExecute = new DateTimePickerActivity.LightExecute() {
            @Override
            public void execute(Activity activity) {
                activity.startActivity(new Intent(activity, ToolsActivity.class));
            }

            @Override
            public String getMsg() {
                return "android开发常用小工具";
            }
        };
    }

    public static DateTimePickerActivity.LightExecute lightExecute = null;

    private void dateTimePicker() {
        lightExecute = new DateTimePickerActivity.LightExecute() {
            @Override
            public void execute(Activity activity) {
                MaterialTimePickerLayout m = new MaterialTimePickerLayout(activity);
                LightDialog.MakeDialog(m.getView(), LightDialog.QGriavty.BOTTOM).show();
            }

            @Override
            public String getMsg() {
                return getString(R.string.timepicker);
            }
        };
    }

    private void calendarUi() {
        lightExecute = new DateTimePickerActivity.LightExecute() {
            @Override
            public void execute(final Activity activity) {
                LightCalenderView mLightCalenderView = new LightCalenderView(activity);
                mLightCalenderView.setListener(new LightCalenderView.LightListener() {
                    @Override
                    public void DateChange(DateTime dateTime) {
                        Toast.makeText(activity, dateTime.toString("yyyy-MM-dd"), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void select(DateTime dateTime) {
                        Toast.makeText(activity, dateTime.toString("yyyy-MM-dd"), Toast.LENGTH_SHORT).show();
                    }
                });
                mLightCalenderView.setBackgroundColor(Color.WHITE);
                LightDialog.MakeDialog(mLightCalenderView).show();
            }

            @Override
            public String getMsg() {
                return getString(R.string.calender);
            }
        };
    }

    private void lightORm() {
        lightExecute = new DateTimePickerActivity.LightExecute() {
            @Override
            public void execute(Activity activity) {
                SqliteOrmDemo sqliteOrmDemo = new SqliteOrmDemo(activity);
                sqliteOrmDemo.show();
            }

            @Override
            public String getMsg() {
                return getString(R.string.lightOrm);
            }
        };
    }

    private void lightHttp() {
        lightExecute = new DateTimePickerActivity.LightExecute() {
            @Override
            public void execute(Activity activity) {
                Toast.makeText(activity, "没有示例", Toast.LENGTH_SHORT).show();
            }

            @Override
            public String getMsg() {
                return getString(R.string.lightHttp);
            }
        };
    }

    private void cityPicker() {
        lightExecute = new DateTimePickerActivity.LightExecute() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void execute(Activity activity) {
                new MeterialCityDialog(activity).show();
            }

            @Override
            public String getMsg() {
                return getString(R.string.citypicker);
            }
        };
    }
}
