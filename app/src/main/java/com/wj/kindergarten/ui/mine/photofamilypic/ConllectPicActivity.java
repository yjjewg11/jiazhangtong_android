package com.wj.kindergarten.ui.mine.photofamilypic;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbum;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.compounets.NestedGridView;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.PfCollectPicAdapter;
import com.wj.kindergarten.ui.more.PfRefreshLinearLayout;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/18.
 */
public class ConllectPicActivity extends BaseActivity{
    private List<AllPfAlbumSunObject> collect_list = new ArrayList<>();
    private PfCollectPicAdapter adapter;
    private GridView gridView;
    private PfRefreshLinearLayout refreshLinear;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.collect_pic_activity;
    }

    @Override
    protected void setNeedLoading() {
    }


    @Override
    protected void onCreate() {
        initViews();
        setTitleText("我的收藏");
        loadData();
    }

    int firstItem = 0;
    int totalItem = 0;
    int visibleItem = 0;

    private void initViews() {
        refreshLinear = (PfRefreshLinearLayout) findViewById(R.id.collect_pic_refresh_linear);
        gridView = (GridView) findViewById(R.id.collect_pic_refresh_gridView);
        adapter = new PfCollectPicAdapter(ConllectPicActivity.this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.showPfSingleinfo(ConllectPicActivity.this, position, (ArrayList<AllPfAlbumSunObject>) collect_list);
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem = firstVisibleItem;
                visibleItem = visibleItemCount;
                totalItem = totalItemCount;
            }
        });

        refreshLinear.setOnRefreshListener(new PfRefreshLinearLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo++;
                loadData();
            }
        });
        refreshLinear.setPullScroll(new PfRefreshLinearLayout.PullScroll() {
            @Override
            public boolean judgeScrollBotom() {
                return firstItem+visibleItem == totalItem;
            }
        });
    }

    int pageNo = 1;
    @Override
    protected void loadData() {
        UserRequest.gePfCollect(this,pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                AllPfAlbum allPfAlbum = (AllPfAlbum) domain;
                if(allPfAlbum != null && allPfAlbum.getList() != null && allPfAlbum.getList().getData() != null
                        && allPfAlbum.getList().getData().size() > 0){
                        collect_list.addAll(allPfAlbum.getList().getData());
                        adapter.setList(collect_list);
                }else{
                    if(pageNo == 1){
                        loadEmpty();
                    }else {
                        ToastUtils.showMessage("没有更多数据了！");
                        refreshLinear.onRefreshComplete();
                        refreshLinear.setMode(PfRefreshLinearLayout.Mode.DISALBED);
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
