package modernmedia.com.cn.exhibitioncalendar.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel.CalendarModel;

/**
 * 获取展览详情接口
 * Created by Eva. on 17/4/5.
 */

public class GetDetailApi extends BaseApi {

    private JSONObject postObject = new JSONObject();
    private CalendarModel calendarModel = new CalendarModel();

    public GetDetailApi(Context c, String id) {
        try {
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
            addPostParams(postObject, "item_id", id);

            setPostParams(postObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public CalendarModel getDetail() {
        return calendarModel;
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
