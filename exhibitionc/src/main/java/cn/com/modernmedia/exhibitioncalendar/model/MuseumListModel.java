package cn.com.modernmedia.exhibitioncalendar.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.model.Entry;

/**
 * 展馆model
 * Created by Eva. on 2017/10/20.
 */

public class MuseumListModel extends Entry {
    private List<MuseumModel> calendarModels = new ArrayList<>();

    private int count;

    public MuseumListModel() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MuseumModel> getCalendarModels() {
        return calendarModels;
    }

    public void setCalendarModels(List<MuseumModel> calendarModels) {
        this.calendarModels = calendarModels;
    }


    public static MuseumListModel parseMuseumListModel(MuseumListModel museumListModel, JSONObject jsonObject) {
        List<MuseumModel> museumListModels = new ArrayList<>();

        JSONArray jsonArray = jsonObject.optJSONArray("museum");
        if (jsonArray == null) return null;
        for (int i = 0; i < jsonArray.length(); i++) {
            MuseumModel c = new MuseumModel();
            museumListModels.add(c.parseMuseumModel(c, jsonArray.optJSONObject(i)));

        }
        museumListModel.setCount(jsonObject.optInt("count"));
        museumListModel.setCalendarModels(museumListModels);
        return museumListModel;
    }


    public static class MuseumModel extends Entry {


        private int appid;
        private String museumId = "";
        private String title = "";
        private String titleEn = "";
        private String openTime = "";
        private String url = "";
        private String telephone = "";
        private String content = "";
        private String thumbnailImg = "";
        private String img = "";
        private int status;
        private String extension = "";
        private String address = "";
        private String cityId = "";
        private String addTime = "";
        private String coverImg = "";
        private String weburl = "";
        private String longitude;
        private String latitude;

        public MuseumModel() {
        }

        public int getAppid() {
            return appid;
        }

        public void setAppid(int appid) {
            this.appid = appid;
        }

        public String getMuseumId() {
            return museumId;
        }

        public void setMuseumId(String museumId) {
            this.museumId = museumId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleEn() {
            return titleEn;
        }

        public void setTitleEn(String titleEn) {
            this.titleEn = titleEn;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getThumbnailImg() {
            return thumbnailImg;
        }

        public void setThumbnailImg(String thumbnailImg) {
            this.thumbnailImg = thumbnailImg;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public String getWeburl() {
            return weburl;
        }

        public void setWeburl(String weburl) {
            this.weburl = weburl;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public static MuseumModel parseMuseumModel(MuseumModel model, JSONObject jsonObject) {
            if (jsonObject == null) return model;
            model.setAppid(jsonObject.optInt("appid"));
            model.setMuseumId(jsonObject.optString("museum_id"));
            model.setTitle(jsonObject.optString("title"));
            model.setTitleEn(jsonObject.optString("title_en"));
            model.setOpenTime(jsonObject.optString("open_time"));
            model.setUrl(jsonObject.optString("url"));
            model.setTelephone(jsonObject.optString("telephone"));
            model.setContent(jsonObject.optString("content"));
            model.setThumbnailImg(jsonObject.optString("thumbnail_img"));
            model.setImg(jsonObject.optString("img"));
            model.setCoverImg(jsonObject.optString("cover_img"));
            model.setWeburl(jsonObject.optString("weburl"));
            model.setStatus(jsonObject.optInt("status"));
            model.setExtension(jsonObject.optString("extension"));
            model.setAddress(jsonObject.optString("address"));
            model.setCityId(jsonObject.optString("city_id"));
            model.setAddTime(jsonObject.optString("add_time"));
            model.setLongitude(jsonObject.optString("longitude"));
            model.setLatitude(jsonObject.optString("latitude"));
            return model;
        }

    }
}
