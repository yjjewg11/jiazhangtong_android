package com.wj.kindergarten.ui.recuitstudents.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.MineSchoolActivity;
import com.wj.kindergarten.ui.other.TopWebView;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.wrapper.DoOwnThing;
import com.wj.kindergarten.wrapper.MoveWeb;

/**
 * Created by tangt on 2015/12/2.
 */
public class MineSchoolIntroduceFragment extends Fragment {
    private View view;
    private MineSchoolActivity activity;
    private TopWebView webView;
    private MoveWeb moveWeb;
    private HintInfoDialog dialog;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        dialog = new HintInfoDialog(getActivity());
        activity = (MineSchoolActivity) getActivity();
        view = inflater.inflate(R.layout.common_top_webview,null);
        webView = (TopWebView)view.findViewById(R.id.common_topWeb);
        activity.setWebView(webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog.show();
             }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        });
        moveWeb = new MoveWeb(webView);
        moveWeb.setDoOwnThing(new MyDothing());
        webView.setOnTouchListener(moveWeb);



        return view;
    }

    public void setUrl(String url){
        webView.loadUrl(url);
    }

    class MyDothing implements DoOwnThing {
            @Override
            public void pullFromTop() {
                 activity.getAnimtor().reverse();
            }

            @Override
            public void pullFromEnd() {
                activity.getAnimtor().start();
            }
    }
}
