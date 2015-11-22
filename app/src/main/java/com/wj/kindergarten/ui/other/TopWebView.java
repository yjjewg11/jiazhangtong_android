package com.wj.kindergarten.ui.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by tangt on 2015/11/20.
 */
public class TopWebView extends WebView {
    public TopWebView(Context context) {
        super(context);
    }
    public boolean isTop ;

    public boolean isTop() {
        return isTop;
    }

    public TopWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // TODO Auto-generated method stub
        isTop = false;
        float webcontent = getContentHeight() * getScale();//webview的高度
        float webnow = getHeight() + getScrollY();//当前webview的高度
        if (getContentHeight() * getScale() - (getHeight() + getScrollY()) == 0) {
//已经处于底端
//            lay_bottom_layout.setVisibility(View.VISIBLE);

        } else {
//            lay_bottom_layout.setVisibility(View.GONE);
        }
//已经处于顶端
        if (getScrollY() == 0) {
            isTop = true;
        }
    }
}
