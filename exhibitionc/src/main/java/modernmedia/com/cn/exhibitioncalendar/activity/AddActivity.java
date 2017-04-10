package modernmedia.com.cn.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.corelib.widget.EvaSwitchBar;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.api.ApiController;
import modernmedia.com.cn.exhibitioncalendar.api.HandleFavApi;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel.CalendarModel;


/**
 * 展览添加页
 * Created by Eva. on 17/4/5.
 */

public class AddActivity extends BaseActivity {
    private TextView title, date, time;
    private EvaSwitchBar notification, tongbu;
    private CalendarModel calendarModel;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        calendarModel = (CalendarModel) getIntent().getSerializableExtra("add_detail");
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.add_title);
        date = (TextView) findViewById(R.id.add_title);
        time = (TextView) findViewById(R.id.add_title);
        notification = (EvaSwitchBar) findViewById(R.id.notification_switch);
        tongbu = (EvaSwitchBar) findViewById(R.id.tong_switch);
        viewPager = (ViewPager) findViewById(R.id.add_viewpager);

        title.setText(calendarModel.getTitle());
        date.setText(Tools.format(Long.valueOf(calendarModel.getStartTime()) * 1000, "yyyy-MM-dd"));
        time.setText(Tools.format(Long.valueOf(calendarModel.getStartTime()) * 1000, "HH:mm"));
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
        }
    }
}
