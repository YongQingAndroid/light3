package com.posun.lightui.recyclerview.suspension;

import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * package light3:com.posun.lightui.recyclerview.suspension.SuspensionLayoutParams.class
 * 作者：zyq on 2018/2/8 15:37
 * 邮箱：zyq@posun.com
 */

public class SuspensionLayoutParams extends FrameLayout.LayoutParams{
    private ViewGroup.LayoutParams OtherLayoutParams;
    public SuspensionLayoutParams(int width, int height) {
        super(width, height);
    }

    public ViewGroup.LayoutParams getOtherLayoutParams() {
        return OtherLayoutParams;
    }

    public SuspensionLayoutParams setOtherLayoutParams(ViewGroup.LayoutParams otherLayoutParams) {
        OtherLayoutParams = otherLayoutParams;
        return this;
    }
}
