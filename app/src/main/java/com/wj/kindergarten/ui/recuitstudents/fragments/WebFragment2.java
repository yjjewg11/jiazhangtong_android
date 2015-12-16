package com.wj.kindergarten.ui.recuitstudents.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.func.SchoolHtmlActivity;
import com.wj.kindergarten.ui.other.TopWebView;
import com.wj.kindergarten.wrapper.WrapperWeb;

/**
 * Created by tangt on 2015/12/1.
 */
public class WebFragment2 extends Fragment implements View.OnTouchListener{
    private View view;
    private SchoolHtmlActivity activity;
    private float downY;
    private TopWebView common_topWeb;
    private boolean isLocationTop;
    private int moveGloal;
    private int upInstance = 60;
    private WrapperWeb wrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        activity = (SchoolHtmlActivity)getActivity();
        view = inflater.inflate(R.layout.common_top_webview,null);
        common_topWeb = (TopWebView)view.findViewById(R.id.common_topWeb);
//        activity.setWebView(common_topWeb);
        common_topWeb.getSettings().setJavaScriptEnabled(true);
        common_topWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (activity.getHintDialog().isShowing()) {
                    activity.getHintDialog().cancel();
                }
            }
        });
        common_topWeb.setOnTouchListener(this);
        wrapper = new WrapperWeb(common_topWeb);
        return view;
    }
    public void setUrl(String url){
        common_topWeb.loadUrl(url);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downY = event.getY();
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE){

            if (event.getY() - downY < 0 && event.getY() - downY < -50 && !isLocationTop) {
                //向上滑动
                activity.getAnimtor().start();
                isLocationTop = true;
            }

            if(common_topWeb.isTop() && isLocationTop){
//                    radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMe
// asuredHeight() * moveDeltaY));
                moveGloal = (int)(( event.getY()-downY)/2);
                if(moveGloal < 0){
                    moveGloal = 0;
                }
                if(moveGloal >= upInstance){
                    moveGloal = upInstance;
                }

                ((LinearLayout.LayoutParams)common_topWeb.getLayoutParams()).setMargins(0,moveGloal,0,0);
                common_topWeb.requestLayout();
            }
        }

        if(event.getAction() == MotionEvent.ACTION_UP){
            if(((LinearLayout.LayoutParams)common_topWeb.getLayoutParams()).topMargin >= upInstance){
                if (isLocationTop) {
                    isLocationTop = false;
                    activity.getAnimtor().reverse();
                }
            }else{

            }

            ObjectAnimator o1 =  ObjectAnimator.ofInt(wrapper, "topMargin", 0).setDuration(500);
            o1.setInterpolator(new DecelerateInterpolator());
            o1.start();
        }
        return false;
    }

}
