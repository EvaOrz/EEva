package modernmedia.com.cn.exhibitioncalendar.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import modernmedia.com.cn.corelib.db.DataHelper;
import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel;

/**
 * Created by Eva. on 17/3/28.
 */

public class GetUserFavListApi extends BaseApi {
    private String post;
    private CalendarListModel calendarListModel = new CalendarListModel();
    private int type;// 1：正在进行，2：已经过期

    public GetUserFavListApi(Context c, String page, int type) {
        this.type = type;
        JSONObject object = new JSONObject();
        try {
            object.put("appid", MyApplication.APPID + "");
            object.put("version", Tools.getAppVersion(c));
            object.put("uid", DataHelper.getUid(c));
            object.put("token", DataHelper.getToken(c));
            object.put("page", page);

            setPostParams(object.toString());
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
        Log.e("GetUserFavListApi", jsonObject.toString());
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
