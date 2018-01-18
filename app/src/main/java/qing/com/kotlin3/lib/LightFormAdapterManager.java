package qing.com.kotlin3.lib;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * package Kotlin3:qing.com.kotlin3.lib.LightFormAdapterManager.class
 * 作者：zyq on 2018/1/18 09:28
 * 邮箱：zyq@posun.com
 */

public class LightFormAdapterManager extends RecyclerView.Adapter<LightFormAdapterManager.Holder> {
    private List<LightFormBaseAdapterInterface> data = new ArrayList<>();
    private Map<LightFormBaseAdapterInterface, Integer> t_size = new HashMap<>();
    private AtomicInteger atomicInteger = new AtomicInteger(50);
    private ViewGroup viewGroup;
    private int spanCount = 3;

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
        for (LightFormBaseAdapterInterface mLightFormBaseAdapter : data) {
            if (mLightFormBaseAdapter.getInstentType() == viewType) {
                item = mLightFormBaseAdapter;
            }
        }
        if (viewGroup == null)
            viewGroup = parent;
        ChildHolder childHolder = item.getView(parent).addOwerFormAdapterManager(this);
        return new Holder(childHolder, item);
    }

    /**
     * 添加子适配器
     *
     * @param adapter 添加的子适配器LightGridAdapter，LightFixedAdapter ，LightListAdapter
     * @return当前对象
     */
    public LightFormAdapterManager addAdapter(LightFormBaseAdapterInterface adapter) {
        adapter.setInstentType(atomicInteger.addAndGet(1));
        if (adapter instanceof LightGridAdapter) {
            LightGridAdapter mLightGridAdapter = (LightGridAdapter) adapter;
            LightFormAdapterManager.this.spanCount = mLightGridAdapter.spanCount;
        }
        data.add(adapter);
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
        LightGridLayoutManager mLightGridLayoutManager = new LightGridLayoutManager(recyclerView.getContext(), LightFormAdapterManager.this.spanCount);
        recyclerView.setLayoutManager(mLightGridLayoutManager);
        recyclerView.setAdapter(this);
        mLightGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                LightFormBaseAdapterInterface mLightFormBaseAdapterInterface = getAdapterByPosition(position);
                if (mLightFormBaseAdapterInterface instanceof LightGridAdapter) {
                    return 1;
                } else if (mLightFormBaseAdapterInterface instanceof LightListAdapter) {
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
                        if (vh.myLightFormBaseAdapter instanceof LightFixedAdapter) {
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
        return item.getInstentType();
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
        holder.myLightFormBaseAdapter.bindView(viewGroup, holder.childHolder, arg);
    }

    /**
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
    private interface LightFormBaseAdapterInterface<H extends ChildHolder> {

        OnItemCilckListener getItemCilckListener();

        /**
         * @return获取数量
         */
        int getItemCount();

        /**
         * @param position
         * @return获取视图类型（暂时没有实现这个功能）
         */
        @Deprecated
        int getViewType(int position);

        /***
         * 创建视图
         * @param viewGroup
         * @return视图的ChildHolder对象
         */
        H getView(ViewGroup viewGroup);

        /**
         * 绑定视图数据
         *
         * @param viewGroup
         * @param holder
         * @param position
         */
        void bindView(ViewGroup viewGroup, H holder, int position);

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
    public static abstract class LightListAdapter<H extends ChildHolder> implements LightFormBaseAdapterInterface<H> {
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
    public static abstract class LightGridAdapter<H extends ChildHolder> implements LightFormBaseAdapterInterface<H> {

        protected int instentType = -1;
        protected int spanCount = 4;
        protected OnItemCilckListener itemCilckListener;

        @Override
        public OnItemCilckListener getItemCilckListener() {
            return itemCilckListener;
        }

        public void setOnItemCilckListener(OnItemCilckListener itemCilckListener) {
            this.itemCilckListener = itemCilckListener;
        }

        public LightGridAdapter(int spanCount) {
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
    public static class LightFixedAdapter implements LightFormBaseAdapterInterface {
        private ChildHolder contentView;

        @Override
        public OnItemCilckListener getItemCilckListener() {
            return null;
        }

        public LightFixedAdapter(View view) {
            this.contentView = new ChildHolder(view);
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
        public ChildHolder getView(ViewGroup viewGroup) {
            return contentView;
        }


        @Override
        public void bindView(ViewGroup viewGroup, ChildHolder holder, int position) {

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
    public final void NotifyDataSetChangedAll() {
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
