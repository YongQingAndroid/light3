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
    private int totalHeight = 0;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    public LightDefultLayoutManager(Context context) {

    }

    int itemCount = 0;

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);
        itemCount = getItemCount();
        //定义竖直方向的偏移量
        int offsetY = 0;
        totalHeight = getHeight();
        int i = 0;
        while (offsetY < totalHeight) {
            //这里就是从缓存里面取出
            View view = recycler.getViewForPosition(i);
            //将View加入到RecyclerView中
            addView(view);
            //对子View进行测量
            measureChildWithMargins(view, 0, 0);
            //把宽高拿到，宽高都是包含ItemDecorate的尺寸
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            //最后，将View布局
            layoutDecorated(view, 0, offsetY, width, offsetY + height);
            //将竖直方向偏移量增大height
            offsetY += height;
            i++;
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    private int verticalScrollOffset;

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        /***添加View**/
        if (dy > 0) {
            View lastView = getChildAt(getChildCount() - 1);
            int position = getPosition(lastView);
            if (position == itemCount - 1) {
                offsetChildrenVertical(0);
                return 0;
            }
            int addheight=0;
            if (lastView.getY() < totalHeight) {
                View view = recycler.getViewForPosition(position + 1);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int width = getDecoratedMeasuredWidth(view);
                int height = getDecoratedMeasuredHeight(view);
                int offsetY = (int) lastView.getY() + lastView.getMeasuredHeight();
                layoutDecorated(view, 0, offsetY, width, offsetY + height);
            }
            offsetChildrenVertical(-dy);
            recyclerView(recycler,true);
        } else {

        }
        return dy;
    }

    private void recyclerView(RecyclerView.Recycler recycler,boolean recyclerBefor) {
        if(recyclerBefor){
            getBeforRecyviews();
        }else{
            getAfterRecyviews();
        }
        for (View view : recyviews) {
            removeAndRecycleView(view, recycler);
        }
    }

    private List<View> recyviews = new ArrayList<>();

    private void getBeforRecyviews() {
        recyviews.clear();
        int i = 0;
        int count = getChildCount();
        while (i < count) {
            View view = getChildAt(i);
            if ((view.getY() + view.getMeasuredHeight()) > 0) {//跌代一直到遇到不能回收的停止
                return;
            }
            recyviews.add(view);
            i++;
        }
    }
    private void getAfterRecyviews() {
        int count = getChildCount();
        int i = count-1;
        while (i >0) {
            View view = getChildAt(i);
            if ((view.getY() + view.getMeasuredHeight()) <=totalHeight) {//跌代一直到遇到不能回收的停止
                return;
            }
            recyviews.add(view);
            i++;
        }
    }
    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }
}
