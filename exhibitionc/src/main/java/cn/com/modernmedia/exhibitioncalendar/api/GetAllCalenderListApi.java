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

public class GetAllCalenderListApi extends BaseApi {
    public enum TAG_TYPE {
        ALL/** 首页推荐 **/
        , NEAR/** 周边信息 **/

    }

    private TAG_TYPE tagType;
    private String post;
    private CalendarListModel calendarListModel = new CalendarListModel();

    public GetAllCalenderListApi(Context c, TAG_TYPE tag_type, String latitude, String longitude) {
        this.tagType = tag_type;
        JSONObject object = new JSONObject();
        try {
            object.put("appid", MyApplication.APPID + "");
            object.put("version", Tools.getAppVersion(c));
            if (tagType == TAG_TYPE.NEAR) {
                addPostParams(object, "latitude", latitude);
                addPostParams(object, "longitude", longitude);
                addPostParams(object, "page", "1");
                addPostParams(object, "limit", "20");
            }
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
        ErrorMsg errorMsg = new ErrorMsg();
        JSONObject errorObject = jsonObject.optJSONObject("error");
        if (!isNull(errorObject)) {
            errorMsg.setNo(errorObject.optInt("no", -1));
            errorMsg.setDesc(errorObject.optString("desc", ""));
        } else {
            calendarListModel = CalendarListModel.parseCalendarListModel(calendarListModel, jsonObject);
            if (calendarListModel == null) return;
            if (tagType == TAG_TYPE.ALL) {
                AppValue.allList.setCount(calendarListModel.getCount());
                AppValue.allList.getCalendarModels().clear();
                AppValue.allList.getCalendarModels().addAll(calendarListModel.getCalendarModels());

            }
        }


    }

    public CalendarListModel getData() {
        return calendarListModel;
    }

    @Override
    protected String getUrl() {
        if (tagType == TAG_TYPE.ALL) return UrlMaker.getAllList();
        else if (tagType == TAG_TYPE.NEAR) return UrlMaker.getNearList();
        return "";
    }
}
