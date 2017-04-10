package modernmedia.com.cn.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.db.DataHelper;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.corelib.model.UserModel;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.corelib.widget.RoundImageView;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.adapter.ExhibitionAdapter;
import modernmedia.com.cn.exhibitioncalendar.api.ApiController;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel.CalendarModel;

/**
 * Created by Eva. on 17/4/4.
 * <p>
 * 我的展览 页面
 */

public class MyListActivity extends BaseActivity {
    private RoundImageView avatar;
    private TextView nickname, clickAdd;
    private UserModel userModel;
    private ListView listView;
    private ImageView cover;
    private ExhibitionAdapter exhibitionAdapter;
    private List<CalendarModel> calendarModels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userModel != null) {
            Tools.setAvatar(this, userModel.getAvatar(), avatar);
            nickname.setText(userModel.getNickName());
//            MyApplication.finalBitmap.display(cover,userModel.get);

            initData(1);
        }
    }

    private void initView() {
        userModel = DataHelper.getUserLoginInfo(this);
        findViewById(R.id.my_back).setOnClickListener(this);
        avatar = (RoundImageView) findViewById(R.id.my_avatar);
        avatar.setOnClickListener(this);
        nickname = (TextView) findViewById(R.id.my_nickname);
        clickAdd = (TextView) findViewById(R.id.l_click_add);
        findViewById(R.id.list_ing).setOnClickListener(this);
        findViewById(R.id.list_ed).setOnClickListener(this);
        listView = (ListView) findViewById(R.id.my_listview);
        exhibitionAdapter = new ExhibitionAdapter(this);
        listView.setAdapter(exhibitionAdapter);
        clickAdd.setOnClickListener(this);
        cover = (ImageView) findViewById(R.id.l_cover);

    }

    private void initData(int type) {
        ApiController.getInstance(this).getMyList(this, "1", type, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    CalendarListModel c = (CalendarListModel) entry;
                    calendarModels.clear();
                    calendarModels.addAll(c.getCalendarModels());
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            exhibitionAdapter.setData(calendarModels);
            exhibitionAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_back:
                finish();
                break;

            case R.id.my_avatar:
                if (userModel == null) {
                    startActivity(new Intent(MyListActivity.this, LoginActivity.class));
                } else startActivity(new Intent(MyListActivity.this, UserCenterActivity.class));
                break;

            case R.id.l_click_add:
                if (userModel == null) {
                    startActivity(new Intent(MyListActivity.this, LoginActivity.class));
                }
                //                else startActivity(new Intent(MyListActivity.this, UserCenterActivity.class));
                break;
        }
    }
}
