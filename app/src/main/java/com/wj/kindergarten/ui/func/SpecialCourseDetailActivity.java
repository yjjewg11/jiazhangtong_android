package com.wj.kindergarten.ui.func;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.socialize.utils.Log;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SpecialCourseInfoList;
import com.wj.kindergarten.bean.SpecialCourseInfoObject;
import com.wj.kindergarten.bean.TrainSchoolInfo;
import com.wj.kindergarten.bean.TrainSchoolInfoListFather;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.CommonSpinnerAdapter;
import com.wj.kindergarten.ui.func.adapter.SchoolCourseAdapter;
import com.wj.kindergarten.ui.func.adapter.SpecialCourseListAdapter;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpecialCourseDetailActivity extends BaseActivity{
    private PullToRefreshListView mListView;

    private int  CHOOSE_SCHOOL_COURSE_TYPE = 1000;

    private List<String> sp_in_variable = Arrays.asList(new String[]{
            "智能排序","评价最高","距离最近"
    });

    private List<String> sp_sort = Arrays.asList(new String[]{
            "英语学习","美术绘画","声乐舞蹈","体育运动"
    });

    //可存放课程和学校分类查询的课程信息
    private List<SpecialCourseInfoObject> allCourse = new ArrayList<>();

    private List<SpecialCourseInfoObject> courses = new ArrayList<>();

    private List<TrainSchoolInfo> school = new ArrayList<>();
    private SpecialCourseListAdapter listViewAdapter;

    private SchoolCourseAdapter schoolCourseAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //加入集合
                case  1:
                    closeRefrush();
                    listViewAdapter.setSepcialList(allCourse);
                    mListView.setAdapter(listViewAdapter);

                    break;
                case  20:
                   final int type = msg.arg1;

                    Collections.sort(listViewAdapter.getSpecialCourseInfoObjectList(), new Comparator<SpecialCourseInfoObject>() {
                        @Override
                        public int compare(SpecialCourseInfoObject lhs, SpecialCourseInfoObject rhs) {
                            int myType  = -1;
                            if (type == 0) {
                                //按智能进行排序
                                ToastUtils.showMessage("按智能排序");
                            } else if (type == 1) {
                                //评价最高
                                ToastUtils.showMessage("按评价最高排序");
                            } else if (type == 2) {
                                //距离最近
                                ToastUtils.showMessage("按距离最近排序");
                            }
                            return myType;
                        }
                    });
                    break;

                case 2:
                    //通知更新学校页面
                    closeRefrush();
                    schoolCourseAdapter.notifyDataSetChanged();
                    break;

                case 3:
                    //关闭刷新状态

                    break;
            }
        }
    };
    private ImageView ivback;

    private void closeRefrush() {
        if(mListView.isRefreshing()) {
            mListView.onRefreshComplete();
        }
    }

    private Spinner studyClass,sort;
    private RadioButton rb_course;
    private RadioButton rb_school;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_sepcial_course_detail;
    }

    @Override
    protected void setNeedLoading() {

        isNeedLoading = true;
    }

    //"英语学习  0" ,"美术绘画  1","声乐舞蹈  2","体育运动  3" 全部课程 4  学校  5
    int type;

    @Override
    protected void onCreate() {
        ivback = (ImageView)findViewById(R.id.iv_top_left_back);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rb_course = (RadioButton) findViewById(R.id.rb_course);
        rb_school = (RadioButton) findViewById(R.id.rb_school);
        setListeners();
        Intent intent = getIntent();
        type = intent.getIntExtra("position", 10);
        setSpinner();
        mListView = (PullToRefreshListView)findViewById(R.id.pulltorefresh_list);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listViewAdapter = new SpecialCourseListAdapter(this);
        mListView.setAdapter(listViewAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class myClass = null;
                Log.i("打印点击的位置"+position);

                Intent intent = null;
                if (CHOOSE_SCHOOL_COURSE_TYPE == 1000) {
                    //放入机构的uuid
                    String uuid = ((SpecialCourseInfoObject) listViewAdapter.getItem(position - 1)).getUuid();
                    intent = new Intent(SpecialCourseDetailActivity.this, SpecialCourseInfoActivity.class);
                    intent.putExtra("uuid", uuid);
                } else {
                    //启动学校详情页面
                    String schoolUuid = ( (TrainSchoolInfo) schoolCourseAdapter.getItem(position-1)).getUuid();
                    intent = new Intent(SpecialCourseDetailActivity.this, SchoolDetailInfoActivity.class);
                    intent.putExtra("schoolUuid",schoolUuid);
                }

                startActivity(intent);
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                //培训机构
                if(CHOOSE_SCHOOL_COURSE_TYPE == 1000){
                    pageNo++;
                    loadData();
                }else{
                    pageSchool++;
                    //请求学校数据
                    getSchoolInfo();
                }
            }
        });
    }

    int  pageSchool = 1;
    private void setListeners() {
        rb_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 4;
                CHOOSE_SCHOOL_COURSE_TYPE = 1000;
                loadData();
            }
        });

        rb_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHOOSE_SCHOOL_COURSE_TYPE = 2000;
                type = 5;
                //点击之后不应该请求数据，只是作为显示，下拉才能刷新
                if(schoolCourseAdapter == null){

                    schoolCourseAdapter = new SchoolCourseAdapter(SpecialCourseDetailActivity.this,school);
                    mListView.setAdapter(schoolCourseAdapter);
                    getSchoolInfo();
                }else{
                    mListView.setAdapter(schoolCourseAdapter);
                }

            }
        });
    }

    private void getSchoolInfo() {
        UserRequest.getAllSchool(this,pageSchool,new RequestResultI(){
            @Override
            public void result(BaseModel domain) {
                TrainSchoolInfoListFather ts = (TrainSchoolInfoListFather) domain;
                if(ts != null&&ts.getList()!=null){
                    school.addAll(ts.getList().getData());
                    //处理学校分类
                    mHandler.sendEmptyMessage(2);
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

    private void setSpinner() {

        studyClass = (Spinner) findViewById(R.id.sp_study_class);
        sort = (Spinner) findViewById(R.id.sp_sort);
        sort.setAdapter(new CommonSpinnerAdapter(this, sp_in_variable));
        studyClass.setAdapter(new CommonSpinnerAdapter(this,sp_sort));

        studyClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                allCourse.clear();
                type = position;
                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Message message = new Message();
                message.arg1 = position;
                message.what = 20;
                mHandler.sendMessage(message);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


    }

    boolean isFirst ;

    int pageNo = 1;
    //请求数据
    @Override
    protected void loadData() {
        UserRequest.getSpecialCourseInfoFormType(this,"",pageNo ,type, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SpecialCourseInfoList list = (SpecialCourseInfoList) domain;
                //如果为课程分类，加入到course集合中，如果为学校分类，加入到school集合中，如果为全部课程加入到allcourse
               //暂定英语分类
                //如果课程获取类别不一样，根据返回类型确定是否是同一类型数据,否则清空集合


//                if(type==1){
//                    courses.clear();
//                    courses.addAll(list.getList().getData());
//                    allCourse.addAll(courses);
//                    //学校分类
//                }else if(type == 5){
//                    school.addAll(list.getList().getData());
//                    allCourse.addAll(school);
//                    //如果是全部课程，则加入到总集合中
//                }else if( type == 8){
                    allCourse.addAll(list.getList().getData());
//                }

                if (!isFirst) {
                    loadSuc();
                } else {

                    mHandler.sendEmptyMessage(1);
                }

                isFirst = true;
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
