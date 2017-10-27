package cn.com.modernmedia.exhibitioncalendar.api.user;

import android.content.Context;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;

/**
 * 搜索接口
 * Created by Eva. on 17/4/5.
 */

public class SearchApi extends BaseApi {

    private String post;
    private CalendarListModel calendarListModel = new CalendarListModel();

    public SearchApi(Context c, String keyword) {
        JSONObject postObject = new JSONObject();
        try {
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
            addPostParams(postObject, "keyword", keyword);

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
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        calendarListModel = CalendarListModel.parseCalendarListModel(calendarListModel, jsonObject);
    }


    public CalendarListModel getCalendarListModel() {
        return calendarListModel;
    }

    @Override
    protected void saveData(String data) {

    }

    @Override
    protected String getUrl() {
        return UrlMaker.search();
    }
}
