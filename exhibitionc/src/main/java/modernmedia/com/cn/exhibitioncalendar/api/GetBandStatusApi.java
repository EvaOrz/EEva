package modernmedia.com.cn.exhibitioncalendar.api;

import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.model.ErrorMsg;
import modernmedia.com.cn.corelib.model.UserModel;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;

/**
 * 获取用户绑定信息
 *
 * @author lusiyuan
 */
public class GetBandStatusApi extends BaseApi {
    private String uid;
    private ErrorMsg error;
    private UserModel user;
    JSONObject postObject = new JSONObject();

    public GetBandStatusApi(String uid, String token) {
        // post 参数设置
        user = new UserModel();

        try {
            addPostParams(postObject, "uid", uid);
            addPostParams(postObject, "token", token);
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            setPostParams(postObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject getPostParams() {
        return postObject;
    }

    protected void setPostParams(JSONObject params) {
        this.postObject = params;
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
