package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * Created by tangt on 2015/11/27.
 */
public class SchoolHtmlActivity extends BaseActivity {

    private TabLayout tab_layout;
    private WebView webView;
    private TextView tv_coll;
    private ImageView iv_coll;
    private HintInfoDialog dialog;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_school_html;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        //TODO传递数据
        Intent intent = getIntent();
        tv_coll = (TextView)findViewById(R.id.textview_1_1);
        iv_coll = (ImageView)findViewById(R.id.imageView_1_1);
        tab_layout = (TabLayout)findViewById(R.id.common_tab_layout);
        tab_layout.addTab(tab_layout.newTab().setText("学校简介"));
        tab_layout.addTab(tab_layout.newTab().setText("招生计划"));
        webView = (WebView)findViewById(R.id.common_webView);
        setWebView(webView);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgressDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideProgressDialog();
            }
        });
        webView.loadUrl("http://www.wenjienet.com");
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        webView.loadUrl("http://www.wenjienet.com");
                        break;
                    case 1:
                        webView.loadUrl("http://www.baidu.com");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
     }

//
//    private void store() {
//        dialog = new HintInfoDialog(this, "收藏中，请稍后...");
//        dialog.show();
//        UserRequest.store(this, osc.getTitle(), 82, uuid, "", new RequestResultI() {
//            @Override
//            public void result(BaseModel domain) {
//                dialog.dismiss();
//                Utils.showToast(SpecialCourseInfoActivity.this, "收藏成功");
//                store1();
//            }
//
//            @Override
//            public void result(List<BaseModel> domains, int total) {
//
//            }
//
//            @Override
//            public void failure(String message) {
//                if (!Utils.stringIsNull(message)) {
//                    Utils.showToast(SpecialCourseInfoActivity.this, message);
//                }
//                dialog.dismiss();
//            }
//        });
//    }
//
//    private void store1() {
//        Drawable drawable = getResources().getDrawable(R.drawable.store2);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
////        tvStore.setCompoundDrawables(null, drawable, null, null);
//        iv_coll.setImageDrawable(drawable);
//        tv_coll.setText("已收藏");
//        tv_coll.setTextColor(getResources().getColor(R.color.title_bg));
//        ocs.setIsFavor(false);
//    }
//
//    private void store2() {
//        Drawable drawable = getResources().getDrawable(R.drawable.store1);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
////        tvStore.setCompoundDrawables(null, drawable, null, null);
//        iv_coll.setImageDrawable(drawable);
//        tv_coll.setText("收藏");
//        tv_coll.setTextColor(getResources().getColor(R.color.color_929292));
//        ocs.setIsFavor(true);
//    }
//
//    private void cancelStore() {
//        dialog = new HintInfoDialog(SpecialCourseInfoActivity.this, "取消收藏中，请稍后...");
//        dialog.show();
//        UserRequest.cancelStore(true, SpecialCourseInfoActivity.this, uuid, new RequestResultI() {
//            @Override
//            public void result(BaseModel domain) {
//                dialog.dismiss();
//                Utils.showToast(SpecialCourseInfoActivity.this, "取消收藏成功");
//                store2();
//            }
//
//            @Override
//            public void result(List<BaseModel> domains, int total) {
//
//            }
//
//            @Override
//            public void failure(String message) {
//                if (!Utils.stringIsNull(message)) {
//                    Utils.showToast(SpecialCourseInfoActivity.this, message);
//                }
//                dialog.dismiss();
//            }
//        });
//    }

}
