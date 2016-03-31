package com.wj.kindergarten.ui.mine.photofamilypic;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.BoutiqueSingleShowAdapter;
import com.wj.kindergarten.utils.GloablUtils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

public class BoutiqueSingleChooseActivity extends BaseActivity {

    private static final int GET_DATA = 1022;
    private String movie_uuid;
    private GridView gridView;
    private BoutiqueSingleShowAdapter adapter;
    private List<AllPfAlbumSunObject> list = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA:
                    List<AllPfAlbumSunObject> objects = db.findAll(AllPfAlbumSunObject.class);
                    if(objects != null){
                        list.addAll(objects);
                        adapter.setList(list);
                    }
                    break;
            }
        }
    };
    private FinalDb db;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_boutique_single_choose;
    }

    @Override
    protected void setNeedLoading() {
        getData();
    }

    @Override
    protected void onCreate() {
        setTitleText("选择照片");
        db = FinalDb.create(this, GloablUtils.FAMILY_UUID_OBJECT,true);
        initViews();
        loadData();
    }

    private void getData() {
        movie_uuid = getIntent().getStringExtra("movie_uuid");
    }

    private void initViews() {
        gridView = (GridView) findViewById(R.id.boutique_sigle_choose_grid);
        adapter = new BoutiqueSingleShowAdapter(this);
        gridView.setAdapter(adapter);
        mHandler.sendEmptyMessage(GET_DATA);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               AllPfAlbumSunObject object = (AllPfAlbumSunObject) adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("path",object.getPath());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
