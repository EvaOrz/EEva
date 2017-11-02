package cn.com.modernmedia.corelib.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eva. on 2017/10/31.
 */

public class VipInfoModel extends Entry {

    private int appid;
    private String nickname = "";
    private String realname = "";
    private String email = "";
    private String phone = "";
    private String province = "";
    private String city = "";
    private String address = "";
    private String goods_id = "";
    private String vip_name = "";
    private String vip_starttime = "";
    private String vip_endtime = "";
    private String icon = "";
    private int level;
    private List<Integer> levellist = new ArrayList<>();

    public  VipInfoModel() {
    }

    public static VipInfoModel parseVipInfoModel(JSONObject jsonObject) {
        VipInfoModel vipInfoModel = new VipInfoModel();
        vipInfoModel.setAppid(jsonObject.optInt("appid"));
        vipInfoModel.setNickname(jsonObject.optString("nickname"));
        vipInfoModel.setRealname(jsonObject.optString("realname"));
        vipInfoModel.setEmail(jsonObject.optString("email"));
        vipInfoModel.setPhone(jsonObject.optString("phone"));
        vipInfoModel.setProvince(jsonObject.optString("province"));
        vipInfoModel.setCity(jsonObject.optString("city"));
        vipInfoModel.setAddress(jsonObject.optString("address"));
        vipInfoModel.setGoods_id(jsonObject.optString("goods_id"));
        vipInfoModel.setVip_name(jsonObject.optString("vip_name"));
        vipInfoModel.setVip_endtime(jsonObject.optString("vip_endtime"));
        vipInfoModel.setVip_starttime(jsonObject.optString("vip_starttime"));
        vipInfoModel.setIcon(jsonObject.optString("icon"));
        vipInfoModel.setLevel(jsonObject.optInt("level"));

        List<Integer> levels = new ArrayList<>();
        JSONArray array = jsonObject.optJSONArray("levellist");
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject j = array.optJSONObject(i);
                if (j != null) {
                    levels.add(j.optInt("level"));
                }
            }
        }
        vipInfoModel.setLevellist(levels);
        return vipInfoModel;
    }


    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getVip_name() {
        return vip_name;
    }

    public void setVip_name(String vip_name) {
        this.vip_name = vip_name;
    }

    public String getVip_starttime() {
        return vip_starttime;
    }

    public void setVip_starttime(String vip_starttime) {
        this.vip_starttime = vip_starttime;
    }

    public String getVip_endtime() {
        return vip_endtime;
    }

    public void setVip_endtime(String vip_endtime) {
        this.vip_endtime = vip_endtime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Integer> getLevellist() {
        return levellist;
    }

    public void setLevellist(List<Integer> levellist) {
        this.levellist = levellist;
    }
}
