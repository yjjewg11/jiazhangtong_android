package com.wj.kindergarten.ui.more;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.wj.kindergarten.utils.CGLog;

/**
 * Created by tangt on 2016/3/4.
 */
public class PfRefreshLinearLayout extends LinearLayout {
    public PfRefreshLinearLayout(Context context) {
        super(context);
    }
    boolean mode = true;
    public void setMode(boolean mode){
        this.mode = mode;
    }
    private StickyGridHeadersGridView stickyGridHeadersGridView;

    public void setStickyGridHeadersGridView(StickyGridHeadersGridView stickyGridHeadersGridView) {
        this.stickyGridHeadersGridView = stickyGridHeadersGridView;
    }


    public PfRefreshLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    float startY;
    float lastY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(pullScroll == null) return super.onInterceptTouchEvent(ev);
                if(mode && pullScroll.judgeScrollBotom()){
                    return true;
                }
                lastY = ev.getRawY();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                CGLog.v("打印linearlayout的滑动 : "+getScrollY());
                if(lastY == 0) lastY = startY;
                int scroll = (int) ( - event.getRawY() + lastY);
                if(getScrollY() < 0){
                    scroll = 0;
                }
                scrollBy(0, scroll);

                requestLayout();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }
    public interface PullScroll{
        boolean judgeScrollBotom();
    }
    private PullScroll pullScroll;

    public void setPullScroll(PullScroll pullScroll) {
        this.pullScroll = pullScroll;
    }

    public interface Mode{
        boolean PULLDOWN = true;
        boolean DISALBED = false;
    }
}
