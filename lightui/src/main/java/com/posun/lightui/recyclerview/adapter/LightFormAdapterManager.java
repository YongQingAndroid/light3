package com.posun.lightui.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.posun.lightui.recyclerview.event.LightOnItemTouchListener;
import com.posun.lightui.recyclerview.android.LightGridLayoutManager;
import com.posun.lightui.recyclerview.lightdefult.LightDefultLayoutManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @丰富RecyclerView视图处理（对应多个适配器）<!注意增加迭代不限深度嵌套使用></>
 * @example&LightFormAdapterManager.addAdapter(childAdapter) LightFormAdapterManager.setRecyclerView(RecyclerView)注意需要放在addAdapter后面使用
 * @class&childAdapter：LightFixedAdapter(固定视图适配器直接实例化使用)(该模式下兼容嵌套RecyclerView横向滚动以及View视图复用) childAdapter.addAdapter(childAdapter)
 * childAdapter：LightGridAdapter表格适配器（抽象类需要继承重写），LightListAdapter列表适配器抽象类需要继承重写
 * @刷新UI LightFormAdapterManager.notifyAllChildItem();刷新全部
 * @局部刷新(伴有动画不兼容V26版本) LightFormAdapterManager.notifyChildItemRangeInserted(&,&, childAdapter);
 * LightFormAdapterManager.notifyChildItemRangeChanged(&,&,childAdapter);
 * LightFormAdapterManager.notifyChildItemRangeRemoved(&,&,childAdapter);
 * package Kotlin3:qing.com.kotlin3.lib.LightFormAdapterManager.class
 * 作者：zyq on 2018/1/18 09:28
 * 邮箱：zyq@posun.com
 */

public class LightFormAdapterManager extends RecyclerView.Adapter<LightFormAdapterManager.Holder> {
    private List<LightFormBaseAdapterInterface> data = new ArrayList<>();
    private Map<LightFormBaseAdapterInterface, Integer> t_size = new HashMap<>();
    //    private List<LightFormAdapterManager> listAdapterManager;
    private AtomicInteger atomicInteger = new AtomicInteger(50);
    private ViewGroup viewGroup;
    private int spanCount = 1;
    private Context context;

    public LightFormAdapterManager(Context context) {
        this.context = context;
    }

    /**
     * 每一种子适配器都是一个类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LightFormBaseAdapterInterface item = null;
        Log.e("LightFormAdapterManager", "onCreateViewHolder");
        String stringViewType = String.valueOf(viewType);
        int resultType = 0;
        for (LightFormBaseAdapterInterface mLightFormBaseAdapter : data) {
            String valueType = String.valueOf(mLightFormBaseAdapter.getInstentType());
            if (stringViewType.startsWith(valueType)) {
                item = mLightFormBaseAdapter;
                /**
                 * 获取子视图类型
                 */
                resultType = Integer.parseInt(stringViewType.substring(valueType.length()));
            }
        }
        if (viewGroup == null)
            viewGroup = parent;
        ChildHolder childHolder = item.onCreateViewHolder(parent, item.getViewType(resultType)).addOwerFormAdapterManager(this);
        return new Holder(childHolder, item);
    }

    /**
     * 添加子适配器
     *
     * @param adapter 添加的子适配器LightGridAdapter，LightRecyclerFixedAdapter ，LightRecyclerListAdapter
     * @return当前对象
     */
    public LightFormAdapterManager addAdapter(LightFormBaseAdapterInterface adapter) {
        adapter.setInstentType(atomicInteger.addAndGet(1));
        if (adapter instanceof LightRecyclerGridAdapter) {
            LightRecyclerGridAdapter mLightGridAdapter = (LightRecyclerGridAdapter) adapter;
            if (data.size() > 0 && data.get(data.size() - 1) instanceof LightRecyclerGridAdapter) {
                data.add(new LightRecyclerFixedAdapter(new View(context)));
            }
            if (LightFormAdapterManager.this.spanCount % mLightGridAdapter.spanCount != 0)
                LightFormAdapterManager.this.spanCount = LightFormAdapterManager.this.spanCount * mLightGridAdapter.spanCount;
        }
        data.add(adapter);
        return this;
    }

    /**
     * 多层递归使用（试验功能慎用 使用场景超过2层多层List嵌套使用）
     *
     * @param adapterManager
     * @return
     */
    public LightFormAdapterManager addAdapterManager(LightFormAdapterManager adapterManager) {
//        if (listAdapterManager == null)
//            listAdapterManager = new ArrayList<>();
//        listAdapterManager.add(adapterManager);
        return this;
    }

    /**
     * 绑定recyclerView 必须在添加自适配器之后才能绑定
     *
     * @param recyclerView
     */
    public void setRecyclerView(RecyclerView recyclerView) {
        if (data.size() == 0)
            throw new RuntimeException("必选先添加子适配器");
        LightDefultLayoutManager mLightGridLayoutManager = new LightDefultLayoutManager(recyclerView.getContext(), LightFormAdapterManager.this.spanCount);
//        LightGridLayoutManager mLightGridLayoutManager = new LightGridLayoutManager(recyclerView.getContext(), LightFormAdapterManager.this.spanCount);

        recyclerView.setLayoutManager(mLightGridLayoutManager);
        recyclerView.setAdapter(this);
//        mLightGridLayoutManager.setSpanSizeLookup(new LightGridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                LightFormBaseAdapterInterface mLightFormBaseAdapterInterface = getAdapterByPosition(position);
//                if (mLightFormBaseAdapterInterface instanceof LightRecyclerGridAdapter) {
//                    return LightFormAdapterManager.this.spanCount / ((LightRecyclerGridAdapter) mLightFormBaseAdapterInterface).spanCount;
//                } else if (mLightFormBaseAdapterInterface instanceof LightRecyclerListAdapter) {
//                    return LightFormAdapterManager.this.spanCount;
//                } else {
//                    return LightFormAdapterManager.this.spanCount;
//                }
//            }
//        });
        mLightGridLayoutManager.setSpanSizeLookup(new LightDefultLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                LightFormBaseAdapterInterface mLightFormBaseAdapterInterface = getAdapterByPosition(position);
                if (mLightFormBaseAdapterInterface instanceof LightRecyclerGridAdapter) {
                    return LightFormAdapterManager.this.spanCount / ((LightRecyclerGridAdapter) mLightFormBaseAdapterInterface).spanCount;
                } else if (mLightFormBaseAdapterInterface instanceof LightRecyclerListAdapter) {
                    return LightFormAdapterManager.this.spanCount;
                } else {
                    return LightFormAdapterManager.this.spanCount;
                }
            }
        });
        recyclerView.addOnItemTouchListener(
                new LightOnItemTouchListener(recyclerView, new LightOnItemTouchListener.OnItemTouchListener<Holder>() {
                    @Override
                    public void onItemClick(Holder vh) {
                        if (vh.myLightFormBaseAdapter instanceof LightRecyclerFixedAdapter) {
                            return;
                        }
                        int arg = getChildAdapterPosition(vh.getAdapterPosition());
                        if (vh.myLightFormBaseAdapter.getItemCilckListener() != null) {
                            vh.myLightFormBaseAdapter.getItemCilckListener().onItemClick(arg);
                        }
                    }

                    @Override
                    public void onLongItemClick(Holder vh) {

                    }
                }
                ));
//        GridDivider mDividerItemDecoration=new GridDivider(context,2, Color.BLUE);
//        recyclerView.addItemDecoration(mDividerItemDecoration);
    }

    /**
     * @param position
     * @return根据列表的位置计算子适配器的位置
     */
    private int getChildAdapterPosition(int position) {
        int size = 0;
        for (LightFormBaseAdapterInterface mLightFormBaseAdapter : data) {
            int itemSize = t_size.get(mLightFormBaseAdapter);
            if (size <= position && (size + itemSize) > position) {
                return position - size;
            }
            size += itemSize;
        }
        return 0;
    }

    /**
     * @param position
     * @return适配器的视图类型
     */
    @Override
    public int getItemViewType(int position) {
        LightFormBaseAdapterInterface item = getAdapterByPosition(position);
        if (item.getInstentType() == -1) {
            item.setInstentType(atomicInteger.addAndGet(1));
        }
        /**
         * 当前的子适配器实例化类型和子视图的类型做合并
         */
        String type = item.getInstentType() + String.valueOf(item.getViewType(position));
        return Integer.parseInt(type);
    }

    /***
     * @param position
     * @return 获取对应的子适配器
     */
    private LightFormBaseAdapterInterface getAdapterByPosition(int position) {
        int size = 0;
        for (LightFormBaseAdapterInterface mLightFormBaseAdapter : data) {
            int itemSize = t_size.get(mLightFormBaseAdapter);
            if (size <= position && (size + itemSize) > position) {
                return mLightFormBaseAdapter;
            }
            size += itemSize;
        }
        return data.get(data.size() - 1);
    }

    /***
     * @param position
     * @return获取对应的真实位置
     */
    private int getTruePositionByChildIndex(int position, LightFormBaseAdapterInterface adapter) {
        int size = 0;
        for (LightFormBaseAdapterInterface mLightFormBaseAdapter : data) {
            if (adapter == mLightFormBaseAdapter) {
                return position + size;
            } else {
                size += t_size.get(mLightFormBaseAdapter);
            }
        }
        return 0;
    }

    /***
     * 调用子适配器进行数据绑定
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        int arg = getChildAdapterPosition(position);
        holder.myLightFormBaseAdapter.onBindViewHolder(viewGroup, holder.childHolder, arg);
    }

    /**
     * 此处不能使用map中的缓存需要重新刷新
     *
     * @return根据子适配器算出视图总数
     */
    @Override
    public int getItemCount() {
        int size = 0;
        for (LightFormBaseAdapterInterface item : data) {
            int itemsize = item.getItemCount();
            size += itemsize;
            t_size.put(item, itemsize);
        }
//        if (listAdapterManager != null && listAdapterManager.size() > 0) {
//            for (LightFormAdapterManager itemManager : listAdapterManager) {//此处涉及循环递归
//                size += itemManager.getItemCount();
//            }
//        }
        return size;
    }

    /**
     * Holder
     */
    static class Holder extends RecyclerView.ViewHolder {
        LightFormBaseAdapterInterface myLightFormBaseAdapter;
        ChildHolder childHolder;

        public Holder(ChildHolder childHolder, LightFormBaseAdapterInterface myLightFormBaseAdapter) {
            super(childHolder.itemView);
            this.childHolder = childHolder;
            childHolder.addOwerHolder(this);
            this.myLightFormBaseAdapter = myLightFormBaseAdapter;
        }
    }

    /***
     * 子Holder
     */
    public static class ChildHolder {
        protected View itemView;
        private int position = 0;
        private WeakReference<Holder> mOwnerHolder;
        private WeakReference<LightFormAdapterManager> mOwnerFormAdapterManager;

        public View getItemView() {
            return itemView;
        }

        public ChildHolder(View view) {
            this.itemView = view;
        }

        private ChildHolder addOwerHolder(Holder mholder) {
            mOwnerHolder = new WeakReference<Holder>(mholder);
            return this;
        }

        private ChildHolder addOwerFormAdapterManager(LightFormAdapterManager lightFormAdapterManager) {
            mOwnerFormAdapterManager = new WeakReference<LightFormAdapterManager>(lightFormAdapterManager);
            return this;
        }

        /**
         * @return获取当前视图的position
         */
        public int getAdapterPosition() {
            if (mOwnerHolder != null && mOwnerHolder.get() != null && mOwnerFormAdapterManager != null && mOwnerFormAdapterManager.get() != null) {
                return mOwnerFormAdapterManager.get().getChildAdapterPosition(mOwnerHolder.get().getAdapterPosition());
            }
            return position;
        }
    }

    /**
     * 自适配器接口
     */
    public interface LightFormBaseAdapterInterface<H extends ChildHolder> {

        OnItemCilckListener getItemCilckListener();

        /**
         * @return获取数量
         */
        int getItemCount();

        /**
         * 过多的视图类型将会影响滑动性能
         *
         * @param position
         * @return获取视图类型
         */
        int getViewType(int position);

        /***
         * 创建视图
         * @param viewGroup
         * @return视图的ChildHolder对象
         */
        H onCreateViewHolder(ViewGroup viewGroup, int viewType);

        /**
         * 绑定视图数据
         *
         * @param viewGroup
         * @param holder
         * @param position
         */
        void onBindViewHolder(ViewGroup viewGroup, H holder, int position);

        /**
         * @return获取子适配器的实例化类型
         */
        int getInstentType();

        /**
         * 设置子适配器的实例化类型
         *
         * @param instentType
         */
        void setInstentType(int instentType);
    }

    /***
     * 列表子视图适配器
     */
    public static abstract class LightRecyclerListAdapter<H extends ChildHolder> implements LightFormBaseAdapterInterface<H> {
        protected int instentType = -1;
        protected OnItemCilckListener itemCilckListener;

        public void setOnItemCilckListener(OnItemCilckListener itemCilckListener) {
            this.itemCilckListener = itemCilckListener;
        }

        @Override
        public OnItemCilckListener getItemCilckListener() {
            return itemCilckListener;
        }

        @Override
        public int getInstentType() {
            return instentType;
        }

        @Override
        public void setInstentType(int instentType) {
            this.instentType = instentType;
        }
    }

    /***
     * 表格子视图适配器
     */
    public static abstract class LightRecyclerGridAdapter<H extends ChildHolder> implements LightFormBaseAdapterInterface<H> {

        protected int instentType = -1;
        protected int spanCount = 1;
        protected OnItemCilckListener itemCilckListener;

        @Override
        public OnItemCilckListener getItemCilckListener() {
            return itemCilckListener;
        }

        public void setOnItemCilckListener(OnItemCilckListener itemCilckListener) {
            this.itemCilckListener = itemCilckListener;
        }

        public LightRecyclerGridAdapter(int spanCount) {
            this.spanCount = spanCount;
        }

        @Override
        public int getInstentType() {
            return instentType;
        }

        @Override
        public void setInstentType(int instentType) {
            this.instentType = instentType;
        }
    }

    /***
     * 固定表单布局适配器
     */
    public static class LightRecyclerFixedAdapter implements LightFormBaseAdapterInterface {
        protected ChildHolder contentHolder;

        public ChildHolder getContentHolder() {
            return contentHolder;
        }

        @Override
        public OnItemCilckListener getItemCilckListener() {
            return null;
        }

        public LightRecyclerFixedAdapter(View view) {
            this.contentHolder = new ChildHolder(view);
        }

        protected int instentType = -1;

        @Override
        public int getItemCount() {
            return 1;
        }

        @Override
        public int getViewType(int position) {
            return 0;
        }

        @Override
        public ChildHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return contentHolder;
        }


        @Override
        public void onBindViewHolder(ViewGroup viewGroup, ChildHolder holder, int position) {

        }

        @Override
        public int getInstentType() {
            return instentType;
        }

        @Override
        public void setInstentType(int instentType) {
            this.instentType = instentType;
        }
    }

    /**
     * 刷新全部视图
     */
    public final void notifyAllChildItem() {
        this.notifyDataSetChanged();
    }

    /**
     * 局部刷新伴有动画效果
     *
     * @param position         需要局部刷新的位置（子适配器中的位置）
     * @param itemCount        局部刷新影响的数量
     * @param adapterInterface 对应子适配器的实例
     */
    public final void notifyChildItemRangeChanged(int position, int itemCount, LightFormBaseAdapterInterface adapterInterface) {
        t_size.put(adapterInterface, adapterInterface.getItemCount());
        this.notifyItemRangeChanged(getTruePositionByChildIndex(position, adapterInterface), itemCount);
    }

    /**
     * 局部刷新伴有动画效果
     *
     * @param positionStart    需要局部刷新的位置（子适配器中的位置）
     * @param itemCount        局部刷新影响的数量
     * @param adapterInterface 对应子适配器的实例
     */
    public final void notifyChildItemRangeInserted(int positionStart, int itemCount, LightFormBaseAdapterInterface adapterInterface) {
        t_size.put(adapterInterface, adapterInterface.getItemCount());
        this.notifyItemRangeInserted(getTruePositionByChildIndex(positionStart, adapterInterface), itemCount);
    }

    /**
     * 局部刷新伴有动画效果
     *
     * @param positionStart    需要局部刷新的位置（子适配器中的位置）
     * @param itemCount        局部刷新影响的数量
     * @param adapterInterface 对应子适配器的实例
     */
    public final void notifyChildItemRangeRemoved(int positionStart, int itemCount, LightFormBaseAdapterInterface adapterInterface) {
        t_size.put(adapterInterface, adapterInterface.getItemCount());
        this.notifyItemRangeRemoved(getTruePositionByChildIndex(positionStart, adapterInterface), itemCount);
    }

    public interface OnItemCilckListener {
        void onItemClick(int position);

        void onLongItemClick(int position);
    }
}
