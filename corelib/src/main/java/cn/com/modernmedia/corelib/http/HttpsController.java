package cn.com.modernmedia.corelib.http;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.com.modernmedia.corelib.listener.FetchDataListener;
import cn.com.modernmedia.corelib.util.Tools;

/**
 * https 请求专用controller
 * Created by Eva. on 2017/10/25.
 */

public class HttpsController {


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


    private static HttpsController instance;

    private static Context mContext;

    private HttpsController(Context context) {
        mContext = context;
    }

    public static synchronized HttpsController getInstance(Context context) {
        mContext = context;
        if (instance == null) instance = new HttpsController(context);
        return instance;
    }

    /**
     * 异步请求服务器数据
     *
     * @param isPost
     */
    public void requestHttpAsycle(final boolean isPost, final String url, final String data, final FetchDataListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isPost) requestHttpPost(url, data, listener);
            }
        }).start();
    }


    /**
     * 支付之后更新服务器订单状态
     */


    /**
     * 同步请求服务器数据:post
     */
    public static void requestHttpPost(final String url, String param, FetchDataListener listener) {
        Request request = null;
        try {

            OkHttpClient client = new OkHttpClient();
            client.setSslSocketFactory(createSSLSocketFactory());
            client.setHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;

                }
            });
            RequestBody requestBody = null;
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("data", param).build();
            request = new Request.Builder().headers(Tools.getRequastHeader(mContext)).url(url).post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            String resData = null;
            if (response.isSuccessful()) {
                resData = response.body().string();
            }
            if (resData == null) {
                return;
            }
            if (listener != null) listener.fetchData(true, resData, true);
        } catch (IOException e) {
            Log.e("http IOException", e.toString());
        } finally {
            if (request != null) request.cacheControl();
        }

    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            // Create a trust manager that does not validate certificate chans
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }


}