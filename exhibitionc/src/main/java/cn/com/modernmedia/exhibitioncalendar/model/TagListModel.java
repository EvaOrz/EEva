package cn.com.modernmedia.exhibitioncalendar.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.com.modernmedia.corelib.model.Entry;

/**
 * Created by Eva. on 17/4/10.
 */

public class TagListModel extends Entry {
    private List<TagInfo> areas = new ArrayList<>();
    private List<TagInfo> citys = new ArrayList<>();
    private List<TagInfo> users = new ArrayList<>();

    public List<TagInfo> getAreas() {
        return areas;
    }

    public void setAreas(List<TagInfo> areas) {
        this.areas = areas;
    }

    public List<TagInfo> getCitys() {
        return citys;
    }

    public void setCitys(List<TagInfo> citys) {
        this.citys = citys;
    }

    public List<TagInfo> getUsers() {
        if (users == null || users.size() == 0) {
            if (citys != null && citys.size() > 4) {

                paixu(citys);
                return citys.subList(0, 5);
            }

        }

        return users;
    }

    public void setUsers(List<TagInfo> users) {
        this.users = users;
    }

    public static TagListModel parseTagListModel(TagListModel tagListModel, JSONObject jsonObject) {
        List<TagInfo> as = new ArrayList<>();
        List<TagInfo> cs = new ArrayList<>();
        List<TagInfo> us = new ArrayList<>();

        JSONArray area = jsonObject.optJSONArray("area");
        JSONArray city = jsonObject.optJSONArray("city");
        JSONArray user = jsonObject.optJSONArray("user");
        if (area != null && area.length() > 0) {
            for (int i = 0; i < area.length(); i++) {
                as.add(TagInfo.parseHouseOrCity(new TagInfo(), area.optJSONObject(i)));
            }
        }
        if (city != null && city.length() > 0) {
            for (int i = 0; i < city.length(); i++) {
                cs.add(TagInfo.parseHouseOrCity(new TagInfo(), city.optJSONObject(i)));
            }
        }
        if (user != null && user.length() > 0) {
            for (int i = 0; i < user.length(); i++) {
                us.add(TagInfo.parseHouseOrCity(new TagInfo(), user.optJSONObject(i)));
            }
        }
        tagListModel.setAreas(as);
        tagListModel.setCitys(cs);
        paixu(us);
        tagListModel.setUsers(us);

        return tagListModel;
    }


    /**
     * 按照sort 属性排序
     *
     * @param list
     */
    private static void paixu(List<TagInfo> list) {
        Collections.sort(list, new Comparator<TagInfo>() {

            /*
             * int compare(Student o1, Student o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(TagInfo o1, TagInfo o2) {

                if (o1.getSort() > o2.getSort()) {
                    return 1;
                }
                if (o1.getSort() == o2.getSort()) {
                    return 0;
                }
                return -1;
            }
        });
    }

    public static class TagInfo extends Entry {
        private String tagId = "";
        private String pidTagId = "";
        private String tagName = "";
        private String tagNameEn = "";
        private String extension = "";
        private int appid;
        private int sort;
        private String timeZone = "";
        private String color = "";
        private BackImg backImg = new BackImg();
        private String title = "";
        private String titleImg = "";
        private String content = "";
        private int type;
        private String tagIdStr = "";
        private int museumNum;
        private String scopeTagId = "";
        private BackImg cityicon = new BackImg();
        private int itemNum;
        private BackImg cityiconWhite = new BackImg();

        private boolean isCheck = false;


        public TagInfo() {
        }

        public static TagInfo parseHouseOrCity(TagInfo tagInfo, JSONObject jsonObject) {
            tagInfo.setTagId(jsonObject.optString("tag_id"));
            tagInfo.setPidTagId(jsonObject.optString("pid_tag_id"));
            tagInfo.setTagName(jsonObject.optString("tag_name"));
            tagInfo.setTagNameEn(jsonObject.optString("tag_name_en"));
            tagInfo.setExtension(jsonObject.optString("extension"));
            tagInfo.setAppid(jsonObject.optInt("appid"));
            tagInfo.setSort(jsonObject.optInt("sort"));
            tagInfo.setTimeZone(jsonObject.optString("time_zone"));
            tagInfo.setColor(jsonObject.optString("color"));
            tagInfo.setBackImg(BackImg.parseBackImg(jsonObject.optJSONObject("backImg")));
            tagInfo.setTitle(jsonObject.optString("title"));
            tagInfo.setTagIdStr(jsonObject.optString("tag_id_str"));
            tagInfo.setTitleImg(jsonObject.optString("title_img"));
            tagInfo.setContent(jsonObject.optString("content"));
            tagInfo.setType(jsonObject.optInt("type"));
            tagInfo.setMuseumNum(jsonObject.optInt("museum_num"));
            tagInfo.setScopeTagId(jsonObject.optString("scope_tag_id"));
            tagInfo.setCityicon(BackImg.parseBackImg(jsonObject.optJSONObject("cityicon")));
            tagInfo.setItemNum(jsonObject.optInt("item_num"));
            tagInfo.setCityiconWhite(BackImg.parseBackImg(jsonObject.optJSONObject("cityicon_white")));

            return tagInfo;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getPidTagId() {
            return pidTagId;
        }

        public void setPidTagId(String pidTagId) {
            this.pidTagId = pidTagId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleImg() {
            return titleImg;
        }

        public void setTitleImg(String titleImg) {
            this.titleImg = titleImg;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTagIdStr() {
            return tagIdStr;
        }

        public void setTagIdStr(String tagIdStr) {
            this.tagIdStr = tagIdStr;
        }

        public int getMuseumNum() {
            return museumNum;
        }

        public void setMuseumNum(int museumNum) {
            this.museumNum = museumNum;
        }

        public String getScopeTagId() {
            return scopeTagId;
        }

        public void setScopeTagId(String scopeTagId) {
            this.scopeTagId = scopeTagId;
        }

        public BackImg getCityicon() {
            return cityicon;
        }

        public void setCityicon(BackImg cityicon) {
            this.cityicon = cityicon;
        }

        public int getItemNum() {
            return itemNum;
        }

        public void setItemNum(int itemNum) {
            this.itemNum = itemNum;
        }

        public BackImg getCityiconWhite() {
            return cityiconWhite;
        }

        public void setCityiconWhite(BackImg cityiconWhite) {
            this.cityiconWhite = cityiconWhite;
        }

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

        public BackImg() {
        }

        public static BackImg parseBackImg(JSONObject jsonObject) {
            BackImg backImg = new BackImg();
            if (jsonObject != null) {
                backImg.setSourceV6PlusImg(jsonObject.optString("sourceV6PlusImg"));
                backImg.setSourceV6Img(jsonObject.optString("sourceV6Img"));
                backImg.setSourceV5Img(jsonObject.optString("sourceV5Img"));
            }
            return backImg;
        }

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
    }

}
