package com.wenjie.jiazhangtong.wxapi.message;

import android.app.Activity;
import android.os.Bundle;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.ui.BaseActivity;

/**
 * MessageActivity
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-20 16:27
 */
public class MessageActivity extends BaseActivity {
    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_test;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        GlobalHandler.getHandler().sendEmptyMessage(1);
        finish();
    }

}
