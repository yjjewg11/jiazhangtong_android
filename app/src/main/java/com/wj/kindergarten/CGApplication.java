package com.wj.kindergarten;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

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
    private List<Address> listAdress ;
    private String latLongString;

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
        LocationManager mLocMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60, 100, mLocLis);
    }


    private LocationListener mLocLis = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("TAG", "打印的是少数少数 onStatusChanged, provider = " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("TAG", "打印的是少数少数 onProviderEnabled, provider = " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("TAG", "打印的是少数少数 onProviderDisabled, provider = " + provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            latitude = (location.getLatitude());
            longitude =( location.getLongitude());
            Log.i("TAG","打印坐标 x : "+ latitude + "   y : "+longitude);
            Geocoder ge = new Geocoder(getInstance());
            String city = null;
            try {
                listAdress = ge.getFromLocation(latitude,longitude,1);
                if(listAdress!=null && listAdress.size()>0){
                    for(int i=0; i<listAdress.size(); i++){
                        Address ad = listAdress.get(i);
                        latLongString +="n";
                        latLongString += ad.getCountryName() +";"+ ad.getLocality();
                        city = (ad.getLocality()).substring(0,2);
                    }

                }
                    final String type = "android";
                    final String mobileVersion = android.os.Build.VERSION.RELEASE;
                    PackageManager manager = getInstance().getPackageManager();
                    PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                    final String appVersion = info.versionName;
                    VersionInfo versionInfo = new VersionInfo(type,mobileVersion,appVersion,city);
                    if(!versionInfo.equals(CGSharedPreference.getVersionInfoReference())){
                        sendVersionInfo(type,mobileVersion,appVersion,city);
                    }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Log.i("TAG", "latitude: " + latitude + ", longitude: " + longitude);
        }
    };


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
}
