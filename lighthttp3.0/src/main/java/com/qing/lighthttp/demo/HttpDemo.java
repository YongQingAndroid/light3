package com.qing.lighthttp.demo;

import android.util.Log;

import com.qing.lighthttp.LightFastJsonCover;
import com.qing.lighthttp.LightHandlerThread;
import com.qing.lighthttp.LightHttp;
import com.qing.lighthttp.lightHttpCustomResult;
import com.qing.lighthttp.lightHttpErr;
import com.qing.lighthttp.lightHttpResult;
import com.qing.lighthttp.lightHttpSteamResult;
import com.qing.lighthttp.lightUIThread;
import com.qing.lighthttp.lightWorkThread;

import java.io.File;

/**
 * package Kotlin3:com.posun.lightui.http.HttpDemo.class
 * 作者：zyq on 2017/12/5 15:01
 * 邮箱：zyq@posun.com
 */

public class HttpDemo {
    public void oncreat() {
        LightHttp.getinstent(this).setBaseURl("http://").addHeader("key", "value").urlPath("{id}", "111111").get("/s/test/{id}/demo.php");
//        LightHttp.getinstent(this).post("/s/test/{id}/demo.php", null);
    }

    @lightWorkThread
    @lightHttpResult("/s/test/{id}/demo.php")
    public void getname1(String arg) {
        Log.i("qing", arg);
    }

    @lightWorkThread
    @lightHttpSteamResult
    @lightHttpResult("/s/demo.php")
    public void getFile(File[] arg) {
    }

    @lightWorkThread
    @lightHttpCustomResult(LightFastJsonCover.class)
    @lightHttpResult("/s/demo.php")
    public void getrsulut(File[] arg) {
    }

    @lightUIThread
    @lightHttpErr
    public void err(Throwable throwable) {

    }

    public void test() {
        LightHandlerThread.postMsgToMainThread(123, new LightHandlerThread.ThreadCall<Integer>() {
            @Override
            public void call(Integer obj) {

            }
        });
    }
}
