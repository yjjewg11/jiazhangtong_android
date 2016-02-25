package com.wj.kindergarten.ui.mine.photofamilypic.pffragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbum;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfDianZan;
import com.wj.kindergarten.bean.PfSingleAssess;
import com.wj.kindergarten.bean.PfSingleAssessObject;
import com.wj.kindergarten.bean.SingleNewInfo;
import com.wj.kindergarten.bean.SinlePfExtraInfo;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.PfInfoFragmentAdapter;
import com.wj.kindergarten.ui.func.adapter.PfPopPicAdapter;
import com.wj.kindergarten.ui.imagescan.AutoDownLoadListener;
import com.wj.kindergarten.ui.mine.photofamilypic.PfGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.SinglePfEditActivity;
import com.wj.kindergarten.ui.more.ListenScrollView;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PfSingleInfoFragment extends Fragment {
    View fragmentView;
    private List<AllPfAlbumSunObject> list = new ArrayList<>();
    private ViewPager viewPager;
    private int position;
    private FinalDb family_uuid_object;

    private TextView[] textViews;
    private PfInfoFragmentAdapter pagerAdapter;
    private PfGalleryActivity activity;
    private SinlePfExtraInfo singleInfo;
    private ViewEmot2 emot2;
    private FrameLayout frame_bottom_tab;
    private LinearLayout bottom_assess;
    private View pf_more_view;
    private ImageView pf_delete;
    private ImageView pf_edit;
    private int[] location;
    private FinalDb db;

    public PfSingleInfoFragment(int position, List<AllPfAlbumSunObject> list) {
        this.position = position;
        this.list.clear();
        this.list.addAll(list);
    }

    private View view;
    private GridView pf_pop_gridView;
    private PfPopPicAdapter pfAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };


    public void changeData(List<AllPfAlbumSunObject> list){
        this.list.clear();
        this.list.addAll(list);
        pagerAdapter.notifyDataSetChanged();
    }

    public void updateSingleData(AllPfAlbumSunObject object){
        int position  =  list.indexOf(object);
        list.remove(position);
        list.add(position, object);
        pagerAdapter.setObjectList(list);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((PfGalleryActivity)getActivity()).hideActionbar();
        if(fragmentView != null) return fragmentView;
        activity = (PfGalleryActivity) getActivity();
        fragmentView = inflater.inflate(R.layout.pf_gallery_activity,null);
        initDB();
        initViews();
        changeTitle();
        return fragmentView;
    }

    private void initDB() {
        db = FinalDb.create(getActivity(),GloablUtils.FAMILY_UUID_OBJECT);
    }

    public void deleteCurrentItem(AllPfAlbumSunObject sunObject) {
        //不知为何，下标自动增加了2，待研究
        int currentIndex =  viewPager.getCurrentItem();
        CGLog.v("打印最近下标 : "+currentIndex);
        //从数据库，网络，轮播图中删除
        AllPfAlbumSunObject object = list.get(viewPager.getCurrentItem());
        db.delete(object);
        list.remove(currentIndex);
        pagerAdapter.setObjectList(list);
        UserRequest.deleteSinglePf(getActivity(), object.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("删除成功");
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }



    private void changeTitle() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (list == null || list.size() == 0) return;
                AllPfAlbumSunObject sunObject = list.get(position);
                setTitleText(TimeUtil.getYMDTimeFromYMDHMS(sunObject.getCreate_time()));
                //做调试，屏蔽

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    int pageNo = 1;



    private void initCollect() {
            //可以收藏
        if(singleInfo.isFavor()){
            Utils.cancleStoreStatus(getActivity(),textViews[0]);
        }else{
            Utils.showStoreStatus(getActivity(),textViews[0]);
        }


        PfDianZan dianZan =  singleInfo.getDianZan();
        if(dianZan != null){
            //可以点赞
            if(dianZan.getYidianzan() == 0){
                Utils.cancleDizanStatus(getActivity(),textViews[2]);
            }else{
                Utils.showDianzanStatus(getActivity(), textViews[2]);
            }
        }
    }

    private void setTitleText(String ymdTimeFromYMDHMS) {
        activity.changeTitle(ymdTimeFromYMDHMS);
    }

    private void initViews() {
        viewPager = (ViewPager) fragmentView.findViewById(R.id.pf_edit_viewPager);
        pagerAdapter = new PfInfoFragmentAdapter(getFragmentManager(),viewPager,this);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.setObjectList(list);
        viewPager.setCurrentItem(position);
    }
}
