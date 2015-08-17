package com.wj.kindergarten.ui.func;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * CourseListActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 17:08
 */
public class CourseListActivity extends BaseActivity {
    private ViewPager viewPager;

    private final int MAX_WEEKEND = 21;
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_course_list;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = false;
    }

    @Override
    protected void onCreate() {
        setTitleText("课程表");

        viewPager = (ViewPager) findViewById(R.id.course_pager);
        CourseFragmentAdapter courseFragmentAdapter = new CourseFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(courseFragmentAdapter);
        viewPager.setCurrentItem(MAX_WEEKEND / 2, false);
    }

    private String calculateCurrentDate(int current) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        int middle = MAX_WEEKEND / 2;
        calendar.add(Calendar.DAY_OF_MONTH, 7 * (current - middle));
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
        String startDay = df.format(calendar.getTime());
        calendar.add(Calendar.DATE, 6);
        String endDay = df.format(calendar.getTime());
        return startDay + "~" + endDay;
    }

    class CourseFragmentAdapter extends FragmentPagerAdapter {
        public CourseFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CourseFragment.buildCourseFragment(calculateCurrentDate(position));
        }

        @Override
        public int getCount() {
            return MAX_WEEKEND;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

        }
    }
}
