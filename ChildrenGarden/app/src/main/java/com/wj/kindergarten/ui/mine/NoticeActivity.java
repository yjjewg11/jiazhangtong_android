package com.wj.kindergarten.ui.mine;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;

/**
 * NoticeActivity
 *
 * @Description:推送通知
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-14 14:50
 */
public class NoticeActivity extends BaseActivity {
    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_message_notice;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("推送通知");
    }
}
