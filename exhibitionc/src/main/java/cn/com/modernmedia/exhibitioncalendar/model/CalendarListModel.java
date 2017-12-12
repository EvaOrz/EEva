package cn.com.modernmedia.exhibitioncalendar.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.model.Entry;

import static cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel.Coordinate.parseCoordinate;
import static cn.com.modernmedia.exhibitioncalendar.model.TagListModel.TagInfo;

/**
 * 展览model
 * Created by Eva. on 17/3/13.
 */

public class CalendarListModel extends Entry {
    private List<CalendarModel> calendarModels = new ArrayList<>();

    private int count;

    public CalendarListModel() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static CalendarListModel parseCalendarListModel(CalendarListModel calendarListModel, JSONObject jsonObject) {
        List<CalendarModel> calendarModels = new ArrayList<>();
        calendarListModel.setCount(jsonObject.optInt("count"));
        JSONArray jsonArray = jsonObject.optJSONArray("item");
        if (jsonArray == null) return null;
        for (int i = 0; i < jsonArray.length(); i++) {
            CalendarModel c = new CalendarModel();
            calendarModels.add(CalendarModel.parseCalendarModel(c, jsonArray.optJSONObject(i)));

        }
        calendarListModel.setCalendarModels(calendarModels);
        return calendarListModel;
    }

    public List<CalendarModel> getCalendarModels() {
        return calendarModels;
    }

    public void setCalendarModels(List<CalendarModel> calendarModels) {
        this.calendarModels = calendarModels;
    }

    public static class CalendarModel extends Entry implements Comparable<CalendarModel> {


        private int appid;
        private String itemId = "";
        private String title = "";
        private String startTime = "";
        private String endTime = "";
        private String url = "";
        private String telephone = "";
        private String content = "";
        private String backgroundImg = "";
        private String img = "";
        private int status;
        private String extension = "";
        private Coordinate coordinates;
        private List<TagInfo> typelist;
        private List<TagInfo> houselist;
        private List<TagInfo> citylist;
        private List<TagInfo> hotlist;
        private String address = "";
        private String eventId = "";
        private String addTime = "";
        private String coverImg = "";
        private String time = "";
        private int type;//   0图文样式,1展览链接样式,2外部链接样式,3专题样式,4直播链接样式
        private String weburl = "";
        private int category;
        private MuseumListModel.MuseumModel museumModel;

        public CalendarModel() {
        }

        public static CalendarModel parseCalendarModel(CalendarModel calendarModel, JSONObject jsonObject) {
            calendarModel.setAppid(jsonObject.optInt("appid"));
            calendarModel.setItemId(jsonObject.optString("item_id"));
            calendarModel.setTitle(jsonObject.optString("title"));
            calendarModel.setStartTime(jsonObject.optString("start_time"));
            calendarModel.setEndTime(jsonObject.optString("end_time"));
            calendarModel.setUrl(jsonObject.optString("url"));
            calendarModel.setTelephone(jsonObject.optString("telephone"));
            calendarModel.setContent(jsonObject.optString("content"));
            calendarModel.setBackgroundImg(jsonObject.optString("background_img"));
            calendarModel.setImg(jsonObject.optString("img"));
            calendarModel.setCoverImg(jsonObject.optString("cover_img"));
            calendarModel.setType(jsonObject.optInt("type"));
            calendarModel.setWeburl(jsonObject.optString("weburl"));
            calendarModel.setStatus(jsonObject.optInt("status"));
            calendarModel.setExtension(jsonObject.optString("extension"));
            calendarModel.setCoordinates(parseCoordinate(jsonObject.optJSONObject("coordinates")));
            calendarModel.setAddress(jsonObject.optString("address"));
            calendarModel.setEventId(jsonObject.optString("event_id"));
            calendarModel.setAddTime(jsonObject.optString("add_time"));
            calendarModel.setTime(jsonObject.optString("time"));
            calendarModel.setTypelist(parseHouseOrCityList(jsonObject.optJSONArray("typelist")));
            calendarModel.setHouselist(parseHouseOrCityList(jsonObject.optJSONArray("houselist")));
            calendarModel.setCitylist(parseHouseOrCityList(jsonObject.optJSONArray("cityOneList")));
            calendarModel.setHotlist(parseHouseOrCityList(jsonObject.optJSONArray("hotlist")));
            calendarModel.setMuseumModel(MuseumListModel.MuseumModel.parseMuseumModel(new MuseumListModel.MuseumModel(), jsonObject.optJSONObject("museum")));
            return calendarModel;
        }

        public MuseumListModel.MuseumModel getMuseumModel() {
            return museumModel;
        }

        public void setMuseumModel(MuseumListModel.MuseumModel museumModel) {
            this.museumModel = museumModel;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public static List<TagInfo> parseHouseOrCityList(JSONArray array) {
            List<TagInfo> list = new ArrayList<>();
            if (array == null || array.length() == 0) return list;
            for (int i = 0; i < array.length(); i++) {
                TagInfo houseOrCity = new TagInfo();
                list.add(TagInfo.parseHouseOrCity(houseOrCity, array.optJSONObject(i)));
            }
            return list;
        }

        public int getAppid() {
            return appid;
        }

        public void setAppid(int appid) {
            this.appid = appid;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
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

        public String getBackgroundImg() {
            return backgroundImg;
        }

        public void setBackgroundImg(String backgroundImg) {
            this.backgroundImg = backgroundImg;
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

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public Coordinate getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Coordinate coordinates) {
            this.coordinates = coordinates;
        }

        public List<TagListModel.TagInfo> getTypelist() {
            return typelist;
        }

        public void setTypelist(List<TagInfo> typelist) {
            this.typelist = typelist;
        }

        public List<TagInfo> getHouselist() {
            return houselist;
        }

        public void setHouselist(List<TagInfo> houselist) {
            this.houselist = houselist;
        }

        public List<TagInfo> getCitylist() {
            return citylist;
        }

        public void setCitylist(List<TagInfo> citylist) {
            this.citylist = citylist;
        }

        public List<TagInfo> getHotlist() {
            return hotlist;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setHotlist(List<TagInfo> hotlist) {
            this.hotlist = hotlist;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public static class Coordinate extends Entry {
            private String coId = "";
            private String longitude = "";
            private String latitude = "";
            private String itemId = "";
            private int appid;
            private String addTime = "";
            private String extension = "";
            private String shape = "";

            public Coordinate() {
            }

            public static Coordinate parseCoordinate(JSONObject jsonObject) {
                Coordinate coordinate = new Coordinate();
                if (jsonObject == null) return coordinate;
                coordinate.setCoId(jsonObject.optString("item_id"));
                coordinate.setLongitude(jsonObject.optString("longitude"));
                coordinate.setLatitude(jsonObject.optString("latitude"));
                coordinate.setItemId(jsonObject.optString(""));
                coordinate.setAppid(jsonObject.optInt("appid"));
                coordinate.setAddTime(jsonObject.optString(""));
                coordinate.setExtension(jsonObject.optString("extension"));
                coordinate.setShape(jsonObject.optString(""));
                return coordinate;
            }

            public String getCoId() {
                return coId;
            }

            public void setCoId(String coId) {
                this.coId = coId;
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

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public int getAppid() {
                return appid;
            }

            public void setAppid(int appid) {
                this.appid = appid;
            }

            public String getAddTime() {
                return addTime;
            }

            public void setAddTime(String addTime) {
                this.addTime = addTime;
            }

            public String getExtension() {
                return extension;
            }

            public void setExtension(String extension) {
                this.extension = extension;
            }

            public String getShape() {
                return shape;
            }

            public void setShape(String shape) {
                this.shape = shape;
            }

        }

        @Override
        public int compareTo(@NonNull CalendarModel o) {
            int a = 0, b = 0;
            if (!TextUtils.isEmpty(this.getEventId())) a = Integer.valueOf(this.getEventId());
            if (!TextUtils.isEmpty(o.getEventId())) b = Integer.valueOf(o.getEventId());

            int i = b - a;//先按照年龄排序
            if (i == 0) {
                //                return this.score - o.getScore();//如果年龄相等了再用分数进行排序
            }
            return i;
        }
    }
}
