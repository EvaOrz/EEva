package cn.com.modernmedia.exhibitioncalendar.api;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.net.URLEncoder;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;

/**
 * 修改用户信息
 *
 * @author ZhuQiao
 */
public class ModifyUserInfoApi extends BaseApi {
    // post 参数设置
    private String post;
    private UserModel user = new UserModel();

    /**
     * 修改用户资料 password为空时，更新用户昵称、头像；反之则更新邮箱
     * <p/>
     * 3.5.0 都为空，则订阅邮件
     *
     * @param uid
     * @param token
     * @param userName
     * @param nickName
     * @param url
     * @param password
     * @param desc
     */
    protected ModifyUserInfoApi(String uid, String token, String userName, String nickName, String url, String password, String desc, boolean pushEmail) {
        super();
        JSONObject object = new JSONObject();
        try {
            object.put("uid", uid);
            object.put("token", token);
            object.put("appid", MyApplication.APPID);
            if (pushEmail) {
                object.put("pushmail", 1);
            } else {
                if (!TextUtils.isEmpty(password)) {
                    object.put("password", password);
                    if (!TextUtils.isEmpty(userName)) {
                        userName = URLEncoder.encode(userName, "UTF-8");
                        object.put("username", userName);
                    }
                } else {
                    if (!TextUtils.isEmpty(nickName)) {
                        nickName = URLEncoder.encode(nickName, "UTF-8");
                        object.put("nickname", nickName);
                    }
                    if (!TextUtils.isEmpty(url)) object.put("avatar", url);
                    if (!TextUtils.isEmpty(desc))
                        object.put("desc", URLEncoder.encode(desc, "UTF-8"));
                }
            }
            Log.v("avatar", object.toString());
            setPostParams(object.toString());
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

    @Override
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        user = UserModel.parseUserModel(user, jsonObject);
    }

    public UserModel getUser() {
        return user;
    }

    @Override
    protected String getUrl() {
        return UrlMaker.getModifyInfoUrl();
    }
}
