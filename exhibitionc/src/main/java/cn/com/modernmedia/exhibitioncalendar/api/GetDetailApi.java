package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;

/**
 * 获取展览详情接口
 * Created by Eva. on 17/4/5.
 */

public class GetDetailApi extends BaseApi {


    private CalendarModel calendarModel = new CalendarModel();
    private String post;

    public GetDetailApi(Context c, String id) {
        try {
            JSONObject postObject = new JSONObject();
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
            addPostParams(postObject, "item_id", id);

            setPostParams(postObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public CalendarModel getDetail() {
        return calendarModel;
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
        JSONArray jsonArray = jsonObject.optJSONArray("item");
        if (jsonArray == null || jsonArray.length() == 0) return;


        calendarModel = CalendarModel.parseCalendarModel(calendarModel, jsonArray.optJSONObject(0));
        Log.e("GetDetailApi", jsonObject.toString());

    }

    @Override
    protected String getUrl() {
        return UrlMaker.getExhibitionDetail();
    }

    @Override
    protected void saveData(String data) {

    }
}
