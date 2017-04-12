package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.widget.RoundImageView;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.adapter.ExhibitionAdapter;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;

/**
 * Created by Eva. on 17/4/4.
 * <p>
 * 我的展览 页面
 */

public class MyListActivity extends BaseActivity {
    private RoundImageView avatar;
    private TextView nickname, clickAdd, ingText, edText;
    private UserModel userModel;
    private ListView listView;
    private ImageView cover;
    //    private ExhibitionAdapter ingAdapter, edAdapter;
    private List<CalendarModel> ingdatas = new ArrayList<>();
    private List<CalendarModel> eddatas = new ArrayList<>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ExhibitionAdapter ingAdapter = new ExhibitionAdapter(MyListActivity.this);
                    listView.setAdapter(ingAdapter);
                    ingAdapter.setData(ingdatas);
                    ingAdapter.notifyDataSetChanged();
                    if (ingdatas.size() > 0) clickAdd.setVisibility(View.GONE);
                    else clickAdd.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    ExhibitionAdapter edAdapter = new ExhibitionAdapter(MyListActivity.this);
                    listView.setAdapter(edAdapter);
                    edAdapter.setData(eddatas);
                    edAdapter.notifyDataSetChanged();
                    clickAdd.setVisibility(View.GONE);
                    break;


            }


        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.loginStatusChange) {
            initData();
        }
    }

    private void initView() {

        findViewById(R.id.my_back).setOnClickListener(this);
        avatar = (RoundImageView) findViewById(R.id.my_avatar);
        avatar.setOnClickListener(this);
        nickname = (TextView) findViewById(R.id.my_nickname);
        clickAdd = (TextView) findViewById(R.id.l_click_add);
        ingText = (TextView) findViewById(R.id.list_ing);
        ingText.setOnClickListener(this);
        edText = (TextView) findViewById(R.id.list_ed);
        edText.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.my_listview);
        clickAdd.setOnClickListener(this);

        View headerView = findViewById(R.id.mylist_headview);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        headerView.measure(w, h);

        cover = (ImageView) findViewById(R.id.l_cover);
        cover.setLayoutParams(new RelativeLayout.LayoutParams(MyApplication.width, headerView.getMeasuredHeight()));
        cover.setImageResource(R.mipmap.my_bg);
    }

    private void initData() {
        userModel = DataHelper.getUserLoginInfo(this);
        if (userModel == null) return;
        Tools.setAvatar(this, userModel.getAvatar(), avatar);
        nickname.setText(userModel.getNickName());
        if (AppValue.myList == null) {
            getIngData();
        } else {
            ingdatas.addAll(AppValue.myList.getCalendarModels());
            handler.sendEmptyMessage(1);
        }

        if (AppValue.edList == null) {
            getEdData();
        } else {
            eddatas.addAll(AppValue.edList.getCalendarModels());
        }
    }

    private void getIngData() {
        ApiController.getInstance(this).getMyList(this, "1", 1, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    CalendarListModel c = (CalendarListModel) entry;
                    ingdatas.clear();
                    ingdatas.addAll(c.getCalendarModels());
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void getEdData() {
        ApiController.getInstance(this).getMyList(this, "1", 2, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof CalendarListModel) {
                    CalendarListModel c = (CalendarListModel) entry;
                    eddatas.clear();
                    eddatas.addAll(c.getCalendarModels());
                }
            }
        });
    }

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

            case R.id.list_ing:
                if (userModel != null) {
                    edText.setBackgroundResource(R.drawable.gray_3radius_corner_bg);
                    ingText.setBackgroundResource(R.drawable.red_3radius_corner_bg);
                    handler.sendEmptyMessage(1);
                }

                break;
            case R.id.list_ed:
                edText.setBackgroundResource(R.drawable.red_3radius_corner_bg);
                ingText.setBackgroundResource(R.drawable.gray_3radius_corner_bg);
                handler.sendEmptyMessage(2);
                break;
        }
    }
}
