package com.wj.kindergarten.ui.map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;

/**
 * Created by tangt on 2015/12/31.
 */
public class MapActivity extends ActionBarActivity {
    private MapView mMapView;
    private MapTransportObject mapTransportObject;
    private double lat;
    private double lon;
    private BaiduMap mBaiduMap;
    private Bitmap bitmapReal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.map_activity);
        mapTransportObject = (MapTransportObject)getIntent().getSerializableExtra("map_transport_object");
        if(mapTransportObject != null){
            getMapPoint(mapTransportObject);
        }
        LatLng latLng = new LatLng(lat,lon);
        mMapView = new MapView(this,
                new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                        .target(latLng).zoom(100).build()));
        mBaiduMap = mMapView.getMap();
//构建Marker图标
        BitmapDescriptor bitmap = null;
        if(bitmap != null){
           bitmap =  BitmapDescriptorFactory
                    .fromBitmap(bitmapReal);
        }else{
           bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location_appoint_point);
        }
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);

//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        setContentView(mMapView);
    }

    private void getMapPoint(MapTransportObject mapTransportObject) {
        String mapPoint =  mapTransportObject.getMap_point();
        if(!TextUtils.isEmpty(mapPoint) && mapPoint != null){
           String [] arrays  =  mapPoint.split(",");
            lon = Double.valueOf(arrays[0]);
            lat = Double.valueOf(arrays[1]);
        }
        bitmapReal =  mapTransportObject.getBitmap();
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
