package com.wj.kindergarten.ui.addressbook.scroll;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by WW on 2014/11/17.
 */
public class MyScrollView extends ScrollView {
    private static final int SC = 0;//滚动监听handler监听
    private RankScrollInterface rankScrollInterface = null;
    private int preScrollY = 0;//之前滚动值

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SC:
                    int ns = getScrollY();//当前滚动值
                    if (preScrollY != ns) {
                        detectScrollStop();
                    } else /*if (preScrollY != 0)*/ {
                        if(ns > 0) {
                            rankScrollInterface.onScrollMove(ns);
                        }
                    }
                    preScrollY = ns;
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                detectScrollStop();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (rankScrollInterface != null) {
            rankScrollInterface.onScrollChanged(this, l, t, oldl, oldt);
        }

        if (t + getHeight() >= computeVerticalScrollRange()) {//判断是否滚动到底部
            rankScrollInterface.onScrollBottom(this, l, t, oldl, oldt);
        }
    }

    private void detectScrollStop() {
        if (getContext() != null && !((Activity) getContext()).isFinishing()) {
            mHandler.sendEmptyMessageDelayed(SC, 0);
        }
    }

    public void setRankScrollInterface(RankScrollInterface rankScrollInterface) {
        this.rankScrollInterface = rankScrollInterface;
    }

    /**
     * 监听ScrollView滑动状态
     */
    public interface RankScrollInterface {
        /**
         * ScrollView滑动坐标改变
         */
        void onScrollChanged(MyScrollView scrollView, int x, int y, int oldX, int oldY);

        /**
         * SScrollView停止滑动
         */
        void onScrollMove(int my);

        void onScrollBottom(MyScrollView scrollView, int x, int y, int oldX, int oldY);
    }

    /**
     * 滑动事件
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 150);
    }
}
