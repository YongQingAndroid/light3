package com.posun.lightui.recyclerview.lightdefult;

import android.support.v7.widget.RecyclerView;

/**
 * 处理recycleView的滚动条
 * package light3:com.posun.lightui.recyclerview.lightdefult.LightScrollBarHelper.class
 * 作者：zyq on 2018/2/5 16:41
 * 邮箱：zyq@posun.com
 */

public class LightScrollBarHelper {
    private double initScrollPosition = 0;//初始化第一屏时的位置
    protected final int ENLARGE = 100, MAX = 100;//ENLARGE放大倍数因为滚动需要保持像素精度越大越好，MAX滚动的最大范围

    public double getInitScrollPosition() {
        return initScrollPosition;
    }

    public void setInitScrollPosition(double initScrollPosition) {
        this.initScrollPosition = initScrollPosition;
    }

    /**
     * 確定滾動的位置
     * @param position 当前的position（底部）
     * @param state
     * @param y 当前底部view的纵坐标
     * @param positionHeight view的高度
     * @return
     */
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
