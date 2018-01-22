package cn.com.modernmedia.exhibitioncalendar.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.model.MapTuijianListModel;

/**
 * 获取地图上推荐
 * Created by Eva. on 2018/1/22.
 */

public class GetMapTuijianApi extends BaseApi {
    private String post;
    private MapTuijianListModel mapTuijianListModel = new MapTuijianListModel();

    /**
     * {
     * "appid": "61",
     * "page":"1",
     * "limit":"20",
     * "map": [
     * {
     * "bd_lon": "143.00000000",
     * "bd_lat": "22.00000000",
     * "gcj_lon": "123",
     * "gcj_lat": "12"
     * <p>
     * }
     * ]
     * }
     */
    public GetMapTuijianApi(String bd_lon, String bd_lat) {
        JSONObject object = new JSONObject();
        try {
            object.put("appid", MyApplication.APPID + "");
            object.put("page", "1");
            object.put("limit", "20");
            JSONArray array = new JSONArray();
            JSONObject js = new JSONObject();
            js.put("bd_lon", bd_lon);
            js.put("bd_lat", bd_lat);
            array.put(js);
            object.put("map", array);
            post = object.toString();
            setPostParams(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getPostParams() {
        return post;
    }

    protected void setPostParams(String params) {
        this.post = params;
    }

    @Override
    protected void saveData(String data) {

    }

    @Override
    protected void handler(JSONObject jsonObject) {
        if (isNull(jsonObject)) return;
        Log.e("GetMapTuijianApi", jsonObject.toString());
        ErrorMsg errorMsg = new ErrorMsg();
        JSONObject errorObject = jsonObject.optJSONObject("error");
        if (!isNull(errorObject)) {
            errorMsg.setNo(errorObject.optInt("no", -1));
            errorMsg.setDesc(errorObject.optString("desc", ""));
        } else {
            mapTuijianListModel = MapTuijianListModel.parseMapTuijianListModel
                    (mapTuijianListModel ,jsonObject);
        }
    }

    public MapTuijianListModel getData() {
        return mapTuijianListModel;
    }

    @Override
    protected String getUrl() {

        return UrlMaker.getMapTuijianList();
    }
}

