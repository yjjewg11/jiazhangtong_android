package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.bean.SingleNewInfo;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.PfPopPicAdapter;
import com.wj.kindergarten.ui.func.adapter.RecycleAdapter;
import com.wj.kindergarten.ui.imagescan.AutoDownLoadListener;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.pffragment.PfInfoAllPIcFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.pffragment.PfSingleInfoFragment;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/14.
 */
public class PfGalleryActivity extends BaseActivity {


    private FrameLayout pf_gallery_new_layout_fl;
    private String[] tags = new String[]{

            "album_list",//fragment放置照片集合
            "single_pf"//放置单张照片详情
    };
    private ArrayList<AllPfAlbumSunObject> objectList;
    private int position;
    private ArrayList<QueryGroupCount> queryGroupCounts;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.pf_gallery_activity_new_layout;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("选择照片");
        getData();
        initViews();
        initFragment();
    }

    private void initFragment() {
        PfSingleInfoFragment fragment = new PfSingleInfoFragment(position, objectList);
        getSupportFragmentManager().beginTransaction().replace(R.id.pf_gallery_new_layout_fl, fragment).commit();
    }

    private void initViews() {
        pf_gallery_new_layout_fl = (FrameLayout) findViewById(R.id.pf_gallery_new_layout_fl);
    }


    public void getData() {
        position = getIntent().getIntExtra("position", 0);
        objectList = (ArrayList<AllPfAlbumSunObject>) getIntent().getSerializableExtra("list");
        queryGroupCounts = (ArrayList<QueryGroupCount>) getIntent().getSerializableExtra("countList");
    }

    public void changeToSingleFragment(int position, List<AllPfAlbumSunObject> objectList) {
        PfSingleInfoFragment fragment = (PfSingleInfoFragment) getSupportFragmentManager().findFragmentByTag(tags[1]);
        if (fragment == null) {
            fragment = new PfSingleInfoFragment(position, objectList);
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.pf_gallery_new_layout_fl, fragment, tags[1]).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tags[0]);
            if (fragment != null && fragment.getUserVisibleHint()) {
                finish();
            } else {
                //此时在详情页面，返回进入列表页面
                Fragment album_fagment = getSupportFragmentManager().findFragmentByTag(tags[0]);
                if (album_fagment == null) {
                    album_fagment = new PfInfoAllPIcFragment(queryGroupCounts);
                }
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.pf_gallery_new_layout_fl, album_fagment, tags[0])
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }
            return true;
        }
        return false;
    }
}
