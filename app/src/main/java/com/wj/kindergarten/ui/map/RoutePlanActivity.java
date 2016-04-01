package com.wj.kindergarten.ui.map;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.GeoPoint;
import com.wj.kindergarten.bean.MapRouteData;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.ToastUtils;

/**
 * Created by tangt on 2016/1/11.
 */
public class RoutePlanActivity extends BaseActivity {

    private GeoPoint st, en;
    private TabLayout tabLayout;
    private ViewPager map_viewPager;
    private RadioButton[] radioButtons;
    private RadioGroup radioGroup;
//    private RoutePlanSearch mSearch;
//    private PlanNode enNode;
//    private PlanNode stNode;
    private AddressFragment car, bus, walk;

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
        initViews();
        initMaps();
//        stNode = PlanNode.withLocation(new LatLng(st.getLat(), st.getLon()));
//        enNode = PlanNode.withLocation(new LatLng(en.getLat(), en.getLon()));
//        mSearch.drivingSearch((new DrivingRoutePlanOption())
//                .from(stNode).to(enNode));
//        mSearch.transitSearch((new TransitRoutePlanOption())
//                .from(stNode).to(enNode));
    }

    private void initMaps() {
//        mSearch = RoutePlanSearch.newInstance();
//        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
//            @Override
//            public void onGetWalkingRouteResult(WalkingRouteResult result) {
//                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                    ToastUtils.showMessage("抱歉，未找到结果");
//                    return;
//                }
//                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//                    // result.getSuggestAddrInfo()
//                    ToastUtils.showMessage("起点或终点输入有歧义！");
//                    return;
//                }
//                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
////                    walk.setMapRouteData(new MapRouteData(result.getRouteLines()));
//                    CGLog.v("打印推荐路线： " + result.getSuggestAddrInfo());
//                    CGLog.v("打印全部路线 : " + result.getRouteLines());
//                    CGLog.v("打印信息 : " + result.getTaxiInfo());
//                    result.getTaxiInfo();
//                    walk.setRouteList(result.getRouteLines());
//                }
//            }
//
//            @Override
//            public void onGetTransitRouteResult(TransitRouteResult result) {
//                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
////                    walk.setMapRouteData(new MapRouteData(result.getRouteLines()));
//                    CGLog.v("打印公交推荐路线： " + result.getSuggestAddrInfo());
//                    CGLog.v("打印公交全部路线 : " + result.getRouteLines());
////                    result.
//                }
//            }
//
//            @Override
//            public void onGetDrivingRouteResult(DrivingRouteResult result) {
//                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
////                    walk.setMapRouteData(new MapRouteData(result.getRouteLines()));
//                    CGLog.v("打印开车推荐路线： " + result.getSuggestAddrInfo());
//                    CGLog.v("打印开车全部路线 : " + result.getRouteLines());
//                }
//            }
//
////            @Override
////            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
////
////            }
//        });
    }

    private void initViews() {
        map_viewPager = (ViewPager) findViewById(R.id.map_viewPager);
        map_viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        if (car == null) {
                            car = new AddressFragment();
                        }
                        return car;
                    case 1:
                        if (bus == null) {
                            bus = new AddressFragment();
                        }
                        return bus;
                    case 2:
                        if (walk == null) {
                            walk = new AddressFragment();
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                mSearch.walkingSearch((new WalkingRoutePlanOption())
//                                        .from(stNode).to(enNode));
                            }
                        },500);
                        return walk;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        map_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int id = 0;
                switch (position) {
                    case 0:
                        id = R.id.check_car;
                        break;
                    case 1:
                        id = R.id.check_bus;
//                        mSearch.transitSearch((new TransitRoutePlanOption())
//                                .from(stNode).city("北京").to(enNode));
                        break;
                    case 2:
                        id = R.id.check_walk;
//                        mSearch.walkingSearch((new WalkingRoutePlanOption())
//                                .from(stNode).to(enNode));
                        break;
                }
                radioGroup.check(id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.map_radioGroup);
        radioButtons = new RadioButton[]{
                (RadioButton) findViewById(R.id.check_car),
                (RadioButton) findViewById(R.id.check_bus),
                (RadioButton) findViewById(R.id.check_walk),
        };
        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnClickListener(new ClickListeners());
        }
    }

    class ClickListeners implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check_car:
                    map_viewPager.setCurrentItem(0, true);
                    break;
                case R.id.check_bus:
                    map_viewPager.setCurrentItem(1, true);
                    break;
                case R.id.check_walk:
                    map_viewPager.setCurrentItem(2, true);
                    break;
            }
        }
    }

}
