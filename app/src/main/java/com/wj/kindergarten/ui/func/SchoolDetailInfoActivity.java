package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.specialcourse.ClassFragment;
import com.wj.kindergarten.ui.specialcourse.SimpleIntroduceFragment;
import com.wj.kindergarten.ui.specialcourse.TeachersSpecialFragment;
import com.wj.kindergarten.utils.ShareUtils;

public class SchoolDetailInfoActivity extends BaseActivity{
    private ViewPager viewPager;

    public String schoolUuid;
    private RelativeLayout[] relatives;
    private RadioGroup radioGroup;
    private RadioButton bt_course,tab_teacher,tab_introduce;
    private RadioButton[] radioButtons;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_schllo_detail_info;
    }

    @Override
    protected void setNeedLoading() {

    }

    private Fragment classInfo,teachers,introduce;

    private Fragment [] fragments = new Fragment[]{
         classInfo,teachers,introduce
    };
    @Override
    protected void onCreate() {

        titleCenterTextView.setText("学校详情");
        radioGroup = (RadioGroup) findViewById(R.id.special_radio_group);
        Intent intent = getIntent();
        schoolUuid = intent.getStringExtra("schoolUuid");
        viewPager = (ViewPager)findViewById(R.id.shcool_detail_viewPager);
        setViews();

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                if(fragments[position]==null){
                    switch (position){
                        case 0: fragments[position] =  new ClassFragment(); break;
                        case 1: fragments[position] =  new TeachersSpecialFragment(); break;
                        case 2: fragments[position] =  new SimpleIntroduceFragment(); break;
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
                    case 0: id = R.id.tab_course; break;
                    case 1: id = R.id.tab_teacher; break;
                    case 2: id = R.id.tab_introduce; break;
                }
                        radioGroup.check(id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setViews() {
        relatives = new RelativeLayout[]{
                (RelativeLayout) findViewById(R.id.train_course_tab_shoucang),
                (RelativeLayout) findViewById(R.id.train_course_tab_share),
                (RelativeLayout) findViewById(R.id.train_course_tab_interaction),
                (RelativeLayout) findViewById(R.id.train_course_tab_ask),
        };
        for(RelativeLayout rl : relatives){
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.train_course_tab_shoucang:

                            break;
                        case R.id.train_course_tab_share:
                            ShareUtils.showShareDialog(SchoolDetailInfoActivity.this, v, "", "", "", "", false);
                            break;
                        case R.id.train_course_tab_interaction:

                            break;
                        case R.id.train_course_tab_ask:
                            Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "18780475970"));
                            startActivity(phoneIntent);
                            break;
                    }
                }
            });
        }



        radioButtons  = new RadioButton[]{
                (RadioButton) findViewById(R.id.tab_course),
                (RadioButton) findViewById(R.id.tab_teacher),
                (RadioButton) findViewById(R.id.tab_introduce)
        };

        for(RadioButton radioButton : radioButtons){
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int vp = 100;
                    switch (v.getId()){

                        case R.id.tab_course: vp = 0; break;
                        case R.id.tab_teacher: vp = 1; break;
                        case R.id.tab_introduce: vp = 2; break;
                    }

                    viewPager.setCurrentItem(vp);
                }
            });
        }
    }


}
