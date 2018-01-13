package com.qing.lighthttp;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Response;

/**
 * package Kotlin3:com.qing.lighthttp.LightFastJsonCover.class
 * 作者：zyq on 2017/12/6 14:12
 * 邮箱：zyq@posun.com
 */

public class LightFastJsonCover implements LightResultCover {
    /**
     * @param response
     * @param type
     * @param extend
     * @param <T>
     * @return
     */
    @Override
    public <T> T just(Response response, Type type, Map<String, Object> extend) {
        try {
            if (type == String.class)
                return (T) response.body().string();
            return JSON.parseObject(response.body().bytes(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
