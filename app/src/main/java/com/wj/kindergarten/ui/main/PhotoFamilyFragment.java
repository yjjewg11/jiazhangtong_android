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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfAlbumList;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfFragmentLinearLayout;
import com.wj.kindergarten.ui.mine.photofamilypic.PfFusionFragment;
import com.wj.kindergarten.utils.Utils;

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
    private FrameLayout back_pf_scroll_fl;
    private PfFragmentLinearLayout pf_back_ll;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initHead();
        if (view != null) return view;
        view = inflater.inflate(R.layout.photo_family_pic, null);
        initViews(view);
        initClickListener();
        initTabLayout();
        loadPfData();
        return view;
    }

    private void initHead() {
        ((MainActivity) getActivity()).clearCenterIcon();
        ((MainActivity) getActivity()).setTitleText("家庭相册");
        ((MainActivity) getActivity()).showLeftButton(R.drawable.hanbao_left);
        ((MainActivity) getActivity()).setTitleRightImage(R.drawable.new_album_carema, 0);
        ((MainActivity) getActivity()).titleLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出菜单
                View view = View.inflate(getActivity(), R.layout.pf_left_choose, null);
                TextView tv_collect = (TextView) view.findViewById(R.id.tv_head_collect);
                TextView tv_up_list = (TextView) view.findViewById(R.id.tv_up_list);
                tv_collect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                tv_up_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                Utils.setPopWindow(popupWindow);
                popupWindow.showAsDropDown(((MainActivity) getActivity()).titleLeftButton);
            }
        });
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
        tab_layout = (TabLayout) v.findViewById(R.id.common_tab_layout);
        viewPager = (ViewPager) v.findViewById(R.id.common_viewPager);
        back_pf_scroll_fl = (FrameLayout) v.findViewById(R.id.back_pf_scroll_fl);
        pf_back_ll = (PfFragmentLinearLayout) v.findViewById(R.id.pf_back_ll);
        pf_back_ll.setJudgeIsVisible(new PfFragmentLinearLayout.JudgeIsVisible() {
            @Override
            public boolean isVisible() {
                return back_pf_scroll_fl.isShown();
            }

            @Override
            public void notVisible() {
                switch (viewPager.getCurrentItem()){
                    case 0 :
                        pfFusionFragment.banScroll();
                        break;
                    case 1 :

                        break;
                }
            }

            @Override
            public void alwaysVisible() {
                switch (viewPager.getCurrentItem()){
                    case 0 :
                        pfFusionFragment.allowScroll();
                        break;
                    case 1 :

                        break;
                }
            }

            @Override
            public float getHeight() {
                return back_pf_scroll_fl.getHeight();
            }
        });

    }

    String[] titles = new String[]{"时光轴", "精品相册"};

    private void initTabLayout() {


        pagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        if (pfFusionFragment == null) {
                            pfFusionFragment = new PfFusionFragment();
                        }
                        return pfFusionFragment;
                    case 1:
                        return new TestFragment();
                    case 2:
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
