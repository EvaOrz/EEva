package modernmedia.com.cn.exhibitioncalendar.api;

import android.text.TextUtils;
import android.util.Log;

import modernmedia.com.cn.corelib.util.ConstData;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;

/**
 * 接口地址管理
 * Created by Eva. on 17/3/14.
 */

public class UrlMaker {

    /**
     * Host信息
     **/
    private static String HOST;
    private static String USER_MODEL_URL;

    /**
     * 初始化host
     */
    public static void setHost() {
        if (MyApplication.DEBUG == 0) {// 线上环境
            HOST = "https://artcalendar.bbwc.cn/index/api/";
            USER_MODEL_URL = "http://user.bbwc.cn/interface/index.php";

        } else if (MyApplication.DEBUG == 1) {// inhouse环境
            HOST = "https://artcalendar-test.bbwc.cn/index/api/";
            USER_MODEL_URL = "http://user.test.bbwc.cn/interface/index.php";
        }
    }

    public static String calendarHomePage = "https://artcalendar.bbwc.cn/html/artCalendar/index.html";
    public static String calendarHomePageDev = "https://artcalendar-test.bbwc.cn/html/artCalendar/index.html";
    public static String calendarHomePageEdit = "https://artcalendar.bbwc.cn/html/editartCalendar/index.html";

    public static String calendarDetailPage = "https://artcalendar.bbwc.cn/html/artCalendar/detail.html";
    public static String calendarDetailPageDev = "https://artcalendar-test.bbwc.cn/html/artCalendar/detail.html";
    public static String calendarDetailPageEdit = "https://artcalendar.bbwc.cn/html/editartCalendar/detail.html";
    public static String calendarAboutPage = "https://artcalendar.bbwc.cn/html/artCalendar/about.html";

    /**
     * 登录接口
     *
     * @return
     */
    public static String login() {
        return "";
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public static String getVerifyCode() {
        return USER_MODEL_URL + "?m=sms&a=send&datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取绑定状态
     *
     * @return
     */
    public static String getBandStatus() {
        return USER_MODEL_URL + "?m=user&a=bindstatus&datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 绑定账号
     *
     * @return
     */
    public static String bandAccount() {
        return USER_MODEL_URL + "?m=user&a=bind&datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 注册接口
     *
     * @return
     */
    public static String register() {
        return USER_MODEL_URL + "?m=user&a=add&datatype=" + ConstData.DATA_TYPE + "&datapass=" + ConstData.DATA_TYPE;
    }

    /**
     * @return 用于找回密码的url
     */
    public static String getPasswordUrl() {
        return USER_MODEL_URL + "?m=user&a=find_password&datatype=" + ConstData.DATA_TYPE;
    }


    /**
     * 上传图片接口
     *
     * @return
     */
    public static String uploadPic() {
        return HOST + "uploadcover?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 添加收藏
     *
     * @return
     */
    public static String addUserFav() {
        return HOST + "eventadd?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 删除收藏
     *
     * @return
     */
    public static String deleteUserFav() {
        return HOST + "eventdel?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 修改收藏
     *
     * @return
     */
    public static String changeUserFav() {
        return HOST + "eventedit?datatype=" + ConstData.DATA_TYPE;
    }


    /**
     * 用户收藏的展览列表
     *
     * @return
     */
    public static String getUserFavList() {
        return HOST + "futurelist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 用户收藏的过期展览列表
     *
     * @return
     */
    public static String getUserFavExpiredList() {
        return HOST + "expiredlist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取热门列表
     *
     * @return
     */
    public static String getHotList() {
        return HOST + "hotlist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取推荐列表
     *
     * @return
     */
    public static String getRecommendedList() {
        return HOST + "recommendedlist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 用户添加城市标签
     *
     * @return
     */
    public static String addCity() {
        return HOST + "cityadd?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 用户删除城市标签
     *
     * @return
     */
    public static String deleteCity() {
        return HOST + "citydel?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取用户城市列表
     *
     * @return
     */
    public static String getUserCitys() {
        return HOST + "mycity?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取某个item的标签列表
     *
     * @return
     */
    public static String getItemTagList() {
        return HOST + "getitemtag?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取展览详情
     *
     * @return
     */
    public static String getExhibitionDetail() {
        return HOST + "details?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取展览列表接口
     *
     * @return
     */
    public static String getExhibitionList() {
        return HOST + "itemlist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 天气接口
     *
     * @param city
     * @return
     */
    public static String getWeather(String city) {
        return HOST + "weather?city=" + city + "&datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 广告接口
     *
     * @return
     */
    public static String getAdvList() {
        String host = "";
        if (MyApplication.DEBUG == 0) {
            host = "http://adver.cdn.bbwc.cn";
        } else {
            host = "http://adver.inhouse.bbwc.cn";
        }
        String url = host + "/adv/v" + ConstData.API_VERSION + "/list/" + MyApplication.APPID + "-" + ConstData.DEVICE_TYPE + "-" + ConstData.DATA_TYPE + ".html";
        if (!TextUtils.isEmpty(ConstData.getAdvUpdateTime())) {
            url += "?updatetime=" + ConstData.getAdvUpdateTime();
        }
        Log.e("getAdvList()", url);
        return url;
    }

    /**
     * @return 用于修改密码的url
     */
    public static String getModifyPasswordUrl() {
        return USER_MODEL_URL + "?m=user&a=modify_password&datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * @return 用于上传头像的url
     */
    public static String getUploadAvatarUrl() {
        return USER_MODEL_URL + "?m=user&a=upload&datatype=" + ConstData.DATA_TYPE;
    }


    /**
     * @return 开放平台用户登录用的url
     */
    public static String getOpenLoginUrl() {
        return USER_MODEL_URL + "?m=user&a=open_login&datatype=" + ConstData.DATA_TYPE + "&datapass=" + ConstData.DATA_TYPE;
    }

    /**
     * @return 用于修改用户资料的url
     */
    public static String getModifyInfoUrl() {
        return USER_MODEL_URL + "?m=user&a=modify&datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 搜索接口
     *
     * @return
     */
    public static String search() {
        return HOST + "search?datatype=" + ConstData.DATA_TYPE;
    }
}
