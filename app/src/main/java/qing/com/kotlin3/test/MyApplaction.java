package qing.com.kotlin3.test;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * package light3:qing.com.kotlin3.test.MyApplaction.class
 * 作者：zyq on 2018/1/26 16:34
 * 邮箱：zyq@posun.com
 */

public class MyApplaction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        java.lang.reflect.Proxy.newProxyInstance(mWindowManager.getClass().getClassLoader(), new Class<?>[]{ViewManager.class}, new ViewManagerHandler(mWindowManager));
////        ViewManager viewManager  mBase ( /android/app/ContextImpl.java) ServiceFetcher  SYSTEM_SERVICE_MAP
////        android.app.ContextImpl.ServiceFetcher    NotificationManagerService
//        try {
//            Field fieldmBase = ContextWrapper.class.getDeclaredField("mBase");
//            fieldmBase.setAccessible(true);
//            fieldmBase.get(this);
//            Class ContextImplclass = Class.forName("com.android.app.ContextImpl");
//            Object o_map = ContextImplclass.getDeclaredField("SYSTEM_SERVICE_MAP").get(null);
//            Log.d("qing", "....");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            resitToast();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resitToast() throws Exception {
//        Method method = Toast.class.getDeclaredMethod("getService", new Class[]{});
//        method.setAccessible(true);
//        Object INotificationManager = method.invoke(null);
////        getClassLoader().loadClass()
//
//        Class NotificationManagerClass = Class.forName("com.android.server.notification.NotificationManagerService");
//        Field field = NotificationManagerClass.getDeclaredField("mToastQueue");
//        field.setAccessible(true);
//        field.set(INotificationManager, new MyArrayList<>());

    }
//
//    public static class ViewManagerHandler implements InvocationHandler {
//        private WindowManager windowManager;
//
//        public ViewManagerHandler(WindowManager windowManager) {
//            this.windowManager = windowManager;
//        }
//
//        @Override
//        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
//            if (method.getName().equals("addView")) {
//                Object obj = objects[1];
//                if (obj != null && obj instanceof WindowManager.LayoutParams) {
//                    WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) obj;
//                    if (layoutParams.type == WindowManager.LayoutParams.TYPE_TOAST) {
//                        Log.i("ViewManagerHandler", "i get Toast");
//                    }
//                }
//            }
//            return method.invoke(windowManager, objects);
//        }
//    }
}
