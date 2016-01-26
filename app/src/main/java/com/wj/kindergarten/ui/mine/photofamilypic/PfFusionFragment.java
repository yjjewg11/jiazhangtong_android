package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbum;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.handler.MessageHandlerListener;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.adapter.PfWallAdapter;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;

import net.tsz.afinal.db.sqlite.DbModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PfFusionFragment extends Fragment {

    private View view;
    private PullToRefreshScrollView pullScroll;
    private PfWallAdapter adapter;
    private List<AllPfAlbumSunObject> albumList = new ArrayList<>();
    //将网络获取的照片按照日期分类排列
    private List<List<AllPfAlbumSunObject>> lists = new ArrayList<>();
    //将按照日期分类的图片地址单独提取出来
    private boolean isFirst;
    private List<String> allList;//接受日期集合
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //查询指定时间前的数据
                case PfLoadDataProxy.NORMAL_DATA :
                    allList = (List<String>) msg.obj;
                    if(allList != null && allList.size() > 0){
                        addListDataByDate();
                    }
                    break;
                //有maxTime后时间的刷新的数据
                case PfLoadDataProxy.REFRESH_DATA :

                    break;
            }
        }
    };
    private PfLoadDataProxy mPfLoadDataProxy;
    private LinearLayout fragment_pf_fusion_linear;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) return view;
        view = inflater.inflate(R.layout.fragment_pf_fusion, null);
        pullScroll = (PullToRefreshScrollView) view.findViewById(R.id.fragment_pf_fusion_scroll);
        fragment_pf_fusion_linear = (LinearLayout) view.findViewById(R.id.fragment_pf_fusion_linear);
        pullScroll.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

//        adapter = new PfWallAdapter(getActivity());

        loadData();

        return view;
    }

    private void addListDataByDate() {

        for(String date : allList){

        List<AllPfAlbumSunObject> objectList =  mPfLoadDataProxy.queryListByDate(MainActivity.getFamily_uuid(), date);
        if(objectList == null) continue;
            int position = -1;
            View view = View.inflate(getActivity(),R.layout.pf_classic_by_date_album, null);

        TextView pf_tv_date_time = (TextView) view.findViewById(R.id.pf_tv_date_time);
        TextView pf_pic_count = (TextView) view.findViewById(R.id.pf_pic_count);
            pf_tv_date_time.setText(""+date);
            pf_pic_count.setText(""+objectList.size());
            LinearLayout linearLayout1 = (LinearLayout) view.findViewById(R.id.pf_classic_album_linear_1);
            LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.pf_classic_album_linear_2);
            LinearLayout linearLayout3 = (LinearLayout) view.findViewById(R.id.pf_classic_album_linear_3);
            ImageView pf_classic_album_0 = (ImageView) view.findViewById(R.id.pf_classic_album_0);
            ImageView pf_classic_album_1 = (ImageView) view.findViewById(R.id.pf_classic_album_1);
            ImageView pf_classic_album_2 = (ImageView) view.findViewById(R.id.pf_classic_album_2);
            ImageView pf_classic_album_3 = (ImageView) view.findViewById(R.id.pf_classic_album_3);
            ImageView pf_classic_album_4 = (ImageView) view.findViewById(R.id.pf_classic_album_4);
            ImageView pf_classic_album_5 = (ImageView) view.findViewById(R.id.pf_classic_album_5);
            int size = objectList.size();
            if(size <= 2){
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
            }else if(size <= 4){
                linearLayout3.setVisibility(View.GONE);
            }
            for(int i = 0 ; i < (objectList.size() > 6 ? 6 : objectList.size()) ; i++ ){
                if(i == 0){
                    setListener(objectList, pf_classic_album_0, i);
                    ImageLoaderUtil.displayMyImage(judgeReturnPath(objectList.get(i), i),pf_classic_album_0);
                }else if(i == 1){
                    setListener(objectList, pf_classic_album_1, i);
                    ImageLoaderUtil.displayMyImage(judgeReturnPath(objectList.get(i), i),pf_classic_album_1);
                }else if(i == 2){
                    setListener(objectList, pf_classic_album_2, i);
                    ImageLoaderUtil.displayMyImage(judgeReturnPath(objectList.get(i), i),pf_classic_album_2);
                }else if(i == 3){
                    setListener(objectList, pf_classic_album_3, i);
                    ImageLoaderUtil.displayMyImage(judgeReturnPath(objectList.get(i), i),pf_classic_album_3);
                }else if(i == 4){
                    setListener(objectList, pf_classic_album_4, i);
                    ImageLoaderUtil.displayMyImage(judgeReturnPath(objectList.get(i), i),pf_classic_album_4);
                }else if(i == 5){
                    setListener(objectList, pf_classic_album_5, i);
                    ImageLoaderUtil.displayMyImage(judgeReturnPath(objectList.get(i), i),pf_classic_album_5);
                }
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            fragment_pf_fusion_linear.addView(view,params);

        }

    }

    private void setListener(List<AllPfAlbumSunObject> objectList, ImageView pf_classic_album_0, int i) {
        pf_classic_album_0.setOnClickListener(new TransportListener(getActivity(),i,objectList));
    }

    private String judgeReturnPath(AllPfAlbumSunObject object, int i) {
        String path = null;
        if (object != null){
            path = object.getPath();
        }
        return path;
    }

    int pageNo = 1;

    public void loadData() {
        mPfLoadDataProxy = new PfLoadDataProxy(getActivity(),mHandler);
        mPfLoadDataProxy.loadData( ((MainActivity)getActivity()).getFamily_uuid() ,pageNo,false);
    }

}
