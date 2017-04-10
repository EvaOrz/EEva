package modernmedia.com.cn.exhibitioncalendar.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.corelib.model.Entry;

import static modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel.CalendarModel.Coordinate.parseCoordinate;
import static modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel.CalendarModel.HouseOrCity.parseHouseOrCity;

/**
 * 展览model
 * Created by Eva. on 17/3/13.
 */

public class CalendarListModel extends Entry {
    private List<CalendarModel> calendarModels = new ArrayList<>();


    public static CalendarListModel parseCalendarListModel(CalendarListModel calendarListModel, JSONObject jsonObject) {
        List<CalendarModel> calendarModels = new ArrayList<>();
        JSONArray jsonArray = jsonObject.optJSONArray("item");
        for (int i = 0; i < jsonArray.length(); i++) {
            CalendarModel c = new CalendarModel();
            calendarModels.add(CalendarModel.parseCalendarModel(c, jsonArray.optJSONObject(i)));

        }
        calendarListModel.setCalendarModels(calendarModels);
        return calendarListModel;
    }

    public CalendarListModel() {
    }

    public static class CalendarModel extends Entry {


        public CalendarModel() {
        }

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
        private List<Coordinate> coordinates;
        private List<HouseOrCity> typelist;
        private List<HouseOrCity> houselist;
        private List<HouseOrCity> citylist;
        private List<HouseOrCity> hotlist;

        private String address = "";
        private String addTime = "";
        private String coverImg = "";
        private String weburl = "";
        private int category;

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

                coordinate.setCoId(jsonObject.optString("co_id"));
                coordinate.setLongitude(jsonObject.optString("co_id"));
                coordinate.setLatitude(jsonObject.optString(""));
                coordinate.setItemId(jsonObject.optString(""));
                coordinate.setAppid(jsonObject.optInt(""));
                coordinate.setAddTime(jsonObject.optString(""));
                coordinate.setExtension(jsonObject.optString(""));
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

        public static class HouseOrCity extends Entry {
            private String tagId = "";
            private String tagName = "";
            private String tagNameEn = "";
            private String extension = "";
            private int appid;
            private int sort;

            public HouseOrCity() {
            }

            public static HouseOrCity parseHouseOrCity(JSONObject jsonObject) {
                HouseOrCity houseOrCity = new HouseOrCity();
                houseOrCity.setTagId(jsonObject.optString("tag_id"));
                houseOrCity.setTagName(jsonObject.optString("tag_name"));
                houseOrCity.setTagNameEn(jsonObject.optString("tag_name_en"));
                houseOrCity.setExtension(jsonObject.optString("extension"));
                houseOrCity.setAppid(jsonObject.optInt("appid"));
                houseOrCity.setSort(jsonObject.optInt("sort"));
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

        public List<Coordinate> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Coordinate> coordinates) {
            this.coordinates = coordinates;
        }

        public List<HouseOrCity> getTypelist() {
            return typelist;
        }

        public void setTypelist(List<HouseOrCity> typelist) {
            this.typelist = typelist;
        }

        public List<HouseOrCity> getHouselist() {
            return houselist;
        }

        public void setHouselist(List<HouseOrCity> houselist) {
            this.houselist = houselist;
        }

        public List<HouseOrCity> getCitylist() {
            return citylist;
        }

        public void setCitylist(List<HouseOrCity> citylist) {
            this.citylist = citylist;
        }

        public List<HouseOrCity> getHotlist() {
            return hotlist;
        }

        public void setHotlist(List<HouseOrCity> hotlist) {
            this.hotlist = hotlist;
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
            calendarModel.setStatus(jsonObject.optInt("status"));
            calendarModel.setExtension(jsonObject.optString("extension"));
            calendarModel.setCoordinates(parseCoordinateList(jsonObject.optJSONArray("coordinates")));
            calendarModel.setAddress(jsonObject.optString("address"));
            calendarModel.setAddTime(jsonObject.optString("add_time"));
            calendarModel.setTypelist(parseHouseOrCityList(jsonObject.optJSONArray("typelist")));
            calendarModel.setHouselist(parseHouseOrCityList(jsonObject.optJSONArray("houselist")));
            calendarModel.setCitylist(parseHouseOrCityList(jsonObject.optJSONArray("citylist")));
            calendarModel.setHotlist(parseHouseOrCityList(jsonObject.optJSONArray("hotlist")));
            return calendarModel;
        }


        public static List<Coordinate> parseCoordinateList(JSONArray array) {

            List<Coordinate> list = new ArrayList<>();
            if (array == null || array.length() == 0) return list;
            for (int i = 0; i < array.length(); i++) {
                list.add(parseCoordinate(array.optJSONObject(i)));
            }
            return list;
        }

        public static List<HouseOrCity> parseHouseOrCityList(JSONArray array) {
            List<HouseOrCity> list = new ArrayList<>();
            if (array == null || array.length() == 0) return list;
            for (int i = 0; i < array.length(); i++) {
                list.add(parseHouseOrCity(array.optJSONObject(i)));
            }
            return list;
        }

    }

    public List<CalendarModel> getCalendarModels() {
        return calendarModels;
    }

    public void setCalendarModels(List<CalendarModel> calendarModels) {
        this.calendarModels = calendarModels;
    }
}