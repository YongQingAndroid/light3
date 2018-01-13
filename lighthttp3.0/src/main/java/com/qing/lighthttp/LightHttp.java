package com.qing.lighthttp;

import android.util.Log;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 轻量网络框架
 * package Kotlin3:com.posun.lightui.http.LightHttp.class
 * 作者：zyq on 2017/12/5 14:58
 * 邮箱：zyq@posun.com
 */

public class LightHttp {
    private static String tag = "lightHttp", err = "LightErr";
    private static String APP_BASEURL = "";
    private boolean isSimple = false;
    private SimpleCallBack callBack;
    private Class result_Class;
    private LightResultCover lightResultCover;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8"), stream = MediaType.parse("application/octet-stream");
    /**
     * 解析回调对象的结果
     */
    public Map<String, Method> map_catch = new HashMap<>();
    /**
     * 缓存需要回调的对象
     */
    private WeakReference obj;
    /**
     * 缓存发起请求的对象所生成的请对类
     */
    private static LightLruCache<Class, LightHttp> LightHttps = new LightLruCache<>((int) (Runtime.getRuntime().maxMemory() / 1024 / 8));
    private static Executor executor = Executors.newFixedThreadPool(2);
    private static Map<String, String> defult_headers = new HashMap<>();
    private Headers.Builder headers;
    /**
     * */
    private String BaseUrl = "";

    /**
     * 设置全局请求头
     *
     * @param key
     * @param value
     */
    public static void addDefultHeader(String key, String value) {
        defult_headers.put(key, value);
    }

    public void setResultCover(LightResultCover lightResultCover) {
        this.lightResultCover = lightResultCover;
    }

    private WeakReference getObj() {
        return obj;
    }

    private void setObj(WeakReference obj) {
        this.obj = obj;
    }

    private LightHttp() {
        BaseUrl = APP_BASEURL;
    }

    private LightHttp(SimpleCallBack callBack) {
        BaseUrl = APP_BASEURL;
        isSimple = true;
        this.callBack = callBack;
    }

    /**
     * 获取网络框架实例对象
     * 解析发送网络请求的对象，缓存该对象
     *
     * @param obj
     * @return
     */
    public static LightHttp getinstent(Object obj) {
        Class clazz = obj.getClass();
        if (LightHttps.containsKey(clazz)) {
            return LightHttps.get(clazz);
        }
        LightHttp mLightHttp = new LightHttp();
        mLightHttp.init(clazz);
        mLightHttp.setObj(new WeakReference(obj));
        LightHttps.put(clazz, mLightHttp);
        return mLightHttp;
    }

    public LightHttp setResultObj(Object object) {
        if (object instanceof Class) {
            result_Class = (Class) object;
        } else {
            result_Class = object.getClass();
        }
        return this;
    }

    /**
     * 设置全局BaseURl
     *
     * @param url
     */
    public static void setAppBaseurl(String url) {
        APP_BASEURL = url;
    }

    /**
     * 添加請求頭
     *
     * @param key
     * @param value
     */
    public LightHttp addHeader(String key, String value) {
        if (headers == null)
            headers = new Headers.Builder();
        headers.add(key, value);
        return this;
    }

    /**
     * 设置跟URl
     *
     * @param url
     * @return
     */
    public LightHttp setBaseURl(String url) {
        this.BaseUrl = url;
        return this;
    }

    /**
     * 设置URl的占位符，和Value
     *
     * @param key
     * @param value
     * @return
     */
    public LightHttp urlPath(String key, String value) {
        return this;
    }

    /**
     * 发起Post请求
     * 判断当前線程是主线程时自动分配工作线程
     *
     * @param url 請求鏈接
     * @param obj post对象当对象为File类型或者List<File>類型时自动解析为文件上传，当对象为自定义Bean对象时使用fastjson解析为JOSN报文
     */
    public void post(final String url, final Object obj) {
        final String true_url = BaseUrl + url;
        if (Thread.currentThread().getName().equalsIgnoreCase("main")) {
            try {
                executeNet(true_url, url, obj);
            } catch (Exception e) {
                callBackErr(e, url);
            }
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    executeNet(true_url, url, obj);
                } catch (Exception e) {
                    callBackErr(e, url);
                }
            }
        });

    }

    /**
     * get請求無需多説
     * 判断当前線程是主线程时自动分配工作线程
     *
     * @param url 请求链接
     */
    public void get(final String url) {
        final String true_url = BaseUrl + url;
        if (!Thread.currentThread().getName().equalsIgnoreCase("main")) {
            try {
                executeNet(true_url, url, null);
            } catch (Exception e) {
                callBackErr(e, url);
            }
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    executeNet(true_url, url, null);
                } catch (Exception e) {
                    callBackErr(e, url);
                }
            }
        });
    }

    /***
     * 回調網絡錯誤
     * @param throwable 错误信息
     */
    private void callBackErr(Throwable throwable, final String url) {
        LightHandlerThread.postMsgToMainThread(throwable, new LightHandlerThread.ThreadCall<Throwable>() {
            @Override
            public void call(Throwable obj) {
                if (isSimple && callBack != null) {
                    callBack.err(obj, url);
                    callBack = null;
                    return;
                }
                if (map_catch.containsKey(err)) {
                    try {
                        map_catch.get(err).setAccessible(true);
                        map_catch.get(err).invoke(LightHttp.this.obj.get(), obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i(tag, "没有找到相关错误回调");
                }
            }
        });

    }

    /**
     * 执行网络请求
     *
     * @param true_url 真是执行网络的URl
     * @param url      用户调用的url
     * @param object   上传对象
     * @throws Exception
     */
    private void executeNet(String true_url, final String url, Object object) throws Exception {
        if (isSimple && callBack != null) {
//            throw new RuntimeException("简单模式下请使用simpleExecute 发起请求");
            Type type = null;
            Type[] typeInterfaces = callBack.getClass().getGenericInterfaces();
            if (result_Class == null && typeInterfaces != null && typeInterfaces.length != 0) {
                ParameterizedType mParameterizedType = (ParameterizedType) callBack.getClass()
                        .getGenericInterfaces()[0];
                type = mParameterizedType.getActualTypeArguments()[0];
            }
            Object response_obj = executeOkHttp(getRequest(true_url, object), null, result_Class == null ? type : result_Class, this);
            if (callBack != null) {
                LightHandlerThread.postMsgToMainThread(response_obj, new LightHandlerThread.ThreadCall<Object>() {
                    @Override
                    public void call(Object obj) {
                        callBack.call(obj, url);
                        callBack = null;
                    }
                });
            }
            return;
        }

        if (map_catch.containsKey(url) && obj != null && obj.get() != null) {
            Method method = map_catch.get(url);
            Type[] types = method.getGenericParameterTypes();
            lightWorkThread mlightWorkThread = method.getAnnotation(lightWorkThread.class);
            boolean postmainThread = true;
            if (mlightWorkThread != null) {
                postmainThread = false;
                Log.i(tag, "lightWorkThread");
            }
            Object response_obj = executeOkHttp(getRequest(true_url, object), method, types[0], this);
            callBackByAnnotation(method, postmainThread, response_obj);
        }
    }

    public interface SimpleCallBack<M> {
        void call(M obj, String url);

        void err(Throwable throwable, String url);
    }


    public static LightHttp simpleRequest(SimpleCallBack callBack) {
        return new LightHttp(callBack);
    }


    private static Object executeOkHttp(Request request1, final Method method, Type type, LightHttp lightHttp) throws Exception {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        /**信任所有证书*/
        mBuilder.sslSocketFactory(createSSLSocketFactory());
        mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
        mBuilder.cookieJar(getCookieJar());
        OkHttpClient client = mBuilder.build();
        Request request = request1;
        Response response = client.newCall(request).execute();

        LightResultCover resultCover = getResultCover(method, lightHttp);
        Object response_obj = resultCover.just(response, type, null);
        response.close();
        return response_obj;
    }

    private void callBackByAnnotation(final Method method, boolean postmainThread, Object response_obj) throws IllegalAccessException, InvocationTargetException {
        if (postmainThread) {
            LightHandlerThread.postMsgToMainThread(response_obj, new LightHandlerThread.ThreadCall<Object>() {
                @Override
                public void call(Object obj) {
                    try {
                        method.setAccessible(true);
                        method.invoke(LightHttp.this.obj.get(), obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            method.invoke(LightHttp.this.obj.get(), obj);
        }
    }


    /**
     * 缓存 sessionid
     *
     * @return
     */
    private static CookieJar getCookieJar() {
        return new CookieJar() {
            //这里一定一定一定是HashMap<String, List<Cookie>>,是String,不是url.
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            /**
             *
             * @param url
             * @param cookies
             */
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            /**
             *
             * @param url
             * @return
             */
            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
    }

    /**
     * 獲取报文接口
     *
     * @param method
     * @return
     */
    private static LightResultCover getResultCover(Method method, LightHttp lightHttp) throws Exception {
        if (method == null && lightHttp.lightResultCover == null) {
            return new LightFastJsonCover();
        }
        if (lightHttp.lightResultCover != null)
            return lightHttp.lightResultCover;
        lightHttpCustomResult mlightHttpCustomResult = method.getAnnotation(lightHttpCustomResult.class);
        lightHttpSteamResult mlightHttpSteamResult = method.getAnnotation(lightHttpSteamResult.class);
        if (mlightHttpCustomResult != null) {
            return (LightResultCover) mlightHttpCustomResult.value().newInstance();
        }
        if (mlightHttpSteamResult != null) {
            return new LightSteamCover();
        }
        return new LightFastJsonCover();
    }

    /**
     * 获取网络请求
     *
     * @param url 请求的真实链接
     * @return
     */
    private Request getRequest(String url, Object body) {
        Request.Builder mRequest = new Request.Builder().url(url);
        if (body != null) {
            RequestBody requestBody = getRequestBody(body);
            mRequest.post(requestBody);
        }
        if (defult_headers != null) {
            if (headers == null)
                headers = new Headers.Builder();
            for (String key : defult_headers.keySet()) {
                headers.add(key, defult_headers.get(key));
            }
        }
        if (headers != null)
            mRequest.headers(headers.build());
        return mRequest.build();
    }

    /**
     * 獲取post請求的body
     *
     * @param body post的表單或者文件路勁
     * @return
     */
    private RequestBody getRequestBody(Object body) {
        if (body instanceof File) {
            return getFilesMultipartBody((File) body);
        }
        if (body instanceof List) {
            List list = (List) body;
            if (list.get(0) instanceof File) {
                return getFilesMultipartBody((List<File>) body);
            }
        }
        if (body instanceof String) {
            return RequestBody.create(JSON, body.toString());
        } else {
            return RequestBody.create(JSON, com.alibaba.fastjson.JSON.toJSONString(body));
        }

    }

    /**
     * 多個文件上傳
     *
     * @param files
     * @return
     */
    public MultipartBody getFilesMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (File file : files) {
            RequestBody fileBody1 = RequestBody.create(stream, file);
            builder.addFormDataPart("file", file.getName(), fileBody1);
        }
        return builder.build();
    }

    /**
     * 單個文件上傳
     *
     * @param file
     * @return
     */
    public MultipartBody getFilesMultipartBody(File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody fileBody1 = RequestBody.create(stream, file);
        String file1Name = file.getName();
        builder.addFormDataPart("file", file1Name, fileBody1);
        return builder.build();
    }

    /**
     * 初始化并解析當前類
     *
     * @param clazz
     */
    private void init(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method mMethod : methods) {
            Annotation mlightHttpResult = mMethod.getAnnotation(lightHttpResult.class);
            Annotation mlightHttpErr = mMethod.getAnnotation(lightHttpErr.class);
            if (mlightHttpErr != null) {
                map_catch.put(err, mMethod);
            }
            if (mlightHttpResult != null) {
                map_catch.put(((lightHttpResult) mlightHttpResult).value(), mMethod);
            }
        }
    }

    /**
     *
     */
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        /**
         * @param hostname
         * @param session
         * @return
         */
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    /**
     *
     */
    private static class TrustAllCerts implements X509TrustManager {
        /**
         * @param x509Certificates
         * @param s
         * @throws java.security.cert.CertificateException
         */
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        /**
         * @param x509Certificates
         * @param s
         * @throws java.security.cert.CertificateException
         */
        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        /**
         * @return
         */
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }
}
