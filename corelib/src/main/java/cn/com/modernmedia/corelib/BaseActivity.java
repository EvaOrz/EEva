package cn.com.modernmedia.corelib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eva. on 17/3/17.
 */

public class BaseActivity extends Activity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    /**
     * 网页跳转uri
     */
    protected String fromHtmlUri = "";
    public Context mContext;

    private Dialog dialog;

    //    private RelativeLayout process_layout;
    //    private ProgressBar loading;
    //    private ImageView error;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        analycisUri();

    }

    /**
     * 分析网页跳转
     */
    private void analycisUri() {
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            fromHtmlUri = uri.toString();

        }
    }

    /**
     * 显示loading图标
     */
    public void showLoading() {
        //        if (process_layout == null) {
        //            System.out.println("未初始化process!!");
        //            return;
        //        }
        //        process_layout.setVisibility(View.VISIBLE);
        //        loading.setVisibility(View.VISIBLE);
        //        error.setVisibility(View.GONE);
    }

    /**
     * 悬浮的dialog
     *
     * @param flag
     */
    public void showLoadingDialog(boolean flag) {
        if (flag) {
            if (dialog == null) {
                dialog = new Dialog(this, R.style.NobackDialog);
                dialog.setContentView(R.layout.processbar);
                dialog.setCancelable(true);
            }
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                    dialog = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showToast(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();

    }

    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }


    /**
     * 显示错误提示
     */
    public void showError() {
        //        if (process_layout == null) {
        //            System.out.println("未初始化process!!");
        //            return;
        //        }
        //        process_layout.setVisibility(View.VISIBLE);
        //        loading.setVisibility(View.GONE);
        //        error.setVisibility(View.VISIBLE);
    }
    //

    /**
     * 隐藏process_layout
     */
    public void disProcess() {
        //        if (process_layout == null) {
        //            System.out.println("未初始化process!!");
        //            return;
        //        }
        //        process_layout.setVisibility(View.GONE);
        //        loading.setVisibility(View.GONE);
        //        error.setVisibility(View.GONE);
    }

    /**
     * 用于给子类继承点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

    }

    public void askPermission(String[] permissions, int requestCode) {
        List<String> ll = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ll.add(permissions[i]);
            }
        }
        if (ll.size() > 0)
            ActivityCompat.requestPermissions(this, (String[]) ll.toArray(new String[ll.size()]), requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101:// 定位权限 + 设备id权限 + 读/写系统存储权限

                break;
            case 102:// 读/写系统日历权限

                break;
            case 103:// 读/写系统存储权限

                break;

        }
    }
}
