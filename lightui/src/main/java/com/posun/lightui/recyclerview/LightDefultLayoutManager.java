package com.posun.lightui.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * package light3:com.posun.lightui.recyclerview.LightDefultLayoutManager.class
 * 作者：zyq on 2018/1/31 11:16
 * 邮箱：zyq@posun.com
 */

public class LightDefultLayoutManager extends RecyclerView.LayoutManager {
    private int totalHeight = 0, totalWight = 0;
    private int spanCount = 3, beforSpan = 0, afterSpan = 0;
    int itemCount = 0;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    public LightDefultLayoutManager(Context context) {

    }

    private SpanSizeLookup mSpanSizeLookup;

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        mSpanSizeLookup = spanSizeLookup;
    }

    public LightDefultLayoutManager(Context context, int spanCount) {
        this.spanCount = spanCount;
    }

    private int getItemSpan(int position) {
        if (mSpanSizeLookup != null) {
            return mSpanSizeLookup.getSpanSize(position);
        }
        return 1;
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);
        beforSpan = 0;
        afterSpan = 0;
        itemCount = getItemCount();
        //定义竖直方向的偏移量
        int offsetY = 0;
        totalHeight = getHeight();
        totalWight = getWidth();
        int i = 0;
        while (offsetY < totalHeight && haveNext(i)) {
            int height = addBeforItem(recycler, i, offsetY);
            offsetY += height;
            i++;
        }
        super.onLayoutChildren(recycler, state);
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
        Log.e("fillLayout", getChildCount() + "");
        if (dy > 0) {
            View lastView = getChildAt(getChildCount() - 1);
            int addheight = (int) (lastView.getY() + lastView.getMeasuredHeight());
            addheight = addheight - totalHeight;
            int position = getPosition(lastView) + 1;
            int offsetY = (int) lastView.getY() + lastView.getMeasuredHeight();
            while (haveNext(position) && addheight <= dy) {
                int height = addBeforItem(recycler, position, offsetY);
                offsetY += height;
                addheight += height;
                position++;
            }
            if (dy > addheight) {
                dy = addheight;
            }
            offsetChildrenVertical(-dy);
            recyclerView(true, recycler);
        } else {
            View fistView = getChildAt(0);
            int addheight = (int) Math.abs(fistView.getY());
            int position = getPosition(fistView) - 1;
            int offsetY = (int) fistView.getY();
            while (position >= 0 && addheight <= Math.abs(dy)) {
                int height = addAfterItem(recycler, position, offsetY);
                offsetY -= height;
                addheight += height;
                position--;
            }
            if (Math.abs(dy) > addheight) {
                dy = -addheight;
            }
            offsetChildrenVertical(-dy);
            recyclerView(false, recycler);
        }
        return dy;
    }

    private int addAfterItem(RecyclerView.Recycler recycler, int position, int offsetY) {
        View view = recycler.getViewForPosition(position);
        addView(view, 0);//很重要
        int span = getItemSpan(position);
        if (beforSpan >= spanCount) {
            beforSpan = 0;
        }
        measureChild(view, 0, 0);
        int width = getDecoratedMeasuredWidth(view);
        int height = getDecoratedMeasuredHeight(view);
        layoutDecoratedWithMargins(view, 0, offsetY - height, width, offsetY);
        return height;
    }

    private int addBeforItem(RecyclerView.Recycler recycler, int position, int offsetY) {
        View view = recycler.getViewForPosition(position);

        int span = getItemSpan(position);
        if (beforSpan >= spanCount) {
            beforSpan = 0;
        }
        int item = totalWight / spanCount;
        int wightUsed = item * (spanCount - span);
        measureChild(view, wightUsed, 0);
        int width = getDecoratedMeasuredWidth(view);
        int height = getDecoratedMeasuredHeight(view);
        int left = beforSpan * item;

        addView(view);
        Log.e("qing", "left=" + left);
//        left = 360;
        int right = width + left;
        layoutDecoratedWithMargins(view, left, offsetY, right, offsetY + height);
        if (beforSpan != 0 && (beforSpan + span) <= spanCount) {
            beforSpan += span;
            return 0;
        } else {
            beforSpan += span;
            return height;
        }
    }

    private boolean haveNext(int position) {
        return position < itemCount;
    }

    private void recyclerView(boolean up, RecyclerView.Recycler recycler) {
        recyviews.clear();
        if (up) {
            RecyclerBeforviews(0);
        } else {
            RecyclerAfterviews(0);
        }
        for (View view : recyviews) {
            removeAndRecycleView(view, recycler);
        }
    }

    private List<View> recyviews = new ArrayList<>();

    private void RecyclerBeforviews(int dy) {
        int i = 0;
        int count = getChildCount();
        while (i < count) {
            View view = getChildAt(i);
            if ((view.getY() + view.getMeasuredHeight()) >= dy) {//跌代一直到遇到不能回收的停止
                return;
            }
            recyviews.add(view);
            i++;
        }
    }

    private void RecyclerAfterviews(int dy) {
        int count = getChildCount();
        int i = count - 1;
        while (i >= 0) {
            View view = getChildAt(i);
            if (view.getY() <= (totalHeight - dy)) {//跌代一直到遇到不能回收的停止
                return;
            }
            recyviews.add(view);
            i--;
        }
    }

    public interface SpanSizeLookup {
        int getSpanSize(int position);
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }
}
