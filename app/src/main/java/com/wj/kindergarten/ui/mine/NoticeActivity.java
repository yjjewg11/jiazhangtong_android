package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.widget.CompoundButton;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;

/**
 * NoticeActivity
 *
 * @Description:推送通知
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-14 14:50
 */
public class NoticeActivity extends BaseActivity {
    private CheckSwitchButton switchButton1 = null;
    private CheckSwitchButton switchButton2 = null;
    private CheckSwitchButton switchButton3 = null;

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
        init();
    }

    private void init() {
        switchButton1 = (CheckSwitchButton) findViewById(R.id.switch_button_1);
        switchButton2 = (CheckSwitchButton) findViewById(R.id.switch_button_2);
        switchButton3 = (CheckSwitchButton) findViewById(R.id.switch_button_3);
        switchButton1.setChecked(CGSharedPreference.getNoticeState(1));
        switchButton2.setChecked(CGSharedPreference.getNoticeState(2));
        switchButton3.setChecked(CGSharedPreference.getNoticeState(3));

        if (!CGSharedPreference.getNoticeState(4)) {
            switchButton1.setChecked(true);
            switchButton2.setChecked(true);
            switchButton3.setChecked(true);
            CGSharedPreference.saveNoticeState(1, true);
            CGSharedPreference.saveNoticeState(2, true);
            CGSharedPreference.saveNoticeState(3, true);
            CGSharedPreference.saveNoticeState(4, true);
        } else {
            if (CGSharedPreference.getNoticeState(1)) {
                switchButton1.setChecked(true);
            } else {
                switchButton1.setChecked(false);
            }

            if (CGSharedPreference.getNoticeState(2)) {
                switchButton2.setChecked(true);
            } else {
                switchButton2.setChecked(false);
            }

            if (CGSharedPreference.getNoticeState(3)) {
                switchButton3.setChecked(true);
            } else {
                switchButton3.setChecked(false);
            }
        }

        switchButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CGSharedPreference.saveNoticeState(1, isChecked);
                if (isChecked) {
                    UserRequest.deviceSave(NoticeActivity.this, 0);//注册设备
                } else {
                    UserRequest.deviceSave(NoticeActivity.this, 2);//注册设备
                }
            }
        });

        switchButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CGSharedPreference.saveNoticeState(2, isChecked);
            }
        });

        switchButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CGSharedPreference.saveNoticeState(3, isChecked);
            }
        });
    }
}
