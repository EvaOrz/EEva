package cn.com.modernmedia.exhibitioncalendar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.webridge.WBWebView;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;

/**
 * Created by Eva. on 17/4/5.
 */
@SuppressLint("SetJavaScriptEnabled")
public class CommonWebView extends WBWebView {
    public static final String ERROR_WEB = "file:///android_asset/error_web.html?g="; // 错误页面
    private static final int TIME_OUT_MSG = 100;
    private CommonWebView me;
    private boolean loadOk = true;
    private Context mContext;
    private boolean isChangeStatus = false;
    private WebProcessListener listener;
    private boolean isFetchNull = false;
    private int x, y;
    private List<String> urlList = new ArrayList<String>();// 图片列表
    private List<String> descList = new ArrayList<String>();// 描述列表
    private boolean hasLoadFromHttp = false;// 防止无限获取http
    private boolean isError = false;// 因为disProcess会延迟0.5S,所以判断一下
    private WebViewLoadListener mLoadListener;
    private String targetUrl = ""; // 要打开的url
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_OUT_MSG:
                    showErrorType(2);
                    break;
                default:
                    break;
            }
        }

    };

    public CommonWebView(Context context, boolean bgIsTransparent) {
        this(context, null, bgIsTransparent);
    }


    public CommonWebView(Context context, AttributeSet attrs, boolean bgIsTransparent) {
        super(context, attrs);
        mContext = context;
        init(bgIsTransparent);
    }

    public CommonWebView(Context context, AttributeSet attrs) {
        this(context, attrs, true);
    }

    @Override
    public void setVerticalScrollbarOverlay(boolean overlay) {
        if (overlay) {
            this.setOverScrollMode(OVER_SCROLL_NEVER);
        }
    }

    private void init(boolean bgIsTransparent) {
        me = this;
        if (bgIsTransparent) this.setBackgroundColor(0);// 设置webview本身的白色背景为透明，以显示在它下面的view

        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 去掉白边

        this.setWebViewClient(new WebViewClient() {

            /**
             * 在页面加载开始时调用
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            /**
             * 在页面加载结束时调用
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                if (loadOk) {
                    getSettings().setBlockNetworkImage(false);
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (!isError) showErrorType(0);
                        }
                    }, 500);
                    view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('head')[0].innerHTML)");

                    targetUrl = url;
                }
                if (mLoadListener != null) mLoadListener.loadComplete(loadOk);
            }

            /**
             * 在点击请求的是链接是才会调用， 重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("内部网页跳转拦截", url);

                if (url.startsWith("slate")) {
                    UriParse.clickSlate(mContext, url, new Entry[]{}, me);
                    return true;
                } else if (url.startsWith("http") || url.startsWith("https")) {
                    targetUrl = url;
                }
                return false;
            }

            /**
             * 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次
             */
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            /**
             * 处理https请求
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                loadOk = false;
                if (mLoadListener != null) mLoadListener.loadComplete(false);
                showErrorType(2);
                handler.proceed(); // 接受所有证书
                //                handler.cancel();// 不支持ssl
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("onReceivedError", failingUrl);

                loadOk = false;
                targetUrl = failingUrl;
                if (mLoadListener != null) mLoadListener.loadComplete(false);
                showErrorType(2);

                super.onReceivedError(view, errorCode, description, failingUrl);

            }

            /**
             * 如果是同一篇文章从缓存拿出，如果以前没有拦截过，那么shouldInterceptRequest只能拦截到它的url,
             * html里面的东西都拦截不到了；
             * 反之，当第一次拦截了东西，那么以后shouldInterceptRequest还是会拦截相同的东西
             * PS:getSettings().setBlockNetworkImage不改回来，是不会拦截图片url的
             */
            // @Override
            // public WebResourceResponse shouldInterceptRequest(WebView view,
            // String url) {
            // return null;
            // }
        });
        setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        if (!isChangeStatus) {
            isChangeStatus = false;
            if (!url.startsWith(ERROR_WEB)) {
                showErrorType(1);
            }
        }
    }

    @Override
    public void reload() {
        if (!TextUtils.isEmpty(targetUrl) && !TextUtils.equals(targetUrl, getUrl())) {
            me.loadUrl(targetUrl);
        } else {
            super.reload();
            showErrorType(1);
        }
    }

    @Override
    public void goBack() {
        super.goBack();
    }

    /**
     * 判断加载完的html是否为空
     *
     * @param html
     */
    private void checkHtmlIsNull(String html) {
        Log.e("is null about html", html + "");
        if (TextUtils.isEmpty(html)) {
            isFetchNull = true;
            me.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (!hasLoadFromHttp) {
                hasLoadFromHttp = true;
                showErrorType(1);
                new Thread() {

                    @Override
                    public void run() {
                        getHtmlIfNull();
                    }

                }.start();
            } else {
                showErrorType(2);
            }
        } else {
            if (isFetchNull) {
                isFetchNull = false;
                me.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
        }
    }

    public void setListener(WebProcessListener listener) {
        this.listener = listener;
    }

    /**
     * 显示状态
     *
     * @param type
     */
    private void showErrorType(final int type) {
        isError = type == 2;
        if (listener != null) handler.post(new Runnable() {

            @Override
            public void run() {
                listener.showStyle(type);
            }
        });
    }

    /**
     * 栏目内容是网页时，需判断是否是起始栏目内容，若不是，点击返回键时则返回到上一级内容，直至起始栏目内容
     *
     * @return
     */
    public boolean doGoBack() {
        boolean goBack = true;
        int steps = computeBackSteps();
        if (steps < 0 && canGoBackOrForward(steps)) {
            goBackOrForward(steps);
            targetUrl = "";
        } else {
            goBack = false;
        }
        return goBack;
    }

    /**
     * 计算应该回退的步数
     *
     * @return
     */
    public int computeBackSteps() {
        WebBackForwardList list = copyBackForwardList();
        int steps = 0;
        int current = list.getCurrentIndex();
        int index = current;
        if (list.getItemAtIndex(current).getUrl().startsWith(ERROR_WEB)) { // 当前项是错误页面
            while (index >= 0) {
                WebHistoryItem item = list.getItemAtIndex(index);
                if (item.getUrl().startsWith(ERROR_WEB)) {
                    steps -= 2;
                    index -= 2;
                } else {
                    index -= 1;
                }
            }
        } else {
            steps -= 1;
        }
        return steps;
    }

    /**
     * 当webview没有加载到html时，通过http获取
     */
    private void getHtmlIfNull() {
        String uri = "";

        if (TextUtils.isEmpty(uri)) {
            showErrorType(2);
            return;
        }
        final String url = uri;
        HttpURLConnection conn = null;
        URL mUrl = null;
        try {
            mUrl = new URL(uri);
            Log.e("流量bug查询**", "CommonWebView:getHtmlIfNull()" + "-----" + url);
            conn = (HttpURLConnection) mUrl.openConnection();
            conn.setConnectTimeout(10 * 10000);
            conn.setReadTimeout(10 * 10000);

            int status = conn.getResponseCode();
            if (status == 200) {
                InputStream is = conn.getInputStream();
                if (is == null) {
                    showErrorType(2);
                    return;
                }
                final String data = receiveData(is);
                if (TextUtils.isEmpty(data)) {
                    showErrorType(2);
                    return;
                }
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        me.loadDataWithBaseURL(url, data, "text/html", "UTF-8", null);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorType(2);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private String receiveData(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buff = new byte[1024];
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

    public interface WebViewLoadListener {
        /**
         * 网页数据加载完成
         *
         * @param success
         */
        public void loadComplete(boolean success);
    }

    public interface WebProcessListener {
        /**
         * process显示状态
         *
         * @param style 0:不显示;1.显示loading;2.显示error
         */
        public void showStyle(int style);
    }
}