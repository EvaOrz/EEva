package cn.com.modernmedia.corelib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.meituan.android.walle.ChannelReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import afinal.FinalBitmap;
import cn.com.modernmedia.corelib.breakpoint.BreakPointUtil;
import cn.com.modernmedia.corelib.breakpoint.DownloadPackageCallBack;

/**
 * /**
 * Created by Eva. on 17/3/23.
 */

public class CommonApplication extends Application {

    /**
     * 文件夹名
     */
    public static String CACHE_FILE_NAME = "calendar";

    // weixin id / secret
    public static String WEIXIN_APP_ID = "";
    public static String WEIXIN_SECRET = "";
    // sina id / secret
    public static String SINA_APP_ID;
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

    public static FinalBitmap finalBitmap;
    /**
     * 登录状态是否改变
     */
    public static boolean loginStatusChange = false;
    public static String installationId;
    private static int memorySize;

    /**
     * 渠道
     */
    public static String CHANNEL = "";

    /**
     * 断点下载类
     */
    public static DownloadPackageCallBack downBack;
    @SuppressLint("UseSparseArrays")
    private static Map<String, BreakPointUtil> breakMap = new HashMap<String, BreakPointUtil>();


    @Override
    public void onCreate() {
        super.onCreate();
        initScreenInfo();
        initChannel();
    }

    /**
     * 初始化渠道号
     */
    public void initChannel() {
        try {
            CHANNEL = ChannelReader.get(new File(getApkPath(this.getApplicationContext()))).getChannel();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(CHANNEL)) {
            CHANNEL = "bbwc";
        }
    }

    @Nullable
    private static String getApkPath(@NonNull final Context context) {
        String apkPath = null;
        try {
            final ApplicationInfo applicationInfo = context.getApplicationInfo();
            if (applicationInfo == null) {
                return null;
            }
            apkPath = applicationInfo.sourceDir;
        } catch (Throwable e) {
        }
        return apkPath;
    }

    /**
     * 初始化页面信息
     */
    public void initScreenInfo() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

    }

    /**
     * 刷新下载进度
     *
     * @param tagName
     */
    public static void notifyDown(String tagName, long complete, long total) {
        if (downBack != null) {
            downBack.onProcess(tagName, complete, total);
        }
    }

    /**
     * 通知开始下载
     *
     * @param tagName
     * @param flag    0:success;1.loading;2.failed;3.pause
     */
    public static void notityDwonload(String tagName, int flag) {
        if (downBack != null) {
            switch (flag) {
                case 0:
                    downBack.onSuccess(tagName, null);
                    break;
                case 1:
                    downBack.onProcess(tagName, -1, -1);
                    break;
                case 2:
                    downBack.onFailed(tagName);
                    break;
                case 3:
                    downBack.onPause(tagName);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 添加往期下载
     *
     * @param breakPointUtil
     */
    public static void addPreIssueDown(String tagName, BreakPointUtil breakPointUtil) {
        breakMap.put(tagName, breakPointUtil);
        removePreIssueDown(tagName);
    }

    private static void removePreIssueDown(String tagName) {
        if (!breakMap.isEmpty()) {
            for (String id : breakMap.keySet()) {
                BreakPointUtil util = breakMap.get(id);
                if (!TextUtils.equals(tagName, id)) {
                    util.pause();
                }
            }
        }
    }
}
