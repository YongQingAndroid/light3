package qing.com.kotlin3.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.posun.lightui.recyclerview.adapter.LightFormAdapterManager;
import com.posun.lightui.recyclerview.adapter.LightRecyclerGroupAdapter;

import qing.com.kotlin3.R;

/**
 * package light3:qing.com.kotlin3.test.TestGroupAdapter.class
 * 作者：zyq on 2018/1/26 10:41
 * 邮箱：zyq@posun.com
 */

public class TestGroupAdapter extends LightRecyclerGroupAdapter<TestGroupAdapter.GHolder,TestGroupAdapter.CHolder>{

    @Override
    public CHolder onCreateContentViewHolder(int position, ViewGroup viewGroup) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter_content, viewGroup,false);
        return new CHolder(view);
    }

    @Override
    public GHolder onCreateGroupViewHolder(int position, ViewGroup viewGroup) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter_title, viewGroup,false);
        return new GHolder(view);
    }

    @Override
    public void onBindContentViewHolder(CHolder childHolder, int position) {
        childHolder.textView.setText("item"+position);
    }

    @Override
    public void onBindGroupViewHolder(GHolder childHolder, int position) {
        childHolder.textView.setText("group"+position/5);
    }

    @Override
    public boolean hasGroup(int position) {
        return position%5==0;
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    static class CHolder extends LightFormAdapterManager.ChildHolder{
         TextView textView;
        public CHolder(View view) {
            super(view);
            textView=view.findViewById(R.id.text_adapter_content_name);
        }
    }
    static class GHolder extends LightFormAdapterManager.ChildHolder{
        TextView textView;
        public GHolder(View view) {
            super(view);
            textView=view.findViewById(R.id.name);
        }
    }
}
