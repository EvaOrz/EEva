package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;

/**
 * 获取展览列表接口
 * 包括  首页推荐、周边等
 * 根据传参解析
 * Created by Eva. on 17/3/28.
 */

public class GetSomeListApi extends BaseApi {
    private String post;
    private CalendarListModel calendarListModel = new CalendarListModel();
    private TAG_TYPE tagType;

    public enum TAG_TYPE {
        RECOMMEND/** 首页推荐 **/
        , NEAR/** 周边信息 **/

    }


    public GetSomeListApi(Context c, TAG_TYPE tag_type, String latitude, String longitude) {
        this.tagType = tag_type;


        JSONObject postObject = new JSONObject();
        try {
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
            if (tagType == TAG_TYPE.NEAR) {
                addPostParams(postObject, "latitude", latitude);
                addPostParams(postObject, "longitude", longitude);
                addPostParams(postObject, "page", "1");
                addPostParams(postObject, "limit", "20");
            }

            setPostParams(postObject.toString());
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
        if (tagType == TAG_TYPE.RECOMMEND) return UrlMaker.getRecommendedList();
        else if (tagType == TAG_TYPE.NEAR) return UrlMaker.getNearList();

        return "";
    }
}
