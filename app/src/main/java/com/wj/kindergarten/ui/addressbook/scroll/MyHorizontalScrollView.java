package com.wj.kindergarten.ui.addressbook.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * MyHorizontalScrollView
 *
 * @Description:
 * @Author: pengqiang.zou
 * @CreateDate: 2015-05-06 16:02
 */
public class MyHorizontalScrollView extends HorizontalScrollView {
    private View mView;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mView != null) {
            mView.scrollTo(l, t);
        }
    }


    public void setScrollView(View view) {
        mView = view;
    }

    /**
     * 滑动事件
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 150);
    }
}
