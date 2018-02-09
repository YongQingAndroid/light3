package qing.com.kotlin3.test.defultmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.posun.lightui.recyclerview.adapter.LightFormAdapterManager;
import com.posun.lightui.recyclerview.lightdefult.LightGroupRecycler;

import qing.com.kotlin3.R;

/**
 * package light3:qing.com.kotlin3.test.defultmanager.TestDefultLayoutManagerGroupAdapter.class
 * 作者：zyq on 2018/2/9 13:04
 * 邮箱：zyq@posun.com
 */

public class TestDefultLayoutManagerGroupAdapter extends LightFormAdapterManager.LightRecyclerListAdapter<TestDefultLayoutManagerGroupAdapter.CHolder> implements LightGroupRecycler.LightGroupAdapter<TestDefultLayoutManagerGroupAdapter.GHolder> {
    @Override
    public int getItemCount() {
        return 50;
    }

    @Override
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public CHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new CHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewGroup viewGroup, CHolder holder, int position) {
        holder.textView.setText(position + "");
    }

    @Override
    public boolean isGroup(int position) {
        return position % 5 == 0;
    }

    @Override
    public GHolder creatGroupHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter_title, viewGroup, false);
        return new GHolder(view);
    }

    @Override
    public void onBindGroupHolder(GHolder holder, int position) {
        holder.textView.setText("group" + position);
    }

    static class CHolder extends LightFormAdapterManager.ChildHolder {
        TextView textView;

        public CHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text_item);
        }
    }

    static class GHolder extends LightGroupRecycler.GroupHolder {
        TextView textView;

        public GHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.name);
        }
    }
}
