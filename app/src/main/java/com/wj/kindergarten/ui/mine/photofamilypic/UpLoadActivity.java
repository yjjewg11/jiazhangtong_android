package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.UpLoadProgressAdapter;

import java.util.ArrayList;

/**
 * Created by tangt on 2016/1/20.
 */
public class UpLoadActivity extends BaseActivity {

    public static final String ALL_UP_PIC_LIST_ACTION = "pf_all_up_pic_list";
    public static final String SINGLE_UPDATE_DATA_ACTION = "pf_single_update_data";
    private ArrayList<String> countList = new ArrayList<>();
    private PullToRefreshListView listView;
    private UpLoadProgressAdapter progressAdapter;
    private UpdateBroadCastReceiver receiver;

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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void register() {
        receiver = new UpdateBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ALL_UP_PIC_LIST_ACTION);
        filter.addAction(SINGLE_UPDATE_DATA_ACTION);
        registerReceiver(receiver, filter);
    }

    private void initViews() {
        listView = (PullToRefreshListView)findViewById(R.id.pulltorefresh_list);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        progressAdapter = new UpLoadProgressAdapter(this);
        listView.setAdapter(progressAdapter);

    }

    private void setEmpty() {
        View noView = View.inflate(this,R.layout.nothing_view,null);
        setContentView(noView);
    }


    public class UpdateBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case ALL_UP_PIC_LIST_ACTION :
                    if(countList == null) return;
                    if(progressAdapter == null) return;
                    countList.clear();
                    countList.addAll((ArrayList<String>)intent.getSerializableExtra("up_count"));

                    break;
                case SINGLE_UPDATE_DATA_ACTION :

                    break;
            }
        }


    }
}
