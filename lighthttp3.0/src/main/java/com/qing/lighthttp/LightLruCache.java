package com.qing.lighthttp;

import android.util.LruCache;

/**
 * package Kotlin3:com.qing.lighthttp.LightLurCath.class
 * 作者：zyq on 2017/12/8 11:41
 * 邮箱：zyq@posun.com
 */

public class LightLruCache<k,v> extends LruCache<k,v> {
    public LightLruCache(int maxSize) {
        super(maxSize);
    }
    public boolean containsKey(k key){
        Object obj=get(key);
        return obj!=null;
    }
}
