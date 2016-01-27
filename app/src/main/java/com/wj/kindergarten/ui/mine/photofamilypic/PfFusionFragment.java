package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.wj.kindergarten.ui.func.adapter.PfAlbumRecycleAdapter;
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
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //查询指定时间前的数据
                case PfLoadDataProxy.NORMAL_DATA:
                    allList = (List<String>) msg.obj;
                    if (allList != null && allList.size() > 0) {
                        addListDataByDate();
                    }
                    break;
                //有maxTime后时间的刷新的数据
                case PfLoadDataProxy.REFRESH_DATA:

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
        pullScroll.setMode(PullToRefreshBase.Mode.DISABLED);
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

        for (String date : allList) {

            List<AllPfAlbumSunObject> objectList = mPfLoadDataProxy.queryListByDate(MainActivity.getFamily_uuid(), date);
            if (objectList == null) continue;

            View view = View.inflate(getActivity(), R.layout.pf_classic_by_date_album, null);
            LinearLayout pf_classic_linear = (LinearLayout) view.findViewById(R.id.pf_classic_linear);
            TextView pf_tv_date_time = (TextView) view.findViewById(R.id.pf_tv_date_time);
            TextView pf_pic_count = (TextView) view.findViewById(R.id.pf_pic_count);
            pf_tv_date_time.setText("" + date);
            pf_pic_count.setText("共" + objectList.size() + "张");
            int size = objectList.size();
            if (size == 1) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
                ImageLoaderUtil.displayMyImage(date,imageView);
                pf_classic_linear.addView(imageView,params);

            }
            if (1 < size && size <= 2) {
                for(int i = 0 ; i < 2 ; i ++){
                    ImageView imageView = new ImageView(getActivity());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,80);
                    params.weight = 1;
                    ImageLoaderUtil.displayMyImage(objectList.get(i).getPath(),imageView);
                    pf_classic_linear.addView(imageView,params);
                }
            } else if (size > 2) {
                RecyclerView recyclerView = new RecyclerView(getActivity());
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                recyclerView.setAdapter(new PfAlbumRecycleAdapter(getActivity(), objectList));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                pf_classic_linear.addView(recyclerView,params);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            fragment_pf_fusion_linear.addView(view, params);

        }

    }

    private void setListener(List<AllPfAlbumSunObject> objectList, ImageView pf_classic_album_0, int i) {
        pf_classic_album_0.setOnClickListener(new TransportListener(getActivity(), i, objectList));
    }

    private String judgeReturnPath(AllPfAlbumSunObject object, int i) {
        String path = null;
        if (object != null) {
            path = object.getPath();
        }
        return path;
    }

    int pageNo = 1;

    public void loadData() {
        mPfLoadDataProxy = new PfLoadDataProxy(getActivity(), mHandler);
        mPfLoadDataProxy.loadData(((MainActivity) getActivity()).getFamily_uuid(), pageNo, false);
    }

}
