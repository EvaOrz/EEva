package cn.com.modernmedia.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.listener.FetchDataListener;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;

/**
 * Created by Eva. on 2017/10/27.
 */

public class MyVipActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vip);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.vip_back).setOnClickListener(this);
    }

    private void initData() {
        ApiController.getInstance(this).getProducts(new FetchDataListener() {
            @Override
            public void fetchData(boolean isSuccess, String data, boolean fromHttp) {

            }
        });

    }

    private void goPay() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vip_back:
                finish();
                break;
        }

    }
}
