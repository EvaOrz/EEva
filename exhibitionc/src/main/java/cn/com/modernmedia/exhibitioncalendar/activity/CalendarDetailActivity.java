package cn.com.modernmedia.exhibitioncalendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;
import cn.com.modernmedia.exhibitioncalendar.view.CommonWebView;
import cn.com.modernmedia.exhibitioncalendar.view.ShareDialog;

/**
 * Created by Eva. on 17/4/4.
 */

public class CalendarDetailActivity extends BaseActivity {

    private String id = "";
    private CalendarModel calendarModel;
    private CommonWebView webView;
    private ApiController apiController;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    webView.setCalendar(calendarModel);
                    break;
                case 1:// 分享

                    new ShareDialog(mContext, calendarModel);

                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        id = getIntent().getStringExtra(UriParse.DETAILCALENDAR);
        mContext = this;
        MyApplication.calendarDetailActivity = this;
        apiController = ApiController.getInstance(this);
        initView();
        if (getIntent().getSerializableExtra("calendar_detail") != null) {
            calendarModel = (CalendarModel) getIntent().getSerializableExtra("calendar_detail");
            id = calendarModel.getItemId();

            webView.loadUrl(UrlMaker.getDetailPage() + "?itemId=" + id);
            handler.sendEmptyMessage(0);
        } else initData();
    }

    private void initData() {
        apiController.getCalendarDetail(this, id, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarModel) {
                    calendarModel = (CalendarModel) entry;
                    handler.sendEmptyMessage(0);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.calendarDetailActivity = null;
    }

    private void initView() {
        findViewById(R.id.detail_back).setOnClickListener(this);

        webView = (CommonWebView) findViewById(R.id.detail_webview);
        if (!TextUtils.isEmpty(id)) {
            webView.loadUrl(UrlMaker.getDetailPage() + "?itemid=" + id);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_back:
                finish();
                break;

        }

    }
}
