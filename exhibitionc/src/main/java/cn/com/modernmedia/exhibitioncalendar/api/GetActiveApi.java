package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.ActiveListModel;
import cn.com.modernmedia.exhibitioncalendar.model.ActiveListModel.ActiveModel;

/**
 * Created by Eva. on 2017/12/8.
 */

public class GetActiveApi extends BaseApi {

    private String post;
    private ActiveListModel activeListModel = new ActiveListModel();

    public GetActiveApi(Context c) {

        JSONObject object = new JSONObject();
        try {
            object.put("appid", MyApplication.APPID + "");
            object.put("uid", DataHelper.getUid(c));
            object.put("token", DataHelper.getToken(c));
            post = object.toString();
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
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        JSONArray activeJson = jsonObject.optJSONArray("active_list");
        List<ActiveModel> actives = new ArrayList<>();
        if (!isNull(activeJson)) {
            for (int i = 0; i < activeJson.length(); i++) {
                if (!isNull(activeJson.optJSONObject(i))) {
                    actives.add(ActiveModel.parseActiveModel(new ActiveModel(), activeJson.optJSONObject(i)));
                }
            }
            activeListModel.setActiveModels(actives);
        }
    }


    public ActiveListModel getData() {
        return activeListModel;
    }

    @Override
    protected void saveData(String data) {

    }

    @Override
    protected String getUrl() {
        return UrlMaker.getActiveList();
    }

}
