package modernmedia.com.cn.exhibitioncalendar;

import android.app.ActivityManager;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import afinal.FinalBitmap;
import modernmedia.com.cn.corelib.CommonApplication;
import modernmedia.com.cn.exhibitioncalendar.api.UrlMaker;

/**
 * Created by Eva. on 17/3/14.
 */

public class MyApplication extends CommonApplication {

    // 展览日历appid
    public static int APPID = 61;
    public static int DEBUG = 0;

    private static int memorySize;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        drawCls = R.drawable.class;
        stringCls = R.string.class;

        UrlMaker.setHost();
        initMemorySize();
    }


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

    private void initMemorySize() {
        final int memoryClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        memorySize = memoryClass * 1024 * 1024 / 8;
        initImageLoader(mContext);
    }


}
