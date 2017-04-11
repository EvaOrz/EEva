package modernmedia.com.cn.exhibitioncalendar.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.corelib.model.WeatherModel;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.adapter.CoverVPAdapter;
import modernmedia.com.cn.exhibitioncalendar.adapter.DetailVPAdapter;
import modernmedia.com.cn.exhibitioncalendar.api.ApiController;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel;
import modernmedia.com.cn.exhibitioncalendar.model.TagListModel;
import modernmedia.com.cn.exhibitioncalendar.util.AppValue;
import modernmedia.com.cn.exhibitioncalendar.view.ChildHeightViewpager;
import modernmedia.com.cn.exhibitioncalendar.view.MainCityListScrollView;


/**
 * Created by Eva. on 17/3/27.
 */

public class MainActivity extends BaseActivity {
    private ImageView weatherImg;
    private TextView weatherTxt, actionButton;
    private ApiController apiController;
    private WeatherModel weatherModel;
    private TagListModel tagListModel;
    private MainCityListScrollView mainCityListScrollView;
    private CalendarListModel calendarListModel;
    private ViewPager coverPager, detailPager;
    private CoverVPAdapter coverVPAdapter;
    private DetailVPAdapter detailVPAdapter;

    private LinearLayout dotLayout;
    private View headView;
    private ListView listView;
    private List<ImageView> dots = new ArrayList<>();

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

        apiController.getAllList(this, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    AppValue.allList = (CalendarListModel) entry;
                }
            }
        });

        apiController.getMyList(this, "", 1, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    AppValue.myList = (CalendarListModel) entry;
                }
            }
        });
        apiController.getMyList(this, "", 2, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    AppValue.edList = (CalendarListModel) entry;
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

                        detailVPAdapter = new DetailVPAdapter(MainActivity.this, calendarListModel.getCalendarModels());
                        detailPager.setAdapter(detailVPAdapter);
                        detailVPAdapter.notifyDataSetChanged();

                        initDots(calendarListModel.getCalendarModels());
                    }
                    break;
            }
        }
    };

    private void initView() {
        findViewById(R.id.main_left).setOnClickListener(this);
        findViewById(R.id.main_right).setOnClickListener(this);



        //        headView = (View) findViewById(R.id.head_view);
        //        part1 = (View) findViewById(R.id.part1);
        //        part2 = (View) findViewById(R.id.part2);
                int headerHeight = findViewById(R.id.main_headview).getHeight();
        // 用户绘制区域
                Rect outRect = new Rect();
                getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
                int screenWidth = outRect.width();
                int screenHeight = outRect.height();
        //         LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout
        //                 .LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //        part1.setLayoutParams(params);
        //        part2.setLayoutParams(params);
        headView = LayoutInflater.from(this).inflate(R.layout.view_main_head, null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(screenWidth,
                screenHeight-headerHeight);
        headView.setLayoutParams(params);
        listView = (ListView) findViewById(R.id.my_listview);
        listView.addHeaderView(headView);


        ((TextView) headView.findViewById(R.id.main_date)).setText(Tools.format(System.currentTimeMillis(), "dd"));
        ((TextView) headView.findViewById(R.id.main_month)).setText(Tools.getEnDate());
        ((TextView) headView.findViewById(R.id.main_week)).setText(Tools.getChinaDate());
        mainCityListScrollView = (MainCityListScrollView) headView.findViewById(R.id.main_city_listview);
        weatherImg = (ImageView) headView.findViewById(R.id.main_weather_img);
        weatherTxt = (TextView) headView.findViewById(R.id.main_weather_txt);
        coverPager = (ViewPager) findViewById(R.id.main_viewpager_cover);
        coverPager.setOffscreenPageLimit(5);
        dotLayout = (LinearLayout) headView.findViewById(R.id.main_dot_layout);
        actionButton = (TextView) headView.findViewById(R.id.main_action);
        actionButton.setOnClickListener(this);

        detailPager = (ChildHeightViewpager) headView.findViewById(R.id.main_viewpager_detail);
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
