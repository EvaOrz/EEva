package modernmedia.com.cn.exhibitioncalendar.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import modernmedia.com.cn.corelib.http.BaseApi;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.corelib.model.WeatherModel;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;

/**
 * Created by Eva. on 17/4/6.
 */

public class GetWeatherApi extends BaseApi {
    private String city;
    private JSONObject postObject = new JSONObject();
    private WeatherModel weatherModel = new WeatherModel();

    public GetWeatherApi(Context c, String city) {
        this.city = city;
        try {
            addPostParams(postObject, "appid", MyApplication.APPID + "");
            addPostParams(postObject, "version", Tools.getAppVersion(c));

            setPostParams(postObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setIsNeedEncode(true);
    }

    @Override
    protected String getUrl() {
        return UrlMaker.getWeather(city);
    }

    @Override
    protected void handler(JSONObject jsonObject) {
        if (jsonObject == null) return;
        Log.e("GetWeatherApi", jsonObject.toString());
        weatherModel.setIcon(jsonObject.optString("icon_url"));
        weatherModel.setAlarm(jsonObject.optString("alarms"));
        JSONArray array = jsonObject.optJSONArray("desc");
        String a = "";

        for (int i = 0; i < array.length(); i++) {
            a = a + array.optString(i) + " ";
        }
        weatherModel.setDesc(a);
    }

    @Override
    protected JSONObject getPostParams() {
        return postObject;
    }

    protected void setPostParams(JSONObject params) {
        this.postObject = params;
    }

    @Override
    protected void saveData(String data) {

    }

    public Entry getData() {
        return weatherModel;
    }
}
