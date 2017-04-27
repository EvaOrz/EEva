package cn.com.modernmedia.exhibitioncalendar;

import android.app.ActivityManager;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import afinal.FinalBitmap;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.util.ConstData;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;

/**
 * Created by Eva. on 17/3/14.
 */

public class MyApplication extends CommonApplication {

    // 展览日历appid
    public static int APPID = 61;
    public static int DEBUG = 0;

    private static int memorySize;

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
        super.onCreate();
        mContext = this;
        drawCls = R.drawable.class;
        stringCls = R.string.class;

        UrlMaker.setHost();
        initMemorySize();

        ConstData.setAppId(APPID);
        WEIXIN_APP_ID = "b682346e1e6b28dba3a47079";
        WEIXIN_SECRET = "26628cd60eeaf05037622770";
        SINA_APP_ID = "3608123411";
        SINA_SECRET = "895c3716c902def304f1bf3c5af5900f";

        SDKInitializer.initialize(getApplicationContext());

    }

    private void initMemorySize() {
        final int memoryClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        memorySize = memoryClass * 1024 * 1024 / 8;
        initImageLoader(mContext);
    }


}
