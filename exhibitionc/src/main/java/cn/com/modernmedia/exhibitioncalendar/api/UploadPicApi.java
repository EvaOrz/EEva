package cn.com.modernmedia.exhibitioncalendar.api;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;

/**
 * 上传图片Api
 * Created by Eva. on 17/3/28.
 */

public class UploadPicApi extends BaseApi {

    @Override
    protected String getUrl() {
        return UrlMaker.uploadPic();
    }

    @Override
    protected void handler(JSONObject jsonObject) {

    }

    @Override
    protected void saveData(String data) {

    }
}
