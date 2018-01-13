package qing.com.lightdatabinding;

import android.widget.TextView;

/**
 * Created by dell on 2017/11/9.
 */

public class Demo {
    private void test() {
        LightDataBingding<DemoEntity> Bingding = new LightDataBingding();
        Bingding.bind_data(null);
        Bingding.getView(TextView.class,1).setText("");
//        Bingding.bind_id(0, data ->data.getName());
//        Bingding.bind_id(1, data ->data.getSex());
        Bingding.getView(TextView.class,1).setText("");
        Bingding.dataNotify();

    }
}
