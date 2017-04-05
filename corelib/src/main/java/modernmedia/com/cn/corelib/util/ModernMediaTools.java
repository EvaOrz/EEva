package modernmedia.com.cn.corelib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.Time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.CommonApplication;
import modernmedia.com.cn.corelib.R;
import modernmedia.com.cn.corelib.util.sina.SinaAPI;
import modernmedia.com.cn.corelib.util.sina.SinaRequestListener;


public class ModernMediaTools {
    private static String makeCard = "";
    private static String imageSrc = "";// 点击对象的html属性
    public static final String EMAIL = "feedback@modernmedia.com.cn";
    public static final String CCEMAIL = "4006503206@modernmedia.com.cn";
    private static final String ASSESS_URL = "https://play.google.com/store/apps/details?id=";

    /**
     * 获取系统时间(m-d h:m)
     *
     * @return
     */
    public static String fetchTime() {
        Time localTime = new Time();
        localTime.setToNow();
        String tt = localTime.format("%m-%d %H:%M");
        return tt;
    }


    public static void showLoadView(Context context, int flag) {
        if (context instanceof BaseActivity) {
            switch (flag) {
                case 0:
                    ((BaseActivity) context).disProcess();
                    break;
                case 1:
                    ((BaseActivity) context).showLoading();
                    break;
                case 2:
                    ((BaseActivity) context).showError();
                    break;
                default:
                    break;
            }
        }
    }

    public static String getPackageFileName(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("/")) {
                url = url.substring(url.lastIndexOf("/"));
            }
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
        }
        return url;
    }

    public static String getPackageFolderName(String url) {
        url = getPackageFileName(url);
        if (url.endsWith(".zip")) {
            url = url.substring(0, url.lastIndexOf(".zip"));
        } else if (url.endsWith(".pdf")) {
            url = url.substring(0, url.lastIndexOf(".pdf"));
        }
        return url;
    }

    /**
     * 断点下载
     *
     * @param context
     * @param tagInfo        期信息
     * @param breakPointUtil
     */
    //    public static void downloadPackage(Context context, TagInfo tagInfo, BreakPointUtil breakPointUtil) {
    //        if (tagInfo == null || TextUtils.isEmpty(tagInfo.getIssueProperty().getFullPackage())) {
    //            Tools.showToast(context, R.string.no_issue_zip);
    //            return;
    //        }
    //        BreakPoint breakPoint = new BreakPoint();
    //        if (FileManager.containThisPackageFolder(tagInfo.getIssueProperty().getFullPackage())) {
    //            // TODO 如果包含解压包，则直接加载
    //            breakPoint.setStatus(BreakPointUtil.DONE);
    //        } else if (FileManager.containThisPackage(tagInfo.getIssueProperty().getFullPackage())) {
    //            // TODO 如果包含zip包,执行断点下载
    //            breakPoint.setStatus(BreakPointUtil.BREAK);
    //        } else {
    //            breakPoint.setStatus(BreakPointUtil.NONE);
    //        }
    //        breakPoint.setTagName(tagInfo.getTagName());
    //        breakPoint.setUrl(tagInfo.getIssueProperty().getFullPackage());
    //        CommonApplication.addPreIssueDown(tagInfo.getTagName(), breakPointUtil);
    //        CommonApplication.notityDwonload(tagInfo.getTagName(), 1);
    //        breakPointUtil.downLoad(breakPoint);
    //    }


    /**
     * 获取makecard的js代码
     *
     * @param context
     * @param x
     * @param y
     * @return
     */
    public static String getMakeCard(Context context, int x, int y) {
        if (TextUtils.isEmpty(makeCard)) {
            InputStreamReader is = null;
            BufferedReader br = null;
            try {
                is = new InputStreamReader(context.getAssets().open("make_card"));// 文件只能放在主工程里面。。。
                br = new BufferedReader(is, 1024);
                String line = "";
                while ((line = br.readLine()) != null) {
                    makeCard += line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return makeCard.replace("##x", x + "").replace("##y", y + "");
    }

    /**
     * 获取点击图片的src
     *
     * @param context
     * @param x
     * @param y
     * @return
     */
    public static String getImageSrc(Context context, int x, int y) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("function(){\n");
        buffer.append("  var x = " + x + "; var y = " + y + ";\n");
        buffer.append("  var scale = screen.width / document.documentElement.clientWidth;\n");
        buffer.append("  x = x / scale;\n");
        buffer.append("  y = y / scale;\n");
        buffer.append("  var el = document.elementFromPoint(x, y);\n");
        buffer.append("  if (el.nodeName == 'IMG') {\n");
        buffer.append("    return el.src;\n");
        buffer.append("  }\n");
        buffer.append("  return '';\n");
        buffer.append("}()");
        return buffer.toString();
    }

    public static String getMeta() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("function(){\n");
        buffer.append("  var m = document.getElementsByTagName('meta');\n");
        buffer.append("  for(var i in m) { \n");
        buffer.append("    if(m[i].name == 'sharemessage') {\n");
        buffer.append("      return m[i].content;\n");
        buffer.append("    }\n");
        buffer.append("  }\n");
        buffer.append("  return '';\n");
        buffer.append("}()");
        return buffer.toString();
    }

    public static String getShareMeta() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("function(){\n");
        buffer.append("  var m = document.getElementsByTagName('meta');\n");
        buffer.append("  for(var i in m) { \n");
        buffer.append("    if(m[i].name == '%@') {\n");
        buffer.append("      return m[i].content;\n");
        buffer.append("    }\n");
        buffer.append("  }\n");
        buffer.append("  return '';\n");
        buffer.append("}()");
        return buffer.toString();
    }

    /**
     * 独立栏目数据库where条件
     *
     * @param fromOffset
     * @param toOffset
     * @return
     */
    public static String checkSelection(String fromOffset, String toOffset, String offsetName, boolean containFL) {
        String greaterOperate = containFL ? " >= " : " > ";
        String smallerOperate = containFL ? " <= " : " < ";
        String result = "";
        // TODO 因为要取文章的范围，必须包含第一篇和最后一篇
        String greaterThanFrom = " and " + offsetName + greaterOperate + "'" + fromOffset + "'";
        String smallerThanTo = " and " + offsetName + smallerOperate + "'" + toOffset + "'";
        if (fromOffset.compareTo("0") == 0 && toOffset.compareTo("0") == 0) {
            // 0_0 获取全部
            return result;
        }
        if (fromOffset.compareTo("0") > 0 && toOffset.compareTo("0") > 0) {
            // from_to 获取中间
            result += greaterThanFrom;
            result += smallerThanTo;
            return result;
        }
        if (fromOffset.compareTo("0") > 0) {
            // from_0
            result += greaterThanFrom;
        } else if (toOffset.compareTo("0") > 0) {
            // 0_to
            result += smallerThanTo;
        }
        return result;
    }


    /**
     * 意见反馈
     *
     * @param context
     */
    public static void feedBack(Context context, String subject, String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_CC, new String[]{CCEMAIL});
            // intent.putExtra(Intent.EXTRA_BCC, bccs);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL});
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doAfterIsOAuthed(final Context context) {
        SinaAPI sinaApi = SinaAPI.getInstance(context);
        sinaApi.followUser(CommonApplication.SINA_APP_ID, new SinaRequestListener() {

            @Override
            public void onFailed(String error) {
                Tools.showToast(context, R.string.follow_failed);
            }

            @Override
            public void onSuccess(String response) {
                Looper.prepare();
                new AlertDialog.Builder(context).setMessage(R.string.follow_success).setPositiveButton(R.string.msg_ok, null).create().show();
                Looper.myLooper();
                Looper.loop();
            }
        });
    }

    /**
     * 评价我们
     *
     * @param context
     */
    public static void assess(Context context) {
        Uri uri = Uri.parse(ASSESS_URL + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
