package modernmedia.com.cn.corelib.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import modernmedia.com.cn.corelib.model.UserModel;


/**
 * 临时数据存储类
 * Created by Eva. on 16/9/16.
 */
public class DataHelper {
    public static final String USER_NAME = "username";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NEWPASSWORD = "newpassword";
    public static final String UID = "uid";
    public static final String TOKEN = "token";
    public static final String SEX = "gender";
    public static final String NICKNAME = "nickname";
    public static final String AVATAR = "avatar";
    public static final String PROFESSION = "position";
    public static final String BIRTHDAY = "birthday";
    public static final String COMPANY = "company";
    public static final String REALNAME = "realname";
    public static final String PUSHSHOWDETAIL = "pushShowDetail";
    public static final String PUSHBYSOUND = "pushBySound";
    public static final String PUSHBYVIBRATE = "pushByVibrate";
    public static final String WEIXIN_ID = "weixin_id";
    public static final String QQ_ID = "qq_id";
    public static final String SINA_ID = "sina_id";
    public static final String QRCODE = "QrCode";
    public static final String PROVINCEN = "province";
    public static final String CITY = "city";

    public static final String CONTACT_QQ = "contact_qq";
    public static final String CONTACT_WEIBO = "contact_weibo";
    public static final String CONTACT_WECHAT = "contact_wechat";

    /**
     * 预约闹铃数
     */
    public static String ALARM_COUNT = "alarm_count";


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
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putString(USER_NAME, user.getUserName());
        editor.putString(PHONE, user.getPhone());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(PASSWORD, user.getPassword());
        editor.putString(NEWPASSWORD, user.getNewPassword());
        editor.putString(UID, user.getUid());
        editor.putString(TOKEN, user.getToken());
        editor.putString(NICKNAME, user.getNickName());
        editor.putString(BIRTHDAY, user.getBirthday());
        editor.putString(AVATAR, user.getAvatar());
        editor.putString(REALNAME, user.getRealName());
        editor.putInt(SEX, user.getSex());
        editor.putString(PROFESSION, user.getPosition());
        editor.putString(COMPANY, user.getCompany());
        editor.putInt(PUSHBYSOUND, user.getPushBySound());
        editor.putInt(PUSHBYVIBRATE, user.getPushByVibrate());
        editor.putInt(PUSHSHOWDETAIL, user.getPushShowDetail());
        editor.putString(WEIXIN_ID, user.getWeixinId());
        editor.putString(QQ_ID, user.getQqId());
        editor.putString(SINA_ID, user.getSinaId());
        editor.putString(QRCODE, user.getQrCode());
        editor.putString(PROVINCEN, user.getProvince());
        editor.putString(CITY, user.getCity());
        editor.putString(CONTACT_QQ, user.getContact().getQq());
        editor.putString(CONTACT_WEIBO, user.getContact().getWeibo());
        editor.putString(CONTACT_WECHAT, user.getContact().getWechat());


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
        user.setNewPassword(getPref(context).getString(NEWPASSWORD, ""));
        user.setUid(getPref(context).getString(UID, ""));
        user.setBirthday(getPref(context).getString(BIRTHDAY, ""));
        user.setToken(getPref(context).getString(TOKEN, ""));
        user.setNickName(getPref(context).getString(NICKNAME, ""));
        user.setAvatar(getPref(context).getString(AVATAR, ""));
        user.setPosition(getPref(context).getString(PROFESSION, ""));
        user.setCompany(getPref(context).getString(COMPANY, ""));
        user.setRealName(getPref(context).getString(REALNAME, ""));
        user.setSex(getPref(context).getInt(SEX, 1));
        user.setPushBySound(getPref(context).getInt(PUSHBYSOUND, 0));
        user.setPushByVibrate(getPref(context).getInt(PUSHBYVIBRATE, 0));
        user.setPushShowDetail(getPref(context).getInt(PUSHSHOWDETAIL, 0));
        user.setWeixinId(getPref(context).getString(WEIXIN_ID, ""));
        user.setQqId(getPref(context).getString(QQ_ID, ""));
        user.setSinaId(getPref(context).getString(SINA_ID, ""));
        user.setQrCode(getPref(context).getString(QRCODE, ""));
        user.setProvince(getPref(context).getString(PROVINCEN, ""));
        user.setCity(getPref(context).getString(CITY, ""));
        UserModel.Contact c = new UserModel.Contact();
        c.setQq(getPref(context).getString(CONTACT_QQ, ""));
        c.setWeibo(getPref(context).getString(CONTACT_WEIBO, ""));
        c.setWechat(getPref(context).getString(CONTACT_WECHAT, ""));
        user.setContact(c);

        if (TextUtils.isEmpty(user.getUid())) return null;
        return user;
    }

    /**
     * 清除登录或者注册后得到的数据
     *
     * @param context
     */
    public static void clearLoginInfo(Context context) {
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putString(USER_NAME, "");
        editor.putString(PASSWORD, "");
        editor.putString(NEWPASSWORD, "");
        editor.putString(PHONE, "");
        editor.putString(EMAIL, "");
        editor.putString(UID, "");
        editor.putString(TOKEN, "");
        editor.putString(NICKNAME, "");
        editor.putString(REALNAME, "");
        editor.putString(PROFESSION, "");
        editor.putString(COMPANY, "");
        editor.putString(AVATAR, "");
        editor.putInt(SEX, 0);
        editor.putString(BIRTHDAY, "");
        editor.putInt(PUSHBYSOUND, 0);
        editor.putInt(PUSHBYVIBRATE, 0);
        editor.putInt(PUSHSHOWDETAIL, 0);
        editor.putString(QRCODE, "");
        editor.putString(PROVINCEN, "");
        editor.putString(CITY, "");
        editor.putString(CONTACT_QQ, "");
        editor.putString(CONTACT_WEIBO, "");
        editor.putString(CONTACT_WECHAT, "");


        editor.commit();
    }

    /**
     * 存储头像下载路径
     *
     * @param url
     */
    public static void saveAvatarUrl(Context context, String userName, String url) {
        SharedPreferences.Editor editor = getPref(context).edit();
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
        return TextUtils.isEmpty(uid) ? "" : uid;
    }


    /**
     * 获取weixin_id
     *
     * @param context
     * @return
     */
    public static String getWeixinId(Context context) {
        String w = getPref(context).getString(WEIXIN_ID, "");
        return TextUtils.isEmpty(w) ? "" : w;
    }

    /**
     * 获取qq_id
     *
     * @param context
     * @return
     */
    public static String getQqId(Context context) {
        String w = getPref(context).getString(QQ_ID, "");
        return TextUtils.isEmpty(w) ? "" : w;
    }

    /**
     * 获取sina_id
     *
     * @param context
     * @return
     */
    public static String getSinaId(Context context) {
        String w = getPref(context).getString(SINA_ID, "");
        return TextUtils.isEmpty(w) ? "" : w;
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

    public static int getAlarmCount(Context context) {
        return getPref(context).getInt(ALARM_COUNT, 0);
    }

    public static void setAlarmCount(Context context, int alarmCount) {
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putInt(ALARM_COUNT, alarmCount);
        editor.commit();
    }
}
