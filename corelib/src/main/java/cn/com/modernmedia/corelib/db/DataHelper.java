package cn.com.modernmedia.corelib.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import cn.com.modernmedia.corelib.model.UserModel;


/**
 * 临时数据存储类
 * Created by Eva. on 16/9/16.
 */
public class DataHelper {

    /**
     * User info
     */
    public static final String USER_NAME = "username";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String UID = "uid";
    public static final String SINA_ID = "sina_id";
    public static final String QQ_ID = "qq_id";
    public static final String WEIXIN_ID = "weixin_id";
    public static final String TOKEN = "token";
    public static final String NICKNAME = "nickname";
    public static final String ISSUELEVEL = "issuelevel";
    public static final String END_TIME = "end_time";
    public static final String DESC = "desc";
    public static final String IS_EMAIL_PUSHED = "email_push";
    public static final String REALNAME = "realname";
    public static final String SEX = "sex";
    public static final String BIRTH = "birth";
    public static final String VIP = "vip";
    public static final String START_TIME = "start_time";
    public static final String VIP_END_TIME = "vip_end_time";
    public static final String INDUSTRY = "industry";
    public static final String POSITION = "position";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String INCOME = "income";
    public static final String LEVEL = "level";
    public static final String SEND = "send";
    public static final String COMPLETEVIP = "completevip";
    public static final String USER_STATUS = "user_status";
    public static final String UNION_ID = "union_id";//微信unionid
    public static final String OPEN_ID = "open_id";//整合第三方openid
    public static final String ISVIP = "isVip";
    public static final String VIPPID = "vipPid";
    public static final String PID = "pid";
    public static final String EBOOKENDTIME = "ebookendtime";
    public static final String CODE_TITLE = "title";//激活成功提示语
    public static final String CODE_ISVIP = "isvip";//激活码是否兑换vip
    public static final String CODE_NEEDADDRESS = "needaddress";//激活码是否需要地址
    public static final String ADDRESS_ID = "address_id";//用户邮寄地址id

    public static final String ADV_TIME = "adv_time";//入版广告时间
    public static final String UUID = "uuid";

    public static final String  LAST_LOGIN_USERNAME = "last_login_username";// 上次登录账号
    public static final String NEW_LOGIN = "new_login";//整合第三方
    public static final String NEW_LOGIN_TIME = "new_login_time";//整合第三方取消时间
    private static final String INDEX_HEAD_AUTO_LOOP = "index_head_auto_loop";// 首页自动轮播开关
    private static final String WIFI_AUTO_PLAY_VEDIO = "wifi_auto_play_vedio";// WiFi下自动播放视频
    private static final String PUSH_SERVICE_ENABLE = "push_service_enable";// 推送服务是否可用
    /**
     * 广告更新时间
     */
    private static final String ADV_UPDATETIME = "adv_updatetime";


    private static SharedPreferences mPref;

    private static SharedPreferences getPref(Context context) {
        if (mPref == null) {
            mPref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mPref;
    }


    /**
     * 存储登录或者注册后得到的uid、token以及用户名
     *
     * @param context
     * @param user
     */
    public static void saveUserLoginInfo(Context context, UserModel user) {
        Editor editor = getPref(context).edit();
        editor.putString(USER_NAME, user.getUserName());
        editor.putString(PHONE, user.getPhone());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(PASSWORD, user.getPassword());
        editor.putString(UID, user.getUid());
        editor.putString(SINA_ID, user.getSinaId());
        editor.putString(QQ_ID, user.getQqId());
        editor.putString(WEIXIN_ID, user.getWeixinId());
        editor.putString(TOKEN, user.getToken());
        editor.putString(NICKNAME, user.getNickName());
        editor.putString(DESC, user.getDesc());
        editor.putInt(IS_EMAIL_PUSHED, user.isPushEmail());
        editor.putInt(SEX, user.getSex());
        editor.putString(REALNAME, user.getRealname());
        editor.putString(BIRTH, user.getBirth());
        editor.putString(VIP, user.getVip());
        editor.putLong(START_TIME, user.getStart_time());
        editor.putLong(VIP_END_TIME, user.getVip_end_time());
        editor.putString(INDUSTRY, user.getIndustry());
        editor.putString(POSITION, user.getPosition());
        editor.putString(INCOME, user.getIncome());
        editor.putString(PROVINCE, user.getProvince());
        editor.putString(CITY, user.getCity());
        editor.putString(SEND, user.getSend());
        editor.putInt(LEVEL, user.getLevel());
        editor.putInt(COMPLETEVIP, user.getCompletevip());
        editor.putInt(USER_STATUS, user.getUser_status());
        editor.putString(UNION_ID, user.getUnionId());
        editor.putString(OPEN_ID, user.getOpenId());
        editor.commit();
    }

    /**
     * 取得登录或者注册后得到的uid、token以及用户名
     *
     * @param context
     * @return
     */
    public static UserModel getUserLoginInfo(Context context) {
        UserModel user = new UserModel();
        user.setUserName(getPref(context).getString(USER_NAME, ""));
        user.setPhone(getPref(context).getString(PHONE, ""));
        user.setEmail(getPref(context).getString(EMAIL, ""));
        user.setPassword(getPref(context).getString(PASSWORD, ""));
        user.setUid(getPref(context).getString(UID, ""));
        user.setSinaId(getPref(context).getString(SINA_ID, ""));
        user.setQqId(getPref(context).getString(QQ_ID, ""));
        user.setWeixinId(getPref(context).getString(WEIXIN_ID, ""));
        user.setToken(getPref(context).getString(TOKEN, ""));
        user.setNickName(getPref(context).getString(NICKNAME, ""));
        user.setDesc(getPref(context).getString(DESC, ""));
        user.setAvatar(getAvatarUrl(context, user.getUserName()));
        // 是否验证邮箱
        user.setPushEmail(getPref(context).getInt(IS_EMAIL_PUSHED, 0));
        user.setSex(getPref(context).getInt(SEX, 2));//1男2女3保密
        user.setRealname(getPref(context).getString(REALNAME, ""));
        user.setBirth(getPref(context).getString(BIRTH, ""));
        user.setVip(getPref(context).getString(VIP, ""));
        user.setStart_time(getPref(context).getLong(START_TIME, 0));
        user.setVip_end_time(getPref(context).getLong(VIP_END_TIME, 0));
        user.setIndustry(getPref(context).getString(INDUSTRY, ""));
        user.setPosition(getPref(context).getString(POSITION, ""));
        user.setIncome(getPref(context).getString(INCOME, ""));
        user.setSend(getPref(context).getString(SEND, ""));
        user.setProvince(getPref(context).getString(PROVINCE, ""));
        user.setCity(getPref(context).getString(CITY, ""));
        user.setLevel(getPref(context).getInt(LEVEL, 0));
        user.setCompletevip(getPref(context).getInt(COMPLETEVIP, 0));
        user.setUser_status(getPref(context).getInt(USER_STATUS, 0));
        user.setUnionId(getPref(context).getString(UNION_ID, ""));
        user.setOpenId(getPref(context).getString(OPEN_ID, ""));

        if (TextUtils.isEmpty(user.getUid())) return null;
        return user;
    }


    /**
     * 清除登录或者注册后得到的数据
     *
     * @param context
     */
    public static void clearLoginInfo(Context context) {
        Editor editor = getPref(context).edit();
        editor.putString(USER_NAME, "");
        editor.putString(PASSWORD, "");
        editor.putString(PHONE, "");
        editor.putString(EMAIL, "");
        editor.putString(UID, "");
        editor.putString(SINA_ID, "");
        editor.putString(QQ_ID, "");
        editor.putString(WEIXIN_ID, "");
        editor.putString(TOKEN, "");
        editor.putString(NICKNAME, "");
        editor.putString(ISSUELEVEL, "0");
        editor.putInt(SEX, 0);
        editor.putString(REALNAME, "");
        editor.putString(BIRTH, "");
        editor.putString(VIP, "");
        editor.putLong(START_TIME, 0);
        editor.putLong(VIP_END_TIME, 0);
        editor.putString(INDUSTRY, "");
        editor.putString(POSITION, "");
        editor.putString(SEND, "");
        editor.putString(PROVINCE, "");
        editor.putString(CITY, "");
        editor.putInt(LEVEL, 0);
        editor.putInt(COMPLETEVIP, 0);
        editor.putInt(USER_STATUS, 0);
        editor.putString(DESC, "");
        editor.putString(UNION_ID, "");
        editor.putString(OPEN_ID, "");
        editor.putLong(EBOOKENDTIME, 0);
        editor.commit();
    }

    /**
     * 存储新的用户名
     *
     * @param context
     * @param userName
     */
    public static void saveUserName(Context context, String userName) {
        Editor editor = getPref(context).edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    /**
     * 存储头像下载路径
     *
     * @param url
     */
    public static void saveAvatarUrl(Context context, String userName, String url) {
        Editor editor = getPref(context).edit();
        editor.putString(userName, url);
        editor.commit();
    }

    /**
     * 取得头像的下载路径
     *
     * @param context
     * @param userName
     * @return
     */
    public static String getAvatarUrl(Context context, String userName) {
        return getPref(context).getString(userName, "");
    }

    /**
     * 获取uid
     *
     * @param context
     * @return
     */
    public static String getUid(Context context) {
        String uid = getPref(context).getString(UID, "");
        return TextUtils.isEmpty(uid) ? "0" : uid;
    }

    /**
     * 获取token
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        return getPref(context).getString(TOKEN, "");
    }


    /**
     * 获取最近一次更新的ADV_UPDATETIME
     *
     * @param context
     * @return
     */
    public static String getAdvUpdateTime(Context context) {
        return getPref(context).getString(ADV_UPDATETIME, "");
    }

    /**
     * 保存最近一次获取的ADV_UPDATETIME
     *
     * @param context
     * @param time
     */
    public static void setAdvUpdateTime(Context context, String time) {
        Editor editor = getPref(context).edit();
        editor.putString(ADV_UPDATETIME, time);
        editor.commit();
    }


    /**
     * 获取首次入版广告时间
     *
     * @return
     */
    public static long getAdvTime(Context context) {
        return getPref(context).getLong(ADV_TIME, 0);
    }

    /**
     * 记录首次入版广告时间
     *
     * @param context
     * @param time
     */
    public static void setAdvTime(Context context, long time) {
        Editor editor = getPref(context).edit();
        editor.putLong(ADV_TIME, time);
        editor.commit();
    }
    /**
     * 获取UUID
     *
     * @return
     */
    public static String  getUUID(Context context) {
        return getPref(context).getString(UUID,"");
    }

    /**
     * 记录UUID
     *
     * @param context
     */
    public static void setUUID(Context context, String uuid) {
        Editor editor = getPref(context).edit();
        editor.putString(UUID, uuid);
        editor.commit();
    }


    /**
     * 存上次登录账号
     * @param context
     * @param name
     */
    public static void setLastLoginUsername(Context context,String name){
        Editor editor = getPref(context).edit();
        editor.putString(LAST_LOGIN_USERNAME, name);
        editor.commit();
    }

    /**
     * 取上次登录账号
     * @param context
     * @return
     */
    public static String getLastLoginUsername(Context context) {
        return getPref(context).getString(LAST_LOGIN_USERNAME, "");
    }

    /**
     * 存储是否点击新的登录接口
     *
     * @param context
     */
    public static void saveHasSync(Context context, boolean isNewLogin) {
        Editor editor = getPref(context).edit();
        editor.putBoolean(NEW_LOGIN, isNewLogin);
        editor.commit();
    }

    /**
     * 取得是否同步过新的微信账号
     *
     * @param context
     * @return
     */
    public static boolean getSync(Context context) {
        return getPref(context).getBoolean(NEW_LOGIN, false);
    }

    public static void clearHasSync(Context context) {
        Editor editor = getPref(context).edit();
        editor.putBoolean(NEW_LOGIN, false);
        editor.commit();
    }




    /**
     * 获取推送服务是否可用
     *
     * @param context
     * @return
     */
    public static boolean isPushServiceEnable(Context context) {
        return getPref(context).getBoolean(PUSH_SERVICE_ENABLE, true);
    }

    /**
     * 设置推送服务是否可用
     *
     * @param context
     */
    public static void setPushServiceEnable(Context context, boolean enable) {
        Editor editor = getPref(context).edit();
        editor.putBoolean(PUSH_SERVICE_ENABLE, enable);
        editor.commit();
    }

    /**
     * 是否首页自动轮播
     *
     * @param context
     * @return
     */
    public static boolean getIndexHeadAutoLoop(Context context) {
        return getPref(context).getBoolean(INDEX_HEAD_AUTO_LOOP, true);
    }

    /**
     * 设置首页是否轮播
     *
     * @param context
     */
    public static void setIndexHeadAutoLoop(Context context, boolean ifAuto) {
        Editor editor = getPref(context).edit();
        editor.putBoolean(INDEX_HEAD_AUTO_LOOP, ifAuto);
        editor.commit();
    }


    /**
     * 是否WiFi下自动播放视频
     *
     * @param context
     * @return
     */
    public static boolean getWiFiAutoPlayVedio(Context context) {
        return getPref(context).getBoolean(WIFI_AUTO_PLAY_VEDIO, true);
    }

    /**
     * 设置WiFi下自动播放视频
     *
     * @param context
     */
    public static void setWiFiAutoPlayVedio(Context context, boolean ifAuto) {
        Editor editor = getPref(context).edit();
        editor.putBoolean(WIFI_AUTO_PLAY_VEDIO, ifAuto);
        editor.commit();
    }
}