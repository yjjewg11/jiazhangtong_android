package com.wj.kindergarten.ui.func;

import android.view.KeyEvent;
import android.webkit.WebView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;

/**
 * Created by tangt on 2015/11/18.
 */
public class LoadSchoolHtmlActivity extends BaseActivity {
    private String groupurl;
    private WebView group_webView;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_load_school_html;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
              titleCenterTextView.setText("学校详情简介");
              group_webView = (WebView)findViewById(R.id.group_webView);
              groupurl =  getIntent().getStringExtra("groupurl");
              setWebView(group_webView);
              group_webView.loadUrl(groupurl);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && group_webView.canGoBack()){
            group_webView.goBack();
            return true;
        }
        finish();
        return false;
    }
}
