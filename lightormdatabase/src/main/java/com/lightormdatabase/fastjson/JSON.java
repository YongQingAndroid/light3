package com.lightormdatabase.fastjson;

import com.lightormdatabase.log.LightLog;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * <注意！！不能再使用途中去掉或增加fastJson这会造成之前序列化的数据无法正确的从数据库中反序列化出来/>
 * 如果前期没有引入fastJson需要DisableFastJson改为true
 * 默认推荐使用fastJson实现序列化二进制（为了框架轻量化并没有继承fastJson）
 * 自动探测当前工程是否引用fastJson环境（缓存fastJson相关对象以及方法避免因为反射造成耗时）
 * 当前反射类允许线程并发
 * package kotlinTest:com.lightormdatabase.fastjson.JSON.class
 * 作者：zyq on 2017/8/23 15:25
 * 邮箱：zyq@posun.com
 */
public class JSON {
    private static Class JSON;
    private static boolean isfirst = true;
    public static boolean DisableFastJson = false;
    private static Map<String, Object> JSONcatch = new HashMap<>();
    private static Map<String, Method> methodMap = new HashMap<>();

    /**
     * 获取fastJson对象
     */
    public static Class getFastJson() {
        try {
            if (DisableFastJson)
                return null;
            if (isfirst && JSON == null) {
                isfirst = false;
                JSON = Class.forName("com.alibaba.fastjson.JSON");
            }
            return JSON;
        } catch (Exception e) {
            LightLog.E(e.toString());
        }
        return null;
    }

    /**
     * 反序列化
     */
    public static <T> T parseObject(byte[] bytes, Type clazz) {
        if (getFastJson() == null)
            return null;
        try {
            Object arrayFeature = getArrayObj("com.alibaba.fastjson.parser.Feature");
            Class Features = arrayFeature.getClass();
            return (T) getMethod("parseObject", new Class[]{byte[].class, Type.class, Features}).invoke(null, bytes, clazz, arrayFeature);
        } catch (Exception e) {
            LightLog.E(e.toString());
        }
        return null;
    }

    /**
     * 反射获取数组对象
     */
    public static Object getArrayObj(String classname) throws Exception {
        if (JSONcatch.containsKey(classname)) {
            return JSONcatch.get(classname);
        }
        Object obj = Array.newInstance(Class.forName(classname), 0);
        JSONcatch.put(classname, obj);
        return obj;
    }

    /**
     * 获取方法
     */
    private static Method getMethod(String name, Class[] classes) throws Exception {
        if (methodMap.containsKey(name)) {
            return methodMap.get(name);
        }
        Method method = getFastJson().getMethod(name,classes);
        methodMap.put(name, method);
        return method;
    }

    /**
     * 序列化
     */
    public static byte[] toJSONBytes(Object object) {
        if (getFastJson() == null)
            return null;
        try {
            Object arrayFeature = getArrayObj("com.alibaba.fastjson.serializer.SerializerFeature");
            Class Features = arrayFeature.getClass();
            return (byte[]) getMethod("toJSONBytes", new Class[]{Object.class, Features}).invoke(null, object, arrayFeature);
        } catch (Exception e) {
            LightLog.E(e.toString());
        }
        return null;
    }
}
