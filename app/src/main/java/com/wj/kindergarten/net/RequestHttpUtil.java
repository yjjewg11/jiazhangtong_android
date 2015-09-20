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
import com.wj.kindergarten.utils.CGLog;

import org.apache.http.HttpEntity;
import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * RequestHttpUtil
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/21 17:00
 */
public class RequestHttpUtil {
    private static AsyncHttpClient client;    //实例话对象
    // public static final String BASE_URL = "http://120.25.248.31/px-mobile/";
    public static final String BASE_URL = "http://jz.wenjienet.com/px-mobile/";

    public synchronized static AsyncHttpClient getClient() {
        if (client == null) {
            synchronized (RequestHttpUtil.class) {
                if (client == null) {
                    client = new AsyncHttpClient(true, 80, 443);
                }
            }
        }
        return client;
    }

    public static void initClient() {
        getClient().setTimeout(Constants.HTTP_TIME_OUT);   //设置链接超时，如果不设置，默认为10s
        PersistentCookieStore myCookieStore = new PersistentCookieStore(CGApplication.getInstance());
        getClient().setCookieStore(myCookieStore);
        getClient().addHeader("Accept-Encoding", "gzip");
        getClient().addHeader("content/type", "application/json;charset=utf-8");
    }

    public static void cancel(Context context, boolean isInterrupt) {
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

    protected static void get(Context context, String urlString, HttpEntity httpEntity, ResponseHandlerInterface responseHandlerInterface) {
        getClient().get(context, urlString, httpEntity, "application/json", responseHandlerInterface);
    }

    //带参数 提交数据
    public static void post(Context context, String uString, RequestParams params, ResponseHandlerInterface resp) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(CGApplication.getInstance());
        List<Cookie> cookies = myCookieStore.getCookies();
        MyCookieManager.add(cookies);
        getClient().post(context, uString, params, resp);
    }

    protected static void post(Context context, String urlString, HttpEntity httpEntity, ResponseHandlerInterface responseHandlerInterface) {
        getClient().post(context, urlString, httpEntity, "application/json;charset=UTF-8", responseHandlerInterface);
    }
}