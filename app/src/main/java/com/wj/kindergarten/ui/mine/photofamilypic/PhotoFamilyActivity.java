package com.wj.kindergarten.ui.mine.photofamilypic;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfAlbumList;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;

import java.util.List;

/**
 * Created by tangt on 2016/1/13.
 */
public class PhotoFamilyActivity extends BaseActivity {
    private RelativeLayout pf_normal_title_left_layout,pf_outLine_cloud,
            pf_normal_title_right_layout;
    private TabLayout tab_layout;
    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    private PfFusionFragment pfFusionFragment;
    private List<PfAlbumListSun> albumList;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.photo_family_pic;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        initViews();
        initClickListener();
        initTabLayout();
        loadPfData();
    }

    private void loadPfData() {
        UserRequest.getPfAlbumList(this, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                  PfAlbumList pfAlbumList = (PfAlbumList) domain;
                  if(pfAlbumList != null && pfAlbumList.getList() != null && pfAlbumList.getList().size() > 0){
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
        pf_normal_title_left_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pf_outLine_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看图片上传进度
            }
        });
        pf_normal_title_right_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动导入相册页面
            }
        });
    }

    private void initViews() {
        pf_normal_title_left_layout = (RelativeLayout)findViewById(R.id.pf_normal_title_left_layout);
        pf_outLine_cloud = (RelativeLayout)findViewById(R.id.pf_outLine_cloud);
        pf_normal_title_right_layout = (RelativeLayout)findViewById(R.id.pf_normal_title_right_layout);
        tab_layout = (TabLayout)findViewById(R.id.common_tab_layout);
        viewPager = (ViewPager)findViewById(R.id.common_viewPager);

    }

    String [] titles = new String[]{"时光轴","精品相册","我的收藏"};

    private void initTabLayout() {
//        tab_layout.addTab(tab_layout.newTab().setText("时光轴"));
//        tab_layout.addTab(tab_layout.newTab().setText("精品相册"));
//        tab_layout.addTab(tab_layout.newTab().setText("我的收藏"));


        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {

//                        if(pfFusionFragment == null){
//                            pfFusionFragment = new PfFusionFragment();
//                        }
                        return new PfFusionFragment();

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
