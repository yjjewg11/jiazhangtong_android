package com.wj.kindergarten.ui.func;

import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.os.Handler;
        import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
        import android.text.TextUtils;
        import android.view.View;
        import android.webkit.WebView;
        import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
        import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
        import android.widget.ScrollView;
        import android.widget.TextView;

        import com.google.gson.annotations.Expose;
        import com.handmark.pulltorefresh.library.PullToRefreshBase;
        import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
        import com.wenjie.jiazhangtong.R;
        import com.wj.kindergarten.bean.BaseModel;
        import com.wj.kindergarten.bean.CallTransfer;
        import com.wj.kindergarten.bean.MoreDiscuss;
        import com.wj.kindergarten.bean.MoreDiscussList;
        import com.wj.kindergarten.bean.OnceSpecialCourse;
        import com.wj.kindergarten.bean.OnceSpecialCourseList;
        import com.wj.kindergarten.bean.SchoolDetail;
        import com.wj.kindergarten.bean.SchoolDetailList;
        import com.wj.kindergarten.bean.SpecialCourseInfoObject;
        import com.wj.kindergarten.net.RequestResultI;
        import com.wj.kindergarten.net.request.UserRequest;
        import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.coursefragments.CourseDetailFragmentThree;
import com.wj.kindergarten.ui.coursefragments.FamilyAssessFragmentThree;
import com.wj.kindergarten.ui.coursefragments.SchoolFragmentThree;
import com.wj.kindergarten.ui.func.CourseInteractionListActivity;
import com.wj.kindergarten.ui.func.NormalReplyListActivity;
import com.wj.kindergarten.ui.more.CallUtils;
import com.wj.kindergarten.ui.other.AnimHelper;
import com.wj.kindergarten.ui.other.ManDrawLine;
import com.wj.kindergarten.ui.other.PulseAnimator;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.ui.other.ScollTextView;
import com.wj.kindergarten.utils.GloablUtils;
        import com.wj.kindergarten.utils.HintInfoDialog;
        import com.wj.kindergarten.utils.ImageLoaderUtil;
        import com.wj.kindergarten.utils.ShareUtils;
        import com.wj.kindergarten.utils.TimeUtil;
        import com.wj.kindergarten.utils.ToastUtils;
        import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;


import java.util.ArrayList;
        import java.util.List;

//特长课程详细信息页面
public class SpecialCourseInfoActivity extends BaseActivity {
    private ArrayList<MoreDiscuss> moreList =new ArrayList<>();
    private Button bt_more;
    private RatingBarView rating_bar,assess_rb;
    private TextView course_detail_train_class_name, detail_course
            ,teach_place,course_spend_time,nornal_course_price,coupon_price;
    private WebView course_detail_info;
    private LinearLayout more_assess_linera;

    private TextView mobile,time,content;

    private ImageView iv_heading;
    private OnceSpecialCourse osc;
    private WebView tv_school_introduce;
    private RelativeLayout[] relativeLayouts;
    private RadioButton rb_three_corse,rb_three_school,rb_three_assess;
    private View.OnClickListener listener;
    public  CourseDetailFragmentThree courseFragment;
    public FamilyAssessFragmentThree familyFragment;
    public SchoolFragmentThree schoolFragment;
    private FragmentTransaction transion;
    private boolean isAgain;
    private LinearLayout draw_line_ll;
    private ManDrawLine line_text;
    private String[] tag = new String[]{"course_tag","school_tag","assess_tag"};
    private boolean isAssessOne;
    private TextView [] three;
    public  int[] bottomHeight = new int[2];
    private FrameLayout btom_llll;
    private TextView tv_share;

    public FrameLayout getBtom_llll() {
        return btom_llll;
    }

    public OnceSpecialCourseList getOcs() {
        return ocs;
    }

    public OnceSpecialCourse getOsc() {
        return osc;
    }

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //加入评论
                    if(moreList.size()>0){
                        bt_more.setClickable(true);
                        for(int i = 1;i <= (moreList.size()<= 3 ? moreList.size() : 3 );i++){
                            MoreDiscuss md = moreList.get(i-1);
                            View view = View.inflate(SpecialCourseInfoActivity.this,R.layout.more_discuss_item,null);
                            mobile = (TextView) view.findViewById(R.id.more_discuss_mobile_number);
                            time = (TextView) view.findViewById(R.id.more_discuss_time);
                            content = (TextView) view.findViewById(R.id.more_discuss_content);
                            assess_rb = (RatingBarView)view.findViewById(R.id.item_special_course_list_view__rating_bar);
                            String mobiletext = md.getCreate_user();
//                            mobile.setText(mobiletext.substring(0,3)+"******"+mobiletext.substring(9));
                            mobile.setText(""+mobiletext);
                            time.setText(""+ TimeUtil.getYMDTimeFromYMDHMS(md.getCreate_time()));
                            content.setText(""+(TextUtils.isEmpty(md.getContent()) == true ? "当前家长暂无评价!" : md.getContent()));
                            assess_rb.setFloatStar(md.getScore(),true);
                            more_assess_linera.addView(view);
                        }
                    }else{
                        bt_more.setText(""+"暂时还没有评论内容!");
                        bt_more.setClickable(false);
                    }
                    break;
            }
        }
    };
    private TextView tv_coll;
    private HintInfoDialog dialog;
    private ImageView iv_coll;
    private OnceSpecialCourseList ocs;
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_special_course_info;
    }

    @Override
    protected void setNeedLoading() {

        isNeedLoading  = true;
    }

    @Override
    protected void onCreate() {
        try{
            setViews();
        }catch (Exception e){
            e.printStackTrace();
        }
        titleCenterTextView.setText("课程详情");
        //画线

        three = new TextView[]{
                (TextView)findViewById(R.id.three_1),
                (TextView)findViewById(R.id.three_2),
                (TextView)findViewById(R.id.three_3)
        };
        three[1].setVisibility(View.INVISIBLE);
        three[2].setVisibility(View.INVISIBLE);

        tv_coll = (TextView)findViewById(R.id.textview_1_1);
        iv_coll = (ImageView)findViewById(R.id.imageView_1_1);
        tv_share = (TextView)findViewById(R.id.textview_2_2);

        if(!ocs.isFavor()){
            tv_coll.setText("已收藏");
            iv_coll.setImageResource(R.drawable.shoucangnewred);
        }else{
            tv_coll.setText("收藏");
            iv_coll.setImageResource(R.drawable.shoucangnewwhtire);
        }

        btom_llll = (FrameLayout)findViewById(R.id.btom_llll);

        draw_line_ll = (LinearLayout)findViewById(R.id.draw_line_ll);
        line_text = new ManDrawLine(this);
        line_text.setDrawWidth(WindowUtils.dm.widthPixels / 3);
        draw_line_ll.addView(line_text);

        //初始化收藏

        //初始化3个切换按钮
        rb_three_corse = (RadioButton)findViewById(R.id.rb_three_corse);
        rb_three_school = (RadioButton)findViewById(R.id.rb_three_school);
        rb_three_assess = (RadioButton)findViewById(R.id.rb_three_assess);
        courseFragment = new CourseDetailFragmentThree();
        schoolFragment = new SchoolFragmentThree();
        familyFragment = new FamilyAssessFragmentThree();
        transion =  getSupportFragmentManager().beginTransaction();
                        transion.add(R.id.container_ll_three, courseFragment,tag[0]).
                        add(R.id.container_ll_three, schoolFragment,tag[1]).
                        add(R.id.container_ll_three, familyFragment,tag[2]).
                                hide(schoolFragment).hide(familyFragment).show(courseFragment).
                                commit();
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (v.getId()){
                    case R.id.rb_three_corse:
                        three[0].setVisibility(View.VISIBLE);
                        three[1].setVisibility(View.INVISIBLE);
                        three[2].setVisibility(View.INVISIBLE);
                        line_text.setLocation(WindowUtils.dm.widthPixels/3);
                        line_text.drawLeft();
                        getSupportFragmentManager().beginTransaction().hide(schoolFragment).hide(familyFragment).show(courseFragment).commit();
                        break;
                    case R.id.rb_three_school:
                        three[0].setVisibility(View.INVISIBLE);
                        three[1].setVisibility(View.VISIBLE);
                        three[2].setVisibility(View.INVISIBLE);
                        if(!isAgain){
                            getSchoolInfo(osc.getGroupuuid());
                            isAgain = true;

                            //获取底部菜单栏的位置

                        }
                        getSupportFragmentManager().beginTransaction().hide(courseFragment).hide(familyFragment).show(schoolFragment).commit();
                        break;
                    case R.id.rb_three_assess:

                        three[2].setVisibility(View.VISIBLE);
                        three[1].setVisibility(View.INVISIBLE);
                        three[0].setVisibility(View.INVISIBLE);
                        if(!isAssessOne){
                            getAssess(1);
                            isAssessOne = true;
                        }
                        getSupportFragmentManager().beginTransaction().hide(schoolFragment).hide(courseFragment).show(familyFragment).commit();
                        break;
                }
            }
        };

        rb_three_corse.setOnClickListener(listener);
        rb_three_school.setOnClickListener(listener);
        rb_three_assess.setOnClickListener(listener);


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

                        String content = osc.getContent();
                        if(Utils.isNull(content) == null || content == null){
                            content = osc.getTitle();
                        }
                        ShareUtils.showShareDialog(SpecialCourseInfoActivity.this,tv_share,osc.getTitle()
                                ,content,osc.getLogo(),ocs.getShare_url(),false);

//                            ToastUtils.showMessage("暂无分享内容!");


                        break;
                    case R.id.train_course_tab_interaction:
                        //启动互动界面
                        Intent intent = new Intent(SpecialCourseInfoActivity.this,CourseInteractionListActivity.class);
                        intent.putExtra("newsuuid",osc.getUuid());
                        intent.putExtra("type", NormalReplyListActivity.TRAIN_COURSE);
                        startActivity(intent);
                        break;
                    case R.id.train_course_tab_ask:
                        CallUtils.showCall(SpecialCourseInfoActivity.this, ocs.getLink_tel().split(GloablUtils.SPLIT_STIRNG_MOBILE_NUMBER),new CallTransfer(osc.getUuid(),82));
                        break;
                }
            }
        };

        for(RelativeLayout relativeLayout:relativeLayouts){
            relativeLayout.setOnClickListener(listenersss);
        }
    }

    private int pageNo = 1;
    @Override
    protected void loadData() {
        if(dialog == null) dialog = new HintInfoDialog(this);
        dialog.show();
        Intent intent = getIntent();
        uuid =  intent.getStringExtra("uuid");
        UserRequest.getSpecialCourseINfoFromClickItem(this, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (dialog.isShowing()) dialog.cancel();
                ocs = (OnceSpecialCourseList) domain;
                if (ocs != null)
                    osc = ocs.getData();
//                if(courseFragment == null)
//                courseFragment = new CourseDetailFragmentThree();
//                courseFragment.setActivity(SpecialCourseInfoActivity.this);
                loadSuc();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });



    }

    public void getAssess(final int page) {
        //获取该对象的评价
        dialog.show();
        UserRequest.getMoreDiscuss(this, uuid, page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (dialog.isShowing()){
                    dialog.cancel();
                }
                MoreDiscussList mdl = (MoreDiscussList) domain;
                if (mdl.getList() != null && mdl.getList().getData() != null
                        && mdl.getList().getData().size() > 0) {
                    familyFragment.addList(mdl.getList().getData());
                }else{
                    if(page == 1){
                        familyFragment.noData();
                    }
                    familyFragment.setNoRequest();
                    ToastUtils.noMoreContentShow();
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

    public void getSchoolInfo(String schooluuid){
        if(dialog.isShowing()){
            dialog.cancel();
        }
        UserRequest.getTrainSchoolDetail(this, schooluuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SchoolDetailList sdl = (SchoolDetailList) domain;
                if (sdl != null) {
                    SchoolDetail sd = sdl.getData();
                    String text = null;
                    schoolFragment.setUrl(sdl.getObj_url());
                    if (!TextUtils.isEmpty(sd.getDescription())) {
                        text = sd.getDescription();
                    } else {
                        text = "暂时没有学校内容介绍!";
                    }
//                    tv_school_introduce.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
//                    tv_school_introduce.setVisibility(View.VISIBLE);
//                    scrollView.onRefreshComplete();
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
        dialog = new HintInfoDialog(SpecialCourseInfoActivity.this, "收藏中，请稍后...");
        dialog.show();
        UserRequest.store(SpecialCourseInfoActivity.this, osc.getTitle(), 82, uuid, "", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(SpecialCourseInfoActivity.this, "收藏成功");
                store1();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(SpecialCourseInfoActivity.this, message);
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
        ocs.setIsFavor(false);
    }

    private void store2() {
        Drawable drawable = getResources().getDrawable(R.drawable.shoucangnewwhtire);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
//        tvStore.setCompoundDrawables(null, drawable, null, null);
        iv_coll.setImageDrawable(drawable);
        tv_coll.setText("收藏");
        tv_coll.setTextColor(getResources().getColor(R.color.color_929292));
        ocs.setIsFavor(true);
    }

    private void cancelStore() {
        dialog = new HintInfoDialog(SpecialCourseInfoActivity.this, "取消收藏中，请稍后...");
        dialog.show();
        UserRequest.cancelStore(true, SpecialCourseInfoActivity.this, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(SpecialCourseInfoActivity.this, "取消收藏成功");
                store2();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(SpecialCourseInfoActivity.this, message);
                }
                dialog.dismiss();
            }
        });
    }

}
