package com.wj.kindergarten.ui.other;

import com.umeng.message.IUmengRegisterCallback;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Utils;

/**
 * Created by tangt on 2016/4/15.
 */
public class CustomIUmengRegisterCallback implements IUmengRegisterCallback {
    @Override
    public void onRegistered(String registrationId) {
        if(!Utils.stringIsNull(registrationId)){
            CGSharedPreference.saveDeviceId(registrationId);
            if (CGSharedPreference.getNoticeState(1)) {
                UserRequest.deviceSave(CGApplication.getInstance(), 0);//注册设备
            } else {
                UserRequest.deviceSave(CGApplication.getInstance(), 2);//注册设备
            }
        }
        CGLog.i("device token = " + registrationId);
    }
}
