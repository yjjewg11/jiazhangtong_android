package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.services.PicUploadService;
import com.wj.kindergarten.ui.BaseActivity;

import com.wj.kindergarten.ui.func.adapter.UpLoadAdapter;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.FinalUtil;
import com.wj.kindergarten.utils.ToastUtils;

import net.tsz.afinal.FinalDb;

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
    public static final String PF_UPDATE_PROGRESS_FAILED = "pf_update_progress_failed";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private UpdateBroadCastReceiver receiver;
    private PicUploadService.TransportBinder binder;
    private FrameLayout upload_activity_fl;

    public PicUploadService.TransportBinder getBinder() {
        return binder;
    }

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
    private FinalDb db;
    private PullToRefreshListView pullListView;
    private List<AlreadySavePath> alreadySavePathsList = new ArrayList<>();
    private UpLoadAdapter upAdapter;

    private void bindSuccess() {
        binder.startTransMission();
        if (binder.getList() == null || binder.getList().size() == 0) {
            judgeAddNoContent(binder.getList());
            return;
        }
        setTitleText("上传进度","全部暂停");
        addView();
    }

    private void addView() {
        ArrayList<AlreadySavePath> list = binder.getList();
        Iterator<AlreadySavePath> iterator = list.iterator();
        while (iterator.hasNext()){
            AlreadySavePath alreadySavePath = iterator.next();
            if (alreadySavePath.getStatus() == 0) list.remove(alreadySavePath);
        }
        alreadySavePathsList.clear();
        alreadySavePathsList.addAll(list);
        upAdapter.notifyDataSetChanged();
//        for (final AlreadySavePath alreadySavePath : binder.getList()) {
//            //判断是否已经上传成功，如果是则不添加
//            final AlreadySavePath dbPath = db.findById(alreadySavePath.getId(), AlreadySavePath.class);
//            if(dbPath.getStatus() == 0) continue;
//            final View view = View.inflate(this, R.layout.upload_progress_item, null);
//            ImageView up_load_progress_image = (ImageView) view.findViewById(R.id.up_load_progress_image);
//            ImageView up_Load_wait = (ImageView) view.findViewById(R.id.up_Load_wait);
//            TextView upload_tv_progress = (TextView) view.findViewById(R.id.upload_tv_progress);
//            up_Load_wait.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //检查状态是否为失败，否则不从此开始上传
//                   AlreadySavePath o = db.findById(dbPath.getId(), AlreadySavePath.class);
//                    if(o.getStatus() == 3){
//                          binder.reStartUpload();
//                    }else{
//                        ToastUtils.showMessage("图片正在上传中，请耐心等待一会儿！");
//                    }
//                }
//            });
//            ImageLoaderUtil.displayMyImage("file://" + alreadySavePath.getLocalPath(), up_load_progress_image);
//            view.setTag(alreadySavePath.getLocalPath());
//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//                    return true;
//                }
//            });
//            linearLayout.addView(view);
//        }
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
        initViews();
        initRemoveView();
        setTitleText("上传进度", rightText);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bindService(new Intent(UpLoadActivity.this, PicUploadService.class), connection, BIND_AUTO_CREATE);
            }
        }, 300);

    }

    String rightText  = "全部开始";
    @Override
    protected void titleRightButtonListener() {
        if(rightText.equals("全部开始")){
            rightText = "全部暂停";
            binder.reStartUpload();
            setTitleText("上传进度",rightText);
        } else if (rightText.equals("全部暂停")){
            rightText = "全部开始";
            binder.giveUpLoad();
            setTitleText("上传进度", rightText);
        }

    }

    private void initRemoveView() {
        db = FinalUtil.getAlreadyUploadDb(this);
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
        upload_activity_fl = (FrameLayout)findViewById(R.id.upload_activity_fl);
        pullListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        pullListView.setMode(PullToRefreshBase.Mode.DISABLED);
        upAdapter = new UpLoadAdapter(this,alreadySavePathsList);
        pullListView.setAdapter(upAdapter);
        pullListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlreadySavePath savePath = (AlreadySavePath) upAdapter.getItem(position - 1);
                ToastUtils.showDialog(UpLoadActivity.this, "提示!", "您确定要删除此图片?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alreadySavePathsList.remove(savePath);
                        binder.cancleUpLoadSinglePic(savePath);
                        dialog.cancel();
                        upAdapter.notifyDataSetChanged();
                    }
                });
                return false;
            }
        });
        pullListView.setLayoutAnimation(getAnimationController());

    }
    protected LayoutAnimationController getAnimationController() {
        int duration=300;
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }
//
//    private void setEmpty() {
//        View noView = View.inflate(this, R.layout.nothing_view, null);
//        setContentView(noView);
//    }


    public class UpdateBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String path = intent.getStringExtra("path");
            int progress = intent.getIntExtra("progress", -1);
            int total = intent.getIntExtra("total", -1);
            CGLog.v("打印progress : " + progress + " total : " + total);
            if(total < progress) return;
//            final View view = linearLayout.findViewWithTag(path);
//            if(view == null) return;
//            RoundCornerProgressBar bar = (RoundCornerProgressBar) (view.findViewById(R.id.up_load_progressBar));
//            TextView tv_progress = (TextView) (view.findViewById(R.id.upload_tv_progress));
//            ImageView upload_wait = (ImageView) view.findViewById(R.id.up_Load_wait);
            AlreadySavePath savePath = new AlreadySavePath(path);
            int index = alreadySavePathsList.indexOf(savePath) < 0 ? 0 : alreadySavePathsList.indexOf(savePath);
            if(alreadySavePathsList.size() <= 0){
                judgeAddNoContent(alreadySavePathsList);
                return;
            }
            AlreadySavePath alreadySavePath = alreadySavePathsList.get(index);
            alreadySavePath.setProgress(progress);
            alreadySavePath.setTotal(total);
            int progressUpdate =(int) (Double.valueOf(progress)/Double.valueOf(total) * 100);
            switch (intent.getAction()) {
                case PF_UPDATE_PROGRESS_LOADING:
                    alreadySavePathsList.set(index,alreadySavePath);
                    CGLog.v("上传图片地址及进度更新 ：" + path + " --->" + progressUpdate);
//                    if(upload_wait.getVisibility() == View.VISIBLE){
//                        upload_wait.setVisibility(View.GONE);
//                    }
//                    if(tv_progress.getVisibility() == View.GONE){
//                        tv_progress.setVisibility(View.VISIBLE);
//                    }
//                    bar.setProgress(progressUpdate);
//                    tv_progress.setText("" + progressUpdate + "%");
                    break;
                case PF_UPDATE_PROGRESS_SUCCESSED:
                    alreadySavePathsList.remove(index);
//                    bar.setProgress(100);
//                    tv_progress.setText("" + 100 + "%");
//                    ObjectAnimator animator =
//                            ObjectAnimator.ofFloat(new LinearWrapper(view), "height", 0);
//                    animator.setDuration(150);
//                    animator.addListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//
//                        }
//                    });
//                    animator.start();
//                    linearLayout.removeView(view);
                    judgeAddNoContent( alreadySavePathsList);



                    break;
                case PF_UPDATE_PROGRESS_FAILED:
                    alreadySavePath.setStatus(3);
//                    tv_progress.setVisibility(View.GONE);
//                    upload_wait.setVisibility(View.VISIBLE);
//                    upload_wait.setImageResource(R.drawable.upload_failed);
//                    bar.setProgress(0);
                    judgeAddNoContent(alreadySavePathsList);
                    break;
            }
            upAdapter.notifyDataSetChanged();
        }
    }

    private void judgeAddNoContent(List<AlreadySavePath> list) {
        if(list != null &&list.size() == 0){
            setTitleText("上传进度","");
            noView(upload_activity_fl);
        }
    }
    class LinearWrapper{
        private View view;

        public LinearWrapper(View view) {
            this.view = view;
        }

        public int getHeight() {
            return ((LinearLayout.LayoutParams)view.getLayoutParams()).height;
        }

        public void setHeight(int height) {
            ((LinearLayout.LayoutParams)view.getLayoutParams()).height = height;
            view.invalidate();
        }
    }

}
