package com.wj.kindergarten.ui.specialcourse;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.wj.kindergarten.ui.func.adapter.SpecialCourseListAdapter;

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
            }
        }
    };
    int pageNo = 1;
    boolean isFirst;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity  =(SchoolDetailInfoActivity) getActivity();
        //初始进来初始化数据
        if(!isFirst){
            inidData();
            isFirst = true;
        }

    }

    private void inidData() {

        UserRequest.getSpecialCourseInfoFormType(getActivity(), activity.schoolUuid, pageNo, 0, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SpecialCourseInfoList tsif = (SpecialCourseInfoList)domain;
                if(tsif != null && tsif.getList() != null){
                    list.addAll(tsif.getList().getData());
                    mHandler.sendEmptyMessage(1);
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
            adapter = new SpecialCourseListAdapter(getActivity());
            adapter.setSepcialList(list);
            mListView.setAdapter(adapter);
            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    pageNo++;
                    inidData();

                }
            });
        }

        return view;
    }





}
