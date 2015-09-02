package com.wenjie.jiazhangtong.wxapi.message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.umeng.common.message.Log;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.utils.CGLog;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

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
            CGLog.e("title: " + msg.title + " text: " + msg.text);
            UTrack.getInstance(context).trackMsgClick(msg);  //应该是在点击通知了之后调用  用于反馈友盟已经点击了通知
            if (null != msg) {
                showNotification(msg);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void showNotification(UMessage message) {
        CGLog.e("showNotification notice state sp->" + CGSharedPreference.getNoticeState(1));
        if (null == message || !CGSharedPreference.getNoticeState(1)) {
            CGLog.e("message is null or notice state false");
            return;
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(message.title)
                        .setContentText(message.text)
                        .setContentInfo("")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true);
        PendingIntent resultPendingIntent = null;

        if (CGSharedPreference.getNoticeState(2) && CGSharedPreference.getNoticeState(3)) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        } else if (CGSharedPreference.getNoticeState(2)) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        } else if (CGSharedPreference.getNoticeState(3)) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        }

        if (null != message) {
            Intent resultIntent = new Intent(this, MainActivity.class);
            resultIntent.putExtra("from", "Push");
            resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify((int) (Math.random() * 1000), mBuilder.build());
        CGLog.e("show notify");
    }

}
