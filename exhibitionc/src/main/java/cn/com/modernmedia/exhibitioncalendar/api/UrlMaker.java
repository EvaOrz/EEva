package cn.com.modernmedia.exhibitioncalendar.api;

import android.text.TextUtils;
import android.util.Log;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.util.ConstData;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;

/**
 * 接口地址管理
 * Created by Eva. on 17/3/14.
 */

public class UrlMaker {

    /**
     * Host信息
     **/
    public static String HOST;
    private static String API_URL;
    private static String USER_MODEL_URL;
    private static String PRODUCT_URL = "";// 付费相关

    // 正式环境
    private static String calendarHomePage = "https://artcalendar.bbwc.cn/html/artCalendar/html/index.html";
    private static String calendarDetailPage = "https://artcalendar.bbwc.cn/html/artCalendar/detail.html";
    public static String calendarAboutPage = "https://artcalendar.bbwc.cn/html/artCalendar/about.html";
    private static String calendarTagPage = "https://artcalendar.bbwc.cn/html/artCalendar/html/topic.html?topic_id=";
    private static String calendarDetailActivePage = "https://artcalendar.bbwc.cn/html/artCalendar/html/activity.html?active_id=";
    private static String calendarHomeMuseumPage = "http://artcalendar.bbwc.cn/html/artCalendar/html/museum_list.html";
    private static String calendarDetailMuseumPage = "http://artcalendar.bbwc" + ".cn/html/artCalendar/html/museum_detail.html?itemid=";

    // 测试环境
    private static String calendarHomePageDev = "https://artcalendar-test.bbwc.cn/html/artCalendar/html/index.html";
    private static String calendarDetailPageDev = "https://artcalendar-test.bbwc.cn/html/artCalendar/detail.html";
    private static String calendarTagPageDev = "https://artcalendar-test.bbwc.cn/html/artCalendar/html/topic.html?topic_id=";
    private static String calendarDetailActivePageDev = "https://artcalendar-test.bbwc.cn/html/artCalendar/html/activity.html?active_id=";
    private static String calendarHomeMuseumPageDev = "http://artcalendar-test.bbwc.cn/html/artCalendar/html/museum_list.html";
    private static String calendarDetailMuseumPageDev = "http://artcalendar-test.bbwc.cn/html/artCalendar/html/museum_detail.html?itemid=";

    // 编辑环境
    private static String calendarDetailActivePageEdit = "https://artcalendar.bbwc.cn/html/editartCalendar/html/activity.html?active_id=";
    private static String calendarHomeMuseumPageEdit = "http://artcalendar.bbwc.cn/html/editartCalendar/html/museum_list.html";
    private static String calendarDetailMuseumPageEdit = "http://artcalendar.bbwc.cn/html/editartCalendar/html/museum_detail.html?museum_id=";

    /**
     * 初始化host
     */
    public static void setHost() {
        if (MyApplication.DEBUG == 0) {// 线上环境
            HOST = "https://artcalendar.bbwc.cn";
            USER_MODEL_URL = "http://user.bbwc.cn/interface/index.php";
            API_URL = HOST + "/api3/";
            PRODUCT_URL = "https://buy.bbwc.cn";

        } else if (MyApplication.DEBUG == 1) {// inhouse环境
            HOST = "https://artcalendar-test.bbwc.cn";
            USER_MODEL_URL = "http://user.test.bbwc.cn/interface/index.php";
            API_URL = HOST + "/index/edit/";
            PRODUCT_URL = "https://buy-test.bbwc.cn";
        }
    }

    public static String getDetailPage() {
        if (MyApplication.DEBUG == 0) {
            return calendarDetailPage;
        } else if (MyApplication.DEBUG == 1) {
            return calendarDetailPageDev;
        }
        return calendarDetailPage;
    }

    /**
     * 获取全部展馆
     *
     * @return
     */
    public static String getZhanguanList() {
        if (MyApplication.DEBUG == 0) {
            return calendarHomeMuseumPage;
        } else if (MyApplication.DEBUG == 1) {
            return calendarHomeMuseumPageDev;
        }
        return "";
    }

    /**
     * 获取展馆详情
     *
     * @param id
     * @return
     */
    public static String getZhanguanDetail(String id) {
        if (MyApplication.DEBUG == 0) {
            return calendarDetailMuseumPage + id;
        } else if (MyApplication.DEBUG == 1) {
            return calendarDetailMuseumPageDev + id;
        }
        Log.e("展馆详情", calendarDetailMuseumPage + id);
        return "";
    }

    public static String getHomePage() {
        if (MyApplication.DEBUG == 0) {
            return calendarHomePage;
        } else if (MyApplication.DEBUG == 1) {
            return calendarHomePageDev;
        }
        return calendarHomePage;
    }

    public static String getTagPage(String tagId) {
        if (MyApplication.DEBUG == 0) {
            return calendarTagPage + tagId;
        } else if (MyApplication.DEBUG == 1) {
            return calendarTagPageDev + tagId;
        }
        return calendarTagPage;
    }

    /**
     * 登录接口
     *
     * @return
     */
    public static String login() {
        return USER_MODEL_URL + "?m=user&a=login&datatype=" + ConstData.DATA_TYPE + "&datapass=" + ConstData.DATA_TYPE;
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
        return API_URL + "uploadcover?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 添加收藏
     *
     * @return
     */
    public static String addUserFav() {
        return API_URL + "eventadd?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 删除收藏
     *
     * @return
     */
    public static String deleteUserFav() {
        return API_URL + "eventdel?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 修改收藏
     *
     * @return
     */
    public static String changeUserFav() {
        return API_URL + "eventedit?datatype=" + ConstData.DATA_TYPE;
    }


    /**
     * 用户收藏的展览列表
     *
     * @return
     */
    public static String getUserFavList() {
        return API_URL + "futurelist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 用户收藏的过期展览列表
     *
     * @return
     */
    public static String getUserFavExpiredList() {
        return API_URL + "expiredlist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取热门列表
     *
     * @return
     */
    public static String getHotList() {
        return API_URL + "hotlist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取推荐列表
     *
     * @return
     */
    public static String getRecommendedList() {
        return API_URL + "recommendedlist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取周边列表
     *
     * @return
     */
    public static String getNearList() {
        return API_URL + "getnearitem?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 用户添加城市标签
     *
     * @return
     */
    public static String addCity() {
        return API_URL + "cityadd?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 用户删除城市标签
     *
     * @return
     */
    public static String deleteCity() {
        return API_URL + "citydel?datatype=" + ConstData.DATA_TYPE;
    }


    /**
     * 全部城市列表
     *
     * @return
     */
    public static String getAllCitys() {
        return API_URL + "citylist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取某个item的标签列表
     *
     * @return
     */
    public static String getItemTagList() {
        return API_URL + "getitemtag?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取展览详情
     *
     * @return
     */
    public static String getExhibitionDetail() {
        return API_URL + "details?datatype=" + ConstData.DATA_TYPE;
    }


    /**
     * 获取展览列表接口
     *
     * @return
     */
    public static String getAllList() {
        return API_URL + "itemlist?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取展馆列表接口
     *
     * @return
     */
    public static String getMuseumList() {
        return API_URL + "museumlist?datatype=" + ConstData.DATA_TYPE;

    }

    /**
     * 天气接口
     *
     * @param city
     * @return
     */
    public static String getWeather(String city) {
        return API_URL + "weather?city=" + city + "&datatype=" + ConstData.DATA_TYPE;
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
     * 推送：收集设备信息接口
     */
    public static String getDeviceInfoForPush() {
        if (MyApplication.DEBUG == 0)
            return "http://user.bbwc.cn/device/add/type/1?datatype=" + ConstData.DATA_TYPE;
        else return "http://user.test.bbwc.cn/device/add/type/1?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 搜索接口
     *
     * @return
     */
    public static String search() {
        return API_URL + "search?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取分享id
     *
     * @return
     */
    public static String getShareId() {
        return API_URL + "addshare?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取分享地址
     *
     * @param shareId
     * @return
     */
    public static String getShareWebUrl(String shareId) {
        return API_URL + "share?id=" + shareId;
    }


    public static String getMuseumDetail() {
        return API_URL + "museumdetails?datatype=" + ConstData.DATA_TYPE;
    }


    /**
     * 新支付：更新订单状态
     * type :1- weixin 2- zhifubao
     */
    public static String newUpdateOrderStatus(int type) {
        return PRODUCT_URL + "/order/pay/type/" + type + "?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 新支付：获取订单
     * type :1- weixin 2- zhifubao
     */
    public static String newGetOrder(int type) {
        return PRODUCT_URL + "/order/add/type/" + type + "?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 获取套餐列表
     *
     * @return
     */
    public static String getVipProducts() {
        return API_URL + "viphome?datatype=" + ConstData.DATA_TYPE;
    }


    /**
     * 新支付：获取用户订阅身份
     */
    public static String getVipInfo() {
        return API_URL + "vipinfo?datatype=" + ConstData.DATA_TYPE;
    }

    /**
     * 激活兑换码
     *
     * @return
     */
    public static String getJihuoCode() {
        if (MyApplication.DEBUG == 0) {
            return "https://artcalendar.bbwc.cn/html/artCalendar/html/redeem.html";
        } else if (MyApplication.DEBUG == 1) {
            return "https://artcalendar-test.bbwc.cn/html/artCalendar/html/redeem.html";
        }
        return "";
    }

    /**
     * 判断版本号，不一致就升级
     *
     * @return
     */
    public static String checkVersion(String version) {
        if (MyApplication.DEBUG == 0) {
            return "https://user.bbwc.cn/device/versionUpdate?appid=" + MyApplication.APP_ID + "&type=android&version=" + version + "&src=" + CommonApplication.CHANNEL;
        } else
            return "https://user-test.bbwc.cn/device/versionUpdate?appid=" + MyApplication.APP_ID + "&type=android&version=" + version + "&src=" + CommonApplication.CHANNEL;
    }

    /**
     * VIP添加、修改（api2以上版本）
     */
    public static String addVipInfo() {
        return API_URL + "vipadd?datatype=" + ConstData.DATA_TYPE;
    }
}
