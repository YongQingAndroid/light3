package qing.com.kotlin3.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.posun.lightui.recyclerview.LightFormAdapterManager;

import qing.com.kotlin3.R;

/**
 * package Kotlin3:qing.com.kotlin3.test.TestListAdapter.class
 * 作者：zyq on 2018/1/18 11:15
 * 邮箱：zyq@posun.com
 */

public class TestGradAdapter extends LightFormAdapterManager.LightGridAdapter<TestGradAdapter.TestHolder> {
    int cont = 5;

    public TestGradAdapter(int spanCount) {
        super(spanCount);
    }

    public void setCont(int cont) {
        this.cont = cont;
    }

    public int getCont() {
        return cont;
    }

    @Override
    public int getItemCount() {
        return cont;
    }

    @Override
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public TestHolder getView(ViewGroup viewGroup,int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new TestHolder(view);
    }

    @Override
    public void bindView(ViewGroup viewGroup, TestHolder holder, int position) {
        holder.textView.setText(position + "");
    }


    class TestHolder extends LightFormAdapterManager.ChildHolder {
        TextView textView;

        public TestHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text_item);
        }
    }


}
