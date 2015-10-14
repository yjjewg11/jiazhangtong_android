package com.wj.kindergarten.ui.func;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.specialcourse.MineCourseFragment;
import com.wj.kindergarten.ui.specialcourse.MineDiscussFragment;
import com.wj.kindergarten.ui.specialcourse.SimpleIntroduceFragment;

public class MineCourseDetailActivity extends BaseActivity{
    private RadioGroup radioGroup;
    private RadioButton radio_course;
    private RadioButton radio_discuss;
    private RadioButton radio_introduce;
    private RadioButton[] radio_bts;
    private ViewPager viewPager;
    private Fragment [] fragments = new Fragment[3];

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_mine_course_detail;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {

        titleCenterTextView.setText("我的课程详情");
        initViews();
    }

    private void initViews() {

        radioGroup = (RadioGroup) findViewById(R.id.mine_course_detail_radio_group);
        radio_bts = new RadioButton[]{
                (RadioButton)findViewById(R.id.mine_course_detail_tab_course),
                (RadioButton)findViewById(R.id.mine_course_detail_tab_introduce),
                (RadioButton)findViewById(R.id.mine_course_detail_tab_discuss)
        };

        for(RadioButton bt :radio_bts){
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = -1;
                    switch (v.getId()){
                        case R.id.mine_course_detail_tab_course : id = 0; break;
                        case R.id.mine_course_detail_tab_introduce : id = 1; break;
                        case R.id.mine_course_detail_tab_discuss : id = 2; break;
                    }
                    viewPager.setCurrentItem(id);
                }
            });
        }

        viewPager = (ViewPager)findViewById(R.id.mine_course_detail_viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(fragments[position] == null) {
                    if(position == 0) {
                        fragments[position] = new MineCourseFragment();
                    }else if(position == 1){
                        fragments [position] = new SimpleIntroduceFragment();
                    }else {
                        fragments [position] = new MineDiscussFragment();
                    }
                }
                return fragments[position];
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                   int id = 0;
                switch (position){
                    case 0 : id = R.id.mine_course_detail_tab_course; break;
                    case 1 : id = R.id.mine_course_detail_tab_introduce; break;
                    case 2 : id = R.id.mine_course_detail_tab_discuss; break;
                }
                radioGroup.check(id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
