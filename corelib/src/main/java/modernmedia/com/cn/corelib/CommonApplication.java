package modernmedia.com.cn.corelib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eva. on 17/3/23.
 */

public class CommonApplication extends Application {

    /**
     * 文件夹名
     */
    public static String CACHE_FILE_NAME = "";

    // weixin id / secret
    public static String WEIXIN_APP_ID = "";
    public static String WEIXIN_SECRET = "";
    // sina id / secret
    public static String SINA_APP_ID = "";
    public static String SINA_SECRET = "";
    // qq id / secret
    public static String QQ_APP_ID = "";
    public static String QQ_SECRET = "";

    public static int width;
    public static int height;
    public static Context mContext;

    public static Class<?> drawCls;// 图片资源class
    public static Class<?> stringCls;// string资源class
    public static Class<?> idsCls;// id资源class
    public static Map<String, Activity> activityMap = new HashMap<String, Activity>();
    /**
     * 登录状态是否改变
     */
    public static boolean loginStatusChange = false;
    public static String installationId;
    private static int memorySize;



    @Override
    public void onCreate() {
        super.onCreate();
    }
}
