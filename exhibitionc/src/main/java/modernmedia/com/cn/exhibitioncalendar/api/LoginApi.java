package modernmedia.com.cn.exhibitioncalendar.api;

import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.model.UserModel;
import modernmedia.com.cn.corelib.util.DESCoder;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;

/**
 * 登录接口
 * Created by Eva. on 17/3/28.
 */

public class LoginApi extends BaseApi {

    private UserModel user = new UserModel();
    private String postObject ;

    protected LoginApi( String userName, String password) {
        setIsNeedDesEncode(true);
        JSONObject object = new JSONObject();
        try {
            // username有邮箱check，可以不编码
            object.put("username", userName);
            // 密码在输入上已经做了限制，也可以不编码
            object.put("password", password);
            object.put("appid", MyApplication.APPID + "");
            postObject  = DESCoder.encode(KEY, object.toString());
            setPostParams(postObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected String getPostParams() {
        return postObject;
    }

    protected void setPostParams(String params) {
        this.postObject = params;
    }

    @Override
    protected void saveData(String data) {

    }

    @Override
    protected void handler(JSONObject object) {
        if (object == null) return;
        user = UserModel.parseUserModel(user, object);
    }

    @Override
    protected String getUrl() {
        return UrlMaker.login();
    }


    public UserModel getUserModel() {
        return user;
    }


}


