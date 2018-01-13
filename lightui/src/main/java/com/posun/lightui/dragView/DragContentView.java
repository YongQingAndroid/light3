package com.posun.lightui.dragView;

import android.content.Context;
import android.view.View;

/**
 * Created by zyq on 2017/2/20.
 * 自定义下拉刷新布局
 */
public interface DragContentView {
    void init(Context context);
    View getView();
    void drage(float move);
    void stateChange(Constant.DragState state);
}
