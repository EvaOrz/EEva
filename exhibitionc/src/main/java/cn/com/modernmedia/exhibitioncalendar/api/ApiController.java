package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.http.BaseApi.FetchApiType;
import cn.com.modernmedia.corelib.http.HttpsController;
import cn.com.modernmedia.corelib.listener.DataCallBack;
import cn.com.modernmedia.corelib.listener.FetchDataListener;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.util.DESCoder;
import cn.com.modernmedia.exhibitioncalendar.api.GetSomeListApi.TAG_TYPE;
import cn.com.modernmedia.exhibitioncalendar.api.user.BandAccountApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.FindPasswordApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.GetBandStatusApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.HandleFavApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.LoginApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.ModifyUserInfoApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.ModifyUserPasswordApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.OpenLoginApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.PushDeviceInfoApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.RegisterApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.SearchApi;
import cn.com.modernmedia.exhibitioncalendar.api.user.UploadAvaterApi;

import static cn.com.modernmedia.corelib.http.BaseApi.FetchApiType.USE_CACHE_FIRST;
import static cn.com.modernmedia.corelib.http.BaseApi.FetchApiType.USE_HTTP_ONLY;

/**
 * Created by Eva. on 17/3/30.
 */

public class ApiController {
    private static ApiController instance;
    private static Context mContext;
    public static String SUCCESS = "SUCCESS";
    public static String ERROR = "ERROR";
    public static String CANCEL = "CANCEL";
    public static String PAYING = "PAYING";
    /**
     * DB存储订单的key
     */
    public static String NEW_WEIXIN_KEY = "NEW_WEIXIN";
    public static String NEW_ALI_KEY = "NEW_ALI";

    private Handler mHandler = new Handler();

    private ApiController(Context context) {
        mContext = context;
    }

    public static synchronized ApiController getInstance(Context context) {
        mContext = context;
        if (instance == null) instance = new ApiController(context);
        return instance;
    }

    private void doGetRequest(BaseApi api, Entry entry, BaseApi.FetchApiType type, FetchEntryListener listener) {
        doGetRequest(api, entry, type, listener, null);
    }

    private void doGetRequest(BaseApi api, final Entry entry, FetchApiType type, final FetchEntryListener listener, final AfterCallBack afterCallBack) {
        api.asyncRequest(mContext, type, new DataCallBack() {

            @Override
            public void callback(boolean success, boolean fromHttp) {
                sendMessage(success ? entry : null, listener, fromHttp, afterCallBack);
            }
        });
    }

    private void doPostRequest(BaseApi api, Entry entry, FetchApiType type, FetchEntryListener listener) {
        doPostRequest(api, entry, type, listener, null);
    }

    private void doPostRequest(BaseApi api, final Entry entry, FetchApiType type, final FetchEntryListener listener, final AfterCallBack afterCallBack) {
        api.asyncRequestByPost(mContext, type, new DataCallBack() {

            @Override
            public void callback(boolean success, boolean fromHttp) {
                sendMessage(success ? entry : null, listener, fromHttp, afterCallBack);
            }
        });
    }

    /**
     * 返回给ui层
     *
     * @param entry
     * @param listener
     * @param fromHttp
     * @param afterCallBack
     */
    private void sendMessage(final Entry entry, final FetchEntryListener listener, final boolean fromHttp, final AfterCallBack afterCallBack) {
        synchronized (mHandler) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    listener.setData(entry);
                    if (afterCallBack != null) afterCallBack.afterCallBack(entry, fromHttp);
                }
            });
        }


    }

    /**
     * 获取展览详情页面
     *
     * @param c
     * @param id
     * @param fetchEntryListener
     */
    public void getCalendarDetail(Context c, String id, FetchEntryListener fetchEntryListener) {
        GetDetailApi getDetailApi = new GetDetailApi(c, id, 0);
        doPostRequest(getDetailApi, getDetailApi.getCalendarDetail(), USE_HTTP_ONLY, fetchEntryListener);
    }


    public void getMuseumDetail(Context c, String id, FetchEntryListener fetchEntryListener) {
        GetDetailApi getDetailApi = new GetDetailApi(c, id, 1);
        doPostRequest(getDetailApi, getDetailApi.getMuseumDetail(), USE_HTTP_ONLY, fetchEntryListener);
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param listener
     */
    public void login(String userName, String password, FetchEntryListener listener) {
        LoginApi loginApi = new LoginApi(userName, password);
        doPostRequest(loginApi, loginApi.getUserModel(), USE_HTTP_ONLY, listener);
    }

    /**
     * 获取广告列表
     *
     * @param listener
     */
    public void getAdvList(FetchApiType fetchApiType, FetchEntryListener listener) {
        GetAdvListApi getAdvListApi = new GetAdvListApi();
        doGetRequest(getAdvListApi, getAdvListApi.getAdvList(), fetchApiType, listener);
    }

    /**
     * 搜索
     *
     * @param c
     * @param k
     * @param listener
     */
    public void search(Context c, String k, FetchEntryListener listener) {
        SearchApi searchApi = new SearchApi(c, k);
        doPostRequest(searchApi, searchApi.getCalendarListModel(), USE_HTTP_ONLY, listener);
    }

    /**
     * @param c
     * @param k
     * @param listener
     */
    public void getWeather(Context c, String k, FetchEntryListener listener) {
        GetWeatherApi getWeatherApi = new GetWeatherApi(c, k);
        doPostRequest(getWeatherApi, getWeatherApi.getData(), USE_HTTP_ONLY, listener);
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @param listener
     */
    public void getVerifyCode(String phone, FetchEntryListener listener) {
        GetVerifyCodeApi operate = new GetVerifyCodeApi(phone);
        doPostRequest(operate, operate.getCode(), USE_HTTP_ONLY, listener);
    }

    /**
     * 获取绑定状态
     *
     * @param uid
     * @param token
     * @param listener
     */
    public void getBandStatus(String uid, String token, FetchEntryListener listener) {
        GetBandStatusApi operate = new GetBandStatusApi(uid, token);
        doPostRequest(operate, operate.getStatus(), USE_HTTP_ONLY, listener);
    }

    /**
     * 获取用户绑定信息
     */
    public void bandAccount(String uid, String token, int bindType, String userName, String code, FetchEntryListener listener) {
        BandAccountApi operate = new BandAccountApi(uid, bindType, token, userName, code);
        doPostRequest(operate, operate.getError(), USE_HTTP_ONLY, listener);
    }

    /**
     * 用户注册
     *
     * @param userName 用户名称
     * @param password 密码
     * @param listener view数据回调接口
     */
    public void register(String userName, String password, String code, String phone, String nick, FetchEntryListener listener) {
        RegisterApi operate = new RegisterApi(userName, password, code, phone, nick);
        doPostRequest(operate, operate.getUser(), USE_HTTP_ONLY, listener);
    }

    /**
     * 忘记密码
     *
     * @param listener view数据回调接口
     */
    public void getPassword(String userName, String code, String newPwd, FetchEntryListener listener) {
        FindPasswordApi operate = new FindPasswordApi(userName, code, newPwd);
        doPostRequest(operate, operate.getData(), USE_HTTP_ONLY, listener);
    }

    /**
     * 上传用户头像
     *
     * @param imagePath 头像存储在本地的路径
     * @param listener  view数据回调接口
     */
    public void uploadUserAvatar(String imagePath, FetchEntryListener listener) {
        UploadAvaterApi operate = new UploadAvaterApi(imagePath);
        doPostRequest(operate, operate.getUploadResult(), USE_HTTP_ONLY, listener);
    }

    /**
     * 开放平台(新浪微博、QQ等)账号登录
     *
     * @param user     用户信息
     * @param avatar   服务器相对地址
     * @param type     平台类型,目前0:普通登录；1：新浪微博；2：腾讯qq；3：微信4Facebook；5phone
     * @param listener view数据回调接口
     */
    public void openLogin(Context context, UserModel user, String avatar, String code, int type, FetchEntryListener listener) {
        OpenLoginApi operate = new OpenLoginApi(context, user, avatar, code, type);
        doPostRequest(operate, operate.getUser(), USE_HTTP_ONLY, listener);
    }

    /**
     * 修改用户资料
     *
     * @param uid      uid
     * @param token    用户token
     * @param userName 用户名
     * @param nickName 昵称
     * @param url      图片的相对地址(通过上传头像获得)
     * @param password 用户登录密码
     * @param desc     用户登录密码
     * @param listener view数据回调接口
     */
    public void modifyUserInfo(Context context, String uid, String token, String realName, String userName, String nickName, String url, String password, String desc, boolean pushEmail, FetchEntryListener listener) {
        ModifyUserInfoApi operate = new ModifyUserInfoApi(context, uid, token, realName, userName, nickName, url, password, desc, pushEmail);
        doPostRequest(operate, operate.getUser(), USE_HTTP_ONLY, listener);
    }

    /**
     * @param c
     * @param page
     */
    public void getMyList(Context c, String page, int type, FetchEntryListener listener) {
        GetMyListApi getMyListApi = new GetMyListApi(c, page, type);
        doPostRequest(getMyListApi, getMyListApi.getData(), USE_HTTP_ONLY, listener);
    }

    /**
     * 推送：提交device info
     *
     * @param context
     * @param listener
     */
    public void pushDeviceInfo(Context context, String token, String type, FetchEntryListener listener) {
        PushDeviceInfoApi pushDeviceInfoApi = new PushDeviceInfoApi(context, token, type);
        doPostRequest(pushDeviceInfoApi, pushDeviceInfoApi.getError(), FetchApiType.USE_HTTP_ONLY, listener);

    }

    /**
     * @param c
     */
    public void getAllList(Context c, FetchEntryListener listener) {
        GetAllCalenderListApi getAllListApi = new GetAllCalenderListApi(c);
        doPostRequest(getAllListApi, getAllListApi.getData(), USE_HTTP_ONLY, listener);
    }

    /**
     * @param c
     */
    public void getCitys(Context c, FetchEntryListener listener) {
        GetCityListApi getUserCitysApi = new GetCityListApi(c);
        doPostRequest(getUserCitysApi, getUserCitysApi.getData(), USE_HTTP_ONLY, listener);
    }

    /**
     * 获取推荐列表
     *
     * @param c
     * @param listener
     */
    public void getRecommondList(Context c, FetchEntryListener listener) {
        GetSomeListApi getRecommendedListApi = new GetSomeListApi(c, TAG_TYPE.RECOMMEND, "", "");
        doPostRequest(getRecommendedListApi, getRecommendedListApi.getCalendarListModel(), USE_CACHE_FIRST, listener);
    }


    /**
     * 获取周边列表
     *
     * @param c
     * @param listener
     */
    public void getNearList(Context c, String latitude, String longitude, FetchEntryListener listener) {
        GetSomeListApi getRecommendedListApi = new GetSomeListApi(c, TAG_TYPE.NEAR, latitude, longitude);
        doPostRequest(getRecommendedListApi, getRecommendedListApi.getCalendarListModel(), USE_CACHE_FIRST, listener);
    }

    /**
     * 获取分享id
     *
     * @param c
     * @param listener
     */
    public void getShareId(Context c, String title, FetchEntryListener listener) {
        GetShareIdApi getShareIdApi = new GetShareIdApi(c, title);
        doPostRequest(getShareIdApi, getShareIdApi.getData(), USE_HTTP_ONLY, listener);
    }

    public void getMuseumList(Context c, FetchEntryListener listener) {
        GetAllMuseumApi getAllMuseumApi = new GetAllMuseumApi(c);
        doPostRequest(getAllMuseumApi, getAllMuseumApi.getData(), USE_HTTP_ONLY, listener);
    }


    public void saveMyCitys(Context c, String sss, FetchEntryListener listener) {
        SaveMyCitysApi addCitysApi = new SaveMyCitysApi(c, sss);
        doPostRequest(addCitysApi, addCitysApi.getData(), USE_HTTP_ONLY, listener);
    }

    /**
     * 修改用户密码
     *
     * @param password    旧密码
     * @param newPassword 新密码
     * @param listener    view数据回调接口
     */
    public void modifyUserPassword(Context c, String password, String newPassword, FetchEntryListener listener) {
        ModifyUserPasswordApi operate = new ModifyUserPasswordApi(c, password, newPassword);
        doPostRequest(operate, operate.getUser(), USE_HTTP_ONLY, listener);
    }

    /**
     * 支付之后更新服务器订单状态
     */

    public static void notifyServer(final String statuStr, final String type, FetchDataListener listener) {
        String data = DataHelper.getOrder(mContext, type);
        if (TextUtils.isEmpty(data)) return;

        try {
            JSONObject jsonObject = new JSONObject(data);
            // 如果状态为空，则是入版更新，取本地订单状态更新
            if (!TextUtils.isEmpty(statuStr)) jsonObject.put("status", statuStr);
            jsonObject.put("appid", CommonApplication.APP_ID);
            jsonObject.put("marketkey", CommonApplication.CHANNEL);

            DataHelper.setOrder(mContext, type, jsonObject.toString());// 更新本地订单状态
            int urltype = type.equals(NEW_WEIXIN_KEY) ? 1 : 2;
            HttpsController.getInstance(mContext).requestHttpAsycle(true, UrlMaker.newUpdateOrderStatus(urltype), jsonObject.toString(), listener);

        } catch (JSONException e) {

        }
    }

    /**
     * 支付之后更新服务器订单状态
     */

    public static void notifyServer(final String statuStr, final String type) {
        String data = DataHelper.getOrder(mContext, type);
        if (TextUtils.isEmpty(data)) return;

        try {
            JSONObject jsonObject = new JSONObject(data);
            // 如果状态为空，则是入版更新，取本地订单状态更新
            if (!TextUtils.isEmpty(statuStr)) jsonObject.put("status", statuStr);
            jsonObject.put("appid", CommonApplication.APP_ID);
            jsonObject.put("marketkey", CommonApplication.CHANNEL);

            DataHelper.setOrder(mContext, type, jsonObject.toString());// 更新本地订单状态


            int urltype = type.equals(NEW_WEIXIN_KEY) ? 1 : 2;

            HttpsController.getInstance(mContext).requestHttpAsycle(true, UrlMaker.newUpdateOrderStatus(urltype), jsonObject.toString(), new FetchDataListener() {
                @Override
                public void fetchData(boolean isSuccess, String data, boolean fromHttp) {
                    if (isSuccess) {
                        DataHelper.clearOrder(mContext, type);
                        if (TextUtils.isEmpty(statuStr) || statuStr.equals(SUCCESS))
                            saveLevel(data);
                    }
                }
            });

        } catch (JSONException e) {

        }

    }


    /**
     * 修改vip邮寄地址
     */
    public static void addressEdit(String name, String phone, String city, String address, String code, String id, FetchDataListener listener) {
        JSONObject object = new JSONObject();
        try {
            object.put("uid", DataHelper.getUid(mContext));
            object.put("token", DataHelper.getToken(mContext));
            object.put("appid", CommonApplication.APP_ID);
            object.put("name", name);
            object.put("phone", phone);
            object.put("city", city);
            object.put("address", address);
            object.put("code", code);
            object.put("id", Integer.parseInt(id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpsController.getInstance(mContext).requestHttpAsycle(true, UrlMaker.editAddress(), object.toString(), listener);
    }


    /**
     * 添加vip邮寄地址
     */
    public static void addressAdd(String name, String phone, String city, String address, String code, FetchDataListener listener) {
        JSONObject object = new JSONObject();
        try {
            object.put("uid", DataHelper.getUid(mContext));
            object.put("token", DataHelper.getToken(mContext));
            object.put("appid", CommonApplication.APP_ID);
            object.put("name", name);
            object.put("phone", phone);
            object.put("city", city);
            object.put("address", address);
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpsController.getInstance(mContext).requestHttpAsycle(true, UrlMaker.addAddress(), object.toString(), listener);
    }


    /**
     * 获取套餐列表
     */
    public static void getProducts(  FetchDataListener listener) {
        JSONObject object = new JSONObject();
        try {
            object.put("uid", DataHelper.getUid(mContext));
            object.put("token", DataHelper.getToken(mContext));
            object.put("appid", CommonApplication.APP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpsController.getInstance(mContext).requestHttpAsycle(true, UrlMaker.getVipProducts(), object
                .toString(), listener);
    }

    /**
     * 获取vip邮寄地址
     */
    public static void addressList(FetchDataListener listener) {
        JSONObject object = new JSONObject();
        try {
            object.put("uid", DataHelper.getUid(mContext));
            object.put("token", DataHelper.getToken(mContext));
            object.put("appid", CommonApplication.APP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpsController.getInstance(mContext).requestHttpAsycle(true, UrlMaker.listddress(), object.toString(), listener);
    }

    /**
     * 支付成功 ||微信回调出错
     *
     * @param data
     */
    public static int saveLevel(String data) {
        String token = DataHelper.getToken(mContext);
        String key = token.substring(token.length() - 8, token.length());// 解析的key
        String json = DESCoder.decode(key, data);
        // 存储解密之后的明文权限
        DataHelper.saveBusinessWeekCrt(mContext, json);
        // 解析付费状态
        int status = 0;
        if (json != null) try {
            JSONObject jsonObject = new JSONObject(json);
            Log.e("getUserPermission", json);
            /**
             * 解析服务器返回的权限值{ status= (已验证-成功) 2 || (已验证-假订单) 4 }刷新本地存储
             */
            status = jsonObject.optInt("status");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;

    }




    /**
     * 增删改行程
     *
     * @param c
     * @param type
     * @param id
     * @param img
     * @param time
     * @param listener
     */
    public void handleFav(Context c, int type, String id, String img, String time, FetchEntryListener listener) {
        HandleFavApi handleFavApi = new HandleFavApi(c, type, id, img, time);
        doPostRequest(handleFavApi, handleFavApi.getData(), USE_HTTP_ONLY, listener);
    }


    private static interface AfterCallBack {
        public void afterCallBack(Entry entry, boolean fromHttp);

    }

}