package com.wj.kindergarten.ui.func;

        import android.content.Intent;
        import android.graphics.drawable.Drawable;
        import android.os.Handler;
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
        import com.wj.kindergarten.bean.BaseModel;
        import com.wj.kindergarten.bean.CallTransfer;
        import com.wj.kindergarten.net.RequestResultI;
        import com.wj.kindergarten.net.request.UserRequest;
        import com.wj.kindergarten.ui.BaseActivity;
        import com.wj.kindergarten.ui.more.CallUtils;
        import com.wj.kindergarten.ui.other.RatingBarView;
        import com.wj.kindergarten.ui.recuitstudents.fragments.AssessSchoolFragment;
        import com.wj.kindergarten.ui.recuitstudents.fragments.MineSchoolIntroduceFragment;
        import com.wj.kindergarten.utils.GloablUtils;
        import com.wj.kindergarten.utils.HintInfoDialog;
        import com.wj.kindergarten.utils.ShareUtils;
        import com.wj.kindergarten.utils.Utils;
        import com.wj.kindergarten.wrapper.WrapperFl;

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


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.mine_school_layout;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        //TODO 开启一个假动画


        tv_coll = (TextView)findViewById(R.id.textview_1_1);
        iv_coll = (ImageView)findViewById(R.id.imageView_1_1);

        ll_school_medal_one = (LinearLayout)findViewById(R.id.ll_school_medal_one);
        item_class_name = (TextView) findViewById(R.id.item_class_name);
        item_special_course_list_view_tv_adresss = (TextView) findViewById(R.id.item_special_course_list_view_tv_adresss);
        item_special_course_list_view_tv_distance = (TextView) findViewById(R.id.item_special_course_list_view_tv_distance);
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
                switch (tab.getPosition()){
                    case 0:
                        getSupportFragmentManager().beginTransaction().show(mineSchoolFragment).hide(recruitFragment).hide(assessSchoolFragment).commit();
                        break;
                    case 1:
                        if(!isFirst){
                            isFirst = true;
                            recruitFragment.setUrl("http://www.baidu.com");
                        }
                        getSupportFragmentManager().beginTransaction().show(recruitFragment).hide(mineSchoolFragment).hide(assessSchoolFragment).commit();
                        break;
                    case 2:
                        if(!isOnce){
                            isOnce = true;
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
}
