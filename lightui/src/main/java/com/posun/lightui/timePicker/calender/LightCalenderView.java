package com.posun.lightui.timePicker.calender;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.joda.time.DateTime;

/**
 * package Kotlin3:com.posun.lightui.timePicker.calender.LightCalenderView.class
 * 作者：zyq on 2018/1/8 14:22
 * 邮箱：zyq@posun.com
 */

public class LightCalenderView extends LinearLayout {
    private ViewPager viewPager;
    private CalenderAdapter adapter;
    private boolean ismonth = true;

    public LightCalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUi();
    }

    public boolean isIsmonth() {
        return ismonth;
    }

    public void setIsmonth(boolean ismonth) {
        this.ismonth = ismonth;
        adapter.setIsmonth(ismonth);
    }

    public void setListener(LightListener listener) {
        if (adapter != null)
            adapter.setListener(listener);
    }

    private void initUi() {
        setOrientation(VERTICAL);
        CalenderTitleView mCalenderTitleView = new CalenderTitleView(getContext());
        addView(mCalenderTitleView, new LayoutParams(LayoutParams.MATCH_PARENT, 70));
        viewPager = new ViewPager(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(viewPager, layoutParams);
        adapter = new CalenderAdapter(viewPager, ismonth);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2, false);
    }

    public interface LightListener {
        void DateChange(DateTime dateTime);

        void select(DateTime dateTime);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(heightMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = getChildAt(0).getMeasuredHeight() + ((ViewGroup) getChildAt(1)).getChildAt(0).getMeasuredHeight();
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }
}
