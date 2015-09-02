package com.wj.kindergarten.ui.func;

import android.view.View;
import android.widget.LinearLayout;

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
        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
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
    }

    private void getSignList() {
        UserRequest.getSignList(mContext, "", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SignList signList = (SignList) domain;
                if (signList.getList() != null && signList.getList().getData() != null) {
                    datas.addAll((ArrayList) ((SignList) domain).getList().getData());
                }
                loadSuc();
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
