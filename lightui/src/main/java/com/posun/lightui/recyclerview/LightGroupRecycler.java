package com.posun.lightui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightGroupRecycler {
    private List<View> groupCatch = new ArrayList<>();
    private List<View> ViewPosition = new ArrayList<>();
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
            recyclerView = new WeakReference<>((RecyclerView) field.get(layoutManager));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //    public void addView(View view) {
//        view.setLayoutParams(new LightRecyLayoutParams(new GroupHolder(view)));
//        lightChildHelper.addView(view, true);
//    }
    public void addChildView(View view, int position) {
        lightChildHelper.addView(view, position, true);
    }

    public void addView(View view, int position) {
        ((LightRecyLayoutParams) view.getLayoutParams()).setPosition(position);
        ViewPosition.add(view);
        lightChildHelper.addView(view, true);
    }

    public void addView(View view, int viewPosition, int position) {
        ((LightRecyLayoutParams) view.getLayoutParams()).setPosition(position);
        ViewPosition.add(viewPosition,view);
        lightChildHelper.addView(view, viewPosition, true);
    }

    /***
     * 帮试图插入lightChildHelper的队尾
     * @param view
     * @param viewPosition
     * @param position
     */
    public void addViewInEnd(View view, int viewPosition, int position) {
        ((LightRecyLayoutParams) view.getLayoutParams()).setPosition(position);
        ViewPosition.add(view);
        lightChildHelper.addView(view, viewPosition, true);
    }

    public int getChildOffset(int position) {
        return lightChildHelper.getChildOffset(position);
    }

    public int getPositionFromGroupView(View view) {
        return ((LightRecyLayoutParams) view.getLayoutParams()).getPosition();
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
        for (View view : ViewPosition) {
            view.offsetTopAndBottom(dy);
        }
    }

    List<View> recyviews = new ArrayList<>();

    public void recyclerOutScreenView(boolean up, int totalHeight) {
        recyviews.clear();
        if (up) {
            recyclerBefor();
        } else {
            RecyclerAfter(totalHeight);
        }
        for (View view : recyviews) {
            removeAndRecycleView(view);
        }
    }

    private void RecyclerAfter(int totalHeight) {
        int count = ViewPosition.size();
        int i = count - 1;
        while (i >= 0) {
            View view = ViewPosition.get(i);
            if (view.getY() <= (totalHeight)) {//跌代一直到遇到不能回收的停止
                return;
            }
            recyviews.add(view);
            i--;
        }
    }

    private void recyclerBefor() {
        for (View view : ViewPosition) {
            if ((view.getY() + view.getMeasuredHeight()) >= 0) {//跌代一直到遇到不能回收的停止
                return;
            }
            recyviews.add(view);
        }
    }

//    public View getFistView() {
//        return recyclerView.get().getChildAt(0);
//    }
//
    public View getLastView() {
        return recyclerView.get().getChildAt(recyclerView.get().getChildCount() - 1);
    }

//    public View getChildAt(int position) {
//        return recyclerView.get().getChildAt(position);
//    }

    public boolean isGroupView(View view) {
        return view.getLayoutParams() instanceof LightRecyLayoutParams;
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
    Method attachViewToParent=null;
    public void attachViewToParent(View child, int index, ViewGroup.LayoutParams params) {
        try {
            if(attachViewToParent==null){
                attachViewToParent=  RecyclerView.class.getDeclaredMethod("attachViewToParent",View.class,int.class, ViewGroup.LayoutParams.class);
                attachViewToParent.setAccessible(true);
            }
            attachViewToParent.invoke(recyclerView.get(),child,index,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    Method removeView=null;
//    public void removeView(View child) {
//        try {
//            if(removeView==null){
//                removeView=  RecyclerView.class.getDeclaredMethod("removeView",View.class);
//                removeView.setAccessible(true);
//            }
//            attachViewToParent.invoke(recyclerView.get(),child);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
}