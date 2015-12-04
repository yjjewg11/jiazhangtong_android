package com.wj.kindergarten.ui.more;


import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.ui.BaseActivity;

public class HtmlActivity extends BaseActivity{
    private String url;
    private WebView webView;
    private WebSettings webSettings;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_html;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {

        titleCenterTextView.setText("校园咨询");
        Intent intent =  getIntent();
        intent.getStringExtra("title");
        url =  intent.getStringExtra("url");
        webView = (WebView) findViewById(R.id.web_html);
        setWebs(webView);

    }

    private void setWebs(WebView myWebView) {
        myWebView.getSettings().setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        myWebView.getSettings().setJavaScriptEnabled(true);
//        myWebView.getSettings().setUserAgentString("");
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setDatabaseEnabled(true);

//        webView.setWebChromeClient(new WebChromeClient());
//        webView.setWebViewClient(new WebViewClient());
//        syncCookie(url);
        webView.loadUrl(url);
    }

//    String httpUrl = "http://jz.wenjienet.com/px-mobile/kd/index.html?fn=phone_myclassNews";
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK & webView.canGoBack()){
            webView.goBack();
            return true;
        }

            finish();
            return false;
    }

    private void syncCookie(String url) {
        try {

            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
//            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", CGApplication.getInstance().getLogin().getJSESSIONID()));
            sbCookie.append(String.format(";domain=%s", ".wenjienet.com"));
            sbCookie.append(String.format(";path=%s", "/"));

            String cookieValue = sbCookie.toString();

            Log.i("TAG","打印cookie ： "+cookieValue);
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
