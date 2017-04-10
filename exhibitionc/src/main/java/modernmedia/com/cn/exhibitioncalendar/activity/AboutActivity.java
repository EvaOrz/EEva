package modernmedia.com.cn.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.api.UrlMaker;
import modernmedia.com.cn.exhibitioncalendar.view.CommonWebView;

/**
 * Created by Eva. on 17/4/4.
 */

public class AboutActivity extends BaseActivity {
    private CommonWebView commonWebView;

    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        url = getIntent().getStringExtra("inapp_webview_url");
        initView();

    }

    private void initView() {
        commonWebView = (CommonWebView) findViewById(R.id.about_webview);
        if (TextUtils.isEmpty(url)) commonWebView.loadUrl(UrlMaker.calendarAboutPage);
        else commonWebView.loadUrl(url);
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
