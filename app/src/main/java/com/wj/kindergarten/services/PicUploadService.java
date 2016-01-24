package com.wj.kindergarten.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.GsonKdUtil;
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tangt on 2016/1/20.
 */
public class PicUploadService extends Service {


    public PicUploadService() {
    }

    private UploadFile uploadFile;
    private String failedPath;
    private FinalDb db;
    private ArrayList<String> list = new ArrayList<>();

    int count = 0;
    int size = 0;
    private AjaxCallBack ajaxCallBack = new AjaxCallBack() {
        @Override
        public void onStart() {
            super.onStart();
            CGLog.v("上传开始!");
            sendBroad(UpLoadActivity.PF_UPDATE_PROGRESS_START, 10);
        }

        @Override
        public void onLoading(long count, long current) {
            super.onLoading(count, current);
            sendBroad(UpLoadActivity.PF_UPDATE_PROGRESS_LOADING, 60);
            CGLog.v("查看上传进度 : "+current+"/"+count);
        }

        @Override
        public void onSuccess(Object o) {
            super.onSuccess(o);
            CGLog.v("上传成功 : " + o.toString());
            BaseModel baseModel = GsonKdUtil.getGson().fromJson(o.toString(), BaseModel.class);
            if(baseModel.getResMsg().getStatus().equals("sessionTimeout")){
                ToastUtils.showMessage(baseModel.getResMsg().getMessage());
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);

                sendBroad(UpLoadActivity.PF_UPDATE_PROGRESS_FAILED,0);
                list.clear();
                return;
            }
            success();
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            super.onFailure(t, errorNo, strMsg);
            CGLog.v("上传失败 : "+strMsg);
        }
    };
    private void success() {
        db.save(new AlreadySavePath(list.get(count)));
        sendBroad(UpLoadActivity.PF_UPDATE_PROGRESS_SUCCESSED,100);
        count++;
        if (count >= size) {
            list.clear();
            return;
        }
        uploadFile.upLoadPf(list.get(count),ajaxCallBack);
    }

    private void sendBroad(String action,int progress) {
        if (isTransmission()) {
            Intent intent = new Intent(action);
            intent.putExtra("path",list.get(count));
            sendBroadcast(intent);
        }
    }

    private UploadImage uploadImage = new UploadImage() {
        @Override
        public void success(Result result) {
            //直接把地址写入到数据库

        }



        @Override
        public void failure(String message) {
            if (!TextUtils.isEmpty(failedPath) && !failedPath.equals(list.get(count))) {
                failedPath = list.get(count);
            } else {
                count++;
            }
            uploadFile.upLoadPf(list.get(count),ajaxCallBack);
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        count = 0;
        ArrayList arrayList = (ArrayList) intent.getSerializableExtra("up_list");
        if (arrayList != null && arrayList.size() > 0) {
            size = arrayList.size();
            list.addAll(arrayList);
            uploadFile.upLoadPf(list.get(0),ajaxCallBack);
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

        public ArrayList<String> getList() {
            return list == null ? null : list;
        }

        public void cancleUpLoadSinglePic(String path) {
            if(list.contains(path)){
                list.remove(path);
                ToastUtils.showMessage("删除成功!");
            }
        }
    }

}
