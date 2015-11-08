package com.wj.kindergarten.ui.func;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.wj.kindergarten.ui.more.CallUtils;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;


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
    private SpecialCourseInfoObject object;
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
                            content.setText(""+md.getContent());
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
    private RelativeLayout rl_price;
    private RelativeLayout rl_free_price;
    private PullToRefreshScrollView scrollView;

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
        setViews();
        if(bt_more == null){
            bt_more = (Button)findViewById(R.id.start_more_dicuss_special);
        }

        titleCenterTextView.setText("课程详情");

        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialCourseInfoActivity.this,MoreSpecialDiscussActivity.class);
                //放入评论对象
                //TODO
                intent.putExtra("uuid",object.getUuid());
                intent.putExtra("discussList",moreList);
                startActivity(intent);
            }
        });



    }

    private void setViews() {



        try{
            setMoreViews();
        }catch (Exception e){

            System.out.println(e);
        }


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
                        if(object != null){
                            String content = osc.getContent();
                            if(Utils.isNull(content) == null || content == null){
                                content = osc.getTitle();
                            }
                            ShareUtils.showShareDialog(SpecialCourseInfoActivity.this,v,osc.getTitle()
                            ,content,object.getLogo(),ocs.getShare_url(),false);
                        }else{
                            ToastUtils.showMessage("暂无分享内容!");
                        }

                        break;
                    case R.id.train_course_tab_interaction:
                        //启动互动界面
                        Intent intent = new Intent(SpecialCourseInfoActivity.this,CourseInteractionListActivity.class);
                        intent.putExtra("newsuuid",osc.getUuid());
                        intent.putExtra("type",NormalReplyListActivity.TRAIN_COURSE);
                        startActivity(intent);
                        break;
                    case R.id.train_course_tab_ask:
                        CallUtils.showCall(SpecialCourseInfoActivity.this, ocs.getLink_tel().split(","),new CallTransfer(osc.getUuid(),82));
                        break;
                }
            }
        };

        for(RelativeLayout relativeLayout:relativeLayouts){
            relativeLayout.setOnClickListener(listenersss);
        }
    }

    private void setMoreViews() {
        if(ocs==null) return;
        scrollView = (PullToRefreshScrollView) findViewById(R.id.course_detail_scrollview);
        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if(osc!=null)
                getSchoolInfo(osc.getGroupuuid());
            }
        });
        rating_bar = (RatingBarView)findViewById(R.id.item_special_course_list_view__rating_bar);
        course_detail_train_class_name = (TextView)findViewById(R.id.course_detail_train_class_name);
        detail_course = (TextView)findViewById(R.id.detail_course);
        teach_place = (TextView)findViewById(R.id.teach_place);
        course_spend_time = (TextView)findViewById(R.id.course_spend_time);
        nornal_course_price = (TextView)findViewById(R.id.nornal_course_price);
        coupon_price = (TextView)findViewById(R.id.coupon_price);
        course_detail_info = (WebView)findViewById(R.id.course_detail_info);
        more_assess_linera = (LinearLayout) findViewById(R.id.more_assess_linera);
        iv_heading = (ImageView)findViewById(R.id.once_iv);
        tv_school_introduce = (WebView)findViewById(R.id.tv_school_introduce);
        tv_school_introduce.setVisibility(View.GONE);
        tv_coll = (TextView)findViewById(R.id.textview_1_1);
        iv_coll = (ImageView)findViewById(R.id.imageView_1_1);
        rl_price = (RelativeLayout)findViewById(R.id.rl_price);
        rl_free_price = (RelativeLayout)findViewById(R.id.rl_free_price);



        ImageLoaderUtil.displayMyImage(object.getLogo(), iv_heading);
        course_detail_train_class_name.setText("" + osc.getTitle());
        rating_bar.setFloatStar(osc.getCt_stars(), true);
        teach_place.setText("" +object.getAddress());
        detail_course.setText(""+osc.getSubtype());

        String text = null;
        if(!TextUtils.isEmpty(osc.getContext())) {
            text = osc.getContext();
        }else{

            text = "暂时没有内容介绍!";
        }
        course_detail_info.loadDataWithBaseURL(null,text,"text/html","utf-8",null);
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
        nornal_course_price.setText(""+osc.getDiscountfees()+" 元");
        coupon_price.setText(""+osc.getFees()+" 元");
        if(!ocs.isFavor()){
            tv_coll.setText("已收藏");
            iv_coll.setImageResource(R.drawable.store2);
        }else{
            tv_coll.setText("收藏");
            iv_coll.setImageResource(R.drawable.store1);
        }

        getAssess();
    }

    private int pageNo = 1;
    @Override
    protected void loadData() {
        Intent intent = getIntent();
        object = (SpecialCourseInfoObject) intent.getSerializableExtra("object");
        UserRequest.getSpecialCourseINfoFromClickItem(this, object.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ocs = (OnceSpecialCourseList) domain;
                if (ocs != null)
                    osc = ocs.getData();
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

    private void getAssess() {
        //获取该对象的评价
        UserRequest.getMoreDiscuss(this, object.getUuid(), 1, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                MoreDiscussList mdl = (MoreDiscussList) domain;
                if (mdl.getList() != null && mdl.getList().getData() != null) {
                    moreList.addAll(mdl.getList().getData());
                    mhandler.sendEmptyMessage(1);
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
        UserRequest.getTrainSchoolDetail(this, schooluuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SchoolDetailList sdl = (SchoolDetailList) domain;
                if (sdl != null && tv_school_introduce != null) {
                    SchoolDetail sd = sdl.getData();
                    String text = null;
                    if(!TextUtils.isEmpty(sd.getDescription())){
                        text = sd.getDescription();
                    }else{
                        text = "暂时没有学校内容介绍!";
                    }
                    tv_school_introduce.loadDataWithBaseURL(null,text, "text/html","utf-8",null);
                    tv_school_introduce.setVisibility(View.VISIBLE);
                    scrollView.onRefreshComplete();
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
        UserRequest.store(SpecialCourseInfoActivity.this, object.getTitle(), 82, object.getUuid(), "", new RequestResultI() {
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
        Drawable drawable = getResources().getDrawable(R.drawable.store2);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
//        tvStore.setCompoundDrawables(null, drawable, null, null);
        iv_coll.setImageDrawable(drawable);
        tv_coll.setText("已收藏");
        tv_coll.setTextColor(getResources().getColor(R.color.title_bg));
        ocs.setIsFavor(false);
    }

    private void store2() {
        Drawable drawable = getResources().getDrawable(R.drawable.store1);
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
        UserRequest.cancelStore(true, SpecialCourseInfoActivity.this, object.getUuid(), new RequestResultI() {
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
