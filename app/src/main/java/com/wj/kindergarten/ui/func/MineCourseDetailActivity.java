package com.wj.kindergarten.ui.func;


<<<<<<< HEAD
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.OnceSpecialCourse;
import com.wj.kindergarten.bean.OnceSpecialCourseList;
import com.wj.kindergarten.bean.SchoolDetailList;
import com.wj.kindergarten.bean.StudyStateObject;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.specialcourse.MineCourseFragment;
import com.wj.kindergarten.ui.specialcourse.MineDiscussFragment;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.List;
=======
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
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

public class MineCourseDetailActivity extends BaseActivity{
    private RadioGroup radioGroup;
    private RadioButton radio_course;
    private RadioButton radio_discuss;
    private RadioButton radio_introduce;
    private RadioButton[] radio_bts;
    private ViewPager viewPager;
    private Fragment [] fragments = new Fragment[3];
<<<<<<< HEAD
    private StudyStateObject sso;
    private RelativeLayout[] relativeLayouts;
    private HintInfoDialog dialog;
    private TextView tv_coll;
    private ImageView iv_coll;
    private OnceSpecialCourse courses;
    private OnceSpecialCourseList oscs;
    private TextView trainSchoolName;
    private TextView student;
    private TextView openTime;
    private TextView schoolName;
    private ImageView iv_school;
    private TextView className;

    public OnceSpecialCourse getCourses() {
        return courses;
    }

    public OnceSpecialCourseList getOscs() {
        return oscs;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 1:
                   //获取课程详情成功，添加数据。
                   break;
           }
        }
    };

    public StudyStateObject getSso() {
        return sso;
    }
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

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
<<<<<<< HEAD
        sso = (StudyStateObject) getIntent().getSerializableExtra("object");
        initViews();
        setChooseRl();
        UserRequest.getSpecialCourseINfoFromClickItem(this, sso.getCourseuuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                oscs = (OnceSpecialCourseList) domain;
                if(oscs != null && oscs.getData() != null){
                    courses =  oscs.getData();
                    ((CourseDetailIntroduceFragment) fragments[1]).setCourse(courses);
                }

                if(!oscs.isFavor()){
                    tv_coll.setText("已收藏");
                    iv_coll.setImageResource(R.drawable.store2);
                }else{
                    tv_coll.setText("收藏");
                    iv_coll.setImageResource(R.drawable.store1);
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


    private void setChooseRl() {
        relativeLayouts = new RelativeLayout[]{
                (RelativeLayout)findViewById(R.id.train_course_tab_shoucang),
                (RelativeLayout)findViewById(R.id.train_course_tab_share),
                (RelativeLayout)findViewById(R.id.train_course_tab_interaction),
                (RelativeLayout)findViewById(R.id.train_course_tab_ask)
        };
        View.OnClickListener listenersss = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.train_course_tab_shoucang:
                        //发送接口
                        if(tv_coll.getText().toString().equals("收藏")){
                            store();
                        }else{
                            cancelStore();
                        }
                        break;
                    case R.id.train_course_tab_share:
                        if(sso != null){
                            ShareUtils.showShareDialog(MineCourseDetailActivity.this, v, Utils.isNull(courses.getTitle())
                                    , Utils.isNull(courses.getContent()), sso.getLogo(), oscs.getShare_url(), false);
                        }else{
                            ToastUtils.showMessage("暂无分享内容!");
                        }

                        break;
                    case R.id.train_course_tab_interaction:
                        //启动互动界面
                        Intent intent = new Intent(MineCourseDetailActivity.this,CourseInteractionListActivity.class);
                        intent.putExtra("newsuuid",sso.getUuid());
                        intent.putExtra("type",NormalReplyListActivity.TRAIN_COURSE);
                        startActivity(intent);
                        break;
                    case R.id.train_course_tab_ask:
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "18780475970"));
                        startActivity(phoneIntent);
                        break;
                }
            }
        };

        for(RelativeLayout relativeLayout:relativeLayouts){
            relativeLayout.setOnClickListener(listenersss);
        }
    }

    private void initViews() {
        trainSchoolName = (TextView)findViewById(R.id.course_status_train_school_name);
        student = (TextView)findViewById(R.id.course_status_student);
        className = (TextView)findViewById(R.id.course_status_class);
        schoolName = (TextView)findViewById(R.id.course_status_education);
        openTime = (TextView)findViewById(R.id.course_status_open_time);
        iv_school = (ImageView)findViewById(R.id.course_status_iv);

        trainSchoolName.setText("学校:"+sso.getGroup_name());
        student.setText("学生:"+sso.getStudent_name());
        schoolName.setText(""+sso.getCourse_title());
        className.setText("班级:"+sso.getClass_name());
        if(sso.getPlandate() != null){
            String text = "<font color='#ff4966'>"+"近期上课:"+sso.getPlandate()+"</font>";
            openTime.setText(Html.fromHtml(text));
        }else{
            openTime.setText("时间暂定");
        }
        ImageLoaderUtil.displayMyImage(sso.getLogo(), iv_school);



        tv_coll = (TextView)findViewById(R.id.textview_1_1);
        iv_coll = (ImageView)findViewById(R.id.imageView_1_1);

=======
        initViews();
    }

    private void initViews() {
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

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
<<<<<<< HEAD
                        fragments [position] = new MineCourseFragment();
                    }else if(position == 1){
                        fragments [position] = new CourseDetailIntroduceFragment();
=======
                        fragments[position] = new MineCourseFragment();
                    }else if(position == 1){
                        fragments [position] = new SimpleIntroduceFragment();
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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
<<<<<<< HEAD


    private void store() {
        dialog = new HintInfoDialog(MineCourseDetailActivity.this, "收藏中，请稍后...");
        dialog.show();
        UserRequest.store(MineCourseDetailActivity.this, sso.getCourse_title(), 82, sso.getUuid(), "", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
//                Utils.showToast(MineCourseDetailActivity.this, "收藏成功");
                store1();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(MineCourseDetailActivity.this, message);
                }
                dialog.dismiss();
            }
        });
    }

    private void store1() {
        Drawable drawable = getResources().getDrawable(R.drawable.store2);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
//        tvStore.setCompoundDrawables(null, drawable, null, null);
        iv_coll.setImageDrawable(drawable);
        tv_coll.setText("已收藏");
        tv_coll.setTextColor(getResources().getColor(R.color.title_bg));
        oscs.setIsFavor(false);
    }

    private void store2() {
        Drawable drawable = getResources().getDrawable(R.drawable.store1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
//        tvStore.setCompoundDrawables(null, drawable, null, null);
        iv_coll.setImageDrawable(drawable);
        tv_coll.setText("收藏");
        tv_coll.setTextColor(getResources().getColor(R.color.color_929292));
        oscs.setIsFavor(true);
    }

    private void cancelStore() {
        dialog = new HintInfoDialog(MineCourseDetailActivity.this, "取消收藏中，请稍后...");
        dialog.show();
        UserRequest.cancelStore(true, MineCourseDetailActivity.this, sso.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(MineCourseDetailActivity.this, "取消收藏成功");
                store2();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(MineCourseDetailActivity.this, message);
                }
                dialog.dismiss();
            }
        });
    }
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
}
