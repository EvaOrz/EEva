package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
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
import cn.com.modernmedia.exhibitioncalendar.api.GetShareIdApi;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;
import cn.com.modernmedia.exhibitioncalendar.view.ShareDialog;

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
    private String shareId;
    private ImageView cover;
    //    private List<CalendarModel> ingdatas = new ArrayList<>();
    //    private List<CalendarModel> eddatas = new ArrayList<>();


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    List<CalendarListModel.CalendarModel> ingdatas = new ArrayList<>();
                    ingdatas.addAll(AppValue.myList.getCalendarModels());

                    ExhibitionAdapter ingAdapter = new ExhibitionAdapter(MyListActivity.this);
                    listView.setAdapter(ingAdapter);
                    ingAdapter.setData(ingdatas);
                    ingAdapter.notifyDataSetChanged();
                    if (ingdatas.size() > 0) clickAdd.setVisibility(View.GONE);
                    else clickAdd.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    List<CalendarListModel.CalendarModel> eddatas = new ArrayList<>();
                    eddatas.addAll(AppValue.edList.getCalendarModels());

                    ExhibitionAdapter edAdapter = new ExhibitionAdapter(MyListActivity.this);
                    listView.setAdapter(edAdapter);
                    edAdapter.setData(eddatas);
                    edAdapter.notifyDataSetChanged();
                    clickAdd.setVisibility(View.GONE);
                    break;
                case 3:// 分享个人行程
                    new ShareDialog(MyListActivity.this, userModel.getUserName() + "的观展行程", "", userModel.getAvatar(), UrlMaker.getShareWebUrl(shareId));
                    break;

            }


        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        initView();
        initData(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CommonApplication.loginStatusChange) {
            Log.e("My_list_login", "loginStatusChange");
            initData(true);
        }
    }

    private void initView() {

        findViewById(R.id.my_back).setOnClickListener(this);
        findViewById(R.id.my_share).setOnClickListener(this);
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

    /**
     * 初始化，没登录
     * <p>
     * 初始化，登录了
     * <p>
     * 登录状态变化，清空
     * 登录状态变化，取线上数据
     *
     * @param isLoginStatusChange
     */
    private void initData(boolean isLoginStatusChange) {
        userModel = DataHelper.getUserLoginInfo(this);
        if (userModel == null) {
            avatar.setImageResource(R.mipmap.avatar_bg);
            nickname.setText(R.string.no_login);
            edText.setBackgroundResource(R.drawable.gray_3radius_corner_bg);
            ingText.setBackgroundResource(R.drawable.red_3radius_corner_bg);
            handler.sendEmptyMessage(1);
            return;
        }
        Tools.setAvatar(this, DataHelper.getAvatarUrl(MyListActivity
                .this, userModel.getUserName()), avatar);
        nickname.setText(userModel.getNickName());
        if (isLoginStatusChange) {
            getIngData();
            getEdData();
        } else {//
            handler.sendEmptyMessage(1);
        }


    }

    private void getIngData() {
        showLoadingDialog(true);
        ApiController.getInstance(this).getMyList(this, "1", 1, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                showLoadingDialog(false);
                if (entry != null && entry instanceof CalendarListModel) {
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
            case R.id.my_share:
                if (userModel == null) {
                    startActivity(new Intent(MyListActivity.this, LoginActivity.class));
                } else {
                    ApiController.getInstance(MyListActivity.this).getShareId(MyListActivity
                            .this, userModel.getUserName() + "的观展行程", new FetchEntryListener() {
                        @Override
                        public void setData(Entry entry) {
                            if (entry != null && entry instanceof GetShareIdApi.ShareId) {
                                shareId = ((GetShareIdApi.ShareId) entry).getId();
                                handler.sendEmptyMessage(3);
                            }


                        }
                    });
                }

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
                edText.setBackgroundResource(R.drawable.gray_3radius_corner_bg);
                ingText.setBackgroundResource(R.drawable.red_3radius_corner_bg);
                if (userModel != null) {
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
