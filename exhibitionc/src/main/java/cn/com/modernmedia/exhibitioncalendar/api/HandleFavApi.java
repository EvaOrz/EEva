package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;

import static cn.com.modernmedia.exhibitioncalendar.api.HandleEventApi.HANDLE_ADD;
import static cn.com.modernmedia.exhibitioncalendar.api.HandleEventApi.HANDLE_DELETE;

/**
 * Created by Eva. on 2017/12/8.
 */

public class HandleFavApi extends BaseApi {
    private int type = 1;
    private String post;
    private ErrorMsg errorMsg;

    /**
     * 'appid':'61',
     * 'uid':'123',
     * 'token':'xxxxx'·,
     * 'like_id':'13', #喜欢ID （展览、活动）
     * 'type':'',   # 类型  1展览 2展馆 3活
     *
     * @param c
     * @param type
     */
    public HandleFavApi(Context c, int type, String id) {
        this.type = type;
        JSONObject postObject = new JSONObject();
        try {
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "uid", DataHelper.getUid(c));
            addPostParams(postObject, "token", DataHelper.getToken(c));
            addPostParams(postObject, "like_id", id);
            addPostParams(postObject, "type", type + "");
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
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        Log.e("HandleFavApi", jsonObject.toString());
        errorMsg = new ErrorMsg();
        JSONObject errorObject = jsonObject.optJSONObject("error");
        if (!isNull(errorObject)) {
            errorMsg.setNo(errorObject.optInt("no", -1));
            errorMsg.setDesc(errorObject.optString("desc", ""));
        } else {
            CalendarListModel calendarListModel = new CalendarListModel();
            CalendarListModel.parseCalendarListModel(calendarListModel, jsonObject);
            if (calendarListModel == null) return;

        }
    }

    @Override
    protected void saveData(String data) {

    }

    public ErrorMsg getData() {
        return errorMsg;
    }

    @Override
    protected String getUrl() {
        if (type == HANDLE_ADD) return UrlMaker.addLike();
        else if (type == HANDLE_DELETE) return UrlMaker.delLike();
        return "";
    }
}