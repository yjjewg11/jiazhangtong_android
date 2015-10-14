package com.wj.kindergarten.ui.func;


import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.OnceSpecialCourse;
import com.wj.kindergarten.bean.OnceSpecialCourseList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

//特长课程详细信息页面
public class SpecialCourseInfoActivity extends BaseActivity {
    private Button bt;

    private List<OnceSpecialCourse> list = new ArrayList<>();
    private Button bt_more;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_special_course_info;

    }

    @Override
    protected void setNeedLoading() {

        isNeedLoading  = true;
    }

    @Override
    protected void onCreate() {


        titleCenterTextView.setText("课程详情");
        bt_more = (Button)findViewById(R.id.start_more_dicuss);
        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialCourseInfoActivity.this,MoreSpecialDiscussActivity.class);
                //放入评论对象
                //TODO
                intent.putExtra("discuss","");
                startActivity(intent);
            }
        });
//        bt = (Button)findViewById(R.id.start_more_dicuss);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SpecialCourseInfoActivity.this,SpecialCourseMoreDicussActivity.class);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    protected void loadData() {
        Intent intent = getIntent();
        String uuid = intent.getStringExtra("uuid");
        UserRequest.getSpecialCourseINfoFromClickItem(this,uuid,new RequestResultI(){
            @Override
            public void result(BaseModel domain) {
                OnceSpecialCourseList ocs = (OnceSpecialCourseList) domain;
                list.add(ocs.getData());
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
}
