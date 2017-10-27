package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.view.ChooseMapPopView;

/**
 * 地图导航页面
 * Created by Eva. on 17/4/24.
 */

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private double latitude, longitude;
    private View mapBg;
    private TextView title, look;
    private CalendarModel calendarModel;// 初始展览model
    private CalendarModel currentCalendarModel;// 当前选中model
    private MapView baiduMapView;
    private LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private LocationClient mLocationClient;
    private BaiduMap mBaiduMap;

    private boolean ifMapReady = false;

    private CalendarListModel nearListModel;// 周边展览列表
    private BitmapDescriptor markerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        markerIcon = BitmapDescriptorFactory.fromResource(R.mipmap.location);

        if (getIntent().getSerializableExtra("map_calendar") != null &&getIntent()
                .getSerializableExtra("map_calendar") instanceof CalendarModel ) {
            calendarModel = (CalendarModel) getIntent().getSerializableExtra("map_calendar");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("latitude")))
            latitude = Double.valueOf(getIntent().getStringExtra("latitude"));
        if (!TextUtils.isEmpty(getIntent().getStringExtra("longitude")))
            longitude = Double.valueOf(getIntent().getStringExtra("longitude"));

        //        if (!TextUtils.isEmpty(getIntent().getStringExtra("map_address")))
        //            address = getIntent().getStringExtra("map_address");
        initView();
        initAllMarker();

    }

    /**
     * 获取周边展览信息
     */
    private void initAllMarker() {
        ApiController.getInstance(this).getNearList(this, latitude + "", longitude + "", new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    nearListModel = (CalendarListModel) entry;
                    handler.sendEmptyMessage(0);
                }
            }
        });
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
        look = (TextView) findViewById(R.id.look_detail);
        look.setOnClickListener(this);

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
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                ifMapReady = false;// map在变化
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                ifMapReady = true;//map 加载完成
            }
        });
        initClick();

        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                addOne(calendarModel, latitude, longitude, true);
                mLocationClient.stop();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        // 注册监听函数
        mLocationClient.start();
    }

    /**
     * 添加单个marker
     *
     * @param calendarModel
     * @param ifCenter
     */
    private void addOne(CalendarModel calendarModel, boolean ifCenter) {
        if (calendarModel == null) return;
        CalendarModel.Coordinate cc = calendarModel.getCoordinates();
        if (cc != null && !TextUtils.isEmpty(cc.getLatitude()) && !TextUtils.isEmpty(cc.getLongitude()))
            addOne(calendarModel, Double.valueOf(cc.getLatitude()), Double.valueOf(cc.getLongitude()), ifCenter);
    }

    /**
     * 添加单个marker
     *
     * @param ifCenter
     */
    private void addOne(CalendarModel c, Double la, Double lo, boolean ifCenter) {
        Log.e("add one ", la + "_____" + lo);
        LatLng cenpt = new LatLng(la, lo);

        if (ifCenter) {

            // 定义地图状态zoom表示缩放级别3-18
            MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(14).build();
            // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            // 改变地图状态
            // 开启定位图层
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }

        // 定义Maker坐标点
        // 构建Marker图标

        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().icon(markerIcon).position(cenpt);
        // 在地图上添加Marker，并显示
        Bundle bundle = new Bundle();
        bundle.putSerializable("calendarmodel", c);
        mBaiduMap.addOverlay(option).setExtraInfo(bundle);
        if (ifCenter) {
            Message m = new Message();
            m.what = 3;
            m.obj = (Marker) mBaiduMap.addOverlay(option);
            handler.sendMessage(m);
        }

        //                //文字，在地图中也是一种覆盖物，开发者可利用相关的接口，快速实现在地图上书写文字的需求。实现方式如下：
        //                //定义文字所显示的坐标点
        //                LatLng llText = new LatLng(latitude, longitude);
        //                //构建文字Option对象，用于在地图上添加文字
        //                OverlayOptions textOption = new TextOptions().bgColor(0x00000000).fontSize(28).fontColor(0xFF000000).text(calendarModel.getAddress()).rotate(0).position(llText);
        //                //在地图上添加该文字对象并显示
        //                mBaiduMap.addOverlay(textOption);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// 添加周边
                    for (CalendarModel c : nearListModel.getCalendarModels()) {
                        if (c != null && ParseUtil.listNotNull(nearListModel.getCalendarModels())) {

                            addOne(c, false);
                        }
                    }

                    break;
                case 1:// 不显示
                    look.setVisibility(View.GONE);

                    break;
                case 2:// 显示
                    look.setVisibility(View.VISIBLE);

                    break;

                case 3:

                    Marker m = (Marker) msg.obj;

                    initPop(m, calendarModel.getAddress());
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
                new ChooseMapPopView(MapActivity.this, latitude + "", longitude + "", calendarModel.getAddress());
                break;

            case R.id.look_detail:
                if (currentCalendarModel != null) {
                    Intent i = new Intent(MapActivity.this, CalendarDetailActivity.class);
                    i.putExtra("calendar_detail", currentCalendarModel);
                    startActivity(i);
                }
                break;
        }
    }

    /**
     * 设置marker点击事件
     */
    private void initClick() {

        BaiduMap.OnMarkerClickListener mMarkerlis = new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                CalendarModel info = (CalendarModel) marker.getExtraInfo().get("calendarmodel");
                if (info != null) {
                    /**
                     * 当前展览不显示查看详情
                     */
                    if (calendarModel.getItemId().equals(info.getItemId())) {
                        handler.sendEmptyMessage(1);
                    } else {
                        currentCalendarModel = info;
                        handler.sendEmptyMessage(2);
                    }
                    initPop(marker, info.getAddress());
                }
                return true;
            }

        };
        mBaiduMap.setOnMarkerClickListener(mMarkerlis);
    }


    private void initPop(Marker marker, String cc) {
//        if (!ifMapReady) return;
        // 创建InfoWindow展示的view
        View popup = View.inflate(this, R.layout.view_marker_pop, null);
        TextView title = (TextView) popup.findViewById(R.id.marker_pop);
        title.setText(cc);

        //将marker所在的经纬度的信息转化成屏幕上的坐标
        final LatLng ll = marker.getPosition();
        Point p = mBaiduMap.getProjection().toScreenLocation(ll);
        p.y -= 70;
        LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
        //为弹出的InfoWindow添加点击事件

        // 创建InfoWindow
        InfoWindow mInfoWindow = new InfoWindow(popup, llInfo, 0);
        // 显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);

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
