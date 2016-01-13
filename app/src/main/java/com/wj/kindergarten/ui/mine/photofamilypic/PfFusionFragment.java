package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbum;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.handler.MessageHandlerListener;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.utils.GloablUtils;

import java.util.ArrayList;
import java.util.List;


public class PfFusionFragment extends Fragment {


    private View view;
    private PullToRefreshListView listView;
    private PfWallAdapter adapter;
    private List<AllPfAlbumSunObject> albumList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) return view;
        view = inflater.inflate(R.layout.fragment_pf_fusion, null);
        listView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh_list);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载数据
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        adapter = new PfWallAdapter(getActivity());
        listView.setAdapter(adapter);
        loadData();
        return view;
    }

    int pageNo = 1;
    String family_uuid = "";

    public void loadData() {
        UserRequest.getPfFusionPic(getActivity(), pageNo, family_uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                AllPfAlbum allPfAlbum = (AllPfAlbum) domain;
                if (allPfAlbum != null && allPfAlbum.getList() != null &&
                        allPfAlbum.getList().getData() != null && allPfAlbum.getList().getData().size() > 0) {
                    albumList.addAll(allPfAlbum.getList().getData());
                    GlobalHandler.getHandler().addMessageHandlerListener(new MessageHandlerListener() {
                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.what == GloablUtils.GET_PF_ALBUM_LIST_SUCCESS){
                                adapter.setImageList(albumList);
                            }
                        }
                    });
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
