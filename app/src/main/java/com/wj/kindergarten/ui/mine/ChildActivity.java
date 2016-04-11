package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.*;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.MineSchoolActivity;
import com.wj.kindergarten.ui.func.adapter.FragmentChildStateAdapter;
import com.wj.kindergarten.ui.more.MyCircleView;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ChildActivity
 *
 * @Description:宝贝详情
 * @Author: Wave
 * @CreateDate: 2015/7/24 1:03
 */
public class ChildActivity extends BaseActivity {
    public static ChildActivity instance;
    private FragmentChildStateAdapter pagerAdapter;

    private ViewPager activity_child_pager;
    private List<ChildInfo> childInfos;
    private LinearLayout activity_child_viewpager_container;
    private int position;
    private MyCircleView mine_child_new_head_circle;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_child_viewpager;
//        layoutId = R.layout.activity_child;
    }

    @Override
    protected void setNeedLoading() {

    }

    String childTag = "noChild";

    @Override
    protected void onCreate() {

        getData();
        activity_child_viewpager_container = (LinearLayout) findViewById(R.id.activity_child_viewpager_container);
        activity_child_pager = (ViewPager) findViewById(R.id.activity_child_mine_viewPager);
        pagerAdapter = new FragmentChildStateAdapter(getSupportFragmentManager(), childInfos);
        activity_child_pager.setAdapter(pagerAdapter);
        mine_child_new_head_circle = (MyCircleView) findViewById(R.id.mine_child_new_head_circle);
        mine_child_new_head_circle.setRadius(10);
        mine_child_new_head_circle.setDistance(40);
        mine_child_new_head_circle.setCount(childInfos.size());

        activity_child_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                mine_child_new_head_circle.setScale(pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (childInfos.size() == 0) {
            activity_child_viewpager_container.removeAllViews();
            View childView = View.inflate(this, R.layout.no_child_view, null);
            childView.setTag(childTag);

            ImageView no_child_view_add_iv = (ImageView) childView.findViewById(R.id.no_child_view_add_iv);
            ImageView no_child_view_img = (ImageView) childView.findViewById(R.id.no_child_view_img);
            TextView no_child_view_tv = (TextView) childView.findViewById(R.id.no_child_view_tv);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ownIntent = new Intent(ChildActivity.this, EditChildActivity.class);
                    startActivityForResult(ownIntent, GloablUtils.OWN);
                }
            };
            no_child_view_img.setOnClickListener(listener);
            no_child_view_tv.setOnClickListener(listener);
            no_child_view_add_iv.setOnClickListener(listener);
            activity_child_viewpager_container.addView(childView);
        }


    }

    private void getData() {
        childInfos = CGApplication.getInstance().getLogin().getList();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data.getBooleanExtra("newData", false)) {
                //如果是没有小孩,进行添加的话,则需要先移除原来的view

                if (activity_child_viewpager_container.findViewWithTag(childTag) != null) {
                    activity_child_viewpager_container.removeAllViews();
                    ViewGroup viewGroup = (ViewGroup) activity_child_pager.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(activity_child_pager);
                    }
                    activity_child_viewpager_container.addView(activity_child_pager);
                }
                pagerAdapter.notifyDataSetChanged();
                sendBroadcast(new Intent(GloablUtils.ADD_NEW_CHILD));
                return;
            }
            ChildInfo childInfo = lookForChildInfo(data.getStringExtra("uuid"));
            switch (requestCode) {
                case GloablUtils.OWN:
                    childInfo.setHeadimg(data.getStringExtra("head"));
                    childInfo.setName(data.getStringExtra("name"));
                    childInfo.setNickname(data.getStringExtra("nick"));
                    childInfo.setSex(data.getIntExtra("sex", 0));
                    childInfo.setBirthday(data.getStringExtra("birth"));
                    childInfo.setIdcard(data.getStringExtra("idCard"));
                    break;
                case GloablUtils.BA:
                    childInfo.setBa_name(data.getStringExtra("name"));
                    childInfo.setBa_tel(data.getStringExtra("tel"));
                    childInfo.setBa_work(data.getStringExtra("work"));
                    break;
                case GloablUtils.MA:
                    childInfo.setMa_name(data.getStringExtra("name"));
                    childInfo.setMa_tel(data.getStringExtra("tel"));
                    childInfo.setMa_work(data.getStringExtra("work"));
                    break;
                case GloablUtils.OTHER:
                    childInfo.setYe_tel(data.getStringExtra("tel1"));
                    childInfo.setNai_tel(data.getStringExtra("tel2"));
                    childInfo.setWaigong_tel(data.getStringExtra("tel3"));
                    childInfo.setWaipo_tel(data.getStringExtra("tel4"));
                    break;
                case GloablUtils.SCHOOL:
                    break;
                case GloablUtils.REMARK:
                    childInfo.setNote(data.getStringExtra("remark"));
                    break;
            }
            notifyData(childInfo);
        }
    }

    private void notifyData(ChildInfo childInfo) {
        int index = childInfos.indexOf(childInfo);
        childInfos.set(index, childInfo);
        pagerAdapter.notifyDataSetChanged();
    }

    private ChildInfo lookForChildInfo(String uuid) {
        Iterator<ChildInfo> iterator = childInfos.iterator();
        while (iterator.hasNext()) {
            ChildInfo i = iterator.next();
            if (uuid.equals(i.getUuid())) {
                return i;
            }
        }
        return null;
    }
}
