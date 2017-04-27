package cn.com.modernmedia.exhibitioncalendar.api;

import android.util.Log;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.exhibitioncalendar.model.UploadAvatarResult;

/**
 * 上传头像
 *
 * @author ZhuQiao
 */
public class UploadAvaterApi extends BaseApi {

    private String imagePath; // 头像在本地的路径
    private UploadAvatarResult result; // 上传结果

    protected UploadAvaterApi(String imagePath) {
        this.imagePath = imagePath;
        result = new UploadAvatarResult();
    }

    protected UploadAvatarResult getUploadResult() {
        return result;
    }

    @Override
    protected String getUrl() {
        return UrlMaker.getUploadAvatarUrl();
    }

    @Override
    protected String getPostImagePath() {
        return imagePath;
    }

    @Override
    protected void handler(JSONObject jsonObject) {
        if (jsonObject != null) {
            Log.e("UploadAvaterApi", jsonObject.toString());
            result.setStatus(jsonObject.optString("status", ""));
            result.setMsg(jsonObject.optString("msg", ""));
            result.setImagePath(jsonObject.optString("url", ""));
            result.setAvatarPath(jsonObject.optString("avatar", ""));
        }
    }

    @Override
    protected void saveData(String data) {

    }

}
