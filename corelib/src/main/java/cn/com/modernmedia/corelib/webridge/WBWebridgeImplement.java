package cn.com.modernmedia.corelib.webridge;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.util.ConstData;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.webridge.WBWebridge.AsynExecuteCommandListener;


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
    public void shareWithMeta(JSONObject json, AsynExecuteCommandListener listener) {
        if (listener != null) {
            Intent i = new Intent();
            i.putExtra("share_json", json.toString());
            i.setAction("show_share_dialog");
            mContext.sendBroadcast(i);
            listener.onCallBack(json.toString());
        }
    }

    /**
     * {"eval":{"command":"openMap","params":{"title":"林冠艺术基金会","latitude":"39.9929660","longitude":"116.5021170","addr":"中国北京市朝阳区酒仙桥路2号798艺术区","image":"http://7xl6wr.com2.z0.glb.qiniucdn.com/1508145410186917.png"},"sequence":7}}
     */
    public void openMap(JSONObject json, AsynExecuteCommandListener listener) {
        if (listener != null) {
            try {


                Intent i = new Intent();
                i.putExtra("open_map", json.toString());
                i.setAction("open_map_activity");
                mContext.sendBroadcast(i);


                json.put("shareResult", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listener.onCallBack(json.toString());
        }
    }

    public void domReady(JSONObject json, AsynExecuteCommandListener listener) {

    }

    /**
     * js 查询登录状态
     *
     * @param listener
     */
    public void queryLoginStatus(JSONObject s, AsynExecuteCommandListener listener) {

        if (listener != null) {

            JSONObject result = new JSONObject();
            try {
                UserModel u = DataHelper.getUserLoginInfo(mContext);
                if (u == null) {
                    result.put("loginStatus", false);
                } else {
                    result.put("loginStatus", true);

                    JSONObject uJson = new JSONObject();
                    uJson.put("userId", u.getUid());
                    uJson.put("userToken", u.getToken());
                    uJson.put("userName", u.getUserName());
                    uJson.put("userAvatarUrl", u.getAvatar());
                    uJson.put("userNickname", u.getNickName());
                    result.put("user", uJson);
                }
                Log.e("webridge:", result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listener.onCallBack(result.toString());
        }


    }

    /**
     * js 调用原生登录
     */
    public void login(JSONObject s, AsynExecuteCommandListener listener) {

        if (listener != null) CommonApplication.asynExecuteCommandListener = listener;

        Intent i = new Intent();
        i.setAction("open_login_activity");
        mContext.sendBroadcast(i);

    }

    /**
     * js 调用原生登录
     */
    public void login(String s, AsynExecuteCommandListener listener) {

        if (listener != null) CommonApplication.asynExecuteCommandListener = listener;

        Intent i = new Intent();
        i.setAction("open_login_activity");
        mContext.sendBroadcast(i);

    }

    /**
     * js 获取app info
     *
     * @param listener
     */
    public void queryAppInfo(String s, AsynExecuteCommandListener listener) {
        if (listener != null) {

            JSONObject result = new JSONObject();
            try {
                PackageManager packageManager = mContext.getPackageManager();
                PackageInfo info = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
                result.put("appID", CommonApplication.APP_ID);
                result.put("deviceUUID", Tools.getMyUUID(mContext));
                result.put("appMode", CommonApplication.DEBUG);
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

    /**
     * js调用vip支付
     * "params": {
     * "goodId": "event_10000",
     * "goodName": "金融创新与人民币国际化论坛",
     * "price": 100,
     * "needTel": "0"
     * },
     * <p>
     * 返回值
     * {
     * "result":{
     * “success": true/false,
     * "data":{
     * "uid":uid,              // uid
     * "appid":appid,          // appid
     * "pid":pid,              // pid
     * "tradeNum":tradeNum,    // 订单号
     * "tradeName":tradeName,  // 产品名称
     * "tradeType":tradeType,  // 支付方式(1微信,2阿里,5applePay)
     * "tradePrice":tradePrice // 价格
     * }
     * },
     * "error": error
     * }
     */
    public void slatePay(JSONObject json, AsynExecuteCommandListener listener) {
        if (json == null) return;

        CommonApplication.asynExecuteCommandListener = listener;

    }

    /**
     * js调用渠道号
     */
    public void getChannel(JSONObject json, AsynExecuteCommandListener listener) {

        try {
            JSONObject result = new JSONObject();


            result.put("businessweek_channel", CommonApplication.CHANNEL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onCallBack(json.toString());
    }


}
