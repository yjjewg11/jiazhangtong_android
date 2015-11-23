package com.wj.kindergarten.ui.func;


import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.StudyStateObject;
import com.wj.kindergarten.bean.StudyStateObjectList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.MineCourseStatusAdapter;
import com.wj.kindergarten.ui.other.ScollTextView;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MineSpecialCourseActivity extends BaseActivity{
    private PullToRefreshListView mListView;
    private MineCourseStatusAdapter adapter;
    private RadioButton rb_study;
    private RadioButton rb_study_over;
    private List<StudyStateObject> studyint_list = new ArrayList<>();
    private List<StudyStateObject> over_list = new ArrayList<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 100:
                if(isStudying == 0){
                    adapter.setList(studyint_list);
                    judgeIsEmpty(studyint_list);
                }else{
                    if(!isFirstLoad){
                        loadData();
                        isFirstLoad = true;
                    }
                    adapter.setList(over_list);
                    judgeIsEmpty(over_list);
                }
                    closeFresh();
                    break;
                case 1:
                    loadData();
                    break;
                case 300:
                    commonClosePullToRefreshListGridView(mListView);
                    break;
            }
        }
    };
    private FrameLayout rl;
    private HintInfoDialog dialog;
    private DisplayMetrics metrics;
    private int[] location = new int[2];
    private LinearLayout scroll_ll;
    private ScollTextView scroll_text;
    private LinearLayout scroll_linear;

    public void judgeIsEmpty(List<StudyStateObject> list){
        if(list.size() == 0){
            rl.removeAllViews();
            noView(rl);
        }else{
            rl.removeAllViews();
            if(mListView.getParent() != null){
                ((ViewGroup)mListView.getParent()).removeView(mListView);
            }
            rl.addView(mListView);
        }
    }
    public static final String FROM_MINE_COURSE_TO_MINE_DETAIL_COURSE = "mine_course_detail_to";

    private void closeFresh() {
        if(mListView.isRefreshing()){
            mListView.onRefreshComplete();
        }
    }

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_mine_special_course;
    }

    @Override
    protected void setNeedLoading() {
    }

    @Override
    protected void onCreate() {
               titleCenterTextView.setText("我的特长课程");
        initViews();
        loadData();

    }

    int pageStudying = 1;
    int pageStudyOver = 1;
    private void initViews() {
        //获取添加的线条在窗口中的位置
        scroll_linear = (LinearLayout)findViewById(R.id.scroll_linear);
        scroll_linear.getLocationInWindow(location);
        scroll_text = new ScollTextView(this);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        scroll_text.setMyWidth(metrics.widthPixels / 2,location[1]);
        scroll_linear.addView(scroll_text);

        rb_study = (RadioButton)findViewById(R.id.rb_study);
        rb_study_over = (RadioButton)findViewById(R.id.rb_study_over);
        rl = (FrameLayout)findViewById(R.id.study_state_fl);

        //动态添加3个textview以显示完成学习和正在学习的状态



        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.rb_study ){
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    isStudying = 0;
                    rb_study.setTextColor(Color.parseColor("#ff4966"));
                    rb_study_over.setTextColor(Color.parseColor("#333333"));
                    scroll_text.drawLeft();
                } else if(v.getId() == R.id.rb_study_over){
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    isStudying = 1;
                    rb_study.setTextColor(Color.parseColor("#333333"));
                    rb_study_over.setTextColor(Color.parseColor("#ff4966"));
                    scroll_text.drawRight();
                }
                mHandler.sendEmptyMessage(100);
            }
        };

        rb_study.setOnClickListener(listener);
        rb_study_over.setOnClickListener(listener);
        mListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        adapter = new MineCourseStatusAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(isStudying == 0){
                    pageStudying++;
                }else{
                    pageStudyOver++;
                }
                loadData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudyStateObject sso = null;
                if(isStudying == 0){
                    if(!studyint_list.isEmpty())
                    sso = studyint_list.get((int)id);
                }
                if(isStudying == 1){
                    if(!over_list.isEmpty())
                    sso = over_list.get((int)id);
                }
                Intent intent = new Intent(MineSpecialCourseActivity.this,MineCourseDetailActivity.class);
                intent.putExtra("courseuuid",sso.getUuid());
                intent.putExtra(FROM_MINE_COURSE_TO_MINE_DETAIL_COURSE,sso);
                startActivity(intent);
            }
        });
    }
    boolean isFirstLoad;
    private int getPageNo = 1;
    private int isStudying = 0;
    @Override
    protected void loadData() {
        int page = -1;
        if(isStudying == 0){
            page = pageStudying;
        }else{
            page = pageStudyOver;
        }
        if(dialog == null){dialog = new HintInfoDialog(this);}
        dialog.show();
        UserRequest.getStudyStatus(this,page,isStudying, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                closeFresh();
              StudyStateObjectList sso = (StudyStateObjectList) domain;
                if(sso!=null && sso.getList()!=null && sso.getList().getData() != null && sso.getList().getData().size() > 0){
                    if(isStudying == 0){
                        studyint_list.addAll(sso.getList().getData());
                    }else{
                        over_list.addAll(sso.getList().getData());
                    }
                    mHandler.sendEmptyMessage(100);
                }else{
                    if(pageStudyOver != 1 ){
                        mHandler.sendEmptyMessage(300);
                    }

                    if(pageStudying != 1){
                        mHandler.sendEmptyMessage(300);
                    }

                }
                if(dialog.isShowing())
                    dialog.cancel();
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
