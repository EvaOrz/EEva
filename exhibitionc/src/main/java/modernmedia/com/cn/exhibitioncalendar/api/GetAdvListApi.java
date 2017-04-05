package modernmedia.com.cn.exhibitioncalendar.api;

import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.exhibitioncalendar.model.AdvListModel;

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
