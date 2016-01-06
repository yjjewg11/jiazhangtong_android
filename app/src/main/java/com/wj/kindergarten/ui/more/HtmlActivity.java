package com.wj.kindergarten.ui.more;


import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.Utils;

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

        commonDialog = new HintInfoDialog(this);
        titleCenterTextView.setText("校园咨询");
        Intent intent =  getIntent();
        String center_title = intent.getStringExtra("center_title");
        if(!TextUtils.isEmpty(center_title)){
            titleCenterTextView.setText(center_title);
        }
        intent.getStringExtra("title");
        url =  intent.getStringExtra("url");
        Log.i("TAG","打印扫码地址 ： "+url);
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

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                commonDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String title = view.getTitle();
                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(Utils.isNull(title))){
                    title = "链接详情";
                }
                titleCenterTextView.setText(title);
                if(commonDialog.isShowing()){
                    commonDialog.cancel();
                }
            }
        });

        Utils.syncCookie(url);
        webView.loadUrl(url);
    }

    String httpUrl = "http://jz.wenjienet.com/px-mobile/kd/index.html?fn=phone_myclassNews";
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK & webView.canGoBack()){
            webView.goBack();
            return true;
        }

            finish();
            return false;
    }

    @Override
    protected void onStop() {
        stopWebview(webView);
        super.onStop();
    }
}
