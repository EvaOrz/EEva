package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.widget.EvaSwitchBar;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.api.HandleFavApi;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.view.AddPopView;


/**
 * 展览添加页
 * Created by Eva. on 17/4/5.
 */

public class AddActivity extends BaseActivity {
    private TextView title, date, time;
    private EvaSwitchBar notification, tongbu;
    private CalendarModel calendarModel;
    private ViewPager viewPager;
    private AddAdapter adapter;
    private List<String> pics = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        calendarModel = (CalendarModel) getIntent().getSerializableExtra("add_detail");
        if (calendarModel != null) {
            initData();
        } else {
            showToast("获取展览信息失败");
        }


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
        tongbu = (EvaSwitchBar) findViewById(R.id.tong_switch);
        viewPager = (ViewPager) findViewById(R.id.add_viewpager);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        notification.setChecked(true);
        tongbu.setChecked(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_back:
                finish();
                break;
            case R.id.add_ok:
                ApiController.getInstance(AddActivity.this).handleFav(AddActivity.this, HandleFavApi.HANDLE_ADD, calendarModel.getItemId(), "img", "", new FetchEntryListener() {
                    @Override
                    public void setData(Entry entry) {

                    }
                });
                break;
            case R.id.add_time:

                AddPopView timePop = new AddPopView(AddActivity.this, 1);
                break;
            case R.id.add_date:

                AddPopView datePop = new AddPopView(AddActivity.this, 2);
                break;

        }
    }


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


    public void changePop(int year,int month,int day,int hour,int minute){

    }
}

