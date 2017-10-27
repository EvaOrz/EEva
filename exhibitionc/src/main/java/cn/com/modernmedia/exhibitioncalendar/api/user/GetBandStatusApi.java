package cn.com.modernmedia.exhibitioncalendar.api.user;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;

/**
 * 获取用户绑定信息
 *
 * @author lusiyuan
 */
public class GetBandStatusApi extends BaseApi {
    private ErrorMsg error;
    private UserModel user;
    private String post;

    public GetBandStatusApi(String uid, String token) {
        // post 参数设置
        user = new UserModel();

        try {
            JSONObject postObject = new JSONObject();
            addPostParams(postObject, "uid", uid);
            addPostParams(postObject, "token", token);
            addPostParams(postObject, "appid", MyApplication.APPID + "");
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

    @Override
    protected String getUrl() {
        return UrlMaker.getBandStatus();
    }

    @Override
    protected void handler(JSONObject jsonObject) {
        System.out.print("11" + jsonObject.toString());
        JSONObject object = jsonObject.optJSONObject("error");
        if (object != null) {
            error.setNo(object.optInt("no", 0));
            error.setDesc(object.optString("desc", ""));
        } else {
            user.setBandPhone(getBool(jsonObject.optInt("phone")));
            user.setBandEmail(getBool(jsonObject.optInt("email")));
            user.setBandWeixin(getBool(jsonObject.optInt("weixin")));
            user.setBandWeibo(getBool(jsonObject.optInt("weibo")));
            user.setBandQQ(getBool(jsonObject.optInt("qq")));
            user.setValEmail(getBool(jsonObject.optInt("valemail")));
            user.setPushEmail(jsonObject.optInt("pushemail"));
        }
    }

    private boolean getBool(int i) {
        return i == 0 ? false : true;
    }

    public UserModel getStatus() {
        return user;
    }

    @Override
    protected void saveData(String data) {

    }


}
