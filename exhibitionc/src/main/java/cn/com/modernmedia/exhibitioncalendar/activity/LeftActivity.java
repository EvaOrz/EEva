package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;

/**
 * Created by Eva. on 17/4/1.
 */

public class LeftActivity extends BaseActivity {

    private TextView allNum, myNum, allNum1;

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
        findViewById(R.id.all_zhanguan).setOnClickListener(this);
        findViewById(R.id.choose_city).setOnClickListener(this);
        findViewById(R.id.my_zhanlan).setOnClickListener(this);
        findViewById(R.id.about_us).setOnClickListener(this);

        allNum = (TextView) findViewById(R.id.all_num);
        allNum1 = (TextView) findViewById(R.id.all_num1);
        myNum = (TextView) findViewById(R.id.my_num);


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (AppValue.allList != null && ParseUtil.listNotNull(AppValue.allList.getCalendarModels())) {
            allNum.setText("共有 " + AppValue.allList.getCount() + " 个正在进行/即将展开");
        }
        allNum1.setText("共" + AppValue.museumList.getCount() + "个展馆");
        String s = "共";
        int ing = 0;
        int will = 0;
        if (AppValue.myList != null && ParseUtil.listNotNull(AppValue.myList.getCalendarModels())) {
            ing = AppValue.myList.getCalendarModels().size();
            s = s + ing + "个展览";
            for (CalendarListModel.CalendarModel c : AppValue.myList.getCalendarModels()) {
                if (Tools.getCalendarStatus(c.getStartTime(), c.getEndTime()) == 1) {
                    will++;
                }
            }
        }
        s = s + " 其中 " + will + " 个展览即将开展";
        myNum.setText(s);

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
            case R.id.all_zhanguan:
                Intent i = new Intent(LeftActivity.this, AboutActivity.class);
                i.putExtra("browser_type", 2);
                startActivity(i);
                break;
            case R.id.my_zhanlan:
                startActivity(new Intent(LeftActivity.this, UserCenterActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(LeftActivity.this, AboutActivity.class));
                break;
            case R.id.choose_city:
                startActivity(new Intent(LeftActivity.this, CityPickActivity.class));
                break;


        }
    }
}
