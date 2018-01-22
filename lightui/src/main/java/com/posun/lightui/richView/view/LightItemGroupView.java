package com.posun.lightui.richView.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.posun.lightui.richView.LightRichActivityManager;

/**
 * package Kotlin3:com.posun.lightui.richView.view.LightItemGroupView.class
 * 作者：zyq on 2018/1/17 11:32
 * 邮箱：zyq@posun.com
 */

public class LightItemGroupView implements LightRichActivityManager.LightItemGroupInterface {
    private int start, end;
    private TextView title;
    private Context context;
    private LinearLayout linearLayout;

    public LightItemGroupView(int start, int end, String lab, Context context) {
        this.start = start;
        this.end = end;
        this.context = context;
        initUi(lab);
    }

    private void initUi(String lab) {
        title = new TextView(context);
        title.setText(lab);
        title.setPadding(0, 10, 0, 10);
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public View getTitleView() {
        return title;
    }

    @Override
    public ViewGroup getViewGroup() {
        return linearLayout;
    }

    @Override
    public int getEndOrder() {
        return end;
    }
}
