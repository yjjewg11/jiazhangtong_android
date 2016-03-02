package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.BoutiqueAlbum;
import com.wj.kindergarten.bean.BoutiqueAlbumListSun;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.adapter.BoutiqueAdapter;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class BoutiqueAlbumFragment extends Fragment {
    View view;
    private PullToRefreshListView pullListView;
    private BoutiqueAdapter boutiqueAdapter;
    PhotoFamilyFragment photoFamilyFragment;
    int firstVisibleItemOwn = -1;
    private List<BoutiqueAlbumListSun> boutiqueAlbumList = new ArrayList<>();
    public BoutiqueAlbumFragment(PhotoFamilyFragment photoFamilyFragment) {
        this.photoFamilyFragment = photoFamilyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initScrollListener();
        if(view != null) return view;
        view = inflater.inflate(R.layout.fragment_test,null);
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh_list);
        boutiqueAdapter = new BoutiqueAdapter(getActivity());
        pullListView.setAdapter(boutiqueAdapter);
        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        pullListView.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisibleItemOwn = firstVisibleItem;
            }
        });
        pullListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),BoutiqueSingleInfoActivity.class);
                intent.putExtra("uuid",boutiqueAlbumList.get(position).getUuid());
                startActivity(intent);
            }
        });
        loadData();

        return view;
    }

    private void initScrollListener() {
        photoFamilyFragment.setSubViewDecideScroll(new PfFragmentLinearLayout.DecideSubViewScroll() {
            @Override
            public void allowScroll() {

            }

            @Override
            public void stopScroll() {

            }

            @Override
            public boolean subViewLocationTop() {
                return firstVisibleItemOwn == 0;
            }
        });
    }

    int pageNo = 1;

   public void loadData(){
       UserRequest.getBoutiqueAlbumList(getActivity(),pageNo, new RequestResultI() {
           @Override
           public void result(BaseModel domain) {
               BoutiqueAlbum boutiqueAlbum = (BoutiqueAlbum) domain;
               if(boutiqueAlbum != null && boutiqueAlbum.getList() != null
                       && boutiqueAlbum.getList().getData() != null && boutiqueAlbum.getList().getData().size() > 0){
                   boutiqueAlbumList.addAll(boutiqueAlbum.getList().getData());
                   boutiqueAdapter.setList(boutiqueAlbumList);
               }else{
                   if(pageNo == 1){
                       ToastUtils.showMessage("没有");
                   }else {
                       ToastUtils.showMessage("没有更多内容了!!!");
                       pullListView.setMode(PullToRefreshBase.Mode.DISABLED);
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
   };
}
