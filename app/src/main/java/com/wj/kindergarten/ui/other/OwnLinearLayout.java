package com.wj.kindergarten.ui.other;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by tangt on 2015/11/22.
 */
public class OwnLinearLayout extends LinearLayout implements View.OnTouchListener{
    private  Wrapper wrapper;
    private LayoutParams tv_params;
    private TextView textView;
    private  TopWebView webView;
    private boolean isOnce;
    private int moveGloal;
    private float downY;
    private int headHeight = 200;

    public void setHeadHeight(int headHeight) {
        this.headHeight = headHeight;
    }

    public TextView getTextView() {
        return textView;
    }

    public TopWebView getWebView() {
        return webView;
    }

    private Handler handler = new Handler();

    public OwnLinearLayout(Context context) {
        super(context);

    }

    public OwnLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        textView = new TextView(context);
        webView = new TopWebView(context);
//        textView.setText("下拉查看全屏");
        textView.setBackgroundColor(Color.parseColor("#ffffff"));
        webView.setBackgroundColor(Color.parseColor("#ff4966"));
        tv_params = new LayoutParams(LayoutParams.MATCH_PARENT,100);
        addView(textView, tv_params);
        addView(webView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        webView.setOnTouchListener(this);
        wrapper = new Wrapper(textView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!isOnce){
            isOnce = true;
            ((LayoutParams)textView.getLayoutParams()).topMargin = -headHeight;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(judgeIsTop() ){
//                    radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMe
// asuredHeight() * moveDeltaY));
                    float down = downY;
                    moveGloal = (int)(( event.getY()-downY)/2);
                    if(moveGloal < 0){
                        moveGloal = 0;
                    }
                    if(moveGloal >= headHeight){
                        moveGloal = headHeight;
                    }
                            ((LayoutParams) textView.getLayoutParams()).topMargin = moveGloal - headHeight;
                             textView.requestLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
                onMyRefreshListener.freshing();
                //如果下拉头超出距离，则刷新
//                if(wrapper.getTopMagin() > -50){
                    animCancle();
//                }
                break;

        }
        return false;
    }

    public void animCancle(){
        ((LayoutParams) textView.getLayoutParams()).topMargin =- headHeight;
//        int margin = wrapper.getTopMagin();
//        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(wrapper,"topMagin",-(headHeight+wrapper.getTopMagin()));
//        objectAnimator.setDuration(500);
//        objectAnimator.setInterpolator(new DecelerateInterpolator());
//        objectAnimator.start();
    }

    private boolean isScrollingDown(MotionEvent event) {
        if(event.getRawY() - event.getY() < 0)
        return false;
        return true;
    }

    public boolean judgeIsTop(){
       return webView.isTop;
    }

    class Wrapper{
        private TextView textView;
        private int topMagin;

        public Wrapper(TextView textView) {
            this.textView = textView;
        }

        public int getTopMagin() {
            return ((LayoutParams)textView.getLayoutParams()).topMargin;
        }

        public void setTopMagin(int topMagin) {
            ((LayoutParams)textView.getLayoutParams()).topMargin = topMagin;
            textView.requestLayout();
        }
    }

    public  interface OnMyRefreshListener{
        void freshing();
    }
    private OnMyRefreshListener onMyRefreshListener;

    public OnMyRefreshListener getOnMyRefreshListener() {
        return onMyRefreshListener;
    }

    public void setOnMyRefreshListener(OnMyRefreshListener onMyRefreshListener) {
        this.onMyRefreshListener = onMyRefreshListener;
    }
}
