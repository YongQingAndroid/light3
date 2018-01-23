package qing.com.kotlin3.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.posun.lightui.listview.LightListViewGroupAdapter;

import qing.com.kotlin3.R;

/**
 * package light3:qing.com.kotlin3.test.TestLightSectionAdapter.class
 * 作者：zyq on 2018/1/23 13:35
 * 邮箱：zyq@posun.com
 */

public class TestLightSectionAdapter extends LightListViewGroupAdapter {
    @Override
    public View getContentView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter_content, null);
        }
        ((TextView) view.findViewById(R.id.text_adapter_content_name)).setText("" + position);
        return view;
    }

    @Override
    public View getGroupView(int position, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter_title, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();

        }
        holder.name.setText("group=" + position / 5);

        return view;
    }

    class Holder {
        private TextView name;

        Holder(View view) {
            name = view.findViewById(R.id.name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), name.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean hasGroup(int position) {
        return position % 5 == 0;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
}
