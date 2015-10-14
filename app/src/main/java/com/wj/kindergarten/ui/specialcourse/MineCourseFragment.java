package com.wj.kindergarten.ui.specialcourse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.func.adapter.MineCourseDetailAdapter;


public class MineCourseFragment extends Fragment{
    View view;
    private PullToRefreshListView mListView;

    private int pageNo = 1;
    private MineCourseDetailAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.layout_pulltorefresh,null);
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
                }
            });
        }
        return view;
    }
}
