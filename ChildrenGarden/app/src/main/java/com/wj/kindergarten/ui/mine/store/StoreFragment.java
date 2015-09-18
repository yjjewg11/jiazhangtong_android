package com.wj.kindergarten.ui.mine.store;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Store;
import com.wj.kindergarten.bean.StoreList;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.handler.MessageHandlerListener;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.ArticleActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * MainFragment
 *
 * @Description: 收藏
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:34
 */
public class StoreFragment extends Fragment {
    private View rootView;
    private PullToRefreshListView mListView;

    private StoreAdapter adapter;
    private ArrayList<Store> dataList = new ArrayList<Store>();
    private int nowPage = 1;
    public static final int REQUEST_CODE = 12;

    private Store store = null;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_interaction, null, false);

            mListView = (PullToRefreshListView) rootView.findViewById(R.id.pulltorefresh_list);
            adapter = new StoreAdapter(getActivity(), dataList);
            mListView.setDividerDrawable(getResources().getDrawable(R.color.line));
            mListView.setAdapter(adapter);
            mListView.setMode(PullToRefreshBase.Mode.BOTH);

            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    nowPage = 1;
                    queryStore(nowPage);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    queryStore(nowPage + 1);
                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (null != dataList && dataList.size() > 0) {
                        store = dataList.get(position - 1);
                        CGLog.d("title: " + store.getTitle());
                        if (store.getType() == 3) {
                            Constants.isStore = true;
                            Intent intent = new Intent(getActivity(), ArticleActivity.class);
                            intent.putExtra("uuid", store.getReluuid());
                            intent.putExtra("fromStore", true);
                            startActivity(intent);
                        }
                    }
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

    public void removeItem() {
        if (null != store) {
            delete(store.getUuid());
        }
    }

    private void delete(String uuid) {
        for (int i = 0; i < dataList.size(); i++) {
            Store store = dataList.get(i);
            if (null != store && store.getUuid().equals(uuid)) {
                dataList.remove(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();
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


    public void queryStore(final int page) {
        UserRequest.queryStore(getActivity(), page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                StoreList storeList = (StoreList) domain;
                if (null != storeList && storeList.getList() != null) {
                    if (page == 1) {
                        dataList.clear();
                    }
                    dataList.addAll(storeList.getList().getData());
                    adapter.notifyDataSetChanged();
                    nowPage = page;
                } else {
                    if (mListView.isRefreshing()) {
                        mListView.onRefreshComplete();
                    }
                    Utils.showToast(CGApplication.getInstance(), "收藏列表为空");
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message) && null != getActivity()) {
                    Utils.showToast(getActivity(), message);
                }
                if (null != mListView && mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
            }
        });
    }
}
