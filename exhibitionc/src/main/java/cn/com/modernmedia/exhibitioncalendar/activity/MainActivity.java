package cn.com.modernmedia.exhibitioncalendar.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchDataListener;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.model.VipInfoModel;
import cn.com.modernmedia.corelib.model.WeatherModel;
import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.adapter.CoverVPAdapter;
import cn.com.modernmedia.exhibitioncalendar.adapter.DetailVPAdapter;
import cn.com.modernmedia.exhibitioncalendar.adapter.VerticleVPAdapter;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.api.user.HandleFavApi;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel;
import cn.com.modernmedia.exhibitioncalendar.push.NewPushManager;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;
import cn.com.modernmedia.exhibitioncalendar.util.UpdateManager;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;
import cn.com.modernmedia.exhibitioncalendar.view.ChildHeightViewpager;
import cn.com.modernmedia.exhibitioncalendar.view.ListItemMenuView;
import cn.com.modernmedia.exhibitioncalendar.view.MainCityListScrollView;
import cn.com.modernmedia.exhibitioncalendar.view.MainToumingView;
import cn.com.modernmedia.exhibitioncalendar.view.VerticalViewPager;
import cn.com.modernmedia.exhibitioncalendar.view.ViewHolder;

import static cn.com.modernmedia.exhibitioncalendar.util.AppValue.myList;


/**
 * Created by Eva. on 17/3/27.
 */

public class MainActivity extends BaseActivity {
    private ImageView weatherImg, avatar;
    private TextView weatherTxt, actionButton, myNum, goVip;
    public ApiController apiController;
    public WeatherModel weatherModel;
    private MainCityListScrollView mainCityListScrollView;
    private CalendarListModel calendarListModel;// 首页推荐数据
    private ViewPager coverPager, detailPager;
    private CoverVPAdapter coverVPAdapter;
    private DetailVPAdapter detailVPAdapter;

    private MainToumingView toumingView;

    private LinearLayout dotLayout, myListLayout;
    private List<ImageView> dots = new ArrayList<>();

    private List<CalendarModel> myCalendarList = new ArrayList<>();

    private VerticalViewPager verticalViewPager;// 两个page的viewpager
    private VerticleVPAdapter verticleVPAdapter;
    private View page1, page2;
    private UserModel userModel;
    private VipInfoModel vipInfoModel = new VipInfoModel();// 用户的vip信息

    private long lastClickTime = 0;// 上次点击返回按钮时间

    //    private int currentPosition = 0;// 当前滑动到的页面


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// 天气数据
                    if (!TextUtils.isEmpty(weatherModel.getIcon()))
                        MyApplication.finalBitmap.display(weatherImg, weatherModel.getIcon());
                    weatherTxt.setText(weatherModel.getDesc());
                    break;
                case 1:// 城市数据
                    if (AppValue.allCitys.getUsers().size() > 5)
                        mainCityListScrollView.setData(AppValue.allCitys.getUsers().subList(0, 5));
                    else mainCityListScrollView.setData(AppValue.allCitys.getUsers());
                    break;
                case 2:// 初始化首页推荐数据
                    if (calendarListModel != null) {
                        coverVPAdapter = new CoverVPAdapter(MainActivity.this, calendarListModel.getCalendarModels(), 0);
                        coverPager.setAdapter(coverVPAdapter);
                        coverVPAdapter.notifyDataSetChanged();

                        detailVPAdapter = new DetailVPAdapter(MainActivity.this, calendarListModel.getCalendarModels());
                        detailPager.setAdapter(detailVPAdapter);
                        detailVPAdapter.notifyDataSetChanged();

                        initDots(calendarListModel.getCalendarModels());
                        if (ParseUtil.listNotNull(calendarListModel.getCalendarModels()))
                            checkAdd(calendarListModel.getCalendarModels().get(0));
                    }
                    break;

                case 3:// 用户数据
                    myListLayout.removeAllViews();
                    myCalendarList.clear();
                    myCalendarList.addAll(AppValue.myList.getCalendarModels());
                    for (CalendarModel ca : myCalendarList) {
                        myListLayout.addView(getMylistItemView(ca));
                    }
                    myNum.setText(myCalendarList.size() + "个待参观展览");
                    if (calendarListModel != null && ParseUtil.listNotNull(calendarListModel.getCalendarModels()))
                        calendarListModel.getCalendarModels().get(coverPager.getCurrentItem()).getItemId();

                case 4:// 头像
                    if (userModel != null) {
                        Tools.setAvatar(MainActivity.this, DataHelper.getAvatarUrl(MainActivity
                                .this, userModel.getUserName()), avatar);
                    }else {

                    }
                    break;

                case 5:// 取消行程
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setTag("delete");
                    actionButton.setText(R.string.menu_cancle);
                    actionButton.setBackgroundResource(R.drawable.green_3radius_corner_bg);
                    break;
                case 6:// 添加行程
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setTag("add");
                    actionButton.setText(R.string.add_to_calendar);
                    actionButton.setBackgroundResource(R.drawable.red_3radius_corner_bg);
                    break;
                case 7://不显示
                    actionButton.setVisibility(View.GONE);

                    break;

                case 8:// 更新首页vip 信息
                    if (vipInfoModel == null || vipInfoModel.getLevel() == 0) {
                        goVip.setText(R.string.my_vip_nolevel);
                    } else {
                        goVip.setText(R.string.my_vip_level);
                    }
                    break;
            }
        }
    };

    /**
     * 初始化 mylist itemview
     *
     * @param item
     * @return
     */
    private View getMylistItemView(final CalendarModel item) {
        ViewHolder viewHolder = ViewHolder.get(MainActivity.this, null, R.layout.item_list);
        TextView title = viewHolder.getView(R.id.l_title);
        TextView city = viewHolder.getView(R.id.l_city);
        TextView date = viewHolder.getView(R.id.l_date);
        ImageView img = viewHolder.getView(R.id.l_img);

        int width = MyApplication.width - 20;
        int height = width * 9 / 16;
        img.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        title.setText(item.getTitle());
        // 显示用户自己设置的时间
        if (!TextUtils.isEmpty(item.getTime())) {
            date.setText(item.getTime());
        } else
            date.setText(Tools.getStringToDate(item.getStartTime()) + "-" + Tools.getStringToDate(item.getEndTime()));

        if (ParseUtil.listNotNull(item.getCitylist())) {
            city.setText(item.getCitylist().get(0).getTagName());
        }
        MyApplication.finalBitmap.display(img, item.getCoverImg());
        final ImageView sandian = viewHolder.getView(R.id.sandian);
        sandian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ListItemMenuView(MainActivity.this, item, sandian);
            }
        });
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CalendarDetailActivity.class);
                i.putExtra(UriParse.DETAILCALENDAR, item.getItemId());
                startActivity(i);
            }
        });
        return viewHolder.getConvertView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MainActivity.this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) ==
                    PackageManager.PERMISSION_GRANTED) {
                uploadDeviceInfoForPush();
            } else {
                askPermission(new String[]{ Manifest.permission.READ_PHONE_STATE, }, 108);
            }
        } else {
            uploadDeviceInfoForPush();
        }
        checkBbwc();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 108:
                // 101的第一个权限 是读定位
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                   uploadDeviceInfoForPush();
                }
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Push需要：上传device信息
     */
    private void uploadDeviceInfoForPush() {
        if (DataHelper.isPushServiceEnable(this)) NewPushManager.getInstance(this).register(this);
    }

    protected void onPause() {
        super.onPause();
        NewPushManager.getInstance(this).onpause(this);
    }


    /**
     * 初始化用户数据
     */
    private void initUserData() {
        userModel = DataHelper.getUserLoginInfo(MainActivity.this);
        handler.sendEmptyMessage(4);

        if (userModel != null) {
            // 正在进行
            apiController.getMyList(this, "1", 1, new FetchEntryListener() {
                @Override
                public void setData(Entry entry) {
                    if (entry != null && entry instanceof CalendarListModel) {

                        handler.sendEmptyMessage(3);
                    }
                }
            });
            // 已经过期
            apiController.getMyList(this, "1", 2, new FetchEntryListener() {
                @Override
                public void setData(Entry entry) {
                    if (entry != null && entry instanceof CalendarListModel) {
                    }
                }
            });
        }
    }

    private void initData() {
        apiController = ApiController.getInstance(this);

        apiController.getWeather(MainActivity.this, AppValue.currentLocation, new FetchEntryListener() {
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
                }
            }
        });

        apiController.getMuseumList(this, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {

            }
        });
        initUserData();
        getIssueLevel();

    }

    /**
     * 取用户的付费权限
     * <p>
     * 入版需要取一下权限
     */
    private void getIssueLevel() {
        if (userModel == null) return;
        apiController.getUserPermission(new FetchDataListener() {
            @Override
            public void fetchData(boolean isSuccess, String data, boolean fromHttp) {
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject != null) {
                            vipInfoModel = VipInfoModel.parseVipInfoModel(jsonObject);
                            DataHelper.saveVipInfo(MainActivity.this, vipInfoModel);
                            handler.sendEmptyMessage(8);
                        }
                    } catch (JSONException e) {

                    }
                }
            }
        });


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

        myListLayout = (LinearLayout) page2.findViewById(R.id.ceshi);
        page2.findViewById(R.id.main_add).setOnClickListener(this);
        page1.findViewById(R.id.main_godown).setOnClickListener(this);
        goVip = (TextView) page1.findViewById(R.id.my_vip_level);
        goVip.setOnClickListener(this);
        myNum = (TextView) page1.findViewById(R.id.main_calendar_num);
        mainCityListScrollView = (MainCityListScrollView) page1.findViewById(R.id.main_city_listview);
        weatherImg = (ImageView) page1.findViewById(R.id.main_weather_img);
        weatherTxt = (TextView) page1.findViewById(R.id.main_weather_txt);
        coverPager = (ViewPager) findViewById(R.id.main_viewpager_cover);
        coverPager.setOffscreenPageLimit(5);

        toumingView = (MainToumingView) page1.findViewById(R.id.touming);
        toumingView.setCallBack(new MainToumingView.MyLayoutCallBack() {
            @Override
            public void scrollByX(boolean ifLeft) {
                int currentPosition = coverPager.getCurrentItem();
                if (ifLeft) {

                    if (currentPosition > 0) {
                        coverPager.setCurrentItem(currentPosition - 1);
                    }
                } else {
                    if (currentPosition < 4) coverPager.setCurrentItem(currentPosition + 1);
                }
            }

            @Override
            public void scrollByY(int i) {

            }
        });

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
                checkAdd(calendarListModel.getCalendarModels().get(detailPager.getCurrentItem()));
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
                checkAdd(calendarListModel.getCalendarModels().get(coverPager.getCurrentItem()));
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
            case R.id.main_action:// 添加|取消
                if (userModel == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    if (calendarListModel != null) {
                        CalendarModel c = calendarListModel.getCalendarModels().get(detailPager.getCurrentItem());
                        if (view.getTag().toString().equals("delete")) {
                            doDelete(c);
                        } else {
                            doAdd(c);
                        }
                    }
                }
                break;

            case R.id.main_add:// 添加红按钮
                startActivity(new Intent(MainActivity.this, CalendarListActivity.class));
                break;
            case R.id.my_vip_level:
                startActivity(new Intent(MainActivity.this, MyVipActivity.class));

                break;
            case R.id.main_godown://
                verticalViewPager.setCurrentItem(1);
                break;
        }

    }

    /**
     * 添加行程
     */
    private void doAdd(CalendarModel c) {
        Intent i = new Intent(MainActivity.this, AddActivity.class);
        i.putExtra("add_type", 0);
        i.putExtra("add_detail", c);
        startActivity(i);


    }

    /**
     * 取消行程
     */
    private void doDelete(CalendarModel c) {
        for (CalendarModel s : myList.getCalendarModels()) {
            if (c.getItemId().equals(s.getItemId())) {
                apiController.handleFav(MainActivity.this, HandleFavApi.HANDLE_DELETE, s.getEventId(), s.getCoverImg(), s.getStartTime(), new FetchEntryListener() {
                    @Override
                    public void setData(Entry entry) {
                        if (entry != null && entry instanceof ErrorMsg) {
                            ErrorMsg errorMsg = (ErrorMsg) entry;
                            showToast(errorMsg.getDesc());

                        } else {
                            handler.sendEmptyMessage(3);
                            handler.sendEmptyMessage(6);// 显示添加
                        }
                    }
                });
                break;
            }
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
        if (CommonApplication.loginStatusChange == 2) {
            Log.e("登录状态变化", "重新请求我的展览列表");
            initUserData();
            vipInfoModel = DataHelper.getVipInfo(this);
            handler.sendEmptyMessage(8);
        } else if (CommonApplication.loginStatusChange == 1) {
            Log.e("我的展览状态变化", "刷新页面");
            // 更新我的展览列表
            handler.sendEmptyMessage(3);
        } else if (CommonApplication.loginStatusChange == 3) {
            Log.e("我的城市收藏状态变化", "刷新页面");
            handler.sendEmptyMessage(1);
        }

        CommonApplication.loginStatusChange = 0;

    }


    /**
     * 更新dot选中状态
     *
     * @param position
     */
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

    /**
     * 更新添加按钮状态
     */
    private void checkAdd(CalendarModel model) {
        List<CalendarModel> ll = myList.getCalendarModels();
        if (model.getType() == 3) {// gone
            handler.sendEmptyMessage(7);
        } else if (ParseUtil.listNotNull(ll)) {

            boolean ifShow = true;
            for (int i = 0; i < ll.size(); i++) {
                if (ll.get(i).getItemId().equals(model.getItemId())) {
                    ifShow = false;
                }
            }
            if (ifShow) {// visible
                handler.sendEmptyMessage(6);
            } else {
                handler.sendEmptyMessage(5);
            }
        } else// invisible

            handler.sendEmptyMessage(6);
    }

    /**
     * 更新版本
     */
    private void checkBbwc() {
        //googleplay 市场禁止检测版本更新
        if (CommonApplication.CHANNEL.equals("googleplay")) {
            return;
        }
        if (DataHelper.getRefuseNoticeVersion(this)){
            return;
        }

        UpdateManager manager = new UpdateManager(this, new UpdateManager.CheckVersionListener() {

            @Override
            public void checkEnd() {
            }
        });
        manager.checkVersion();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long clickTime = System.currentTimeMillis() / 1000;
            if (clickTime - lastClickTime >= 3) {
                lastClickTime = clickTime;
                showToast(R.string.exit_app);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
