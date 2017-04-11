package modernmedia.com.cn.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.util.ParseUtil;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.util.AppValue;

/**
 * Created by Eva. on 17/4/1.
 */

public class LeftActivity extends BaseActivity {

    private TextView allNum, myNum;

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
        findViewById(R.id.about_us).setOnClickListener(this);

        allNum = (TextView) findViewById(R.id.all_num);

        myNum = (TextView) findViewById(R.id.my_num);

        if (AppValue.allList != null && ParseUtil.listNotNull(AppValue.allList.getCalendarModels())) {
            allNum.setText("共有" + AppValue.allList.getCalendarModels().size() + "个正在进行/即将展开");
        }
        String s = "共";
        if (AppValue.myList != null && ParseUtil.listNotNull(AppValue.myList.getCalendarModels())) {
            s = s + AppValue.myList.getCalendarModels().size() + "个展览";
        }
        if (AppValue.edList != null && ParseUtil.listNotNull(AppValue.edList.getCalendarModels())) {
            s = s + " 其中" + AppValue.edList.getCalendarModels().size() + "个展览即将开展";
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.left_setting:
                startActivity(new Intent(LeftActivity.this, SettingActivity.class));
                break;
            case R.id.left_sousuo:
                startActivity(new Intent(LeftActivity.this, SearchActivity.class));
                break;
            case R.id.all_zhanlan:
                startActivity(new Intent(LeftActivity.this, CalendarListActivity.class));
                break;
            case R.id.hot_zhanlan:
                Intent i = new Intent(LeftActivity.this, CalendarListActivity.class);
                i.putExtra("list_tagid", "13");
                i.putExtra("list_tagname", "热门");
                startActivity(i);
                break;
            case R.id.my_zhanlan:
                startActivity(new Intent(LeftActivity.this, MyListActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(LeftActivity.this, AboutActivity.class));
                break;


        }
    }
}
