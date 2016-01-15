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

        if(!flIsLocationTop){
            moveEvent(ev);
        }

//        if(flIsLocationTop && )

        return super.onInterceptTouchEvent(ev);
    }

    private void moveEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                rowY =  ev.getRawY();
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(getScrollY()) > judgeIsVisible.getHeight()){

                }else{
                    scrollBy(0,(int)(rowY - ev.getRawY()));
                }
                rowY = ev.getRawY();
                CGLog.v("打印滑动的初始位置 ; " + rowY);
                CGLog.v("打印现在的Y的值： "+ev.getRawY());
                CGLog.v("打印Y的差值 ："+ (ev.getRawY()-rowY));
                CGLog.v("打印移动量 : ");
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
}
