package com.wj.kindergarten.ui.more;


import android.content.Intent;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wenjie.jiazhangtong.R;
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

    private void setWebs(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(0);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //支持缩放
        webSettings.setBuiltInZoomControls(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启缓存数据库功能set
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
//        webSettings.setBlockNetworkImage(true);
        //设置缓存模式

        //设置缓存路径
//        webSettings.setAppCachePath(appCachePath+"/clear");
        //允许访问文件
        webSettings.setAllowFileAccess(true);

        webView.setAlpha(1);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK & webView.canGoBack()){
            webView.goBack();
            return true;
        }

            finish();
            return false;

    }
}
