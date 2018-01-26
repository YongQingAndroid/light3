package com.posun.lightui.recyclerview;

import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * package light3:com.posun.lightui.recyclerview.LightTwoDimensionalAdapter.class
 * 作者：zyq on 2018/1/26 11:33
 * 邮箱：zyq@posun.com
 */
@Deprecated
public abstract class LightTwoDimensionalAdapter<G extends LightFormAdapterManager.ChildHolder, C extends LightFormAdapterManager.ChildHolder> extends LightRecyclerGroupAdapter<G, C> {
    @Override
    public C onCreateContentViewHolder(int position, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public G onCreateGroupViewHolder(int position, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindContentViewHolder(C childHolder, int position) {

    }

    @Override
    public void onBindGroupViewHolder(G childHolder, int position) {

    }

    @Override
    public boolean hasGroup(int position) {
        return false;
    }

    private Map<Integer, Integer> groupIndex = new HashMap<>();

    @Override
    public int getItemCount() {
        int size = 0;
        int i = 0;
        int groupSize = getGroupCount();
        while (i < groupSize) {
            size += getChildCount(i);
            i++;
        }
        return size;
    }

    public abstract int getGroupCount();

    public abstract int getChildCount(int groupPosition);
}
