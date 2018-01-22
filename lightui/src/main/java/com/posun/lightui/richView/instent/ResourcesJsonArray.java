package com.posun.lightui.richView.instent;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * package light3:com.posun.lightui.richView.instent.ResourcesJsonArray.class
 * 作者：zyq on 2018/1/22 16:59
 * 邮箱：zyq@posun.com
 */

public class ResourcesJsonArray extends JSONArray {
    public ResourcesJsonArray(String value) throws JSONException {
       super(value);
    }
    public String[] toStringArray() throws JSONException {
        int size = length();
        String[] array = new String[size];
        for (int i = 0; i < size; i++) {
            array[i] = getString(i);
        }
        return array;
    }
}
