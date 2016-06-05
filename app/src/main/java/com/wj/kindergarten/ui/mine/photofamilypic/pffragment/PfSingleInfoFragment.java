package com.wj.kindergarten.ui.mine.photofamilypic.pffragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.adapter.PfInfoFragmentAdapter;
import com.wj.kindergarten.ui.func.adapter.PfPopPicAdapter;
import com.wj.kindergarten.ui.mine.photofamilypic.PfGalleryActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.FinalUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

public class PfSingleInfoFragment extends Fragment {
    View fragmentView;
    private List<AllPfAlbumSunObject> list = new ArrayList<>();
    private ViewPager viewPager;
    private int position;
    private PfInfoFragmentAdapter pagerAdapter;
    private PfGalleryActivity activity;
    private FinalDb dbObj;
    private FinalDb dbUpload;

    public PfSingleInfoFragment(int position, List<AllPfAlbumSunObject> list) {
        this.position = position;
        this.list.clear();
        this.list.addAll(list);
    }

    public PfSingleInfoFragment() {
    }

    public void setList(List<AllPfAlbumSunObject> list,int position) {
        this.list.clear();
        this.list.addAll(list);
        this.position = position;
        if(pagerAdapter == null || viewPager == null) return;
        pagerAdapter.setObjectList(list);
        viewPager.setCurrentItem(position);
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
        dbObj = FinalUtil.getFamilyUuidObjectDb(getActivity());
        dbUpload = FinalUtil.getAlreadyUploadDb(getActivity());
    }

    public void deleteCurrentItem(AllPfAlbumSunObject sunObject) {
        final int currentIndex =  viewPager.getCurrentItem();
        CGLog.v("打印最近下标 : "+currentIndex);
        //从数据库，网络，轮播图中删除
        final AllPfAlbumSunObject object = list.get(viewPager.getCurrentItem());

        UserRequest.deleteSinglePf(getActivity(), object.getUuid(), new RequestFailedResult() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("删除成功");
                dbObj.delete(object);
                String sql = "data_id = '"+object.getUuid()+"'";
                dbUpload.deleteByWhere(AlreadySavePath.class,sql);
                list.remove(currentIndex);
                if(list.size() == 0) {
                    getActivity().finish();
                    return;
                }
                pagerAdapter.setObjectList(list);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

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





    private void setTitleText(String ymdTimeFromYMDHMS) {
        activity.changeTitle(ymdTimeFromYMDHMS);
    }

    private void initViews() {
        viewPager = (ViewPager) fragmentView.findViewById(R.id.pf_edit_viewPager);
        pagerAdapter = new PfInfoFragmentAdapter(getFragmentManager(),viewPager,this);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.setObjectList(list);
        if(list.size() > 0){
            viewPager.setCurrentItem(position);
        }
    }

}
