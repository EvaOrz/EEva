/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.modernmedia.corelib.util.sina.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.BitmapHelper;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.NetworkHelper;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * HTTP 请求类，用于管理通用的 HTTP 请求、图片上传下载等。 TODO：（To be design...）
 *
 * @author SINA
 * @since 2013-11-05
 */
public class HttpManager {

    // private static final String BOUNDARY = "7cd4a6d158c";
    private static final String BOUNDARY = getBoundry();
    private static final String MP_BOUNDARY = "--" + BOUNDARY;
    private static final String END_MP_BOUNDARY = "--" + BOUNDARY + "--";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    private static final String HTTPMETHOD_POST = "POST";
    private static final String HTTPMETHOD_GET = "GET";

    private static final int SET_CONNECTION_TIMEOUT = 5 * 1000;
    private static final int SET_SOCKET_TIMEOUT = 20 * 1000;

    /**
     * 根据 URL 异步请求数据。
     *
     * @param url    请求的地址
     * @param method "GET" or "POST"
     * @param params 存放参数的容器
     * @param file   文件路径，如果 是发送带有照片的微博的话，此参数为图片在 SdCard 里的绝对路径
     * @return 返回响应结果
     * @throws WeiboException 如果发生错误，则以该异常抛出
     */
    public static String openUrl(String url, String method, WeiboParameters params, String file) throws WeiboException {
        String result = "";
        //        try {
        //            HttpClient client = getNewHttpClient();
        //            HttpUriRequest request = null;
        //            ByteArrayOutputStream bos = null;
        //            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());
        //            if (method.equals(HTTPMETHOD_GET)) {
        //                url = url + "?" + encodeUrl(params);
        //                HttpGet get = new HttpGet(url);
        //                request = get;
        //            } else if (method.equals(HTTPMETHOD_POST)) {
        //                HttpPost post = new HttpPost(url);
        //                request = post;
        //                byte[] data = null;
        //                String _contentType = params.getValue("content-type");
        //
        //                bos = new ByteArrayOutputStream();
        //                if (!TextUtils.isEmpty(file)) {
        //                    paramToUpload(bos, params);
        //                    post.setHeader("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
        //                    // 不在进行图片压缩
        //                    // Utility.UploadImageUtils.revitionPostImageSize(file);
        //                    imageContentToUpload(bos, file);
        //                } else {
        //                    if (_contentType != null) {
        //                        params.remove("content-type");
        //                        post.setHeader("Content-Type", _contentType);
        //                    } else {
        //                        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        //                    }
        //
        //                    String postParam = encodeParameters(params);
        //                    data = postParam.getBytes("UTF-8");
        //                    bos.write(data);
        //                }
        //                data = bos.toByteArray();
        //                bos.close();
        //                ByteArrayEntity formEntity = new ByteArrayEntity(data);
        //                post.setEntity(formEntity);
        //            } else if (method.equals("DELETE")) {
        //                request = new HttpDelete(url);
        //            }
        //            HttpResponse response = client.execute(request);
        //            StatusLine status = response.getStatusLine();
        //            int statusCode = status.getStatusCode();
        //
        //            if (statusCode != 200) {
        //                result = readHttpResponse(response);
        //                throw new WeiboHttpException(result, statusCode);
        //            }
        //            result = readHttpResponse(response);
        return result;
        //        } catch (IOException e) {
        //            throw new WeiboException(e);
        //        }
    }

    /**
     * 由于发私信和发微博对 formdata name="pic" 还是 name="file" 要求不一致，所以提供一个新的接口。
     *
     * @param url    请求的地址
     * @param method "GET" or "POST"
     * @param params 存放参数的容器
     * @param file   文件路径，如果是发送带有照片的微博的话，此参数为图片在 SdCard 里的绝对路径
     * @return 返回响应结果
     * @throws WeiboException 如果发生错误，则以该异常抛出
     */
    public static String uploadFile(String url, String method, WeiboParameters params, String file) throws WeiboException {
        String result = "";
        //        try {
        //            HttpClient client = getNewHttpClient();
        //            HttpUriRequest request = null;
        //            ByteArrayOutputStream bos = null;
        //            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());
        //            if (method.equals(HTTPMETHOD_GET)) {
        //                url = url + "?" + encodeUrl(params);
        //                HttpGet get = new HttpGet(url);
        //                request = get;
        //            } else if (method.equals(HTTPMETHOD_POST)) {
        //                HttpPost post = new HttpPost(url);
        //                request = post;
        //                byte[] data = null;
        //                String _contentType = params.getValue("content-type");
        //
        //                bos = new ByteArrayOutputStream();
        //                if (!TextUtils.isEmpty(file)) {
        //                    paramToUpload(bos, params);
        //                    post.setHeader("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
        //                    // 不在进行图片压缩
        //                    // Utility.UploadImageUtils.revitionPostImageSize(file);
        //                    fileToUpload(bos, file);
        //                } else {
        //                    if (_contentType != null) {
        //                        params.remove("content-type");
        //                        post.setHeader("Content-Type", _contentType);
        //                    } else {
        //                        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        //                    }
        //
        //                    String postParam = encodeParameters(params);
        //                    data = postParam.getBytes("UTF-8");
        //                    bos.write(data);
        //                }
        //                data = bos.toByteArray();
        //                bos.close();
        //                ByteArrayEntity formEntity = new ByteArrayEntity(data);
        //                post.setEntity(formEntity);
        //            } else if (method.equals("DELETE")) {
        //                request = new HttpDelete(url);
        //            }
        //            HttpResponse response = client.execute(request);
        //            StatusLine status = response.getStatusLine();
        //            int statusCode = status.getStatusCode();
        //
        //            if (statusCode != 200) {
        //                result = readHttpResponse(response);
        //                throw new WeiboHttpException(result, statusCode);
        //            }
        //            result = readHttpResponse(response);
        return result;
        //        } catch (IOException e) {
        //            throw new WeiboException(e);
        //        }
    }

    /**
     * 根据 URL 异步请求数据。
     *
     * @param context 应用程序上下文
     * @param url     请求的地址
     * @param method  "GET" or "POST"
     * @param params  存放参数的容器
     * @param file    文件路径，如果是发送带有照片的微博的话，此参数为图片在 SdCard 里的绝对路径
     * @return 返回响应结果
     * @throws WeiboException 如果发生错误，则以该异常抛出
     */
    public static ByteArrayOutputStream openUrl4Binary(Context context, String url, String method, WeiboParameters params, String file) throws WeiboException {
        ByteArrayOutputStream result = null;
        //        try {
        //            HttpClient client = getNewHttpClient();
        //            HttpUriRequest request = null;
        //            ByteArrayOutputStream bos = null;
        //            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());
        //            if (method.equals(HTTPMETHOD_GET)) {
        //                url = url + "?" + encodeUrl(params);
        //                HttpGet get = new HttpGet(url);
        //                request = get;
        //            } else if (method.equals(HTTPMETHOD_POST)) {
        //                HttpPost post = new HttpPost(url);
        //                request = post;
        //                byte[] data = null;
        //                String _contentType = params.getValue("content-type");
        //
        //                bos = new ByteArrayOutputStream();
        //                if (!TextUtils.isEmpty(file)) {
        //                    paramToUpload(bos, params);
        //                    post.setHeader("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
        //                    UploadImageUtils.revitionPostImageSize(context, file);
        //                    imageContentToUpload(bos, file);
        //                } else {
        //                    if (_contentType != null) {
        //                        params.remove("content-type");
        //                        post.setHeader("Content-Type", _contentType);
        //                    } else {
        //                        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        //                    }
        //
        //                    String postParam = encodeParameters(params);
        //                    data = postParam.getBytes("UTF-8");
        //                    bos.write(data);
        //                }
        //                data = bos.toByteArray();
        //                bos.close();
        //                ByteArrayEntity formEntity = new ByteArrayEntity(data);
        //                post.setEntity(formEntity);
        //            } else if (method.equals("DELETE")) {
        //                request = new HttpDelete(url);
        //            }
        //            HttpResponse response = client.execute(request);
        //            StatusLine status = response.getStatusLine();
        //            int statusCode = status.getStatusCode();
        //
        //            if (statusCode != 200) {
        //                String resultStr = readHttpResponse(response);
        //                throw new WeiboHttpException(resultStr, statusCode);
        //            }
        //            result = readBytesFromHttpResponse(response);
        return result;
        //        } catch (IOException e) {
        //            throw new WeiboException(e);
        //        }
    }

    //    private static HttpClient getNewHttpClient() {
    //        try {
    //            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    //            trustStore.load(null, null);
    //
    //            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
    //            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    //
    //            HttpParams params = new BasicHttpParams();
    //
    //            HttpConnectionParams.setConnectionTimeout(params, 10000);
    //            HttpConnectionParams.setSoTimeout(params, 10000);
    //
    //            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    //            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
    //
    //            SchemeRegistry registry = new SchemeRegistry();
    //            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    //            registry.register(new Scheme("https", sf, 443));
    //
    //            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
    //
    //            HttpConnectionParams.setConnectionTimeout(params, SET_CONNECTION_TIMEOUT);
    //            HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);
    //            HttpClient client = new DefaultHttpClient(ccm, params);
    //            // if (NetState.Mobile == NetStateManager.CUR_NETSTATE) {
    //            // // 获取当前正在使用的APN接入点
    //            // HttpHost proxy = NetStateManager.getAPN();
    //            // if (null != proxy) {
    //            // client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
    //            // proxy);
    //            // }
    //            // }
    //            return client;
    //        } catch (Exception e) {
    //            return new DefaultHttpClient();
    //        }
    //    }

    private static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    if (chain != null && chain.length > 0) chain[0].checkValidity();
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    private static void paramToUpload(OutputStream baos, WeiboParameters params) throws WeiboException {
        String key = "";
        for (int loc = 0; loc < params.size(); loc++) {
            key = params.getKey(loc);
            StringBuilder temp = new StringBuilder(10);
            temp.setLength(0);
            temp.append(MP_BOUNDARY).append("\r\n");
            temp.append("content-disposition: form-data; name=\"").append(key).append("\"\r\n\r\n");
            temp.append(params.getValue(key)).append("\r\n");
            byte[] res = temp.toString().getBytes();
            try {
                baos.write(res);
            } catch (IOException e) {
                throw new WeiboException(e);
            }
        }
    }

    private static void imageContentToUpload(OutputStream out, String imgpath) throws WeiboException {
        if (imgpath == null) {
            return;
        }
        StringBuilder temp = new StringBuilder();

        temp.append(MP_BOUNDARY).append("\r\n");
        temp.append("Content-Disposition: form-data; name=\"pic\"; filename=\"").append("news_image").append("\"\r\n");
        String filetype = "image/png";
        temp.append("Content-Type: ").append(filetype).append("\r\n\r\n");
        // temp.append("content-disposition: form-data; name=\"file\"; filename=\"")
        // .append(imgpath).append("\"\r\n");
        // temp.append("Content-Type: application/octet-stream; charset=utf-8\r\n\r\n");
        byte[] res = temp.toString().getBytes();
        FileInputStream input = null;
        try {
            out.write(res);
            input = new FileInputStream(imgpath);
            byte[] buffer = new byte[1024 * 50];
            while (true) {
                int count = input.read(buffer);
                if (count == -1) {
                    break;
                }
                out.write(buffer, 0, count);
            }
            out.write("\r\n".getBytes());
            out.write(("\r\n" + END_MP_BOUNDARY).getBytes());
        } catch (IOException e) {
            throw new WeiboException(e);
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new WeiboException(e);
                }
            }
        }
    }

    private static void fileToUpload(OutputStream out, String filepath) throws WeiboException {
        if (filepath == null) {
            return;
        }
        StringBuilder temp = new StringBuilder();

        temp.append(MP_BOUNDARY).append("\r\n");

        temp.append("content-disposition: form-data; name=\"file\"; filename=\"").append(filepath).append("\"\r\n");
        temp.append("Content-Type: application/octet-stream; charset=utf-8\r\n\r\n");
        byte[] res = temp.toString().getBytes();
        FileInputStream input = null;
        try {
            out.write(res);
            input = new FileInputStream(filepath);
            byte[] buffer = new byte[1024 * 50];
            while (true) {
                int count = input.read(buffer);
                if (count == -1) {
                    break;
                }
                out.write(buffer, 0, count);
            }
            out.write("\r\n".getBytes());
            out.write(("\r\n" + END_MP_BOUNDARY).getBytes());
        } catch (IOException e) {
            throw new WeiboException(e);
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new WeiboException(e);
                }
            }
        }
    }

//    /**
    //     * 读取HttpResponse数据
    //     *
    //     * @param response
    //     * @return
    //     */
    //    private static String readHttpResponse(HttpResponse response) {
    //        String result = "";
    //        HttpEntity entity = response.getEntity();
    //        InputStream inputStream;
    //        try {
    //            inputStream = entity.getContent();
    //            ByteArrayOutputStream content = new ByteArrayOutputStream();
    //
    //            Header header = response.getFirstHeader("Content-Encoding");
    //            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
    //                inputStream = new GZIPInputStream(inputStream);
    //            }
    //
    //            int readBytes = 0;
    //            byte[] sBuffer = new byte[512];
    //            while ((readBytes = inputStream.read(sBuffer)) != -1) {
    //                content.write(sBuffer, 0, readBytes);
    //            }
    //            result = new String(content.toByteArray(), "UTF-8");
    //            return result;
    //        } catch (IllegalStateException e) {
    //        } catch (IOException e) {
    //        }
    //        return result;
    //    }

    /**
     * 读取HttpResponse 字节流ByteArrayOutputStream
     *
     * @param response
     * @return
     */
//    private static ByteArrayOutputStream readBytesFromHttpResponse(HttpResponse response) {
    //        HttpEntity entity = response.getEntity();
    //        InputStream inputStream;
    //        try {
    //            inputStream = entity.getContent();
    //            ByteArrayOutputStream content = new ByteArrayOutputStream();
    //
    //            Header header = response.getFirstHeader("Content-Encoding");
    //            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
    //                inputStream = new GZIPInputStream(inputStream);
    //            }
    //
    //            int readBytes = 0;
    //            byte[] sBuffer = new byte[512];
    //            while ((readBytes = inputStream.read(sBuffer)) != -1) {
    //                content.write(sBuffer, 0, readBytes);
    //            }
    //            return content;
    //        } catch (IllegalStateException e) {
    //        } catch (IOException e) {
    //        }
    //        return null;
    //    }

    /**
     * 产生11位的boundary
     */
    static String getBoundry() {
        StringBuffer _sb = new StringBuffer();
        for (int t = 1; t < 12; t++) {
            long time = System.currentTimeMillis() + t;
            if (time % 3 == 0) {
                _sb.append((char) time % 9);
            } else if (time % 3 == 1) {
                _sb.append((char) (65 + time % 26));
            } else {
                _sb.append((char) (97 + time % 26));
            }
        }
        return _sb.toString();
    }

    public static String encodeUrl(WeiboParameters parameters) {
        if (parameters == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            boolean first = true;

            for (int loc = 0; loc < parameters.size(); ++loc) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }

                String _key = parameters.getKey(loc);
                String _value = parameters.getValue(_key);
                if (_value == null) {
                    LogUtil.i("encodeUrl", "key:" + _key + " \'s value is null");
                } else {
                    try {
                        sb.append(URLEncoder.encode(parameters.getKey(loc), "UTF-8") + "=" + URLEncoder.encode(parameters.getValue(loc), "UTF-8"));
                    } catch (UnsupportedEncodingException var7) {
                        var7.printStackTrace();
                    }
                }

                LogUtil.i("encodeUrl", sb.toString());
            }

            return sb.toString();
        }
    }

    public static String encodeParameters(WeiboParameters httpParams) {
        if (httpParams != null && !isBundleEmpty(httpParams)) {
            StringBuilder buf = new StringBuilder();
            int j = 0;

            for (int loc = 0; loc < httpParams.size(); ++loc) {
                String key = httpParams.getKey(loc);
                if (j != 0) {
                    buf.append("&");
                }

                try {
                    buf.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(httpParams.getValue(key), "UTF-8"));
                } catch (UnsupportedEncodingException var6) {
                    ;
                }

                ++j;
            }

            return buf.toString();
        } else {
            return "";
        }


    }

    private static boolean isBundleEmpty(WeiboParameters bundle) {
        return bundle == null || bundle.size() == 0;
    }

    private static boolean doesExisted(File file) {
        return file != null && file.exists();
    }

    private static boolean doesExisted(String filepath) {
        return TextUtils.isEmpty(filepath) ? false : doesExisted(new File(filepath));
    }


    public static final class UploadImageUtils {
        public UploadImageUtils() {
        }

        public static boolean revitionPostImageSize(Context context, String picfile) {
            try {
                if (NetworkHelper.isWifiValid(context)) {
                    revitionImageSizeHD(picfile, 1600, 75);
                } else {
                    revitionImageSize(picfile, 1024, 75);
                }

                return true;
            } catch (IOException var3) {
                var3.printStackTrace();
                return false;
            }
        }

        private static void revitionImageSize(String picfile, int size, int quality) throws IOException {
            if (size <= 0) {
                throw new IllegalArgumentException("size must be greater than 0!");
            } else if (!doesExisted(picfile)) {
                throw new FileNotFoundException(picfile == null ? "null" : picfile);
            } else if (!BitmapHelper.verifyBitmap(picfile)) {
                throw new IOException("");
            } else {
                FileInputStream input = new FileInputStream(picfile);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(input, (Rect) null, opts);

                try {
                    input.close();
                } catch (Exception var10) {
                    var10.printStackTrace();
                }

                boolean rate = false;

                int temp;
                for (temp = 0; opts.outWidth >> temp > size || opts.outHeight >> temp > size; ++temp) {
                    ;
                }

                opts.inSampleSize = (int) Math.pow(2.0D, (double) temp);
                opts.inJustDecodeBounds = false;
                Bitmap var11 = safeDecodeBimtapFile(picfile, opts);
                if (var11 == null) {
                    throw new IOException("Bitmap decode error!");
                } else {
                    deleteDependon(picfile);
                    makesureFileExist(picfile);
                    FileOutputStream output = new FileOutputStream(picfile);
                    if (opts != null && opts.outMimeType != null && opts.outMimeType.contains("png")) {
                        var11.compress(Bitmap.CompressFormat.PNG, quality, output);
                    } else {
                        var11.compress(Bitmap.CompressFormat.JPEG, quality, output);
                    }

                    try {
                        output.close();
                    } catch (Exception var9) {
                        var9.printStackTrace();
                    }

                    var11.recycle();
                }
            }
        }

        private static Bitmap safeDecodeBimtapFile(String bmpFile, BitmapFactory.Options opts) {
            BitmapFactory.Options optsTmp = opts;
            if (opts == null) {
                optsTmp = new BitmapFactory.Options();
                optsTmp.inSampleSize = 1;
            }

            Bitmap bmp = null;
            FileInputStream input = null;
            boolean MAX_TRIAL = true;
            int i = 0;

            while (i < 5) {
                try {
                    input = new FileInputStream(bmpFile);
                    bmp = BitmapFactory.decodeStream(input, (Rect) null, opts);

                    try {
                        input.close();
                    } catch (IOException var9) {
                        var9.printStackTrace();
                    }
                    break;
                } catch (OutOfMemoryError var11) {
                    var11.printStackTrace();
                    optsTmp.inSampleSize *= 2;

                    try {
                        input.close();
                    } catch (IOException var10) {
                        var10.printStackTrace();
                    }

                    ++i;
                } catch (FileNotFoundException var12) {
                    break;
                }
            }

            return bmp;
        }

        private static void revitionImageSizeHD(String picfile, int size, int quality) throws IOException {
            if (size <= 0) {
                throw new IllegalArgumentException("size must be greater than 0!");
            } else if (!doesExisted(picfile)) {
                throw new FileNotFoundException(picfile == null ? "null" : picfile);
            } else if (!BitmapHelper.verifyBitmap(picfile)) {
                throw new IOException("");
            } else {
                int photoSizesOrg = 2 * size;
                FileInputStream input = new FileInputStream(picfile);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(input, (Rect) null, opts);

                try {
                    input.close();
                } catch (Exception var14) {
                    var14.printStackTrace();
                }

                boolean rate = false;

                int temp;
                for (temp = 0; opts.outWidth >> temp > photoSizesOrg || opts.outHeight >> temp > photoSizesOrg; ++temp) {
                    ;
                }

                opts.inSampleSize = (int) Math.pow(2.0D, (double) temp);
                opts.inJustDecodeBounds = false;
                Bitmap var16 = safeDecodeBimtapFile(picfile, opts);
                if (var16 == null) {
                    throw new IOException("Bitmap decode error!");
                } else {
                    deleteDependon(picfile);
                    makesureFileExist(picfile);
                    int org = var16.getWidth() > var16.getHeight() ? var16.getWidth() : var16.getHeight();
                    float rateOutPut = (float) size / (float) org;
                    if (rateOutPut < 1.0F) {
                        Bitmap output;
                        while (true) {
                            try {
                                output = Bitmap.createBitmap((int) ((float) var16.getWidth() * rateOutPut), (int) ((float) var16.getHeight() * rateOutPut), Bitmap.Config.ARGB_8888);
                                break;
                            } catch (OutOfMemoryError var15) {
                                System.gc();
                                rateOutPut = (float) ((double) rateOutPut * 0.8D);
                            }
                        }

                        if (output == null) {
                            var16.recycle();
                        }

                        Canvas e = new Canvas(output);
                        Matrix matrix = new Matrix();
                        matrix.setScale(rateOutPut, rateOutPut);
                        e.drawBitmap(var16, matrix, new Paint());
                        var16.recycle();
                        var16 = output;
                    }

                    FileOutputStream var17 = new FileOutputStream(picfile);
                    if (opts != null && opts.outMimeType != null && opts.outMimeType.contains("png")) {
                        var16.compress(Bitmap.CompressFormat.PNG, quality, var17);
                    } else {
                        var16.compress(Bitmap.CompressFormat.JPEG, quality, var17);
                    }

                    try {
                        var17.close();
                    } catch (Exception var13) {
                        var13.printStackTrace();
                    }

                    var16.recycle();
                }
            }
        }
    }

    private static boolean deleteDependon(File file, int maxRetryCount) {
        int retryCount = 1;
        maxRetryCount = maxRetryCount < 1 ? 5 : maxRetryCount;
        boolean isDeleted = false;
        if (file != null) {
            while (!isDeleted && retryCount <= maxRetryCount && file.isFile() && file.exists()) {
                if (!(isDeleted = file.delete())) {
                    ++retryCount;
                }
            }
        }

        return isDeleted;
    }

    private static void makesureFileExist(File file) {
        if (file != null) {
            if (!file.exists()) {
                makesureParentExist(file);
                createNewFile(file);
            }

        }
    }

    private static void createNewFile(File file_) {
        if (file_ != null) {
            if (!__createNewFile(file_)) {
                throw new RuntimeException(file_.getAbsolutePath() + " doesn\'t be created!");
            }
        }
    }

    private static boolean __createNewFile(File file_) {
        if (file_ == null) {
            return false;
        } else {
            makesureParentExist(file_);
            if (file_.exists()) {
                delete(file_);
            }

            try {
                return file_.createNewFile();
            } catch (IOException var2) {
                var2.printStackTrace();
                return false;
            }
        }
    }

    private static void delete(File f) {
        if (f != null && f.exists() && !f.delete()) {
            throw new RuntimeException(f.getAbsolutePath() + " doesn\'t be deleted!");
        }
    }

    private static void makesureFileExist(String filePath_) {
        if (filePath_ != null) {
            makesureFileExist(new File(filePath_));
        }
    }

    private static void makesureParentExist(File file_) {
        if (file_ != null) {
            File parent = file_.getParentFile();
            if (parent != null && !parent.exists()) {
                mkdirs(parent);
            }

        }
    }

    private static void mkdirs(File dir_) {
        if (dir_ != null) {
            if (!dir_.exists() && !dir_.mkdirs()) {
                throw new RuntimeException("fail to make " + dir_.getAbsolutePath());
            }
        }
    }


    private static boolean deleteDependon(String filepath, int maxRetryCount) {
        return TextUtils.isEmpty(filepath) ? false : deleteDependon(new File(filepath), maxRetryCount);
    }

    private static boolean deleteDependon(String filepath) {
        return deleteDependon((String) filepath, 0);
    }


}
