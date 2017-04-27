package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;

/**
 * Created by Eva. on 17/3/28.
 */

public class GetAllListApi extends BaseApi {

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
        //        Log.e("GetAllListApi", jsonObject.toString());
        ErrorMsg errorMsg = new ErrorMsg();
        JSONObject errorObject = jsonObject.optJSONObject("error");
        if (!isNull(errorObject)) {
            errorMsg.setNo(errorObject.optInt("no", -1));
            errorMsg.setDesc(errorObject.optString("desc", ""));
        } else {
            calendarListModel = CalendarListModel.parseCalendarListModel(calendarListModel, jsonObject);
            if (calendarListModel == null) return;
            AppValue.allList.setCount(calendarListModel.getCount());
            AppValue.allList.getCalendarModels().clear();
            AppValue.allList.getCalendarModels().addAll(calendarListModel.getCalendarModels());
        }


    }

    public CalendarListModel getData() {
        return calendarListModel;
    }

    @Override
    protected String getUrl() {

        return UrlMaker.getAllList();
    }
}
