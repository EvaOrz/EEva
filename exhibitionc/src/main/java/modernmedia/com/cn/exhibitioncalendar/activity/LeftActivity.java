package modernmedia.com.cn.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.exhibitioncalendar.R;

/**
 * Created by Eva. on 17/4/1.
 */

public class LeftActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left);
        initView();
    }

    private void initView() {
        findViewById(R.id.left_back).setOnClickListener(this);
        findViewById(R.id.left_setting).setOnClickListener(this);
        findViewById(R.id.left_sousuo).setOnClickListener(this);
        findViewById(R.id.all_zhanlan).setOnClickListener(this);
        findViewById(R.id.hot_zhanlan).setOnClickListener(this);
        findViewById(R.id.my_zhanlan).setOnClickListener(this);
        findViewById(R.id.book_iart).setOnClickListener(this);
        findViewById(R.id.about_us).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.left_setting:
                startActivity(new Intent(LeftActivity.this, SettingActivity.class));
                break;
            case R.id.left_sousuo:
                startActivity(new Intent(LeftActivity.this, SettingActivity.class));
                break;
            case R.id.all_zhanlan:
                startActivity(new Intent(LeftActivity.this, CalendarListActivity.class));
                break;
            case R.id.hot_zhanlan:
                startActivity(new Intent(LeftActivity.this, CalendarDetailActivity.class));
                break;
            case R.id.my_zhanlan:
                startActivity(new Intent(LeftActivity.this, MyListActivity.class));
                break;
            case R.id.book_iart:
                break;
            case R.id.about_us:
                startActivity(new Intent(LeftActivity.this, SettingActivity.class));
                break;


        }
    }
}
