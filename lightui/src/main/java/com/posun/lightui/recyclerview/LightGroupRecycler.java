package com.posun.lightui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LightGroupRecycler {
    private List<View> groupCatch = new ArrayList<>();
    private Map<View, Integer> ViewPosition = new HashMap<>();
    private Map<Integer, Integer> groupUpperSpan = new HashMap<>();
    private WeakReference<RecyclerView> recyclerView;
    private LightChildHelper lightChildHelper;

    public void removeAndRecycleView(View view) {
        lightChildHelper.removeView(view);
        ViewPosition.remove(view);
        if (groupCatch.size() < 10) {
            groupCatch.add(view);
        }
    }

    public void addGroupUpperSpan(int position, int span) {
        groupUpperSpan.put(position, span);
    }

    public int getGroupUpperSpan(int position, int totalSpan) {
        if (groupUpperSpan.containsKey(position))
            return totalSpan - groupUpperSpan.get(position);
        return 0;
    }

    public RecyclerView getRecyclerView() {
        if (recyclerView != null && recyclerView.get() != null)
            return recyclerView.get();
        return null;
    }

    public LightGroupRecycler(RecyclerView.LayoutManager layoutManager) {
        initRecyclerView(layoutManager);
        lightChildHelper = new LightChildHelper(layoutManager);
    }

    private void initRecyclerView(RecyclerView.LayoutManager layoutManager) {
        try {
            Field field = RecyclerView.LayoutManager.class.getDeclaredField("mRecyclerView");
            field.setAccessible(true);
            recyclerView = new WeakReference<RecyclerView>((RecyclerView) field.get(layoutManager));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void addView(View view) {
        view.setLayoutParams(new LightRecyLayoutParams(new GroupHolder(view)));
        lightChildHelper.addView(view, true);
    }

    public void addView(View view, int position) {
        ViewPosition.put(view, position);
        lightChildHelper.addView(view, true);
    }

    public View getGroupViewForPosition(int position) {
        GroupHolder viewHolder = null;
        if (groupCatch.size() > 0) {
            viewHolder = getGroupHolder(groupCatch.get(0));
            groupCatch.remove(0);
        } else {
            viewHolder = getGroupAdapter().creatGroupHolder(recyclerView.get(), 0);
            viewHolder.itemView.setLayoutParams(new LightRecyLayoutParams(viewHolder));
        }
        getGroupAdapter().onBindGroupHolder(viewHolder, position);
        return viewHolder.itemView;
    }

    public GroupHolder getGroupHolder(View view) {
        return ((LightRecyLayoutParams) view.getLayoutParams()).getGroupViewHolderInt();
    }

    private WeakReference<LightGroupAdapter> groupAdapter;

    public LightGroupAdapter getGroupAdapter() {
        if (groupAdapter != null && groupAdapter.get() != null)
            return groupAdapter.get();
        if (recyclerView != null && recyclerView.get() != null) {
            RecyclerView.Adapter adapter = recyclerView.get().getAdapter();
            if (adapter instanceof LightGroupAdapter) {
                return (LightGroupAdapter) adapter;
            }
        }
        return null;
    }

    public void offsetChildrenVertical(int dy) {
        Iterator iter = ViewPosition.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            View view = (View) entry.getKey();
            view.offsetTopAndBottom(dy);
        }
    }

    List<View> recyviews = new ArrayList<>();

    public void recyclerOutScreenView() {
        recyviews.clear();
        recyclerBefor();
        for (View view : recyviews) {
            removeAndRecycleView(view);
        }
    }

    private void recyclerBefor() {
        Iterator iter = ViewPosition.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            View view = (View) entry.getKey();
            if ((view.getY() + view.getMeasuredHeight()) >= 0) {//跌代一直到遇到不能回收的停止
                return;
            }
            recyviews.add(view);
        }
    }

    public interface LightGroupAdapter<T extends GroupHolder> {
        boolean isGroup(int position);

        T creatGroupHolder(ViewGroup viewGroup, int type);

        void onBindGroupHolder(T holder, int position);

    }

    public static class GroupHolder extends RecyclerView.ViewHolder {
        public GroupHolder(View itemView) {
            super(itemView);
        }
    }

}