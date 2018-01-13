package lightplugin.posun.com.lbsx64;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qing.lighthttp.LightHttp;
import com.qing.lighthttp.lightHttpErr;
import com.qing.lighthttp.lightHttpResult;
import com.qing.lighthttp.lightUIThread;

import java.net.URISyntaxException;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LightActivity.init(this);
        LightHttp.setAppBaseurl("https://www.mocky.io");
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this, MyAccessibilityService.class);
                startService(startIntent);
//                testNet();
//                  start();

//                https://www.mocky.io/
//                LightHttp.getinstent(MainActivity.this).get("/v2/5185415ba171ea3a00704eed");
//                if (isAppInstalled(MainActivity.this, "com.autonavi.minimap")) {
//                    goToGaode("世界之窗");
//                } else if (isAppInstalled(MainActivity.this, "com.autonavi.minimap")) {
//                    goToBaidu("世界之窗");
//                } else {
//                    Toast.makeText(getApplication(), "请下载高德地图", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void start() {
        Intent intent = new Intent(this, Main3Activity.class);
        LightActivity.with(this).startforResult(intent, new LightActivity.CallBack<Intent>() {
            @Override
            public void call(Intent obj) {
                Toast.makeText(MainActivity.this, "call =" + obj, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
    private void testNet() {
        LightHttp.simpleRequest(new LightHttp.SimpleCallBack() {
            @Override
            public void call(Object obj, String url) {
                Toast.makeText(MainActivity.this, "" + ((MyEntity) obj).getHello(), Toast.LENGTH_SHORT).show();
//                Log.e("qing", String.valueOf(obj));
            }

            @Override
            public void err(Throwable throwable, String url) {
                Log.e("qing", throwable.getMessage());
            }
        }).setResultObj(String.class).urlPath("{id}", "123456").get("/v2/5185415ba171ea3a00704eed/{id}/find");
    }

    private void goToGaode(String adress) {
        StringBuffer stringBuffer = new StringBuffer("androidamap://route?sourceApplication=").append("amap");

        stringBuffer.append("&dlat=").append("")
                .append("&dlon=").append("")
                .append("&dname=").append(adress)
                .append("&dev=").append(0)
                .append("&t=").append(0);

        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        startActivity(intent);
    }

    /**
     * check the app is installed
     */
    private boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    private void goToBaidu(String adress) {
        Intent intent = null;
        try {
            intent = Intent.getIntent("intent://map/direction?destination=" + adress + "&mode=driving#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }

    @lightUIThread
    @lightHttpResult("/v2/5185415ba171ea3a00704eed")
    private void getNetString(MyEntity arg) {
        Toast.makeText(this, arg.getHello(), Toast.LENGTH_SHORT).show();
    }

    @lightHttpErr
    private void getErr(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public static class MyEntity {
        private String hello;

        public String getHello() {
            return hello;
        }

        public void setHello(String hello) {
            this.hello = hello;
        }
    }

}
