package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;

/**
 * 获取城市（标签）列表
 * Created by Eva. on 17/3/28.
 */

public class GetCityListApi extends BaseApi {
    private String post;
    private TagListModel tagListModel = new TagListModel();

    public GetCityListApi(Context c, int type) {
        try {
            JSONObject postObject = new JSONObject();
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
            addPostParams(postObject, "uid", DataHelper.getUid(c));
            addPostParams(postObject, "token", DataHelper.getToken(c));
            // 1 地域列表 2 城市列表 3 用户收藏城市列表 （uid, token 必传)
            //不传 1，2，3 全部返回
            if (type > 0) addPostParams(postObject, "type", type +"");
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
        return UrlMaker.getAllCitys();
    }

    @Override
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        tagListModel = TagListModel.parseTagListModel(tagListModel, jsonObject);
        AppValue.allCitys = tagListModel;

    }


    public TagListModel getData() {
        return tagListModel;
    }

    @Override
    protected void saveData(String data) {

    }
}
