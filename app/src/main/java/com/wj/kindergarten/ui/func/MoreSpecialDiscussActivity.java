package com.wj.kindergarten.ui.func;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.MoreDiscuss;
import com.wj.kindergarten.bean.MoreDiscussList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.MoreDiscussAdapter;

import java.util.ArrayList;
import java.util.List;

public class MoreSpecialDiscussActivity extends BaseActivity{
    private PullToRefreshListView mListView;
    private MoreDiscussAdapter adapter;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_more_special_discuss;

    }

    private String ext_uuid;
    private int pageNo = 1;
    private List<MoreDiscuss> list = new ArrayList<>();
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    //获取数据完毕，通知更新
                    adapter.setList(list);
                    if(mListView.isRefreshing()){
                        mListView.onRefreshComplete();
                    }
                    break;
            }
        }
    };

    @Override
    protected void setNeedLoading() {

        isNeedLoading  = true;
    }

    @Override
    protected void onCreate() {
        titleCenterTextView.setText("更多评论");
        Intent intent = getIntent();
        ext_uuid = intent.getStringExtra("discuss");
        mListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        adapter =  new MoreDiscussAdapter(this);
        mListView.setAdapter(adapter);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                         pageNo++;
                         loadMyData();
            }
        });

        loadMyData();
    }

    boolean isFirst ;

    public void loadMyData(){
        UserRequest.getMoreDiscuss(this, ext_uuid, pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                MoreDiscussList mdl = (MoreDiscussList) domain;
                if (mdl != null && mdl.getList() != null) {
                    list.addAll(mdl.getList().getData());
                }
                if (!isFirst) {
                    loadSuc();
                } else {
                    mhandler.sendEmptyMessage(1);
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
