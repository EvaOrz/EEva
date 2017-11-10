package cn.com.modernmedia.exhibitioncalendar.api.user;

import android.util.Log;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.VersionModel;

/**
 * 判断app版本接口
 * <p>
 * appid：应用标识
 * type：终端类型（ios|android）
 * version：当前app版本
 * src：下载渠道
 *
 * @author ZhuQiao
 */
public class CheckVersionApi extends BaseApi {
    private String url = "";
    private VersionModel version;

    public CheckVersionApi(String v) {
        url = UrlMaker.checkVersion(v);
        version = new VersionModel();
    }

    public VersionModel getVersion() {
        return version;
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        Log.e("CheckVersionApi", jsonObject.toString());
        JSONObject data = jsonObject.optJSONObject("data");
        version.setVersion(data.optInt("cur_ver", -1));
        version.setSrc(data.optString("src", ""));
        version.setChangelog(data.optString("feature", ""));
        version.setDownload_url(data.optString("download", ""));
    }

    @Override
    protected void saveData(String data) {
    }

}
