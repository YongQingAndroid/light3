package com.posun.lightui.dragView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;


/**
 * 通用下拉刷新
 * 目前兼容（listView ，RecyclerView，ScrollView，GridView,ExpandableListView以及其他常用非滚动ViewGroup组件的自由组合）
 * 目前暂未测试WebView的兼容情况
 * 相比系统SwipeRefreshLayout更加安全，切对子View没有包装需求，可以拥有多个子布局
 * 下拉动画事件可自由定制
 * Created by zyq on 2017/2/24.
 */
public class LightSwipGroup extends LinearLayout {
    private DragFace pull_layout;
    private float mLastY = -1;
    private boolean mIsBeingDragged = false;
    private AdapterView adapterView;
    private RecyclerView mRecyclerView;
    private ScrollView scrollView;
    private int height = -1;
    private static final float DRAG_RATE = 3;
    private boolean ismore = false;

    /****
     * 拖动阻尼
     **/
    public LightSwipGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightSwipGroup(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.setOrientation(VERTICAL);
        pull_layout = new DragFace(getContext(), PraseDragView.class);
        this.addView(pull_layout);
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                reset();
            }
        });
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof ListView || child instanceof GridView) {
            adapterView = (AdapterView) child;
        } else if (child instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) child;
        } else if (child instanceof ScrollView) {
            scrollView = (ScrollView) child;
        }
        super.addView(child, index, params);
    }

    private boolean isTopIntercept() {
        if (adapterView != null) {
            View firstChild = adapterView.getChildAt(0);
            if (firstChild != null) {
                if (firstChild.getTop() == 0) {
                    return true;
                }
            }
        } else if (mRecyclerView != null) {
            return mRecyclerView.computeVerticalScrollOffset() == 0;
        } else if (scrollView != null) {
            if (scrollView.getScrollY() == 0) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 分发或消费事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsBeingDragged = false;
                mLastY = ev.getRawY();
                return false;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getRawY() - mLastY;
                if (currentY > 0) {
                    mIsBeingDragged = isTopIntercept();
                    break;
                }
                return mIsBeingDragged;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void reset() {
        if (mRecyclerView == null) {
            return;
        }
        if (height == -1) {
            height = mRecyclerView.getMeasuredHeight();
            ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
            lp.height = height;
            mRecyclerView.setLayoutParams(lp);
        }
    }

    /**
     * 处理滑动事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                float deltaY = ev.getRawY() - mLastY;
                if (deltaY > 0) {
                    pull_layout.onMove(deltaY / DRAG_RATE);
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pull_layout.state != Constant.DragState.NORMAL) {
                    pull_layout.recovery();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 销毁View
     **/
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
