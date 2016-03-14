package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BoutiqueSingleInfoObject;
import com.wj.kindergarten.bean.PfModeNameObject;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
import com.wj.kindergarten.utils.GloablUtils;

import net.tsz.afinal.FinalDb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PfChoosedPicActivity extends BaseActivity {

    private GridView gridView;
    private RelativeLayout next_step_rl;
    private List<AllPfAlbumSunObject> objectList ;
    private HashMap<String,Boolean> selectMap = new HashMap<>();
    private PfChoosePicAdapter adapter;
    private ArrayList<String> images = new ArrayList<>();
    private String uuid;
    private PfModeNameObject pfModeNameObject;
    private BoutiqueSingleInfoObject boutiqueSingleInfoObject;
    private FinalDb db;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_pf_choosed_pic;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        ActivityManger.getInstance().addPfActivities(this);
        setTitleText("选择图片");
        db = FinalDb.create(this, GloablUtils.FAMILY_UUID_OBJECT);
        getData();
        initViews();
        initClickListener();
    }

    private void initClickListener() {
        next_step_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //带着集合进入编辑页面
                 Intent intent = new Intent(PfChoosedPicActivity.this,BoutiqueAlbumEditActivity.class);
                 intent.putExtra("objectList", (ArrayList) adapter.getSelectList());
                 intent.putExtra("object",boutiqueSingleInfoObject);
                 startActivity(intent);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == objectList.size() - 1) {
                    returnList();
                    finish();
                } else {
                    //不要查看详细，改成切换封面
                    adapter.clearSingleList(position);
                    boutiqueSingleInfoObject.setHerald(adapter.getCurrentObj(position).getPath());
//                    Intent intent = new Intent(PfChoosedPicActivity.this, PhotoWallActivity.class);
//                    ArrayList<String> simplePic = new ArrayList<>();
//                    simplePic.add(images.get(position));
//                    intent.putStringArrayListExtra(PhotoWallActivity.KEY_LIST, simplePic);
//                    startActivity(intent);
                }
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
//        pfModeNameObject = (PfModeNameObject) intent.getSerializableExtra("mode");
        objectList = (List<AllPfAlbumSunObject>) intent.getSerializableExtra("objectList");
        boutiqueSingleInfoObject = (BoutiqueSingleInfoObject) intent.getSerializableExtra("object");
        //说明是新建，
        if(boutiqueSingleInfoObject == null) boutiqueSingleInfoObject = new BoutiqueSingleInfoObject();
        if(objectList != null && objectList.size() > 0){
            boutiqueSingleInfoObject.setHerald(objectList.get(0).getPath());
            Iterator<AllPfAlbumSunObject> iterator = objectList.iterator();
            while (iterator.hasNext()){
                AllPfAlbumSunObject object = iterator.next();
                selectMap.put(object.getPath(),true);
                images.add(object.getPath());
            }
            setTitleText("选择图片("+objectList.size()+")");
        }
    }

    private void initViews() {
              gridView = (GridView) findViewById(R.id.choose_pf_grid);
              next_step_rl = (RelativeLayout) findViewById(R.id.pf_choose_rl);
              adapter = new PfChoosePicAdapter(objectList,100,selectMap,gridView);
              gridView.setAdapter(adapter);
    }

    public void returnList(){
          Intent intent = new Intent();
          intent.putExtra("selectList", (ArrayList) adapter.getSelectList());
          setResult(RESULT_OK, intent);
    }

    @Override
    protected void titleLeftButtonListener() {
        returnList();
        super.titleLeftButtonListener();
    }

    @Override
    public void onBackPressed() {
        returnList();
        finish();
    }

    public void selectChange(HashMap<String, Boolean> mSelectMap) {
        setTitleText("选择图片("+mSelectMap.size()+")");
    }
}
