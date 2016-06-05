package com.wj.kindergarten.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wj.kindergarten.ui.more.HtmlActivity;

/**
 * Created by tangt on 2015/12/29.
 */
public class ReceiverTwoCodeURl extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getStringExtra("url");
        Intent startActivity = new Intent(context, HtmlActivity.class);
        startActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity.putExtra("center_title","二维码链接");
        startActivity.putExtra("url",url);
        context.startActivity(startActivity);
    }
}
