package com.wj.kindergarten.ui.specialcourse;

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
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.TeacherCount;
import com.wj.kindergarten.bean.TeacherCountList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.SchoolDetailInfoActivity;
import com.wj.kindergarten.ui.func.TeacherDetailInfoActivity;
import com.wj.kindergarten.ui.func.adapter.TeacherSpecialAdapter;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class TeachersSpecialFragment extends Fragment {
    private PullToRefreshListView mListView;
    private View view;
    private TeacherSpecialAdapter tsa;
    private ArrayList<TeacherCount> list = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 100:
                    tsa.setList(list);
                    break;
            }
        }
    };

    private int pageNo = 1;
    private SchoolDetailInfoActivity schoolDetail;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void load() {

        schoolDetail =  (SchoolDetailInfoActivity) getActivity();
        UserRequest.getTeacherFromUuid(getActivity(), (schoolDetail.getSchoolDetail().getUuid()), pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                TeacherCountList tc = (TeacherCountList) domain;
                if (tc != null && tc.getList() != null && tc.getList().getData()!= null &&
                        tc.getList().getData().size() > 0) {
                    if (!list.containsAll(tc.getList().getData())) {
                        list.addAll(tc.getList().getData());
                        mHandler.sendEmptyMessage(100);
                    }
                }else{
                    if(pageNo == 1){
                        if(view != null){
                            ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.no_view_fl);
                            viewGroup.removeAllViews();
                            View no = View.inflate(getActivity(),R.layout.nothing_view,null);
                            viewGroup.addView(no);
                        }
                    }else{
                        ToastUtils.showMessage("没有更多内容了!");
                    }
                }
                if(mListView.isRefreshing()){
                    mListView.onRefreshComplete();
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

    boolean isFirst ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (view == null){
            view = inflater.inflate(R.layout.fragment_teachers_special,null);
            mListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh_list);
            tsa = new TeacherSpecialAdapter(getActivity());
            mListView.setAdapter(tsa);
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    pageNo++;
                    load();
                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //点击跳转老师详情页面
                    Intent intent = new Intent(getActivity(), TeacherDetailInfoActivity.class);
                    intent.putExtra("teacheruuid",list.get((int)id).getUuid());
                    startActivity(intent);
                }
            });

            if(!isFirst){
                load();
                isFirst = true;
            }

        }
        return view;
    }
}
