package cn.com.modernmedia.exhibitioncalendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.view.ChooseMapPopView;

/**
 * 地图导航页面
 * Created by Eva. on 17/4/24.
 */

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private double latitude, longitude;
    private View mapBg;
    private TextView title;
    private CalendarModel calendarModel;

    private MapView baiduMapView;
    private LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private LocationClient mLocationClient;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        calendarModel = (CalendarModel) getIntent().getSerializableExtra("map_calendar");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("latitude")))
            latitude = Double.valueOf(getIntent().getStringExtra("latitude"));
        if (!TextUtils.isEmpty(getIntent().getStringExtra("longitude")))
            longitude = Double.valueOf(getIntent().getStringExtra("longitude"));
        //        address = getIntent().getStringExtra("map_address");


        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        baiduMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        baiduMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        baiduMapView.onPause();
    }

    private void initView() {
        mapBg = findViewById(R.id.map_bg);
        title = (TextView) findViewById(R.id.map_title);
        // 图片太大了
        if (calendarModel != null && !TextUtils.isEmpty(calendarModel.getBackgroundImg())) {
            MyApplication.finalBitmap.display(mapBg, calendarModel.getBackgroundImg());

            title.setText(calendarModel.getTitle());
        }
        findViewById(R.id.map_back).setOnClickListener(this);
        findViewById(R.id.map_daohang).setOnClickListener(this);

        /**
         * google
         */
        //        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapview);
        //        mapFragment.getMapAsync(this);
        /**
         * baidu
         */
        baiduMapView = (MapView) findViewById(R.id.baidu_mapview);
        mBaiduMap = baiduMapView.getMap();

        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                LatLng cenpt = new LatLng(latitude, longitude);
                // 定义地图状态zoom表示缩放级别3-18
                MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(14).build();
                // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                // 改变地图状态
                // 开启定位图层

                mBaiduMap.setMapStatus(mMapStatusUpdate);


                // 定义Maker坐标点
                // 构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.location);
                // 构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(cenpt).icon(bitmap);
                // 在地图上添加Marker，并显示
                mBaiduMap.clear();
                mBaiduMap.addOverlay(option);


                //文字，在地图中也是一种覆盖物，开发者可利用相关的接口，快速实现在地图上书写文字的需求。实现方式如下：
                //定义文字所显示的坐标点
                LatLng llText = new LatLng(latitude, longitude);
                //构建文字Option对象，用于在地图上添加文字
                OverlayOptions textOption = new TextOptions().bgColor(0x00000000).fontSize(28).fontColor(0xFF000000).text(calendarModel.getAddress()).rotate(0).position(llText);
                //在地图上添加该文字对象并显示
                mBaiduMap.addOverlay(textOption);

                mLocationClient.stop();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        // 注册监听函数
        mLocationClient.start();


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:


                    break;
            }
        }
    };


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.map_back:
                finish();
                break;
            case R.id.map_daohang:
                new ChooseMapPopView(MapActivity.this, latitude + "", longitude + "");
                break;
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        //        LatLng latLng = new LatLng(latitude, longitude);
        //
        //        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        //        //.snippet("The most populous city in Australia.")
        //        map.addMarker(new MarkerOptions().title(calendarModel.getAddress()).position(latLng));
    }


}
