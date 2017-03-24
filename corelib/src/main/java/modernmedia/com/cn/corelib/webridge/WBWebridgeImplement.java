package modernmedia.com.cn.corelib.webridge;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Picture;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.corelib.CommonApplication;
import modernmedia.com.cn.corelib.R;
import modernmedia.com.cn.corelib.webridge.WBWebridge.AsynExecuteCommandListener;


/**
 * 根据js发送的command注册的方法
 *
 * @author user
 */
public class WBWebridgeImplement implements WBWebridgeListener {

    private Context mContext;

    public WBWebridgeImplement(Context c) {
        mContext = c;
    }

    // ======================js调用的native方法======================

    /**
     * 分享 (异步) {"command":"share","sequence":2,"params":{"content":
     * "展望2016：一年大事一览:彭博新闻记者将继续报道2016年的重大事件，让我们按时间顺序一起展望2016热点事件。","thumb":
     * "http:\/\/s.qiniu.bb.bbwc.cn\/issue_0\/category\/2016\/0104\/5689cda80b488_90x90.jpg","link":"http:\/\/read.bbwc.cn\/dufabs.html
     * " } }
     * <p>
     * shareChannel: ShareTypeWeibo,        ShareTypeWeixin,        ShareTypeWeixinFriendCircle
     */
    public void share(JSONObject json, AsynExecuteCommandListener listener) {
        if (listener != null) {
            try {

                json.put("shareResult", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listener.onCallBack(json.toString());
        }
    }


    /**
     * js查询本地付费权限
     *
     * @param json {"level":0}
     */
    public void isPaid(JSONObject json, AsynExecuteCommandListener listener) {

    }

    public void isPaidForLevel(JSONObject json, AsynExecuteCommandListener listener) {

    }


    public void queryAppInfo(AsynExecuteCommandListener listener) {

    }

    public void domReady(JSONObject json, AsynExecuteCommandListener listener) {

    }

    /**
     * js 获取app info
     *
     * @param listener
     */
    public void queryAppInfo(JSONObject json, AsynExecuteCommandListener listener) {
        if (listener != null) {

            JSONObject result = new JSONObject();
            try {
                PackageManager packageManager = mContext.getPackageManager();
                PackageInfo info = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
                result.put("appID", ConstData.getInitialAppId());
                result.put("deviceUUID", CommonApplication.getMyUUID());
                result.put("appMode", ConstData.IS_DEBUG);
                result.put("appApiVersion", ConstData.API_VERSION);
                result.put("appDisplayName", info.applicationInfo.loadLabel(packageManager));
                result.put("appBundleName", "");
                result.put("appVersion", info.versionName);
                result.put("appBuild", info.versionCode);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            listener.onCallBack(result.toString());
        }
    }

    public void uploadPics(JSONObject json, AsynExecuteCommandListener listener) {
        if (listener != null) {
            JSONObject result = new JSONObject();
            mContext.startActivity(new Intent(mContext, UploadPicsActivity.class));
            listener.onCallBack(result.toString());
        }
    }

    /**
     * js调用原生直播（支付）
     */
    public void getLiveInfos(JSONObject json, AsynExecuteCommandListener listener) {

        try {
            JSONObject params = json.optJSONObject("params");


            json.put("shareResult", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onCallBack(json.toString());
    }


}
