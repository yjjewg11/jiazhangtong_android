package com.wj.kindergarten.ui.specialcourse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.func.adapter.SpecialCourseListAdapter;


public class TeachersSpecialFragment extends Fragment {
    private PullToRefreshListView mListView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null){
            view = inflater.inflate(R.layout.fragment_teachers_special,null);
            mListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh_list);
            mListView.setAdapter(new SpecialCourseListAdapter(getActivity()));
        }
        return view;
    }
}
