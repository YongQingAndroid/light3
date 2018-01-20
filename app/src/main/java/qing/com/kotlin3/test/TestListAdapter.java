package qing.com.kotlin3.test;

import android.graphics.Color;
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

public class TestListAdapter extends LightFormAdapterManager.LightListAdapter {
    @Override
    public int getItemCount() {
        return 150;
    }

    @Override
    public int getViewType(int position) {
        return position % 2;
    }

    @Override
    public LightFormAdapterManager.ChildHolder getView(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
        if (viewType == 1) {
            return new TestHolder(view);
        }
        return new TestHolder1(view);
    }

    @Override
    public void bindView(ViewGroup viewGroup, LightFormAdapterManager.ChildHolder holder, int position) {
        if (holder instanceof TestHolder) {
            ((TestHolder) holder).textView.setText("" + position);
        } else {
            ((TestHolder1) holder).textView.setText("" + position);
        }
    }


    class TestHolder extends LightFormAdapterManager.ChildHolder {
        TextView textView;

        public TestHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.item_t);
            textView.setTextColor(Color.BLACK);
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(view.getContext(), "TestHolderPosition=" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

    class TestHolder1 extends LightFormAdapterManager.ChildHolder {
        TextView textView;

        public TestHolder1(View view) {
            super(view);
            textView = view.findViewById(R.id.item_t);
            textView.setTextColor(Color.RED);
        }
    }
}
