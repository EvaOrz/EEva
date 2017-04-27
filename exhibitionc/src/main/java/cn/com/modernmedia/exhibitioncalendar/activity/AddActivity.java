package cn.com.modernmedia.exhibitioncalendar.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.widget.EvaSwitchBar;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.api.HandleFavApi;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;
import cn.com.modernmedia.exhibitioncalendar.view.AddPopView;


/**
 * 展览添加页
 * Created by Eva. on 17/4/5.
 */

public class AddActivity extends BaseActivity implements EvaSwitchBar.OnChangeListener {
    private TextView title, date, time;
    private EvaSwitchBar notification, tongbu;
    private CalendarModel calendarModel;
    private ViewPager viewPager;
    private AddAdapter adapter;
    private List<String> pics = new ArrayList<>();
    private ApiController apiController;

    private int type = 0;// 0:添加；1：编辑
    private boolean tongbuStatus = true, alarmStatus = false;


    private static String CALANDER_URL = "content://com.android.calendar/calendars";
    private static String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        apiController = ApiController.getInstance(this);
        calendarModel = (CalendarModel) getIntent().getSerializableExtra("add_detail");
        type = getIntent().getIntExtra("add_type", 0);
        if (calendarModel != null) {
            initData();
        } else {
            showToast("获取展览信息失败");
        }
        askPermission(Manifest.permission.READ_CALENDAR, 101);
        askPermission(Manifest.permission.WRITE_CALENDAR, 102);

    }

    private void initData() {
        title.setText(calendarModel.getTitle());
        date.setText(Tools.format(Long.valueOf(calendarModel.getStartTime()) * 1000, "yyyy-MM-dd"));
        time.setText(Tools.format(Long.valueOf(calendarModel.getStartTime()) * 1000, "HH:mm"));
        parsePic(calendarModel.getImg());
        adapter = new AddAdapter(this, pics);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        findViewById(R.id.add_back).setOnClickListener(this);
        findViewById(R.id.add_ok).setOnClickListener(this);
        title = (TextView) findViewById(R.id.add_title);
        date = (TextView) findViewById(R.id.add_date);
        time = (TextView) findViewById(R.id.add_time);
        date.setOnClickListener(this);
        time.setOnClickListener(this);
        notification = (EvaSwitchBar) findViewById(R.id.notification_switch);
        notification.setOnChangeListener(this);
        tongbu = (EvaSwitchBar) findViewById(R.id.tong_switch);
        tongbu.setOnChangeListener(this);
        viewPager = (ViewPager) findViewById(R.id.add_viewpager);

        int width = MyApplication.width - 20;
        int height = width * 9 / 16;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(0, 30, 0, 0);
        viewPager.setLayoutParams(params);
        viewPager.setOffscreenPageLimit(3);
        notification.setChecked(false);
        tongbu.setChecked(true);
    }


    @Override
    public void onChange(EvaSwitchBar sb, boolean state) {

        switch (sb.getId()) {
            case R.id.notification_switch:
                if (state) alarmStatus = true;
                else alarmStatus = false;
                break;
            case R.id.tong_switch:
                if (state) tongbuStatus = true;
                else tongbuStatus = false;
                break;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_back:
                finish();
                break;
            case R.id.add_ok:
                showLoadingDialog(true);
                String img = pics.get(viewPager.getCurrentItem());
                String tt = date.getText() + " " + time.getText();
                if (type == 1) {

                    apiController.handleFav(AddActivity.this, HandleFavApi.HANDLE_EDIT, calendarModel.getEventId(), img, tt, new FetchEntryListener() {
                        @Override
                        public void setData(Entry entry) {
                            showLoadingDialog(false);
                            if (entry != null && entry instanceof ErrorMsg) {
                                ErrorMsg errorMsg = (ErrorMsg) entry;
                                showToast(errorMsg.getDesc());

                            } else {
                                getCurrentModel();
                                handler.sendEmptyMessage(1);
                                showToast(R.string.edit_success);
                            }
                        }
                    });
                } else {
                    apiController.handleFav(AddActivity.this, HandleFavApi.HANDLE_ADD, calendarModel.getItemId(), img, tt, new FetchEntryListener() {
                        @Override
                        public void setData(Entry entry) {
                            showLoadingDialog(false);
                            if (entry != null && entry instanceof ErrorMsg) {
                                ErrorMsg errorMsg = (ErrorMsg) entry;
                                showToast(errorMsg.getDesc());

                            } else {
                                getCurrentModel();
                                handler.sendEmptyMessage(1);
                                showToast(R.string.add_success);
                            }

                        }
                    });
                }
                break;
            case R.id.add_time:

                AddPopView timePop = new AddPopView(AddActivity.this, 1, calendarModel.getStartTime());
                break;
            case R.id.add_date:

                AddPopView datePop = new AddPopView(AddActivity.this, 2, calendarModel.getStartTime());
                break;

        }
    }

    private void getCurrentModel() {
        for (CalendarModel c : AppValue.myList.getCalendarModels()) {
            if (c.getItemId().equals(calendarModel.getItemId())) {
                calendarModel = c;
            }
        }
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String sta = calendarModel.getStartTime();
            String end = calendarModel.getEndTime();
            Long ss = 0L, ee = 0L;
            if (!TextUtils.isEmpty(sta)) ss = Long.valueOf(sta);
            if (!TextUtils.isEmpty(end)) ee = Long.valueOf(end);
            addCalendarEvent(AddActivity.this, calendarModel.getTitle(), calendarModel.getContent(), ss, ee);
        }
    };

    private void parsePic(String img) {
        String arr[] = img.split("###");
        for (int i = 0; i < arr.length; i++) {
            pics.add(arr[i]);
        }

    }

    public class AddAdapter extends PagerAdapter {
        protected List<String> list = new ArrayList<String>();
        protected Context mContext;

        public AddAdapter(Context context, List<String> list) {
            this.mContext = context;
            this.list = list;

        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView view = new ImageView(mContext);
            MyApplication.finalBitmap.display(view, list.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
        }

    }


    public void changePop(int type, int year, int month, int day, int hour, int minute) {

        if (type == 1) {
            String h = hour + "", m = minute + "";
            if (hour < 10) h = "0" + h;
            if (minute < 10) m = "0" + m;

            time.setText(h + ":" + m);
        } else if (type == 2) {
            date.setText(year + "-" + month + "-" + day);
        }


    }

    /**
     * 检查是否有日历账户
     *
     * @param context
     * @return
     */
    private int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALANDER_URL), null, null, null, null);
        try {
            if (userCursor == null)//查询返回空值
                return -1;
            int count = userCursor.getCount();
            if (count > 0) {//存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }


    private static String CALENDARS_NAME = "test";
    private static String CALENDARS_ACCOUNT_NAME = "test@gmail.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.exchange";
    private static String CALENDARS_DISPLAY_NAME = "测试账户";

    /**
     * 添加日历账户
     *
     * @param context
     * @return
     */
    private long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALANDER_URL);
        calendarUri = calendarUri.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true").appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME).appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE).build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    //检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
    private int checkAndAddCalendarAccount(Context context) {
        int oldId = checkCalendarAccount(context);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    /**
     * 添加系统日历事件
     *
     * @param context
     * @param title
     * @param description
     * @param beginTime
     * @param endTime
     */

    public void addCalendarEvent(Context context, String title, String description, long beginTime, long endTime) {
        // 获取日历账户的id
        int calId = checkAndAddCalendarAccount(context);
        Log.e("日历账户id：", calId + "");
        if (calId < 0) {


            // 获取账户id失败直接返回，添加日历事件失败
            return;
        }

        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", description);
        // 插入账户的id
        event.put("calendar_id", calId);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(beginTime);//设置开始时间
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(endTime);//设置终止时间
        long end = mCalendar.getTime().getTime();

        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Beijing");  //这个是时区，必须有，
        //添加事件
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALANDER_EVENT_URL), event);
        if (newEvent == null) {
            // 添加日历事件失败直接返回
            return;
        }
        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        // 提前一小时有提醒
        values.put(CalendarContract.Reminders.MINUTES, 60);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALANDER_REMIDER_URL), values);
        if (uri == null) {
            // 添加闹钟提醒失败直接返回
            return;
        }
    }
}

