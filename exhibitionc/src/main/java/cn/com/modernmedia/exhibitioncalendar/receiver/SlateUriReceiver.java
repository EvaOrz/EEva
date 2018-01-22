package cn.com.modernmedia.exhibitioncalendar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.activity.LoginActivity;
import cn.com.modernmedia.exhibitioncalendar.activity.MapActivity;

/**
 * Created by Eva. on 17/3/28.
 */

public class SlateUriReceiver extends BroadcastReceiver {

    public static String SLATEURI_LOGIN = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("show_share_dialog")) {

            if (!TextUtils.isEmpty(intent.getExtras().getString("share_json"))) {

                Message ms = new Message();
                ms.what = 0;
                if (!TextUtils.isEmpty(intent.getExtras().getString("share_json"))) {
                    Bundle b = new Bundle();
                    b.putString("share_json", intent.getExtras().getString("share_json"));
                    ms.setData(b);
                }
                if (MyApplication.museumDetailActivity != null && MyApplication.museumDetailActivity.handler != null)
                    MyApplication.museumDetailActivity.handler.sendMessage(ms);
                if (MyApplication.calendarDetailActivity != null && MyApplication.calendarDetailActivity.handler != null)
                    MyApplication.calendarDetailActivity.handler.sendMessage(ms);
            }
        } else if (intent.getAction().equals("open_map_activity")) {
            String ss = intent.getStringExtra("open_map");
            if (!TextUtils.isEmpty(ss)) {
                try {
                    JSONObject sss = new JSONObject(ss);
                    if (sss == null) return;
                    JSONObject json = sss.optJSONObject("params");
                    if (json == null) return;
                    String latitude = json.optString("latitude");
                    String longitude = json.optString("longitude");
                    String title = json.optString("title");
                    String add = json.optString("addr");
                    String img = json.optString("image");


                    Intent i = new Intent(context, MapActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("latitude", latitude);
                    i.putExtra("longitude", longitude);
                    i.putExtra("map_address", add);
                    i.putExtra("map_title", title);
                    i.putExtra("map_img", img);

                    context.startActivity(i);
                } catch (JSONException e) {

                }
            }
        } else if (intent.getAction().equals("open_login_activity")) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
