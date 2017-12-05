package cn.com.modernmedia.exhibitioncalendar.api.user;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;


/**
 * Created by Eva. on 17/3/28.
 */

public class HandleFavApi extends BaseApi {
    public static int HANDLE_ADD = 0;
    public static int HANDLE_EDIT = 1;
    public static int HANDLE_DELETE = 2;
    private int type = 1;
    private String post;
    private ErrorMsg errorMsg;

    /**
     * 'appid':'61',
     * 'version':'1',
     * 'uid':'123',
     * 'token':'xxxxx',
     * 'item_id':'1',
     * 'img':'http://xxxx',
     * 'time':'2016-12-01 09:00:00'
     *
     * @param c
     * @param type
     */
    public HandleFavApi(Context c, int type, String id, String img, String time) {
        this.type = type;
        JSONObject postObject = new JSONObject();
        try {
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
            addPostParams(postObject, "uid", DataHelper.getUid(c));
            addPostParams(postObject, "token", DataHelper.getToken(c));

            if (type == HANDLE_ADD) addPostParams(postObject, "item_id", id);
            else addPostParams(postObject, "event_id", id);
            if (type != HANDLE_DELETE) {
                addPostParams(postObject, "img", img);
                addPostParams(postObject, "time", time);
            }
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

            /**
             * 保存本地数据
             */
            AppValue.myList.getCalendarModels().clear();
            AppValue.myList.getCalendarModels().addAll(calendarListModel.getCalendarModels());
            CommonApplication.loginStatusChange = 1;
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
        if (type == HANDLE_ADD) return UrlMaker.addUserFav();
        else if (type == HANDLE_EDIT) return UrlMaker.changeUserFav();
        else if (type == HANDLE_DELETE) return UrlMaker.deleteUserFav();
        return "";
    }
}
