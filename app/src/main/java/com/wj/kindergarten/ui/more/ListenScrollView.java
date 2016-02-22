package com.wj.kindergarten.ui.more;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by tangt on 2016/2/22.
 */
public class ListenScrollView extends ScrollView {
    private OnScrollChanged onScrollChanged;

    public void setOnScrollChanged(OnScrollChanged onScrollChanged) {
        this.onScrollChanged = onScrollChanged;
    }

    public ListenScrollView(Context context) {
        super(context);
    }

    public ListenScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(onScrollChanged == null) throw new RuntimeException("onScrollChanged can't be null");
        onScrollChanged.onScrollChanged(l,t,oldl,oldt);
        super.onScrollChanged(l, t, oldl, oldt);

    }

    public interface OnScrollChanged{
        void onScrollChanged(int l,int t,int oldl,int oldt);
    }
}
