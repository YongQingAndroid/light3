package com.posun.lightui.listview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * package light3:com.posun.lightui.listview.LightSelectionAdapter.class
 * 作者：zyq on 2018/1/23 11:46
 * 邮箱：zyq@posun.com
 */

public abstract class LightListViewGroupAdapter extends BaseAdapter {
    private List<View> catchViews = new ArrayList<>();

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            holder.rootView = new LinearLayout(viewGroup.getContext());
            holder.rootView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            holder.rootView.setOrientation(LinearLayout.VERTICAL);
            view = holder.rootView;
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.rootView.removeAllViews();
        Boolean issection = hasGroup(position);
        if (issection) {
            View catchView = null;
            catchView = getCatchView(holder);
            if(catchView==null){
                catchView = getGroupView(position, catchView, holder.rootView);
                catchView.setOnClickListener(onClickListener); //屏蔽点击group item时 触发onitemClick
            }else{
                catchView = getGroupView(position, catchView, holder.rootView);
            }
            holder.addTitleView(catchView);
        } else if (holder.titleView != null) {
            catchViews.add(holder.titleView);
            holder.titleView = null;
        }
        View contentView = null;
        contentView = holder.contentview;
        contentView = getContentView(position, contentView, holder.rootView);
        holder.addContentView(contentView);
        return view;
    }
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //屏蔽点击group item时 触发onitemClick
        }
    };
    private View getCatchView(Holder holder) {
        View catchView = null;
        if (holder.titleView != null) {
            return holder.titleView;
        } else if (catchViews.size() > 0) {
            catchView = catchViews.get(0);
            catchViews.remove(0);
        }
        return catchView;
    }

    static class Holder {
        LinearLayout rootView;
        View contentview, titleView;

        public void addTitleView(View view) {
            this.titleView = view;
            rootView.addView(view);
        }

        public void addContentView(View view) {
            this.contentview = view;
            rootView.addView(view);
        }
    }

    public abstract View getContentView(int position, View view, ViewGroup viewGroup);

    public abstract View getGroupView(int position, View view, ViewGroup viewGroup);

    public abstract boolean hasGroup(int position);
}
