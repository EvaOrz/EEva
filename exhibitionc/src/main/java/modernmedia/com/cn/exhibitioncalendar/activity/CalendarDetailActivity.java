package modernmedia.com.cn.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.exhibitioncalendar.R;

/**
 * Created by Eva. on 17/4/4.
 */

public class CalendarDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);
        initView();
    }

    private void initView() {
        findViewById(R.id.detail_back).setOnClickListener(this);
        findViewById(R.id.detail_add).setOnClickListener(this);
        findViewById(R.id.detail_share).setOnClickListener(this);

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
