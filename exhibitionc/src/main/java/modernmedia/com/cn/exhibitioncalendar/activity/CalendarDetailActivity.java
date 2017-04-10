package modernmedia.com.cn.exhibitioncalendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.api.ApiController;
import modernmedia.com.cn.exhibitioncalendar.api.UrlMaker;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import modernmedia.com.cn.exhibitioncalendar.util.UriParse;
import modernmedia.com.cn.exhibitioncalendar.view.CommonWebView;

/**
 * Created by Eva. on 17/4/4.
 */

public class CalendarDetailActivity extends BaseActivity {

    private String id = "";
    private CalendarModel calendarModel;
    private CommonWebView webView;
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);
        id = getIntent().getStringExtra(UriParse.DETAILCALENDAR);
        initView();
        initData();
    }

    private void initData() {
        ApiController.getInstance(this).getDetail(this, id, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarModel) {
                    calendarModel = (CalendarModel) entry;
                    handler.sendEmptyMessage(0);
                }

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 图片太大了
                    if (calendarModel != null && !TextUtils.isEmpty(calendarModel.getBackgroundImg()))
                        MyApplication.finalBitmap.display(view, calendarModel.getBackgroundImg());
                    break;
            }
        }
    };

    private void initView() {
        view = findViewById(R.id.detail_bg);
        findViewById(R.id.detail_back).setOnClickListener(this);
        findViewById(R.id.detail_add).setOnClickListener(this);
        findViewById(R.id.detail_share).setOnClickListener(this);
        webView = (CommonWebView) findViewById(R.id.detail_webview);
        if (!TextUtils.isEmpty(id)) {
            webView.loadUrl(UrlMaker.calendarDetailPage + "?itemid=" + id);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_back:
                finish();
                break;
            case R.id.detail_add:
                break;
            case R.id.detail_share:
                break;


        }

    }
}
