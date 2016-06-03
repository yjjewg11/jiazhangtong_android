package com.wj.kindergarten.ui.more;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.WindowUtils;

/**
 * Created by tangt on 2016/3/4.通用的下拉刷新组件!！！
 */
public class PfRefreshLinearLayout extends LinearLayout {
    private static final float CRITICAL_REFRESH_DISTANCE = 150;
    private TextView tv_bottom;
    private ImageView image_bottom;
    private final int REFRESH_DISTANCE = (int) (45*WindowUtils.getDesnity());
    private final int BOTTOM_HEIGHT = 50;
    private ViewPropertyAnimator valueAnima;
    private TextView refresh_linear_top_tv;
    private ImageView refresh_linear_top_iv;
    private RelativeLayout refresh_linear_top_rl;
    private ViewPropertyAnimator valueAnimaTop;
    private Handler mHandler = new Handler();

    public PfRefreshLinearLayout(Context context) {
        super(context);
    }
    int mode = Mode.PULL_BOTH;
    public void setMode(int mode){
        this.mode = mode;
    }
    private Scroller scroller;



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
        refresh_linear_top_tv = (TextView) findViewById(R.id.refresh_linear_top_tv);
        refresh_linear_top_iv = (ImageView) findViewById(R.id.refresh_linear_top_iv);
        refresh_linear_top_rl = (RelativeLayout) findViewById(R.id.refresh_linear_top_rl);
        CGLog.v("打印测量绘制前 : "+refresh_linear_top_rl.getMeasuredHeight() +" height : "+ refresh_linear_top_rl.getHeight());
        refresh_linear_top_rl.measure(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        int height =  refresh_linear_top_rl.getMeasuredHeight();
        CGLog.v("打印测量绘制后 : "+refresh_linear_top_rl.getMeasuredHeight() +" height : "+ refresh_linear_top_rl.getHeight());
        LinearLayout.LayoutParams params = (LayoutParams) refresh_linear_top_rl.getLayoutParams();
        params.topMargin = -height;
        refresh_linear_top_rl.setLayoutParams(params);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        CGLog.v("打印分发事件 ： "+ev.getAction());
        return super.dispatchTouchEvent(ev);
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
                if(getRefreshing()) return false;
                if(pullScroll == null) return super.onInterceptTouchEvent(ev);
                //判断上拉刷新,如果模式为both或者上拉刷新皆可
                if((mode == Mode.PULL_BOTH || mode == Mode.PULL_FROM_END) &&
                        pullScroll.judgeScrollBotom() && scrollValue < 0
                        && Math.abs(scrollValue) > 10){
                    pullDirection = GloablUtils.FROM_DOWN;
                    return true;
                }
                //这个是下拉刷新
                PullScrollBoth scrollBoth = (PullScrollBoth) pullScroll;
                if((mode == Mode.PULL_BOTH || mode == Mode.PULL_FROM_UP)
                        && scrollBoth.judgeScrollTop() && scrollValue > 0 &&
                        Math.abs(scrollValue) > 10){
                    pullDirection = GloablUtils.FROM_UP;
                    return true;
                }
                interactLastY = ev.getRawY();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    int pullDirection = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        MotionEvent.ACTION_CANCEL
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                CGLog.v("打印linearlayout的滑动 : "+getScrollY());
                if(lastY == 0) lastY = startY;
                //判断在刷新就不滑动.
                if(getRefreshing()) return false;
                switch (pullDirection){
                    //在顶部方向，做下拉刷新
                    case GloablUtils.FROM_UP:
                        //判断手势向上还是向下
                        upMove(event);
                        break;
                    //在底部的方向，做上拉刷新
                    case GloablUtils.FROM_DOWN:
                        downMove(event);
                        break;
                }

                lastY = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                //判断是否大于刷新距离，如果是则加载新数据
                switch (pullDirection){
                    case GloablUtils.FROM_UP:
                        leaveUp();
                        break;
                    case GloablUtils.FROM_DOWN:
                        leaveDown();
                        break;
                }

                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void leaveUp() {
        if(Math.abs(getScrollY()) > REFRESH_DISTANCE){
            scroller.startScroll(0, getScrollY(), 0, (int) -(getScrollY() + BOTTOM_HEIGHT * WindowUtils.getDesnity()), 400);
            if(onRefreshListener == null){
                onRefreshComplete();
            }else {
                setRefreshing(true);
                //位于上面，下拉刷新
                refresh_linear_top_iv.setImageDrawable(getResources().getDrawable(R.drawable.pf_refresh_load));
                valueAnimaTop = refresh_linear_top_iv.animate().rotation(360*600)
                        .setInterpolator(new LinearInterpolator()).setDuration(500 * 1000);
                refresh_linear_top_tv.setText("正在刷新...");
                onRefreshListener.pullFromTopRefresh();
            }
        }else{
            if(getRefreshing()){
                scroller.startScroll(0,getScrollY(),0, (int) -(getScrollY()+BOTTOM_HEIGHT*WindowUtils.getDesnity()),400);
            }else {
                scroller.startScroll(0,getScrollY(),0,-getScrollY(),400);
            }
        }
    }

    private void leaveDown() {
        if(getScrollY() > REFRESH_DISTANCE){
            scroller.startScroll(0,getScrollY(),0, (int) -(getScrollY()-BOTTOM_HEIGHT* WindowUtils.getDesnity()),400);
            valueAnima = createBottomAnim();
            valueAnima.start();
            if(onRefreshListener == null){
                onRefreshComplete();
            }else {
                setRefreshing(true);
                tv_bottom.setText("正在刷新...");
                onRefreshListener.pullFromEndRefresh();
            }
        }else{
            if(getRefreshing()){
                scroller.startScroll(0,getScrollY(),0, (int) -(getScrollY()-BOTTOM_HEIGHT*WindowUtils.getDesnity()),400);
            }else {
                scroller.startScroll(0,getScrollY(),0,-getScrollY(),400);
            }
        }
    }

    private ViewPropertyAnimator createBottomAnim() {
        return image_bottom.animate().rotation(359*600).setInterpolator(new LinearInterpolator()).setDuration(500 * 1000);
    }

    private void upMove(MotionEvent event) {
        float scroll = (event.getRawY() - lastY);
        //如果是下拉操作，则对滑动作缩小
        int scrollDistance = (int) scroll;
        if(scroll > 0){
            scrollDistance = (int) (scroll/2);
        }
        //如果滑到顶部，继续上滑则滑动设置为0
        if(getScrollY() > 0 && scroll < 0){
            scrollDistance = 0;
        }
        if(getScrollY() <= -CRITICAL_REFRESH_DISTANCE* WindowUtils.getDesnity() && scroll > 0){
            scrollDistance = 0;
        }
        scrollBy(0, -scrollDistance);
        invalidate();
        if(Math.abs(getScrollY())>= REFRESH_DISTANCE){
            refresh_linear_top_tv.setText("松手刷新...");
            refresh_linear_top_iv.setImageDrawable(getResources().getDrawable(R.drawable.up_refresh));
        }else {
            refresh_linear_top_iv.setImageDrawable(getResources().getDrawable(R.drawable.down_refresh));
            refresh_linear_top_tv.setText("下拉刷新");
        }
    }

    private void downMove(MotionEvent event) {
        float scroll = ( - event.getRawY() + lastY);
        //如果是上拉，则对距离做缩短，造成用力拉的感觉
        if(scroll > 0){
            scroll = scroll/2;
        }

        if(getScrollY() < 0 && scroll < 0){
            scroll = 0;
        }
        if(getScrollY() >= CRITICAL_REFRESH_DISTANCE* WindowUtils.getDesnity() && scroll > 0){
            scroll = 0;
        }

        scrollBy(0, (int) scroll);
        invalidate();
        if(getScrollY() >= REFRESH_DISTANCE){
            tv_bottom.setText("松手刷新...");
        }else {
            tv_bottom.setText("上拉刷新");
        }
        int ratio = Math.abs(getScrollY());
        image_bottom.setRotation(ratio);
    }

    public interface PullScroll{
        boolean judgeScrollBotom();
    }
    public interface PullScrollBoth extends PullScroll{
        boolean judgeScrollTop();
    }
    private PullScroll pullScroll;

    public void setPullScroll(PullScroll pullScroll) {
        this.pullScroll = pullScroll;
    }

    public interface Mode{
        int PULL_FROM_UP = GloablUtils.PULL_FROM_TOP;
        int DISALBED = GloablUtils.DISALBED;
        int PULL_FROM_END = GloablUtils.PULL_FROM_EDN;
        int PULL_BOTH = GloablUtils.PULL_BOTH;
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
        void pullFromEndRefresh();
        void pullFromTopRefresh();
    }
    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public final void onRefreshComplete(){
        setRefreshing(false);
        switch (pullDirection){
            case GloablUtils.FROM_UP:
                if(valueAnimaTop != null)
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            valueAnimaTop.cancel();
                            refresh_linear_top_iv.setRotation(0);
                        }
                    }, 300);
                refresh_linear_top_tv.setText("下拉刷新...");
                refresh_linear_top_iv.setImageDrawable(getResources().getDrawable(R.drawable.down_refresh));
                break;
            case GloablUtils.FROM_DOWN:
                tv_bottom.setText("上拉刷新...");
                if(valueAnima != null)
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            valueAnima.cancel();
                        }
                    },300);
                break;
        }
        scroller.startScroll(0, getScrollY(), 0, -getScrollY());
        invalidate();
    }

    public void fromEndBeginRefresh(){
        setRefreshing(true);
        scroller.startScroll(0, 0, 0, (int) (BOTTOM_HEIGHT * WindowUtils.getDesnity()));
        createBottomAnim();
        pullDirection = GloablUtils.FROM_DOWN;
        onRefreshListener.pullFromEndRefresh();
    }
}
