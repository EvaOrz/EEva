package cn.com.modernmedia.exhibitioncalendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.MuseumListModel.MuseumModel;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;
import cn.com.modernmedia.exhibitioncalendar.view.CommonWebView;
import cn.com.modernmedia.exhibitioncalendar.view.ShareDialog;

/**
 * 展馆详情页面
 * Created by Eva. on 2017/10/19.
 */

public class MuseumDetailActivity extends BaseActivity {


    private String id = "";
    private MuseumModel museumModel;
    private ApiController apiController;
    private CommonWebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_detail);
        id = getIntent().getStringExtra(UriParse.DETAILMUSEUM);
        apiController = ApiController.getInstance(this);
        initView();
        initData();
        MyApplication.museumDetailActivity = this;
    }


    private void initView() {
        findViewById(R.id.detail_back).setOnClickListener(this);
        webView = (CommonWebView) findViewById(R.id.detail_webview);
        if (!TextUtils.isEmpty(id)) {
            webView.loadUrl(UrlMaker.getZhanguanDetail(id));
        }
    }

    private void initData() {
        apiController.getMuseumDetail(this, id, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof MuseumModel) {
                    museumModel = (MuseumModel) entry;
                    //                    webView.loadUrl(UrlMaker.getZhanguanDetail(id) );
                }

            }
        });
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle b = msg.getData();
                    String ss = b.getString("share_json");
                    try {
                        JSONObject sss = new JSONObject(ss);
                        if (sss == null) return;
                        JSONObject json = sss.optJSONObject("params");
                        if (json == null) return;
                        String pic = json.optString("thumb");
                        String desc = json.optString("desc");
                        String link = json.optString("link");
                        String title = json.optString("title");

                        new ShareDialog(MuseumDetailActivity.this, title, desc, pic, UrlMaker.getZhanguanDetail(link));
                    } catch (JSONException e) {

                    }
                    break;

            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_back:
                finish();
        }
    }
}