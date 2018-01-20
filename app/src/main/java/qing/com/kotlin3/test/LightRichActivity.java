package qing.com.kotlin3.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.posun.lightui.richView.LightRichActivityManager;

/**
 * Created by qing on 2018/1/11.
 */

public class LightRichActivity extends AppCompatActivity {
    private LinearLayout rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        setContentView(rootView);
        LightRichActivityManager lightRichActivityManager = new LightRichActivityManager() {

            @Override
            public void AddRichView(View view) {
                rootView.addView(view);
            }
        };
        try {
            lightRichActivityManager.initUIData(new RichTest(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
