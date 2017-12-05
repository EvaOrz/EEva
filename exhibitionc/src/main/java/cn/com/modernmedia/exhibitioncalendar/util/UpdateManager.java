package cn.com.modernmedia.exhibitioncalendar.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.util.FileManager;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.VersionModel;

/**
 * 版本升级
 *
 * @author ZhuQiao
 */
public class UpdateManager {
    private static final int DOWN_UPDATE = 10000;
    private static final int DOWN_OVER = 20000;
    public static final int CHECK_DOWN = 30000;

    private Context mContext;
    private CheckVersionListener listener;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private int progress;
    private boolean interceptFlag = false;
    private String apkName = "";
    private VersionModel version;
    private Window window;
    private Dialog mDialog;
    private TextView lodTxt, zanbuTxt, downTxt, cancleTxt;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                case CHECK_DOWN:
                    showNoticeDialog();
                    break;
            }
        }

        ;
    };

    public interface CheckVersionListener {
        /**
         * 比较已经停止，继续读取getissue接口
         */
        public void checkEnd();
    }

    public UpdateManager(Context mContext, CheckVersionListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    /**
     * 获取服务器数据
     */
    public void checkVersion() {
        ApiController.getInstance(mContext).checkVersion(Tools.getAppVersionName(mContext), new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                if (entry instanceof VersionModel) {
                    version = (VersionModel) entry;
                    /**
                     * 存储最新版本号
                     */
                    DataHelper.setLastVersion(mContext, version.getVersion());
                    compare();
                } else {
                    listener.checkEnd();
                }
            }
        });
    }

    /**
     * 获取服务器数据
     */
    public void checkVersion(VersionModel version) {
        if (DataHelper.getRefuseNoticeVersion(mContext)) return;
        this.version = version;
        compare();
    }


    /**
     * 线上版本高于本地版本
     * <p>
     * 没有新版本把本地时间置空
     */
    private void compare() {
        Log.e("版本更新", version.getVersion() + "^^^" + Tools.getAppIntVersionName(mContext));
        if (version.getVersion() > Tools.getAppIntVersionName(mContext) && !TextUtils.isEmpty(version.getDownload_url())) {

            mHandler.sendEmptyMessage(CHECK_DOWN);
        } else {
            listener.checkEnd();
        }
    }

    /**
     * 显示更新dialog
     */
    private void showNoticeDialog() {
        mDialog = new Dialog(mContext, R.style.CustomDialog);
        mDialog.show();
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_update);

        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lodTxt = (TextView) window.findViewById(R.id.update_log);
        zanbuTxt = (TextView) window.findViewById(R.id.update_zanbu);
        downTxt = (TextView) window.findViewById(R.id.update_download);
        cancleTxt = (TextView) window.findViewById(R.id.update_cancle);
        lodTxt.setText(version.getChangelog());
        lodTxt.setVisibility(View.VISIBLE);
        mProgress = (ProgressBar) window.findViewById(R.id.update_process);
        mProgress.setVisibility(View.GONE);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                listener.checkEnd();
            }
        });
        cancleTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interceptFlag = true;
                DataHelper.setRefuseNoticeVersion(mContext, true);
                listener.checkEnd();
                mDialog.dismiss();
            }
        });
        zanbuTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelper.setRefuseNoticeVersion(mContext, true);
                listener.checkEnd();
                mDialog.dismiss();
            }
        });
        window.findViewById(R.id.update_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lodTxt.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                zanbuTxt.setVisibility(View.GONE);
                downTxt.setVisibility(View.GONE);
                cancleTxt.setVisibility(View.VISIBLE);
                downLoadApk();
            }
        });
    }

    private void dissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            try {
                mDialog.cancel();
                mDialog.dismiss();
                mDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 下载apk
     */
    private void downLoadApk() {
        apkName = new String(CommonApplication.CACHE_FILE_NAME + version.getVersion() + ".apk");
        new DownApkThread(version.getDownload_url()).start();
    }

    private class DownApkThread extends Thread {
        private String apkUrl;

        public DownApkThread(String url) {
            apkUrl = url;
        }

        @Override
        public void run() {
            FileOutputStream fos = null;
            InputStream is = null;
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.e("流量bug查询**", "UpdateManager:DownApkThread" + "-----" + apkUrl);
                conn.connect();
                int length = conn.getContentLength();
                is = conn.getInputStream();

                File ApkFile = FileManager.getApkByName(apkName);
                fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.
            } catch (Exception e) {
                e.printStackTrace();
                showToast();
                dissDialog();
                listener.checkEnd();
            } finally {
                try {
                    if (fos != null) fos.close();
                    if (is != null) is.close();
                    dissDialog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = FileManager.getApkByName(apkName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        mContext.startActivity(intent);

    }

    private void showToast() {
        if (mContext instanceof BaseActivity)
            ((BaseActivity) mContext).showToast(R.string.download_error);

    }
}
