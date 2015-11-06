package com.wj.kindergarten.ui.func;


import android.content.Intent;
<<<<<<< HEAD
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
=======
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
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SpecialCourseInfoList;
import com.wj.kindergarten.bean.SpecialCourseInfoObject;
<<<<<<< HEAD
import com.wj.kindergarten.bean.SpecialCourseType;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
import com.wj.kindergarten.bean.TrainSchoolInfo;
import com.wj.kindergarten.bean.TrainSchoolInfoListFather;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.CommonSpinnerAdapter;
<<<<<<< HEAD
import com.wj.kindergarten.ui.func.adapter.OwnAdapter;
import com.wj.kindergarten.ui.func.adapter.SchoolCourseAdapter;
import com.wj.kindergarten.ui.func.adapter.SpecialCourseListAdapter;
import com.wj.kindergarten.ui.func.adapter.SpinnerAreaAdapter;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
=======
import com.wj.kindergarten.ui.func.adapter.SchoolCourseAdapter;
import com.wj.kindergarten.ui.func.adapter.SpecialCourseListAdapter;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
import java.util.List;

public class SpecialCourseDetailActivity extends BaseActivity{
    private PullToRefreshListView mListView;
<<<<<<< HEAD
    private String[] arraySort = new String[]{"intelligent","appraise","distance"};
    private int study_spinner_position = -1;
    private int sort_spinner_position = -1;

    private int  CHOOSE_SCHOOL_COURSE_TYPE = 1000;
    private List<SpecialCourseType> typeList = new ArrayList<>();

    private List<String> sp_in_variable = new ArrayList<>();

    private List<String> sp_sort = Arrays.asList(new String[]{
            "智能排序","评价最高","距离最近"
=======

    private int  CHOOSE_SCHOOL_COURSE_TYPE = 1000;

    private List<String> sp_in_variable = Arrays.asList(new String[]{
            "智能排序","评价最高","距离最近"
    });

    private List<String> sp_sort = Arrays.asList(new String[]{
            "英语学习","美术绘画","声乐舞蹈","体育运动"
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    });

    //可存放课程和学校分类查询的课程信息
    private List<SpecialCourseInfoObject> allCourse = new ArrayList<>();

<<<<<<< HEAD
=======
    private List<SpecialCourseInfoObject> courses = new ArrayList<>();

>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    private List<TrainSchoolInfo> school = new ArrayList<>();
    private SpecialCourseListAdapter listViewAdapter;

    private SchoolCourseAdapter schoolCourseAdapter;
<<<<<<< HEAD
    private Handler mHandler;

    {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                isPullDown = false;
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                boolean isChooseCourse = (CHOOSE_SCHOOL_COURSE_TYPE == 1000 ? true : false);
                int httpType = -1;
                switch (msg.what) {
                    //加入集合
                    case REPALCE_VIEW:
                        replaceNothingView();
                        break;
                    case 1:
                        closeRefrush();
                        listViewAdapter.setSepcialList(allCourse);

                        break;
                    case 20:
                        final int type = msg.arg1;

                        break;

                    case 2:
                        //通知更新学校页面
                        closeRefrush();
                        schoolCourseAdapter.setList(school);
                        break;

                    case 3:
                        //关闭刷新状态

                        break;

                    case 100:
                    case 101:
                    case 102:
                    case 103:
                        if (isChooseCourse) {
                            allCourse.clear();
                            pageNo = 1;
                            loadData();
                        }
                        break;
                    case 200:
                    case 201:
                    case 202:
                        if (isChooseCourse) {
                            allCourse.clear();
                            pageNo = 1;
                            loadData();
                        } else {
                            school.clear();
                            pageSchool = 1;
                            getSchoolInfo();
                        }
                        break;
                    case 333:
                        //请求时没有数据进行显示
                        ToastUtils.showMessage("没有更多内容了！");
                        closeRefrush();
                        break;
                    default:
                        if (mListView.isRefreshing()) mListView.onRefreshComplete();

                }
            }
        };
    }

    private RelativeLayout ivback;
    private ListView listView_choose;
    private OwnAdapter ownAdapter;
    private String sort_string;
    private TextView sp_city;
    private HintInfoDialog dialog;
    boolean isPullDown;
    private FrameLayout replace_view;
    private static final int REPALCE_VIEW = 123;
=======
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
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

    private void closeRefrush() {
        if(mListView.isRefreshing()) {
            mListView.onRefreshComplete();
        }
    }

<<<<<<< HEAD
    private Button studyClass,sort;
=======
    private Spinner studyClass,sort;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    private RadioButton rb_course;
    private RadioButton rb_school;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_sepcial_course_detail;
    }

    @Override
    protected void setNeedLoading() {
<<<<<<< HEAD
        sp_in_variable.add(0,"查看全部");
    }

=======

        isNeedLoading = true;
    }

    //"英语学习  0" ,"美术绘画  1","声乐舞蹈  2","体育运动  3" 全部课程 4  学校  5
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    int type;

    @Override
    protected void onCreate() {
<<<<<<< HEAD
        replace_view = (FrameLayout)findViewById(R.id.replace_view);
        dialog = new HintInfoDialog(this);
        ivback = (RelativeLayout)findViewById(R.id.iv_top_left_back);
=======
        ivback = (ImageView)findViewById(R.id.iv_top_left_back);
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rb_course = (RadioButton) findViewById(R.id.rb_course);
        rb_school = (RadioButton) findViewById(R.id.rb_school);
<<<<<<< HEAD
        sp_city = (TextView)findViewById(R.id.spinner_city_special);
        cityChoose(sp_city);
        setListeners();
        Intent intent = getIntent();
        if(typeList.isEmpty()){
            typeList.addAll((ArrayList<SpecialCourseType>) intent.getSerializableExtra("list"));

            for(SpecialCourseType type : typeList){
                sp_in_variable.add(type.getDatavalue());
            }
        }
        type = intent.getIntExtra("key", -1);
        int id = intent.getIntExtra("id",-1);
        setButton(id);
=======
        setListeners();
        Intent intent = getIntent();
        type = intent.getIntExtra("position", 10);
        setSpinner();
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
        mListView = (PullToRefreshListView)findViewById(R.id.pulltorefresh_list);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listViewAdapter = new SpecialCourseListAdapter(this);
        mListView.setAdapter(listViewAdapter);
<<<<<<< HEAD
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
=======

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class myClass = null;
                Log.i("打印点击的位置"+position);
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

                Intent intent = null;
                if (CHOOSE_SCHOOL_COURSE_TYPE == 1000) {
                    //放入机构的uuid
<<<<<<< HEAD
                    SpecialCourseInfoObject object = (SpecialCourseInfoObject) listViewAdapter.getItem((int)id);
                    intent = new Intent(SpecialCourseDetailActivity.this, SpecialCourseInfoActivity.class);
                    intent.putExtra("uuid", object.getUuid());
                    intent.putExtra("object",object);
                } else {
                    //启动学校详情页面
                    TrainSchoolInfo school = ( (TrainSchoolInfo) schoolCourseAdapter.getItem((int)id));
                    intent = new Intent(SpecialCourseDetailActivity.this, SchoolDetailInfoActivity.class);
                    intent.putExtra("school",school);
=======
                    String uuid = ((SpecialCourseInfoObject) listViewAdapter.getItem(position - 1)).getUuid();
                    intent = new Intent(SpecialCourseDetailActivity.this, SpecialCourseInfoActivity.class);
                    intent.putExtra("uuid", uuid);
                } else {
                    //启动学校详情页面
                    String schoolUuid = ( (TrainSchoolInfo) schoolCourseAdapter.getItem(position-1)).getUuid();
                    intent = new Intent(SpecialCourseDetailActivity.this, SchoolDetailInfoActivity.class);
                    intent.putExtra("schoolUuid",schoolUuid);
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
                }

                startActivity(intent);
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

<<<<<<< HEAD
                isPullDown = true;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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
<<<<<<< HEAD

        loadData();
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    }

    int  pageSchool = 1;
    private void setListeners() {
        rb_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                CHOOSE_SCHOOL_COURSE_TYPE = 1000;
                mListView.setAdapter(listViewAdapter);
=======
                type = 4;
                CHOOSE_SCHOOL_COURSE_TYPE = 1000;
                loadData();
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
            }
        });

        rb_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHOOSE_SCHOOL_COURSE_TYPE = 2000;
<<<<<<< HEAD
                //点击之后不应该请求数据，只是作为显示，下拉才能刷新
                if (schoolCourseAdapter == null) {
                    schoolCourseAdapter = new SchoolCourseAdapter(SpecialCourseDetailActivity.this);
                    mListView.setAdapter(schoolCourseAdapter);
                    getSchoolInfo();
                } else {
=======
                type = 5;
                //点击之后不应该请求数据，只是作为显示，下拉才能刷新
                if(schoolCourseAdapter == null){

                    schoolCourseAdapter = new SchoolCourseAdapter(SpecialCourseDetailActivity.this,school);
                    mListView.setAdapter(schoolCourseAdapter);
                    getSchoolInfo();
                }else{
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
                    mListView.setAdapter(schoolCourseAdapter);
                }

            }
        });
    }

    private void getSchoolInfo() {
<<<<<<< HEAD
        //TODO
        if(!isPullDown){
            dialog.show();
        }

        UserRequest.getAllSchool(this,pageSchool,sort_string,type,new RequestResultI(){
            @Override
            public void result(BaseModel domain) {
                TrainSchoolInfoListFather ts = (TrainSchoolInfoListFather) domain;
                if(ts != null&&ts.getList()!=null && ts.getList().getData() != null && ts.getList().getData().size() > 0){
                    school.addAll(ts.getList().getData());
                    //处理学校分类
                    mHandler.sendEmptyMessage(2);
                }else{
                    if(pageSchool == 1){
                        mHandler.sendEmptyMessage(123);
                    }else{
                        mHandler.sendEmptyMessage(333);
                    }
                }



=======
        UserRequest.getAllSchool(this,pageSchool,new RequestResultI(){
            @Override
            public void result(BaseModel domain) {
                TrainSchoolInfoListFather ts = (TrainSchoolInfoListFather) domain;
                if(ts != null&&ts.getList()!=null){
                    school.addAll(ts.getList().getData());
                    //处理学校分类
                    mHandler.sendEmptyMessage(2);
                }
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

<<<<<<< HEAD
    private void setButton(int id) {

        studyClass = (Button) findViewById(R.id.sp_study_class);
        sort = (Button) findViewById(R.id.sp_sort);
        sort.setText("智能排序");
        studyClass.setText("" + sp_in_variable.get(id));
        View.OnClickListener listener  = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                    View view = View.inflate(getApplicationContext(),R.layout.window_list,null);

                    listView_choose = (ListView) view.findViewById(R.id.window_lsit);
                    ownAdapter = new OwnAdapter(SpecialCourseDetailActivity.this);
                    listView_choose.setAdapter(ownAdapter);
                    final PopupWindow popupWindowss = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    popupWindowss.setAnimationStyle(R.style.ShareAnimBase);
                    popupWindowss.setFocusable(true);
                    popupWindowss.setTouchable(true);
                    popupWindowss.setOutsideTouchable(true);
                    popupWindowss.getContentView().setFocusableInTouchMode(true);
                    popupWindowss.getContentView().setFocusable(true);
                    popupWindowss.setBackgroundDrawable(new BitmapDrawable());
                    popupWindowss.update();
                    popupWindowss.showAsDropDown(v, 0, 0);
                    view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popupWindowss.dismiss();
                    }
                     });
                    listView_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        boolean istrue = true;

                        if(replace_view.findViewById(R.id.pulltorefresh_list) == null){
                            replacePrimaryView();
                        }

                        if (v.getId() == R.id.sp_study_class) {
                            if(position == 0){
                                studyClass.setText("查看全部");
                                type = -1;
                            }else{
                                studyClass.setText(""+typeList.get(position-1).getDatavalue());
                                type = typeList.get(position-1).getDatakey();
                            }

//                            if(CHOOSE_SCHOOL_COURSE_TYPE == 2000) {
//                                istrue = false;
//                                ToastUtils.showMessage("暂不支持此功能!");
//                            }
                        } else if (v.getId() == R.id.sp_sort) {
                            sort.setText(""+sp_sort.get(position));
                            sort_string = arraySort[position];
                        }
                        popupWindowss.dismiss();
                        try{
                            if(CHOOSE_SCHOOL_COURSE_TYPE == 1000){
                                allCourse.clear();
                                pageNo = 1;
                                loadData();
                            }else{

//                                if(istrue){
                                    school.clear();
                                    pageSchool = 1;
                                    getSchoolInfo();
//                                }

                            }
                        }catch (Exception e){

                            System.out.println(e);
                        }
                    }
                });

                if(v.getId()==R.id.sp_study_class){
                    ownAdapter.setList(sp_in_variable);
                }else if(v.getId() == R.id.sp_sort){
                    ownAdapter.setList(sp_sort);
                }
            }
        };

        studyClass.setOnClickListener(listener);
        sort.setOnClickListener(listener);
    }

    boolean isFirst;
=======
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
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

    int pageNo = 1;
    //请求数据
    @Override
    protected void loadData() {
<<<<<<< HEAD
        if(!isPullDown){
            dialog.show();
        }
        UserRequest.getSpecialCourseInfoFormType(this,"",pageNo ,type,sort_string,"",new RequestResultI() {
=======
        UserRequest.getSpecialCourseInfoFormType(this,"",pageNo ,type, new RequestResultI() {
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
            @Override
            public void result(BaseModel domain) {
                SpecialCourseInfoList list = (SpecialCourseInfoList) domain;
                //如果为课程分类，加入到course集合中，如果为学校分类，加入到school集合中，如果为全部课程加入到allcourse
               //暂定英语分类
                //如果课程获取类别不一样，根据返回类型确定是否是同一类型数据,否则清空集合
<<<<<<< HEAD
//
                if(list.getList() != null && list.getList().getData() != null
                        && list.getList().getData().size() > 0){
                    allCourse.addAll(list.getList().getData());
                    mHandler.sendEmptyMessage(1);
                }else{
                    if(pageNo == 1){
                        mHandler.sendEmptyMessage(REPALCE_VIEW);
                    }else{
                        mHandler.sendEmptyMessage(333);
                    }

                }



=======


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
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

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
<<<<<<< HEAD
    public void replacePrimaryView(){
        replace_view.removeAllViews();
        if(mListView.getParent() != null){
            ((ViewGroup)mListView.getParent()).removeAllViews();
        }

        replace_view.addView(mListView);
    }

    public void replaceNothingView(){
        View view = View.inflate(this,R.layout.nothing_view,null);
        replace_view.removeAllViews();
        replace_view.addView(view);
    }
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
}
