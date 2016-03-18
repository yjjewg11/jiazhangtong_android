package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.BoutiqueSingleInfoObject;
import com.wj.kindergarten.bean.PfModeName;
import com.wj.kindergarten.bean.PfModeNameObject;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.BoutiqueModeAdapter;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ToastUtils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class BoutiqueModeActivity extends BaseActivity {


    @ViewInject(id = R.id.boutique_mode_grid)
    private PullToRefreshGridView gridView;
    @ViewInject(id = R.id.boutique_mode_next_step,click = "onClick")
    private RelativeLayout boutique_mode_next_step;
    private BoutiqueModeAdapter adapter;
    private List<PfModeNameObject> list = new ArrayList<>();
    private List<AllPfAlbumSunObject> objectList;
    private BoutiqueSingleInfoObject boutiqueSingleInfoObject;
    private PfModeNameObject object;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_boutique_mode;
    }

    @Override
    protected void setNeedLoading() {
    }

    @Override
    protected void onCreate() {
        ActivityManger.getInstance().finishPfActivities();
        ActivityManger.getInstance().addPfActivities(this);
        FinalActivity.initInjectedView(this);
        commonDialog = new HintInfoDialog(this,"数据加载中...请稍候");
        getData();
        initData();
        initView();
        initClick();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.boutique_mode_next_step:

                if(object.getKey() == null || TextUtils.isEmpty(object.getKey())){
                    if(list != null && list.size() > 0){
                        object = list.get(0);
                    }
                }
                object.setAlbumUUid(boutiqueSingleInfoObject.getUuid());
                object.setHerald(boutiqueSingleInfoObject.getHerald());
                Intent intent = new Intent(BoutiqueModeActivity.this, BoutiqueModeReviewActivity.class);
                intent.putExtra("objectList", (ArrayList) objectList);
                intent.putExtra("objectMode", object);
                startActivity(intent);
                break;
        }
    }

    private void getData() {
        Intent intent = getIntent();
        objectList = (ArrayList)intent.getSerializableExtra("objectList");
        boutiqueSingleInfoObject = (BoutiqueSingleInfoObject) intent.getSerializableExtra("object");
        if(boutiqueSingleInfoObject != null){
            if(object == null) object = new PfModeNameObject();
            object.setHerald(boutiqueSingleInfoObject.getHerald());
            object.setKey(boutiqueSingleInfoObject.getTemplate_key());
            object.setTitle(boutiqueSingleInfoObject.getTitle());
            object.setAlbumUUid(boutiqueSingleInfoObject.getUuid());
        }
    }

    private void initClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                object = (PfModeNameObject) adapter.getItem(position);
                adapter.setKey(object.getKey());
                adapter.notifyDataSetChanged();
            }
        });
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                pageNo++;
                loadMData(false);
            }
        });

    }

    private void initView() {
        adapter = new BoutiqueModeAdapter(this);
        adapter.setKey(boutiqueSingleInfoObject.getTemplate_key());
        gridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        gridView.setAdapter(adapter);
        loadMData(true);
    }

    private void initData() {
        setTitleText("精品相册模板");
    }

    int pageNo = 1;
    private void loadMData(boolean dialog) {
        commonDialog.show();
        UserRequest.getBoutiqueMode(this,pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if(commonDialog.isShowing()){
                    commonDialog.dismiss();
                }
                PfModeName pfModeName = (PfModeName) domain;
                if(pfModeName != null && pfModeName.getList() != null
                        && pfModeName.getList().getData() != null && pfModeName.getList().getData().size() > 0){
                    list.addAll(pfModeName.getList().getData());
                    adapter.setList(list);
                }else{
                    if(pageNo == 1){
                        loadEmpty();
                    }else{
                        gridView.setMode(PullToRefreshBase.Mode.DISABLED);
                        ToastUtils.showMessage("没有更多内容了！");
                    }
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
    public void onBackPressed() {
        ActivityManger.getInstance().finishPfActivities();
        super.onBackPressed();
    }
}
