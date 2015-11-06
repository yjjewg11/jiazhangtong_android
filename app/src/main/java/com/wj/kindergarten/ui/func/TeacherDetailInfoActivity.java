package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.TeacherDetailInfo;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SpecialCourseInfoList;
import com.wj.kindergarten.bean.SpecialCourseInfoObject;
import com.wj.kindergarten.bean.TeacherDetail;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.SpecialCourseListAdapter;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/28.
 */
public class TeacherDetailInfoActivity extends BaseActivity {

    private WebView teacher_introduce_tv;
    private SpecialCourseListAdapter adapter;
    private String uuid;

    private List<SpecialCourseInfoObject> list = new ArrayList<>();
    private PullToRefreshScrollView scroll_view;
    private LinearLayout all_course_of_teacher;

    public TeacherDetail getTeacherDetail() {
        return teacherDetail;
    }

    private TeacherDetail teacherDetail;
    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_teacher_detail_info;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //添加数据
                    if(scroll_view.isRefreshing()) scroll_view.onRefreshComplete();
                    for(final SpecialCourseInfoObject object : list){
                        View view = View.inflate(TeacherDetailInfoActivity.this,R.layout.item_special_course_list_view,null);
                        ImageView imageView = (ImageView) view.findViewById(R.id.item_special_course_list_view_image_view);
                        RatingBarView ratingBar = (RatingBarView) view.findViewById(R.id.item_special_course_list_view__rating_bar);
                        TextView item_special_course_list_view_tv_adresss = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_adresss);
                        TextView item_class_name = (TextView) view.findViewById(R.id.item_class_name);
                        TextView item_special_course_list_view_tv_distance = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_distance);
                        TextView item_special_course_list_view_tv_edcucation = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_edcucation);
                        TextView item_special_course_list_view_tv_study_people = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_study_people);
                        ratingBar.setFloatStar(object.getCt_stars(), true);
                        ImageLoaderUtil.displayMyImage(object.getLogo(), imageView);
//                        if(Utils.MearsureText(object.getTitle())> (item_class_name.getWidth() - 10)){
//
//                        }
                        item_class_name.setText("" + object.getTitle());
//                                .length() > 15 ? (object.getTitle().substring(0,8)+"...") : object.getTitle()));
                        item_special_course_list_view_tv_adresss.setText(""+object.getAddress());
//                                .length() > 8 ? (object.getAddress().substring(0,8)+"...") : object.getAddress()));
                        //TODO
                        String text = "<font  color='#ff4966'>"+object.getCt_study_students()+"</font>"+"人已学";
                        item_special_course_list_view_tv_distance.setText(""+object.getDistance());
                        item_special_course_list_view_tv_edcucation.setText("" + object.getGroup_name());
                        item_special_course_list_view_tv_study_people.setText(Html.fromHtml(text));
                        view.setBackgroundResource(R.drawable.setting_item_click_selector);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(TeacherDetailInfoActivity.this, SpecialCourseInfoActivity.class);
//                                intent.putExtra("position",position);
                                intent.putExtra("object", object);
                                startActivity(intent);
                            }
                        });

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        all_course_of_teacher.addView(view, params);

                        TextView tv_line = new TextView(TeacherDetailInfoActivity.this);
                        tv_line.setBackgroundColor(Color.parseColor("#bbbbbb"));
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1);
                        all_course_of_teacher.addView(tv_line,llp);
                    }


                    break;
            }
        }
    };
    @Override
    protected void setNeedLoading() {


        uuid = getIntent().getStringExtra("teacheruuid");
        isNeedLoading = true;
    }

    @Override
    protected void onCreate() {
        ImageView imageView = (ImageView) findViewById(R.id.item_special_course_list_view_image_view);
        RatingBarView ratingBar = (RatingBarView) findViewById(R.id.item_special_course_list_view__rating_bar);
//        TextView item_special_course_list_view_tv_adresss = (TextView) findViewById(R.id.item_special_course_list_view_tv_adresss);
        TextView item_class_name = (TextView) findViewById(R.id.item_class_name);
        TextView item_special_course_list_view_tv_distance = (TextView)findViewById(R.id.item_special_course_list_view_tv_distance);
        TextView item_special_course_list_view_tv_edcucation = (TextView) findViewById(R.id.item_special_course_list_view_tv_edcucation);
        TextView item_special_course_list_view_tv_study_people = (TextView)findViewById(R.id.item_special_course_list_view_tv_study_people);
        item_special_course_list_view_tv_distance.setVisibility(View.INVISIBLE);
        item_special_course_list_view_tv_study_people.setVisibility(View.INVISIBLE);
        scroll_view =(PullToRefreshScrollView) findViewById(R.id.teacher_detail_scroll);
        all_course_of_teacher = (LinearLayout)findViewById(R.id.all_course_of_teacher);

        teacher_introduce_tv = (WebView) findViewById(R.id.teacher_introduce_tv);




        if(teacherDetail != null){
            String text = null;
            if(!TextUtils.isEmpty(teacherDetail.getContent())){
                text = teacherDetail.getContent();
            }else{
                text = "暂时没有老师的相关简介!";
            }
            teacher_introduce_tv.loadDataWithBaseURL(null,text,"text/html","utf-8",null);
            ImageLoaderUtil.displayMyImage(teacherDetail.getImg(), imageView);
            ratingBar.setFloatStar(teacherDetail.getCt_stars(), true);
            item_class_name.setText("" + teacherDetail.getName()+"老师");
            item_class_name.setTextColor(getResources().getColor(R.color.black));
            item_special_course_list_view_tv_edcucation.setText("教授课程:"+teacherDetail.getCourse_title());
        }
        adapter = new SpecialCourseListAdapter(this);

        scroll_view.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        scroll_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageNo++;
                getCourse();
            }
        });

        getCourse();
    }

    int pageNo = 1;
    @Override
    protected void loadData() {
        UserRequest.getTeacherDetailInfo(this, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                TeacherDetailInfo teacherDetailInfo = (TeacherDetailInfo) domain;
                if (teacherDetailInfo != null) {
                    teacherDetail = teacherDetailInfo.getData();
                    loadSuc();
                } else {
                    loadEmpty();
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

    private void getCourse() {
        UserRequest.getSpecialCourseInfoFormType(this, "", pageNo, -1, "", uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SpecialCourseInfoList specialCourseInfoList = (SpecialCourseInfoList) domain;
                if (specialCourseInfoList != null) {
                    if (specialCourseInfoList.getList() != null && specialCourseInfoList.getList().getData() != null
                            && specialCourseInfoList.getList().getData().size() > 0) {
                        list.addAll(specialCourseInfoList.getList().getData());
                        mHandler.sendEmptyMessage(1);
                    }else{
                        if(pageNo == 1){
                            noView(all_course_of_teacher);
                        }else{
                            ToastUtils.showMessage("没有更多内容了！");
                        }
                        if(scroll_view.isRefreshing()) scroll_view.onRefreshComplete();

                    }
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
}
