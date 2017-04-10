package modernmedia.com.cn.exhibitioncalendar.api;

import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.model.ErrorMsg;
import modernmedia.com.cn.corelib.model.VerifyCode;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;

/**
 * Created by Eva. on 17/4/7.
 */

public class GetVerifyCodeApi extends BaseApi {

    private ErrorMsg error;
    private VerifyCode code;// 验证码
    private JSONObject postObject = new JSONObject();

    protected GetVerifyCodeApi(String phone) {
        this.error = new ErrorMsg();
        // post 参数设置
        try {
            addPostParams(postObject, "phone", phone);
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
        return UrlMaker.getVerifyCode();
    }

    //{"error":{"no":1000,"desc":"发送验证码失败，请稍后再试"},"appid":1,"phone":"15910725520"}
    @Override
    protected void handler(JSONObject jsonObject) {
        JSONObject object = jsonObject.optJSONObject("error");
        if (object != null) {
            error.setNo(object.optInt("no", 0));
            error.setDesc(object.optString("desc", ""));
        } else code = new VerifyCode(jsonObject);
    }

    public VerifyCode getCode() {
        return this.code;
    }

    @Override
    protected void saveData(String data) {
    }

}
