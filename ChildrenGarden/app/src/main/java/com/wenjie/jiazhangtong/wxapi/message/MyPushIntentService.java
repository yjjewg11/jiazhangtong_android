package com.wenjie.jiazhangtong.wxapi.message;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.umeng.common.message.Log;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Utils;

/**
 * Developer defined push intent service.
 * Remember to call {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}.
 *
 * @author lucas
 */
public class MyPushIntentService extends UmengBaseIntentService {
    private static final String TAG = MyPushIntentService.class.getName();

    @Override
    protected void onMessage(Context context, Intent intent) {
        super.onMessage(context, intent);
        try {
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            CGLog.d("msg: " + msg.toString());
            Utils.showToast(CGApplication.getInstance(),"成功");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
