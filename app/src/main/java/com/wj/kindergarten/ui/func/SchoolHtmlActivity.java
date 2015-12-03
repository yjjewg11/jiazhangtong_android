package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.CallTransfer;
import com.wj.kindergarten.bean.SchoolDetail;
import com.wj.kindergarten.bean.SchoolDetailList;
import com.wj.kindergarten.bean.TrainSchoolInfo;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.more.CallUtils;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.ui.recuitstudents.fragments.SchoolAssessFragment;
import com.wj.kindergarten.ui.recuitstudents.fragments.WebFragment;
import com.wj.kindergarten.ui.recuitstudents.fragments.WebFragment2;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.wrapper.WrapperFl;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tangt on 2015/11/27.
 */
public class SchoolHtmlActivity extends BaseActivity {

    private TabLayout tab_layout;
    private RelativeLayout common_rl;
    private TextView tv_coll;
    private ImageView iv_coll;
    private HintInfoDialog dialog;
    private WebFragment recuritFragment;
    private WebFragment2 schoolFragment;
    private SchoolAssessFragment schoolAssessFragment;
    private TrainSchoolInfo schoolInfo;
    private HintInfoDialog hintDialog;
    private SchoolDetailList sdl;
    private SchoolDetail schoolDetail;
    public static final int SUCCESS_GET_DATA = 101;
    private boolean isFirst;
    private boolean isOnce;
    private FrameLayout anim_fl;
    private int upInstance = 100;
    private ImageView iv_madle;
    private TextView tv_study_people;


    public HintInfoDialog getHintDialog() {
        return hintDialog;
    }

    private ObjectAnimator animtor;

    public ObjectAnimator getAnimtor() {
        return animtor;
    }

    private WrapperFl wrapper;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS_GET_DATA:
                    if(TextUtils.isEmpty(sdl.getDistance())){
                        item_special_course_list_view_tv_distance.setVisibility(View.INVISIBLE);
                    }else{
                        item_special_course_list_view_tv_distance.setVisibility(View.VISIBLE);
                    }
                    item_class_name.setText(schoolDetail.getBrand_name());
                    tv_study_people.setText(schoolDetail.getCt_study_students()+"人就读");
                    tv_study_people.setTextColor(Color.parseColor("#ff4966"));
                    item_special_course_list_view__rating_bar.setFloatStar(schoolDetail.getCt_stars(), true);
                    ImageLoaderUtil.displayMyImage(schoolDetail.getImg(),item_special_course_list_view_image_view);
                    item_special_course_list_view_tv_adresss.setText(schoolDetail.getAddress());
                    item_special_course_list_view_tv_distance.setText(sdl.getDistance());
                    if(schoolDetail.getSummary() != null && schoolDetail.getSummary().split(",") != null){
                        for(String s : schoolDetail.getSummary().split(",")){
                            TextView textView = new TextView(SchoolHtmlActivity.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                            textView.setText(s);
                            textView.setTextSize(11);
                            ll_school_medal_one.addView(textView,params);
                        }
                    }else{
                        iv_madle.setVisibility(View.GONE);
                        ll_school_medal_one.setVisibility(View.GONE);

                    }
                    if(!sdl.isFavor()){
                        tv_coll.setText("已收藏");
                        iv_coll.setImageResource(R.drawable.shoucangnewred);
                    }else{
                        tv_coll.setText("收藏");
                        iv_coll.setImageResource(R.drawable.shoucangnewwhtire);
                    }
                    schoolFragment.setUrl(sdl.getObj_url());
                    tab_layout.getLocationInWindow(fl_location);
                    //创建移除动画
                    wrapper =new WrapperFl(anim_fl);
                    animtor = ObjectAnimator.ofInt(wrapper ,"topMargin",-fl_location[1]);
                    animtor.setDuration(1000);
                    animtor.setInterpolator(new AccelerateDecelerateInterpolator());
                    break;
            }
        }
    };
    private int [] fl_location = new int[2];
    private LinearLayout ll_school_medal_one;
    private TextView item_class_name;
    private TextView item_special_course_list_view_tv_adresss;
    private TextView item_special_course_list_view_tv_distance;
    private RatingBarView item_special_course_list_view__rating_bar;
    private ImageView item_special_course_list_view_image_view;
    private RelativeLayout[] relativeLayouts;

    public SchoolDetailList getSdl() {
        return sdl;
    }

    public SchoolDetail getSchoolDetail() {
        return schoolDetail;
    }

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_school_html;
    }

    @Override
    protected void setNeedLoading() {
        //不调用isneeding方法，加载会让tablayout失效，原因不明
    }

    @Override
    protected void onCreate() {
        //TODO传递数据
        hintDialog = new HintInfoDialog(this);
        hintDialog.show();
        Intent intent = getIntent();
        schoolInfo = (TrainSchoolInfo)intent.getSerializableExtra("object");
        //获取学校详情
        getSchoolTrainInfo();

        tv_study_people = (TextView)findViewById(R.id.tv_study_people);
        anim_fl = (FrameLayout)findViewById(R.id.anim_fl);
        iv_madle = (ImageView)findViewById(R.id.iv_madle);
        ll_school_medal_one = (LinearLayout)findViewById(R.id.ll_school_medal_one);
        item_class_name = (TextView) findViewById(R.id.item_class_name);
        item_special_course_list_view_tv_adresss = (TextView) findViewById(R.id.item_special_course_list_view_tv_adresss);
        item_special_course_list_view_tv_distance = (TextView) findViewById(R.id.item_special_course_list_view_tv_distance);
        item_special_course_list_view__rating_bar = (RatingBarView)findViewById(R.id.item_special_course_list_view__rating_bar);
        item_special_course_list_view_image_view = (ImageView)findViewById(R.id.item_special_course_list_view_image_view);

        titleCenterTextView.setText(schoolInfo.getBrand_name());
        tv_coll = (TextView)findViewById(R.id.textview_1_1);
        iv_coll = (ImageView)findViewById(R.id.imageView_1_1);
        tab_layout = (TabLayout)findViewById(R.id.common_tab_layout);
        tab_layout.addTab(tab_layout.newTab().setText("学校简介"));
        tab_layout.addTab(tab_layout.newTab().setText("招生计划"));
        tab_layout.addTab(tab_layout.newTab().setText("家长评论"));

        //添加底部选择框按钮
        setViews();

        common_rl = (RelativeLayout)findViewById(R.id.common_rl);
        schoolFragment = new WebFragment2();
        recuritFragment = new WebFragment();
        schoolAssessFragment = new SchoolAssessFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.common_rl,schoolFragment).add(R.id.common_rl,recuritFragment).
                add(R.id.common_rl,schoolAssessFragment).hide(recuritFragment).hide(schoolAssessFragment).show(schoolFragment).commit();

        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(wrapper.getTopMargin() < 0){
                    animtor.reverse();
                }

                switch (tab.getPosition()) {


                    //判断是否进行动画
                    case 0:

                        getSupportFragmentManager().beginTransaction().show(schoolFragment).hide(recuritFragment).hide(schoolAssessFragment).commit();
                        break;
                    case 1:
                        if(!isFirst){
                            isFirst = true;
                            recuritFragment.setUrl(sdl.getRecruit_url());
                        }
                        getSupportFragmentManager().beginTransaction().show(recuritFragment).hide(schoolFragment).hide(schoolAssessFragment).commit();
                        break;
                    case 2:
                        if(!isOnce){
                            isOnce = true;
                            schoolAssessFragment.queryAssess();
                        }
                        getSupportFragmentManager().beginTransaction().show(schoolAssessFragment).hide(recuritFragment).hide(schoolFragment).commit();
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
     }

    //根据学校uuid获取学校详情
    private void getSchoolTrainInfo() {
        UserRequest.getTrainSchoolDetailFromRecruit(this, schoolInfo.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                sdl = (SchoolDetailList) domain;
                if(sdl != null && sdl.getData() != null){
                  schoolDetail =   sdl.getData();
                    mHandler.sendEmptyMessage(SUCCESS_GET_DATA);
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


    private void store() {
        dialog = new HintInfoDialog(this, "收藏中，请稍后...");
        dialog.show();
        UserRequest.store(this, schoolDetail.getBrand_name(), 82, schoolDetail.getUuid(), "", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(SchoolHtmlActivity.this, "收藏成功");
                store1();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(SchoolHtmlActivity.this, message);
                }
                dialog.dismiss();
            }
        });
    }

    private void store1() {
        Drawable drawable = getResources().getDrawable(R.drawable.shoucangnewred);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
//        tvStore.setCompoundDrawables(null, drawable, null, null);
        iv_coll.setImageDrawable(drawable);
        tv_coll.setText("已收藏");
        tv_coll.setTextColor(getResources().getColor(R.color.title_bg));
        sdl.setIsFavor(false);
    }

    private void store2() {
        Drawable drawable = getResources().getDrawable(R.drawable.shoucangnewwhtire);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
//        tvStore.setCompoundDrawables(null, drawable, null, null);
        iv_coll.setImageDrawable(drawable);
        tv_coll.setText("收藏");
        tv_coll.setTextColor(getResources().getColor(R.color.color_929292));
        sdl.setIsFavor(true);
    }

    private void cancelStore() {
        dialog = new HintInfoDialog(SchoolHtmlActivity.this, "取消收藏中，请稍后...");
        dialog.show();
        UserRequest.cancelStore(true, SchoolHtmlActivity.this, schoolDetail.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(SchoolHtmlActivity.this, "取消收藏成功");
                store2();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(SchoolHtmlActivity.this, message);
                }
                dialog.dismiss();
            }
        });
    }



    private void setViews() {

        relativeLayouts = new RelativeLayout[]{
                (RelativeLayout)findViewById(R.id.train_course_tab_shoucang),
                (RelativeLayout)findViewById(R.id.train_course_tab_share),
                (RelativeLayout)findViewById(R.id.train_course_tab_interaction),
                (RelativeLayout)findViewById(R.id.train_course_tab_ask)
        };
        View.OnClickListener listenersss = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.train_course_tab_shoucang:
                        //发送接口
                        if(tv_coll.getText().toString().equals("收藏")){
                            store();
                        }else{
                            cancelStore();
                        }
                        break;
                    case R.id.train_course_tab_share:

                        String content = schoolDetail.getBrand_name();
                        if(Utils.isNull(content) == null || content == null){
//                            content = osc.getTitle();
                        }
                        String shareUrl = "";
                        if(tab_layout.getSelectedTabPosition() == 1){
                            shareUrl = sdl.getRecruit_url();
                        }else{
                            shareUrl = sdl.getShare_url();
                        }
                        ShareUtils.showShareDialog(SchoolHtmlActivity.this, v, content
                                , "", schoolDetail.getImg(), shareUrl, false);

//                            ToastUtils.showMessage("暂无分享内容!");


                        break;
                    case R.id.train_course_tab_interaction:
                        //启动互动界面
                        Intent intent = new Intent(SchoolHtmlActivity.this,CourseInteractionListActivity.class);
                        intent.putExtra("newsuuid",schoolDetail.getUuid());
                        intent.putExtra("type", NormalReplyListActivity.TRAIN_COURSE);
                        startActivity(intent);
                        break;
                    case R.id.train_course_tab_ask:
                        CallUtils.showCall(SchoolHtmlActivity.this, schoolDetail.getLink_tel().split(GloablUtils.SPLIT_STIRNG_MOBILE_NUMBER), new CallTransfer(schoolDetail.getUuid(), 82));
                        break;
                }
            }
        };

        for(RelativeLayout relativeLayout:relativeLayouts){
            relativeLayout.setOnClickListener(listenersss);
        }
    }

    private JudgeIsTop judgeIsTop;

    public void setJudgeIsTop(JudgeIsTop judgeIsTop) {
        this.judgeIsTop = judgeIsTop;
    }

    int firstY;


    interface JudgeIsTop{
        boolean judgeIsTop();
    }
}
