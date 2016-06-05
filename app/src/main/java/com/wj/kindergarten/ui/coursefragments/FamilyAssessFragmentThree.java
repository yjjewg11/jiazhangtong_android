package com.wj.kindergarten.ui.coursefragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.MoreDiscuss;
import com.wj.kindergarten.bean.MoreDiscussList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.SpecialCourseInfoActivity;
import com.wj.kindergarten.ui.func.adapter.MoreDiscussAdapter;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2015/11/19.
 */
public class FamilyAssessFragmentThree extends Fragment {
    private View view;
    private PullToRefreshListView mListView;
    private MoreDiscussAdapter adapter;
    private List<MoreDiscuss> moreList = new ArrayList<>();
    private Activity activity;
    private int pageNo = 1;
    private HintInfoDialog dialog;
    private RelativeLayout container_ll;


    public PullToRefreshListView getmListView() {
        return mListView;
    }

    public void setNoRequest(){
        if(mListView.isRefreshing()) mListView.onRefreshComplete();

        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(view != null) return view;
        activity =  getActivity();
        view = View.inflate(getActivity(),R.layout.full_layout_list, null);
        container_ll = (RelativeLayout)view.findViewById(R.id.course_detail_rl);
        mListView =(PullToRefreshListView) view.findViewById(R.id.pulltorefresh_list);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mListView.onRefreshComplete();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                 pageNo++;
                 getAssess(pageNo);
            }
        });
        adapter =  new MoreDiscussAdapter(getActivity());
        mListView.setAdapter(adapter);
        return view;
    }

    public void noData(){
        ((BaseActivity)activity).noView(container_ll);
    }

    public void getAssess(final int page) {
        //获取该对象的评价
        if(dialog == null) dialog = new HintInfoDialog(getActivity());
        UserRequest.getMoreDiscuss(getActivity(), ((SpecialCourseInfoActivity)activity).getUuid(), page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                MoreDiscussList mdl = (MoreDiscussList) domain;
                if (mdl.getList() != null && mdl.getList().getData() != null
                        && mdl.getList().getData().size() > 0) {
                    moreList.addAll(mdl.getList().getData());
                    adapter.setList(moreList);
                } else {
                    if (page == 1) {
                        if(activity instanceof SpecialCourseInfoActivity){
                            ((SpecialCourseInfoActivity)activity).familyFragment.noData();
                        }

                    }else{
                        ((SpecialCourseInfoActivity)activity).familyFragment.setNoRequest();
                        ToastUtils.noMoreContentShow();
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
