package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.ActiveListModel.ActiveModel;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.model.MuseumListModel.MuseumModel;
import cn.com.modernmedia.exhibitioncalendar.model.RecommandModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;

/**
 * 获取展览列表接口
 * 包括  首页推荐、周边等
 * 根据传参解析
 * Created by Eva. on 17/3/28.
 */

public class GetRecommandApi extends BaseApi {
    private String post;
    private RecommandModel recommandModel = new RecommandModel();

    private List<CalendarModel> items = new ArrayList<>();
    private List<MuseumModel> museums = new ArrayList<>();
    private List<ActiveModel> actives = new ArrayList<>();
    private List<CalendarModel> specials = new ArrayList<>();
    private List<CalendarModel> recos = new ArrayList<>();

    public GetRecommandApi(Context c) {


        JSONObject postObject = new JSONObject();
        try {
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));
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
        JSONArray itemJson = jsonObject.optJSONArray("item");
        JSONArray museumJson = jsonObject.optJSONArray("museum");
        JSONArray specialJson = jsonObject.optJSONArray("special");
        JSONArray activeJson = jsonObject.optJSONArray("active");
        JSONArray recoJson = jsonObject.optJSONArray("recommend");

        if (!isNull(itemJson)) {
            for (int i = 0; i < itemJson.length(); i++) {
                if (!isNull(itemJson.optJSONObject(i))) {
                    items.add(CalendarModel.parseCalendarModel(new CalendarModel(), itemJson.optJSONObject(i)));
                }
            }
        }
        if (!isNull(museumJson)) {
            for (int i = 0; i < museumJson.length(); i++) {
                if (!isNull(museumJson.optJSONObject(i))) {
                    museums.add(MuseumModel.parseMuseumModel(new MuseumModel(), museumJson.optJSONObject(i)));
                }
            }
        }
        if (!isNull(activeJson)) {
            for (int i = 0; i < activeJson.length(); i++) {
                if (!isNull(activeJson.optJSONObject(i))) {
                    actives.add(ActiveModel.parseActiveModel(new ActiveModel(), activeJson.optJSONObject(i)));
                }
            }
        }
        if (!isNull(recoJson)) {
            for (int i = 0; i < recoJson.length(); i++) {
                if (!isNull(recoJson.optJSONObject(i))) {
                    CalendarModel cc = new CalendarModel();
                    CalendarModel.parseCalendarModel(cc, recoJson.optJSONObject(i));
                    int recommendId = recoJson.optJSONObject(i).optInt("recommend_id");
                    if (recommendId > 0) {
                        cc.setItemId(recommendId + "");
                    }
                    recos.add(cc);
                }
            }
        }
        recommandModel.setCalendarModels(items);
        recommandModel.setMuseumModels(museums);
        recommandModel.setActiveModels(actives);
        recommandModel.setRecommandModels(recos);
        AppValue.activeModelList.clear();
        AppValue.activeModelList.addAll(actives);
    }


    public RecommandModel getRecommandModel() {
        return recommandModel;
    }

    @Override
    protected void saveData(String data) {

    }

    @Override
    protected String getUrl() {
        return UrlMaker.getRecommendedList();
    }
}
