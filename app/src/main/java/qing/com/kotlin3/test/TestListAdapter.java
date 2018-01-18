package qing.com.kotlin3.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import qing.com.kotlin3.R;
import qing.com.kotlin3.lib.LightFormAdapterManager;

/**
 * package Kotlin3:qing.com.kotlin3.test.TestListAdapter.class
 * 作者：zyq on 2018/1/18 11:15
 * 邮箱：zyq@posun.com
 */

public class TestListAdapter extends LightFormAdapterManager.LightListAdapter<TestListAdapter.TestHolder> {
    @Override
    public int getItemCount() {
        return 150;
    }

    @Override
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public TestHolder getView(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
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
            textView = view.findViewById(R.id.item_t);
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(view.getContext(), "TestHolderPosition=" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }
}
