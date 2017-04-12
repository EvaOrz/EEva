package cn.com.modernmedia.exhibitioncalendar.api;

import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.exhibitioncalendar.model.AdvListModel;

/**
 * Created by Eva. on 17/3/30.
 */

public class GetAdvListApi extends BaseApi {
    private AdvListModel advListModel;

    @Override
    protected String getUrl() {
        return UrlMaker.getAdvList();
    }

    @Override
    protected void handler(JSONObject jsonObject) {

    }

    @Override
    protected void saveData(String data) {

    }

    public Entry getAdvList() {
        return advListModel;
    }
}
