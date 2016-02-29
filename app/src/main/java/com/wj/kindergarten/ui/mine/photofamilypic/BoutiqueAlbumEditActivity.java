package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.PfModeNameObject;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.BoutiqueAlbumEditAdapter;
import com.wj.kindergarten.utils.GloablUtils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoutiqueAlbumEditActivity extends BaseActivity {

    private BoutiqueAlbumEditAdapter adapter;
    private List<AllPfAlbumSunObject> objectList;
    public static final int BOUTIQUE_ALBUM_EDIT_SINGLE_OBJECT = 6006;
    private GridView gridView;
    @ViewInject(id = R.id.boutique_album_edit_next_step)
    private TextView boutique_album_edit_next_step;
    private String uuid;
    private PfModeNameObject pfModeNameObject;


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_boutique_album_edit;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        FinalActivity.initInjectedView(this);
        setTitleText("编辑照片信息");
        getData();
        initViews();
        initClick();
    }

    private void initClick() {
        boutique_album_edit_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoutiqueAlbumEditActivity.this,BoutiqueModeActivity.class);
                intent.putExtra("objectList", (ArrayList) objectList);
//                intent.putExtra("mode",pfModeNameObject);
                startActivity(intent);
            }
        });
    }

    public void startForResult(AllPfAlbumSunObject object){
        Intent intent = new Intent(this, SinglePfEditActivity.class);
        intent.putExtra("object", object);
        startActivityForResult(intent, BOUTIQUE_ALBUM_EDIT_SINGLE_OBJECT, null);
    }


    private void initViews() {
        gridView = (GridView) findViewById(R.id.boutique_edit_child_grid);
        adapter = new BoutiqueAlbumEditAdapter(this);
        adapter.setObjectList(objectList);
        gridView.setAdapter(adapter);
    }

    public void getData() {
        Intent intent = getIntent();
        objectList = (ArrayList<AllPfAlbumSunObject>) intent.getSerializableExtra("objectList");
//        pfModeNameObject =(PfModeNameObject) intent.getSerializableExtra("mode");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) return;
        if (requestCode == BOUTIQUE_ALBUM_EDIT_SINGLE_OBJECT ){
            AllPfAlbumSunObject o = (AllPfAlbumSunObject) data.getSerializableExtra("object");
            if(o != null){
                int index = objectList.indexOf(o);
                objectList.remove(o);
                objectList.add(index,o);
                adapter.setObjectList(objectList);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
