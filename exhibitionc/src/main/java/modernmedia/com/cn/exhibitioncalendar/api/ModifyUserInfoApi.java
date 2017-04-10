package modernmedia.com.cn.exhibitioncalendar.api;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.net.URLEncoder;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.model.UserModel;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;

/**
 * 修改用户信息
 *
 * @author ZhuQiao
 */
public class ModifyUserInfoApi extends BaseApi {
    // post 参数设置
    private JSONObject object = new JSONObject();
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
            setPostParams(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected JSONObject getPostParams() {
        return object;
    }

    protected void setPostParams(JSONObject params) {
        this.object = params;
    }


    @Override
    protected void saveData(String data) {

    }

    @Override
    protected void handler(JSONObject jsonObject) {
        if (object == null) return;
        user = UserModel.parseUserModel(user, object);
    }

    public UserModel getUser() {
        return user;
    }

    @Override
    protected String getUrl() {
        return UrlMaker.getModifyInfoUrl();
    }
}
