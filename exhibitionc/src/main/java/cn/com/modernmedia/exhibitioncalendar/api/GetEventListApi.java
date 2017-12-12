package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;

/**
 * Created by Eva. on 17/3/28.
 */

public class GetEventListApi extends BaseApi {
    private String post;
    private CalendarListModel calendarListModel = new CalendarListModel();

    public GetEventListApi(Context c, String page) {
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
        if (isNull(jsonObject)) return;
        ErrorMsg errorMsg = new ErrorMsg();
        JSONObject errorObject = jsonObject.optJSONObject("error");
        if (!isNull(errorObject)) {
            errorMsg.setNo(errorObject.optInt("no", -1));
            errorMsg.setDesc(errorObject.optString("desc", ""));
        } else {
            calendarListModel = CalendarListModel.parseCalendarListModel(calendarListModel, jsonObject);
            if (calendarListModel == null) return;

            /**
             * 保存本地数据
             */
            AppValue.myList.getCalendarModels().clear();
            AppValue.myList.getCalendarModels().addAll(calendarListModel.getCalendarModels());
        }
    }

    public CalendarListModel getData() {
        return calendarListModel;
    }

    @Override
    protected String getUrl() {

        return UrlMaker.getEventList();
    }
}
