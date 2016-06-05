package com.wj.kindergarten.net;

import android.content.Context;

import android.text.TextUtils;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.common.Constants;

import cz.msebera.android.httpclient.HttpEntity;


/**
 * RequestHttpUtil
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/21 17:00
 */
public class RequestHttpUtil {
    private static AsyncHttpClient client;    //实例话对象
//     public static final String BASE_URL = "http://120.25.248.31/px-mobile/";
    //测试地址

    //专用测试服务器地址

    public static final String BASE_URL = "http://120.25.212.44/px-mobile/";


//    public static final String BASE_URL = "http://192.168.0.115:8080/px-mobile/";

//    public static final String BASE_URL = "http://192.168.0.108:8080/px-mobile/";

//正式地址

//    public static final String BASE_URL = "http://jz.wenjienet.com/px-mobile/";

    public synchronized static AsyncHttpClient getClient() {
        return getClient(0);
    }
    public synchronized static AsyncHttpClient getClient(long time) {
        if (client == null) {
            synchronized (RequestHttpUtil.class) {
                if (client == null) {
                    client = new AsyncHttpClient(true, 80, 443);
                }
            }
        }
        if(time <= 0){
            client.setTimeout(Constants.HTTP_TIME_OUT);
        }else {
            client.setTimeout((int) time);
        }

        return client;
    }


    //    private void getCookie(AsyncHttpClient httpClient) {
//        List<Cookie> cookies =  httpClient.getgetCookies();
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < cookies.size(); i++) {
//            Cookie cookie = cookies.get(i);
//            String cookieName = cookie.getName();
//            String cookieValue = cookie.getValue();
//            if (!TextUtils.isEmpty(cookieName)
//                    && !TextUtils.isEmpty(cookieValue)) {
//                sb.append(cookieName + "=");
//                sb.append(cookieValue+";");
//            }
//        }
//        savePreference(mContext,SID, sb.toString());
//    }
//
    public static void initClient() {
        getClient().setTimeout(Constants.HTTP_TIME_OUT);   //设置链接超时，如果不设置，默认为10s
        getCookie();
        getClient().addHeader("Accept-Encoding", "gzip");
        getClient().addHeader("content/type", "application/json;charset=utf-8");

//        initAfinal();
    }


    private static void getCookie() {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(CGApplication.getInstance());
        getClient().setCookieStore(new PersistentCookieStore(CGApplication.getInstance()));
    }

    public static void cancel(Context context, boolean isInterrupt) {
//        getCookie();

        getClient().cancelRequests(context, isInterrupt);
    }

    //用一个完整url获取一个string对象
    protected static void get(Context context, String urlString, AsyncHttpResponseHandler res) {
        getClient().get(context, urlString, res);
    }

    //url里面带参数
    protected static void get(Context context, String urlString, RequestParams params, AsyncHttpResponseHandler res) {

        getClient().get(context, urlString, params, res);
    }

    //不带参数，获取json对象或者数组
    protected static void get(Context context, String urlString, JsonHttpResponseHandler res) {

        getClient().get(context, urlString, res);
    }

    //带参数，获取json对象或者数组
    protected static void get(Context context, String urlString, RequestParams params, JsonHttpResponseHandler res) {
        getClient().get(context, urlString, params, res);
    }

    //下载数据使用，会返回byte数据
    protected static void get(Context context, String uString, BinaryHttpResponseHandler bHandler) {

        getClient().get(context, uString, bHandler);
    }

//    protected static void get(Context context, String urlString, HttpEntity httpEntity, ResponseHandlerInterface responseHandlerInterface) {
//
//        getClient().get(context, urlString, httpEntity, "application/json", responseHandlerInterface);
//    }

    //带参数 提交数据
    public static void post(Context context, String uString, RequestParams params, ResponseHandlerInterface resp) {
        getClient().post(context, uString, params, resp);
    }

    public static void post(Context context, String urlString, HttpEntity httpEntity, ResponseHandlerInterface responseHandlerInterface) {
        getClient().post(context, urlString, httpEntity, "application/json;charset=UTF-8", responseHandlerInterface);
    }
    public static void postPf(Context context, String uString, RequestParams params, ResponseHandlerInterface resp) {
        getClient(Constants.HTTP_PIC_TIME_OUT).post(context, uString, params, resp);
    }

//    private static FinalHttp afinal;
//
//    private static void initAfinal() {
//        afinal = new FinalHttp();
//        PersistentCookieStore myCookieStore = new PersistentCookieStore(CGApplication.getInstance());
//        afinal.configCookieStore(myCookieStore);
//    }
//
//    public static void afinalPost(Context context, String path, AjaxParams params, AjaxCallBack ajaxCallBack) {
//        afinal.post(path,params,ajaxCallBack);
//    }
}
