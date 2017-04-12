package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.model.WeatherModel;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.adapter.CoverVPAdapter;
import cn.com.modernmedia.exhibitioncalendar.adapter.DetailVPAdapter;
import cn.com.modernmedia.exhibitioncalendar.adapter.ExhibitionAdapter;
import cn.com.modernmedia.exhibitioncalendar.adapter.VerticleVPAdapter;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel;
import cn.com.modernmedia.exhibitioncalendar.push.NewPushManager;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;
import cn.com.modernmedia.exhibitioncalendar.view.ChildHeightViewpager;
import cn.com.modernmedia.exhibitioncalendar.view.MainCityListScrollView;
import cn.com.modernmedia.exhibitioncalendar.view.MyListView;
import cn.com.modernmedia.exhibitioncalendar.view.VerticalViewPager;


/**
 * Created by Eva. on 17/3/27.
 */

public class MainActivity extends BaseActivity {
    private ImageView weatherImg, avatar;
    private TextView weatherTxt, actionButton, myNum;
    private ApiController apiController;
    private WeatherModel weatherModel;
    private TagListModel tagListModel;
    private MainCityListScrollView mainCityListScrollView;
    private CalendarListModel calendarListModel;
    private ViewPager coverPager, detailPager;
    private CoverVPAdapter coverVPAdapter;
    private DetailVPAdapter detailVPAdapter;

    private LinearLayout dotLayout;
    private List<ImageView> dots = new ArrayList<>();

    private MyListView listView;
    private ExhibitionAdapter myAdapter;
    private List<CalendarModel> myCalendarList = new ArrayList<>();

    private VerticalViewPager verticalViewPager;
    private VerticleVPAdapter verticleVPAdapter;
    private View page1, page2;
    private UserModel userModel;


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

                        detailVPAdapter = new DetailVPAdapter(MainActivity.this, calendarListModel.getCalendarModels());
                        detailPager.setAdapter(detailVPAdapter);
                        detailVPAdapter.notifyDataSetChanged();

                        initDots(calendarListModel.getCalendarModels());
                    }
                    break;

                case 3:// 用户数据
                    myAdapter.setData(myCalendarList);
                    myAdapter.notifyDataSetChanged();

                    myNum.setText(myCalendarList.size() + "个待参观展览");

                case 4:// 用户数据
                    if (userModel != null)
                        Tools.setAvatar(MainActivity.this, userModel.getAvatar(), avatar);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        uploadDeviceInfoForPush();
    }


    /**
     * 初始化用户数据
     */
    private void initUserData() {
        userModel = DataHelper.getUserLoginInfo(MainActivity.this);
        handler.sendEmptyMessage(4);

        // 正在进行
        apiController.getMyList(this, "1", 1, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    AppValue.myList = (CalendarListModel) entry;
                    myCalendarList.clear();
                    myCalendarList.addAll(AppValue.myList.getCalendarModels());
                    handler.sendEmptyMessage(3);
                }
            }
        });
        //         已经过期
        apiController.getMyList(this, "1", 2, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    AppValue.edList = (CalendarListModel) entry;
                }
            }
        });
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

        apiController.getAllList(this, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    AppValue.allList = (CalendarListModel) entry;
                }
            }
        });
        initUserData();

    }

    private void initView() {
        findViewById(R.id.main_left).setOnClickListener(this);
        avatar = (ImageView) findViewById(R.id.main_right);
        avatar.setOnClickListener(this);

        verticalViewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        page1 = LayoutInflater.from(this).inflate(R.layout.view_main_page1, null);
        page2 = LayoutInflater.from(this).inflate(R.layout.view_main_page2, null);
        List<View> views = new ArrayList<>();
        views.add(page1);
        views.add(page2);
        verticleVPAdapter = new VerticleVPAdapter(this, views);
        verticalViewPager.setAdapter(verticleVPAdapter);
        verticleVPAdapter.notifyDataSetChanged();


        listView = (MyListView) page2.findViewById(R.id.my_listview);
        myAdapter = new ExhibitionAdapter(this);
        listView.setAdapter(myAdapter);
        page2.findViewById(R.id.main_add).setOnClickListener(this);
        ((TextView) page1.findViewById(R.id.main_date)).setText(Tools.format(System.currentTimeMillis(), "dd"));
        ((TextView) page1.findViewById(R.id.main_month)).setText(Tools.getEnDate());
        ((TextView) page1.findViewById(R.id.main_week)).setText(Tools.getChinaDate());
        myNum = (TextView) page1.findViewById(R.id.main_calendar_num);
        mainCityListScrollView = (MainCityListScrollView) page1.findViewById(R.id.main_city_listview);
        weatherImg = (ImageView) page1.findViewById(R.id.main_weather_img);
        weatherTxt = (TextView) page1.findViewById(R.id.main_weather_txt);
        coverPager = (ViewPager) findViewById(R.id.main_viewpager_cover);
        coverPager.setOffscreenPageLimit(5);
        dotLayout = (LinearLayout) page1.findViewById(R.id.main_dot_layout);
        actionButton = (TextView) page1.findViewById(R.id.main_action);
        actionButton.setOnClickListener(this);

        detailPager = (ChildHeightViewpager) page1.findViewById(R.id.main_viewpager_detail);
        detailPager.setOffscreenPageLimit(5);
        detailPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int width = coverPager.getWidth();
                //滑动外部Viewpager
                coverPager.scrollTo((int) (width * position + width * positionOffset), 0);
                updateDots(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        coverPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int width = detailPager.getWidth();
                //滑动外部Viewpager
                detailPager.scrollTo((int) (width * position + width * positionOffset), 0);
                updateDots(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
            case R.id.main_action:
                if (calendarListModel != null) {

                    Intent i = new Intent(MainActivity.this, AddActivity.class);
                    i.putExtra("add_detail", calendarListModel.getCalendarModels().get(detailPager.getCurrentItem()));
                    startActivity(i);
                }
                break;

            case R.id.main_add:// 添加
                startActivity(new Intent(MainActivity.this, CalendarListActivity.class));
                break;
        }

    }


    /**
     * 设置dot
     */
    public void initDots(List<CalendarListModel.CalendarModel> itemList) {
        dotLayout.removeAllViews();
        dots.clear();
        ImageView iv;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(12, 12);

        lp.leftMargin = 5;
        for (int i = 0; i < itemList.size(); i++) {
            iv = new ImageView(this);
            if (i == 0) {
                iv.setImageResource(R.drawable.dot_active);
            } else {
                iv.setImageResource(R.drawable.dot);
            }
            dotLayout.addView(iv, lp);
            dots.add(iv);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NewPushManager.getInstance(this).onresume(this);
        if (MyApplication.loginStatusChange) {
            Log.e("登录状态变化", "登录状态变化");
            initUserData();
            MyApplication.loginStatusChange = false;

        }


    }


    /**
     * Push需要：上传device信息
     */
    private void uploadDeviceInfoForPush() {
        if (DataHelper.isPushServiceEnable(this)) NewPushManager.getInstance(this).register(this);
    }


    public void updateDots(int position) {
        if (dots != null && dots.size() > position && dots.size() > 1) {
            for (int i = 0; i < dots.size(); i++) {
                if (i == position) {
                    dots.get(i).setImageResource(R.drawable.dot_active);
                } else {
                    dots.get(i).setImageResource(R.drawable.dot);
                }
            }
        }
    }
}
