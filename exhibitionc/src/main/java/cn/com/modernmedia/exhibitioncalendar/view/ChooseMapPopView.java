package cn.com.modernmedia.exhibitioncalendar.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.exhibitioncalendar.R;

/**
 * Created by Eva. on 17/4/25.
 */

public class ChooseMapPopView {

    private Context mContext;
    private PopupWindow window;
    private String ll, lll;

    public ChooseMapPopView(Context context, String ll, String lll) {
        this.mContext = context;
        this.ll = ll;
        this.lll = lll;


        init();
    }

    private void init() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_map_pop, null);
        window = new PopupWindow(view, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setAnimationStyle(R.style.fetch_image_popup_anim);
        window.update();
        window.setBackgroundDrawable(new

                BitmapDrawable());
        window.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        view.findViewById(R.id.map_baidu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNative_Baidu();
            }
        });
        view.findViewById(R.id.map_gaode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNative_Gaode();
            }
        });
        view.findViewById(R.id.map_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNative_Google();
            }
        });
        view.findViewById(R.id.map_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNative_Baidu();
            }
        });

    }


    /**
     * 开发 > URI API > Android
     * 跳转到百度地图
     */
    public void startNative_Baidu() {
        Intent intent;
        if (isAvilible(mContext, "com.baidu.BaiduMap")) {//传入指定应用包名

            try {
                //                          intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving®ion=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                intent = Intent.getIntent("intent://map/direction?" +
                        //"origin=latlng:"+"34.264642646862,108.95108518068&" +   //起点  此处不传值默认选择当前位置
                        "destination=latlng:" + ll + "," + lll + "|name:我的目的地" +        //终点
                        "&mode=driving&" +          //导航路线方式
                        "region=北京" +           //
                        "&src=" + "展览日历" + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                mContext.startActivity(intent); //启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        } else {//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
            Toast.makeText(mContext, "您尚未安装百度地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        }

    }


    /**
     * 开发 > URI API > Android
     * 调起高德地图
     */
    public void startNative_Gaode() {
        Intent intent;
        if (isAvilible(mContext, "com.autonavi.minimap")) {
            try {
                intent = Intent.getIntent("androidamap://navi?sourceApplication=" + "展览日历" + "&poiname=我的目的地&lat=" + ll + "&lon" + "=" + lll + "&dev=0");
                mContext.startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        }
    }


    public void startNative_Google() {
        Intent mapIntent;
        if (isAvilible(mContext, "com.google.android.apps.maps")) {
            //+ ", + Sydney " + "+Australia"
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + ll + "," + lll);
            mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            mContext.startActivity(mapIntent);
        } else {
            Toast.makeText(mContext, "您尚未安装谷歌地图", Toast.LENGTH_LONG).show();

            Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
            mapIntent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(mapIntent);
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
