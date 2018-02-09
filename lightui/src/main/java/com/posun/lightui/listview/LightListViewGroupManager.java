package com.posun.lightui.listview;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * package light3:com.posun.lightui.listview.LightListViewGroupManager.class
 * 作者：zyq on 2018/1/23 10:10
 * 邮箱：zyq@posun.com
 */

public class LightListViewGroupManager {
    ListViewTopGroup mListViewTopGroup = null;
    private ListView listView;
    private View mViewSectionPin;
    private float mSectionPinOffset = 0f;
    private FrameLayout frameLayout;
    private int beforindex = -1;

    public void init(Activity activity, ListView listView) {
        this.listView = listView;
        if (frameLayout == null)
            frameLayout = new FrameLayout(activity);
        ViewGroup ac = (ViewGroup) listView.getParent();
        ac.removeView(listView);
        ac.addView(frameLayout, listView.getLayoutParams());
        frameLayout.addView(listView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mListViewTopGroup = new ListViewTopGroup(activity);
        frameLayout.addView(mListViewTopGroup, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            /**
             * @param absListView      ListView
             * @param firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
             * @param visibleItemCount 当前能看见的列表项个数（小半个也算，并且header views 和 footer views 也算在里面了）
             * @param totalItemCount   列表项总是 (header count + adapter count + footer count)
             */
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (getAdapter() == null && !(getAdapter() instanceof LightListViewGroupAdapter)) {
                    return;
                }
                int headerViewCount = getHeaderViewsCount();
                if (getAdapter().getCount() <= 0) {
                    return;
                }
                int adapterFirstVisibleItem = firstVisibleItem - headerViewCount;
                int pinViewAdapterPosition = getPinViewAdapterPosition(adapterFirstVisibleItem);
                /**提高滑動速度減少繪製*/
                if (pinViewAdapterPosition != -1 && beforindex != pinViewAdapterPosition) {
                    View view = getSectionPinView(pinViewAdapterPosition);
                    if (mViewSectionPin == null || mListViewTopGroup.getChildCount() == 0) {
                        mViewSectionPin = view;
                        if (mViewSectionPin.getLayoutParams() == null) {
                            mViewSectionPin.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        }
                        mListViewTopGroup.addView(mViewSectionPin);
                    }
                    beforindex = pinViewAdapterPosition;
                } else if (pinViewAdapterPosition < 0 && (beforindex == 0 || beforindex < 0)) { ///当有HeaderViews时及时移除悬浮View
                    mListViewTopGroup.removeAllViews();
                    beforindex = pinViewAdapterPosition;
                }
                if (mViewSectionPin == null) {
                    return;
                }
//                Log.i("zyq", pinViewAdapterPosition + "beforindex =" + beforindex);
                for (int index = 0; index < visibleItemCount; index++) {
                    int adapterPosition = index + adapterFirstVisibleItem;
                    /**
                     * 判断是不是section
                     */
                    LightListViewGroupAdapter adapter = (LightListViewGroupAdapter) getAdapter();
                    if (adapter.hasGroup(adapterPosition)) {
                        View sectionView = LightListViewGroupManager.this.listView.getChildAt(index);
                        int sectionTop = sectionView.getTop();
                        int pinViewHeight = mListViewTopGroup.getHeight();
                        if (sectionTop < pinViewHeight && sectionTop > 0) {
                            mSectionPinOffset = sectionTop - pinViewHeight;
                        }
                        break;
                    }
                }
                mListViewTopGroup.invalidate(mSectionPinOffset);
                mSectionPinOffset = 0f;
            }
        });
    }

    private ListAdapter mListAdapter;

    private ListAdapter getAdapter() {
        if (mListAdapter != null)
            return mListAdapter;
        if ((listView.getAdapter() instanceof HeaderViewListAdapter)) {
            return mListAdapter = ((HeaderViewListAdapter) listView.getAdapter()).getWrappedAdapter();
        }
        return mListAdapter = listView.getAdapter();
    }

    public int getHeaderViewsCount() {
        return listView.getHeaderViewsCount();
    }

    /**
     * 获取固定在顶部的View
     *
     * @return View
     */
    private View getSectionPinView(int adapterPosition) {
        if (getAdapter() == null) {
            return null;
        }
        return ((LightListViewGroupAdapter) getAdapter()).getGroupView(adapterPosition, mViewSectionPin, mListViewTopGroup);
    }

    /**
     * 根据第一个可见的adapter的位置去获取临近的一个section的位置
     *
     * @param adapterFirstVisible 第一个可见的adapter的位置
     * @return -1：未找到 >=0 找到位置
     */
    private int getPinViewAdapterPosition(int adapterFirstVisible) {
        if (getAdapter() == null || !(getAdapter() instanceof LightListViewGroupAdapter)) {
            return -1;
        }
        LightListViewGroupAdapter adapter = (LightListViewGroupAdapter) getAdapter();
        for (int index = adapterFirstVisible; index >= 0; index--) {
            if (adapter.hasGroup(index)) {
                return index;
            }
        }
        return -1;
    }

}
