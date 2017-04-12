package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel;

/**
 * Created by Eva. on 17/3/28.
 */

public class GetUserCitysApi extends BaseApi {
    private String post;
    private TagListModel tagListModel = new TagListModel();

    public GetUserCitysApi(Context c) {
        try {
            JSONObject postObject = new JSONObject();
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
            addPostParams(postObject, "uid", DataHelper.getUid(c));
            addPostParams(postObject, "token", DataHelper.getToken(c));
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
        return UrlMaker.getUserCitys();
    }

    @Override
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        tagListModel = TagListModel.parseTagListModel(tagListModel, jsonObject);

    }


    public TagListModel getData() {
        return tagListModel;
    }

    @Override
    protected void saveData(String data) {

    }
}
