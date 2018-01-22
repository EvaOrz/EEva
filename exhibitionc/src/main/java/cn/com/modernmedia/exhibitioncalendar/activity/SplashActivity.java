package cn.com.modernmedia.exhibitioncalendar.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.LocationClientOption;

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
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;

import static cn.com.modernmedia.exhibitioncalendar.MyApplication.locationListener;
import static cn.com.modernmedia.exhibitioncalendar.MyApplication.mLocationClient;


/**
 * 入版页面
 * Created by Eva. on 17/3/13.
 */

public class SplashActivity extends BaseActivity {
    protected AdvListModel advList;
    private Uri fromHtmlArticleUri;// 网页跳转文章页面url参数
    //    private boolean ifWaitPermission = true;

    private boolean isNomalStart = true;//用于链接点击返回跳出splash画面flag

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (SplashActivity.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                InitLocation();
                gotoMainActivity();
            } else {
                askPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 101);
            }
        } else {
            InitLocation();
            gotoMainActivity();
        }
        /**
         * 网页跳转测试用
         */
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            fromHtmlArticleUri = uri;
            Log.e("fromHtmlArticleUri", fromHtmlArticleUri.toString());
        }
        //展览日历暂时不检查广告
        getAdvList(FetchApiType.USE_HTTP_ONLY);
    }


    @Override
    protected void onResume() {

        if (!isNomalStart) {
            checkHasHtmlAdv();
        }
        super.onResume();

    }

    public void gotoMainActivity() {
        //        if (ifWaitPermission) return;
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

        if (fromHtmlArticleUri != null) {
            String i = fromHtmlArticleUri.toString().replace("slate61://", "slate://");
            boolean flag = UriParse.clickSlate(SplashActivity.this, i, new Entry[]{new AdvItem()}, null, new Class<?>[0]);
            if (flag) {
                isNomalStart = false;
                finish();
            } else {
                checkHasHtmlAdv();
            }
        } else if (ParseUtil.listNotNull(picList)) {
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101:
                // 101的第一个权限 是读定位
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    InitLocation();
                }
                // 101的第二个权限 是读设备id
                if ((grantResults.length > 1) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    DataHelper.setUUID(mContext, Tools.getMyUUID(mContext));
                }

                break;

        }
        //        ifWaitPermission = false;// 可以不用等了
        gotoMainActivity();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置高精度定位定位模式
        option.setCoorType("bd09ll");//设置百度经纬度坐标系格式
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为1000ms
        option.setAddrType("all");
        option.setIsNeedAddress(true);//反编译获得具体位置，只有网络定位才可以
        if (mLocationClient != null) {
            mLocationClient.setLocOption(option);
            mLocationClient.registerLocationListener(locationListener);
            mLocationClient.start();
        }
    }


    @Override
    protected void onStop() {
        if (locationListener != null) {
            mLocationClient.unRegisterLocationListener(locationListener);
            mLocationClient.stop();
        }

        super.onStop();
    }

}
