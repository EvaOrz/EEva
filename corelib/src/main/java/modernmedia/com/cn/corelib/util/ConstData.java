package modernmedia.com.cn.corelib.util;

/**
 * 常量类
 * Created by Eva. on 17/3/14.
 */

public class ConstData {

    /**
     * 接口版本号
     **/
    public static String API_VERSION = "9";
    /**
     * 设备号
     **/
    public static String DEVICE_TYPE = "10";
    /**
     * 数据类型（1 protobuf格式 2 json格式）
     **/
    public static final String DATA_TYPE = "2";

    /**
     * 版本号
     **/
    public static final int VERSION = 100;
    /**
     * splash停留时间
     **/
    public static int SPLASH_DELAY_TIME = 1000;
    /**
     * 读取数据buffer大小
     **/
    public static final int BUFFERSIZE = 1024;

    /**
     * 连接超时时间
     **/
    public static final int CONNECT_TIMEOUT = 10 * 1000;
    /**
     * 读取超时时间
     **/
    public static final int READ_TIMEOUT = 10 * 1000;

    /**
     * 广告更新时间
     */
    public static String advUpdateTime = "";
    /**
     * 文件保存地址
     **/
    public static String DEFAULT_DATA_PATH = "/artCalendar/datas/";
    public static String DEFAULT_APK_PATH = "/artCalendar/apk/";
    public static String DEFAULT_PACKAGE_PATH = "/artCalendar/package/";
    public static String DEFAULT_TEMPLATE_PATH = "/artCalendar/templates/";
    public static String DEFAULT_IMAGE_PATH = "/artCalendar/images/";
    /**
     * crash
     */
    public static final String CRASH_NAME = "crash";

    public static String getAdvUpdateTime() {
        return advUpdateTime;
    }

    public static final int REQUEST_READ_PHONE_STATE = 1;

}
