package modernmedia.com.cn.exhibitioncalendar;

import android.app.Application;

/**
 * Created by Eva. on 17/3/14.
 */

public class MyApplication extends Application {

    // 展览日历appid
    public static int APPID = 21;
    public static int DEBUG = 1;

    @Override
    public void onCreate() {
        super.onCreate();

//        //创建默认的ImageLoader配置参数
//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
//
//        //Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(configuration);
    }
}
