package modernmedia.com.cn.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.api.UrlMaker;
import modernmedia.com.cn.exhibitioncalendar.view.CommonWebView;

/**
 * Created by Eva. on 17/4/4.
 */

public class CalendarListActivity extends BaseActivity {

    private CommonWebView wbWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_list);
        initView();
    }

    private void initView() {
        wbWebView = (CommonWebView) findViewById(R.id.list_webview);
        wbWebView.loadUrl(UrlMaker.calendarHomePage);
        findViewById(R.id.calendar_list_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calendar_list_back:
                finish();
                break;
        }
    }
}