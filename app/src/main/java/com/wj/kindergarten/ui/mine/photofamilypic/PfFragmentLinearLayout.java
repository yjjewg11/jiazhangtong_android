package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.ObjectAnimator;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.utils.CGLog;

/**
 * Created by tangt on 2016/1/15.
 */
public class PfFragmentLinearLayout extends LinearLayout {

    private FrameLayout contentFl;

    public void setContentFl(FrameLayout contentFl) {
        this.contentFl = contentFl;
    }

    private int poor;

    public int getFlTopMargin(){
       return  ((LinearLayout.LayoutParams)contentFl.getLayoutParams()).topMargin;
    }

    public void setFlTopMargin(int margin){
        ((LayoutParams)contentFl.getLayoutParams()).topMargin = margin;
        contentFl.requestLayout();
    }

    private boolean judgeScrollByABS(){
       int abs =  Math.abs(contentFl.getHeight()) - Math.abs(((LayoutParams)contentFl.getLayoutParams()).topMargin);
       return abs > 0;
    }

    private DecideSubViewScroll decideSubViewScroll;
    //进行上拉刷新的view，
    View pullUpView;
    public void setPullUpView(View pullUpView){
        this.pullUpView = pullUpView;
    }

    public void setDecideSubViewScroll(DecideSubViewScroll decideSubViewScroll) {
        this.decideSubViewScroll = decideSubViewScroll;
    }

    public PfFragmentLinearLayout(Context context) {
        super(context);
    }

    public PfFragmentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    int startY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //通过拦截事件来进行抽屉滑动，
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getRawY();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                decideSubViewScroll.allowScroll();
                int poor = (int) (ev.getRawY() - lastY);
                //特殊情况，先要求必须上滑，那么在fl的topMargin=0时，同时在往下拉就不拦截;
                // 并且在topMargin == 0时，判断手势如果向上，也不拦截
                CGLog.v("topMargin : "+getFlTopMargin());
                if(getFlTopMargin() == 0 && poor > 0 && pullUpView != null){
                    return super.onInterceptTouchEvent(ev);
                }

                //情况3：当fl的Math.abs(fl.topMargin < fl.getHeight()) ,进行拦截
                if(judgeScrollByABS() && Math.abs(ev.getRawY() - startY) > 10){
                    CGLog.v("打印父控件拦截 : ");
                    return true;
                }

                //拦截情况4：当fl的topMargin等于fl.getHeight()时，并且子view位于顶部，并判断是上滑还是下滑
                //并且滑动一段距离后进行拦截

                if(contentFl.getHeight() == Math.abs(getFlTopMargin()) &&
                        decideSubViewScroll.subViewLocationTop() && poor > 0
                        && Math.abs(ev.getRawY() - startY) > 10){
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);

    }


    int lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                poor = (int) (event.getRawY() - lastY);
                poor =  (lastY == 0 ? 0 : poor);

                int topMargin = getFlTopMargin()+poor;
                if(topMargin > 0){
                    topMargin = 0;
                }
                if(Math.abs(topMargin) > contentFl.getHeight()){
                    topMargin = -contentFl.getHeight();
                }
                setFlTopMargin(topMargin);
                decideSubViewScroll.stopScroll();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                addAnim(event);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void addAnim(MotionEvent event) {
        if(Math.abs(getFlTopMargin()) > 0){
            //动画上移,根据手势判断
            if(poor < 0){
                flMoveTop();
                //动画下移
            }else {
                flMoveBottom();
            }
        }
    }

    public interface DecideSubViewScroll{
        void allowScroll();
        void stopScroll();
        boolean subViewLocationTop();
    }

    class Wrapper {
        FrameLayout frameLayout;

        public Wrapper(FrameLayout frameLayout) {
            this.frameLayout = frameLayout;
        }

        public int getTopMargin() {
            return ((LinearLayout.LayoutParams)(frameLayout.getLayoutParams())).topMargin;
        }

        public synchronized void setTopMargin(int topMargin) {
            ((LinearLayout.LayoutParams)(frameLayout.getLayoutParams())).topMargin = topMargin;
            frameLayout.requestLayout();
        }
    }

    public void flMoveTop(){
        int height = contentFl.getHeight();
        ObjectAnimator animator = ObjectAnimator.ofInt(new Wrapper(contentFl),"topMargin",-height);
        animator.setDuration(300).setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
    public void flMoveBottom(){
        ObjectAnimator animator = ObjectAnimator.ofInt(new Wrapper(contentFl),"topMargin",0);
        animator.setDuration(300).setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
