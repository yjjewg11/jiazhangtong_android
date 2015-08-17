package com.wj.kindergarten.ui;

import android.view.View;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.utils.ShareUtils;

/**
 * TestActivity
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-14 17:59
 */
public class TestActivity extends BaseActivity {
    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_test;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("test");
        final TextView test = (TextView) findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.showShareDialog(TestActivity.this, test, "test", "test", "", "http://bbs.umeng.com/thread-646-1-1.html");
            }
        });
    }
}
