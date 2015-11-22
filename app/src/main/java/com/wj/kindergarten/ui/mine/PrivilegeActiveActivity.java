package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PrivilegeActiveList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.PrivilegeDetailActivity;
import com.wj.kindergarten.ui.func.adapter.PrivilegeActiveAdapter;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/10/29.
 */
public class PrivilegeActiveActivity extends BaseActivity {
    private PullToRefreshListView listView;
    private int pageNo = 0;
    private PrivilegeActiveAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    //成功，关闭刷新
                    if(listView.isRefreshing()){
                        listView.onRefreshComplete();
                    }
                    break;
                case 2:
                    listView.setRefreshing();
                    break;
            }
        }
    };
    private boolean isFirst;
    private FrameLayout privelige_nocontent_fl;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_privilege_head_page;
     }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        privelige_nocontent_fl = (FrameLayout)findViewById(R.id.privelige_nocontent_fl);
        titleCenterTextView.setText("优惠活动");
        listView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        adapter = new PrivilegeActiveAdapter(this);
        listView.setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setDividerDrawable(null);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!isFirst) {
                    pageNo = 1;
                    loadData();
                    isFirst = true;
                } else {
                    handler.sendEmptyMessageDelayed(1,300);
                }


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                pageNo++;
                loadData();
            }
        });
        handler.sendEmptyMessageDelayed(2,300);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PrivilegeActiveActivity.this, PrivilegeDetailActivity.class);
                intent.putExtra("pauuid",adapter.getList().get(position-1).getUuid());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {
        //加载数据
        UserRequest.getPrivilegeByPage(this,pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                PrivilegeActiveList pal = (PrivilegeActiveList) domain;
                if(pal != null && pal.getList() != null && pal.getList().getData()!=null && pal.getList().getData().size() > 0 ){
                    adapter.setList(pal.getList().getData());
                    handler.sendEmptyMessage(1);
                }else{
                    if(pageNo == 1){
                        noView(findViewById(R.id.privelige_nocontent_fl));
                    }else{
                        //关闭请求
                        listView.onRefreshComplete();
                        listView.setMode(PullToRefreshBase.Mode.DISABLED);
                        ToastUtils.showMessage("没有更多内容了!");
                    }

                }
                if(listView.isRefreshing()) listView.onRefreshComplete();
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
