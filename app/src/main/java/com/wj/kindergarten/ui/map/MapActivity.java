package com.wj.kindergarten.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.GeoPoint;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

/**
 * Created by tangt on 2015/12/31.
 */
public class MapActivity extends ActionBarActivity {
    private MapView mMapView;
    private MapTransportObject mapTransportObject;
    private double lat;
    private double lon;
    private BaiduMap mBaiduMap;
    private String path;
    private TextView tv_map_school_name,tv_map_address,report_mistake,look_route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        mapTransportObject = (MapTransportObject)getIntent().getSerializableExtra("map_transport_object");
        if(mapTransportObject != null){
            getMapPoint(mapTransportObject);
        }
        initViews();
        initMap();

//        setContentView(mMapView);

        //添加显示

        initData();
    }

    private void initMap() {
        mBaiduMap.setMyLocationEnabled(true);
// 构造定位数据
        LatLng latLng = locatorPosition();
//        mMapView = new MapView(this,
//                new BaiduMapOptions().mapStatus(new MapStatus.Builder()
//                        .target(latLng).zoom(100).build()));

//构建Marker图标
        BitmapDescriptor bitmap = null;
        if(!TextUtils.isEmpty(path) && ImageLoaderUtil.getImageFromCache(path) != null
                && !ImageLoaderUtil.getImageFromCache(path).isRecycled()){
           bitmap =  BitmapDescriptorFactory
                    .fromBitmap(ImageLoaderUtil.getImageFromCache(path));
        }else{
           bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location_appoint_point);
        }
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);

//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    private void initData() {
        if(mapTransportObject != null){
            tv_map_address.setText(Utils.isNull(mapTransportObject.getAddress()));
            tv_map_school_name.setText(Utils.isNull(mapTransportObject.getName()));
        }
        look_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   //启动路线规划图
                Intent intent = new Intent(MapActivity.this,RoutePlanActivity.class);
                intent.putExtra("st",new GeoPoint(CGApplication.latitude,CGApplication.longitude));
                intent.putExtra("en",new GeoPoint(lat,lon));
                MapActivity.this.startActivity(intent);
            }
        });
    }

    private void initViews() {
        mMapView = (MapView)findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        tv_map_address = (TextView)findViewById(R.id.tv_map_address);
        tv_map_school_name = (TextView)findViewById(R.id.tv_map_school_name);
        look_route = (TextView)findViewById(R.id.look_route);
        report_mistake = (TextView)findViewById(R.id.report_mistake);
    }

    @NonNull
    private LatLng locatorPosition() {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(360)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(lat)
                .longitude(lon).build();
// 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        mBaiduMap.setMaxAndMinZoomLevel(100,50);
        LatLng latLng = new LatLng(lat,lon);
//通知地图状态改变，更新数据
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(latLng, 30);
        mBaiduMap.setMapStatus(update);
        return latLng;
    }

    private void getMapPoint(MapTransportObject mapTransportObject) {
        String mapPoint =  mapTransportObject.getMap_point();
        if(!TextUtils.isEmpty(mapPoint) && mapPoint != null){
           String [] arrays  =  mapPoint.split(",");
            lon = Double.valueOf(arrays[0]);
            lat = Double.valueOf(arrays[1]);
        }
        path =  mapTransportObject.getPath();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
    }


    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理

    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理

    }
}
