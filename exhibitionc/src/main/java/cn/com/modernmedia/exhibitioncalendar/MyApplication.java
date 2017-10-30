package cn.com.modernmedia.exhibitioncalendar;

import android.app.ActivityManager;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import afinal.FinalBitmap;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.util.ConstData;
import cn.com.modernmedia.exhibitioncalendar.activity.MuseumDetailActivity;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;

/**
 * Created by Eva. on 17/3/14.
 */

public class MyApplication extends CommonApplication {


    private static int memorySize;
    public static LocationClient mLocationClient;//定位SDK的核心类

    public static MuseumDetailActivity museumDetailActivity;


    /**
     * 初始化整个app的配置
     *
     * @param context
     */
    private static void initImageLoader(Context context) {
        finalBitmap = FinalBitmap.create(context); // 初始化
        finalBitmap.configBitmapLoadThreadSize(3);// 定义线程数量

        finalBitmap.configMemoryCacheSize(memorySize);// 设置缓存大小
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                //.memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory().tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onCreate() {

        mContext = this;
        drawCls = R.mipmap.class;
        stringCls = R.string.class;

        initMemorySize();
        APPID = 61;
        DEBUG = 1;
        UrlMaker.setHost();
        ConstData.setAppId(APPID);
        WEIXIN_APP_ID = "wx9320801de5f7e77a";
        WEIXIN_SECRET = "f23486b3fa95b337be103cd5edbf92b7";
        WEIXIN_PARTNER_ID = "1459768302";

        SINA_APP_ID = "3608123411";
        SINA_SECRET = "895c3716c902def304f1bf3c5af5900f";

        SDKInitializer.initialize(getApplicationContext());

        /**
         * 百度定位client
         */
        mLocationClient = new LocationClient(this.getApplicationContext());

        super.onCreate();
    }

    private void initMemorySize() {
        final int memoryClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        memorySize = memoryClass * 1024 * 1024 / 8;
        initImageLoader(mContext);
    }

    /**
     * 实现实位回调监听
     */
    public static BDLocationListener locationListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            AppValue.currentLocation = bdLocation.getCity();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    };


}
