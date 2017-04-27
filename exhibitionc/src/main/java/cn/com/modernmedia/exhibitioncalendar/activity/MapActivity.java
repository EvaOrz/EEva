package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
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
    private String address;

    private MapView baiduMapView;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        calendarModel = (CalendarModel) getIntent().getSerializableExtra("map_calendar");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("latitude")))
            latitude = Double.valueOf(getIntent().getStringExtra("latitude"));
        if (!TextUtils.isEmpty(getIntent().getStringExtra("longitude")))
            longitude = Double.valueOf(getIntent().getStringExtra("longitude"));
        address = getIntent().getStringExtra("map_address");
        initView();


        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
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
        BaiduMap mBaiduMap = baiduMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        BDLocation location = new BDLocation();
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(latitude).longitude(longitude).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

        mCurrentMode = LocationMode.NORMAL;
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfiguration(config);
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);

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


    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.e("Baidu_map", "action: " + s);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Log.e("Baidu_map", "key 验证出错! 错误码 :" + intent.getIntExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0) + " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                Log.e("Baidu_map", "key 验证成功! 功能可以正常使用");
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Log.e("Baidu_map", "网络出错");
            }
        }
    }

    private SDKReceiver mReceiver;


    @Override
    public void onMapReady(GoogleMap map) {
        //        LatLng latLng = new LatLng(latitude, longitude);
        //
        //        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        //        //.snippet("The most populous city in Australia.")
        //        map.addMarker(new MarkerOptions().title(calendarModel.getAddress()).position(latLng));
    }


}
