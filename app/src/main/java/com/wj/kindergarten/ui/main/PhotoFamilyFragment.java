package com.wj.kindergarten.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfAlbumList;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfFusionFragment;

import java.util.List;

/**
 * Created by tangt on 2016/1/14.
 */
public class PhotoFamilyFragment extends Fragment {
    private TabLayout tab_layout;
    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    private List<PfAlbumListSun> albumList;
    private View view;
    private PfFusionFragment pfFusionFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getActivity()).clearCenterIcon();
        ((MainActivity)getActivity()).setTitleText("家庭相册");
        ((MainActivity)getActivity()).showCenterIcon(((MainActivity) getActivity()).TITLE_CENTER_TYPE_RIGHT, R.drawable.albums_icon_bg);
        ((MainActivity)getActivity()).setTitleRightImage(R.drawable.xiajiatou,0);
        if(view != null) return view;
        view = inflater.inflate(R.layout.photo_family_pic,null);
        initViews(view);
        initClickListener();
        initTabLayout();
        loadPfData();
        return view;
    }

    private void loadPfData() {
        UserRequest.getPfAlbumList(getActivity(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                PfAlbumList pfAlbumList = (PfAlbumList) domain;
                if (pfAlbumList != null && pfAlbumList.getList() != null && pfAlbumList.getList().size() > 0) {
                    albumList = pfAlbumList.getList();
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

    private void initClickListener() {

    }

    private void initViews(View v) {
        tab_layout = (TabLayout)v.findViewById(R.id.common_tab_layout);
        viewPager = (ViewPager)v.findViewById(R.id.common_viewPager);

    }

    String [] titles = new String[]{"时光轴","精品相册","我的收藏"};

    private void initTabLayout() {


        pagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {

                switch (position){
                    case 0 :
                        if(pfFusionFragment == null){
                            pfFusionFragment = new PfFusionFragment();
                        }
                        return pfFusionFragment;
                    case 1 :
                         return new TestFragment();
                    case 2 :
                        return new TestFragment();
                }


                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };

        viewPager.setAdapter(pagerAdapter);
        tab_layout.setupWithViewPager(viewPager);


    }
}
