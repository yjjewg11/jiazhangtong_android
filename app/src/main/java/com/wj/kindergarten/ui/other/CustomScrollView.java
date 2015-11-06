package com.wj.kindergarten.ui.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


public class CustomScrollView extends ScrollView{

    public interface ListenerScrollViewScroll{
        void scrollChanged();
        void scrollBottom();
    }

    private ListenerScrollViewScroll listener;

    public void setListener(ListenerScrollViewScroll listener) {
        this.listener = listener;
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context) {
        super(context);
    }




    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(t + getHeight() >=  computeVerticalScrollRange()){
            //ScrollView滑动到底部了
            listener.scrollBottom();
        }
    }
}


