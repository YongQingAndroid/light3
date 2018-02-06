package com.posun.lightui.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * package light3:com.posun.lightui.recyclerview.LightDefultLayoutManager.class
 * 作者：zyq on 2018/1/31 11:16
 * 邮箱：zyq@posun.com
 */

public class LightDefultLayoutManager extends RecyclerView.LayoutManager {
    private int totalHeight = 0, totalWight = 0;
    private int beforSpan = 0, afterSpan = 0;
    protected int spanCount = 2;
    private LightScrollBarHelper mLightScrollBarHelper;
    int itemCount = 0;
    private Context context;
    private LightGroupRecycler groupRecycler;
    private boolean isGroupAdapter = true;
    private LightLayoutState layoutState;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    public LightDefultLayoutManager(Context context) {
        this.context = context;
    }

    private SpanSizeLookup mSpanSizeLookup;

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        mSpanSizeLookup = spanSizeLookup;
    }

    public LightDefultLayoutManager(Context context, int spanCount) {
        this.spanCount = spanCount;
        this.context = context;
    }

    int getItemSpan(int position) {
        if (mSpanSizeLookup != null) {
            int span = mSpanSizeLookup.getSpanSize(position);
            return span > spanCount ? spanCount : span;
        }
        return 1;
    }

    private void onLayoutLastViewSpanAndOffsetY(View view) {
        if (groupRecycler.isGroupView(view)) {
            beforSpan = 0;
        } else {
            int span = getItemSpan(getPosition(view));
            int x = (int) view.getX();
            beforSpan = (x / (totalWight / spanCount));
            if (beforSpan != 0)
                beforSpan -= span;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        int position = 0;
        //定义竖直方向的偏移量
        int offsetY = 0;
        beforSpan = 0;
        afterSpan = 0;
        itemCount = getItemCount();
        totalHeight = getHeight();
        totalWight = getWidth();
        View view = getFistView();
        if (view != null) {
            position = getPosition(view);
            if (position >= itemCount) {
                position = 0;
            } else {
                offsetY = (int) view.getY();
                onLayoutLastViewSpanAndOffsetY(view);
            }
        }
        detachAndScrapAttachedViews(recycler);
        if (groupRecycler == null)
            groupRecycler = new LightGroupRecycler(this);
        if (layoutState == null) {
            layoutState = new LightLayoutState(recycler);
        }
        if (position != 0) {
            position -= 1;
        }
        layoutState.setPosition(position);
        layoutState.setOffsetY(offsetY);
        layoutState.totalHeight.setValue(0);
        while (layoutState.offsetY.getIndex() < totalHeight && layoutState.haveNext()) {
            addBeforItemWithGroup(recycler, layoutState);
        }
        double initScrollPosition = (layoutState.position.getIndex() - position) / (double) itemCount;
        initScrollPosition = initScrollPosition * 100;
        if (mLightScrollBarHelper == null) {
            mLightScrollBarHelper = new LightScrollBarHelper();
        }
        mLightScrollBarHelper.setInitScrollPosition(initScrollPosition);
        if (view != null) {
            groupRecycler.removegroupUpperSpan();
        }
    }

    @Override
    public void detachAndScrapAttachedViews(RecyclerView.Recycler recycler) {
        super.detachAndScrapAttachedViews(recycler);
        if (groupRecycler != null) {
            groupRecycler.removeAllViews();
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        dy = fillLayout(dy, recycler);
        return dy;
    }

    private int fillLayout(int dy, RecyclerView.Recycler recycler) {
        Log.e("fillLayout", groupRecycler.getRecyclerView().getChildCount() + "");
        if (dy > 0) {
            View lastView = getLastView();
            int addheight = (int) (lastView.getY() + lastView.getMeasuredHeight());
            addheight = addheight - totalHeight;
            int offsetY = (int) lastView.getY() + lastView.getMeasuredHeight();
            offsetY += resitLastViewSpanAndOffsetY(lastView);
            layoutState.setOffsetY(offsetY);
            layoutState.totalHeight.setValue(addheight);
            layoutState.setPosition(getPosition(lastView));
            while (layoutState.haveNext() && layoutState.totalHeight.getIndex() <= dy) {
                addBeforItemWithGroup(recycler, layoutState);
            }
            if (dy > layoutState.totalHeight.getIndex()) {
                dy = layoutState.totalHeight.getIndex();
            }
            offsetChildrenVertical(-dy);
            recyclerView(true, recycler);
        } else {
            View fistView = getFistView();
            int addheight = 0;
            int offsetY = (int) fistView.getY();
            if (offsetY < 0) {
                addheight = (int) Math.abs(fistView.getY());
            }
            int index = getPosition(fistView);
            resitfistViewSpan(fistView);
            int juageheight = juageHasGroupView(fistView, offsetY, index);
            addheight += juageheight;
            offsetY -= juageheight;
            layoutState.setPosition(index);
            layoutState.setOffsetY(offsetY);
            layoutState.totalHeight.setValue(addheight);
            while (layoutState.havePreviousView() && layoutState.totalHeight.getIndex() <= Math.abs(dy)) {
                addAfterItemWithGroup(recycler, layoutState);
            }
            if (Math.abs(dy) > layoutState.totalHeight.getIndex()) {
                dy = -layoutState.totalHeight.getIndex();
            }
            offsetChildrenVertical(-dy);
            recyclerView(false, recycler);
        }
        return dy;
    }

    /***
     *
     * @param fistview
     * @param offsetY
     * @param position
     * @return
     */
    private int juageHasGroupView(View fistview, int offsetY, int position) {
        int addheight = 0;
        if (isGroupAdapter && groupRecycler.getGroupAdapter().isGroup(position)) {
            if (!groupRecycler.isGroupView(fistview)) {
                View view = groupRecycler.getGroupViewForPosition(position);
                measureChild(view, 0, 0);
                groupRecycler.addViewToRecycleView(view, 0, this, true);
                int viewHeight = view.getMeasuredHeight();
                layoutDecoratedWithMargins(view, 0, offsetY - viewHeight, totalHeight, offsetY);
                addheight += viewHeight;
            }
            afterSpan = groupRecycler.getGroupUpperSpan(position, this);
        }
        return addheight;
    }

    public View getFistView() {
        View adapterView = getChildAt(0);
        if (!isGroupAdapter || groupRecycler == null) {
            return adapterView;
        }
        View groupView = groupRecycler.getGroupViewAt(0);
        if (groupView == null)
            return adapterView;
        return groupView.getY() < adapterView.getY() ? groupView : adapterView;
    }

    public View getLastView() {
        View adapterView = getChildAt(getChildCount() - 1);
        if (!isGroupAdapter) {
            return adapterView;
        }
        View groupView = groupRecycler.getGroupViewAt(groupRecycler.getGroupViewCount() - 1);
        if (groupView == null)
            return adapterView;
        return groupView.getY() > adapterView.getY() ? groupView : adapterView;
    }

    @Override
    public int getPosition(View view) {
        if (groupRecycler.isGroupView(view)) {
            return groupRecycler.getPositionFromGroupView(view);
        }
        return super.getPosition(view);
    }

    /***
     * 向上滑动时加载底部View重置底部加载坐标位置
     * @param view
     */
    private int resitLastViewSpanAndOffsetY(View view) {
        if (groupRecycler.isGroupView(view)) {
            beforSpan = 0;
            return 0;
        }
        int span = getItemSpan(getPosition(view));
        int x = (int) view.getX();
        beforSpan = (x / (totalWight / spanCount)) + span;
        if (beforSpan != 0 && beforSpan != spanCount) {
            return -view.getMeasuredHeight();
        }
        return 0;
    }

    /**
     * 向下滑动时加载顶部View重置顶部加载坐标位置
     *
     * @param view
     */
    private void resitfistViewSpan(View view) {
        int x = totalWight - ((int) view.getX());//向下滑动时视图为逆序添加
        afterSpan = spanCount - (x / (totalWight / spanCount));
    }

    private void addAfterItemWithGroup(RecyclerView.Recycler recycler, LightLayoutState state) {
        View view = state.getPreviousView();
        if (view == null)
            return;
        if (groupRecycler.isGroupView(view)) {
            groupRecycler.addViewToRecycleView(view, 0, this, true);
            int viewHeight = view.getMeasuredHeight();
            layoutDecoratedWithMargins(view, 0, state.offsetY.getIndex() - viewHeight, totalHeight, state.offsetY.getIndex());
            state.totalHeight.add(viewHeight);
            state.offsetY.reduce(viewHeight);
            afterSpan = groupRecycler.getGroupUpperSpan(state.position.getIndex(), this);
        } else {
            addAfterItem(view, state);
        }
    }

    private void addAfterItem(View view, LightLayoutState state) {
        groupRecycler.addViewToRecycleView(view, 0, this, false);
        int span = getItemSpan(state.position.getIndex());
        if (afterSpan >= spanCount) {
            afterSpan = 0;
        }
        int item = totalWight / spanCount;
        int wightUsed = item * (spanCount - span);
        measureChild(view, wightUsed, 0);
        int width = getDecoratedMeasuredWidth(view);
        int height = getDecoratedMeasuredHeight(view);
        int right = totalWight - (afterSpan * item);
        int left = right - width;
        layoutDecoratedWithMargins(view, left, state.offsetY.getIndex() - height, right, state.offsetY.getIndex());
        if ((afterSpan + span) < spanCount) {
            afterSpan += span;
        } else {
            afterSpan += span;
            if (!state.position.isLockshowGroup()) {
                state.totalHeight.add(height);
                state.offsetY.reduce(height);
            }
        }
    }

    private void addBeforItemWithGroup(RecyclerView.Recycler recycler, LightLayoutState state) {
        View view = state.getNextView();
        if (groupRecycler.isGroupView(view)) {
            measureChild(view, 0, 0);
            groupRecycler.addViewToRecycleView(view, this, true);
            int height = view.getMeasuredHeight();
            layoutDecoratedWithMargins(view, 0, state.offsetY.getIndex(), totalHeight, state.offsetY.getIndex() + height);
            beforSpan = 0;
            state.offsetY.add(height);
            addBeforItem1(recycler, state);
        } else {
            addBeforItem(view, state);
        }
    }

    private void addBeforItem1(RecyclerView.Recycler recycler, LightLayoutState state) {
        while (state.haveNext() && beforSpan != spanCount && !groupRecycler.getGroupAdapter().isGroup(state.position.getIndex() + 1)) {
            addBeforItem(state.getNextView(), state);
        }
    }

    private void addBeforItem(View view, LightLayoutState state) {
        int span = getItemSpan(state.position.getIndex());
        if (beforSpan >= spanCount) {
            beforSpan = 0;
        }
        int item = totalWight / spanCount;
        int wightUsed = item * (spanCount - span);
        measureChild(view, wightUsed, 0);
        int width = getDecoratedMeasuredWidth(view);
        int height = getDecoratedMeasuredHeight(view);
        int left = beforSpan * item;
        groupRecycler.addViewToRecycleView(view, this, false);
        int right = width + left;
        layoutDecoratedWithMargins(view, left, state.offsetY.getIndex(), right, state.offsetY.getIndex() + height);

        if ((beforSpan + span) < spanCount) {
            beforSpan += span;
            if (!state.haveNext()) {//已经滑动到了底部
                state.offsetY.add(height);
                state.totalHeight.add(height);
            }
        } else {
            beforSpan += span;
            state.offsetY.add(height);
            state.totalHeight.add(height);
        }
    }

    /**
     * 回收屏幕外面的视图
     *
     * @param up
     * @param recycler
     */
    private void recyclerView(boolean up, RecyclerView.Recycler recycler) {
        groupRecycler.recyclerOutScreenView(up, totalHeight, this);
        groupRecycler.removeAndRecycleView(this, recycler);
    }

    public interface SpanSizeLookup {
        int getSpanSize(int position);
    }

    @Override
    public void offsetChildrenVertical(int dy) {
        int size = groupRecycler.getRecyclerView().getChildCount();
        for (int i = 0; i < size; i++) {
            View view = groupRecycler.getRecyclerView().getChildAt(i);
            view.offsetTopAndBottom(dy);
        }
    }

    @Override
    public int computeVerticalScrollRange(RecyclerView.State state) {
        return mLightScrollBarHelper.MAX * mLightScrollBarHelper.ENLARGE;
    }

    @Override
    public int computeVerticalScrollExtent(RecyclerView.State state) {
        return (int) (mLightScrollBarHelper.ENLARGE * mLightScrollBarHelper.getInitScrollPosition());
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        View view = getChildAt(getChildCount() - 1);
        int position = getPosition(view);
        return mLightScrollBarHelper.verticalScroll(position, state, totalHeight - (int) view.getY(), view.getHeight());
    }

    private class ItemIndex {
        private int num;
        private boolean LockshowGroup = false;

        public ItemIndex(int num) {
            this.num = num;
        }

        public int getIndex() {
            return num;
        }

        public void next() {
            num++;
        }

        public void previous() {
            num--;
        }

        public void add(int arg) {
            num += arg;
        }

        public void reduce(int arg) {
            num -= arg;
        }

        public void setValue(int arg) {
            this.num = arg;
        }

        public boolean isLockshowGroup() {
            return LockshowGroup;
        }

        public void setLockshowGroup(boolean lockshowGroup) {
            LockshowGroup = lockshowGroup;
        }

        @Override
        public String toString() {
            return String.valueOf(num);
        }
    }

    private class LightLayoutState {
        ItemIndex position = new ItemIndex(-1);
        ItemIndex offsetY = new ItemIndex(0);
        ItemIndex totalHeight = new ItemIndex(0);
        private RecyclerView.Recycler recycler;

        LightLayoutState(RecyclerView.Recycler recycler) {
            this.recycler = recycler;
        }

        public void setPosition(int position) {
            if (position == 0) {
                position = -1;
            }
            this.position.setValue(position);
            this.position.setLockshowGroup(false);
        }

        public void setOffsetY(int offsetY) {
            this.offsetY.setValue(offsetY);
        }

        /***
         * @return
         */
        public View getNextView() {
            View view = null;
            if (!position.isLockshowGroup())
                position.next();
            if (isGroupAdapter && groupRecycler.getGroupAdapter().isGroup(position.getIndex()) && !position.isLockshowGroup()) {
                view = groupRecycler.getGroupViewForPosition(position.getIndex());
                position.setLockshowGroup(true);
                groupRecycler.addGroupUpperSpan(position.getIndex(), beforSpan);
                if (beforSpan % spanCount != 0 && spanCount != 1) {
                    View topview = getChildAt(getChildCount() - 1);
                    int otherLine = topview.getMeasuredHeight();
                    totalHeight.add(otherLine);
                    offsetY.add(otherLine);
                }
            } else {
                view = recycler.getViewForPosition(position.getIndex());
                position.setLockshowGroup(false);
            }
            return view;
        }

        /***
         * @return
         */
        public View getPreviousView() {
            View view = null;
            if (!position.isLockshowGroup())
                position.previous();
            if (!position.isLockshowGroup()) {
                view = recycler.getViewForPosition(position.getIndex());
                if (isGroupAdapter && groupRecycler.getGroupAdapter().isGroup(position.getIndex())) {
                    position.setLockshowGroup(true);
                }
            } else if (isGroupAdapter && position.isLockshowGroup()) {
                View viewItem = getChildAt(0);
                offsetY.reduce(viewItem.getHeight());
                totalHeight.add(viewItem.getHeight());
                position.setLockshowGroup(false);
                view = groupRecycler.getGroupViewForPosition(position.getIndex());
                measureChild(view, 0, 0);
            }
            return view;
        }

        private boolean havePreviousView() {
            if (position.isLockshowGroup())
                return true;
            return position.getIndex() > 0;
        }

        private boolean haveNext() {
            if (position.isLockshowGroup())
                return true;
            return position.getIndex() + 1 < itemCount;
        }
    }
    /**废弃代码位置addBeforItem*/
//        if (beforSpan != 0 && span + beforSpan > spanCount) { /**需要优化比较耗费性能(当屏幕剩余空间装不下是调用)**/ 错错错后添加View
//            left = 0;
//            beforSpan = 0;
//            int childHeight = getChildAt(getChildCount() - 1).getHeight();
//            state.offsetY.add(childHeight);
//            beforSpan += span;
//            int right = width + left;
//            layoutDecoratedWithMargins(view, left, offsetY, right, offsetY + height);
//            if (beforSpan >= spanCount) {//判断当前换行的item是否屏幕是否还有剩余空间
//                height = childHeight + height;
//            } else {
//                height = childHeight;
//            }
//        } else {
//            int right = width + left;
//            layoutDecoratedWithMargins(view, left, offsetY, right, offsetY + height);
//        }
}
