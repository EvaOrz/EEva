package modernmedia.com.cn.exhibitioncalendar.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel;

/**
 * Created by Eva. on 17/3/28.
 */

public class GetAllListApi extends BaseApi{

    private String post;
    private CalendarListModel calendarListModel = new CalendarListModel();

    public GetAllListApi(Context c) {
        JSONObject object = new JSONObject();
        try {
            object.put("appid", MyApplication.APPID + "");
            object.put("version", Tools.getAppVersion(c));
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
        Log.e("GetAllListApi", jsonObject.toString());
        calendarListModel = CalendarListModel.parseCalendarListModel(calendarListModel, jsonObject);
    }

    public CalendarListModel getData() {
        return calendarListModel;
    }

    @Override
    protected String getUrl() {

        return UrlMaker.getAllList();
    }
}
