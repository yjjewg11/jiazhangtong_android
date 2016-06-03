package com.wj.kindergarten.wrapper;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by tangt on 2015/12/2.
 */
public class WrapperFl {
    private FrameLayout frameLayout;
    private int topMargin;

    public WrapperFl() {
    }

    public WrapperFl(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public int getTopMargin() {
        return ((LinearLayout.LayoutParams)frameLayout.getLayoutParams()).topMargin;
    }

    public void setTopMargin(int topMargin) {
         ((LinearLayout.LayoutParams)frameLayout.getLayoutParams()).topMargin = topMargin;
         frameLayout.requestLayout();
    }
}
