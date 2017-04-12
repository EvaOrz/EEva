package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;

/**
 * Created by Eva. on 17/3/28.
 */

public class GetMyListApi extends BaseApi {
    private String post;
    private CalendarListModel calendarListModel = new CalendarListModel();
    private int type;// 1：正在进行，2：已经过期

    public GetMyListApi(Context c, String page, int type) {
        this.type = type;
        JSONObject object = new JSONObject();
        try {
            object.put("appid", MyApplication.APPID + "");
            object.put("version", Tools.getAppVersion(c));
            object.put("uid", DataHelper.getUid(c));
            object.put("token", DataHelper.getToken(c));
            object.put("page", page);
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
    protected void saveData(String data) {

    }

    @Override
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        Log.e("GetMyListApi", jsonObject.toString());
        calendarListModel = CalendarListModel.parseCalendarListModel(calendarListModel, jsonObject);
    }

    public CalendarListModel getData() {
        return calendarListModel;
    }

    @Override
    protected String getUrl() {
        if (type == 1) return UrlMaker.getUserFavList();
        else if (type == 2) {
            UrlMaker.getUserFavExpiredList();
        }
        return null;
    }
}
