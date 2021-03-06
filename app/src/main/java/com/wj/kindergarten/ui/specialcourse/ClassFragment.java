package com.wj.kindergarten.ui.specialcourse;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SpecialCourseInfoList;
import com.wj.kindergarten.bean.SpecialCourseInfoObject;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.SchoolDetailInfoActivity;
import com.wj.kindergarten.ui.func.SpecialCourseInfoActivity;
import com.wj.kindergarten.ui.func.adapter.SpecialCourseListAdapter;
import com.wj.kindergarten.utils.ToastUtils;


import java.util.ArrayList;
import java.util.List;


public class ClassFragment extends Fragment {
    private SchoolDetailInfoActivity activity;
    private List<SpecialCourseInfoObject> list = new ArrayList<>();
    private SpecialCourseListAdapter adapter;
    private PullToRefreshListView mListView;

    public ClassFragment() {
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    //请求数据完毕，通知更新
                    if(mListView.isRefreshing()){
                        mListView.onRefreshComplete();
                    }
                    if(adapter != null)
                    adapter.setSepcialList(list);
                    break;
                case 2:
                    //没有数据
                    FrameLayout rl = (FrameLayout) view.findViewById(R.id.class_fllll);
                    ((SchoolDetailInfoActivity) getActivity()).noView(rl);
                    break;
            }
        }
    };
    int pageNo = 1;
    boolean isFirst;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始进来初始化数据
    }

    private void inidData() {
        activity  =(SchoolDetailInfoActivity) getActivity();
        UserRequest.getSpecialCourseInfoFormType(getActivity(), activity.getSchooluuid(), pageNo, -1,"","", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SpecialCourseInfoList tsif = (SpecialCourseInfoList)domain;
                if(tsif != null && tsif.getList() != null && tsif.getList().getData() != null
                        && tsif.getList().getData().size() > 0){
                    list.addAll(tsif.getList().getData());
                    mHandler.sendEmptyMessage(1);
                }else{
                    if(pageNo == 1){
                        mHandler.sendEmptyMessage(2);
                    }else{
                        activity.commonClosePullToRefreshListGridView(mListView);
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

    View view = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null){
            view = inflater.inflate(R.layout.fragment_class,null);
            mListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh_list);
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            adapter = new SpecialCourseListAdapter(getActivity());
            adapter.setSepcialList(list);
            mListView.setAdapter(adapter);
            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    pageNo++;
                    inidData();
                }
            });
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //点击启动学校详情页面
                    Intent intent = new Intent(getActivity(), SpecialCourseInfoActivity.class);
                    intent.putExtra("uuid",list.get(position-1).getUuid());
                    startActivity(intent);
                }
            });
            if(!isFirst){
                inidData();
                isFirst = true;
            }

        }

        return view;
    }





}
