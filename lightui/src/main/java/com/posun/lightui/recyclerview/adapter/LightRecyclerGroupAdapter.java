package com.posun.lightui.recyclerview.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * package light3:com.posun.lightui.listview.LightSelectionAdapter.class
 * 作者：zyq on 2018/1/23 11:46
 * 邮箱：zyq@posun.com
 */

public abstract class LightRecyclerGroupAdapter<G extends LightFormAdapterManager.ChildHolder, C extends LightFormAdapterManager.ChildHolder> implements LightFormAdapterManager.LightFormBaseAdapterInterface<LightRecyclerGroupAdapter.Holder> {
    private List<LightFormAdapterManager.ChildHolder> catchViews = new ArrayList<>();
    protected int instentType = -1;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //屏蔽点击group item时 触发onitemClick
        }
    };

    private LightFormAdapterManager.ChildHolder getCatchView(Holder holder) {
        LightFormAdapterManager.ChildHolder catchView = null;
        if (holder.titleView != null) {
            return holder.titleView;
        } else if (catchViews.size() > 0) {
            catchView = catchViews.get(0);
            catchViews.remove(0);
        }
        return catchView;
    }

    @Override
    public LightFormAdapterManager.OnItemCilckListener getItemCilckListener() {
        return null;
    }


    @Override
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LinearLayout linearLayout = new LinearLayout(viewGroup.getContext());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return new Holder(linearLayout);
    }

    @Override
    public void onBindViewHolder(ViewGroup viewGroup, Holder holder, int position) {
        holder.rootView.removeAllViews();
        Boolean issection = hasGroup(position);
        if (issection) {
            LightFormAdapterManager.ChildHolder catchViewHolder = null;
            catchViewHolder = getCatchView(holder);
            catchViewHolder = getGroupViewHolder(position, catchViewHolder, holder.rootView);
            holder.addTitleHolder(catchViewHolder);
        } else if (holder.titleView != null) {
            catchViews.add(holder.titleView);
            holder.titleView = null;
        }
        LightFormAdapterManager.ChildHolder contentViewHolder = null;
        contentViewHolder = holder.contentview;
        contentViewHolder = getContentViewHolder(position, contentViewHolder, holder.rootView);
        holder.addContentHOlder(contentViewHolder);
    }


    @Override
    public int getInstentType() {
        return instentType;
    }

    @Override
    public void setInstentType(int instentType) {
        this.instentType=instentType;
    }

    static class Holder extends LightFormAdapterManager.ChildHolder {
        LinearLayout rootView;
        LightFormAdapterManager.ChildHolder contentview, titleView;

        public Holder(LinearLayout view) {
            super(view);
            rootView = view;
        }

        public void addTitleHolder(LightFormAdapterManager.ChildHolder childHolder) {
            this.titleView = childHolder;
            rootView.addView(childHolder.itemView);
        }

        public void addContentHOlder(LightFormAdapterManager.ChildHolder childHolder) {
            this.contentview = childHolder;
            rootView.addView(childHolder.itemView);
        }
    }

    public C getContentViewHolder(int position, LightFormAdapterManager.ChildHolder childHolder, ViewGroup viewGroup) {
        if (childHolder == null) {
            childHolder = onCreateContentViewHolder(position, viewGroup);
            Log.d("qing","onCreateContentViewHolder");
        }
        onBindContentViewHolder((C)childHolder, position);
        return (C) childHolder;
    }

    public G getGroupViewHolder(int position, LightFormAdapterManager.ChildHolder childHolder, ViewGroup viewGroup) {
        if (childHolder == null) {
            childHolder = onCreateGroupViewHolder(position, viewGroup);
            Log.d("qing","onCreateGroupViewHolder");
//            childHolder.itemView.setOnClickListener(onClickListener);
        }
        onBindGroupViewHolder((G)childHolder, position);
        return (G) childHolder;
    }

    public abstract C onCreateContentViewHolder(int position, ViewGroup viewGroup);

    public abstract G onCreateGroupViewHolder(int position, ViewGroup viewGroup);

    public abstract void onBindContentViewHolder(C childHolder, int position) ;

    public abstract void onBindGroupViewHolder(G childHolder, int position) ;


    public abstract boolean hasGroup(int position);
}
