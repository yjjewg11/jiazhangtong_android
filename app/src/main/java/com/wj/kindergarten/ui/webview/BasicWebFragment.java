package com.wj.kindergarten.ui.webview;

import android.app.Dialog;
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
import com.wj.kindergarten.ui.other.TopWebView;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ToastUtils;

/**
 * Created by tangt on 2016/1/5.
 * @description 此类用于所有显示网页的fragment。
 */
public class BasicWebFragment extends Fragment {
    protected TopWebView webView;
    private HintInfoDialog dialog;

    public TopWebView getWebView() {
        return webView;
    }
    public void setUrl(String url){
        if(webView == null) {
            throw new NullPointerException("webview can't be null !");
        }
        webView.loadUrl(url);
    }

    protected View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        dialog = new HintInfoDialog(getActivity());
        view = inflater.inflate(R.layout.common_top_webview,null);
        webView = (TopWebView) view.findViewById(R.id.common_topWeb);
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
        webView.setOnLongClickListener(new WebClickListeners(getActivity()));
        return view;
    }
}
