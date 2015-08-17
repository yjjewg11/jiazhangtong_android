package com.wj.kindergarten.ui.func;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.Food;
import com.wj.kindergarten.ui.BaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * FoodListActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 21:03
 */
public class FoodListActivity extends BaseActivity {
    private ViewPager viewPager;
    private List<Food> foods = new ArrayList<>();
    private final int MAX_DAY = 41;
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_food_list;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = false;
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onCreate() {
        setTitleText("每日食谱");
        viewPager = (ViewPager) findViewById(R.id.food_fragment);

        viewPager.setAdapter(new FoodFragmentAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(MAX_DAY / 2, false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isProgressDialogIsShowing()) {
                hideProgressDialog();
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String calculateCurrentDate(int current) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int middle = MAX_DAY / 2;
        calendar.add(Calendar.DAY_OF_MONTH, current - middle);
        return df.format(calendar.getTime());
    }

    class FoodFragmentAdapter extends FragmentPagerAdapter {
        public FoodFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FoodFragment.buildFoodFragment(calculateCurrentDate(position));
        }

        @Override
        public int getCount() {
            return MAX_DAY;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

        }
    }
}
