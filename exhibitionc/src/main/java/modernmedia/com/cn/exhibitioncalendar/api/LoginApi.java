package modernmedia.com.cn.exhibitioncalendar.api;

import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.model.UserModel;

/**
 * 登录接口
 * Created by Eva. on 17/3/28.
 */

public class LoginApi extends BaseApi {

    private UserModel userModel;

    @Override
    protected void saveData(String data) {

    }

    @Override
    protected void handler(JSONObject jsonObject) {

    }

    @Override
    protected String getUrl() {
        return UrlMaker.login();
    }


    public UserModel getUserModel() {
        return userModel;
    }
}
