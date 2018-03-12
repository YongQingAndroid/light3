package com.posun.lightdemo.demos;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.posun.lightdemo.MainActivity;
import com.posun.lightdemo.R;
import com.posun.lightui.citypicker.LightDialog;

import java.lang.ref.WeakReference;

import thereisnospon.codeview.Code;
import thereisnospon.codeview.CodeView;
import thereisnospon.codeview.CodeViewTheme;

public class DemoDialog  {
    View rootview;
    LightDialog mLightDialog;
    private WeakReference<Activity>weakReference;
    private DateTimePickerActivity.LightExecute lightExecute;
    public DemoDialog(Activity activity){
        this(activity,MainActivity.lightExecute);
    }
    public DemoDialog(Activity activity,DateTimePickerActivity.LightExecute lightExecute){
        this.lightExecute=lightExecute;
        weakReference=new WeakReference<Activity>(activity);
        initUi();
    }
    CodeView codeView = null;
    private void initUi() {
        rootview= LayoutInflater.from(weakReference.get()).inflate(R.layout.activity_date_time_picker,null);
        findViewById(R.id.execut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLightDialog.cancel();
                lightExecute.execute(weakReference.get());
            }
        });
        codeView = findViewById(R.id.codeview);
        codeView.setVisibility(View.VISIBLE);
        codeView.setTheme(CodeViewTheme.ATELIER_CAVE_LIGHT).fillColor();
        new android.os.Handler().post(new Runnable() {
            @Override
            public void run() {
                codeView.showCode(lightExecute.getMsg());
            }
        });
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "没有说明", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private <T extends View>T findViewById(@IdRes int id){
        return (T)rootview.findViewById(id);
    }
    public void show(){
        mLightDialog= LightDialog.MakeDialog(rootview);
//        mLightDialog.setFullSrceen(true);
        mLightDialog.setHasPadding(false);
        mLightDialog.show();
    }
}
