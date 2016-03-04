package com.wj.kindergarten.ui.more;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.WindowUtils;

/**
 * Created by tangt on 2016/3/4.
 */
public class PfRefreshLinearLayout extends LinearLayout {
    private TextView tv_bottom;
    private ImageView image_bottom;
    private final int REFRESH_DISTANCE = (int) (100*WindowUtils.getDesnity());
    private final int BOTTOM_HEIGHT = 50;
    private ViewPropertyAnimator valueAnima;

    public PfRefreshLinearLayout(Context context) {
        super(context);
    }
    boolean mode = true;
    public void setMode(boolean mode){
        this.mode = mode;
    }
    private StickyGridHeadersGridView stickyGridHeadersGridView;
    private Scroller scroller;

    public void setStickyGridHeadersGridView(StickyGridHeadersGridView stickyGridHeadersGridView) {
        this.stickyGridHeadersGridView = stickyGridHeadersGridView;
    }


    public PfRefreshLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        this.context = context;
    }

    private Context context;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        image_bottom = (ImageView) findViewById(R.id.image_bottom);
    }


    float startY;
    float lastY;
    float interactLastY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = ev.getRawY();
                lastY = 0;
                interactLastY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                if(interactLastY == 0) interactLastY = startY;
                int scrollValue = (int) (ev.getRawY() - interactLastY);
                if(getRefreshing()) return true;
                if(pullScroll == null) return super.onInterceptTouchEvent(ev);
                if(mode && pullScroll.judgeScrollBotom() && scrollValue < 0 && !getRefreshing()){
                    return true;
                }
                interactLastY = ev.getRawY();
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
                float scroll = ( - event.getRawY() + lastY);
                //如果是上拉，则对距离做缩短，造成用力拉的感觉
                if(scroll > 0){
                    scroll = scroll/2;
                }

                if(getScrollY() < 0 && scroll < 0){
                    scroll = 0;
                }
                if(getScrollY() >= 150* WindowUtils.getDesnity() && scroll > 0){
                    scroll = 0;
                }

                scrollBy(0, (int) scroll);
                invalidate();
                if(getScrollY() >= REFRESH_DISTANCE){
                    tv_bottom.setText("松手刷新...");
                }else {
                    tv_bottom.setText("上拉刷新");
                }
                int ratio = Math.abs(getScrollY())*3;
                image_bottom.setRotation(ratio);
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //判断是否大于刷新距离，如果是则加载新数据
                if(getScrollY() > REFRESH_DISTANCE){
                    scroller.startScroll(0,getScrollY(),0, (int) -(getScrollY()-BOTTOM_HEIGHT*WindowUtils.getDesnity()),400);
                    valueAnima = image_bottom.animate().rotation(359*600).setInterpolator(new LinearInterpolator()).setDuration(500 * 1000);
                    valueAnima.start();
                    if(onRefreshListener == null){
                        onRefreshComplete();
                    }else {
                        setRefreshing(true);
                        tv_bottom.setText("正在刷新...");
                        onRefreshListener.onRefresh();
                    }
                }else{
                    if(getRefreshing()){
                        scroller.startScroll(0,getScrollY(),0, (int) -(getScrollY()-BOTTOM_HEIGHT*WindowUtils.getDesnity()),400);
                    }else {
                        scroller.startScroll(0,getScrollY(),0,-getScrollY(),400);
                    }
                }
                invalidate();
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

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(0,scroller.getCurrY());
            invalidate();
        }
    }
    private boolean Refreshing = false;

    public boolean getRefreshing() {
        return Refreshing;
    }

    public void setRefreshing(boolean Refreshing) {
        this.Refreshing = Refreshing;
    }

    public interface OnRefreshListener{
        void onRefresh();
    }
    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void onRefreshComplete(){
        setRefreshing(false);
        tv_bottom.setText("上拉刷新...");
        valueAnima.cancel();
        scroller.startScroll(0, getScrollY(), 0, -getScrollY());
        invalidate();
    }
}
