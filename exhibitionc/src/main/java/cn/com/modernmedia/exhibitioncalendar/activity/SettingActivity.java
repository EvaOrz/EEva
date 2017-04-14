package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.widget.EvaSwitchBar;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.push.NewPushManager;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Eva. on 17/4/4.
 */

public class SettingActivity extends BaseActivity implements EvaSwitchBar.OnChangeListener {
    private TextView version;// 版本信息
    private EvaSwitchBar autoLoop;// head自动循环播放开关
    private EvaSwitchBar wifiVedio;// WiFi下视频自动播放开关
    private EvaSwitchBar pushSwitch;// 推送开关

    private boolean index_head_auto_loop = true, wifi_auto_play_vedio = true, ifPush = true;

    private UserModel mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initstatus();
        initView();
    }

    private void initstatus() {
        ifPush = DataHelper.isPushServiceEnable(this);
        if (ifPush) {
            Log.e("setting", "推送开启状态");

        } else Log.e("setting", "推送关闭状态");
    }

    private void initView() {
        mUser = DataHelper.getUserLoginInfo(this);
        findViewById(R.id.setting_back).setOnClickListener(this);

        findViewById(R.id.setting_auto_loop).setOnClickListener(this);
        findViewById(R.id.setting_wifi_auto_vedio).setOnClickListener(this);

        version = (TextView) findViewById(R.id.setting_version);
        initVersion();
        autoLoop = (EvaSwitchBar) findViewById(R.id.auto_loop_switch);
        autoLoop.setChecked(index_head_auto_loop);
        wifiVedio = (EvaSwitchBar) findViewById(R.id.wifi_auto_vedio_switch);
        wifiVedio.setChecked(wifi_auto_play_vedio);
        pushSwitch = (EvaSwitchBar) findViewById(R.id.setting_push_switch);

        pushSwitch.setChecked(ifPush);

        autoLoop.setOnChangeListener(this);
        wifiVedio.setOnChangeListener(this);
        pushSwitch.setOnChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setting_auto_loop) {// 首页焦点图自动轮播

        } else if (v.getId() == R.id.setting_wifi_auto_vedio) {// Wi-Fi下自动播放视频

        } else if (v.getId() == R.id.setting_back) {
            finish();
        }
    }


    /**
     * 消息推送
     */
    private void setPush() {
        Toast.makeText(this, "消息推送", Toast.LENGTH_SHORT).show();
    }


    /**
     * 初始化程序名称和版本信息
     */
    private void initVersion() {
        PackageManager packageManager = getPackageManager();
        try {
            final PackageInfo info = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            String versionText = info.applicationInfo.loadLabel(packageManager) + " " + info.versionName;
            if (MyApplication.DEBUG != 0) {
                versionText = versionText + "（" + "测试版" + "）";
            }
            version.setText(versionText);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }

    /**
     * 清除缓存
     */
    private void clearApplicationData() {
        try {
            File dir = this.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 更新订阅状态用
     */
    @Override
    protected void onResume() {
        super.onResume();
        initstatus();
        initView();
    }


    @Override
    public void onChange(EvaSwitchBar buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.auto_loop_switch) {
            DataHelper.setIndexHeadAutoLoop(SettingActivity.this, isChecked);
        } else if (buttonView.getId() == R.id.wifi_auto_vedio_switch) {
            DataHelper.setWiFiAutoPlayVedio(SettingActivity.this, isChecked);
        } else if (buttonView.getId() == R.id.setting_push_switch) {
            if (isChecked) {// 开启推送
                DataHelper.setPushServiceEnable(this, true);
                resumePush();
            } else {// 关闭推送
                NewPushManager.getInstance(this).closePush(SettingActivity.this);
            }
        }

    }

    public void resumePush() {

        Log.e("极光恢复", "极光恢复推送");
        JPushInterface.resumePush(getApplicationContext());
        Log.e("极光恢复推送", JPushInterface.isPushStopped(getApplicationContext()) ? "停止" : "开启");
    }


}
