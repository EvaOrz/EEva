package cn.com.modernmedia.exhibitioncalendar.util;

import android.content.Context;

import com.flurry.android.FlurryAgent;

import java.util.HashMap;
import java.util.Map;

import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.util.Tools;

/**
 * Created by Eva. on 2017/11/27.
 */

public class FlurryEvent {

    //首页城市位点击
    public static final String ACClickHomeCity = "AC-Click-HomeCity";

    //首页轮播位点击
    public static final String ACClickHomeCarousel = "AC-Click-HomeCarousel";

    //首页“添加到日历”点击
    public static final String ACClickHomeAddCalendar = "AC-Click-HomeAddCalendar";

    //首页左上按钮点击
    public static final String ACClickLeftNavi = "AC-Click-LeftNavi";

    //收藏“+”按钮总点击
    public static final String ACClickDetailAdd = "AC-Click-DetailAdd";

    //同步到本地日历数
    public static final String ACSyncSystemCalendar = "AC-Sync-SystemCalendar";

    //我的展览列表分享数
    public static final String ACShareMyCalendars = "AC-Share-MyCalendars";

    //单个展览分享数
    public static final String ACShareSingleCalendar = "AC-Share-SingleCalendar";

    public static void logACClickHomeCity(Context context) {
        Map<String, String> map = setDefaultMap(context, "0", "0");
        FlurryAgent.logEvent(ACClickHomeCity, map);
    }

    public static void logACClickHomeCarousel(Context context) {
        Map<String, String> map = setDefaultMap(context, "0", "0");
        FlurryAgent.logEvent(ACClickHomeCarousel, map);
    }

    public static void logACClickHomeAddCalendar(Context context) {
        Map<String, String> map = setDefaultMap(context, "0", "0");
        FlurryAgent.logEvent(ACClickHomeAddCalendar, map);
    }

    public static void logACClickLeftNavi(Context context) {
        Map<String, String> map = setDefaultMap(context, "0", "0");
        FlurryAgent.logEvent(ACClickLeftNavi, map);
    }

    public static void logACClickDetailAdd(Context context) {
        Map<String, String> map = setDefaultMap(context, "0", "0");
        FlurryAgent.logEvent(ACClickDetailAdd, map);
    }

    public static void logACSyncSystemCalendar(Context context) {
        Map<String, String> map = setDefaultMap(context, "0", "0");
        FlurryAgent.logEvent(ACSyncSystemCalendar, map);
    }

    public static void logACShareMyCalendars(Context context) {
        Map<String, String> map = setDefaultMap(context, "0", "0");
        FlurryAgent.logEvent(ACShareMyCalendars, map);
    }

    public static void logACShareSingleCalendar(Context context) {
        Map<String, String> map = setDefaultMap(context, "0", "0");
        FlurryAgent.logEvent(ACShareSingleCalendar, map);
    }


    /**
     * deviceToken|uid|articleId|catId 例：
     * 314D3D6C-A350-4924-BCBA-DD8644445400|17259|0|cat_32_zuixin
     */
    private static Map<String, String> setDefaultMap(Context context, String articleId, String catId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("linkageKey", Tools.getDeviceToken(context) + "|" + DataHelper.getUid(context) + "|" + articleId + "|" + catId);
        return map;
    }

}
