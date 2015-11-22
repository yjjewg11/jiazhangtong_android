package com.wj.kindergarten.ui.webview;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.handmark.pulltorefresh.library.extras.PullToRefreshWebView2;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SchoolDetail;
import com.wj.kindergarten.bean.SchoolDetailList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.LoadSchoolHtmlActivity;
import com.wj.kindergarten.ui.func.NormalReplyListActivity;

import java.util.List;

/**
 * Created by tangt on 2015/11/18.
 */
public class LoadHtmlActivity extends BaseActivity {
    private String url;
    private PullToRefreshWebView load_html_webView;
    private WebView webView;
    private String groupuuid;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_load_html;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        titleCenterTextView.setText("课程内容简介");
        Intent intent = getIntent();
        url = intent.getStringExtra("courseurl");
        groupuuid = intent.getStringExtra("groupuuid");
        load_html_webView = (PullToRefreshWebView)findViewById(R.id.load_html_webView);
        webView = load_html_webView.getRefreshableView();
        webView.loadUrl(url);
        load_html_webView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        load_html_webView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<WebView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<WebView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {
                              getSchoolInfo(groupuuid);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }

        finish();
        return false;
    }

    public void getSchoolInfo(String schooluuid){
        UserRequest.getTrainSchoolDetail(this, schooluuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                final SchoolDetailList sdl = (SchoolDetailList) domain;
                if (sdl != null && sdl.getData() != null) {
                    SchoolDetail sd = sdl.getData();
                    String text = null;
                    Intent intent = new Intent(LoadHtmlActivity.this, LoadSchoolHtmlActivity.class);
                    intent.putExtra("groupurl", sdl.getObj_url());
                    startActivity(intent);
                    if(load_html_webView.isRefreshing())load_html_webView.onRefreshComplete();
                    if (!TextUtils.isEmpty(sd.getDescription())) {
                        text = sd.getDescription();
                    } else {
                        text = "  查看更多详细内容!";
                    }
                }

            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }
}
