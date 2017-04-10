package modernmedia.com.cn.corelib.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import modernmedia.com.cn.corelib.listener.FetchDataListener;
import modernmedia.com.cn.corelib.util.TimeCollectUtil;


/**
 * Created by Eva. on 16/9/16.
 */
public class HttpRequestController {
    /**
     * 连接超时时间
     **/
    public static final int CONNECT_TIMEOUT = 10 * 1000;
    /**
     * 读取数据超时时间
     **/
    public static final int READ_TIMEOUT = 10 * 1000;
    /**
     * 读取数据buffer大小
     **/
    public static final int BUFFERSIZE = 1024;
    private static HttpRequestController instance = null;

    private HttpRequestController() {
    }

    public static HttpRequestController getInstance() {
        if (instance == null) {
            instance = new HttpRequestController();
        }
        return instance;
    }

    public void fetchHttpData(RequestThread requestThread) {
        requestThread.start();
    }

    /**
     * 读取网络接口子线程
     *
     * @author ZhuQiao
     */
    public static class RequestThread extends Thread {
        @SuppressWarnings("unused")
        private Context context;
        private URL mUrl = null;
        private FetchDataListener mFetchDataListener;
        private String url = "";
        private boolean isPost = false; // 默认用GET方式
        private JSONObject jsonParams; // post用参数
        private String imagePath; // 要上传的图片存储路径(目前仅支持post方式)
        private BaseApi mOperate;
        private String userAgent = "";// iweekly统计广告使用
        private Map<String, String> headerMap = new HashMap<String, String>();
        private int responseCode = -1;

        public RequestThread(Context context, String url, BaseApi baseOperate) {
            this.context = context;
            this.url = url == null ? "" : url;
            mOperate = baseOperate;
            if (!TextUtils.isEmpty(url)) {
                try {
                    mUrl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setmFetchDataListener(FetchDataListener mFetchDataListener) {
            this.mFetchDataListener = mFetchDataListener;
        }

        protected boolean isPost() {
            return isPost;
        }

        public void setPost(boolean isPost) {
            this.isPost = isPost;
        }

        protected void setPostParams(JSONObject nameValuePairs) {
            this.jsonParams = nameValuePairs;
        }

        protected void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public void setHeaderMap(Map<String, String> headerMap) {
            if (headerMap != null && !headerMap.isEmpty()) {
                this.headerMap = headerMap;
            }
        }

        @Override
        public void run() {
            if (mUrl == null) {
                return;
            }
            // time collect
            TimeCollectUtil.getInstance().saveRequestTime(url, true, 0, false);
            Log.d("http", url);
            if (isPost) {
                doPost();
            } else {
                doGet();
            }
        }

        private void doGet() {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) mUrl.openConnection();
                Log.e("流量bug查询**", "HttpRequestController:doGet()" + "-----" + url);
                if (!TextUtils.isEmpty(userAgent)) {
                    conn.addRequestProperty("User-Agent", userAgent);
                }
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);
                // 添加头部信息
                Iterator<String> iterator = headerMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = headerMap.get(key);
                    if (!TextUtils.isEmpty(key)) {
                        conn.setRequestProperty(key, value);
                    }
                }
                responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    InputStream is = conn.getInputStream();
                    if (is == null) {
                        fetchLocalDataInBadNet();
                        return;
                    }
                    String data = receiveData(is);
                    if (TextUtils.isEmpty(data)) {
                        fetchLocalDataInBadNet();
                        return;
                    }
                    // time collect
                    TimeCollectUtil.getInstance().saveRequestTime(url, false, 200, false);
                    showToast("from http:" + url);
                    if (mFetchDataListener != null) mFetchDataListener.fetchData(true, data, true);
                    reSetUpdateTime();
                } else {
                    fetchLocalDataInBadNet();
                }
            } catch (Exception e) {
                fetchLocalDataInBadNet();
                if (e != null && !TextUtils.isEmpty(e.getMessage()))
                    Log.e("HttpRequestController", e.getMessage());
            } finally {
                if (conn != null) conn.disconnect();
            }
        }

        private void doPost() {
            Request request = null;
            try {

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = null;
                /**
                 * 提交图片文件
                 */
                if (!TextUtils.isEmpty(imagePath)) {
                    Log.e("上传图片：", imagePath);
                    // 上传图片处理
                    MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
                    MediaType mediaType = MediaType.parse("image/jpg;charset=utf-8");
                    builder.addFormDataPart("image", imagePath, RequestBody.create(mediaType, new File(imagePath)));
                    //这里的img就是文件的键名，服务器可以根据这个获取
                    requestBody = builder.build();
                    //                    requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addPart(Headers.of("Content-Disposition", "form-data; name=\"image\""), RequestBody.create(MediaType.parse("image/jpg"), f)).build();
                }
                /**
                 * 提交参数表单
                 */
                if (jsonParams != null)

                    requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("data", jsonParams.toString()).build();

                request = new Request.Builder().url(url).post(requestBody).build();
                /**
                 * add请求头
                 */
                Iterator<String> iterator = headerMap.keySet().iterator();
                Headers headers = null;
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = headerMap.get(key);
                    if (!TextUtils.isEmpty(key)) {
                        //                        headers = new Headers.Builder().addHeader(value, key);

                    }
                }
                Response response = client.newCall(request).execute();
                String resData = null;
                if (response.isSuccessful()) {
                    resData = response.body().string();
                }
                if (resData == null) {

                    fetchLocalDataInBadNet();
                    return;
                }
                TimeCollectUtil.getInstance().saveRequestTime(url, false, 200, false);
                //                Log.e("http 返回", resData);
                if (mFetchDataListener != null) mFetchDataListener.fetchData(true, resData, true);
                reSetUpdateTime();
            } catch (IOException e) {
                Log.e("http IOException", e.toString());
                fetchLocalDataInBadNet();
            } finally {
                if (request != null) request.cacheControl();
            }
        }

        private String receiveData(InputStream is) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                byte[] buff = new byte[BUFFERSIZE];
                int readed = -1;
                while ((readed = is.read(buff)) != -1) {
                    baos.write(buff, 0, readed);
                }
                byte[] result = baos.toByteArray();
                if (result == null) return null;
                return new String(result);
            } finally {
                if (is != null) is.close();
                if (baos != null) baos.close();
            }
        }

        private void fetchLocalDataInBadNet() {
            if (mOperate != null) mOperate.fetchDataFromCacheByNetError(responseCode);
            // 给除了SlateBaseOperate之外的别的可能需要调用http请求的方法回调
            if (mFetchDataListener != null) mFetchDataListener.fetchData(false, null, false);
        }


        private void reSetUpdateTime() {
        }

        private void showToast(String str) {
        }


    }


}