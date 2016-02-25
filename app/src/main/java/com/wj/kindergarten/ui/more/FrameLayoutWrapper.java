package com.wj.kindergarten.ui.more;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by tangt on 2016/2/25.
 */
public class FrameLayoutWrapper  {
    private FrameLayout frameLayout;
    private int bottomMargin;

    public FrameLayoutWrapper(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public int getBottomMargin() {
        return ((LinearLayout.LayoutParams)frameLayout.getLayoutParams()).bottomMargin;
    }

    public void setBottomMargin(int bottomMargin) {
        ((LinearLayout.LayoutParams)frameLayout.getLayoutParams()).bottomMargin = bottomMargin;
        frameLayout.requestLayout();
    }
}
