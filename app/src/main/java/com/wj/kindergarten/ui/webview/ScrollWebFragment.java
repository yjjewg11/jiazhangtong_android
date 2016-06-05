package com.wj.kindergarten.ui.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.wrapper.DoOwnThing;
import com.wj.kindergarten.wrapper.MoveWeb;

/**
 * Created by tangt on 2016/1/5.
 */
public class ScrollWebFragment extends BasicWebFragment {
    private MoveWeb moveWeb;
    private DoOwnThing doOwnThing;
    private String httpUrl;

    public ScrollWebFragment(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public ScrollWebFragment() {
    }

    public void setDoOwnThing(DoOwnThing doOwnThing) {
        this.doOwnThing = doOwnThing;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        super.onCreateView(inflater, container, savedInstanceState);

        moveWeb = new MoveWeb(webView);
        moveWeb.setDoOwnThing(doOwnThing);
        webView.setOnTouchListener(moveWeb);
        setUrl(httpUrl);

        return view;
    }

}
