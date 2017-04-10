package modernmedia.com.cn.exhibitioncalendar.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.activity.AboutActivity;
import modernmedia.com.cn.exhibitioncalendar.activity.CalendarDetailActivity;

/**
 * 自定义协议解析类
 * Created by Eva. on 17/3/14.、
 * <p>
 * <p>
 * 打开分类浏览
 * <p>
 * slate://category
 * <p>
 * 打开分类浏览指定栏目(固定标题,有筛选)
 * <p>
 * slate://search/{tagid}/{tagname}
 * <p>
 * 打开指定栏目标(自定标题,无筛选,无分享)
 * <p>
 * slate://specialSearch/{tagid}/{tagname}
 * <p>
 * 打开指定栏目标(自定标题,无筛选,有分享)
 * <p>
 * slate://specialSearch/{tagid}/{tagname}/{itemid}
 * <p>
 * 打开展览详情页
 * <p>
 * slate://detailCalendar/{itemid}
 * <p>
 * 打开直播展览详情页
 * <p>
 * slate://detailLiveCalendar/{itemid}
 */

public class UriParse {
    public static String DETAILCALENDAR = "detailCalendar";
    public static String CATEGORY = "category";
    public static String SEARCH = "search";
    public static String APECIALSEARCH = "specialSearch";
    public static String DETAILLIVECALENDAR = "detailLiveCalendar";


    /**
     * 普通列表点击
     *
     * @param context
     * @param entries [0] ArticleItem;[1]TransferArticle...
     * @param cls     [0]为特定的文章页
     */
    public static void clickSlate(Context context, Entry[] entries, Class<?>... cls) {
        click(context, entries, null, cls);
    }

    /**
     * 特殊view点击(commonwebview)
     *
     * @param context
     * @param entries [0] ArticleItem;[1]TransferArticle;[2]issue...
     * @param view
     * @param cls
     */
    public static void clickSlate(Context context, Entry[] entries, View view, Class<?>... cls) {
        click(context, entries, view, cls);
    }

    private static void click(Context context, Entry[] entries, View view, Class<?>... cls) {
        //        if (entries != null && entries[0] instanceof ArticleItem) {
        //            String link = "";
        //            ArticleItem item = (ArticleItem) entries[0];
        //            if (item.getAdvSource() != null) {// 广告
        //                link = item.getAdvSource().getLink();
        //                if (TextUtils.isEmpty(link)) return;
        //                if (link.startsWith("slate://adv/")) {
        //                    // TODO 跳转到广告文章
        //                    AdvUriParse.clickSlate(context, link, entries, view, cls);
        //                    return;
        //                }
        //                link = link.replace("adv/", "");
        //            } else {
        //                link = item.getSlateLink();
        //            }

        clickSlate(context, "", entries, view, cls);
        //        }
    }

    /*
     * 返回uri类型和相关参数
     *
     * @param String uri
     *
     * @return ArrayList list
     *
     * @list.get(0) 为uri类型 article,column....
     */
    public static ArrayList<String> parser(String uri) {
        ArrayList<String> list = new ArrayList<String>();
        if (TextUtils.isEmpty(uri)) return list;
        String[] Array = uri.split("://");
        if (Array.length > 1) {
            String[] param = Array[1].split("/");
            list.add(param[0]);

        }

        return list;
    }

    public static void clickSlate(Context context, String link, Entry[] entries, View view, Class<?>... cls) {
        if (TextUtils.isEmpty(link)) {
            //            doLinkNull(context, entries, cls);
        } else if (link.toLowerCase().startsWith("http://") || link.toLowerCase().startsWith("https://")) {
            doLinkHttp(context, link);
        } else if (link.toLowerCase().startsWith("slate://")) {
            List<String> list = parser(link);
            String key = list.size() > 0 ? list.get(0) : "";
            if (key.equals(CATEGORY)) {

            } else if (key.equals(DETAILCALENDAR)) {
                Intent i = new Intent(context, CalendarDetailActivity.class);
                i.putExtra(DETAILCALENDAR, detailCalendar(link));
                context.startActivity(i);
            }
        } else if (link.startsWith("tel://")) {
            String arr[] = link.split("tel://");
            if (arr.length == 2) doCall(context, arr[1]);
        }

    }

    // slate://detailCalendar/1075
    private static String detailCalendar(String uri) {
        String[] array = uri.split("detailCalendar/");
        if (array.length == 2) {
            return array[1];
        }
        return "";
    }

    /**
     * 跳转到拨号页面
     *
     * @param context
     * @param telNumber 电话号码
     */
    public static void doCall(Context context, String telNumber) {
        try {
            Uri uri = Uri.parse("tel:" + telNumber); // 拨打电话号码的URI格式
            Intent intent = new Intent(); // 实例化Intent
            intent.setAction(Intent.ACTION_DIAL); // 指定Action
            intent.setData(uri); // 设置数据
            context.startActivity(intent);// 启动Acitivity
        } catch (Exception e) {
            Tools.showToast(context, R.string.dial_error);
            e.printStackTrace();
        }
    }

    /**
     * 如果slate以link为开头，跳转至外部浏览器
     *
     * @param context
     * @param link
     */
    public static void doLinkHttp(Context context, String link) {
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ((Activity) context).startActivity(intent);
    }


    /**
     * 跳转到内部浏览器
     *
     * @param link
     */
    public static void doLinkWeb(Context context, String link, Class<?>... cls) {

        Log.e("跳转到内部浏览器", link);

        Intent intent = new Intent(context, AboutActivity.class);
        intent.putExtra("inapp_webview_url", link);
        context.startActivity(intent);
        if (context instanceof Activity)
            ((Activity) context).overridePendingTransition(R.anim.down_in, R.anim.hold);
    }


}
