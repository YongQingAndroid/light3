package com.posun.lightui.dragView;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.posun.lightui.QlightUnit;

/**
 * Created by zyq on 2017/2/20.
 * 下拉刷新UI实现
 */
public class PraseDragView implements DragContentView {
    private LinearLayout DragLayout;
    private TextView textView;
    private CircleImageView mCircleView;
    private MaterialProgressDrawable mProgress;
    private final String nomer = "正在下拉...";
    private Context context;
    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
    @Override
    public void init(Context context) {
        this.context=context;
        DragLayout = new LinearLayout(context);
        DragLayout.setOrientation(LinearLayout.HORIZONTAL);
        DragLayout.setGravity(Gravity.CENTER);
        DragLayout.setBackgroundColor(Color.GRAY);
        textView = new TextView(context);
        textView.setPadding(10, 0, 0, 0);
        textView.setTextColor(Color.WHITE);
        textView.setText(nomer);
        createProgressView();
    }
    @Override
    public View getView() {
        return DragLayout;
    }

    @Override
    public void drage(float move) {
        mProgress.setRotation(move * 2);
    }
    @Override
    public void stateChange(Constant.DragState state) {
         switch (state){
            case  NORMAL:
                textView.setText(nomer);
                break;
             case DRAGE:
                 textView.setText(nomer);
                 break;
             case DRAGE_DOWN:
                 textView.setText("释放刷新...");
                 break;
             case LOADING:
             case LOADINGCONTINUE:
                 textView.setText("正在刷新...");
                 try {
                     mProgress.start();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 break;
             case FINISH:
                 if (mProgress != null) {
                     mProgress.stop();
                     mProgress.setStartEndTrim(0f, 0.8f);
                     mProgress.setArrowScale(1f);
                     mProgress.setProgressRotation(1);
                     mProgress.showArrow(true);
                 }
                 break;
         }
    }
    private void createProgressView() {
        mCircleView = new CircleImageView(context, CIRCLE_BG_LIGHT);
        mProgress = new MaterialProgressDrawable(context, mCircleView);
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        mProgress.setColorSchemeColors(Color.RED, Color.BLACK);
        mProgress.setAlpha(255);
        mProgress.setStartEndTrim(0f, 0.8f);
        mProgress.setArrowScale(1f);
        mProgress.setProgressRotation(0.9f);
        mProgress.showArrow(true);
        mCircleView.setImageDrawable(mProgress);
        int hight = QlightUnit.dip2px(context, 25f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(hight, hight);
        mCircleView.setLayoutParams(layoutParams);
        DragLayout.addView(mCircleView);
        DragLayout.addView(textView);
    }
}
