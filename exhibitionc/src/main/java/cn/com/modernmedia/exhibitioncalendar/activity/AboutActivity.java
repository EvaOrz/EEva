package cn.com.modernmedia.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.view.CommonWebView;

/**
 * 显示单独的网页页面
 * Created by Eva. on 17/4/4.
 */

public class AboutActivity extends BaseActivity {
    private CommonWebView commonWebView;
    private TextView title;
    private String url;
    private int type = 0;// 0：about页面 1：browser 2：全部展馆

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        url = getIntent().getStringExtra("inapp_webview_url");
        type = getIntent().getIntExtra("browser_type", 0);
        initView();

    }

    private void initView() {
        commonWebView = (CommonWebView) findViewById(R.id.about_webview);
        title = (TextView) findViewById(R.id.about_title);
        if (type == 0) {
            commonWebView.loadUrl(UrlMaker.calendarAboutPage);
        } else if (type == 2) {
            title.setText(R.string.all_zhanguan);
            commonWebView.loadUrl(UrlMaker.getZhanguanList());
        } else {
            title.setText("");
            commonWebView.loadUrl(url);
        }
        findViewById(R.id.about_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_back:
                finish();
                break;
        }
    }
}
