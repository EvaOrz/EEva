package modernmedia.com.cn.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.api.UrlMaker;
import modernmedia.com.cn.exhibitioncalendar.view.CommonWebView;

/**
 * Created by Eva. on 17/4/4.
 */

public class CalendarListActivity extends BaseActivity {

    private CommonWebView wbWebView;
    private String tagId, tagName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_list);
        tagId = getIntent().getStringExtra("list_tagid");
        tagName = getIntent().getStringExtra("list_tagname");
        initView();
    }

    private void initView() {
        wbWebView = (CommonWebView) findViewById(R.id.list_webview);
        String url = UrlMaker.calendarHomePage;
        if (!TextUtils.isEmpty(tagId) && !TextUtils.isEmpty(tagName)) {
            try {
                url = url + "?tagid=" + tagId + "&tagname=" + URLEncoder.encode(URLEncoder.encode(tagName, "UTF-8"), "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }
        }
        wbWebView.loadUrl(url);
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
