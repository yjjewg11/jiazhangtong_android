package com.wj.kindergarten.ui.func;

        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.drawable.Drawable;
        import android.os.Handler;
        import android.os.Message;
        import android.support.design.widget.TabLayout;
        import android.view.View;
        import android.view.animation.AccelerateDecelerateInterpolator;
        import android.view.animation.DecelerateInterpolator;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.nineoldandroids.animation.ObjectAnimator;
        import com.wenjie.jiazhangtong.R;
        import com.wj.kindergarten.CGApplication;
        import com.wj.kindergarten.bean.BaseModel;
        import com.wj.kindergarten.bean.CallTransfer;
        import com.wj.kindergarten.bean.ChildInfo;
        import com.wj.kindergarten.bean.Group;
        import com.wj.kindergarten.bean.SchoolDetail;
        import com.wj.kindergarten.bean.SchoolDetailList;
        import com.wj.kindergarten.net.RequestResultI;
        import com.wj.kindergarten.net.request.UserRequest;
        import com.wj.kindergarten.ui.BaseActivity;
        import com.wj.kindergarten.ui.more.CallUtils;
        import com.wj.kindergarten.ui.other.RatingBarView;
        import com.wj.kindergarten.ui.recuitstudents.fragments.AssessSchoolFragment;
        import com.wj.kindergarten.ui.recuitstudents.fragments.MineSchoolIntroduceFragment;
        import com.wj.kindergarten.utils.GloablUtils;
        import com.wj.kindergarten.utils.HintInfoDialog;
        import com.wj.kindergarten.utils.ImageLoaderUtil;
        import com.wj.kindergarten.utils.ShareUtils;
        import com.wj.kindergarten.utils.Utils;
        import com.wj.kindergarten.wrapper.WrapperFl;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by tangt on 2015/12/2.
 */
public class MineSchoolActivity extends BaseActivity{
    private TabLayout tab_layout;
    private AssessSchoolFragment assessSchoolFragment;
    private MineSchoolIntroduceFragment recruitFragment;
    private boolean isFirst;
    private boolean isOnce;
    private MineSchoolIntroduceFragment mineSchoolFragment;
    private LinearLayout ll_school_medal_one;
    private TextView item_class_name;
    private TextView item_special_course_list_view_tv_adresss;
    private TextView item_special_course_list_view_tv_distance;
    private RatingBarView item_special_course_list_view__rating_bar;
    private ImageView item_special_course_list_view_image_view;
    private FrameLayout anim_mine_fl;
    private RelativeLayout[] relativeLayouts;
    private TextView tv_coll;
    private ImageView iv_coll;
    private SchoolDetailList sdl;
    private FrameLayout tab_layout_fl;
    private int[] location = new int[2];
    private SchoolDetail schoolDatil;
    private TextView tv_study_people;
    private ImageView iv_madle;
    private String groupuuid;
    private RelativeLayout rl_all_content;

    public String getGroupuuid() {
        return groupuuid;
    }

    public SchoolDetailList getSdl() {
        return sdl;
    }

    private static final int GET_DATA_SUCCESS = 1001;
    private ObjectAnimator animtor;

    public ObjectAnimator getAnimtor() {
        return animtor;
    }

    private Wrapper wrapper;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case GET_DATA_SUCCESS:

                    titleCenterTextView.setText(schoolDatil.getBrand_name());
                    mineSchoolFragment.setUrl(sdl.getObj_url());
                    if(schoolDatil.getSummary() != null){
                        for(String s : schoolDatil.getSummary().split(",")){
                            TextView textView = new TextView(MineSchoolActivity.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                            textView.setText(s);
                            textView.setTextSize(11);
                            ll_school_medal_one.addView(textView,params);
                        };
                    }else{
                        iv_madle.setVisibility(View.GONE);
                        ll_school_medal_one.setVisibility(View.GONE);
                    }
                    tv_study_people.setTextColor(Color.parseColor("#ff4966"));
                    tv_study_people.setText(schoolDatil.getCt_study_students()+"人就读");
                    item_class_name.setText(sdl.getData().getBrand_name());
                    item_special_course_list_view_tv_adresss.setText(schoolDatil.getAddress());
                    if(schoolDatil.getDistance() == null){
                        item_special_course_list_view_tv_distance.setVisibility(View.INVISIBLE);
                    }
                    item_special_course_list_view_tv_distance.setText(schoolDatil.getDistance());
                    item_special_course_list_view__rating_bar.setFloatStar(schoolDatil.getCt_stars(), true);
                    ImageLoaderUtil.displayMyImage(sdl.getData().getImg(), item_special_course_list_view_image_view);

                    //创建动画
                    tab_layout_fl.getLocationInWindow(location);
                    wrapper = new Wrapper(anim_mine_fl);
                    animtor = ObjectAnimator.ofInt(wrapper,"topMargin",-location[1]).setDuration(1000);
                    break;
            }
        }
    };


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.mine_school_layout;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void titleRightButtonListener() {
        String shareUrl = null;
        if(tab_layout.getSelectedTabPosition() == 0){
            shareUrl = sdl.getShare_url();
        }else if(tab_layout.getSelectedTabPosition() == 1){
            shareUrl = sdl.getRecruit_url();
        }
        ShareUtils.showShareDialog(this, titleRightImageView, schoolDatil.getBrand_name(),
                schoolDatil.getBrand_name(), schoolDatil.getImg(), shareUrl, false);
    }


    @Override
    protected void onCreate() {


        groupuuid = getIntent().getStringExtra("groupuuid");

        tv_coll = (TextView)findViewById(R.id.textview_1_1);
        iv_coll = (ImageView)findViewById(R.id.imageView_1_1);
        setTitleText("问界互动家园",R.drawable.school_share);
        rl_all_content = (RelativeLayout)findViewById(R.id.rl_all_content);
        rl_all_content.setBackground(null);
        iv_madle = (ImageView)findViewById(R.id.iv_madle);
        tab_layout_fl = (FrameLayout)findViewById(R.id.tab_layout_fl);
        ll_school_medal_one = (LinearLayout)findViewById(R.id.contain_include_medal);
        item_class_name = (TextView) findViewById(R.id.item_class_name);
        tv_study_people = (TextView)findViewById(R.id.tv_study_people);
        item_special_course_list_view_tv_adresss = (TextView) findViewById(R.id.item_special_course_list_view_tv_adresss);
        item_special_course_list_view_tv_distance = (TextView) findViewById(R.id.recruit_student_tv_distance);
        item_special_course_list_view__rating_bar = (RatingBarView)findViewById(R.id.item_special_course_list_view__rating_bar);
        item_special_course_list_view_image_view = (ImageView)findViewById(R.id.item_special_course_list_view_image_view);
        item_special_course_list_view__rating_bar.setFloatStar(50,true);
        tab_layout = (TabLayout)findViewById(R.id.common_tab_layout);
        tab_layout.addTab(tab_layout.newTab().setText("学校简介"));
        tab_layout.addTab(tab_layout.newTab().setText("招生计划"));
        tab_layout.addTab(tab_layout.newTab().setText("评价学校"));
        anim_mine_fl = (FrameLayout)findViewById(R.id.anim_mine_fl);

        mineSchoolFragment = new MineSchoolIntroduceFragment();
        recruitFragment = new MineSchoolIntroduceFragment();
        assessSchoolFragment = new AssessSchoolFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mine_fragment_container,mineSchoolFragment).add(R.id.mine_fragment_container,assessSchoolFragment).
                add(R.id.mine_fragment_container,recruitFragment).hide(recruitFragment).hide(assessSchoolFragment).show(mineSchoolFragment).commit();
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(wrapper.getTopMargin() < 0){
                    animtor.reverse();
                }
                switch (tab.getPosition()){
                    case 0:
                        getSupportFragmentManager().beginTransaction().show(mineSchoolFragment).hide(recruitFragment).hide(assessSchoolFragment).commit();
                        break;
                    case 1:
                        if(!isFirst){
                            isFirst = true;
                            recruitFragment.setUrl(sdl.getRecruit_url());
                        }
                        getSupportFragmentManager().beginTransaction().show(recruitFragment).hide(mineSchoolFragment).hide(assessSchoolFragment).commit();
                        break;
                    case 2:
                        if(!isOnce){
                            isOnce = true;
                            assessSchoolFragment.queryAssess();
                        }
                        getSupportFragmentManager().beginTransaction().show(assessSchoolFragment).hide(recruitFragment).hide(mineSchoolFragment).commit();
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

        //获取学校详情
        UserRequest.getTrainSchoolDetailFromRecruit(this, getGroupuuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                sdl = (SchoolDetailList) domain;
                if(sdl != null && sdl.getData() != null){
                    schoolDatil =  sdl.getData();
                    handler.sendEmptyMessage(GET_DATA_SUCCESS);
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

//    private void setViews() {
//
//        relativeLayouts = new RelativeLayout[]{
//                (RelativeLayout)findViewById(R.id.train_course_tab_shoucang),
//                (RelativeLayout)findViewById(R.id.train_course_tab_share),
//                (RelativeLayout)findViewById(R.id.train_course_tab_interaction),
//                (RelativeLayout)findViewById(R.id.train_course_tab_ask)
//        };
//        View.OnClickListener listenersss = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()){
//                    case R.id.train_course_tab_shoucang:
//                        //发送接口
//                        if(tv_coll.getText().toString().equals("收藏")){
//                            store();
//                        }else{
//                            cancelStore();
//                        }
//                        break;
//                    case R.id.train_course_tab_share:
//
//                        String content = schoolDetail.getBrand_name();
//                        if(Utils.isNull(content) == null || content == null){
////                            content = osc.getTitle();
//                        }
//                        String shareUrl = "";
//                        if(tab_layout.getSelectedTabPosition() == 1){
//                            shareUrl = sdl.getRecruit_url();
//                        }else{
//                            shareUrl = sdl.getShare_url();
//                        }
//                        ShareUtils.showShareDialog(MineSchoolActivity.this, v, content
//                                , "", schoolDetail.getImg(), shareUrl, false);
//
////                            ToastUtils.showMessage("暂无分享内容!");
//
//
//                        break;
//                    case R.id.train_course_tab_interaction:
//                        //启动互动界面
//                        Intent intent = new Intent(MineSchoolActivity.this,CourseInteractionListActivity.class);
//                        intent.putExtra("newsuuid",schoolDetail.getUuid());
//                        intent.putExtra("type", NormalReplyListActivity.TRAIN_COURSE);
//                        startActivity(intent);
//                        break;
//                    case R.id.train_course_tab_ask:
//                        CallUtils.showCall(MineSchoolActivity.this, schoolDetail.getLink_tel().split(GloablUtils.SPLIT_STIRNG_MOBILE_NUMBER), new CallTransfer(schoolDetail.getUuid(), 82));
//                        break;
//                }
//            }
//        };
//
//        for(RelativeLayout relativeLayout:relativeLayouts){
//            relativeLayout.setOnClickListener(listenersss);
//        }
//    }
//
//    private void store() {
//        dialog = new HintInfoDialog(this, "收藏中，请稍后...");
//        dialog.show();
//        UserRequest.store(this, schoolDetail.getBrand_name(), 82, schoolDetail.getUuid(), "", new RequestResultI() {
//            @Override
//            public void result(BaseModel domain) {
//                dialog.dismiss();
//                Utils.showToast(MineSchoolActivity.this, "收藏成功");
//                store1();
//            }
//
//            @Override
//            public void result(List<BaseModel> domains, int total) {
//
//            }
//
//            @Override
//            public void failure(String message) {
//                if (!Utils.stringIsNull(message)) {
//                    Utils.showToast(MineSchoolActivity.this, message);
//                }
//                dialog.dismiss();
//            }
//        });
//    }
//
//    private void store1() {
//        Drawable drawable = getResources().getDrawable(R.drawable.store2);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
////        tvStore.setCompoundDrawables(null, drawable, null, null);
//        iv_coll.setImageDrawable(drawable);
//        tv_coll.setText("已收藏");
//        tv_coll.setTextColor(getResources().getColor(R.color.title_bg));
//        sdl.setIsFavor(false);
//    }
//
//    private void store2() {
//        Drawable drawable = getResources().getDrawable(R.drawable.store1);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
////        tvStore.setCompoundDrawables(null, drawable, null, null);
//        iv_coll.setImageDrawable(drawable);
//        tv_coll.setText("收藏");
//        tv_coll.setTextColor(getResources().getColor(R.color.color_929292));
//        sdl.setIsFavor(true);
//    }
//
//    private void cancelStore() {
//        dialog = new HintInfoDialog(SchoolHtmlActivity.this, "取消收藏中，请稍后...");
//        dialog.show();
//        UserRequest.cancelStore(true, MineSchoolActivity.this, schoolDetail.getUuid(), new RequestResultI() {
//            @Override
//            public void result(BaseModel domain) {
//                dialog.dismiss();
//                Utils.showToast(MineSchoolActivity.this, "取消收藏成功");
//                store2();
//            }
//
//            @Override
//            public void result(List<BaseModel> domains, int total) {
//
//            }
//
//            @Override
//            public void failure(String message) {
//                if (!Utils.stringIsNull(message)) {
//                    Utils.showToast(MineSchoolActivity.this, message);
//                }
//                dialog.dismiss();
//            }
//        });
//    }

    class Wrapper{
        FrameLayout frameLayout;
        private int topMargin;

        public Wrapper(FrameLayout frameLayout) {
            this.frameLayout = frameLayout;
        }

        public int getTopMargin() {
            return ((RelativeLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin;
        }

        public void setTopMargin(int topMargin) {
            ((RelativeLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = topMargin;
            frameLayout.requestLayout();
        }
    }
}
