package com.posun.lightui.recyclerview.suspension;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.posun.lightui.recyclerview.lightdefult.LightDefultLayoutManager;

/**
 * package light3:com.posun.lightui.recyclerview.suspension.SuspensionManager.class
 * 作者：zyq on 2018/2/7 10:49
 * 邮箱：zyq@posun.com
 */

public class SuspensionManager {
    private FrameLayout frameLayout;
    private LightDefultLayoutManager manager;

    /***
     * @param childView
     * @param parentView
     */
    public void replaceViewParent(View childView, ViewGroup parentView) {
        ViewGroup ac = (ViewGroup) childView.getParent();
        ac.removeView(childView);
        ac.addView(parentView, childView.getLayoutParams());
        parentView.addView(childView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    private int beforindex = -1;
    private View mViewSectionPin;

    public void setRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null || !(layoutManager instanceof LightDefultLayoutManager)) {
            throw new RuntimeException("please set LightDefultLayoutManager fist");
        }
        frameLayout = new FrameLayout(recyclerView.getContext());
        replaceViewParent(recyclerView, frameLayout);
        manager = (LightDefultLayoutManager) layoutManager;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int adapterFirstVisibleItem = manager.getPosition(manager.getFistView());
                int previousPosition = manager.getGroupRecycler().getPreviousGroup(adapterFirstVisibleItem, beforindex);
                if (previousPosition != -1 && previousPosition != Integer.MIN_VALUE && beforindex != previousPosition) {
                    View view = getSectionPinView(previousPosition);
                    if (mViewSectionPin == null) {
                        mViewSectionPin = view;
                        if (mViewSectionPin.getLayoutParams() == null || !(mViewSectionPin.getLayoutParams() instanceof SuspensionLayoutParams)) {
                            mViewSectionPin.setLayoutParams(new SuspensionLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).setOtherLayoutParams(mViewSectionPin.getLayoutParams()));
                        }
                        frameLayout.addView(mViewSectionPin);
                    }
                    if (mViewSectionPin.getVisibility() != View.VISIBLE) {
                        mViewSectionPin.setVisibility(View.VISIBLE);
                    }
                    beforindex = previousPosition;
                } else if (previousPosition == Integer.MIN_VALUE) {
                    mViewSectionPin.setVisibility(View.GONE);
                    beforindex = -1;
                }
                View sectionView = getNextVisibilityView();
                if (sectionView == null || mViewSectionPin == null)
                    return;
                int sectionTop = sectionView.getTop();
                int height = mViewSectionPin.getHeight();
                if (sectionTop < height && sectionTop > 0) {
                    int top = sectionTop - height;
                    mViewSectionPin.layout(mViewSectionPin.getLeft(), top, mViewSectionPin.getRight(), sectionTop);
                } else {
                    resitLayout(mViewSectionPin);
                }
            }
        });
        manager.addRefreshListener(new LightDefultLayoutManager.RefreshListener() {
            @Override
            public void onRefresh() {
                if (mViewSectionPin != null && beforindex != -1)
                    getSectionPinView(beforindex);
            }
        });

    }

    private void resitLayout(View view) {
        view.layout(view.getLeft(), 0, view.getRight(), view.getMeasuredHeight());
    }

    private View getNextVisibilityView() {
        return manager.getGroupRecycler().getFistScreenGroupView();
    }

    /**
     * 获取固定在顶部的View
     *
     * @return View
     */
    private View getSectionPinView(int adapterPosition) {
        return manager.getGroupRecycler().getGroupViewForPosition(adapterPosition, mViewSectionPin);
    }
}
