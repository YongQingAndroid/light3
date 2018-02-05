package com.posun.lightui.recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * package light3:com.posun.lightui.recyclerview.LightScrollBarHelper.class
 * 作者：zyq on 2018/2/5 16:41
 * 邮箱：zyq@posun.com
 */

public class LightScrollBarHelper {
    private double initScrollPosition = 0;
    protected final int ENLARGE = 100, MAX = 100;

    public double getInitScrollPosition() {
        return initScrollPosition;
    }

    public void setInitScrollPosition(double initScrollPosition) {
        this.initScrollPosition = initScrollPosition;
    }

    public int verticalScroll(int position, RecyclerView.State state, int y, int positionHeight) {
        double count = state.getItemCount();
        double cha = 0;
        cha = (y / (double) positionHeight);
        double itemOff = (MAX * ENLARGE) / (double) count;
        cha = itemOff * cha;
        double offset = (MAX * (position / count)) - getInitScrollPosition();
        if (offset < 0)
            return 0;
        return (int) ((offset * ENLARGE) + cha);
    }
//    staticsetVerticalScrollBarEnabled
}
