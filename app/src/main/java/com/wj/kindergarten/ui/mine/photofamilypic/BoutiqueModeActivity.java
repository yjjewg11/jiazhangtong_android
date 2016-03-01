package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.wenjie.jiazhangtong.R;
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
    private BoutiqueModeAdapter adapter;
    private List<PfModeNameObject> list = new ArrayList<>();
    private List<AllPfAlbumSunObject> objectList;
    private BoutiqueSingleInfoObject boutiqueSingleInfoObject;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_boutique_mode;
    }

    @Override
    protected void setNeedLoading() {
    }

    @Override
    protected void onCreate() {
        FinalActivity.initInjectedView(this);
        commonDialog = new HintInfoDialog(this,"数据加载中...请稍候");
        getData();
        initData();
        initView();
        initClick();
    }

    private void getData() {
        Intent intent = getIntent();
        objectList = (ArrayList)intent.getSerializableExtra("objectList");
        boutiqueSingleInfoObject = (BoutiqueSingleInfoObject) intent.getSerializableExtra("object");
    }

    private void initClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PfModeNameObject object = (PfModeNameObject) adapter.getItem(position);
                Intent intent = new Intent(BoutiqueModeActivity.this,BoutiqueModeReviewActivity.class);
                intent.putExtra("objectList",(ArrayList)objectList);
                intent.putExtra("key",object.getKey());
                intent.putExtra("object",boutiqueSingleInfoObject);
                startActivity(intent);
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
}
