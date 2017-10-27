package cn.com.modernmedia.exhibitioncalendar.api.user;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;

/**
 * 忘记密码
 *
 * @author lusiyuan
 */
public class FindPasswordApi extends BaseApi {
    private String post;
    private UserModel user = new UserModel();

    /**
     * 邮箱
     *
     * @param userName
     */
    public FindPasswordApi(String userName, String code, String newPwd) {

        try {
            JSONObject object = new JSONObject();
            object.put("username", userName);
            object.put("appid", MyApplication.APPID);
            if (code != null) object.put("code", code);
            if (newPwd != null) object.put("newpassword", newPwd);

            setPostParams(object.toString());
        } catch (JSONException e) {
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
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        user = UserModel.parseUserModel(user, jsonObject);
    }

    @Override
    protected void saveData(String data) {

    }

    @Override
    protected String getUrl() {
        return UrlMaker.getPasswordUrl();
    }

    public UserModel getData() {
        return user;
    }


}
