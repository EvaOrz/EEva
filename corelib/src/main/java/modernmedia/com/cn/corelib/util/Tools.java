package modernmedia.com.cn.corelib.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.CommonApplication;
import modernmedia.com.cn.corelib.R;
import modernmedia.com.cn.corelib.db.DataHelper;
import modernmedia.com.cn.corelib.listener.ImageDownloadStateListener;
import modernmedia.com.cn.corelib.model.UserModel;

import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static android.widget.ImageView.ScaleType.CENTER_INSIDE;
import static android.widget.ImageView.ScaleType.FIT_CENTER;
import static android.widget.ImageView.ScaleType.FIT_END;
import static android.widget.ImageView.ScaleType.FIT_START;
import static android.widget.ImageView.ScaleType.MATRIX;

/**
 * 工具类
 * Created by Eva. on 16/9/16.
 */
public class Tools {
    public static final int ID_ERROR = -1;
    public static final int ID_COLOR = -2;
    public static final int ID_HTTP = -3;
    public static final int ID_GIF = -4;
    public static final int STROKE = 2;// 边框宽度

    public static final int REQUEST_ZOOM = 111;
    private static Handler handler = new Handler();


    public static void transforCircleBitmap(Context context, int resId, ImageView imageView) {
        transforCircleBitmap(BitmapFactory.decodeResource(context.getResources(), resId), imageView);
    }

    /**
     * 把图片转换成圆形
     *
     * @param bitmap
     * @return
     */
    public static void transforCircleBitmap(Bitmap bitmap, ImageView imageView) {
        if (bitmap == null) return;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 取小的
        int radius = width > height ? height / 2 : width / 2;
        Bitmap output = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        int left = 0, top = 0;
        int right = width, bottom = height;
        if (width > height) {
            left = width / 2 - radius;
            right = width / 2 + radius;
        } else if (width < height) {
            top = height / 2 - radius;
            bottom = height / 2 + radius;
        }
        Rect src = new Rect(left, top, right, bottom);// 截取原始图片的地方
        Rect dst = new Rect(0, 0, 2 * radius, 2 * radius);

        paint.setAntiAlias(true);
        canvas.drawCircle(radius, radius, radius - STROKE, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, src, dst, paint);

        setCircle(imageView, output, canvas, radius);
    }

    /**
     * 画边框
     *
     * @param imageView
     * @param output
     * @param canvas
     * @param radius
     */
    private static void setCircle(ImageView imageView, Bitmap output, Canvas canvas, int radius) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE);
        paint.setColor(Color.parseColor("#FFCCCCCC"));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawCircle(radius, radius, radius - STROKE, paint);
        imageView.setImageBitmap(output);
    }

    public static String getStringToDate(String timeStr) {
        long t = Long.parseLong(timeStr) * 1000;
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String start = format.format(new Date(t));
        return start;
    }


    /**
     * 判断用户名、密码是否为空
     *
     * @param str
     * @param view
     * @return
     */
    public static boolean checkString(String str, View view, Animation animation) {
        if (TextUtils.isEmpty(str)) {
            Tools.doAnim(view, animation);
            return false;
        }
        return true;
    }

    /**
     * 判断密码 ，不能少于6位
     *
     * @param str
     * @param view
     * @param animation
     * @return
     */

    public static boolean checkPwd(Context c, String str, View view, Animation animation) {
        if (TextUtils.isEmpty(str) || str.length() < 6) {
            Tools.doAnim(view, animation);
            //            showToast(c, R.string.password_hint);
            return false;
        }
        return true;
    }

    /**
     * 获取直播状态
     * type 1:已结束 2：直播中 3：未开始
     */
    public static int getEventStatus(String startTime, String endTime) {
        long now = System.currentTimeMillis() / 1000;
        long s = Long.valueOf(startTime);
        long e = Long.valueOf(endTime);
        if (now < s) {
            return 3;
        } else if (s < now && now < e) {
            return 2;
        } else if (now > e) {
            return 1;
        }
        return 1;
    }

    /**
     * 进入系统的图片裁剪页面
     *
     * @param activity
     * @param uri
     * @param path     图片保存路径
     */
    public static void startPhotoZoom(final Activity activity, final Uri uri, final String path) {
        if (uri == null || TextUtils.isEmpty(path)) return;
        Log.e("剪裁", uri.toString() + "   " + path);
        // TODO 防止当拍完照立马剪裁，内存被系统回收导致不执行接下来的方法，放在handler里
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent("com.android.activity_camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 64);
                intent.putExtra("outputY", 64);
                intent.putExtra("scale", true);// 黑边
                intent.putExtra("scaleUpIfNeeded", true);// 黑边
                intent.putExtra("return-data", true);
                intent.putExtra("output", Uri.fromFile(new File(path))); // 保存路径
                activity.startActivityForResult(intent, REQUEST_ZOOM);
            }
        }, 500);
    }


    /**
     * 格式化日期
     *
     * @param time    时间（毫秒）
     * @param pattern 日期格式
     * @return
     */
    public static String format(long time, String pattern) {
        if (TextUtils.isEmpty(pattern)) return "";
        pattern = pattern.replace("@n", "\n");
        try {
            SimpleDateFormat format;
            //            if (TextUtils.equals("english", language)) {
            //                format = new SimpleDateFormat(pattern, Locale.ENGLISH);
            //            } else {
            format = new SimpleDateFormat(pattern);
            //            }
            return format.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 执行动画
     *
     * @param view
     * @param animation
     */
    private static void doAnim(View view, Animation animation) {
        view.startAnimation(animation);
    }


    public static <T> boolean listNotNull(List<T> list) {
        return list != null && !list.isEmpty();
    }


    public static boolean isAppBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.e("后台运行", appProcess.processName);
                    return true;
                } else {
                    Log.e("前台运行", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 返回手机唯一标识
     *
     * @return
     */
    public static String getMyUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imie = "" + tm.getDeviceId();
        String tmSerial = "" + tm.getSimSerialNumber();
        String androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) imie.hashCode() << 32) | tmSerial.hashCode());
        return MD5.MD5Encode(deviceUuid.toString());
    }

    /**
     * 判断当前手机是否有ROOT权限
     *
     * @return
     */
    public static boolean isRooted() {
        boolean bool = false;

        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else bool = true;
        } catch (Exception e) {
        }
        return bool;
    }

    /**
     * 获取应用版本信息
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            final PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return null;
    }

    /**
     * 显示toast
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).showToast(resId);
        }
    }

    /**
     * 显示toast
     *
     * @param context
     * @param resStr
     */
    public static void showToast(Context context, String resStr) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).showToast(resStr);
        }
    }

    /**
     * 获取要添加的头部信息
     *
     * @return
     */
    public static Map<String, String> getRequastHeader(Context context) {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-Slate-UserId", DataHelper.getUid(context));
        headerMap.put("X-Slate-DeviceId", DataHelper.getUUID(context));
        //        headerMap.put("X-Slate-AppId", ConstData.getInitialAppId() + "");
        headerMap.put("X-SLATE-JAILBROKEN", Tools.isRooted() ? "11" : "10");//是否root（如果可以获取就获取）

        headerMap.put("X-SLATE-CLIENTTYPE", "android");
        headerMap.put("X-SLATE-CLIENTVERSION", Tools.getAppVersion(context));
        return headerMap;
    }

    public static String parseString(Context context, int resId, Object... args) {
        return String.format(context.getString(resId), args);
    }

    /**
     * 检测网络状态
     *
     * @param context
     * @return
     */
    public static boolean checkNetWork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        /* 网络连接状态 */
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * 设置图片的scale type,默认为fix_xy
     *
     * @param iv
     * @param scale_type
     */
    public static void setScaleType(ImageView iv, String scale_type) {
        if (!TextUtils.isEmpty(scale_type)) {
            if (scale_type.equals(CENTER_CROP)) {
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else if (scale_type.equals(FIT_CENTER)) {
                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else if (scale_type.equals(CENTER_INSIDE)) {
                iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else if (scale_type.equals(FIT_END)) {
                iv.setScaleType(ImageView.ScaleType.FIT_END);
            } else if (scale_type.equals(FIT_START)) {
                iv.setScaleType(ImageView.ScaleType.FIT_START);
            } else if (scale_type.equals(MATRIX)) {
                iv.setScaleType(ImageView.ScaleType.MATRIX);
            } else {
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        } else {
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    /**
     * 检查输入的数据是否合法,当前检查数据是否是邮箱形式（手机号码）及是否为null或者空
     *
     * @param data 账号
     * @return true:账号格式有效; false:给出相应的错误提示
     */
    public static boolean checkIsEmailOrPhone(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            String text = context.getString(R.string.msg_login_email_null);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean ifEmail = true, ifPhone = true;
        data = data.trim();
        // 检查账号是否是邮箱格式
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]" + "{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            ifEmail = false;
        }
        // 检查账号是否是电话号码格式
        ifPhone = checkIsPhone(context, data);

        if (!(ifEmail || ifPhone)) {
            String text = context.getString(R.string.msg_login_email_error);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }

        return ifEmail || ifPhone;
    }


    public static boolean checkIsEmail(Context context, String data) {
        if (TextUtils.isEmpty(data)) {
            String text = context.getString(R.string.msg_login_email_null);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean ifEmail = true;
        data = data.trim();
        // 检查账号是否是邮箱格式
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]" + "{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            ifEmail = false;
        }
        return ifEmail;
    }

    /**
     * 检查输入的数据是否合法,当前检查数据是否是手机号码
     *
     * @param data 账号
     * @return true:账号格式有效; false:给出相应的错误提示
     */
    public static boolean checkIsPhone(Context context, String data) {
        //        String str1 = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        //        Pattern p = Pattern.compile(str1);
        //        Matcher m = p.matcher(data);
        //        if (!m.matches()) {
        //            return false;
        //        }
        if (data.length() == 11 && !data.contains("@") && !(Pattern.compile("[a-zA-z]").matcher(data).find()))
            return true;
        else return false;

    }

    /**
     * 检查输入的密码是否合法,当前只检查是否为null或者空
     *
     * @param password 密码
     * @return true:密码格式有效; false:给出相应的错误提示
     */
    public static boolean checkPasswordFormat(Context context, String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, R.string.msg_login_pwd_null, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 获取设备ID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 设置头像
     *
     * @param user
     * @param avatar
     */
    public static void setAvatar(Context context, UserModel user, ImageView avatar) {
        if (user != null) {
            setAvatar(context, user.getAvatar(), avatar);
        } else {
            setAvatar(context, "", avatar);
        }
    }

    /**
     * 设置头像
     *
     * @param url
     * @param avatar
     */
    public static void setAvatar(Context context, String url, final ImageView avatar) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        CommonApplication.finalBitmap.display(url, new ImageDownloadStateListener() {

            @Override
            public void loading() {

            }

            @Override
            public void loadOk(Bitmap bitmap, NinePatchDrawable drawable, byte[] gifByte) {
                transforCircleBitmap(bitmap, avatar);
            }

            @Override
            public void loadError() {
            }
        });
    }

    /**
     * 通过时区获取当前时间
     */

    public static String getTimeFromZone(String zone) {
        SimpleDateFormat dff = new SimpleDateFormat("HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone(zone));
        String ee = dff.format(new Date());
        return ee;

    }
}
