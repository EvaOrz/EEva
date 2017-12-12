package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.model.MuseumListModel.MuseumModel;
import cn.com.modernmedia.exhibitioncalendar.model.RecommandModel;

/**
 * 获取收藏列表
 * Created by Eva. on 2017/12/8.
 */

public class GetLikeListApi extends BaseApi {
    private String post;
    private RecommandModel recommandModel = new RecommandModel();

    private List<CalendarModel> items = new ArrayList<>();
    private List<MuseumModel> museums = new ArrayList<>();

    public GetLikeListApi(Context c) {

        JSONObject object = new JSONObject();
        try {
            object.put("appid", MyApplication.APPID + "");
            object.put("version", Tools.getAppVersion(c));
            object.put("uid", DataHelper.getUid(c));
            object.put("token", DataHelper.getToken(c));
            object.put("page", "1");
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
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        JSONArray likeJson = jsonObject.optJSONArray("like");
        JSONArray itemJson = jsonObject.optJSONArray("item");
        JSONArray museumJson = jsonObject.optJSONArray("museum");

        List<LikeModel> likes1 = new ArrayList<>();
        List<LikeModel> likes2 = new ArrayList<>();
        if (!isNull(likeJson)) {
            for (int i = 0; i < likeJson.length(); i++) {
                if (!isNull(likeJson.optJSONObject(i))) {
                    LikeModel ll = LikeModel.parseLikeModel(new LikeModel(), likeJson.optJSONObject(i));
                    if (ll.getType() == 1) likes1.add(ll);
                    else if (ll.getType() == 2) likes2.add(ll);
                }
            }
        }

        if (!isNull(itemJson)) {
            for (int i = 0; i < itemJson.length(); i++) {
                if (!isNull(itemJson.optJSONObject(i))) {
                    CalendarModel c1 = CalendarModel.parseCalendarModel(new CalendarModel(), itemJson.optJSONObject(i));
                    for (LikeModel l1 : likes1) {
                        if (l1.getLikeId() == Integer.valueOf(c1.getItemId())) {
                            c1.setEventId(l1.getEventId() + "");
                            break;
                        }
                    }
                    items.add(c1);
                }
            }

        }
        if (!isNull(museumJson)) {
            for (int i = 0; i < museumJson.length(); i++) {
                if (!isNull(museumJson.optJSONObject(i))) {
                    MuseumModel m1 = MuseumModel.parseMuseumModel(new MuseumModel(), museumJson.optJSONObject(i));
                    for (LikeModel l2 : likes2) {
                        if (l2.getLikeId() == Integer.valueOf(m1.getMuseumId())) {
                            m1.setEvntId(l2.getEventId());
                            break;
                        }
                    }
                    museums.add(m1);
                }
            }
        }
        recommandModel.setCalendarModels(items);
        recommandModel.setMuseumModels(museums);
    }


    public RecommandModel getRecommandModel() {
        return recommandModel;
    }

    @Override
    protected void saveData(String data) {

    }

    @Override
    protected String getUrl() {
        return UrlMaker.getLikeList();
    }

    private static class LikeModel extends Entry {
        private int eventId;
        private int likeId;
        private int type; // 1: 展览 2: 展馆

        private LikeModel() {

        }

        public static LikeModel parseLikeModel(LikeModel likeModel, JSONObject jsonObject) {
            likeModel.setEventId(jsonObject.optInt("event_id"));
            likeModel.setType(jsonObject.optInt("type"));
            likeModel.setLikeId(jsonObject.optInt("like_id"));
            return likeModel;
        }

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }

        public int getLikeId() {
            return likeId;
        }

        public void setLikeId(int likeId) {
            this.likeId = likeId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}