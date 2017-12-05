package cn.com.modernmedia.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.ActiveListModel.ActiveModel;
import cn.com.modernmedia.exhibitioncalendar.util.FlurryEvent;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;
import cn.com.modernmedia.exhibitioncalendar.view.CommonWebView;
import cn.com.modernmedia.exhibitioncalendar.view.ShareDialog;

/**
 * Created by Eva. on 2017/12/1.
 */

public class ActiveActivity extends BaseActivity {
    private ActiveModel activeModel;
    private CommonWebView webView;
    private ImageView addButton;
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        mContext = this;
        if (getIntent().getSerializableExtra(UriParse.DETAILACTIVE) == null || !(getIntent().getSerializableExtra(UriParse.DETAILACTIVE) instanceof ActiveModel))
            finish();
        activeModel = (ActiveModel) getIntent().getSerializableExtra(UriParse.DETAILACTIVE);
        initView();
    }

    private void initView() {
        view = findViewById(R.id.detail_bg);
        findViewById(R.id.detail_back).setOnClickListener(this);
        addButton = (ImageView) findViewById(R.id.detail_add);
        addButton.setOnClickListener(this);
        findViewById(R.id.detail_share).setOnClickListener(this);
        webView = (CommonWebView) findViewById(R.id.detail_webview);
        webView.loadUrl(UrlMaker.getActivePage(activeModel.getActiveId()));
        // 图片太大了
        if (activeModel != null && !TextUtils.isEmpty(activeModel.getBackgroundImg()))
            MyApplication.finalBitmap.display(view, activeModel.getBackgroundImg());

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
                // flurry log
                FlurryEvent.logACShareSingleCalendar(mContext);
                new ShareDialog(ActiveActivity.this, activeModel.getTitle(), activeModel.getContent(), activeModel.getCoverImg(), activeModel.getWeburl());

                break;
        }

    }
}
