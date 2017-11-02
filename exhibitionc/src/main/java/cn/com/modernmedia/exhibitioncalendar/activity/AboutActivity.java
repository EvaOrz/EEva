package cn.com.modernmedia.exhibitioncalendar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.view.CommonWebView;

/**
 * 显示单独的网页页面
 * Created by Eva. on 17/4/4.
 */

public class AboutActivity extends Activity implements View.OnClickListener {
    private CommonWebView commonWebView;
    private TextView title, content;
    private String url;
    private int type = 0;// 0：about页面 1：browser 2：全部展馆 3:加载html 4：激活兑换码

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
        content = (TextView) findViewById(R.id.about_textview);
        title = (TextView) findViewById(R.id.about_title);
        if (type == 0) {
            commonWebView.loadUrl(UrlMaker.calendarAboutPage);
        } else if (type == 2) {
            title.setText(R.string.all_zhanguan);
            commonWebView.loadUrl(UrlMaker.getZhanguanList());
        } else if (type == 3) {
            title.setText("用户协议");
            commonWebView.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            content.setText(R.string.notice_vip);
        } else if (type == 4) {
            title.setText(R.string.duihuan_code);
            commonWebView.loadUrl(UrlMaker.getJihuoCode());
        }else {
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
