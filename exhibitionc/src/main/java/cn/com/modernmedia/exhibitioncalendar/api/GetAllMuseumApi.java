package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.MuseumListModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;

/**
 * Created by Eva. on 2017/10/23.
 */

public class GetAllMuseumApi extends BaseApi {

    private String post;
    private MuseumListModel museumListModel = new MuseumListModel();

    public GetAllMuseumApi(Context c) {
        JSONObject postObject = new JSONObject();
        try {
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            //                    "longitude":"1.2",        // 通过坐标搜索展馆。  优先级 2
            //            "latitude":"2.4",         // 通过坐标搜索展馆。  优先级 2
            //            "city_ids":"1,2,3,4,5"    // 通过城市搜索展馆    优先级 3
            //            "keyword":"xxxx"           // 通过关键字搜索展馆  优先级 1
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
    protected void saveData(String data) {

    }

    @Override
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;

        museumListModel = MuseumListModel.parseMuseumListModel(museumListModel, jsonObject);
        if (museumListModel == null) return;
        AppValue.museumList.setCount(museumListModel.getCount());
        AppValue.museumList.getCalendarModels().clear();
        AppValue.museumList.getCalendarModels().addAll(museumListModel.getCalendarModels());

    }

    public MuseumListModel getData() {
        return museumListModel;
    }

    @Override
    protected String getUrl() {

        return UrlMaker.getMuseumList();
    }
}
