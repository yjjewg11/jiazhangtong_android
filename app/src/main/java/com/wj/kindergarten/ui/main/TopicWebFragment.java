package com.wj.kindergarten.ui.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.Utils;

/**
 * Created by tangt on 2015/12/9.
 */
public class TopicWebFragment extends Fragment {
    private View view;
    private WebView webView;
    private HintInfoDialog dialog;
    private String url;
    private Bundle bundle;

    @Override
    public void onStart() {
        super.onStart();
        webView.restoreState(bundle);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        dialog = new HintInfoDialog(getActivity());
        view = inflater.inflate(R.layout.common_top_webview,null);
        webView =(WebView) view.findViewById(R.id.common_topWeb);
        ((MainActivity)getActivity()).setWebView(webView);
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

        return view;
    }

    String tpurl = "http://192.168.0.116:8080/px-rest/sns/index.html?v=1";
    public void setWebUrl(String url) {
        if(TextUtils.isEmpty(url)) {
            url = "http://www.wenjienet.com";
        }else if (url.equals(this.url)){
            return;
        }
        this.url = url;
        Utils.syncCookie(url);
        webView.loadUrl(url);
    }

    @Override
    public void onStop() {
        super.onStop();
        bundle = new Bundle();
        webView.saveState(bundle);
    }
}
