package com.wj.kindergarten;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wenjie.jiazhangtong.R;

import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.bean.Group;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.bean.VersionInfo;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.net.RequestHttpUtil;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JJGApplication
 *
 * @author Wave
 * @data: 2015/5/20
 * @version: v1.0
 */
public class CGApplication extends Application {
    public static CGApplication context = null;

    public static double latitude = -1;
    public static double longitude = -1;

    private Login login = null;
    private Map<String, Group> groupMap = new HashMap<>();
    private Map<String, ChildInfo> childInfoMap = new HashMap<>();
    public static DisplayImageOptions options = null;
    private List<Address> listAdress;
    private String latLongString;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    public String getLatLongString() {
        return latLongString;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        String cachePath = Constants.cachePath;
        ImageLoaderUtil.initImageLoader(this, R.drawable.touxiang, cachePath, 10, 0);
        RequestHttpUtil.initClient();
//        initDirs();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.friends_sends_pictures_no)
                .showImageForEmptyUri(R.drawable.friends_sends_pictures_no)
                .showImageOnFail(R.drawable.friends_sends_pictures_no)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(0)).build();


        requestNetworkLocation();

    }

    public static CGApplication getInstance() {
        return context;
    }

    public Login getLogin() {
        return login;
    }

    public Map<String, Group> getGroupMap() {
        return groupMap;
    }

    public Map<String, ChildInfo> getChildInfoMap() {
        return childInfoMap;
    }

    public void setLogin(Login login) {
        this.login = login;

        groupMap.clear();
        childInfoMap.clear();
        if (login != null) {
            for (Group group : login.getGroup_list()) {
                if (group != null) {
                    groupMap.put(group.getUuid(), group);
                }
            }

            for (ChildInfo childInfo : login.getList()) {
                if (childInfo != null) {
                    childInfoMap.put(childInfo.getClassuuid(), childInfo);
                }
            }
        }
    }

    private void initDirs() {
        File parentDir = new File(Constants.parentPath);
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }

        File cacheDir = new File(Constants.cachePath);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
    }

    private void requestNetworkLocation() {

        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();
    }


    private void sendVersionInfo(final String type, final String mobileVersion, final String appVersion, final String city) throws PackageManager.NameNotFoundException {

        UserRequest.sendVersion(this, type, mobileVersion, appVersion, city, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                VersionInfo versionInfo = new VersionInfo(type, mobileVersion, appVersion, city);
                CGSharedPreference.setVersionInfoReference(versionInfo);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.s(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            latitude = (bdLocation.getLatitude());
            longitude = (bdLocation.getLongitude());
            Log.i("TAG", "打印坐标 x : " + latitude + "   y : " + longitude);
            Geocoder ge = new Geocoder(getInstance());
            String city = null;
            try {
                listAdress = ge.getFromLocation(latitude, longitude, 1);
                if (listAdress != null && listAdress.size() > 0) {
                    for (int i = 0; i < listAdress.size(); i++) {
                        Address ad = listAdress.get(i);
                        latLongString += "n";
                        latLongString += ad.getCountryName() + ";" + ad.getLocality();
                        if(!TextUtils.isEmpty(ad.getLocality())){
                            city = (ad.getLocality()).substring(0, 2);
                        }

                    }

                }

                final String type = "android";
                final String mobileVersion = android.os.Build.VERSION.RELEASE;
                PackageManager manager = getInstance().getPackageManager();
                PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                final String appVersion = info.versionName;
                if(TextUtils.isEmpty(city)){
                    latitude = -1;
                    longitude = -1;
                    return ;
                }
                VersionInfo versionInfo = new VersionInfo(type, mobileVersion, appVersion, city);
                if (!versionInfo.equals(CGSharedPreference.getVersionInfoReference())) {
                    sendVersionInfo(type, mobileVersion, appVersion, city);
                }
                mLocationClient.stop();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


