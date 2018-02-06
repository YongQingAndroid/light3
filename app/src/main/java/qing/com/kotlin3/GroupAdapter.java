package qing.com.kotlin3;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.posun.lightui.recyclerview.LightGroupRecycler;


/**
 * package light3:qing.com.kotlin3.GroupAdapter.class
 * 作者：zyq on 2018/2/2 15:05
 * 邮箱：zyq@posun.com
 */

public class GroupAdapter extends RecyclerView.Adapter implements LightGroupRecycler.LightGroupAdapter {
    boolean test = false;
    String tag = "--->Item";

    public void setTest() {
        if (tag.equals("")) {
            tag = "--->Item";
        } else {
            tag = "";
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Holder) holder).textView.setText(position + tag);
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    @Override
    public boolean isGroup(int position) {
        return position % 5 == 0;
    }

    @Override
    public LightGroupRecycler.GroupHolder creatGroupHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_adapter_title, viewGroup, false);
        Log.i("creatGroupHolder", "creatGroupHolder");
        return new GroupHolder(view);
    }


    //
    @Override
    public void onBindGroupHolder(LightGroupRecycler.GroupHolder holder, int position) {
        ((GroupHolder) holder).textView.setText("group" + position + tag);
    }


    class GroupHolder extends LightGroupRecycler.GroupHolder {
        TextView textView;

        public GroupHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), textView.getText() + "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView textView;

        public Holder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_item);

        }
    }
}
