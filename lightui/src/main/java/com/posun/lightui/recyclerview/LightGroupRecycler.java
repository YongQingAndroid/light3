package com.posun.lightui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightGroupRecycler {
    private List<View> groupCatch = new ArrayList<>();
    private List<View> screenGroupViews = new ArrayList<>();
    private Map<Integer, Integer> groupUpperSpan = new HashMap<>();
    //    SparseIntArray
    private WeakReference<RecyclerView> recyclerView;
    private LightChildHelper lightChildHelper;

    /**
     * @param index
     * @return
     */
    public View getGroupViewAt(int index) {
        if (index < screenGroupViews.size())
            return screenGroupViews.get(index);
        return null;
    }

    public void removeAllViews() {
        for (View view : screenGroupViews) {
            lightChildHelper.removeView(view);
        }
        groupCatch.addAll(screenGroupViews);
        screenGroupViews.clear();
        groupUpperSpan.clear();
    }

    public int getGroupViewCount() {
        return screenGroupViews.size();
    }

    /**
     * 回收屏幕外面的视图
     *
     * @param layoutManager
     * @param recycler
     */
    public void removeAndRecycleView(RecyclerView.LayoutManager layoutManager, RecyclerView.Recycler recycler) {
        for (View view : recyviews) {
            if (isGroupView(view)) {
                lightChildHelper.removeView(view);
                screenGroupViews.remove(view);
                if (groupCatch.size() < 10) {
                    groupCatch.add(view);
                }
            } else {
                layoutManager.removeAndRecycleView(view, recycler);
            }
        }
    }

    /**
     * 方便倒序添加视图
     *
     * @param position
     * @param span
     */
    public void addGroupUpperSpan(int position, int span) {
        if (!groupUpperSpan.containsKey(position))
            groupUpperSpan.put(position, span);
    }

    /***
     * 倒序添加视图
     * @param position
     * @return
     */
    public int getGroupUpperSpan(int position, LightDefultLayoutManager manager) {
        if (groupUpperSpan.containsKey(position))
            return manager.spanCount - groupUpperSpan.get(position);
        return tryGetGroupUpperSpan(position, manager);
    }

    /***
     *
     * @param position
     * @param manager
     * @return
     */
    private int tryGetGroupUpperSpan(int position, LightDefultLayoutManager manager) {
        LightGroupAdapter adapter = getGroupAdapter();
        int i = position - 1, span = 0;
        while (i >= 0) {
            span += manager.getItemSpan(i);
            if (adapter.isGroup(i)) {
                break;
            }
            i--;
        }
        int newspan = manager.spanCount - (span % manager.spanCount);
        groupUpperSpan.put(position, newspan);
        return newspan;
    }

    /**
     * @return
     */
    public RecyclerView getRecyclerView() {
        if (recyclerView != null && recyclerView.get() != null)
            return recyclerView.get();
        return null;
    }

    /***
     * 構造方法
     * @param layoutManager
     */
    public LightGroupRecycler(RecyclerView.LayoutManager layoutManager) {
        initRecyclerView(layoutManager);
        lightChildHelper = new LightChildHelper(layoutManager);
    }

    /***
     * 初始化
     * @param layoutManager
     */
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

    /***
     * 添加视图
     * @param view 子视图
     * @param viewPosition 子视图要插入的位置
     * @param layoutManager recyclerView 的LayoutManager
     * @param isGroup 是否为组
     */
    public void addViewToRecycleView(View view, int viewPosition, RecyclerView.LayoutManager layoutManager, boolean isGroup) {
        if (isGroup) {
            addView(view, viewPosition);
            screenGroupViews.add(viewPosition == -1 ? screenGroupViews.size() : viewPosition, view);
        } else {
            layoutManager.addView(view, viewPosition);
        }
    }

    public void addViewToRecycleView(View view, RecyclerView.LayoutManager layoutManager, boolean isGroup) {
        addViewToRecycleView(view, -1, layoutManager, isGroup);
    }

    public void addChildView(View view, int position) {
        lightChildHelper.addView(view, position, true);
    }

    private void addView(View view, int viewPosition) {
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
        lightChildHelper.addView(view, viewPosition, true);
    }

    /***
     *
     * @param view
     * @return
     */
    public int getPositionFromGroupView(View view) {
        return ((LightRecyLayoutParams) view.getLayoutParams()).getPosition();
    }

    /**
     * @param position
     * @return
     */
    public View getGroupViewForPosition(int position) {
        GroupHolder viewHolder = null;
        if (groupCatch.size() > 0) {
            viewHolder = getGroupHolder(groupCatch.get(0));
            groupCatch.remove(0);
        } else {
            viewHolder = getGroupAdapter().creatGroupHolder(recyclerView.get(), 0);
            viewHolder.itemView.setLayoutParams(new LightRecyLayoutParams(viewHolder));
        }
        ((LightRecyLayoutParams) viewHolder.itemView.getLayoutParams()).setPosition(position);
        getGroupAdapter().onBindGroupHolder(viewHolder, position);
        return viewHolder.itemView;
    }

    /**
     * @param view
     * @return
     */
    public GroupHolder getGroupHolder(View view) {
        return ((LightRecyLayoutParams) view.getLayoutParams()).getGroupViewHolderInt();
    }

    private WeakReference<LightGroupAdapter> groupAdapter;

    /**
     * @return
     */
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


    List<View> recyviews = new ArrayList<>();

    /***
     *
     * @param up
     * @param totalHeight
     * @param layoutManager
     */
    public void recyclerOutScreenView(boolean up, int totalHeight, RecyclerView.LayoutManager layoutManager) {
        recyviews.clear();
        recyclerAllOutScreenView(totalHeight, up, layoutManager);
        recyclerAllOutScreenGroupView(totalHeight, up);
    }

    /***
     *
     * @param totalHeight
     * @param up
     * @param layoutManager
     */
    private void recyclerAllOutScreenView(int totalHeight, boolean up, RecyclerView.LayoutManager layoutManager) {
        int count = layoutManager.getChildCount();
        int i = 0;
        while (i < count) {
            View view = null;
            if (up) {
                view = layoutManager.getChildAt(i);
            } else {
                view = layoutManager.getChildAt((count - 1) - i);
            }
            int y = (int) view.getY();
            i++;
            int height = view.getMeasuredHeight();
            if (!up && y <= totalHeight) {//跳过或终止循环
                break;
            } else if (up && (y + height) >= 0) {
                break;
            }
            recyviews.add(view);
        }

    }

    /**
     * @param totalHeight
     * @param up
     */
    private void recyclerAllOutScreenGroupView(int totalHeight, boolean up) {
        int count = screenGroupViews.size();
        int i = 0;
        while (i < count) {
            View view = null;
            if (up) {
                view = screenGroupViews.get(i);
            } else {
                view = screenGroupViews.get((count - 1) - i);
            }
            int y = (int) view.getY();
            i++;
            int height = view.getMeasuredHeight();
            if (!up && y <= totalHeight) {//跳过或终止循环
                break;
            } else if (up && (y + height) >= 0) {
                break;
            }
            recyviews.add(view);
        }

    }

    /**
     * @param position
     * @return
     */
    public View getChildAt(int position) {
        return recyclerView.get().getChildAt(position);
    }

    /***
     *
     * @param view
     * @return
     */
    public boolean isGroupView(View view) {
        return view.getLayoutParams() instanceof LightRecyLayoutParams;
    }

    /***
     * @param <T>
     */
    public interface LightGroupAdapter<T extends GroupHolder> {
        boolean isGroup(int position);

        T creatGroupHolder(ViewGroup viewGroup, int type);

        void onBindGroupHolder(T holder, int position);

    }

    /***
     *
     */
    public static class GroupHolder extends RecyclerView.ViewHolder {
        public GroupHolder(View itemView) {
            super(itemView);
        }
    }
}