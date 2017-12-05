package cn.com.modernmedia.exhibitioncalendar.model;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.model.Entry;

/**
 * 首页推荐用的数据
 * Created by Eva. on 2017/12/1.
 */

public class RecommandModel extends Entry {

    private List<CalendarListModel.CalendarModel> calendarModels = new ArrayList<>();
    private List<MuseumListModel.MuseumModel> museumModels = new ArrayList<>();
    private List<ActiveListModel.ActiveModel> activeModels = new ArrayList<>();

    // 推荐排序用
    private List<CalendarListModel.CalendarModel> recommandModels = new ArrayList<>();

    public List<CalendarListModel.CalendarModel> getCalendarModels() {
        return calendarModels;
    }

    public void setCalendarModels(List<CalendarListModel.CalendarModel> calendarModels) {
        this.calendarModels = calendarModels;
    }

    public List<MuseumListModel.MuseumModel> getMuseumModels() {
        return museumModels;
    }

    public void setMuseumModels(List<MuseumListModel.MuseumModel> museumModels) {
        this.museumModels = museumModels;
    }

    public List<ActiveListModel.ActiveModel> getActiveModels() {
        return activeModels;
    }

    public void setActiveModels(List<ActiveListModel.ActiveModel> activeModels) {
        this.activeModels = activeModels;
    }

    public List<CalendarListModel.CalendarModel> getRecommandModels() {
        return recommandModels;
    }

    public void setRecommandModels(List<CalendarListModel.CalendarModel> recommandModels) {
        this.recommandModels = recommandModels;
    }
}
