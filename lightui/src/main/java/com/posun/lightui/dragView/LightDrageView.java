package com.posun.lightui.dragView;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * package Kotlin3:com.posun.lightui.dragView.LightDrageView.class
 * 作者：zyq on 2018/1/4 11:27
 * 邮箱：zyq@posun.com
 */

public class LightDrageView extends FrameLayout {
    private LinearLayout linearLayout,innerlinearLayout;
    private View contentView;
    private boolean load = true;

    public LightDrageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public LightDrageView(@NonNull Context context) {
        super(context);
        initUI();
    }

    private void initUI() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        linearLayout=new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        innerlinearLayout = new LinearLayout(getContext());
        innerlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerlinearLayout.setBackgroundColor(Color.RED);
        innerlinearLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        addView(linearLayout, layoutParams);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        contentView = child;
//        contentView.setLayoutParams(params);
        super.addView(child, index, params);
    }

    public void addItems(View... view) {
        if (view == null)
            return;
        for (View item : view) {
            linearLayout.addView(item);
        }
    }

    public void submit() {
        if (!load && contentView != null) {
            addView(contentView);
        }
    }

    public static class ItemBuilder {
        private int bg_color = Color.BLUE, text_color = Color.WHITE;
        private List<String> labels = new ArrayList<>();

        public ItemBuilder addLableString(String... arg) {
            if (arg == null)
                return this;
            labels.addAll(Arrays.asList(arg));
            return this;
        }

        public int getBg_color() {
            return bg_color;
        }

        public ItemBuilder setBg_color(int bg_color) {
            this.bg_color = bg_color;
            return this;
        }

        public int getText_color() {
            return text_color;
        }

        public ItemBuilder setText_color(int text_color) {
            this.text_color = text_color;
            return this;
        }

        public View[] build(Context context) {
            List<View> views = new ArrayList<>();
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            for (String string : labels) {
                TextView textView = new TextView(context);
                textView.setBackgroundColor(bg_color);
                textView.setTextColor(text_color);
                textView.setText(string);
                textView.setLayoutParams(layoutParams);
                views.add(textView);
            }
            return views.toArray(new View[]{});
        }

        public View buildgetItem(Context context, int index) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            TextView textView = new TextView(context);
            textView.setBackgroundColor(bg_color);
            textView.setTextColor(text_color);
            textView.setText(labels.get(index));
            textView.setLayoutParams(layoutParams);
            return textView;
        }
    }
}
