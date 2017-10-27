package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;

/**
 * 保存我的城市收藏接口
 * Created by Eva. on 2017/10/20.
 */

public class SaveMyCitysApi extends BaseApi {
    private String post;
    private ErrorMsg errorMsg = new ErrorMsg();

    public SaveMyCitysApi(Context c, String sss) {
        errorMsg = new ErrorMsg();
        try {
            JSONObject postObject = new JSONObject();
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
            addPostParams(postObject, "uid", DataHelper.getUid(c));
            addPostParams(postObject, "token", DataHelper.getToken(c));

            addPostParams(postObject, "tag_id_str", sss);

            post = postObject.toString();

            setPostParams(post);

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
        return UrlMaker.addCity();
    }

    //{"error":{"no":200,"desc":"成功"}}
    @Override
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        JSONObject err = jsonObject.optJSONObject("error");
        errorMsg.parse(err);

        if (errorMsg.getNo() == 200) CommonApplication.loginStatusChange = 3;
    }


    public ErrorMsg getData() {
        return errorMsg;
    }

    @Override
    protected void saveData(String data) {

    }
}
