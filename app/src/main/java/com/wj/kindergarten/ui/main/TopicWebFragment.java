package com.wj.kindergarten.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.wenjie.jiazhangtong.R;

/**
 * Created by tangt on 2015/12/9.
 */
public class TopicWebFragment extends Fragment {
    private View view;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        view = inflater.inflate(R.layout.common_top_webview,null);
        webView =(WebView) view.findViewById(R.id.common_topWeb);
        ((MainActivity)getActivity()).setWebView(webView);
        webView.loadUrl("http://www.baidu.com");
        return view;
    }

    public WebView getWebView() {
        return webView;
    }
}
