package modernmedia.com.cn.exhibitioncalendar.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.model.UserModel;
import modernmedia.com.cn.corelib.util.DESCoder;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;

/**
 * 开放平台(新浪微博、QQ、微信)账号登录
 *
 * @author: zhufei
 */

public class OpenLoginApi extends BaseApi {
    private int mType = 0; // 开放平台类型(1:新浪微博；2:QQ；3:微信；4Facebook；5phone
    private Context context;

    private UserModel user = new UserModel();
    private String post;


    protected OpenLoginApi(Context context, UserModel user, String avatar, String code, int type) {
        this.context = context;
        setIsNeedDesEncode(true);
        mType = type;
        // post 参数设置
        JSONObject object = new JSONObject();
        try {//4.0整合第三方
            addPostParams(object, "nickname", user.getNickName());
            addPostParams(object, "avatar", avatar);
            if (type == 1) { // 新浪微博
                addPostParams(object, "username", user.getSinaId());
                object.put("openid", user.getSinaId());
                object.put("param", user.getOpenLoginJson());
            } else if (type == 2) { // qq
                addPostParams(object, "username", user.getQqId());
                object.put("openid", user.getQqId());
                object.put("param", user.getOpenLoginJson());
            } else if (type == 3) { // 微信
                addPostParams(object, "username", user.getWeixinId());
                object.put("openid", user.getWeixinId());
                object.put("unionid", user.getUnionId());
                object.put("param", user.getOpenLoginJson());
            } else if (type == 5) { // 手机登录 手机验证码
                object.put("openid", user.getPhone());
                object.put("code", code);
            }
            object.put("logintype", type + "");
            object.put("deviceid", Tools.getDeviceId(context));
            object.put("appid", MyApplication.APPID + "");
            post = DESCoder.encode(KEY, object.toString());
            Log.e("des2", post);

            setPostParams(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String getPostParams() {
        return post;
    }

    protected void setPostParams(String params) {
        this.post = params;
    }


    @Override
    protected void saveData(String data) {

    }


    public UserModel getUser() {
        return user;
    }

    @Override
    protected void handler(JSONObject object) {
        if (object == null) return;
        user = UserModel.parseUserModel(user, object);

        if (!TextUtils.isEmpty(user.getOpenId()) && mType == 2) { // QQ账号登录
            user.setQqId(user.getOpenId());
        } else if (!TextUtils.isEmpty(user.getOpenId()) && mType == 3) {// 微信账号登录
            user.setWeixinId(user.getOpenId());
        } else if (!TextUtils.isEmpty(user.getOpenId()) && mType == 1) {// 新浪微博账号登录
            user.setSinaId(user.getOpenId());
        }
    }

    @Override
    protected String getUrl() {
        return UrlMaker.getOpenLoginUrl();
    }


}
