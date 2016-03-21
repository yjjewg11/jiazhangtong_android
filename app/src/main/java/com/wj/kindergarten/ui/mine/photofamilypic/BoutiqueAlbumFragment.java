package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.observer.Watcher;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ToastUtils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoutiqueAlbumFragment extends Fragment implements Watcher {
    View view;
    @ViewInject(id = R.id.pulltorefresh_list)
    private PullToRefreshListView pullListView;
    private BoutiqueAdapter boutiqueAdapter;
    PhotoFamilyFragment photoFamilyFragment;
    int firstVisibleItemOwn = -1;
    private List<BoutiqueAlbumListSun> boutiqueAlbumList = new ArrayList<>();
    private HintInfoDialog dialog;
    private String family_uuid;
    private String[] typeAll = {"mine", "all"};
    String nowType = typeAll[0];

    public BoutiqueAlbumFragment() {
    }

    public BoutiqueAlbumFragment(PhotoFamilyFragment photoFamilyFragment, String family_uuid) {
        this.photoFamilyFragment = photoFamilyFragment;
        this.family_uuid = family_uuid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initScrollListener();
        if (view != null) return view;
        if (photoFamilyFragment == null) photoFamilyFragment = PhotoFamilyFragment.instance;

        dialog = new HintInfoDialog(getActivity());
        photoFamilyFragment.getObserver().registerObserver(this);
        view = inflater.inflate(R.layout.fragment_test,container,false);
        FinalActivity.initInjectedView(this, view);
        pullListView.getRefreshableView().setDividerHeight(0);
        boutiqueAdapter = new BoutiqueAdapter(getActivity());
        boutiqueAdapter.setRel_uuid(family_uuid);
        pullListView.setAdapter(boutiqueAdapter);
        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo++;
                loadData(nowType);
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
                Intent intent = new Intent(getActivity(), BoutiqueSingleInfoActivity.class);
                intent.putExtra("uuid", boutiqueAlbumList.get(position - 1).getUuid());
                MainActivity.instance.startActivityForResult(intent, GloablUtils.DELETE_BOUTIQUE_ALBUM_SUCCESSED);
            }
        });
        loadData(nowType);

        return view;
    }

    private void initScrollListener() {
        if (photoFamilyFragment == null) return;
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

    public void loadData(String type) {
        if (!pullListView.isRefreshing()) {
            dialog.show();
        }
        UserRequest.getBoutiqueAllbumListFromType(getActivity(), type, pageNo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (pullListView.isRefreshing()) {
                    pullListView.onRefreshComplete();
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                BoutiqueAlbum boutiqueAlbum = (BoutiqueAlbum) domain;
                if (boutiqueAlbum != null && boutiqueAlbum.getList() != null
                        && boutiqueAlbum.getList().getData() != null) {
                    if (pageNo == 1) boutiqueAlbumList.clear();
                    boutiqueAlbumList.addAll(boutiqueAlbum.getList().getData());
                    boutiqueAdapter.setList(boutiqueAlbumList);
                } else {
                    if (pageNo == 1) {
//                        ToastUtils.showMessage("没有");
                    } else {
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
    }

    public void updateData(String uuid) {
        Iterator<BoutiqueAlbumListSun> iterator = boutiqueAlbumList.iterator();
        while (iterator.hasNext()) {
            BoutiqueAlbumListSun sun = iterator.next();
            if (sun.getUuid().equals(uuid)) {
                boutiqueAlbumList.remove(sun);
                boutiqueAdapter.setList(boutiqueAlbumList);
                break;
            }
        }
    }

    @Override
    public void refreshUUid(String family_uuid) {
        if (!TextUtils.isEmpty(family_uuid)) {
            this.family_uuid = family_uuid;
            refreshData();
        }
    }

    private void refreshData() {
        pageNo = 1;
        loadData(nowType);
    }

    public void loadDataAccordingType(int position) {
        if (position == 0) {
            nowType = typeAll[0];
        } else if (position == 1) {
            nowType = typeAll[1];
        }
        refreshData();
    }

}
