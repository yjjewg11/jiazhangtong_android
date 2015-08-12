package com.wj.kindergarten.ui.func;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.CourseList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * CourseListActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 17:08
 */
public class CourseListActivity extends BaseActivity {

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_course_list;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = true;
    }

    @Override
    protected void loadData() {
        setTitleText("课程表");
        getCourseList();
    }

    @Override
    protected void onCreate() {
        setTitleText("课程表");

    }

    private void getCourseList() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(calendar.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        String date = mYear + "-" + mMonth + "-" + mDay;
        String uuid = "";
        if (CGApplication.getInstance().getLogin() != null && CGApplication.getInstance().getLogin().getList() != null
                && CGApplication.getInstance().getLogin().getList().size() > 0) {
            uuid = CGApplication.getInstance().getLogin().getList().get(0).getClassuuid();
        }
        UserRequest.getCourseList(mContext, date, date, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                CourseList courseList = (CourseList) domain;
                if (courseList != null && courseList.getList() != null && courseList.getList().size() > 0) {
                    loadSuc();
                } else {
                    loadEmpty();
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                loadFailed();
            }
        });
    }
}
