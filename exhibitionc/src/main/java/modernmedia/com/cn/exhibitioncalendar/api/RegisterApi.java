package modernmedia.com.cn.exhibitioncalendar.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.model.UserModel;
import modernmedia.com.cn.corelib.util.DESCoder;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;

/**
 * Created by Eva. on 17/3/28.
 */

public class RegisterApi extends BaseApi {
    private UserModel user = new UserModel();
    private JSONObject object = new JSONObject();


    protected RegisterApi(String userName, String password, String code, String phone, String nick) {
        setIsNeedEncode(true);
        try {
            object.put("username", userName);
            object.put("password", password);
            object.put("appid", MyApplication.APPID + "");

            if (code != null && code.length() > 0) object.put("code", code);// 验证码
            if (phone != null && phone.length() > 0) object.put("phone", phone);// 电话号码
            if (nick != null && nick.length() > 0) {// nick name
                object.put("nickname", URLEncoder.encode(nick, "UTF-8"));
            }
            String msg = DESCoder.encode(KEY, object.toString());
            JSONObject j = new JSONObject();
            j.put("data", j);
            setPostParams(j);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void handler(JSONObject jsonObject) {
        if (object == null) return;
        user = UserModel.parseUserModel(user, jsonObject);
    }

    @Override
    protected JSONObject getPostParams() {
        return object;
    }

    protected void setPostParams(JSONObject params) {
        this.object = params;
    }

    @Override
    protected String getUrl() {
        return UrlMaker.register();
    }

    @Override
    protected void saveData(String data) {

    }

    public UserModel getUser() {
        return user;
    }
}
