package qing.com.kotlin3.test;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by qing on 2018/1/26.
 */

public class MyArrayList<E> extends ArrayList<E> {
    @Override
    public boolean add(E e) {
        Log.e("qing", "MyArrayList");
        return super.add(e);
    }
}
