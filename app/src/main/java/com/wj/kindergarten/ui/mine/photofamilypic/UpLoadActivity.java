package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.PfProgressItem;
import com.wj.kindergarten.services.PicUploadService;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.UpLoadProgressAdapter;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tangt on 2016/1/20.
 */
public class UpLoadActivity extends BaseActivity {

    public static final String PF_UPDATE_PROGRESS_START = "pf_update_progress_start";
    public static final String PF_UPDATE_PROGRESS_LOADING = "pf_update_progress_loading";
    public static final String PF_UPDATE_PROGRESS_SUCCESSED = "pf_update_progress_successed";
    public static final String PF_UPDATE_PROGRESS_FAILED= "pf_update_progress_failed";

    private UpdateBroadCastReceiver receiver;
    private PicUploadService.TransportBinder binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (PicUploadService.TransportBinder) service;
            bindSuccess();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private LinearLayout linearLayout;
    private ImageView up_Load_wait;

    private void bindSuccess() {
        binder.startTransMission();
        if (binder.getList() == null || binder.getList().size() == 0){
            judgeAddNoContent();
            return;
        }
        addView();
    }

    private void addView() {
        linearLayout.removeAllViews();
        for (final String path : binder.getList()) {
            final View view = View.inflate(this, R.layout.upload_progress_item, null);
            ImageView up_load_progress_image = (ImageView) view.findViewById(R.id.up_load_progress_image);
            ProgressBar up_load_progressBar = (ProgressBar) view.findViewById(R.id.up_load_progressBar);
            up_Load_wait = (ImageView) view.findViewById(R.id.up_Load_wait);
            ImageLoaderUtil.displayMyImage("file://"+path,up_load_progress_image);
            view.setTag(path);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ToastUtils.showDialog(UpLoadActivity.this, "提示!", "您确定要删除此图片?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            binder.cancleUpLoadSinglePic(path);
                            linearLayout.removeView(view);
                            dialog.cancel();
                        }
                    });
                    return true;
                }
            });
            linearLayout.addView(view);
        }
    }

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.up_load_activity;
    }


    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {

        register();
        setTitleText("上传进度");
        initViews();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binder.stopTransMission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        binder.stopTransMission();
        unbindService(connection);
    }

    private void register() {
        receiver = new UpdateBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PF_UPDATE_PROGRESS_START);
        filter.addAction(PF_UPDATE_PROGRESS_LOADING);
        filter.addAction(PF_UPDATE_PROGRESS_SUCCESSED);
        filter.addAction(PF_UPDATE_PROGRESS_FAILED);
        registerReceiver(receiver, filter);
    }

    private void initViews() {
        bindService(new Intent(this, PicUploadService.class), connection, BIND_AUTO_CREATE);
        linearLayout = (LinearLayout) findViewById(R.id.pf_scroll_progress_linear);

    }

    private void setEmpty() {
        View noView = View.inflate(this, R.layout.nothing_view, null);
        setContentView(noView);
    }



    public class UpdateBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String path = intent.getStringExtra("path");
            switch (intent.getAction()) {
                case PF_UPDATE_PROGRESS_START:
                    int index =  linearLayout.indexOfChild(linearLayout.findViewWithTag(path));
                    linearLayout.removeViews(0,index);
                    ((ProgressBar)
                            (linearLayout.findViewWithTag(path).findViewById(R.id.up_load_progressBar))).setProgress(95);
                    break;
                case PF_UPDATE_PROGRESS_LOADING:
                    ((ProgressBar)
                            (linearLayout.findViewWithTag(path).findViewById(R.id.up_load_progressBar))).setProgress(99);
                    break;
                case PF_UPDATE_PROGRESS_SUCCESSED:
                     linearLayout.removeView(linearLayout.findViewWithTag(path));
                     judgeAddNoContent();
                    break;
                case PF_UPDATE_PROGRESS_FAILED :
                    linearLayout.removeAllViews();
                    judgeAddNoContent();
                    break;
            }
        }
    }

    private void judgeAddNoContent() {
        if(linearLayout.getChildCount() == 0){
            setContentView(View.inflate(this,R.layout.nothing_view,null));
//            TextView textView = new TextView(this);
//            textView.setText("暂时还没有上传内容!");
//            textView.setGravity(Gravity.CENTER);
//            linearLayout.addView(textView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

}
