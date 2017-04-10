package modernmedia.com.cn.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONObject;

import modernmedia.com.cn.corelib.db.DataHelper;
import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;

/**
 * Created by Eva. on 17/3/28.
 */

public class GetUserCitysApi extends BaseApi {
    private JSONObject postObject = new JSONObject();

    public GetUserCitysApi(Context c) {
        try {
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
            addPostParams(postObject, "uid", DataHelper.getUid(c));
            addPostParams(postObject, "token", DataHelper.getToken(c));

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
        return UrlMaker.getUserCitys();
    }

    @Override
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;


    }

    @Override
    protected void saveData(String data) {

    }
}
