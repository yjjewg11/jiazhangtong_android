package com.wj.kindergarten.ui.func;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Sign;
import com.wj.kindergarten.bean.SignList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.SignAdapter;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * SignListActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 2:15
 */
public class SignListActivity extends BaseActivity {
    private PullToRefreshListView mListView;
    private LinearLayout contentLayout;
    private LinearLayout noneLayout;
    private SignAdapter signAdapter;
    private List<Sign> datas = new ArrayList<>();


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_sign_list;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = true;
    }

    @Override
    protected void loadData() {
        setTitleText("签到记录");
        getSignList();
    }

    @Override
    protected void onCreate() {
        setTitleText("签到记录");

        mListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        signAdapter = new SignAdapter(mContext, datas);
        mListView.setAdapter(signAdapter);
        contentLayout = (LinearLayout) findViewById(R.id.sign_content);
        noneLayout = (LinearLayout) findViewById(R.id.sign_none);

        if (datas == null || datas.size() <= 0) {
            contentLayout.setVisibility(View.GONE);
            noneLayout.setVisibility(View.VISIBLE);
        } else {
            contentLayout.setVisibility(View.VISIBLE);
            noneLayout.setVisibility(View.GONE);
        }

        setListeners();
    }

    private void setListeners() {
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                page++;
                getSignList();

            }
        });
    }

    int page = 1;
    private void getSignList() {
        UserRequest.getSignList(mContext, "",page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SignList signList = (SignList) domain;
<<<<<<< HEAD
                if (signList.getList() != null && signList.getList().getData() != null && signList.getList().getData().size() > 0) {
                    datas.addAll(signList.getList().getData());
                    if(signAdapter!=null) {signAdapter.notifyDataSetChanged();}
                    if(page == 1){
                        loadSuc();
                    }
                    if(mListView.isRefreshing()){
                        mListView.onRefreshComplete();
                    }
                }else{
                   loadEmpty();
                }





=======
                if(mListView.isRefreshing()){
                    mListView.onRefreshComplete();
                }

                if (signList.getList() != null && signList.getList().getData() != null) {
                    datas.addAll((ArrayList) ((SignList) domain).getList().getData());
                    if(signAdapter!=null) {signAdapter.notifyDataSetChanged();}
                }
                loadSuc();
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(SignListActivity.this, message);
                }
                loadFailed();
            }
        });
    }


}
