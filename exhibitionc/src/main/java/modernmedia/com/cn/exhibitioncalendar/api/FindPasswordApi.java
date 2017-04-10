package modernmedia.com.cn.exhibitioncalendar.api;

import org.json.JSONException;
import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.model.UserModel;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;

/**
 * 忘记密码
 *
 * @author lusiyuan
 */
public class FindPasswordApi extends BaseApi {
    private JSONObject object = new JSONObject();
    private UserModel user = new UserModel();

    /**
     * 邮箱
     *
     * @param userName
     */
    protected FindPasswordApi(String userName, String code, String newPwd) {

        try {
            object.put("username", userName);
            object.put("appid", MyApplication.APPID);
            if (code != null) object.put("code", code);
            if (newPwd != null) object.put("newpassword", newPwd);

            setPostParams(object);
        } catch (JSONException e) {
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
    protected void handler(JSONObject jsonObject) {
        if (object == null) return;
        user = UserModel.parseUserModel(user, object);
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
