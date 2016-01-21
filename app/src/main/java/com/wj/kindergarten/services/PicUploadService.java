package com.wj.kindergarten.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.net.upload.Result;
import com.wj.kindergarten.net.upload.UploadFile;
import com.wj.kindergarten.net.upload.UploadImage;
import com.wj.kindergarten.ui.mine.photofamilypic.UpLoadActivity;
import com.wj.kindergarten.utils.CGLog;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by tangt on 2016/1/20.
 */
public class PicUploadService extends Service {
    private UploadFile uploadFile;
    private FinalDb db;

    int count = 0;
    int size = 0;
    private UploadImage uploadImage =  new UploadImage() {
        @Override
        public void success(Result result) {
            //直接把地址写入到数据库
            db.save(new AlreadySavePath(list.get(count)));
            if(judgeIsStart()){
                Intent intent = new Intent(getApplicationContext(),UpLoadActivity.class);
                intent.putExtra("up_list",list);
                intent.putExtra("up_pic_now",list.get(count));
                getApplicationContext().startActivity(intent);
            }
            count ++;
            if(count >= size) return;
            uploadFile.upLoadPf(list.get(count));
        }

        @Override
        public void failure(String message) {

        }
    };
    private ArrayList<String> list;


    @Override
    public void onCreate() {
        super.onCreate();
        db = FinalDb.create(getApplicationContext());
        uploadFile = new UploadFile(getApplicationContext(),uploadImage, 0, 720, 1280);
    }

    public void destory(){
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CGLog.v("intentService线程已被销毁");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        count = 0;
        list = (ArrayList) intent.getSerializableExtra("up_list");
        if(list != null && list.size() > 0){
            uploadFile.upLoadPf(list.get(0));
            size = list.size();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    String loadName = "com.wj.kindergarten.ui.mine.photofamilypic.UpLoadActivity";
    private boolean judgeIsStart(){
        ActivityManager am =(ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo task = tasks.get(0);
        if (task != null) {
            return TextUtils.equals(task.topActivity.getPackageName(), getPackageName()) && TextUtils.equals(task.topActivity.getClassName(), loadName);
        }
        return false;
    }
}
