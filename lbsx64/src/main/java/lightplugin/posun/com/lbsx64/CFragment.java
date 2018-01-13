package lightplugin.posun.com.lbsx64;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * package Kotlin3:lightplugin.posun.com.lbsx64.CFragment.class
 * 作者：zyq on 2017/12/26 17:44
 * 邮箱：zyq@posun.com
 */

@SuppressLint("ValidFragment")
public class CFragment extends Fragment {
    private CallBack callBack;
    private Map<Integer, LightActivity.CallBack> callBackMap = new HashMap<>();

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public Map<Integer, LightActivity.CallBack> getCallBackMap() {
        return callBackMap;
    }

    @Override
    public void onDestroy() {
        if (callBack != null) {
            callBack.call("onDestroy");
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        if (callBack != null) {
            callBack.call("onDestroyView");
        }
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callBackMap.containsKey(requestCode)) {
            callBackMap.get(requestCode).call(data);
            callBackMap.remove(requestCode);
        }
    }

    public interface CallBack {
        void call(String arg);
    }
}
