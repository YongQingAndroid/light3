package com.posun.lightui.recyclerview.event;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * package Kotlin3:qing.com.kotlin3.lib.LightOnItemTouchListener.class
 * 作者：zyq on 2018/1/18 14:01
 * 邮箱：zyq@posun.com
 */

public class LightOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat mGestureDetectorCompat;
    private RecyclerView mRecyclerView;
    private OnItemTouchListener onItemTouchListener;

    public void setOnItemTouchListener(OnItemTouchListener onItemTouchListener) {
        this.onItemTouchListener = onItemTouchListener;
    }

    public LightOnItemTouchListener(RecyclerView recyclerView) {
        this(recyclerView, null);
    }

    public LightOnItemTouchListener(RecyclerView recyclerView, OnItemTouchListener onItemTouchListener) {
        mRecyclerView = recyclerView;
        this.onItemTouchListener = onItemTouchListener;
        mGestureDetectorCompat = new GestureDetectorCompat(mRecyclerView.getContext(),
                new MyGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View childe = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childe != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(childe);
                if (onItemTouchListener != null)
                    onItemTouchListener.onItemClick(VH);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View childe = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childe != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(childe);
                if (onItemTouchListener != null)
                    onItemTouchListener.onLongItemClick(VH);
            }
        }
    }

    public interface OnItemTouchListener<T extends RecyclerView.ViewHolder> {
        void onItemClick(T vh);

        void onLongItemClick(T vh);
    }
}
