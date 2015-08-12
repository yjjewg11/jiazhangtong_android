package com.wj.kindergarten.ui.func;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Interaction;
import com.wj.kindergarten.bean.InteractionList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.adapter.InteractionAdapter;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * MainFragment
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:34
 */
public class InteractionFragment extends Fragment {
    private View rootView;
    private PullToRefreshListView mListView;

    private InteractionAdapter interactionAdapter;
    private List<Interaction> dataList = new ArrayList<>();
    private int nowPage = 1;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((BaseActivity) getActivity()).clearCenterIcon();
//        ((BaseActivity) getActivity()).setTitleText("互动", R.drawable.interaction_send);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_interaction, null, false);

            mListView = (PullToRefreshListView) rootView.findViewById(R.id.pulltorefresh_list);
            interactionAdapter = new InteractionAdapter(getActivity(), dataList);
            mListView.setDividerDrawable(getResources().getDrawable(R.color.line));
            mListView.setAdapter(interactionAdapter);
            mListView.setMode(PullToRefreshBase.Mode.BOTH);

            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    nowPage = 1;
                    getInteractionList(nowPage);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    getInteractionList(nowPage + 1);
                }
            });

            mHandler.sendEmptyMessageDelayed(0, 300);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!mListView.isRefreshing()) {
                        mListView.setRefreshing();
                    }
                    break;
                case 1:

                    break;
            }
        }
    };

    private void getInteractionList(final int page) {
        UserRequest.getInteractionList(getActivity(), "", page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                InteractionList interactionList = (InteractionList) domain;
                if (interactionList != null && interactionList.getList() != null
                        && interactionList.getList().getData() != null) {
                    if (page == 1) {
                        dataList.clear();
                    }
//                    dataList.addAll(interactionList.getList().getData());
                    dataList.addAll(interactionList.getList().getData());
                    interactionAdapter.notifyDataSetChanged();
                    nowPage = page;
                } else {
                    if (mListView.isRefreshing()) {
                        mListView.onRefreshComplete();
                    }
                    Utils.showToast(CGApplication.getInstance(), "获取互动列表失败");
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                Utils.showToast(CGApplication.getInstance(), message);
            }
        });
    }
}
