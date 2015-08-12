package com.wj.kindergarten.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.mine.LoginActivity;
import com.wj.kindergarten.utils.Utils;


/**
 * SplashActivity
 *
 * @author Wave
 * @data: 2015/5/20
 * @version: v1.0
 */
public class SplashActivity extends Activity {
    private static final int SPLASH_DELAY = 0;
    private static final int SLASH_DELAY_TIME = 0 * 1000;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SPLASH_DELAY:
                    if (Utils.isLoginIn()) {
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
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "api_key");
        mHandler.sendEmptyMessageDelayed(SPLASH_DELAY, SLASH_DELAY_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeMessages(SPLASH_DELAY);
        }
        super.onDestroy();
    }
}
