package com.lightormdatabase.tools;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package Kotlin3:com.lightormdatabase.tools.SingleManager.class
 * 作者：zyq on 2017/11/27 16:27
 * 邮箱：zyq@posun.com
 */

public class SingleManager {
    private static Map<Class, Object> maps;
//    public static synchronized <T> T getInstance(Class<T> clazz, Object... intArgs) {
//        if (maps == null)
//            maps = new HashMap<>();
//        if (maps.containsKey(clazz)) {
//            return (T) maps.get(clazz);
//        }
//        Constructor[] cons = clazz.getDeclaredConstructors();
//        if (cons != null && cons.length > 0) {
//            try {
//                Object obj = null;
//                if (intArgs == null || intArgs.length == 0) {
//                    obj = clazz.newInstance();
//                } else {
//                    Constructor constructor = getConstructor(cons, intArgs);
//                    constructor.setAccessible(true);
//                    obj = constructor.newInstance(intArgs);
//                }
//                maps.put(clazz, obj);
//                return (T) obj;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
    public static synchronized <T> T getInstance(Class<T> clazz, Object... intArgs) {
        if (maps == null)
            maps = new HashMap<>();
        if (maps.containsKey(clazz)) {
            return (T) maps.get(clazz);
        }
        try {
            List<Class> classList = new ArrayList<>();
            if (intArgs != null && intArgs.length > 0) {
                for (Object item : intArgs) {
                    classList.add(item.getClass());
                }
            }
            Object obj = null;
            if (intArgs == null || intArgs.length == 0) {
                obj = clazz.newInstance();
            } else {
                Constructor constructor = clazz.getDeclaredConstructor(classList.toArray(new Class[]{}));
                constructor.setAccessible(true);
                obj = constructor.newInstance(intArgs);
            }
            maps.put(clazz, obj);
            return (T) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取相同类型的构造器
     *
     * @param cons
     * @param intArgs
     * @return
     */
    private Constructor getConstructor(Constructor[] cons, Object[] intArgs) {
        Constructor constructor = null;
        for (Constructor item : cons) {
            if (issameConstructor(item, intArgs)) {
                constructor = item;
            }
        }
        return constructor;
    }

    /**
     * 判断是否為同一個構造器
     *
     * @param item
     * @param intArgs
     * @return
     */
    private boolean issameConstructor(Constructor item, Object[] intArgs) {
        Class[] calssm = item.getParameterTypes();
        if (calssm.length != intArgs.length)
            return false;
        for (int i = 0; i < calssm.length; i++) {
            if (calssm[i] != intArgs[i])
                return false;
        }
        return true;
    }

    public static void clear() {
        maps.clear();
    }

    public static void ramoveCatch(Class clazz) {
        if (maps.containsKey(clazz))
            maps.remove(clazz);
    }
}
