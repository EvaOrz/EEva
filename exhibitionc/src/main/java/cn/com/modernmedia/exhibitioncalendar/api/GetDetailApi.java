package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.model.MuseumListModel.MuseumModel;

/**
 * 获取展览详情接口
 * Created by Eva. on 17/4/5.
 */

public class GetDetailApi extends BaseApi {


    private CalendarModel calendarModel = new CalendarModel();
    private MuseumModel museumModel = new MuseumModel();
    private String post;
    private int type = 0;// 0：展览 1：展馆

    public GetDetailApi(Context c, String id, int type) {
        this.type = type;
        try {
            JSONObject postObject = new JSONObject();
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            if (type == 0) {
                addPostParams(postObject, "version", Tools.getAppVersion(c));
                addPostParams(postObject, "item_id", id);
            } else if (type == 1) {
                addPostParams(postObject, "museum_id", id);
            }
            setPostParams(postObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public CalendarModel getCalendarDetail() {
        return calendarModel;
    }

    public MuseumModel getMuseumDetail() {
        return museumModel;
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

        if (type == 0) {
            JSONArray jsonArray = jsonObject.optJSONArray("item");
            if (jsonArray == null || jsonArray.length() == 0) return;
            calendarModel = CalendarModel.parseCalendarModel(calendarModel, jsonArray.optJSONObject(0));
        } else if (type == 1) {
            museumModel = MuseumModel.parseMuseumModel(museumModel, jsonObject);

        }

    }

    @Override
    protected String getUrl() {
        if (type == 0) return UrlMaker.getExhibitionDetail();
        else if (type == 1) return UrlMaker.getMuseumDetail();
        return "";
    }

    @Override
    protected void saveData(String data) {

    }
}
