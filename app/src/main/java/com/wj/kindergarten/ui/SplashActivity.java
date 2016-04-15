package com.wj.kindergarten.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.adsmogo.adview.AdsMogoLayout;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.onlineconfig.OnlineConfigLog;
import com.umeng.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.update.UmengUpdateAgent;
import com.wenjie.jiazhangtong.R;
import com.wenjie.jiazhangtong.wxapi.message.MyPushIntentService;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.ThreeInfo;
import com.wj.kindergarten.common.CGSharedPreference;

import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.mine.LoginActivity;
import com.wj.kindergarten.ui.more.DoEveryThing;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Utils;
import org.json.JSONObject;



/**
 * SplashActivity
 *
 * @author Wave
 * @data: 2015/5/20
 * @version: v1.0
 */
public class SplashActivity extends Activity implements DoEveryThing {
    private static final int SPLASH_DELAY = 0;
    private  int SLASH_DELAY_TIME = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SPLASH_DELAY:
                    //判断是否存有JESSIONID
                    if (!TextUtils.isEmpty(CGSharedPreference.getStoreJESSIONID())) {
                        String[] str = CGSharedPreference.getLogin();
//                        UserRequest.login2(SplashActivity.this, str[0], str[1]);
                        //有在调到主页面的同时获取用户信息
                        if (CGSharedPreference.getNoticeState(1)) {
                            UserRequest.deviceSave(getApplicationContext(), 0);//注册设备
                        } else {
                            UserRequest.deviceSave(SplashActivity.this, 2);//注册设备
                        }
                        GlobalHandler.getHandler().sendEmptyMessage(1011);
                        UserRequest.getUserInfo(SplashActivity.this,CGSharedPreference.getStoreJESSIONID(),CGSharedPreference.getJESSIONID_MD5());
                        startActivityFromSplash();
                        //判断是否进行过第三方登录
                    }
//                    else if (!Utils.stringIsNull(CGSharedPreference.getAccess_Token())
//                               && !Utils.stringIsNull(CGSharedPreference.getlogin_type())){
//                        UserRequest.getThreeUserInfo(SplashActivity.this,
//                                CGSharedPreference.getAccess_Token(), CGSharedPreference.getlogin_type(),
//                                SplashActivity.this);
//                    }
                    else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };
    private PushAgent mPushAgent;

    private void startActivityFromSplash() {
        Intent mainIntent = new Intent(SplashActivity.this, GuideActivity.class);
        mainIntent.putExtra("from", "splash");
        startActivity(mainIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActivityManger.getInstance().addActivity(this);

        try {
            OnlineConfigAgent.getInstance().updateOnlineConfig(getApplicationContext());
            OnlineConfigAgent.getInstance().setDebugMode(true);

            mPushAgent = PushAgent.getInstance(getApplicationContext());
            mPushAgent.setDebugMode(true);
            mPushAgent.onAppStart();
            mPushAgent.enable(mRegisterCallback);
            mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
            String device_token = UmengRegistrar.getRegistrationId(getApplicationContext());
            CGSharedPreference.saveDeviceId(device_token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        com.umeng.socialize.utils.Log.LOG = true;
        int delay = getIntent().getIntExtra("delay",3000);
        mHandler.sendEmptyMessageDelayed(SPLASH_DELAY, delay);
    }


    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            CGSharedPreference.saveDeviceId(registrationId);
            CGLog.d("device token = " + registrationId);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(getApplicationContext());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeMessages(SPLASH_DELAY);
        }
        mPushAgent.disable();
        mPushAgent = null;
        OnlineConfigAgent.getInstance().removeOnlineConfigListener();
        AdsMogoLayout.clear();
//        adsMogoLayoutCode.clearThread();
        super.onDestroy();
    }

    @Override
    public void everyThing() {
        startActivityFromSplash();
    }
}

