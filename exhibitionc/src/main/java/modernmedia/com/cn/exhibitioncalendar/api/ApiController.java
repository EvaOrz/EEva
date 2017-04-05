package modernmedia.com.cn.exhibitioncalendar.api;

import android.content.Context;
import android.os.Handler;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.http.BaseApi.FetchApiType;
import modernmedia.com.cn.corelib.listener.DataCallBack;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;

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
     * 登录
     *
     * @param context
     * @param userName
     * @param password
     * @param listener
     */
    public void login(Context context, String userName, String password, FetchEntryListener listener) {
        LoginApi loginApi = new LoginApi();
        doPostRequest(loginApi, loginApi.getUserModel(), FetchApiType.USE_HTTP_ONLY, listener);
    }

    /**
     * 获取广告列表
     *
     * @param listener
     */
    public void getAdvList(FetchApiType fetchApiType ,FetchEntryListener listener) {
        GetAdvListApi getAdvListApi = new GetAdvListApi();
        doGetRequest(getAdvListApi, getAdvListApi.getAdvList(), fetchApiType, listener);
    }

}