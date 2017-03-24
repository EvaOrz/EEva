package modernmedia.com.cn.corelib.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户model
 * Created by Eva. on 16/9/16.
 * <p/>
 * * <p/>
 * <p/>
 * <p/>
 * optional int32 uid = 1;
 * optional string token = 2;
 * optional string phone = 3;
 * optional string realname = 4;
 * optional string email = 5;
 * optional string openid = 6;
 * optional string nickname = 7;
 * optional string avatar = 8;
 * optional string password = 9;
 * optional string newpassword = 10;
 * optional string deviceid = 11;
 * optional string devicetoken = 12;
 * optional int32 appid = 13;
 * optional string devicetype = 14;
 * optional string code = 15;
 * optional string opentype = 16;
 * optional string opentoken = 17;
 * optional int32 gender = 18;
 * optional string birthday = 19;
 * optional string education = 20;
 * optional string province = 21;
 * optional string city = 22;
 * optional string area = 23;
 * optional string company = 24;
 * optional string jobtitle = 25;
 * optional Error error = 26;
 * optional string address = 27;
 * optional Contact contact = 28;
 * optional UserExt userext = 29;
 */
public class UserModel extends Entry {
    private String uid = "";
    // 用户名(邮箱或邮箱)
    private String userName = "";
    private String realName = "";
    private String phone = "";
    private String email = "";
    // 密码(注册和修改资料时传)
    private String password = "";
    // 昵称
    private String nickName = "";
    // 头像
    private String avatar = "";
    // 新浪uid(新浪用户登陆时用)
    private String sinaId = "";
    private String weixinId = "";
    private String qqId = "";
    // 用户token
    private String token = "";
    // 设备id
    private String deviceId = "";
    private String hosttype;
    // 设备token
    private String deviceToken = "";
    // 新密码(修改密码时用)
    private String newPassword = "";
    private String company;
    private String position;
    private String province;
    private String city;
    private int sex;//1：男 2：女
    private String birthday;
    private int pushShowDetail = 0;// 推送显示详情
    private int pushBySound = 0;// 推送声音开关
    private int pushByVibrate = 0;//推送震动开关
    private String QrCode;// 二维码
    private Contact contact = new Contact();// 第三方账号信息
    private boolean isFreind = false;// 是否是好友关系

    public boolean isFreind() {
        return isFreind;
    }

    public void setFreind(boolean freind) {
        isFreind = freind;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    // 错误信息
    private ErrorMsg error = new ErrorMsg();

    public ErrorMsg getError() {
        return error;
    }

    public void setError(ErrorMsg error) {
        this.error = error;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSinaId() {
        return sinaId;
    }

    public void setSinaId(String sinaId) {
        this.sinaId = sinaId;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }

    public String getQqId() {
        return qqId;
    }

    public int getPushByVibrate() {
        return pushByVibrate;
    }

    public void setPushByVibrate(int pushByVibrate) {
        this.pushByVibrate = pushByVibrate;
    }

    public int getPushShowDetail() {
        return pushShowDetail;
    }

    public void setPushShowDetail(int pushShowDetail) {
        this.pushShowDetail = pushShowDetail;
    }

    public int getPushBySound() {
        return pushBySound;
    }

    public void setPushBySound(int pushBySound) {
        this.pushBySound = pushBySound;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getQrCode() {
        return QrCode;
    }

    public void setQrCode(String qrCode) {
        QrCode = qrCode;
    }


    public UserModel() {

    }

    /**
     * 解析person列表
     *
     * @param array
     * @return
     */
    public static List<UserModel> parseList(JSONArray array) {
        List<UserModel> personModelList = new ArrayList<UserModel>();
        JSONObject object;
        for (int i = 0; i < array.length(); i++) {
            object = array.optJSONObject(i);
            if (isNull(object)) continue;
            UserModel personModel = new UserModel();
            personModel.parse(object);
            personModelList.add(personModel);
        }
        return personModelList;
    }

    public void parse(JSONObject jsonObject) {
        if (jsonObject == null) return;
        setUid(jsonObject.optInt("uid") + "");
        // getFriends 接口uid解析
        if (jsonObject.optInt("fuid") != 0) setUid(jsonObject.optInt("fuid") + "");

        setPassword(jsonObject.optString("password"));
        setNewPassword(jsonObject.optString("newpassword"));
        setToken(jsonObject.optString("token"));
        setPhone(jsonObject.optString("phone"));
        setAvatar(jsonObject.optString("avatar"));
        setRealName(jsonObject.optString("realname"));
        setNickName(jsonObject.optString("nickname"));
        setEmail(jsonObject.optString("email"));
        setCompany(jsonObject.optString("company"));
        setSex(jsonObject.optInt("sex"));
        setContact(Contact.parse(jsonObject.optJSONObject("contact")));
        setPosition(jsonObject.optString("jobtitle"));
        setBirthday(jsonObject.optString("birthday"));
        setProvince(jsonObject.optString("province"));
        setCity(jsonObject.optString("city"));
        setHosttype(jsonObject.optString("hosttype"));
        setQrCode(jsonObject.optString("QrCode"));
        ErrorMsg e = new ErrorMsg();
        e.parse(jsonObject.optJSONObject("error"));
        setError(e);

        JSONObject userext = jsonObject.optJSONObject("userext");
        if (userext != null) {
            setPushBySound(userext.optInt("pushBySound"));
            setPushByVibrate(userext.optInt("pushByVibrate"));
            setPushShowDetail(userext.optInt("pushShowDetail"));
        }
    }


    public static class Contact extends Entry {
        private String qq = "";
        private String weibo = "";
        private String wechat = "";

        public static Contact parse(JSONObject j) {
            Contact c = new Contact();
            if (j != null) {
                c.setQq(j.optString("qq"));
                c.setWeibo(j.optString("weibo"));
                c.setWechat(j.optString("wechat"));
            }
            return c;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getWeibo() {
            return weibo;
        }

        public void setWeibo(String weibo) {
            this.weibo = weibo;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }
    }

    public String getHosttype() {
        return hosttype;
    }

    public void setHosttype(String hosttype) {
        this.hosttype = hosttype;
    }

    /**
     * list model
     */
    public static class UserListModel extends Entry {

        public UserListModel() {

        }

        private List<UserModel> userModels;

        public List<UserModel> getUserModels() {
            return userModels;
        }

        public void setUserModels(List<UserModel> userModels) {
            this.userModels = userModels;
        }
    }


}
