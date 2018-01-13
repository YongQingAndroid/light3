package com.qing.lighthttp;

import android.os.Looper;

/**
 * 线程切换工具
 * package Kotlin3:com.posun.lightui.http.LightHandlerThread.class
 * 作者：zyq on 2017/12/6 10:11
 * 邮箱：zyq@posun.com
 */

public class LightHandlerThread {
    /**
     * 转递数据到Ui线程
     *
     * @param msg  传递的数据
     * @param call 回掉
     * @param <T>
     */
    public static <T> void postMsgToMainThread(final T msg, final ThreadCall<T> call) {
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                call.call(msg);
            }
        });
    }

    /**
     * 回調接口
     *
     * @param <M>
     */
    public interface ThreadCall<M> {
        /**
         * @param obj
         */
        void call(M obj);
    }
}
