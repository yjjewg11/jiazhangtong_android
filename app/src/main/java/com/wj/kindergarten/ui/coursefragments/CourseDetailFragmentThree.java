package com.wj.kindergarten.ui.coursefragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.handmark.pulltorefresh.library.extras.PullToRefreshWebView2;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.OnceSpecialCourse;
import com.wj.kindergarten.bean.OnceSpecialCourseList;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.CourseDetailIntroduceFragment;
import com.wj.kindergarten.ui.func.MineCourseDetailActivity;
import com.wj.kindergarten.ui.func.SpecialCourseInfoActivity;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.ui.other.TopWebView;
import com.wj.kindergarten.ui.webview.LoadHtmlActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.WindowUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tangt on 2015/11/19.
 */
public class CourseDetailFragmentThree extends Fragment implements View.OnTouchListener,GestureDetector.OnGestureListener {
    private View view;
    private TextView study_people_detail;
    private TextView fit_age;
    private RatingBarView rating_bar;
    private TextView course_detail_train_class_name;
    private TextView detail_course;
    private TextView teach_place;
    private TextView course_spend_time;
    private TextView nornal_course_price;
    private TextView coupon_price;
    private TopWebView course_detail_info;
    private LinearLayout more_assess_linera;
    private ImageView iv_heading;
    private TextView tv_coll;
    private ImageView iv_coll;
    private RelativeLayout rl_price;
    private RelativeLayout rl_free_price;
    private BaseActivity activity;
    private OnceSpecialCourse osc;
    private OnceSpecialCourseList ocs;
    private RadioGroup three_rb_all;
    private FrameLayout head_title;
    private FrameLayout btom_llll;
    private TextView black_width;
    private Animation anim;
    public static final int FLING_SPEED = 500;
    private RelativeLayout.LayoutParams params;
    private LinearLayout top_all_content;
    private GestureDetector gd;
    private Animation animShow;
    private int[] location;
    private LinearLayout rl_replace_content;
    private BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,1,1000*60, TimeUnit.MILLISECONDS,queue);
    private Handler handler = new Handler();
    private LinearLayout lllll_1111;
    private LinearLayout.LayoutParams ll_params;
    private int height;
    private int llHeight;
    private ObjectAnimator upLinear;
    private WrapperView wrapper;
    private ObjectAnimator upWeb;
    private MoveLinear wrapLinear;
    private ObjectAnimator upScale;
    private float downY;
    private int moveGloal;
    private int upInstance = 60;
    private boolean isOnce;
//    private LeftRunnable lefeRun;
//    private RightRunnable rightRun;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) return view;
        view = inflater.inflate(R.layout.course_detail_fragment_three, null);
        activity = (SpecialCourseInfoActivity)getActivity();
        lllll_1111 = (LinearLayout) view.findViewById(R.id.lllll_1111);
        rl_replace_content = (LinearLayout) view.findViewById(R.id.rl_replace_content);
        top_all_content = (LinearLayout) view.findViewById(R.id.top_all_content);
        study_people_detail = (TextView) view.findViewById(R.id.study_people_detail);
        fit_age = (TextView) view.findViewById(R.id.fit_age);
        rating_bar = (RatingBarView) view.findViewById(R.id.item_special_course_list_view__rating_bar);
        course_detail_train_class_name = (TextView) view.findViewById(R.id.course_detail_train_class_name);
        detail_course = (TextView) view.findViewById(R.id.detail_course);
        teach_place = (TextView) view.findViewById(R.id.teach_place);
        course_spend_time = (TextView) view.findViewById(R.id.course_spend_time);
        nornal_course_price = (TextView) view.findViewById(R.id.nornal_course_price);
        coupon_price = (TextView) view.findViewById(R.id.coupon_price);
        course_detail_info = (TopWebView) view.findViewById(R.id.course_detail_info);
        iv_heading = (ImageView) view.findViewById(R.id.once_iv);
        rl_price = (RelativeLayout) view.findViewById(R.id.rl_price);
        rl_free_price = (RelativeLayout) view.findViewById(R.id.rl_free_price);
        gd = new GestureDetector(getActivity(), this);
//        activity.setWebView(course_detail_info);
        //TODO
        course_detail_info.setOnTouchListener(this);
        //获取webview在屏幕中的位置
        location = new int[2];
        gd = new GestureDetector(getActivity(), this);
        ll_params = (LinearLayout.LayoutParams) lllll_1111.getLayoutParams();
        wrapper = new WrapperView(course_detail_info);
        wrapLinear = new MoveLinear(lllll_1111);
//                /下拉出现全屏


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!isOnce){
            isOnce = true;
        } else {
            return;
        }
        if(activity instanceof  SpecialCourseInfoActivity){
            SpecialCourseInfoActivity activitySif = (SpecialCourseInfoActivity) getActivity();
            osc =   activitySif.getOsc();
            ocs = activitySif.getOcs();
        }else if(activity instanceof MineCourseDetailActivity){
            MineCourseDetailActivity activityMca = (MineCourseDetailActivity) getActivity();
            CourseDetailIntroduceFragment fragment = (CourseDetailIntroduceFragment) activityMca.fragments[1];
            ocs = fragment.getOscs();
            if(ocs != null)
            osc = fragment.getOscs().getData();
        }
        if(osc == null || osc == null) return ;
        ImageLoaderUtil.displayMyImage(osc.getLogo(), iv_heading);
        String people = "<font color='#ff4966'>"+osc.getCt_study_students()+"人已学</font>";
        study_people_detail.setText(Html.fromHtml(people));
        fit_age.setText(ocs.getAge_min_max());
        course_detail_train_class_name.setText("" + osc.getTitle());
        rating_bar.setFloatStar(osc.getCt_stars(), true);
        teach_place.setText("" +osc.getAddress());
        detail_course.setText("" + osc.getSubtype());
        try{
            course_detail_info.loadUrl(ocs.getObj_url());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(osc.getFees() < 1){
            rl_free_price.setVisibility(View.GONE);
        }else{
            rl_free_price.setVisibility(View.VISIBLE);
        }
        if(osc.getDiscountfees() < 1){
            rl_price.setVisibility(View.GONE);
        }else{
            rl_price.setVisibility(View.VISIBLE);
        }
        nornal_course_price.setText("" + osc.getDiscountfees() + " 元");
        coupon_price.setText("" + osc.getFees() + " 元");

    }

    public void setText(OnceSpecialCourseList ocs){
        this.ocs = ocs;
        this.osc = ocs.getData();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downY = event.getY();
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(judgeIsTop() && isLocationTop){
//                    radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMe
// asuredHeight() * moveDeltaY));
                moveGloal = (int)(( event.getY()-downY)/2);
                if(moveGloal < 0){
                    moveGloal = 0;
                }
                if(moveGloal >= upInstance){
                    moveGloal = upInstance;
                }

                ((RelativeLayout.LayoutParams)course_detail_info.getLayoutParams()).setMargins(0,moveGloal,0,0);
                course_detail_info.requestLayout();
            }
        }

        if(event.getAction() == MotionEvent.ACTION_UP){
            if(((RelativeLayout.LayoutParams)course_detail_info.getLayoutParams()).topMargin >= upInstance){
                if (isLocationTop) {
                    isLocationTop = false;
                    upWeb.reverse();
                    upLinear.reverse();
//                upScale.reverse();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ((RelativeLayout.LayoutParams)course_detail_info.getLayoutParams()).topMargin = 0;
//                            course_detail_info.requestLayout();
//                        }
//                    },1000);

                }
            }else{

//                ((RelativeLayout.LayoutParams) course_detail_info.getLayoutParams()).topMargin = 0;
//                course_detail_info.requestLayout();
            }

            ObjectAnimator o1 =  ObjectAnimator.ofInt(wrapper, "topMagin", -moveGloal).setDuration(500);
            o1.setInterpolator(new DecelerateInterpolator());
            o1.start();
        }
        return gd.onTouchEvent(event);
    }

    int[] locationNow = new int[2];
    boolean isLocationTop;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        //如果向上滑，并且速度大于200，那么向上跳转。

        course_detail_info.getLocationInWindow(locationNow);
        if(height == 0){
            float midu =  WindowUtils.dm.density;
            lllll_1111.getLocationInWindow(location);
            llHeight = location[1];
            height = locationNow[1];
            int[] b_l = new int[2];
            ((SpecialCourseInfoActivity)activity).getBtom_llll().getLocationInWindow(b_l);
            upScale = ObjectAnimator.ofFloat(lllll_1111, "alpha", 1.0f, 0.0f);
            upWeb = ObjectAnimator.ofInt(wrapper, "height", b_l[1] - height, b_l[1] - llHeight);
            upLinear = ObjectAnimator.ofInt(wrapLinear, "topMagin", 0, -(height - llHeight));
            setAnimator(upWeb);
            setAnimator(upLinear);
            setAnimator(upScale);
            upScale.setDuration(500);
        }

        if(e1.getY() - e2.getY() > 100 && Math.abs(velocityY) > FLING_SPEED && !isLocationTop){

            upWeb.start();
            upLinear.start();
//            upScale.start();

            isLocationTop = true;

        }


        return false;
    }

    private boolean judgeIsTop() {
        return course_detail_info.isTop();
    }

    private void setAnimator(ObjectAnimator animator){
        animator.setDuration(600);
        animator.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public void onStart() {
        super.onStart();
//        course_detail_info.getRefreshableView().getLocationInWindow(location);
    }

    class WrapperView{
        private View webView;
        private int height;
        private int topMagin;


        public void setTopMagin(int topMagin) {
            ((RelativeLayout.LayoutParams)webView.getLayoutParams()).topMargin = topMagin;
            webView.requestLayout();
        }

        public int getTopMagin() {
            return ((RelativeLayout.LayoutParams)webView.getLayoutParams()).topMargin;
        }

        public WrapperView(View webView) {
            this.webView = webView;
        }

        public int getHeight() {
            return webView.getLayoutParams().height;
        }

        public void setHeight(int height) {
            webView.getLayoutParams().height = height;
            webView.requestLayout();
        }
    }

    class MoveLinear{
        private LinearLayout linear;
        private int topMagin;

        public MoveLinear(LinearLayout linear) {
            this.linear = linear;
        }

        public int getTopMagin() {
            return ((LinearLayout.LayoutParams)linear.getLayoutParams()).topMargin;
        }

        public void setTopMagin(int topMagin) {
            ((LinearLayout.LayoutParams)linear.getLayoutParams()).topMargin = topMagin;
             linear.requestLayout();
        }
    }
}
