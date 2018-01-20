package com.posun.lightui.richView.instent;

import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.posun.lightui.richView.LightActionExeCute;
import com.posun.lightui.richView.LightRichActivityManager;

/**
 * package Kotlin3:com.posun.lightui.richView.instent.TestClick.class
 * 作者：zyq on 2018/1/15 10:11
 * 邮箱：zyq@posun.com
 */

public class SimpleClickExeCute implements LightActionExeCute {
    private EventBean tag;

    @Override
    public void execute(LightRichActivityManager.LightItemIntface arg, LightRichActivityManager activityManager) {
        if (tag != null) {
            switch (tag.getType()) {
                case TIMEPICKER:
                    break;
                case ACTION:
                    break;
                case NET_GET:
                    activityManager.commitAllData();
                    Toast.makeText(((View) arg).getContext(), "cilck: " + JSON.toJSONString(activityManager.getDataobj()), Toast.LENGTH_SHORT).show();
                    break;
                case NET_POST:
                    break;
                case SELECTDATA:
                    break;
            }
        }

    }

    public SimpleClickExeCute(EventBean tag) {
        this.tag = tag;
    }
}
