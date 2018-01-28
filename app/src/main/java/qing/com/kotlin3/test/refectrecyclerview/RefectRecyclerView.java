package qing.com.kotlin3.test.refectrecyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by qing on 2018/1/28.
 */

public class RefectRecyclerView {
    static Map<String, Object> catch_map = new HashMap<>();

    public static boolean scrapIsRemoved(Object object) {
        try {
            String name = "isRemoved";
            Method method = catch_map.containsKey(name) ? (Method) catch_map.get(name) : RecyclerView.ViewHolder.class.getDeclaredMethod(name, new Class[]{});
            if (!method.isAccessible())
                method.setAccessible(true);
            return (Boolean) method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasFlexibleChildInBothOrientations(Object object) {
        try {
            Method method = RecyclerView.ViewHolder.class.getDeclaredMethod("hasFlexibleChildInBothOrientations", new Class[]{});
            if (!method.isAccessible())
                method.setAccessible(true);
            return (Boolean) method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Rect getmDecorInsets(Object object) {
        try {
            String name = "mDecorInsets";
            Field field = catch_map.containsKey(name) ? (Field) catch_map.get(name) : RecyclerView.LayoutParams.class.getDeclaredField(name);
            if (!field.isAccessible())
                field.setAccessible(true);
            return (Rect) field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean shouldMeasure(Object object, String name, Object... values) {
        try {
            Method method = catch_map.containsKey(name) ? (Method) catch_map.get(name) : RecyclerView.LayoutManager.class.getDeclaredMethod(name, new Class[]{View.class, int.class, int.class, RecyclerView.LayoutParams.class});
            if (!method.isAccessible())
                method.setAccessible(true);
            return (Boolean) method.invoke(object, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void dispatchLayoutStep2(Object object) {
        String name = "dispatchLayoutStep2";
        try {
            Method method = catch_map.containsKey(name) ? (Method) catch_map.get(name) : RecyclerView.class.getDeclaredMethod(name, new Class[]{});
            if (!method.isAccessible())
                method.setAccessible(true);
            method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMeasuredDimensionFromChildren(Object object, Object... values) {
        String name = "setMeasuredDimensionFromChildren";
        try {
            Method method = catch_map.containsKey(name) ? (Method) catch_map.get(name) : RecyclerView.LayoutManager.class.getDeclaredMethod(name, new Class[]{int.class, int.class});
            if (!method.isAccessible())
                method.setAccessible(true);
            method.invoke(object, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
