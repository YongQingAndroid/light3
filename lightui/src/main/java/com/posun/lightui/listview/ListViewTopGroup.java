package com.posun.lightui.listview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * package light3:com.posun.lightui.listview.ListViewTopGroup.class
 * 作者：zyq on 2018/1/23 10:55
 * 邮箱：zyq@posun.com
 */

public class ListViewTopGroup extends LinearLayout {
    float mSectionPinOffset = 0f;

    public ListViewTopGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewTopGroup(Context context) {
        super(context);
    }

    public void invalidate(float mSectionPinOffset) {
        this.mSectionPinOffset = mSectionPinOffset;
        super.invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        View mViewSectionPin = getChildAt(0);
        if (mViewSectionPin != null) {
            canvas.translate(0, mSectionPinOffset);
            canvas.clipRect(0, 0, getWidth(), mViewSectionPin.getMeasuredHeight()); // needed
            mViewSectionPin.draw(canvas);
        }
        super.dispatchDraw(canvas);
    }
}
