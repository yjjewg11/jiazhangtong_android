package com.wj.kindergarten.ui.specialcourse;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.MineAllCourse;
import com.wj.kindergarten.bean.MineAllCourseList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.MineCourseDetailActivity;
import com.wj.kindergarten.ui.func.adapter.MineCourseDetailAdapter;

import java.util.ArrayList;
import java.util.List;


public class MineCourseFragment extends Fragment{
    View view;
    private ListView mListView;

    private int pageNo = 1;
    private MineCourseDetailAdapter adapter;
    private List<MineAllCourse> list = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    adapter.setList(list);
                    break;
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    String classuuid = null;
    private void loadData() {
        UserRequest.getMineAllCourse(getActivity(),pageNo,classuuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                MineAllCourseList mac = (MineAllCourseList) domain;
                if(mac!=null & mac.getList()!=null){
                    list.addAll(mac.getList().getData());
                    mHandler.sendEmptyMessage(100);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
           MineCourseDetailActivity mcd = (MineCourseDetailActivity) getActivity();
            classuuid = mcd.getSso().getUuid();
            loadData();
            view = inflater.inflate(R.layout.fragment_mine_course,null);
            mListView = (ListView) view.findViewById(R.id.list_view_common);
            adapter = new MineCourseDetailAdapter(getActivity());
            mListView.setAdapter(adapter);

//            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        return view;
    }
}
