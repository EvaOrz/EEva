package cn.com.modernmedia.exhibitioncalendar.api;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.model.VerifyCode;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;

/**
 * Created by Eva. on 17/4/7.
 */

public class GetVerifyCodeApi extends BaseApi {

    private ErrorMsg error;
    private VerifyCode code;// 验证码
    private String post;

    protected GetVerifyCodeApi(String phone) {
        this.error = new ErrorMsg();
        // post 参数设置
        JSONObject postObject = new JSONObject();
        try {
            addPostParams(postObject, "phone", phone);
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
