package modernmedia.com.cn.corelib.util.weixin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import modernmedia.com.cn.corelib.CommonApplication;
import modernmedia.com.cn.corelib.R;
import modernmedia.com.cn.corelib.listener.OpenAuthListener;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.corelib.util.sina.SinaAPI;
import modernmedia.com.cn.corelib.util.sina.SinaAuth;
import modernmedia.com.cn.corelib.util.sina.SinaRequestListener;

import static modernmedia.com.cn.corelib.util.Tools.parseString;


/**
 * 微信分享类
 * Created by Eva. on 16/10/8.
 */
public class WeixinShare {
    private static final int THUMB_SIZE = 80;
    protected Bitmap serverBitmap;// 服务器端的图片
    protected Bitmap iconBitmap;// icon图片 只有微信微博在没有服务器端图片的情况下可以使用icon代替

    private static WeixinShare instance;
    private static final String SHARE_IMAGE_PATH_NAME = "/" + "Zhiliao" + "/temp/";// 分享图片临时文件夹，退出应用删除
    private static final String SAVE_IMAGE_PATH_NAME = "/" + "Zhiliao" + "/";// 保存图片
    private String defaultPath = Environment.getExternalStorageDirectory().getPath();
    private Context mContext;

    // 第三方app与微信通信的openapi接口
    private IWXAPI api;

    /**
     * 构造方法
     */
    private WeixinShare(Context context) {
        mContext = context;
        api = WXAPIFactory.createWXAPI(context, CommonApplication.WEIXIN_APP_ID, true);
        // 将应用注册到手机上
        if (!api.registerApp(CommonApplication.WEIXIN_APP_ID)) {
            Toast.makeText(mContext, R.string.no_weixin, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取app icon
     *
     * @return
     */
    private Bitmap getAppIcon() {

        return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_icon);
    }

    /**
     * 通过邮箱分享
     */
    public void shareByMail(String title, String desc, String url) {
        Uri uri = Uri.parse("mailto:xx@gmail.com");
        String[] email = {"3802**92@qq.com"};

        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
            intent.putExtra(Intent.EXTRA_SUBJECT, parseString(mContext, R.string
                    .share_by_email_title, R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, getWeiBoContent(title, desc, url));

            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void shareToWeiBo(String content) {
        Bitmap bitmap = serverBitmap == null ? iconBitmap : serverBitmap;
        shareWithSina(content, bitmap);
    }

    private void shareWithSina(String extraText, Bitmap bitmap) {
        String path = "";
        File file = createShareBitmap(bitmap, null, false);
        if (file != null && file.exists()) path = file.getAbsolutePath();
        shareWidthSina(extraText, path);
    }

    private String createName(long dateTaken) {
        dateTaken = dateTaken == 0 ? System.currentTimeMillis() : dateTaken;
        return DateFormat.format("yyyy-MM-dd_kk.mm.ss", dateTaken).toString() + ".jpg";
    }

    private File createShareBitmap(Bitmap bitmap, String fileName, boolean save) {
        if (bitmap == null) return null;
        if (TextUtils.isEmpty(fileName)) {
            fileName = createName(0);
        }
        String imagePath = defaultPath + (save ? SAVE_IMAGE_PATH_NAME : SHARE_IMAGE_PATH_NAME);
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File picPath = new File(imagePath + fileName);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(picPath), 1024);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return picPath;
    }

    private void shareWidthSina(final String content, final String path) {
        SinaAuth auth = new SinaAuth(mContext);
        if (auth.checkIsOAuthed()) {
            SinaAPI.getInstance(mContext).sendTextAndImage(content, path, new SinaRequestListener() {

                @Override
                public void onSuccess(String response) {
                    Log.e("微博分享", response);
                    Tools.showToast(mContext, R.string.share_success);
                }

                @Override
                public void onFailed(String error) {
                    Tools.showToast(mContext, R.string.share_failed);
                }
            });
        } else {
            auth.oAuth();
            auth.setWeiboAuthListener(new OpenAuthListener() {

                @Override
                public void onCallBack(boolean isSuccess, String uid, String token) {
                    if (isSuccess) {
                        shareWidthSina(content, path);
                    }
                }
            });
        }
    }


    /**
     * 下载图片
     *
     * @param url
     */
    public void prepareShareAfterFetchBitmap(String url) {
//        MyApplication.finalBitmap.display(url, new ImageDownloadStateListener() {
//
//            @Override
//            public void loading() {
//            }
//
//            @Override
//            public void loadOk(Bitmap bitmap, NinePatchDrawable drawable, byte[] gifByte) {
//                serverBitmap = bitmap;
//            }
//
//            @Override
//            public void loadError() {
//                iconBitmap = getAppIcon();
//            }
//        });
    }

    /**
     * 获取ShareToWeixin单例对象
     *
     * @param context 上下文
     * @return ShareToWeixin的单例对象
     * @变更记录 2013-2-28 下午2:42:26
     * @author
     */
    public static WeixinShare getInstance(Context context) {
        if (instance == null) {
            instance = new WeixinShare(context);
        }
        return instance;
    }

    private static byte[] bmpToByteArray(Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取微博分享内容（除了微信分享，其它分享内容都同微博）
     *
     * @return
     */
    public String getWeiBoContent(String title, String desc, String url) {
        return Tools.parseString(mContext, R.string.share_wp_content, title, desc, url);
    }


    /**
     * @return the api
     */
    public IWXAPI getApi() {
        return api;
    }

    /**
     * 发送图片和文字到微信
     *
     * @param title             要发送的标题
     * @param content           要发送的文字内容
     * @param url               点击显示的页面
     * @param bmp               要发送的图片
     * @param isSharedToFriends true表示发送到朋友圈，false表示发送到会话
     * @变更记录 2013-2-28 下午2:43:17
     * @author
     */
    public void shareImageAndTextToWeixin(String title, String content, String url, Bitmap bmp, boolean isSharedToFriends) {
        // 检测微信是否安装
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext, R.string.no_weixin, Toast.LENGTH_SHORT).show();
            return;
        }

        // 微信4.2以上支持发送到朋友圈
        if (isSharedToFriends && api.getWXAppSupportAPI() < 0x21020001) {
            Toast.makeText(mContext, R.string.weixin_version_low, Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        if (!TextUtils.isEmpty(url)) webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        if (!TextUtils.isEmpty(title)) msg.title = title;
        else msg.title = mContext.getString(R.string.app_name);
        if (!TextUtils.isEmpty(content)) msg.description = content;

        if (bmp == null)
            bmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_icon);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        msg.thumbData = bmpToByteArray(thumbBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        // 判断是否分享到朋友圈（默认分享到会话）
        if (isSharedToFriends) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }


    /*
     * 3.4 分享文字到朋友圈
     */
    public void shareText(String shareContent, boolean isSharedToFriends) {
        //初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = shareContent;
        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = shareContent;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //transaction字段用于唯一标识一个请求
        req.transaction = buildTransaction("textshare");
        req.message = msg;
        //发送的目标场景， 可以选择发送到会话 WXSceneSession 或者朋友圈 WXSceneTimeline。 默认发送到会话。
        if (isSharedToFriends) req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
