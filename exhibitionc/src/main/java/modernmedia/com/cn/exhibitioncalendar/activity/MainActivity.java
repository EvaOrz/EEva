package modernmedia.com.cn.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.corelib.model.WeatherModel;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.adapter.CoverVPAdapter;
import modernmedia.com.cn.exhibitioncalendar.api.ApiController;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel;
import modernmedia.com.cn.exhibitioncalendar.model.TagListModel;
import modernmedia.com.cn.exhibitioncalendar.view.MainCityListScrollView;


/**
 * Created by Eva. on 17/3/27.
 */

public class MainActivity extends BaseActivity {
    private ImageView weatherImg;
    private TextView weatherTxt;
    private ApiController apiController;
    private WeatherModel weatherModel;
    private TagListModel tagListModel;
    private MainCityListScrollView mainCityListScrollView;
    private CalendarListModel calendarListModel;
    private ViewPager coverPager, detailPager;
    private CoverVPAdapter coverVPAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        apiController = ApiController.getInstance(this);
        apiController.getWeather(this, "beijing", new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof WeatherModel) {
                    weatherModel = (WeatherModel) entry;
                    handler.sendEmptyMessage(0);
                }
            }
        });

        apiController.getCitys(this, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof TagListModel) {
                    tagListModel = (TagListModel) entry;
                    handler.sendEmptyMessage(1);
                }
            }
        });
        apiController.getRecommondList(this, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    calendarListModel = (CalendarListModel) entry;
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!TextUtils.isEmpty(weatherModel.getIcon()))
                        MyApplication.finalBitmap.display(weatherImg, weatherModel.getIcon());
                    weatherTxt.setText(weatherModel.getDesc());
                    break;
                case 1:
                    mainCityListScrollView.setData(tagListModel.getHouseOrCities());
                    break;
                case 2:
                    if (calendarListModel != null) {
                        coverVPAdapter = new CoverVPAdapter(MainActivity.this, calendarListModel.getCalendarModels());
                        coverPager.setAdapter(coverVPAdapter);
                        coverVPAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    private void initView() {
        findViewById(R.id.main_left).setOnClickListener(this);
        findViewById(R.id.main_right).setOnClickListener(this);

        mainCityListScrollView = (MainCityListScrollView) findViewById(R.id.main_city_listview);
        weatherImg = (ImageView) findViewById(R.id.main_weather_img);
        weatherTxt = (TextView) findViewById(R.id.main_weather_txt);
        coverPager = (ViewPager) findViewById(R.id.main_viewpager_cover);
        detailPager = (ViewPager) findViewById(R.id.main_viewpager_detail);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_left:
                startActivity(new Intent(MainActivity.this, LeftActivity.class));
                break;
            case R.id.main_right:
                startActivity(new Intent(MainActivity.this, MyListActivity.class));
                break;
        }

    }
}
