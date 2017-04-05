package modernmedia.com.cn.exhibitioncalendar.util;

import android.text.TextUtils;

import modernmedia.com.cn.corelib.util.ParseUtil;
import modernmedia.com.cn.exhibitioncalendar.model.AdvListModel;
import modernmedia.com.cn.exhibitioncalendar.model.AdvListModel.AdvItem;


public class AdvTools {
    /**
     * 广告是否过期
     *
     * @param starttime
     * @param endtime
     * @return
     */
    public static boolean advIsExpired(String starttime, String endtime) {
        long currentTime = System.currentTimeMillis() / 1000;
        return currentTime < ParseUtil.stol(starttime) || currentTime > ParseUtil.stol(endtime);
    }

    /**
     * 获取是否包含当前cat
     *
     * @param catPosition
     * @param item
     * @param catId
     * @return
     */
    public static boolean containThisCat(int catPosition, AdvListModel.AdvItem item, String catId) {
        return parseStarPosition(item, item.getTagname(), catPosition, catId);
    }

    /**
     * 解析带*的位置
     *
     * @param item
     * @param star     例如cat为item.getCatId,组图为item.getPage...
     * @param position
     * @param value    当star不包含*时，判断value和star的值是否相等(cat,page)
     * @return
     */
    private static boolean parseStarPosition(AdvItem item, String star, int position, String value) {
        if (item.getAdvType() == AdvListModel.BETWEEN_CAT && position == -1)// 只有栏目间广告需要判断position
            return false;
        if (TextUtils.isEmpty(star) || advIsExpired(item.getStartTime(), item.getEndTime()))
            return false;
        if (!TextUtils.isEmpty(star)) {
            if (star.contains("*")) {
                return containPositionByStar(star, position);
            } else {
                return containMoreId(star, value);
            }
        }
        return false;
    }

    /**
     * 如果是*，* / 2。。。这些位置的时候，是否包含当前position
     *
     * @param star
     * @param position
     * @return
     */
    public static boolean containPositionByStar(String star, int position) {
        if (star.equals("*")) {
            // TODO star为*,代表所有
            return true;
        }
        String splite[] = star.split("/");
        if (splite.length == 2) {
            int s = ParseUtil.stoi(splite[1], -1);
            if (s != -1) {
                // TODO 表示每s个元素包含一个广告或者每s个元素前面添加广告
                return position % s == 0;
            }
        }
        return false;
    }

    public static boolean containMoreId(String star, String value) {
        if (!star.contains("-") && !star.contains(",")) {
            // TODO 如果位置不带*并且不带"-"和",",那么直接比较位置和当前value是否相等
            return value.equals(star);
        }
        if (star.startsWith("-")) {
            // TODO 排除star包含的值（只要不包含在排除的里面，就行）
            star = star.substring(1);
            boolean excludeThis = false;
            for (String parm : star.split(",")) {
                if (value.equals(parm)) {
                    excludeThis = true;
                    break;
                }
            }
            return !excludeThis;
        } else {
            boolean containThis = false;
            for (String parm : star.split(",")) {
                if (value.equals(parm)) {
                    containThis = true;
                    break;
                }
            }
            return containThis;
        }
    }

//
//    /**
//     * 广告展示的统计
//     *
//     * @param item
//     */
//    public static void requestImpression(ArticleItem item) {
//        if (item != null && item.getAdvTracker() != null) {
//            requestImpression(item.getAdvTracker().getImpressionUrl());
//        }
//    }
//
//    /**
//     * 广告展示的统计
//     *
//     * @param url
//     */
//    public static void requestImpression(String url) {
//        HttpRequestController.getInstance().requestHttp(url);
//    }
//
//    /**
//     * 广告点击的统计
//     *
//     * @param item
//     */
//    public static void requestClick(ArticleItem item) {
//        if (item != null && item.getAdvTracker() != null) {
//            requestClick(item.getAdvTracker().getClickUrl());
//        }
//    }
//
//    /**
//     * 广告点击的统计
//     *
//     * @param url
//     */
//    private static void requestClick(String url) {
//        HttpRequestController.getInstance().requestHttp(url);
//    }
}
