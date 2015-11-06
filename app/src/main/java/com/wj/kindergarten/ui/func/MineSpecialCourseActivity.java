package com.wj.kindergarten.ui.func;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MineSpecialCourseActivity extends BaseActivity{
    private PullToRefreshListView mListView;
    private MineCourseStatusAdapter adapter;
    private RadioButton rb_study;
    private RadioButton rb_study_over;
    private TextView red_left;
    private TextView red_right;
    private List<StudyStateObject> studyint_list = new ArrayList<>();
    private List<StudyStateObject> over_list = new ArrayList<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 100:
                if(isStudying == 0){
                    adapter.setList(studyint_list);
                }else{
                    adapter.setList(over_list);
                }
                    closeFresh();
                    break;
                case 1:
                    loadData();
                    break;
                case 300:
                    ToastUtils.showMessage("没有更多内容了！");
                    closeFresh();
                    break;
            }
        }
    };

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

        rb_study = (RadioButton)findViewById(R.id.rb_study);
        rb_study_over = (RadioButton)findViewById(R.id.rb_study_over);

        red_left = (TextView)findViewById(R.id.red_left);
        red_right = (TextView)findViewById(R.id.red_right);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.rb_study ){

                    isStudying = 0;
                    red_left.setVisibility(View.VISIBLE);
                    red_right.setVisibility(View.INVISIBLE);
                } else if(v.getId() == R.id.rb_study_over){
                    red_left.setVisibility(View.INVISIBLE);
                    red_right.setVisibility(View.VISIBLE);
                    isStudying = 1;
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
                intent.putExtra("object",sso);
                startActivity(intent);
            }
        });
    }

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
        UserRequest.getStudyStatus(this,page,isStudying, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
              StudyStateObjectList sso = (StudyStateObjectList) domain;
                if(sso!=null && sso.getList()!=null && sso.getList().getData() != null && sso.getList().getData().size() > 0){
                    if(isStudying == 0){
                        studyint_list.addAll(sso.getList().getData());
                    }else{
                        over_list.addAll(sso.getList().getData());
                    }
                    mHandler.sendEmptyMessage(100);
                }else{
                    mHandler.sendEmptyMessage(300);
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
