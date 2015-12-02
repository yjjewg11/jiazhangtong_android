package com.wj.kindergarten.ui.recuitstudents.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.MoreDiscuss;
import com.wj.kindergarten.bean.MoreDiscussList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.SchoolHtmlActivity;
import com.wj.kindergarten.ui.func.adapter.MoreDiscussAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2015/12/1.
 */
public class SchoolAssessFragment extends Fragment {
    private View view;
    private PullToRefreshListView listView;
    private MoreDiscussAdapter adapter;
    private int pageNo = 1;
    private boolean isFirst;
    private SchoolHtmlActivity activity;
    private List<MoreDiscuss> list = new ArrayList<>();
    private LinearLayout container_ll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;

        activity = (SchoolHtmlActivity)getActivity();
        view = inflater.inflate(R.layout.layout_pulltorefresh,null);
        container_ll = (LinearLayout)view.findViewById(R.id.contain_ll);
        listView =(PullToRefreshListView) view.findViewById(R.id.pulltorefresh_list);
        adapter =  new MoreDiscussAdapter(getActivity());
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                   pageNo++;
                   queryAssess();
            }
        });



        return view;
    }

    public void queryAssess(){
        UserRequest.getMoreDiscuss(activity,activity.getSchoolDetail().getUuid(),pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                MoreDiscussList mdl = (MoreDiscussList) domain;
                if(mdl != null && mdl.getList() != null && mdl.getList().getData() != null
                        && mdl.getList().getData().size() > 0){
                    list.addAll(mdl.getList().getData());
                    adapter.setList(list);
                }else{
                    if(pageNo == 1){
                        activity.noView(container_ll);
                    }else{
                        activity.commonClosePullToRefreshListGridView(listView);
                    }
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
