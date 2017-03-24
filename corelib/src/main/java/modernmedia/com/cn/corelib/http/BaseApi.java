package modernmedia.com.cn.corelib.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import modernmedia.com.cn.corelib.http.HttpRequestController.RequestThread;
import modernmedia.com.cn.corelib.listener.DataCallBack;
import modernmedia.com.cn.corelib.listener.FetchDataListener;
import modernmedia.com.cn.corelib.util.TimeCollectUtil;
import modernmedia.com.cn.corelib.util.Tools;


/**
 * 封装服务器返回数据父类
 * <p/>
 * Created by Eva. on 16/9/16.
 */
public abstract class BaseApi {
    private HttpRequestController mController = HttpRequestController.getInstance();
    private Context mContext;
    private boolean success = false;// 是否解析成功
    private boolean isUploadAvatar = false;// 是否是上传头像
    private DataCallBack callBack;
    protected FetchApiType mFetchApiType = FetchApiType.USE_HTTP_FIRST;

    /**
     * 缓存文件是否为数据库
     */
    protected boolean cacheIsDb = false;

    public void setIsUpload(boolean isUploadAvatar) {
        this.isUploadAvatar = isUploadAvatar;
    }

    public static enum FetchApiType {
        /**
         * 优先获取服务器数据
         */
        USE_HTTP_FIRST, /**
         * 优先获取缓存
         */
        USE_CACHE_FIRST, /**
         * 只获取服务器数据
         */
        USE_HTTP_ONLY, /**
         * 只获取缓存数据
         */
        USE_CACHE_ONLY
    }

    public static class CallBackData {
        public boolean success = false;
        public String data = "";
    }

    /**
     * 缓存数据回调接口
     *
     * @author zhuqiao
     */
    protected static interface CacheCallBack {
        public void onCallBack(CallBackData callBackData);
    }

    /**
     * 由子类提供
     */
    protected abstract String getUrl();

    /**
     * 由子类提供
     */
    protected JSONObject getPostParams() {
        return null;
    }

    /**
     * 由子类提供
     */
    protected String getPostImagePath() {
        return null;
    }


    /**
     * 以get方式请求服务器，并解析Json数据
     *
     * @param context
     * @param fetchApiType 是否优先使用本地数据
     * @param callBack
     */
    public void asyncRequest(Context context, FetchApiType fetchApiType, DataCallBack callBack) {
        mContext = context;
        this.callBack = callBack;
        if (TextUtils.isEmpty(getUrl()) || callBack == null) {
            // TODO 提示错误信息
            return;
        }
        requestCache(fetchApiType, false);
    }

    /**
     * 以post方式请求服务器，并解析Json数据
     *
     * @param context
     * @param fetchApiType 是否优先使用本地数据
     * @param callBack
     */
    public void asyncRequestByPost(Context context, FetchApiType fetchApiType, DataCallBack callBack) {
        mContext = context;
        this.callBack = callBack;
        if (TextUtils.isEmpty(getUrl()) || callBack == null) {
            // TODO 提示错误信息
            return;
        }
        requestCache(fetchApiType, true);
    }

    /**
     * 请求缓存数据
     *
     * @param fetchApiType
     * @param isPost
     */
    private void requestCache(final FetchApiType fetchApiType, final boolean isPost) {
        mFetchApiType = fetchApiType;

        if (mFetchApiType == FetchApiType.USE_HTTP_FIRST || mFetchApiType == FetchApiType.USE_HTTP_ONLY) {
            requestHttp(fetchApiType, isPost);
            return;
        }

        fetchDataFromCache(new CacheCallBack() {

            @Override
            public void onCallBack(CallBackData callBackData) {
                if (callBackData == null) {
                    requestHttp(fetchApiType, isPost);
                    return;
                }
                if (callBackData.success) {
                    // 有效的缓存数据, 回调
                    doCacheCallBack(true, callBackData.data);
                    return;
                }
                if (mFetchApiType == FetchApiType.USE_CACHE_ONLY) {
                    // 缓存无效，并且只取缓存的形式，那么回调错误
                    doCacheCallBack(false, null);
                } else {
                    requestHttp(fetchApiType, isPost);
                }
            }
        });
    }

    /**
     * 请求服务器数据
     *
     * @param fetchApiType 获取接口数据形式
     * @param isPost
     */
    private void requestHttp(FetchApiType fetchApiType, boolean isPost) {
        String url = getUrl();
        RequestThread thread = new RequestThread(mContext, url, this);

        if (isPost) {
            thread.setPost(true);
            thread.setPostParams(getPostParams());
            thread.setImagePath(getPostImagePath());
        }
        thread.setHeaderMap(Tools.getRequastHeader(mContext));
        thread.setUserAgent("exhibitioncalendar" + Tools.getAppVersion(mContext));
        thread.setmFetchDataListener(new FetchDataListener() {

            @Override
            public void fetchData(boolean isSuccess, String data, boolean fromHttp) {
                if (isSuccess && !TextUtils.isEmpty(data)) {
                    // NOTE
                    // 如果获取成功，那么执行handler;否则，会去执行fetchDataFromCacheByNetError这个方法
                    handlerData(isSuccess, data, fromHttp);
                }
            }
        });
        mController.fetchHttpData(thread);
    }

    /**
     * 解析数据
     *
     * @param isSuccess
     * @param data
     * @param fromHttp
     */
    private void handlerData(boolean isSuccess, String data, boolean fromHttp) {
        if (isSuccess) {
            if (TextUtils.isEmpty(data)) {
                Log.e("********", "网络出错111" + getUrl());
                // showToast(R.string.net_error);
            } else {
                try {
                    if (isUploadAvatar) {
                        JSONObject avatar = new JSONObject();
                        avatar.put("avatar", data);
                        handler(avatar);
                        saveData(data);
                        success = true;
                    } else {
                        if (data.equals("[]")) {
                            data = "{}";
                        }

                        JSONObject obj = new JSONObject(data);
                        if (isNull(obj)) {
                            Log.e("********", "网络出错222" + getUrl());
                            // showToast(R.string.net_error);
                        } else {
                            handler(obj);
                            saveData(data);
                            success = true;
                        }

                    }
                } catch (JSONException e) {
                    Log.e(getUrl(), ":can not transform to jsonobject");
                    e.printStackTrace();
                    Log.e("网络出错3333", data);
                    // showToast(R.string.net_error);
                }
            }
        } else {
            Log.e("********", "网络出错444" + getUrl());
            //			showToast(R.string.net_error);
        }
        if (callBack != null) callBack.callback(success, fromHttp);
    }

    /**
     * 从缓存中获取数据(优先使用或者只能使用缓存)
     *
     * @return
     */
    private void fetchDataFromCache(CacheCallBack cacheCallBack) {
        if (cacheCallBack == null) return;
        if (cacheIsDb) {
            fetchDataFromDB(cacheCallBack);
        } else {
            fetchDataFromSD(cacheCallBack);
        }
    }

    /**
     * 接口请求失败，尝试获取缓存
     */
    protected void fetchDataFromCacheByNetError(int responseCode) {
        TimeCollectUtil collect = TimeCollectUtil.getInstance();
        if (mFetchApiType == FetchApiType.USE_HTTP_ONLY) {
            collect.saveRequestTime(getUrl(), false, responseCode, false);
            // 如果只能从网络获取，那么直接返回错误
            Log.e("net error:", getUrl());
            doCacheCallBack(false, null);
            return;
        }
        if (mFetchApiType == FetchApiType.USE_CACHE_FIRST) {
            collect.saveRequestTime(getUrl(), false, responseCode, false);
            // 如果缓存优先，说明一开始已经获取过缓存了，那么直接返回错误
            Log.e("net error:", getUrl());
            doCacheCallBack(false, null);
            return;
        }
        // NOTE 本身已经在子线程中了，所以不需要重新开启子线程了
        CallBackData result = cacheIsDb ? fetchDataFromDB() : fetchDataFromSD();
        doCacheCallBack(result.success, result.data);

        collect.saveRequestTime(getUrl(), false, responseCode, result.success);
    }

    /**
     * 缓存回调给调用者
     *
     * @param success
     * @param data
     */
    private void doCacheCallBack(boolean success, String data) {
        if (cacheIsDb) {
            if (!success) {
                // showToast(R.string.net_error);
                Log.e("********", "网络出错99999" + getUrl());
            }
            if (callBack != null) {
                callBack.callback(success, false);
            }
        } else {
            handlerData(success, data, false);
        }
    }

    /**
     * 从sd卡获取数据
     *
     * @param cacheCallBack
     */
    private void fetchDataFromSD(final CacheCallBack cacheCallBack) {
        if (cacheCallBack == null) return;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                cacheCallBack.onCallBack(fetchDataFromSD());
            }
        });
        thread.start();
        thread = null;
    }

    /**
     * 从数据库获取数据
     *
     * @param cacheCallBack
     */
    private void fetchDataFromDB(final CacheCallBack cacheCallBack) {
        if (cacheCallBack == null) return;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                cacheCallBack.onCallBack(fetchDataFromDB());
            }
        });
        thread.start();
        thread = null;
    }

    /**
     * 从sd卡获取数据(由统一的子类提供)
     *
     * @return
     */
    protected CallBackData fetchDataFromSD() {
        return new CallBackData();
    }

    /**
     * 从数据库获取数据(由特定的子类提供)
     *
     * @return
     */
    protected CallBackData fetchDataFromDB() {
        return new CallBackData();
    }

    /**
     * 由子类去解析json数据
     *
     * @param jsonObject
     */
    protected abstract void handler(JSONObject jsonObject);

    /**
     * 解析完保存数据
     *
     * @param data
     */
    protected abstract void saveData(String data);

    public Context getmContext() {
        return mContext;
    }

    /**
     * JSONObject是否为空
     *
     * @param object
     * @return
     */
    protected boolean isNull(JSONObject object) {
        return JSONObject.NULL.equals(object) || object == null;
    }

    /**
     * JSONArray是否为空
     *
     * @param array
     * @return
     */
    protected boolean isNull(JSONArray array) {
        return JSONObject.NULL.equals(array) || array == null || array.length() == 0;
    }

    /**
     * 将Value值进行UTF-8编码
     *
     * @param obj
     * @param key
     * @param value
     * @throws Exception
     */
    protected void addPostParams(JSONObject obj, String key, String value) throws Exception {
        if (!TextUtils.isEmpty(value)) {
            String encode = URLEncoder.encode(value, "UTF-8");
            // 数据中含换行符时，不能编码，否则服务器端在解析该json时会无法解析
            String br = URLEncoder.encode("\n", "UTF-8");
            if (encode.contains(br)) {
                encode = encode.replace(br, "\n");
            }
            obj.put(key, encode);
        }
    }
}