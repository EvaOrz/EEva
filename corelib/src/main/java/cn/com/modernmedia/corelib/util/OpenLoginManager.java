package cn.com.modernmedia.corelib.util;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.R;
import cn.com.modernmedia.corelib.model.UserModel;


/**
 * 第三方登录manager
 * Created by Eva. on 16/9/27.
 */
public class OpenLoginManager {

    public interface OnOpenLoginCallback {
        void onLogin(boolean isFirstLogin, UserModel user, String openToken);
    }

    private Context mContext;
    private static OpenLoginManager instance;

    // 第三方app与微信通信的openapi接口
    private IWXAPI api;


    private OpenLoginManager(Context context) {
        this.mContext = context;
    }

    public static OpenLoginManager getInstance(Context context) {
        if (instance == null) {
            instance = new OpenLoginManager(context);
        }
        return instance;
    }


    /**
     * 授权登陆
     */
    public void loginWithWeixin() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(mContext, CommonApplication.WEIXIN_APP_ID, true);
        }

        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext, R.string.no_weixin, Toast.LENGTH_SHORT).show();
            return;
        }

        api.registerApp(CommonApplication.WEIXIN_APP_ID);

        SendAuth.Req req = new SendAuth.Req();
        // post_timeline
        req.scope = "snsapi_userinfo";
        req.state = "weixin_login";
        api.sendReq(req);

    }

    /**
     * 授权登陆
     */
    public void loginWithSina() {
        //        SinaAPI.getInstance(mContext).
    }
}
