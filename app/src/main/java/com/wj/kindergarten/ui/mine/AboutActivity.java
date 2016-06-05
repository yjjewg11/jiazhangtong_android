package com.wj.kindergarten.ui.mine;

import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.Utils;

/**
 * AboutActivity
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-13 23:02
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_about;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("关于我们");
        TextView textView = (TextView) findViewById(R.id.tv_version);
        textView.setText("V"+Utils.getVersion(AboutActivity.this));
    }
}
