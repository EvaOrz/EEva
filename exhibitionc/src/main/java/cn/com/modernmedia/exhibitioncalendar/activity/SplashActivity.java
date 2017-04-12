package cn.com.modernmedia.exhibitioncalendar.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.BaseApi.FetchApiType;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.util.ConstData;
import cn.com.modernmedia.corelib.util.FileManager;
import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.AdvListModel;
import cn.com.modernmedia.exhibitioncalendar.model.AdvListModel.AdvItem;
import cn.com.modernmedia.exhibitioncalendar.model.AdvListModel.AdvItem.AdvSource;
import cn.com.modernmedia.exhibitioncalendar.util.AdvTools;

import static cn.com.modernmedia.corelib.CommonApplication.mContext;


/**
 * 入版页面
 * Created by Eva. on 17/3/13.
 */

public class SplashActivity extends BaseActivity {
    protected AdvListModel advList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getAdvList(FetchApiType.USE_HTTP_ONLY);

        askPermission();
    }


    public void gotoMainActivity() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
                overridePendingTransition(R.anim.alpha_out, R.anim.hold);
            }
        }, ConstData.SPLASH_DELAY_TIME);

    }


    /**
     * 分析入版广告
     */
    private void analycisAd() {
        // 下载广告
        if (TextUtils.equals(DataHelper.getAdvUpdateTime(mContext), "")) {
            // 广告更新时间未变化
            getAdvList(FetchApiType.USE_CACHE_FIRST);
        } else {
            getAdvList(FetchApiType.USE_HTTP_ONLY);
        }
    }

    private void initAdv() {
        if (advList == null || !ParseUtil.mapContainsKey(advList.getAdvMap(), AdvListModel.RU_BAN)) {// 取缓存
            checkHasAdv(null, null);
        } else {// 线上拿
            List<AdvItem> list = advList.getAdvMap().get(AdvListModel.RU_BAN);
            if (ParseUtil.listNotNull(list)) {
                for (AdvItem item : list) {
                    if (checkPicAdvValid(item)) return;
                }
            }
            checkHasAdv(null, null);
        }
    }

    protected void gotoAdvActivity(ArrayList<String> picList, AdvItem item) {
        Intent intent = new Intent(mContext, AdvertisementActivity.class);
        intent.putExtra(AdvertisementActivity.ADVACTIVITY_PIC_LIST, picList);
        intent.putExtra(AdvertisementActivity.ADVACTIVITY_ADV_ITEM, item);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.alpha_out, R.anim.hold);
    }

    protected void checkHasAdv(final ArrayList<String> picList, final AdvListModel.AdvItem item) {

        if (ParseUtil.listNotNull(picList)) {
            gotoAdvActivity(picList, item);
        } else {
            // 没有图片广告，继续判断有没有html广告
            checkHasHtmlAdv();
        }
    }

    /**
     * 判断是否有html入版广告
     */
    private void checkHasHtmlAdv() {
        if (!(advList instanceof AdvListModel)) {
            gotoMainActivity();
            return;
        }
        if (advList == null || !ParseUtil.mapContainsKey(advList.getAdvMap(), AdvListModel.RU_BAN)) {
            gotoMainActivity();
            return;
        }
        List<AdvItem> list = advList.getAdvMap().get(AdvListModel.RU_BAN);
        if (!ParseUtil.listNotNull(list)) {
            gotoMainActivity();
            return;
        }

        for (AdvItem item : list) {
            if (!advIsValid(item)) continue;
            String zip = item.getSourceList().get(0).getUrl();
            if (TextUtils.isEmpty(zip)) continue;
            if (!FileManager.containThisPackageFolder(zip)) continue;
            if (TextUtils.isEmpty(FileManager.getHtmlName(zip))) continue;
            gotoAdvActivity(null, item);
            return;
        }
        gotoMainActivity();
    }


    /**
     * 判断图片广告是否有效
     *
     * @param item
     * @return
     */
    private boolean checkPicAdvValid(AdvItem item) {
        if (!advIsValid(item)) return false;
        if (item.getShowType() != 0)// 不是图片广告
            return false;

        ArrayList<String> picList = new ArrayList<String>();
        for (AdvSource pic : item.getSourceList()) {
            // NOTE 当所有图片都下载成功时，才进入入版广告页
            if (CommonApplication.finalBitmap.getBitmapFromDiskCache(pic.getUrl()) == null) {
                return false;
            }
            picList.add(pic.getUrl());
        }
        checkHasAdv(picList, item);
        return true;
    }

    /**
     * 广告是否有效
     *
     * @param item
     * @return
     */
    protected boolean advIsValid(AdvItem item) {
        // 广告是否过期
        if (AdvTools.advIsExpired(item.getStartTime(), item.getEndTime())) return false;
        // 是否有资源列表
        if (!ParseUtil.listNotNull(item.getSourceList())) return false;
        return true;
    }


    private void getAdvList(FetchApiType fetchApiType) {
        ApiController.getInstance(this).getAdvList(fetchApiType, new FetchEntryListener() {

            @Override
            public void setData(Entry entry) {
                if (entry instanceof AdvListModel) {
                    advList = (AdvListModel) entry;
                }
                initAdv();
            }
        });
    }

    private void askPermission() {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
            //TODO
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ConstData.REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    DataHelper.setUUID(SplashActivity.this, Tools.getMyUUID(SplashActivity.this));
                }
        }
    }

}
