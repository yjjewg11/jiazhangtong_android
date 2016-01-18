package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.wj.kindergarten.utils.CGLog;

/**
 * Created by tangt on 2016/1/15.
 */
public class PfFragmentLinearLayout extends LinearLayout {

    boolean flIsLocationTop;

    public JudgeIsVisible judgeIsVisible;
    private OnInterceptTouchEvent onInterceptTouchEvent;

    public void setOnInterceptTouchEvent(OnInterceptTouchEvent onInterceptTouchEvent) {
        this.onInterceptTouchEvent = onInterceptTouchEvent;
    }

    public void setJudgeIsVisible(JudgeIsVisible judgeIsVisible) {
        this.judgeIsVisible = judgeIsVisible;
    }

    public PfFragmentLinearLayout(Context context) {
        super(context);
    }

    public PfFragmentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    float rowY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //通过拦截事件来进行抽屉滑动，是否进行拦截通过判断1.上面那个布局容器可见，2.当位于顶端时，listview是否位于顶端。

//            moveEvent(ev);
        onInterceptTouchEvent.onInterceptTouch(ev);


//        if(flIsLocationTop && )

        return super.onInterceptTouchEvent(ev);
//
    }

    private void moveEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                rowY =  ev.getRawY();
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(getScrollY()) > judgeIsVisible.getHeight()){
                       flIsLocationTop = true;
                    int scroll = 0;
                    //判断在滑动顶部时，如何继续上滑则禁止，下滑则允许。
                    if(rowY - ev.getRawY() > 0){

                    }else{
                      scroll = (int) (rowY - ev.getRawY());
                    }
                    scrollBy(0,scroll);
                }else{
                    //如果滑动到最底部，继续下滑则禁止
//                    int scrollBottom = 0;
//                    if(rowY - ev.getRawY() < 0){
//
//                    }else{
//                        scrollBottom = (int) (rowY - ev.getRawY());
//                    }
                    scrollBy(0,(int) (rowY - ev.getRawY()));
                }


                rowY = ev.getRawY();
                CGLog.v("打印滑动的初始位置 ; " + rowY);
                CGLog.v("打印现在的Y的值： "+ev.getRawY());
                CGLog.v("打印Y的差值 ："+ (ev.getRawY()-rowY));
                CGLog.v("打印移动量 : "+getScrollY());
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
    }

    public interface JudgeIsVisible{
        boolean isVisible();
        void notVisible();
        void alwaysVisible();
        float getHeight();
    }

    public interface OnInterceptTouchEvent{
        boolean onInterceptTouch(MotionEvent event);
    }
}
