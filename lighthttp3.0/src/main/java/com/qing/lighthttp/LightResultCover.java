package com.qing.lighthttp;

import java.lang.reflect.Type;
import java.util.Map;
import okhttp3.Response;

/**
 * package Kotlin3:com.qing.lighthttp.LightResultCover.class
 * 作者：zyq on 2017/12/6 14:08
 * 邮箱：zyq@posun.com
 */

/**
 * 网络回调解析接口
 */
public interface LightResultCover {
    /**
     * 网络回调方法
     * @param response
     * @param type
     * @param extend
     * @param <T>
     * @return
     */
    <T> T just(Response response, Type type, Map<String, Object> extend);
}
