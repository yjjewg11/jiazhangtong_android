package com.wj.kindergarten.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.wj.kindergarten.utils.GloablUtils;

import java.io.IOException;

/**
 * Created by tangt on 2016/2/18.
 */
public class PlayMusicService extends Service {
    private static final int QUIT_LOOP = 101;
    public Handler sunHandler;
    private StopReceiver receiver;
    private Thread thread = new Thread(){

        @Override
        public void run() {
            Looper.prepare();
            sunHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case HANDLE_MESSAGE:
                            String path = (String) msg.obj;
                            int status = msg.arg1;
                            mediaPlayer.reset();

                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                            try {
                                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(path));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            switch (status){
                                case 0 :
                                    //开始播放
                                    try {
                                        mediaPlayer.prepare();
                                        mediaPlayer.start();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 1 :
                                    //暂停播放
                                    if(mediaPlayer.isPlaying()){
                                        mediaPlayer.pause();
                                    }
                                    break;
                                case 2 :
                                    //停止播放
                                    mediaPlayer.stop();
                                    break;
                            }
                            break;
                        case QUIT_LOOP:
                            Looper.myLooper().quit();
                            break;
                    }
                }
            };
            Looper.loop();

        }
    };

    public PlayMusicService() {
        thread.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        register();
        mediaPlayer = new MediaPlayer();
    }

    private void register() {
        receiver = new StopReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GloablUtils.STOP_MUSIC_PLAY);
        registerReceiver(receiver,intentFilter);
    }

    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    final int HANDLE_MESSAGE = 100;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String musicPath = intent.getStringExtra("musicPath");
        int status = intent.getIntExtra("status", -1);
        //如果正在播放则停止播放
        Message message = new Message();
        message.obj = musicPath;
        message.arg1 = status;
        message.what = HANDLE_MESSAGE;
        sunHandler.sendMessage(message);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    class StopReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            sunHandler.sendEmptyMessage(QUIT_LOOP);
            stopSelf();
        }
    }
}
