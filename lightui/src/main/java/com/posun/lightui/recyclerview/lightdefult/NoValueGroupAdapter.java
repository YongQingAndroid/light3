package com.posun.lightui.recyclerview.lightdefult;

import android.view.ViewGroup;

/**
 * package light3:com.posun.lightui.recyclerview.lightdefult.NoValueGroupAdapter.class
 * 作者：zyq on 2018/2/9 14:21
 * 邮箱：zyq@posun.com
 */

public class NoValueGroupAdapter implements LightGroupRecycler.LightGroupAdapter {
    @Override
    public boolean isGroup(int position) {
        return false;
    }

    @Override
    public LightGroupRecycler.GroupHolder creatGroupHolder(ViewGroup viewGroup, int type) {
        return null;
    }

    @Override
    public void onBindGroupHolder(LightGroupRecycler.GroupHolder holder, int position) {

    }
}
