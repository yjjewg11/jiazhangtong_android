package com.wj.kindergarten.ui.func;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MoreSpecialDiscussActivity extends BaseActivity{
    private PullToRefreshListView mListView;
    private MoreDiscussAdapter adapter;
    private ArrayList<MoreDiscuss> moreList = new ArrayList<>();
    private TextView no_assess_tv;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_more_special_discuss;

    }

    private String ext_uuid;
    private int pageNo = 2;
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

//        isNeedLoading  = true;
    }

    @Override
    protected void onCreate() {
        no_assess_tv = (TextView)findViewById(R.id.no_assess_tv);
        titleCenterTextView.setText("更多评论");
        Intent intent = getIntent();
        ext_uuid = intent.getStringExtra("uuid");
        moreList = (ArrayList<MoreDiscuss>) intent.getSerializableExtra("discussList");
        mListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        adapter =  new MoreDiscussAdapter(this);
        mListView.setAdapter(adapter);
        adapter.setList(moreList);
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
    }

    boolean isFirst ;

    public void loadMyData(){
        UserRequest.getMoreDiscuss(this, ext_uuid, pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                MoreDiscussList mdl = (MoreDiscussList) domain;
                if (mdl != null && mdl.getList() != null && mdl.getList().getData() != null &&
                        mdl.getList().getData().size() > 0) {
                    list.addAll(mdl.getList().getData());
                    mhandler.sendEmptyMessage(1);
                }else{
                    ToastUtils.showMessage("没有更多内容了!");
                    if(mListView.isRefreshing()) mListView.onRefreshComplete();
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
