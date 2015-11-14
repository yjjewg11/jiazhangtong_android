package com.wj.kindergarten.ui.specialcourse;

import android.os.Bundle;

import android.os.Handler;
import android.os.Message;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.MineAllCourse;
import com.wj.kindergarten.bean.MineAllCourseList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.MineCourseDetailActivity;
import com.wj.kindergarten.ui.func.adapter.MineCourseDetailAdapter;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class MineCourseFragment extends Fragment{
    View view;
    private PullToRefreshListView mListView;

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
    private FrameLayout fl;
    private MineCourseDetailActivity mcd;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    String classuuid = null;
    private void loadData() {
        mcd =(MineCourseDetailActivity) getActivity();
        mcd.showDialog();
        UserRequest.getMineAllCourse(getActivity(),pageNo,classuuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                mcd.cancleDialog();
                if(mListView.isRefreshing()) {mListView.onRefreshComplete();}
                MineAllCourseList mac = (MineAllCourseList) domain;
                if(mac!=null & mac.getList()!=null && mac.getList().getData().size() > 0){
                    list.addAll(mac.getList().getData());
                    mHandler.sendEmptyMessage(100);
                }else{
                    if(pageNo == 1){
                        ((MineCourseDetailActivity) getActivity()).noView(fl);
                    }else{
                        ToastUtils.showMessage("没有更多内容了!");
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(view != null) return view;
           MineCourseDetailActivity mcd = (MineCourseDetailActivity) getActivity();
            classuuid = mcd.getCourseuuid();
            loadData();
            view = inflater.inflate(R.layout.fragment_mine_course,null);
            fl =(FrameLayout) view.findViewById(R.id.mine_course_detail_fl);
            mListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh_list);
            adapter = new MineCourseDetailAdapter(getActivity());
            mListView.setAdapter(adapter);
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                            pageNo++;
                            loadData();
                }
            });

//            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//        mcd.viewMap.put(GloablUtils.MINE_COURSE_FRAGMENT,view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("TAG","fragment  1111 的销毁已经执行！");
    }
}
