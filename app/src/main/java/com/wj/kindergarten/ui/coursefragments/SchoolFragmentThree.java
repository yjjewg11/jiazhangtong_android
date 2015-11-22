package com.wj.kindergarten.ui.coursefragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.func.SpecialCourseInfoActivity;
import com.wj.kindergarten.utils.HintInfoDialog;

/**
 * Created by tangt on 2015/11/19.
 */
public class SchoolFragmentThree extends Fragment {
    private View view;
    private WebView webView;
    private SpecialCourseInfoActivity spec;
    private FrameLayout fl;
    private HintInfoDialog diaLog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        view = inflater.inflate(R.layout.activity_load_school_html, null);
        fl = (FrameLayout) view.findViewById(R.id.school_head);
        fl.setVisibility(View.GONE);
        webView = (WebView) view.findViewById(R.id.group_webView);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if(diaLog == null)
                diaLog = new HintInfoDialog(getActivity());
                diaLog.show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                diaLog.cancel();
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });


        return view;
    }

    public void setUrl(String obj_url) {

        webView.loadUrl(obj_url);
    }
}
