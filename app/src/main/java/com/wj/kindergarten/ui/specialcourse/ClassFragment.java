package com.wj.kindergarten.ui.specialcourse;

<<<<<<< HEAD
import android.content.Intent;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.AdapterView;
import android.widget.FrameLayout;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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
<<<<<<< HEAD
import com.wj.kindergarten.ui.func.SpecialCourseInfoActivity;
import com.wj.kindergarten.ui.func.adapter.SpecialCourseListAdapter;
import com.wj.kindergarten.utils.ToastUtils;
=======
import com.wj.kindergarten.ui.func.adapter.SpecialCourseListAdapter;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

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
<<<<<<< HEAD
                case 2:
                    //没有数据
                    FrameLayout rl = (FrameLayout) view.findViewById(R.id.class_fllll);
                    ((SchoolDetailInfoActivity) getActivity()).noView(rl);
                    break;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
            }
        }
    };
    int pageNo = 1;
    boolean isFirst;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
<<<<<<< HEAD

        //初始进来初始化数据
    }

    private void inidData() {
        activity  =(SchoolDetailInfoActivity) getActivity();
        UserRequest.getSpecialCourseInfoFormType(getActivity(), activity.getSchool().getUuid(), pageNo, -1,"","", new RequestResultI() {
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
                        if(mListView.isRefreshing()) mListView.onRefreshComplete();
                        ToastUtils.showMessage("没有更多内容了!");
                    }

=======
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
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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
<<<<<<< HEAD
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
                    intent.putExtra("object",list.get(position-1));
                    startActivity(intent);
                }
            });
            if(!isFirst){
                inidData();
                isFirst = true;
            }
=======
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
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
        }

        return view;
    }





}
