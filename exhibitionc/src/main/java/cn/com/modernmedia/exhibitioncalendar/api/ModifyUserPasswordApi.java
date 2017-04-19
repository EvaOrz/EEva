package cn.com.modernmedia.exhibitioncalendar.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.model.UserModel;


public class ModifyUserPasswordApi extends BaseApi {
    // post 参数设置
    private String post;
    private UserModel user = new UserModel();
    private Context context;

    protected ModifyUserPasswordApi(Context c, String password, String newPassword) {
        super();
        this.context =c;
        // post 参数设置
        JSONObject object = new JSONObject();
        try {
            object.put("uid", DataHelper.getUid(c));
            object.put("token", DataHelper.getToken(c));
            object.put("username", DataHelper.getUserLoginInfo(c).getUserName());
            if (TextUtils.isEmpty(password)) password = "";

            object.put("password", password);
            object.put("newpassword", newPassword);

            post = object.toString();
            setPostParams(post);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
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
        Log.e("ModifyUserPasswordApi", jsonObject.toString());
        ErrorMsg errorMsg = new ErrorMsg();
        JSONObject errorObject = jsonObject.optJSONObject("error");
        if (!isNull(errorObject)) {
            errorMsg.setNo(errorObject.optInt("no", -1));
            errorMsg.setDesc(errorObject.optString("desc", ""));
            user.setError(errorMsg);
        }
        if (errorMsg.getNo() == 0) {
            user = UserModel.parseUserModel(user, jsonObject);
            DataHelper.saveUserLoginInfo(context, user);
        }

    }

    public UserModel getUser() {
        return user;
    }



    @Override
    protected String getUrl() {
        return UrlMaker.getModifyPasswordUrl();
    }

}
