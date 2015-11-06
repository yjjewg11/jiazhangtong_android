package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.wj.kindergarten.bean.CallTransfer;
import com.wj.kindergarten.bean.SchoolDetail;
import com.wj.kindergarten.bean.SchoolDetailList;
import com.wj.kindergarten.bean.TrainSchoolInfo;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.more.CallUtils;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.ui.specialcourse.ClassFragment;
import com.wj.kindergarten.ui.specialcourse.SimpleIntroduceFragment;
import com.wj.kindergarten.ui.specialcourse.TeachersSpecialFragment;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

public class SchoolDetailInfoActivity extends BaseActivity{
    private ViewPager viewPager;
    private RelativeLayout[] relatives;
    private RadioGroup radioGroup;
    private RadioButton bt_course,tab_teacher,tab_introduce;
    private RadioButton[] radioButtons;

    private TrainSchoolInfo school;
    private ImageView iv_head;
    private RatingBarView rating_bar;
    private TextView adress,class_name,distance,edcucation,spend_people;
    private TextView tv_coll;
    private ImageView iv_coll;
    private HintInfoDialog dialog;
    private SchoolDetailList detailList;
    private SchoolDetail schoolDetail;

    public SchoolDetail getSchoolDetail() {
        return schoolDetail;
    }

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_schllo_detail_info;
    }


    public TrainSchoolInfo getSchool() {
        return school;
    }

    @Override
    protected void setNeedLoading() {
        Intent intent = getIntent();
        school = (TrainSchoolInfo) intent.getSerializableExtra("school");
        isNeedLoading = true;
    }

    private Fragment classInfo,teachers,introduce;

    private Fragment [] fragments = new Fragment[]{
         classInfo,teachers,introduce
    };
    @Override
    protected void onCreate() {

        titleCenterTextView.setText("学校详情");
        radioGroup = (RadioGroup) findViewById(R.id.special_radio_group);
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
                switch (position) {
                    case 0:
                        id = R.id.tab_course;
                        break;
                    case 1:
                        id = R.id.tab_teacher;
                        break;
                    case 2:
                        id = R.id.tab_introduce;
                        break;
                }
                radioGroup.check(id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void loadData() {

        //获取学校详情
        UserRequest.getTrainSchoolDetail(this, school.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
               detailList = (SchoolDetailList) domain;
                if(detailList != null){
                   schoolDetail =  detailList.getData();
                }
                loadSuc();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void setViews() {
        iv_head = (ImageView) findViewById(R.id.item_special_course_list_view_image_view);
        rating_bar = (RatingBarView) findViewById(R.id.item_special_course_list_view__rating_bar);
        adress = (TextView) findViewById(R.id.item_special_course_list_view_tv_adresss);
        class_name = (TextView) findViewById(R.id.item_class_name);
        distance = (TextView) findViewById(R.id.item_special_course_list_view_tv_distance);
        edcucation = (TextView) findViewById(R.id.item_special_course_list_view_tv_edcucation);
        spend_people = (TextView) findViewById(R.id.item_special_course_list_view_tv_study_people);
        tv_coll = (TextView)findViewById(R.id.textview_1_1);
        iv_coll = (ImageView)findViewById(R.id.imageView_1_1);

        distance.setVisibility(View.GONE);
        ImageLoaderUtil.displayMyImage(school.getImg(), iv_head);
        rating_bar.setFloatStar(schoolDetail.getCt_stars(), true);
        adress.setText("地点:" + school.getAddress());
        class_name.setText("" +  school.getBrand_name());
        edcucation.setText("" + Utils.isNull(school.getSummary()) );
        String text = "<font color='#ff4966'>"+school.getCt_study_students()+"</font>"+"人感兴趣";
        spend_people.setText(Html.fromHtml(text));
        //TODO
        if(!detailList.isFavor()){
            tv_coll.setText("已收藏");
            iv_coll.setImageResource(R.drawable.store2);
        }else{
            tv_coll.setText("收藏");
            iv_coll.setImageResource(R.drawable.store1);
        }


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
                            if(tv_coll.getText().toString().equals("收藏")){
                                store();
                            }else{
                                cancelStore();
                            }
                            break;
                        case R.id.train_course_tab_share:
                            String content = schoolDetail.getBrand_name();
                            ShareUtils.showShareDialog(SchoolDetailInfoActivity.this, v, schoolDetail.getBrand_name(),content, schoolDetail.getImg(),detailList.getShare_url(), false);
                            break;
                        case R.id.train_course_tab_interaction:
                            Intent intent = new Intent(SchoolDetailInfoActivity.this,CourseInteractionListActivity.class);
                            intent.putExtra("newsuuid",school.getUuid());
                            intent.putExtra("type",NormalReplyListActivity.TRAIN_SCHOOL);
                            startActivity(intent);
                            break;
                        case R.id.train_course_tab_ask:
                            CallUtils.showCall(SchoolDetailInfoActivity.this,schoolDetail.getLink_tel().split(","),new CallTransfer(school.getUuid(),81));
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


    private void store() {
        dialog = new HintInfoDialog(SchoolDetailInfoActivity.this, "收藏中，请稍后...");
        dialog.show();
        UserRequest.store(SchoolDetailInfoActivity.this, school.getBrand_name(), 81, school.getUuid(), "", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(SchoolDetailInfoActivity.this, "收藏成功");
                store1();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(SchoolDetailInfoActivity.this, message);
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
        detailList.setIsFavor(false);
    }

    private void store2() {
        Drawable drawable = getResources().getDrawable(R.drawable.store1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
//        tvStore.setCompoundDrawables(null, drawable, null, null);
        iv_coll.setImageDrawable(drawable);
        tv_coll.setText("收藏");
        tv_coll.setTextColor(getResources().getColor(R.color.color_929292));
        detailList.setIsFavor(true);
    }

    private void cancelStore() {
        dialog = new HintInfoDialog(SchoolDetailInfoActivity.this, "取消收藏中，请稍后...");
        dialog.show();
        UserRequest.cancelStore(true, SchoolDetailInfoActivity.this, school.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(SchoolDetailInfoActivity.this, "取消收藏成功");
                store2();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(SchoolDetailInfoActivity.this, message);
                }
                dialog.dismiss();
            }
        });
    }

}
