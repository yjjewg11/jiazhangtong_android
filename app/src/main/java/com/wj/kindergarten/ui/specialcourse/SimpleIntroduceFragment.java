package com.wj.kindergarten.ui.specialcourse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SchoolDetail;
import com.wj.kindergarten.bean.SchoolDetailList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.MineCourseDetailActivity;
import com.wj.kindergarten.ui.func.SchoolDetailInfoActivity;
import com.wj.kindergarten.ui.other.TopWebView;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wenjie.jiazhangtong.R;


public class SimpleIntroduceFragment extends Fragment implements View.OnTouchListener {

    private View view;

    private SchoolDetail sd;

    boolean isFirst;
    private TopWebView tv_html;
    private ImageView iv_img;
    private float downY;
    private SchoolDetailInfoActivity activity;
    private int moveGloal;
    private ObjectAnimator resetAnim;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            return view;
        }



        activity = (SchoolDetailInfoActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_simple_introduce, null);
        tv_html = (TopWebView) view.findViewById(R.id.tv_html);
        tv_html.setOnTouchListener(this);
        activity.setWebView(tv_html);

        resetAnim = ObjectAnimator.ofInt(new Wrapper(tv_html),"topMargin",-moveGloal);
        resetAnim.setDuration(400);
        resetAnim.setInterpolator(new DecelerateInterpolator());
        if (!isFirst) {
            loadData();
            activity.createAnim();
            isFirst = true;
        }

        return view;
    }

    private void loadData() {
        SchoolDetailInfoActivity sif = (SchoolDetailInfoActivity) getActivity();
        UserRequest.getTrainSchoolDetail(sif, sif.getSchooluuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SchoolDetailList sdl = (SchoolDetailList) domain;
                if (sdl != null && sdl.getData() != null) {
                    sd = sdl.getData();
                    tv_html.getSettings().setJavaScriptEnabled(true);
                    tv_html.loadUrl(sdl.getObj_url());
//                    tv_html.loadDataWithBaseURL(null,sd.getDescription(),"text/html","utf-8",null);
                } else {

                    RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.introduce_rllll);
                    relativeLayout.removeAllViews();
                    relativeLayout.addView(View.inflate(getActivity(), R.layout.nothing_view, null));
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            if (event.getY() - downY < 0 && event.getY() - downY < -50 && !activity.isLocationTop()) {
                //向上滑动
                activity.getAnimTop().start();
                activity.setIsLocationTop(true);
            }

            //判断webview是否位于顶部，
            if (tv_html.isTop() && activity.isLocationTop()) {

                moveGloal = (int) (event.getY() - downY);
                if (moveGloal < 0) {
                    moveGloal = 0;
                }
                if (moveGloal > 160) {
                    moveGloal = 160;
                }
                //对实际移动距离做缩小，造成用力拉的感觉
                ((RelativeLayout.LayoutParams) tv_html.getLayoutParams()).topMargin = moveGloal / 4;
                tv_html.requestLayout();
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (tv_html.isTop() && activity.isLocationTop()) {
                if (moveGloal > 60) {
                    //滑动距离足够，可以进行刷新操作。
                    activity.getAnimTop().reverse();
                    activity.setIsLocationTop(false);
                }
            }

            resetAnim = ObjectAnimator.ofInt(new Wrapper(tv_html),"topMargin",0);
            resetAnim.setDuration(500);
            resetAnim.setInterpolator(new DecelerateInterpolator());
            resetAnim.start();

        }
        return false;
    }

    class Wrapper {
        private WebView webView;
        private int topMargin;

        public int getTopMargin() {
            return ((RelativeLayout.LayoutParams) webView.getLayoutParams()).topMargin;
        }

        public void setTopMargin(int topMargin) {
            ((RelativeLayout.LayoutParams) webView.getLayoutParams()).topMargin = topMargin;
            webView.requestLayout();
        }

        public Wrapper(WebView webView) {
            this.webView = webView;
        }
    }
}
