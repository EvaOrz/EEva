package modernmedia.com.cn.exhibitioncalendar;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import modernmedia.com.cn.exhibitioncalendar.api.UrlMaker;

import static modernmedia.com.cn.corelib.util.ConstData.REQUEST_READ_PHONE_STATE;

/**
 * Created by Eva. on 17/3/14.
 */

public class MyApplication extends Application {

    // 展览日历appid
    public static int APPID = 61;
    public static int DEBUG = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        UrlMaker.setHost();


        //        //创建默认的ImageLoader配置参数
        //        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        //
        //        //Initialize ImageLoader with configuration.
        //        ImageLoader.getInstance().init(configuration);
    }




}
