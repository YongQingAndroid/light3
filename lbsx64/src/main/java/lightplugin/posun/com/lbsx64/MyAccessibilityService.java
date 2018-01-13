package lightplugin.posun.com.lbsx64;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * package Kotlin3:lightplugin.posun.com.lbsx64.MyAccessibilityService.class
 * 作者：zyq on 2018/1/6 10:06
 * 邮箱：zyq@posun.com
 */

@RequiresApi(api = Build.VERSION_CODES.DONUT)
public class MyAccessibilityService extends AccessibilityService {
    private String packageName="com.tencent.mm";
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        String eventTypeName = "";
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventTypeName = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventTypeName = "TYPE_VIEW_FOCUSED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventTypeName = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                eventTypeName = "TYPE_VIEW_SELECTED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                eventTypeName = "TYPE_VIEW_TEXT_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventTypeName = "TYPE_WINDOW_STATE_CHANGED";
                //打印当前界面的类名
                String className = accessibilityEvent.getClassName().toString();
                //demo app
                    AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                    if (nodeInfo != null) {
                        //根据控件名称获取控件
//                        recycleByControlName(nodeInfo, "开始");
//                        AccessibilityNodeInfo info = accessibilityNodeInfo;
//                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        //根据控件ID获取控件
                        recycleByControlId(nodeInfo, packageName+":id/k9");
                        for (AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfoList) {
                            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                        }
                    }
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventTypeName = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                eventTypeName = "TYPE_TOUCH_EXPLORATION_GESTURE_END";
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                eventTypeName = "TYPE_ANNOUNCEMENT";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                eventTypeName = "TYPE_TOUCH_EXPLORATION_GESTURE_START";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                eventTypeName = "TYPE_VIEW_HOVER_ENTER";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                eventTypeName = "TYPE_VIEW_HOVER_EXIT";
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                eventTypeName = "TYPE_VIEW_SCROLLED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                eventTypeName = "TYPE_VIEW_TEXT_SELECTION_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventTypeName = "TYPE_WINDOW_CONTENT_CHANGED";
                break;
            default:
                break;
        }
    }
    private AccessibilityNodeInfo accessibilityNodeInfo;
    public void recycleByControlName(AccessibilityNodeInfo node, String controlName) {
        if (node.getChildCount() == 0) {
            if (node.getText() != null) {
                if (controlName.equals(node.getText().toString())) {
                    accessibilityNodeInfo =  node;
                }
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    recycleByControlName(node.getChild(i), controlName);
                }
            }
        }
    }
    private List<AccessibilityNodeInfo> accessibilityNodeInfoList;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void recycleByControlId(AccessibilityNodeInfo node, String controlID) {
        if (node.getChildCount() == 0) {
            if (node.findAccessibilityNodeInfosByViewId(controlID).size() > 0) {
                accessibilityNodeInfoList = node.findAccessibilityNodeInfosByViewId(controlID);
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    if (node.findAccessibilityNodeInfosByViewId(controlID).size() > 0) {
                        List<AccessibilityNodeInfo> infoList = node.findAccessibilityNodeInfosByViewId(controlID);
                        accessibilityNodeInfoList = node.findAccessibilityNodeInfosByViewId(controlID);
                    }
                    recycleByControlId(node.getChild(i), controlID);
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }
}
