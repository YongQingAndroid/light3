package qing.com.kotlin3.test;

import android.view.View;
import android.view.ViewGroup;

import com.posun.lightui.listview.LightListViewGroupAdapter;

/**
 * package light3:qing.com.kotlin3.test.TestGroupListAdapter.class
 * 作者：zyq on 2018/1/23 14:41
 * 邮箱：zyq@posun.com
 */

public class TestGroupListAdapter extends LightListViewGroupAdapter {
    @Override
    public View getContentView(int position, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getGroupView(int position, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean hasGroup(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
}
