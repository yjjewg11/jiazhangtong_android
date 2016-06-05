package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.videogo.constant.IntentConsts;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SingleCameraInfo;
import com.wj.kindergarten.bean.VideoList;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.VideoListAdapter;
import com.wj.kindergarten.ui.more.PfRefreshLinearLayout;
import com.wj.kindergarten.utils.ToastUtils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends BaseActivity {


    @ViewInject(id = R.id.pulltorefresh_list)
    PullToRefreshListView listView;
    private List<SingleCameraInfo> list = new ArrayList<>();
    private VideoListAdapter adapter;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_video_list;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText(getResources().getString(R.string.video));
        FinalActivity.initInjectedView(this);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        initRefreshListener();
        initOnItemClickListener();
        adapter = new VideoListAdapter(this);
        listView.setAdapter(adapter);
        queryViewByPage(pageNo);
    }

    private void initOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VideoListActivity.this,RealPlayActivity.class);
                intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO,list.get(position - 1));
                startActivity(intent);
            }
        });
    }

    private void initRefreshListener() {
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo = 1;
                queryViewByPage(pageNo);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo++;
                queryViewByPage(pageNo);
            }
        });
    }

    int pageNo = 1;
    public void queryViewByPage(final int pageNo){
        UserRequest.getVideoListByPage(this, pageNo, new RequestFailedResult() {
            @Override
            public void result(BaseModel domain) {
                listView.onRefreshComplete();
                VideoList videoList = (VideoList) domain;
                if(videoList != null && videoList.getList() != null &&
                        videoList.getList().getData() != null && videoList.getList().getData().size()> 0){
                    if(pageNo == 1)list.clear();
                    list.addAll(videoList.getList().getData());
                    adapter.setList(list);
                }else {
                    if(pageNo == 1){
                        listView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }else {
                        listView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                    ToastUtils.showMessage("没有更多内容了!!！");
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }
        });
    }
}
