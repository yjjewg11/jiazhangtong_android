package com.wj.kindergarten.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.adsmogo.adview.AdsMogoLayout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.onlineconfig.OnlineConfigLog;
import com.umeng.onlineconfig.UmengOnlineConfigureListener;
import com.wenjie.jiazhangtong.R;
import com.wenjie.jiazhangtong.wxapi.message.MyPushIntentService;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.mine.LoginActivity;
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
public class SplashActivity extends Activity {
    private static final int SPLASH_DELAY = 0;
    private static final int SLASH_DELAY_TIME = 3 * 1000;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SPLASH_DELAY:
                    if (Utils.isLoginIn() && !CGSharedPreference.getLoginOut()) {
                        String[] str = CGSharedPreference.getLogin();
                        UserRequest.login2(SplashActivity.this, str[0], str[1]);
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        mainIntent.putExtra("from", "splash");
                        startActivity(mainIntent);
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //  AnalyticsConfig.enableEncrypt(true);//
        ActivityManger.getInstance().addActivity(this);

        try {

            OnlineConfigAgent.getInstance().updateOnlineConfig(this);
            OnlineConfigAgent.getInstance().setDebugMode(true);
            UmengOnlineConfigureListener configureListener = new UmengOnlineConfigureListener() {
                @Override
                public void onDataReceived(JSONObject json) {
                    // TODO Auto-generated method stub
                    OnlineConfigLog.d("OnlineConfig", "json=" + json.toString());

                }
            };
            OnlineConfigAgent.getInstance().setOnlineConfigListener(configureListener);


            PushAgent mPushAgent = PushAgent.getInstance(this);
            mPushAgent.setDebugMode(true);
            mPushAgent.onAppStart();
            mPushAgent.enable(mRegisterCallback);
            mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
            String device_token = UmengRegistrar.getRegistrationId(this);
            CGSharedPreference.saveDeviceId(device_token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        com.umeng.socialize.utils.Log.LOG = true;
        mHandler.sendEmptyMessageDelayed(SPLASH_DELAY, SLASH_DELAY_TIME);
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
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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

        AdsMogoLayout.clear();
//        adsMogoLayoutCode.clearThread();
        super.onDestroy();
    }
}

