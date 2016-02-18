package com.wj.kindergarten.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.GsonKdUtil;
import com.wj.kindergarten.net.upload.ProgressCallBack;
import com.wj.kindergarten.net.upload.Result;
import com.wj.kindergarten.net.upload.UploadFile;
import com.wj.kindergarten.net.upload.UploadImage;
import com.wj.kindergarten.ui.mine.LoginActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.UpLoadActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tangt on 2016/1/20.
 */
public class PicUploadService extends Service {


    private ArrayList<AlreadySavePath> listObject;

    public PicUploadService() {
    }

    private UploadFile uploadFile;
    private String failedPath;
    private FinalDb db;

    int count = 0;
    int size = 0;

    private ProgressCallBack progressCallBack = new ProgressCallBack() {
        @Override
        public void progress(int current, int total) {
            sendBroad(UpLoadActivity.PF_UPDATE_PROGRESS_LOADING,current,total);
        }
    };

    private void successes() {
        AlreadySavePath alreadySavePath = listObject.get(count);
        alreadySavePath.setStatus(0);
        alreadySavePath.setSuccess_time(new Date());
        db.update(alreadySavePath);
        sendBroad(UpLoadActivity.PF_UPDATE_PROGRESS_SUCCESSED,100,100);
        count++;
        if (count >= size) {
            if(listObject != null)listObject.clear();
            return;
        }
        uploadFile.upLoadPf(listObject.get(count).getLocalPath(),progressCallBack);
    }

    private void sendBroad(String action,int progress,int total) {
        if (isTransmission()) {
            Intent intent = new Intent(action);
            intent.putExtra("path",listObject.get(count).getLocalPath());
            intent.putExtra("progress",progress);
            intent.putExtra("total",total);
            sendBroadcast(intent);
        }
    }

    private UploadImage uploadImage = new UploadImage() {
        @Override
        public void success(Result result) {
            //直接把地址写入到数据库
            successes();
        }
        @Override
        public void failure(String message) {
            CGLog.v("上传失败 : " + message);
            AlreadySavePath alreadySavePath = listObject.get(count);
            alreadySavePath.setStatus(3);
            db.update(alreadySavePath);
            count++;
            if(count >= size){
                if(listObject != null)listObject.clear();
                return;
            }
        }
    };

    private boolean isTransMission;


    boolean isNeedCirculate = true;

    @Override
    public void onCreate() {
        super.onCreate();
        db = FinalDb.create(getApplicationContext());
        uploadFile = new UploadFile(getApplicationContext(), uploadImage, 0, 720, 1280);

    }

    public void destory() {
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
        return new TransportBinder();
    }
    boolean upLoad = true;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        upLoad = true;
        //判断是否是wifi状态
        if(!Utils.isWifi(getApplicationContext())){
            ToastUtils.showDialog(getApplicationContext(), "提示！", "当前处于非WIFI网络，确定上传?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    upLoad = false;
                }
            });
        }

        //判断是否上传
        if(upLoad){
            count = 0;
            String sql = "status = '1'";
            listObject = (ArrayList<AlreadySavePath>) db.findAllByWhere(AlreadySavePath.class,sql);
            if(listObject != null && listObject.size() > 0){
                size = listObject.size();
                uploadFile.upLoadPf(listObject.get(0).getLocalPath(), progressCallBack);
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }


    String loadName = "com.wj.kindergarten.ui.mine.photofamilypic.UpLoadActivity";

    private boolean isTransmission() {
        return isTransMission;
    }

    public class TransportBinder extends Binder {
        public void startTransMission() {
            isTransMission = true;
        }

        public void stopTransMission() {
            isTransMission = false;
        }

        public ArrayList<AlreadySavePath> getList() {
            return listObject == null ? null : listObject;
        }

        public void cancleUpLoadSinglePic(AlreadySavePath alreadySavePath) {
            if(listObject.contains(alreadySavePath)){
                listObject.remove(alreadySavePath);
                db.delete(alreadySavePath);
                ToastUtils.showMessage("删除成功!");
            }
        }
    }

}
