package modernmedia.com.cn.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel;

/**
 * 搜索接口
 * Created by Eva. on 17/4/5.
 */

public class SearchApi extends BaseApi {

    private JSONObject postObject = new JSONObject();
    private CalendarListModel calendarListModel = new CalendarListModel();

    public SearchApi(Context c, String keyword) {

        try {
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
            addPostParams(postObject, "keyword", keyword);

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
