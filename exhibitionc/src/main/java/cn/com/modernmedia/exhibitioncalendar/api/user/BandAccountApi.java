package cn.com.modernmedia.exhibitioncalendar.api.user;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;

/**
 * 绑定账号： 手机、邮箱、微博、微信、QQ
 *
 * @author lusiyuan
 */
public class BandAccountApi extends BaseApi {
    public static int PHONE = 1;
    public static int EMAIL = 2;
    public static int WEIXIN = 3;
    public static int WEIBO = 4;
    public static int QQ = 5;
    private ErrorMsg error;
    private String userName;
    private String post;

    public BandAccountApi(String uid, int bindType, String token, String userName, String code) {
        error = new ErrorMsg();

        try {
            JSONObject postObject = new JSONObject();
            addPostParams(postObject, "uid", uid);
            addPostParams(postObject, "token", token);
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            if (bindType == PHONE) {
                addPostParams(postObject, "phone", userName);
                addPostParams(postObject, "bindtype", "phone");
                addPostParams(postObject, "code", code);
            } else if (bindType == EMAIL) {
                addPostParams(postObject, "username", userName);
                addPostParams(postObject, "email", userName);
                addPostParams(postObject, "bindtype", "email");
            } else if (bindType == WEIXIN) {
                addPostParams(postObject, "sinaid", userName);
                addPostParams(postObject, "bindtype", "weixin");
            } else if (bindType == WEIBO) {
                addPostParams(postObject, "sinaid", userName);
                addPostParams(postObject, "bindtype", "weibo");
            } else if (bindType == QQ) {
                addPostParams(postObject, "sinaid", userName);
                addPostParams(postObject, "bindtype", "qq");
            }

            setPostParams(postObject.toString());
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

    public ErrorMsg getError() {
        return error;
    }

    @Override
    protected String getUrl() {
        return UrlMaker.bandAccount();
    }

    public String getUserName() {
        return userName;
    }

    @Override
    protected void handler(JSONObject jsonObject) {
        JSONObject object = jsonObject.optJSONObject("error");
        if (object != null) {
            error.setNo(object.optInt("no", 0));
            error.setDesc(object.optString("desc", ""));
            if (error.getNo() == 0) {
                error.setDesc(jsonObject.optString("jsonObject"));
            }
        }
    }

    @Override
    protected void saveData(String data) {

    }


}
