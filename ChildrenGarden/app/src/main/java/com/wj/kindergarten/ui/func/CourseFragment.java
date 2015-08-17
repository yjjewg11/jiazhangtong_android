package com.wj.kindergarten.ui.func;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Course;
import com.wj.kindergarten.bean.CourseList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;

import java.util.List;

/**
 * CourseFragment
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/16 13:23
 */
public class CourseFragment extends Fragment {
    private TextView courseDateTv;

    private Context mContext;
    private String date;
    private List<Course> courses;

    public static CourseFragment buildCourseFragment(String date) {
        CourseFragment courseFragment = new CourseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        courseFragment.setArguments(bundle);
        return courseFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getArguments().getString("date");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = View.inflate(mContext, R.layout.fragment_course_list, null);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        courseDateTv = (TextView) view.findViewById(R.id.course_date);
        courseDateTv.setText(date);

    }

    private void initDate() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && date != null) {
            getCourseList();
        }
    }

    private void showProgressDialog() {
        if (mContext != null) {
            ((CourseListActivity) mContext).showProgressDialog();
        }
    }

    private void hideProgressDialog() {
        if (mContext != null) {
            ((CourseListActivity) mContext).hideProgressDialog();
        }
    }

    private void getCourseList() {
        showProgressDialog();
        String[] startEndDay = date.split("~");
        String uuid = "";
        if (CGApplication.getInstance().getLogin() != null && CGApplication.getInstance().getLogin().getList() != null
                && CGApplication.getInstance().getLogin().getList().size() > 0) {
            uuid = CGApplication.getInstance().getLogin().getList().get(0).getClassuuid();
        }
        UserRequest.getCourseList(mContext, startEndDay[0], startEndDay[1], uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                hideProgressDialog();
                CourseList courseList = (CourseList) domain;
                if (courseList != null && courseList.getList() != null && courseList.getList().size() > 0) {
                    courses = courseList.getList();
                } else {

                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                hideProgressDialog();

            }
        });
    }
}
