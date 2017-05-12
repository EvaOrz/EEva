package cn.com.modernmedia.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.view.CommonWebView;

/**
 * Created by Eva. on 17/4/4.
 */

public class CalendarListActivity extends BaseActivity {

    private CommonWebView wbWebView;
    private String tagId, tagName, titleTxt;
    private ImageView back;
    private TextView tilte;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_list);
        tagId = getIntent().getStringExtra("list_tagid");
        tagName = getIntent().getStringExtra("list_tagname");
        titleTxt = getIntent().getStringExtra("list_title");
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.calendar_list_back);
        back.setOnClickListener(this);
        tilte = (TextView) findViewById(R.id.calendar_list_title);
        wbWebView = (CommonWebView) findViewById(R.id.list_webview);
        String url = "";
        try {
            if (!TextUtils.isEmpty(titleTxt)) {
                tilte.setText(titleTxt);
                back.setImageResource(R.mipmap.finish_white);
                url = UrlMaker.getTagPage() + "?tagid=" + tagId + "&sharetitle=" + URLEncoder.encode(URLEncoder.encode(titleTxt, "UTF-8"), "UTF-8");
            } else
                url = UrlMaker.getHomePage() + "?tagid=" + tagId + "&tagname=" + URLEncoder.encode(URLEncoder.encode(tagName, "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        wbWebView.loadUrl(url);


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
