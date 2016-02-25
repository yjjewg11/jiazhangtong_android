package com.wj.kindergarten.ui.func;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfMusic;
import com.wj.kindergarten.bean.PfMusicSunObject;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.ShowMusicAdapter;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/21.
 */
public class FindMusicOfPfActivity extends BaseActivity {
    private PullToRefreshListView listView;
    private ShowMusicAdapter adapter;
    List<PfMusicSunObject> pfMusics = new ArrayList<>();
    private static final int GET_DATA_SUCCESSED = 3006;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESSED:
                    adapter.setList(pfMusics);
                    break;
            }
        }
    };


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.find_music_of_pf;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void titleLeftButtonListener() {
        super.titleLeftButtonListener();
        stopMusic();
    }

    @Override
    protected void onCreate() {
        setTitleText("选择背景音乐", "确认");
        initViews();
        initClick();

        loadMusic();

    }

    private void initClick() {

    }

    int pageNo = 1;
    private void loadMusic() {
        UserRequest.getModeMusic(this,pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                PfMusic pfMusic = (PfMusic) domain;
                if(pfMusic != null && pfMusic.getList() != null
                        && pfMusic.getList().getData() != null && pfMusic.getList().getData().size() > 0){
                    pfMusics.addAll(pfMusic.getList().getData());
                    handler.sendEmptyMessage(GET_DATA_SUCCESSED);
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    @Override
    protected void titleRightButtonListener() {
        if(adapter.getList() == null) return;
        if(adapter.getList().size() == 0){
            ToastUtils.showMessage("选首歌吧!");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("object", adapter.getList().get(0));
        setResult(RESULT_OK, intent);
        stopMusic();
        finish();
    }

    private void stopMusic() {
        sendBroadcast(new Intent(GloablUtils.STOP_MUSIC_PLAY));
    }

    @Override
    public void onBackPressed() {
        stopMusic();
        super.onBackPressed();
    }

    private void initViews() {
        listView = (PullToRefreshListView)findViewById(R.id.pulltorefresh_list);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        adapter = new ShowMusicAdapter(this);
        listView.setAdapter(adapter);
    }


}
