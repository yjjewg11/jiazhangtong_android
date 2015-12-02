package com.wj.kindergarten.wrapper;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.ObjectAnimator;
import com.wj.kindergarten.ui.other.TopWebView;

/**
 * Created by tangt on 2015/12/2.
 */
public class MoveWeb implements View.OnTouchListener{
    private TopWebView common_topWeb;
    private int moveGloal;
    private int upInstance = 60;
    private WrapperWeb wrapper;
    private DoOwnThing OwnThing;

    public void setDoOwnThing(DoOwnThing doOwnThing) {
        OwnThing = doOwnThing;
    }

    public MoveWeb(TopWebView common_topWeb) {
        this.common_topWeb = common_topWeb;
        wrapper = new WrapperWeb(common_topWeb);
    }

    private float downY;
    private boolean isLocationTop;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downY = event.getY();
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE){

            if (event.getY() - downY < 0 && event.getY() - downY < -50 && !isLocationTop) {
                //向上滑动
                isLocationTop = true;
                OwnThing.pullFromEnd();
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
                    OwnThing.pullFromTop();
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
