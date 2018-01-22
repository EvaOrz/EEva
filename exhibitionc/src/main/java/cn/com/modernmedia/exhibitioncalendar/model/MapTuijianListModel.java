package cn.com.modernmedia.exhibitioncalendar.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.model.Entry;

/**
 * Created by Eva. on 2018/1/22.
 */

public class MapTuijianListModel extends Entry {

    private List<MapTuijianModel> mapTuijianModels = new ArrayList<>();

    private int count;

    public List<MapTuijianModel> getMapTuijianModels() {
        return mapTuijianModels;
    }

    public void setMapTuijianModels(List<MapTuijianModel> mapTuijianModels) {
        this.mapTuijianModels = mapTuijianModels;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public MapTuijianListModel()

    {
    }

    /**
     * "id": 4,
     * "type": 1,
     * "bd_lon": "123.00000000",
     * "bd_lat": "12.01000000",
     * "gcj_lon": "123.00000000",
     * "gcj_lat": "12.00000000",
     * "title": "title",
     * "address": "address",
     * "status": 1,
     * "distance": "0"
     */
    public static class MapTuijianModel extends Entry {
        private int id;
        private int type;
        private String bd_lon;

        private String bd_lat;
        private String gcj_lon;
        private String gcj_lat;
        private String title;
        private String address;
        private int status;
        private String distance;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getBd_lon() {
            return bd_lon;
        }

        public void setBd_lon(String bd_lon) {
            this.bd_lon = bd_lon;
        }

        public String getBd_lat() {
            return bd_lat;
        }

        public void setBd_lat(String bd_lat) {
            this.bd_lat = bd_lat;
        }

        public String getGcj_lon() {
            return gcj_lon;
        }

        public void setGcj_lon(String gcj_lon) {
            this.gcj_lon = gcj_lon;
        }

        public String getGcj_lat() {
            return gcj_lat;
        }

        public void setGcj_lat(String gcj_lat) {
            this.gcj_lat = gcj_lat;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public MapTuijianModel() {
        }

        public static MapTuijianModel parseMapTuijianModel(JSONObject jsonObject) {
            if (jsonObject == null) return null;
            MapTuijianModel model = new MapTuijianModel();
            model.setId(jsonObject.optInt("id"));
            model.setType(jsonObject.optInt("type"));
            model.setTitle(jsonObject.optString("title"));
            model.setAddress(jsonObject.optString("address"));
            model.setBd_lat(jsonObject.optString("bd_lat"));
            model.setBd_lon(jsonObject.optString("bd_lon"));
            model.setGcj_lon(jsonObject.optString("gcj_lon"));
            model.setGcj_lat(jsonObject.optString("gcj_lat"));
            model.setStatus(jsonObject.optInt("status"));
            model.setDistance(jsonObject.optString("distance"));

            return model;
        }
    }

    public static MapTuijianListModel parseMapTuijianListModel(MapTuijianListModel mapTuijianListModel, JSONObject jsonObject) {
        if (isNull(jsonObject)) return null;
        List<MapTuijianModel> datas = new ArrayList<>();
        JSONArray jsonArray = jsonObject.optJSONArray("map");
        if (jsonArray == null) return null;
        for (int i = 0; i < jsonArray.length(); i++) {
            MapTuijianModel c = new MapTuijianModel();
            datas.add(c.parseMapTuijianModel(jsonArray.optJSONObject(i)));

        }
        mapTuijianListModel.setMapTuijianModels(datas);
        return mapTuijianListModel;
    }
}
