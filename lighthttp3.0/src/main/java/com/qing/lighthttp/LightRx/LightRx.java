package com.qing.lighthttp.LightRx;

/**
 * package Kotlin3:com.qing.lighthttp.LightRx.LightRx.class
 * 作者：zyq on 2017/12/21 16:29
 * 邮箱：zyq@posun.com
 */

public class LightRx {
    public static void doSomeThing(call call){
    }
    interface call<K>{
       <Q>Q execute(K obj);
    }


}
