package modernmedia.com.cn.exhibitioncalendar.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.corelib.model.Entry;

/**
 * Created by Eva. on 17/4/10.
 */

public class TagListModel extends Entry {
    private List<HouseOrCity> houseOrCities = new ArrayList<>();


    public static TagListModel parseTagListModel(TagListModel tagListModel, JSONObject jsonObject) {
        List<HouseOrCity> houseOrCities = new ArrayList<>();
        JSONArray jsonArray = jsonObject.optJSONArray("tag");
        if (jsonArray == null) return null;
        for (int i = 0; i < jsonArray.length(); i++) {
            HouseOrCity c = new HouseOrCity();
            houseOrCities.add(HouseOrCity.parseHouseOrCity(c, jsonArray.optJSONObject(i)));

        }
        tagListModel.setHouseOrCities(houseOrCities);
        return tagListModel;
    }

    public List<HouseOrCity> getHouseOrCities() {
        return houseOrCities;
    }

    public void setHouseOrCities(List<HouseOrCity> houseOrCities) {
        this.houseOrCities = houseOrCities;
    }

    public static class HouseOrCity extends Entry {
        private String tagId = "";
        private String tagName = "";
        private String tagNameEn = "";
        private String extension = "";
        private int appid;
        private int sort;
        private String timeZone = "";
        private String color = "";
        private BackImg backImg = new BackImg();

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public BackImg getBackImg() {
            return backImg;
        }

        public void setBackImg(BackImg backImg) {
            this.backImg = backImg;
        }

        public HouseOrCity() {
        }

        public static HouseOrCity parseHouseOrCity(HouseOrCity houseOrCity, JSONObject jsonObject) {
            houseOrCity.setTagId(jsonObject.optString("tag_id"));
            houseOrCity.setTagName(jsonObject.optString("tag_name"));
            houseOrCity.setTagNameEn(jsonObject.optString("tag_name_en"));
            houseOrCity.setExtension(jsonObject.optString("extension"));
            houseOrCity.setAppid(jsonObject.optInt("appid"));
            houseOrCity.setSort(jsonObject.optInt("sort"));
            houseOrCity.setTimeZone(jsonObject.optString("time_zone"));
            houseOrCity.setColor(jsonObject.optString("color"));
            houseOrCity.setBackImg(BackImg.parseBackImg(jsonObject.optJSONObject("backImg")));
            return houseOrCity;
        }

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getTagNameEn() {
            return tagNameEn;
        }

        public void setTagNameEn(String tagNameEn) {
            this.tagNameEn = tagNameEn;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getAppid() {
            return appid;
        }

        public void setAppid(int appid) {
            this.appid = appid;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }

    public static class BackImg extends Entry {
        private String sourceV6PlusImg = "";
        private String sourceV6Img = "";
        private String sourceV5Img = "";

        public String getSourceV6PlusImg() {
            return sourceV6PlusImg;
        }

        public void setSourceV6PlusImg(String sourceV6PlusImg) {
            this.sourceV6PlusImg = sourceV6PlusImg;
        }

        public String getSourceV6Img() {
            return sourceV6Img;
        }

        public void setSourceV6Img(String sourceV6Img) {
            this.sourceV6Img = sourceV6Img;
        }

        public String getSourceV5Img() {
            return sourceV5Img;
        }

        public void setSourceV5Img(String sourceV5Img) {
            this.sourceV5Img = sourceV5Img;
        }

        public BackImg() {
        }

        public static BackImg parseBackImg(JSONObject jsonObject) {
            BackImg backImg = new BackImg();
            backImg.setSourceV6PlusImg(jsonObject.optString("sourceV6PlusImg"));
            backImg.setSourceV6Img(jsonObject.optString("sourceV6Img"));
            backImg.setSourceV5Img(jsonObject.optString("sourceV5Img"));
            return backImg;
        }
    }

}
