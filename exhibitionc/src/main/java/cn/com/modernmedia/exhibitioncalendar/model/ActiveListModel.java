package cn.com.modernmedia.exhibitioncalendar.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.model.Entry;

/**
 * 活动model
 * Created by Eva. on 2017/12/1.
 */

public class ActiveListModel extends Entry {
    private List<ActiveModel> activeModels = new ArrayList<>();

    public List<ActiveModel> getActiveModels() {
        return activeModels;
    }

    public void setActiveModels(List<ActiveModel> activeModels) {
        this.activeModels = activeModels;
    }

    public static class ActiveModel extends Entry {
        private int appid;
        private int activeId;
        private String title = "";
        private String startTime = "";
        private String endTime = "";
        private int goodsId;
        private int level;
        private String url = "";
        private String telephone = "";
        private String content = "";
        private String coverImg = "";
        private String backgroundImg = "";
        private String longitude = "";
        private String latitude = "";
        private String address = "";
        private String desc = "";
        private int type;
        private String weburl = "";
        private String backgroundImg_x = "";
        private String state_desc = "";

        public ActiveModel() {
        }

        public static ActiveModel parseActiveModel(ActiveModel activeModel, JSONObject jsonObject) {
            activeModel.setAppid(jsonObject.optInt("appid"));
            activeModel.setActiveId(jsonObject.optInt("active_id"));
            activeModel.setTitle(jsonObject.optString("title"));
            activeModel.setStartTime(jsonObject.optString("start_time"));
            activeModel.setEndTime(jsonObject.optString("end_time"));
            activeModel.setUrl(jsonObject.optString("url"));
            activeModel.setTelephone(jsonObject.optString("telephone"));
            activeModel.setContent(jsonObject.optString("content"));
            activeModel.setBackgroundImg(jsonObject.optString("background_img"));
            activeModel.setGoodsId(jsonObject.optInt("goods_id"));
            activeModel.setCoverImg(jsonObject.optString("cover_img"));
            activeModel.setType(jsonObject.optInt("type"));
            activeModel.setWeburl(jsonObject.optString("weburl"));
            activeModel.setLevel(jsonObject.optInt("level"));
            activeModel.setLongitude(jsonObject.optString("longitude"));
            activeModel.setLatitude(jsonObject.optString("latitude"));
            activeModel.setDesc(jsonObject.optString("desc"));
            activeModel.setBackgroundImg_x(jsonObject.optString("address"));
            activeModel.setAddress(jsonObject.optString("address"));
            activeModel.setState_desc(jsonObject.optString("state_desc"));
            return activeModel;
        }

        public int getAppid() {
            return appid;
        }

        public void setAppid(int appid) {
            this.appid = appid;
        }

        public int getActiveId() {
            return activeId;
        }

        public void setActiveId(int activeId) {
            this.activeId = activeId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
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

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public String getBackgroundImg() {
            return backgroundImg;
        }

        public void setBackgroundImg(String backgroundImg) {
            this.backgroundImg = backgroundImg;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getWeburl() {
            return weburl;
        }

        public void setWeburl(String weburl) {
            this.weburl = weburl;
        }

        public String getBackgroundImg_x() {
            return backgroundImg_x;
        }

        public void setBackgroundImg_x(String backgroundImg_x) {
            this.backgroundImg_x = backgroundImg_x;
        }

        public String getState_desc() {
            return state_desc;
        }

        public void setState_desc(String state_desc) {
            this.state_desc = state_desc;
        }
    }
}