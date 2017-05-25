package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;
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
    private ImageView addButton;
    private View view;
    private ApiController apiController;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 图片太大了
                    if (calendarModel != null && !TextUtils.isEmpty(calendarModel.getBackgroundImg()))
                        MyApplication.finalBitmap.display(view, calendarModel.getBackgroundImg());

                    List<CalendarModel> ll = AppValue.myList.getCalendarModels();
                    if (ParseUtil.listNotNull(ll)) {
                        for (int i = 0; i < ll.size(); i++) {
                            if (ll.get(i).getItemId().equals(calendarModel.getItemId())) {
                                addButton.setVisibility(View.INVISIBLE);
                            }
                        }

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);
        id = getIntent().getStringExtra(UriParse.DETAILCALENDAR);

        apiController = ApiController.getInstance(this);
        initView();
        if (getIntent().getSerializableExtra("calendar_detail") != null) {
            calendarModel = (CalendarModel) getIntent().getSerializableExtra("calendar_detail");
            id = calendarModel.getItemId();
            webView.setCalendarModel(calendarModel);
            webView.loadUrl(UrlMaker.getDetailPage() + "?itemid=" + id);
            handler.sendEmptyMessage(0);
        } else initData();
    }

    private void initData() {
        apiController.getDetail(this, id, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarModel) {
                    calendarModel = (CalendarModel) entry;
                    webView.setCalendarModel(calendarModel);
                    handler.sendEmptyMessage(0);
                }

            }
        });
    }

    private void initView() {
        view = findViewById(R.id.detail_bg);
        findViewById(R.id.detail_back).setOnClickListener(this);
        addButton = (ImageView) findViewById(R.id.detail_add);
        addButton.setOnClickListener(this);
        findViewById(R.id.detail_share).setOnClickListener(this);
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
            case R.id.detail_add:

                if (DataHelper.getUserLoginInfo(CalendarDetailActivity.this) == null) {
                    startActivity(new Intent(CalendarDetailActivity.this, LoginActivity.class));

                } else {
                    Intent i = new Intent(CalendarDetailActivity.this, AddActivity.class);
                    i.putExtra("add_type", 0);
                    i.putExtra("add_detail", calendarModel);
                    startActivity(i);
                }
                break;
            case R.id.detail_share:
                if (calendarModel != null) {
                    calendarModel.setWeburl(UrlMaker.getDetailPage() + "?itemid=" + id);
                    new ShareDialog(CalendarDetailActivity.this, calendarModel);
                }

                break;


        }

    }
}
