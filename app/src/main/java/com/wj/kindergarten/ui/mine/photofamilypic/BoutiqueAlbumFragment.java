package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.adapter.BoutiqueAdapter;

import java.util.List;

public class BoutiqueAlbumFragment extends Fragment {
    View view;
    private PullToRefreshListView pullListView;
    private BoutiqueAdapter boutiqueAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        view = inflater.inflate(R.layout.fragment_test,null);
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh_list);
        boutiqueAdapter = new BoutiqueAdapter(getActivity());
        pullListView.setAdapter(boutiqueAdapter);
        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        loadData();

        return view;
    }
    int pageNo = 1;

   public void loadData(){
       UserRequest.getBoutiqueAlbumList(getActivity(),pageNo, new RequestResultI() {
           @Override
           public void result(BaseModel domain) {

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
