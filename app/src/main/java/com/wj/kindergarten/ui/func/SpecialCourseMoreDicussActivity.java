package com.wj.kindergarten.ui.func;


import android.widget.LinearLayout;

import com.wj.kindergarten.ui.BaseActivity;

public class SpecialCourseMoreDicussActivity extends BaseActivity{
    private LinearLayout ll;

    @Override
    protected void setContentLayout() {

//        layoutId = R.layout.activity_special_course_more_dicuss;
    }

    @Override
    protected void setNeedLoading() {

        isNeedLoading = true;
    }

    @Override
    protected void onCreate() {

//        ll = (LinearLayout)findViewById(R.id.special_dicuss_more_send);
//        ViewEmot2 viewEmot2 = new ViewEmot2(this, new SendMessage() {
//            @Override
//            public void send(String message) {
//                //点击发送消息出去并且更新
//            }
//        });
//        ll.addView(viewEmot2);
    }
}
