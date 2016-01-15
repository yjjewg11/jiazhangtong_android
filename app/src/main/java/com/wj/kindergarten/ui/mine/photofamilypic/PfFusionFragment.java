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
import com.wj.kindergarten.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class PfFusionFragment extends Fragment {


    private View view;
    private PullToRefreshListView listView;
    private PfWallAdapter adapter;
    private List<AllPfAlbumSunObject> albumList = new ArrayList<>();
    //将网络获取的照片按照日期分类排列
    private List<List<AllPfAlbumSunObject>> lists = new ArrayList<>();
    //将按照日期分类的图片地址单独提取出来
    private boolean isFirst;


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
            isFirst = true;
            addListener();
            loadData();



        return view;
    }

    private void addListener() {
        GlobalHandler.getHandler().addMessageHandlerListener(new MessageHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == GloablUtils.GET_PF_ALBUM_LIST_SUCCESS){
                    adapter.setImageList(lists);
                }
            }
        });
        GlobalHandler.getHandler().addMessageHandlerListener(new MessageHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == GloablUtils.CLASSFIY_PF_BY_DATE){
                    //将集合里面的照片按日期分类
                    if(albumList != null && albumList.size() > 0){
                        Iterator<AllPfAlbumSunObject> iterator = albumList.iterator();
                        String date = null;
                        while (iterator.hasNext()){
                            AllPfAlbumSunObject sunObject = iterator.next();
                            if(sunObject == null) continue;
                            if(date == null ||
                                    !TimeUtil.getYMDTimeFromYMDHMS(sunObject.getPhoto_time()).equals(date)){
                                ArrayList<AllPfAlbumSunObject> list = new ArrayList<>();
                                list.add(sunObject);
                                lists.add(list);
                            }else{
                                lists.get(lists.size()-1).add(sunObject);
                            }
                            date = TimeUtil.getYMDTimeFromYMDHMS(sunObject.getPhoto_time());
                        }
                        GlobalHandler.getHandler().sendEmptyMessage(GloablUtils.GET_PF_ALBUM_LIST_SUCCESS);
                    }

            }
            }
        });
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
                    GlobalHandler.getHandler().sendEmptyMessage(GloablUtils.CLASSFIY_PF_BY_DATE);
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
