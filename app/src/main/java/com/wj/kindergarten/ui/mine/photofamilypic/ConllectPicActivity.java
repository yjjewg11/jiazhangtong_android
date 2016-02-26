package com.wj.kindergarten.ui.mine.photofamilypic;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.compounets.NestedGridView;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.PfCollectPicAdapter;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/18.
 */
public class ConllectPicActivity extends BaseActivity{
    private GridView gridView;
    private List<AllPfAlbumSunObject> collect_list;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.collect_pic_activity;
    }

    @Override
    protected void setNeedLoading() {

    }


    @Override
    protected void onCreate() {
        collect_list = (List<AllPfAlbumSunObject>) getIntent().getSerializableExtra("collect_list");
        initViews();
        setTitleText("我的收藏");

    }

    private void initViews() {
        gridView = (GridView)findViewById(R.id.collect_grid_view);
        gridView.setAdapter(new PfCollectPicAdapter(ConllectPicActivity.this,collect_list));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.showPfSingleinfo(ConllectPicActivity.this, position, (ArrayList<AllPfAlbumSunObject>) collect_list);
            }
        });

    }
}
