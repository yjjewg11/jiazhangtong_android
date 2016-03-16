package com.wj.kindergarten.services;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.bean.PfResult;
import com.wj.kindergarten.net.upload.ProgressCallBack;
import com.wj.kindergarten.net.upload.Result;
import com.wj.kindergarten.net.upload.UploadFile;
import com.wj.kindergarten.net.upload.UploadImage;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.UpLoadActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.dbupdate.UploadPathDbTwo;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.FinalUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ThreadManager;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tangt on 2016/1/20.
 */
public class PicUploadService extends Service {


    private ArrayList<AlreadySavePath> listObject;
    int successCount = 0;

    public PicUploadService() {
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    private UploadFile uploadFile;
    private String failedPath;
    private FinalDb db;

    int count = 0;
    int size = 0;

    private ProgressCallBack progressCallBack = new ProgressCallBack() {
        @Override
        public void progress(int current, int total) {
            CGLog.v("打印回调 ： "+current+"/ "+total);
            sendBroad(UpLoadActivity.PF_UPDATE_PROGRESS_LOADING, current, total);
        }
    };

    private void successes(final String data_id) {
        ThreadManager.instance.excuteRunnable(new Runnable() {
            @Override
            public synchronized void run() {
                size = listObject.size();
                AlreadySavePath alreadySavePath = listObject.get(count);
                alreadySavePath.setStatus(0);
                alreadySavePath.setSuccess_time(new Date());
                alreadySavePath.setData_id(data_id);
                db.update(alreadySavePath);
                mHandler.post(new SuccessRunable());
            }
        });
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
            String data_id = null;
            PfResult pfResult = (PfResult) result;
            if(pfResult != null) {
                data_id = pfResult.getData_id();
            }
            successes(data_id);
        }
        @Override
        public void failure(String message) {
            CGLog.v("上传失败 : " + message);
            ThreadManager.instance.excuteRunnable(new Runnable() {
                @Override
                public synchronized void run() {
                    AlreadySavePath alreadySavePath = listObject.get(count);
                    sendBroad(UpLoadActivity.PF_UPDATE_PROGRESS_FAILED, 0, 0);
                    alreadySavePath.setStatus(3);
                    db.update(alreadySavePath);
                    mHandler.post(new FailRunnable());
                }
            });
        }
    };

    private void checkAlreadyUpload(String localPath, ProgressCallBack progressCallBack) {
        //检查数据库是否有此数据，否则不上传
        uploadFile.upLoadPf(localPath, progressCallBack);
    }

    private boolean isTransMission;


    boolean isNeedCirculate = true;

    @Override
    public void onCreate() {
        super.onCreate();
        db = FinalUtil.getAlreadyUploadDb(this);
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
        successCount = 0;
        //判断是否是wifi状态

        //判断是否上传
        findPic();
        if(listObject == null || listObject.size() == 0) return super.onStartCommand(intent,flags,startId);

        if(!Utils.isWifi(getApplicationContext())){
            ToastUtils.showDialog(MainActivity.instance, "提示！", "当前处于非WIFI网络，确定上传?", new DialogInterface.OnClickListener() {
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
        if(upLoad){
            count = 0;
            if(listObject != null && listObject.size() > 0){
                size = listObject.size();
                checkAlreadyUpload(listObject.get(0).getLocalPath(), progressCallBack);
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    private void findPic() {
        String sql = "status = '1' or status = '3'";
        listObject = (ArrayList<AlreadySavePath>) db.findAllByWhere(AlreadySavePath.class,sql);
    }


    String loadName = "com.wj.kindergarten.ui.mine.photofamilypic.UpLoadActivity";

    private boolean isTransmission() {
        return isTransMission;
    }

    public class TransportBinder extends Binder {

        public void reStartUpload(){
            findPic();
            if(listObject != null && listObject.size() > 0)
            checkAlreadyUpload(listObject.get(0).getLocalPath(),progressCallBack);
        }

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

    class SuccessRunable implements Runnable{
            @Override
            public void run() {
                sendBroadcast(new Intent(GloablUtils.ALREADY_UPLOADING));
                sendBroad(UpLoadActivity.PF_UPDATE_PROGRESS_SUCCESSED, 100, 100);
                listObject.remove(count);
                if (listObject.size() == 0) {
                    sendBroadcast(new Intent(GloablUtils.ALREADY_UPLOADING_FINISHED));
                    judgeUpdateData();
                    return;
                }
                checkAlreadyUpload(listObject.get(count).getLocalPath(), progressCallBack);
            }

        private void judgeUpdateData() {
            findPic();
            if(listObject == null || listObject.size() == 0){
                sendBroadcast(new Intent(GloablUtils.REQUEST_PIC_NEW_DATA));
            }
        }
    }

    class FailRunnable implements Runnable{
            @Override
            public void run() {
                listObject.remove(count);
                if(listObject.size() == 0) return;
                checkAlreadyUpload(listObject.get(count).getLocalPath(), progressCallBack);
            }
    }

}
