package modernmedia.com.cn.exhibitioncalendar.api;

import android.content.Context;
import android.os.Handler;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.http.BaseApi.FetchApiType;
import modernmedia.com.cn.corelib.listener.DataCallBack;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.corelib.model.UserModel;

import static modernmedia.com.cn.corelib.http.BaseApi.FetchApiType.USE_CACHE_FIRST;
import static modernmedia.com.cn.corelib.http.BaseApi.FetchApiType.USE_HTTP_ONLY;

/**
 * Created by Eva. on 17/3/30.
 */

public class ApiController {
    private static ApiController instance;
    private static Context mContext;

    private Handler mHandler = new Handler();

    private ApiController(Context context) {
        mContext = context;
    }

    public static synchronized ApiController getInstance(Context context) {
        mContext = context;
        if (instance == null) instance = new ApiController(context);
        return instance;
    }

    private static interface AfterCallBack {
        public void afterCallBack(Entry entry, boolean fromHttp);

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
    public void getDetail(Context c, String id, FetchEntryListener fetchEntryListener) {
        GetDetailApi getDetailApi = new GetDetailApi(c, id);
        doPostRequest(getDetailApi, getDetailApi.getDetail(), USE_HTTP_ONLY, fetchEntryListener);
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
    public void modifyUserInfo(String uid, String token, String userName, String nickName, String url, String password, String desc, boolean pushEmail, FetchEntryListener listener) {
        ModifyUserInfoApi operate = new ModifyUserInfoApi(uid, token, userName, nickName, url, password, desc, pushEmail);
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
     * @param c
     */
    public void getAllList(Context c, FetchEntryListener listener) {
        GetAllListApi getAllListApi = new GetAllListApi(c);
        doPostRequest(getAllListApi, getAllListApi.getData(), USE_HTTP_ONLY, listener);
    }

    /**
     * @param c
     */
    public void getCitys(Context c, FetchEntryListener listener) {
        GetUserCitysApi getUserCitysApi = new GetUserCitysApi(c);
        doPostRequest(getUserCitysApi, getUserCitysApi.getData(), USE_HTTP_ONLY, listener);
    }

    /**
     * 获取推荐列表
     *
     * @param c
     * @param listener
     */
    public void getRecommondList(Context c, FetchEntryListener listener) {
        GetRecommendedListApi getRecommendedListApi = new GetRecommendedListApi(c);
        doPostRequest(getRecommendedListApi, getRecommendedListApi.getCalendarListModel(), USE_CACHE_FIRST, listener);
    }


    public void handleFav(Context c, int type, String id, String img, String time,
                          FetchEntryListener listener){
        HandleFavApi handleFavApi = new HandleFavApi(c,type,id,img,time);
        doPostRequest(handleFavApi, handleFavApi.getData(),
                USE_HTTP_ONLY,
                listener);
    }

}