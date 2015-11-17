package com.wj.kindergarten.ui.webview;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Introduce;
import com.wj.kindergarten.net.RequestHttpUtil;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * SchoolIntroduceActivity
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 21:58
 */
public class SchoolIntroduceActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private LinearLayout layout_sd_type;
    private LinearLayout ll_sd_type;
    private ImageView ivSD1;
    private ImageView ivSD2;
    private TextView tvSD1;
    private TextView tvSD2;

    private String htmlSD1 = "";
    private String htmlSD2 = "";
    private int height = 0;
    private String uuid = "";
    private String url1 = "";
    private String url2 = "";
    private static final String QUERY_SCHOOL_INTRODUCE1 = RequestHttpUtil.BASE_URL + "rest/share/getKDInfo.html";//校园介绍
    private static final String QUERY_SCHOOL_INTRODUCE2 = RequestHttpUtil.BASE_URL + "rest/share/getRecruitBygroupuuid.html";//招生计划
    private int type = 0;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_school_introduce;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = false;
    }

    @Override
    protected void onCreate() {
        type = getIntent().getIntExtra("type",0);
        if(type == 1) {
            setTitleText("校园相关");
        }else{
            setTitleText("招生计划");
        }
        showCenterIcon(BaseActivity.TITLE_CENTER_TYPE_RIGHT, R.drawable.title_down);

        init();
        uuid = getIntent().getStringExtra("uuid");
        url1 = QUERY_SCHOOL_INTRODUCE1 + "?uuid=" + uuid;
        url2 = QUERY_SCHOOL_INTRODUCE2 + "?uuid=" + uuid;
        loadData1();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.school_webview);
        layout_sd_type = (LinearLayout) findViewById(R.id.layout_sd_type);
        ll_sd_type = (LinearLayout) findViewById(R.id.ll_sd_type);
        ivSD1 = (ImageView) findViewById(R.id.iv_sd1);
        ivSD2 = (ImageView) findViewById(R.id.iv_sd2);
        tvSD1 = (TextView) findViewById(R.id.tv_sd_1);
        tvSD2 = (TextView) findViewById(R.id.tv_sd_2);

        tvSD1.setOnClickListener(this);
        tvSD2.setOnClickListener(this);
        layout_sd_type.setOnClickListener(this);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ll_sd_type.measure(w, h);
        height = ll_sd_type.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParamsChild = ll_sd_type.getLayoutParams();
        layoutParamsChild.height = 0;
        ll_sd_type.setLayoutParams(layoutParamsChild);

        if(type == 1){
            ivSD1.setVisibility(View.VISIBLE);
            ivSD2.setVisibility(View.GONE);
        }else{
            ivSD2.setVisibility(View.VISIBLE);
            ivSD1.setVisibility(View.GONE);
        }
    }

    private void loadData1() {
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {//先不加载图片，等文字加载完成后才加载图片，在4.4以上的系统如果image的url只会加载一个，所以先加载
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }

        if(type == 1) {
            webView.loadUrl(url1);
        }else{
            webView.loadUrl(url2);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.getSettings().setLoadsImagesAutomatically(true);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(type == 1) {
                    webView.loadUrl(url1);
                }else{
                    webView.loadUrl(url2);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sd_1:
                webView.loadUrl(url1);
                ivSD1.setVisibility(View.VISIBLE);
                ivSD2.setVisibility(View.GONE);
                setTitleText("校园介绍");
                hideLayout();
                break;
            case R.id.tv_sd_2:
                webView.loadUrl(url2);
                ivSD2.setVisibility(View.VISIBLE);
                ivSD1.setVisibility(View.GONE);
                setTitleText("招生计划");
                hideLayout();
                break;
            case R.id.layout_sd_type:
                hideLayout();
                break;
            default:
                break;
        }
    }

    @Override
    protected void titleCenterButtonListener() {
        super.titleCenterButtonListener();
        changeTitle();
    }

    private boolean isShow = false;

    public void changeTitle() {
        if (!isShow) {
            isShow = true;
            showCenterIcon(BaseActivity.TITLE_CENTER_TYPE_RIGHT, R.drawable.title_up);
            layout_sd_type.setVisibility(View.VISIBLE);
            Utils.showLayout(ll_sd_type, 0, height, 300);
        } else {
            hideLayout();
        }
    }

    private void hideLayout() {
        isShow = false;
        showCenterIcon(BaseActivity.TITLE_CENTER_TYPE_RIGHT, R.drawable.title_down);
        Utils.showLayout(ll_sd_type, height, 0, 300, layout_sd_type);
    }
}
