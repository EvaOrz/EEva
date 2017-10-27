package cn.com.modernmedia.exhibitioncalendar.util;

import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.model.MuseumListModel;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel;

/**
 * Created by Eva. on 17/4/11.
 */

public class AppValue {
    /**
     * 展览
     */
    public static CalendarListModel allList = new CalendarListModel();
    public static CalendarListModel myList = new CalendarListModel();
    public static CalendarListModel edList = new CalendarListModel();
    /**
     * 展馆
     */
    public  static MuseumListModel museumList = new MuseumListModel();
    /**
     * 城市
     */
    public  static TagListModel allCitys = new TagListModel();
    /**
     * 当前经纬度
     */
    public static String currentLocation = "北京市";
}
