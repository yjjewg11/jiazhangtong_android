package com.wj.kindergarten.wrapper;

import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by tangt on 2015/12/2.
 */
public class WrapperWeb {
    private WebView webView;
    private int topMargin;

    public WrapperWeb(WebView webView) {
        this.webView = webView;
    }

    public int getTopMargin() {
        return ((LinearLayout.LayoutParams)webView.getLayoutParams()).topMargin;
    }

    public void setTopMargin(int topMargin) {
        ((LinearLayout.LayoutParams)webView.getLayoutParams()).topMargin = topMargin;
        webView.requestLayout();

    }
}
