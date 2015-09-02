package com.wj.kindergarten.ui.webview;

import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.net.MyCookieManager;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.CGLog;

/**
 * WebviewActivity
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 20:38
 */
public class WebviewActivity extends BaseActivity {

    private WebView webView = null;
    private String url = "";
    private LinearLayout layoutWebview = null;
    private RelativeLayout layoutLoad = null;
    private ImageView ivLoadFailure = null;
    private TextView tvLoadInfo = null;
    private ProgressBar progressBar = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_webview;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        String title = getIntent().getStringExtra("title");
        setTitleText(title);
        url = getIntent().getStringExtra("url");
        String CookieStr = CookieManager.getInstance().getCookie(url);
        CGLog.d("cookie: " + CookieStr);//浏览器中设置的cookie照到设置就ok了
        init();
        syncCookie(url);
        load();
    }

    private void syncCookie(String url) {
        try {
            CookieSyncManager.createInstance(CGApplication.getInstance());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s", CGApplication.getInstance().getLogin().getJSESSIONID()));
            sbCookie.append(String.format(";domain=%s", "jz.wenjienet.com"));
            sbCookie.append(String.format(";path=%s", "/px-mobile"));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() {
        layoutWebview = (LinearLayout) findViewById(R.id.layout_webview);
        webView = (WebView) findViewById(R.id.webview);
        ivLoadFailure = (ImageView) findViewById(R.id.iv_load_failure);
        progressBar = (ProgressBar) findViewById(R.id.info_loading_progress);
        tvLoadInfo = (TextView) findViewById(R.id.info_loading_load);
        layoutLoad = (RelativeLayout) findViewById(R.id.layout_reload_2);

        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
    }

    private void load() {
        if (Build.VERSION.SDK_INT >= 19) {//先不加载图片，等文字加载完成后才加载图片，在4.4以上的系统如果image的url只会加载一个，所以先加载
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.getSettings().setLoadsImagesAutomatically(true);
                layoutWebview.setVisibility(View.VISIBLE);
                layoutLoad.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                loadFailure();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void loadFailure() {
        progressBar.setVisibility(View.GONE);
        ivLoadFailure.setVisibility(View.VISIBLE);
        tvLoadInfo.setText(getString(R.string.loading_failed));
        layoutLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ivLoadFailure.setVisibility(View.GONE);
                tvLoadInfo.setText(getString(R.string.loading_content));
                webView.loadUrl(url);
            }
        });
    }
}
