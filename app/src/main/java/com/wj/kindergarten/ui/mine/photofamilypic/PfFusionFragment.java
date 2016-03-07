package com.wj.kindergarten.ui.mine.photofamilypic;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
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
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.compounets.NestedGridView;
import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.handler.MessageHandlerListener;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.adapter.PfAlbumRecycleAdapter;
import com.wj.kindergarten.ui.func.adapter.PfFirstTimeAdapter;
import com.wj.kindergarten.ui.func.adapter.PfWallAdapter;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.ui.more.BanScrollView;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;

import net.tsz.afinal.db.sqlite.DbModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PfFusionFragment extends Fragment {

    private String family_uuid;
    private PhotoFamilyFragment photoFamilyFragment;
    private BanScrollView banScrollView;
    boolean noMoreData;

    public BanScrollView getBanScrollView() {
        return banScrollView;
    }

    public boolean scrollIsTop(){
        return banScrollView.getRefreshableView().getScrollY() == 0;
    }

    public PfFusionFragment(PhotoFamilyFragment photoFamilyFragment,String family_uuid) {
        super();
        this.photoFamilyFragment = photoFamilyFragment;
        this.family_uuid = family_uuid;
    }


    private View view;
    private PullToRefreshScrollView pullScroll;
    private PfWallAdapter adapter;
    private List<AllPfAlbumSunObject> albumList = new ArrayList<>();
    //将网络获取的照片按照日期分类排列
    private List<List<AllPfAlbumSunObject>> lists = new ArrayList<>();
    //将按照日期分类的图片地址单独提取出来
    //保存日期的集合
    private List<QueryGroupCount> queryGroupCounts = new ArrayList<>();
    private boolean isFirst;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //查询指定时间前的数据
                case PfLoadDataProxy.NORMAL_DATA:
                    List<QueryGroupCount> allList = (List<QueryGroupCount>) msg.obj;
                    List<QueryGroupCount> newData = new ArrayList<>();
                    if (allList != null && allList.size() > 0) {
                        for(QueryGroupCount queryGroupCount : allList){
                            if(!queryGroupCounts.contains(queryGroupCount)){
                                queryGroupCounts.add(queryGroupCount);
                                newData.add(queryGroupCount);
                            }
                        }
                        if(newData.size() > 0){
//                            addListDataByDate(newData);
                            addDataByOrder(fragment_pf_fusion_linear,newData);
                        }

                    }
                    break;
                //有maxTime后时间的刷新的数据
                case PfLoadDataProxy.REFRESH_DATA:
                     showView();
                    break;
            }
        }
    };



    private void showView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TextView textView = new TextView(getActivity());
        textView.setText(""+"您有新数据了，点击更新!");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPfLoadDataProxy.loadData(family_uuid,1,false);
            }
        });
        builder.setView(textView);
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params =  window.getAttributes();
        params.x = 100;
        params.y = 150;
        params.gravity = Gravity.TOP;
        window.setAttributes(params);
        dialog.show();

    }

    private PfLoadDataProxy mPfLoadDataProxy;
    private LinearLayout fragment_pf_fusion_linear;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initScrollListen();
        if (view != null) return view;
        view = inflater.inflate(R.layout.fragment_pf_fusion, null);
//        pullScroll = (PullToRefreshScrollView) view.findViewById(R.id.fragment_pf_fusion_scroll);
//        fragment_pf_fusion_linear = (LinearLayout) view.findViewById(R.id.fragment_pf_fusion_linear);
//        banScrollView = (BanScrollView) view.findViewById(R.id.fragment_pf_fusion_scroll);
        banScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        photoFamilyFragment.setLocationChanged(new PhotoFamilyFragment.LocationChanged() {
            @Override
            public void onTop() {
                banScrollView.setCanScroll(true);
                if (!noMoreData) {

                }

            }

            @Override
            public void onBottom() {
            }
        });
        pullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                   mPfLoadDataProxy.loadData(family_uuid,1,true);
            }
        });

        loadData();

        return view;
    }

    private void initScrollListen() {
        photoFamilyFragment.setSubViewDecideScroll(new PfFragmentLinearLayout.DecideSubViewScroll() {

            @Override
            public void allowScroll() {
                banScrollView.setCanScroll(true);
            }

            @Override
            public void stopScroll() {
                banScrollView.setCanScroll(false);
            }

            @Override
            public boolean subViewLocationTop() {
                return banScrollView.getRefreshableView().getScrollY() == 0;
            }
        });
    }

    private void addDataByOrder(LinearLayout linearLayout,List<QueryGroupCount> newData) {
        for(QueryGroupCount count : newData){
            List<AllPfAlbumSunObject> objectList = mPfLoadDataProxy.queryListByDate(family_uuid,count.getDate());
                if(objectList == null) continue;
                View view = View.inflate(getActivity(),R.layout.pf_family_first_time_item,null);
                TextView pf_family_first_item_time = (TextView) view.findViewById(R.id.pf_family_first_item_time);
                TextView pf_family_first_item_count = (TextView) view.findViewById(R.id.pf_family_first_item_count);
                pf_family_first_item_time.setText("" + count.getDate());
                pf_family_first_item_count.setText("共" + count.getCount() + "张");
                NestedGridView gridView = (NestedGridView) view.findViewById(R.id.pf_family_first_item_grid);
                gridView.setVisibility(View.VISIBLE);
//                PfFirstTimeAdapter adapter = new PfFirstTimeAdapter(getActivity(),objectList);
                gridView.setAdapter(adapter);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(view, params);
            }
        }

    private void addListDataByDate(List<QueryGroupCount> allList) {

        for (QueryGroupCount count : allList) {

            List<AllPfAlbumSunObject> objectList = mPfLoadDataProxy.queryListByDate(family_uuid, count.getDate());
            if (objectList == null) continue;

            View view = View.inflate(getActivity(), R.layout.pf_classic_by_date_album, null);
            ImageView iv_0 = (ImageView) view.findViewById(R.id.pf_iv_0);
            ImageView iv_1 = (ImageView) view.findViewById(R.id.pf_iv_1);
            ImageView iv_2 = (ImageView) view.findViewById(R.id.pf_iv_2);
            ImageView iv_3 = (ImageView) view.findViewById(R.id.pf_iv_3);
            ImageView iv_4 = (ImageView) view.findViewById(R.id.pf_iv_4);
            ImageView iv_5 = (ImageView) view.findViewById(R.id.pf_iv_5);
            TextView pf_tv_date_time = (TextView) view.findViewById(R.id.pf_tv_date_time);
            TextView pf_pic_count = (TextView) view.findViewById(R.id.pf_pic_count);
            pf_tv_date_time.setText("" + count.getDate());
            pf_pic_count.setText("共" + count.getCount() + "张");
            int size = objectList.size();
            if (size > 0){
                for (int i = 0 ; i < size ; i++){
                    if(i == 0){
                        iv_0.setOnClickListener(new TransportListener(getActivity(),i,objectList,allList));
                        ImageLoaderUtil.displayMyImage(objectList.get(i).getPath(),iv_0);
                    }else if(i == 1){
                        iv_1.setOnClickListener(new TransportListener(getActivity(),i,objectList,allList));
                        ImageLoaderUtil.displayMyImage(objectList.get(i).getPath(),iv_1);
                    }else if(i == 2){
                        iv_2.setOnClickListener(new TransportListener(getActivity(),i,objectList,allList));
                        ImageLoaderUtil.displayMyImage(objectList.get(i).getPath(),iv_2);
                    }else if(i == 3){
                        iv_3.setOnClickListener(new TransportListener(getActivity(),i,objectList,allList));
                        ImageLoaderUtil.displayMyImage(objectList.get(i).getPath(),iv_3);
                    }else if(i == 4){
                        iv_4.setOnClickListener(new TransportListener(getActivity(),i,objectList,allList));
                        ImageLoaderUtil.displayMyImage(objectList.get(i).getPath(),iv_4);
                    }else if(i == 5){
                        iv_5.setOnClickListener(new TransportListener(getActivity(),i,objectList,allList));
                        ImageLoaderUtil.displayMyImage(objectList.get(i).getPath(),iv_5);
                    }
                }
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
        mPfLoadDataProxy.setDataLoadFinish(new PfLoadDataProxy.DataLoadFinish() {
            @Override
            public void finish() {
                if(pullScroll.isRefreshing()){
                    pullScroll.onRefreshComplete();
                }
            }

            @Override
            public void noMoreData() {

                noMoreData = true;
            }

        });
        mPfLoadDataProxy.loadData(family_uuid, pageNo, false);
    }

}
