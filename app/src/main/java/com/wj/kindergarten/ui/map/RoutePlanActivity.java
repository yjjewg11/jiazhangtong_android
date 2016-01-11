package com.wj.kindergarten.ui.map;

import android.content.Intent;
import android.support.design.widget.TabLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.GeoPoint;
import com.wj.kindergarten.ui.BaseActivity;

/**
 * Created by tangt on 2016/1/11.
 */
public class RoutePlanActivity extends BaseActivity{

    private GeoPoint st,en;
    private TabLayout tabLayout;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.route_plan_activity;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("查看路线");
        Intent intent = getIntent();
        st = (GeoPoint) intent.getSerializableExtra("st");
        en = (GeoPoint) intent.getSerializableExtra("en");
//        tabLayout.addTab(tabLayout.newTab().);
    }


}
