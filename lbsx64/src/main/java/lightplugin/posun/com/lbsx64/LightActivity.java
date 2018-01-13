package lightplugin.posun.com.lbsx64;

import android.app.Activity;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * package Kotlin3:lightplugin.posun.com.lbsx64.LightActivity.class
 * 作者：zyq on 2017/12/26 17:41
 * 邮箱：zyq@posun.com
 */

public class LightActivity implements CFragment.CallBack {
    protected CFragment fragment;
    protected static AtomicInteger atomicInteger = new AtomicInteger(1000);
    private static Map<Activity, LightActivity> activityLightActivityMap = new HashMap<>();

    public static LightActivity with(Activity activity) {
        if (activityLightActivityMap.containsKey(activity)) {
            return activityLightActivityMap.get(activity);
        }
        LightActivity lightactivity = new LightActivity();
        ViewGroup viewGroup = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setId(getId());
        ((ViewGroup) viewGroup.getChildAt(0)).addView(linearLayout);
        lightactivity.fragment = new CFragment();
        lightactivity.fragment.setCallBack(lightactivity);
        activity.getFragmentManager().beginTransaction().replace(getId(), lightactivity.fragment).commit();
        activityLightActivityMap.put(activity, lightactivity);
        return lightactivity;
    }

    public static void init(Activity activity) {
        with(activity);
    }

    public void startforResult(Intent intent, CallBack callBack) {
        int key = atomicInteger.getAndAdd(1);
        startforResult(intent, callBack, key);

    }

    public void startforResult(Intent intent, CallBack callBack, int code) {
//        ParameterizedType mParameterizedType = (ParameterizedType) callBack.getClass()
//                .getGenericInterfaces()[0];
//        Type type = mParameterizedType.getActualTypeArguments()[0];
        fragment.getCallBackMap().put(code, callBack);
        fragment.startActivityForResult(intent, code);
    }

    @Override
    public void call(String arg) {
        switch (arg) {
            case "onDestroy":
                Toast.makeText(fragment.getActivity(), "onDestroy", Toast.LENGTH_SHORT).show();
                activityLightActivityMap.remove(fragment.getActivity());
                break;
        }
    }

    public interface CallBack<T extends Intent> {
        void call(T obj);
    }

    public static int getId() {
        return 1000000;
    }
}
