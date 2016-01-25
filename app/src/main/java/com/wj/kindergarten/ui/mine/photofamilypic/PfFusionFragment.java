package com.wj.kindergarten.ui.mine.photofamilypic;

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
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //查询指定时间前的数据
                case PfLoadDataProxy.NORMAL_DATA :
                    List<AllPfAlbumSunObject> allList = (List<AllPfAlbumSunObject>) msg.obj;
                    if(allList != null && allList.size() > 0){
                        albumList.addAll(allList);
                    }
                    break;
                //有maxTime后时间的刷新的数据
                case PfLoadDataProxy.REFRESH_DATA :

                    break;
            }
        }
    };
    private PfLoadDataProxy mPfLoadDataProxy;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) return view;
        view = inflater.inflate(R.layout.fragment_pf_fusion, null);
        pullScroll = (PullToRefreshScrollView) view.findViewById(R.id.fragment_pf_fusion_scroll);
        pullScroll.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        adapter = new PfWallAdapter(getActivity());

        loadData();

        return view;
    }

    private void addListener() {
        GlobalHandler.getHandler().addMessageHandlerListener(new MessageHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == GloablUtils.PF_ALBUM_ADD_WATERFALL_PIC){
                   View view = View.inflate(getActivity(),R.layout.pf_classic_by_date_album, null);
                    TextView pf_tv_date_time = (TextView) view.findViewById(R.id.pf_tv_date_time);
                    TextView pf_pic_count = (TextView) view.findViewById(R.id.pf_pic_count);
//                    LinearLayout pf_fusion_out_ll = (LinearLayout) view.findViewById(R.id.pf_fusion_out_ll);
//                    LinearLayout pf_album_linearLayout_left = (LinearLayout) view.findViewById(R.id.pf_album_linearLayout_left);
//                    LinearLayout pf_album_linearLayout_center = (LinearLayout) view.findViewById(R.id.pf_album_linearLayout_center);
//                    LinearLayout pf_album_linearLayout_right = (LinearLayout) view.findViewById(R.id.pf_album_linearLayout_right);

//                    if (lists != null && lists.size() > 0 &&
//                            lists.get(position) != null && lists.get(position).size() > 0) {
//                        viewHolder.pf_pic_count.setText("共" + lists.get(position).size() + "张");
//                        viewHolder.pf_tv_date_time.setText("" +
//                                TimeUtil.getYMDTimeFromYMDHMS(lists.get(position).get(0).getPhoto_time()));
//                        if (lists.get(position).size() == 1) {
//                            viewHolder.pf_album_linearLayout_center.setVisibility(View.GONE);
//                            viewHolder.pf_album_linearLayout_right.setVisibility(View.GONE);
//                            ImageView imageView = new ImageView(context);
//                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                            viewHolder.pf_fusion_out_ll.addView(imageView, setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                    ViewGroup.LayoutParams.MATCH_PARENT));
//                            ImageLoaderUtil.displayMyImage(lists.get(position).get(0).getPath(), imageView);
//                        } else if (lists.get(position).size() > 1 && lists.get(position).size() <= 3) {
//                            viewHolder.pf_album_linearLayout_center.setVisibility(View.GONE);
//                            viewHolder.pf_album_linearLayout_right.setVisibility(View.GONE);
//                            for (AllPfAlbumSunObject object : lists.get(position)) {
//                                ImageView imageView = new ImageView(context);
//                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                                params.weight = 1;
//                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                                viewHolder.pf_fusion_out_ll.addView(imageView, params);
//                                ImageLoaderUtil.displayMyImage(object.getPath(), imageView);
//                            }
//                        } else if (lists.get(position).size() > 3) {
//                            //给容器中添加3个垂直linearLayout用来添加图片
//                            int count = 0;
//                            for (AllPfAlbumSunObject object : lists.get(position)) {
//                                if(count == 3){
//                                    count = 0;
//                                }
//                                count ++;
//                                ImageView imageView = new ImageView(context);
//                                ImageLoaderUtil.displayMyImage(object.getPath(),imageView);
//                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                                        LinearLayout.LayoutParams.WRAP_CONTENT);
//                                if(count == 0){
//                                    viewHolder.pf_album_linearLayout_left.addView(imageView, params);
//                                }else if(count == 1){
//                                    viewHolder.pf_album_linearLayout_center.addView(imageView, params);
//                                }else if(count == 2){
//                                    viewHolder.pf_album_linearLayout_right.addView(imageView, params);
//                                }
//
//                            }
//
//
//                        }
//                    }



                }
            }
        });
        GlobalHandler.getHandler().addMessageHandlerListener(new MessageHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == GloablUtils.GET_PF_ALBUM_LIST_SUCCESS) {
                    adapter.setImageList(lists);
                }
            }
        });
        GlobalHandler.getHandler().addMessageHandlerListener(new MessageHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == GloablUtils.CLASSFIY_PF_BY_DATE) {
                    //将集合里面的照片按日期分类
                    if (albumList != null && albumList.size() > 0) {
                        Iterator<AllPfAlbumSunObject> iterator = albumList.iterator();
                        String date = null;
                        while (iterator.hasNext()) {
                            AllPfAlbumSunObject sunObject = iterator.next();
                            if (sunObject == null) continue;
                            if (date == null ||
                                    !TimeUtil.getYMDTimeFromYMDHMS(sunObject.getPhoto_time()).equals(date)) {
                                ArrayList<AllPfAlbumSunObject> list = new ArrayList<>();
                                list.add(sunObject);
                                lists.add(list);
                            } else {
                                lists.get(lists.size() - 1).add(sunObject);
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

    public void loadData() {
        mPfLoadDataProxy = new PfLoadDataProxy(getActivity(),mHandler);
        mPfLoadDataProxy.loadData( ((MainActivity)getActivity()).getFamily_uuid() ,pageNo,false);
    }

}
