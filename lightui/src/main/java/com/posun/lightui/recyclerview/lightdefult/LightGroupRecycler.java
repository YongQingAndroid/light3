package com.posun.lightui.recyclerview.lightdefult;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.posun.lightui.recyclerview.adapter.LightFormAdapterManager;
import com.posun.lightui.recyclerview.suspension.SuspensionLayoutParams;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class LightGroupRecycler {
    private List<View> groupCatch = new ArrayList<>();
    private List<View> screenGroupViews = new ArrayList<>();
    private TreeMap<Integer, Integer> groupUpperSpan = new TreeMap<>();
    private Map<Integer, Integer> itemOtherLineSpan = new HashMap<>();
    private WeakReference<RecyclerView> recyclerView;
    private LightChildHelper lightChildHelper;
    private NoValueGroupAdapter noValueGroupAdapter;
    public static final int NOVALUE = Integer.MIN_VALUE;


    public View getFistScreenGroupView() {
        if (screenGroupViews.size() > 0) {
            return screenGroupViews.get(0);
        }
        return null;
    }

    public int getPreviousGroup(int position, int groupPosition) {
        if (isGroup(position)) {
            return position;
        }
        if (position >= groupPosition) {
            return groupPosition;
        }
        int previousPosition = -1;
        if (groupPosition != -1) {
            SortedMap<Integer, Integer> sortedSet = groupUpperSpan.headMap(groupPosition);
            if (sortedSet.size() != 0) {
                previousPosition = sortedSet.lastKey();
            } else {
                return Integer.MIN_VALUE;
            }
        }
        return previousPosition;
    }

    public void setItemOtherLineSpan(int position, int span) {
        itemOtherLineSpan.put(position, span);
    }

    public int getItemOtherLineSpan(int position) {
        Integer integer = itemOtherLineSpan.get(position);
        return integer == null ? NOVALUE : integer;
    }

    public void removeItemOtherLineSpan() {
        itemOtherLineSpan.clear();
    }

    /**
     * @param index
     * @return
     */
    public View getGroupViewAt(int index) {
        if (index >= 0 && index < screenGroupViews.size())
            return screenGroupViews.get(index);
        return null;
    }

    public void removegroupUpperSpan() {
        groupUpperSpan.clear();
    }

    public void removeAllViews() {
        for (View view : screenGroupViews) {
            lightChildHelper.removeView(view);
        }
        groupCatch.addAll(screenGroupViews);
        screenGroupViews.clear();
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
     * @param position
     * @param manager
     * @return
     */
    private int tryGetGroupUpperSpan(int position, LightDefultLayoutManager manager) {
        LightGroupAdapter adapter = getGroupAdapter(position);
        int i = position - 1, span = 0;
        while (i >= 0) {
            int itemspan = manager.getItemSpan(i);
            if (itemspan + span > manager.spanCount) {
                span = itemspan;
            } else {
                span += itemspan;
            }
            if (adapter.isGroup(i)) {//
                break;
            }
            if (i > 500) {//防止卡顿向上遍历500如果依旧寻找不到group放弃寻找
                span = 0;
                i = Integer.MAX_VALUE;
                break;
            }
            i--;
        }
        int newspan = manager.spanCount - (span % manager.spanCount);
        if (i != Integer.MAX_VALUE) {
            groupUpperSpan.put(position, newspan);
        }
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
    public static int getPositionFromGroupView(View view) {
        return ((LightRecyLayoutParams) view.getLayoutParams()).getPosition();
    }

    /**
     * @param position
     * @return
     */
    public View getGroupViewForPosition(int position) {
        GroupHolder viewHolder = null;
        int fictitious = getFictitious(position);//多适配器的虚拟位置
        if (groupCatch.size() > 0) {
            viewHolder = getGroupHolder(groupCatch.get(0));
            groupCatch.remove(0);
        } else {
            viewHolder = getGroupAdapter(position).creatGroupHolder(recyclerView.get(), 0);
            viewHolder.itemView.setLayoutParams(new LightRecyLayoutParams(viewHolder));
        }
        ((LightRecyLayoutParams) viewHolder.itemView.getLayoutParams()).setPosition(position);
        getGroupAdapter(position).onBindGroupHolder(viewHolder, fictitious);
        return viewHolder.itemView;
    }

    /**
     * @param position
     * @return
     */
    public View getGroupViewForPosition(int position, View view) {
        GroupHolder viewHolder = null;
        LightGroupAdapter adapter = getGroupAdapter(position);
        int fictitious = getFictitious(position);
        if (view != null) {
            viewHolder = ((LightRecyLayoutParams) ((SuspensionLayoutParams) view.getLayoutParams()).getOtherLayoutParams()).getGroupViewHolderInt();
        } else {
            viewHolder = adapter.creatGroupHolder(recyclerView.get(), 0);
            viewHolder.itemView.setLayoutParams(new LightRecyLayoutParams(viewHolder));
        }
        ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
        if (layoutParams instanceof SuspensionLayoutParams) {
            ((LightRecyLayoutParams) ((SuspensionLayoutParams) layoutParams).getOtherLayoutParams()).setPosition(position);
        } else {
            ((LightRecyLayoutParams) layoutParams).setPosition(position);
        }
        adapter.onBindGroupHolder(viewHolder, fictitious);
        return viewHolder.itemView;
    }

    public int getFictitious(int position) {
        if (adapterManagerReference != null && adapterManagerReference.get() != null) {
            return adapterManagerReference.get().getChildAdapterPosition(position);
        }
        return position;
    }

    /**
     * @param view
     * @return
     */
    public GroupHolder getGroupHolder(View view) {
        return ((LightRecyLayoutParams) view.getLayoutParams()).getGroupViewHolderInt();
    }

    public boolean haveGroup() {
        if (recyclerView.get().getAdapter() instanceof LightFormAdapterManager) {
            LightFormAdapterManager adapterManager = (LightFormAdapterManager) recyclerView.get().getAdapter();
            return adapterManager.haveGroup;
        } else if (recyclerView.get().getAdapter() instanceof LightGroupAdapter) {
            return true;
        }
        return false;
    }

    private WeakReference<LightGroupAdapter> groupAdapter;
    private WeakReference<LightFormAdapterManager> adapterManagerReference;


    public boolean isGroup(int position) {
        return getGroupAdapter(position).isGroup(getFictitious(position));
    }

    /**
     * @return
     */
    public LightGroupAdapter getGroupAdapter(int position) {
        if (adapterManagerReference != null || recyclerView.get().getAdapter() instanceof LightFormAdapterManager) {
            LightFormAdapterManager adapterManager = null;
            if (adapterManagerReference == null || adapterManagerReference.get() == null) {
                adapterManagerReference = new WeakReference<>((LightFormAdapterManager) recyclerView.get().getAdapter());
            }
            adapterManager = adapterManagerReference.get();
            Object adapterInterface = adapterManager.getAdapterByPosition(position);
            if (adapterInterface instanceof LightGroupAdapter) {
                return (LightGroupAdapter) adapterInterface;
            }
            if (noValueGroupAdapter == null) {
                noValueGroupAdapter = new NoValueGroupAdapter();
            }
            return noValueGroupAdapter;
        }
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

        public int getGroupAdapterPosition() {
            return getPositionFromGroupView(itemView);
        }
    }
}