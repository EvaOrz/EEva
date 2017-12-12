package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.model.VipInfoModel;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.widget.RoundImageView;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.adapter.ActiveAdapter;
import cn.com.modernmedia.exhibitioncalendar.adapter.ExhibitionAdapter;
import cn.com.modernmedia.exhibitioncalendar.adapter.FavMuseumAdapter;
import cn.com.modernmedia.exhibitioncalendar.adapter.MyCityAdapter;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.ActiveListModel;
import cn.com.modernmedia.exhibitioncalendar.model.ActiveListModel.ActiveModel;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel.CalendarModel;
import cn.com.modernmedia.exhibitioncalendar.model.MuseumListModel;
import cn.com.modernmedia.exhibitioncalendar.model.RecommandModel;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel.TagInfo;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;
import cn.com.modernmedia.exhibitioncalendar.view.NoScrollListView;

/**
 * Created by Eva. on 17/4/4.
 * <p>
 * 我的展览 页面
 */

public class UserCenterActivity extends BaseActivity {
    private RoundImageView avatar;
    private TextView nickname, goVip, fav1Num, fav2Num;
    private UserModel userModel;
    private LinearLayout containView;
    private ImageView cover, favCover11, favCover22;
    private ApiController apiController;

    // 底部导航
    private RadioButton radioButton_xing;// 行程
    private RadioButton radioButton_acti;// 活动
    private RadioButton radioButton_fav;// 收藏
    private RadioButton radioButton_city;// 城市

    private ListView xingListView;
    private ExhibitionAdapter exhibitionAdapter;
    private ListView actiListView;
    private ActiveAdapter activeAdapter;

    private View favView;
    private RelativeLayout favCover1, favCover2;
    private NoScrollListView favList1, favList2;
    private ExhibitionAdapter fav1Adapter;
    private ListView cityView;
    private MyCityAdapter myCityAdapter;
    private FavMuseumAdapter fav2Adapter;

    private List<CalendarModel> xingData = new ArrayList<>();
    private List<CalendarModel> fav1Data = new ArrayList<>();
    private List<MuseumListModel.MuseumModel> fav2Data = new ArrayList<>();
    private List<ActiveModel> activeData = new ArrayList<>();
    private List<TagListModel.TagInfo> cityData = new ArrayList<>();

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:// 更新行程状态
                    exhibitionAdapter.notifyDataSetChanged();
                    break;
                case 2:// 更新活动状态
                    activeAdapter.notifyDataSetChanged();
                    break;
                case 3:// 更新收藏状态
                    fav1Num.setText("已收藏 " + fav1Data.size());
                    fav2Num.setText("已收藏 " + fav2Data.size());
                    fav1Adapter.notifyDataSetChanged();
                    fav2Adapter.notifyDataSetChanged();
                    break;
                case 5:// 更新城市状态
                    myCityAdapter.notifyDataSetChanged();
                    break;
                case 4:// 更新用户状态
                    if (userModel == null) {
                        avatar.setImageResource(R.mipmap.avatar_bg);
                        nickname.setText(R.string.no_login);
                    } else {
                        Tools.setAvatar(mContext, DataHelper.getAvatarUrl(mContext, userModel.getUserName()), avatar);
                        nickname.setText(userModel.getNickName());

                        VipInfoModel vipInfoModel = DataHelper.getVipInfo(UserCenterActivity.this);
                        if (vipInfoModel == null || vipInfoModel.getLevel() == 0) {
                            goVip.setText(R.string.my_vip_nolevel);
                        } else {
                            goVip.setText(R.string.my_vip_level);
                        }
                    }


                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        mContext = this;
        apiController = ApiController.getInstance(this);
        userModel = DataHelper.getUserLoginInfo(mContext);
        initView();
        initData(false);
        getFavData();
        getActiveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userModel = DataHelper.getUserLoginInfo(mContext);
        handler.sendEmptyMessage(4);
        // 登录状态改变
        if (CommonApplication.loginStatusChange == 2) {
            initData(true);

        }
    }

    private void initView() {
        findViewById(R.id.my_back).setOnClickListener(this);
        avatar = (RoundImageView) findViewById(R.id.my_avatar);
        avatar.setOnClickListener(this);
        goVip = (TextView) findViewById(R.id.my_vip_level);
        goVip.setOnClickListener(this);
        nickname = (TextView) findViewById(R.id.my_nickname);
        containView = (LinearLayout) findViewById(R.id.usercenter_contain);
        View headerView = findViewById(R.id.mylist_headview);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        headerView.measure(w, h);
        cover = (ImageView) findViewById(R.id.l_cover);
        cover.setLayoutParams(new RelativeLayout.LayoutParams(MyApplication.width, headerView.getMeasuredHeight()));
        cover.setImageResource(R.mipmap.my_bg);

        xingListView = new ListView(this);
        xingListView.setDivider(null);
        exhibitionAdapter = new ExhibitionAdapter(mContext, xingData);
        xingListView.setAdapter(exhibitionAdapter);
        actiListView = new ListView(this);
        actiListView.setDivider(null);
        activeAdapter = new ActiveAdapter(mContext, activeData);
        actiListView.setAdapter(activeAdapter);
        cityView = new ListView(this);
        cityView.setDivider(null);
        myCityAdapter = new MyCityAdapter(this, cityData);
        cityView.setAdapter(myCityAdapter);
        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TagInfo tagInfo = cityData.get(position);
                Intent i = new Intent(mContext, CalendarListActivity.class);
                i.putExtra("list_tagid", tagInfo.getTagId());
                i.putExtra("list_tagname", tagInfo.getTagName());
                startActivity(i);
            }
        });
        favView = LayoutInflater.from(this).inflate(R.layout.view_my_fav, null);
        fav1Num = (TextView) favView.findViewById(R.id.fav_1_num);
        fav2Num = (TextView) favView.findViewById(R.id.fav_2_num);
        favCover11 = (ImageView) favView.findViewById(R.id.zhanlan_cover1);
        favCover22 = (ImageView) favView.findViewById(R.id.zhanguan_cover1);
        favCover11.setOnClickListener(this);
        favCover22.setOnClickListener(this);
        favCover1 = (RelativeLayout) favView.findViewById(R.id.zhanlan_cover);
        favCover1.setOnClickListener(this);
        favCover2 = (RelativeLayout) favView.findViewById(R.id.zhanguan_cover);
        favCover2.setOnClickListener(this);
        int width = MyApplication.width;
        int height = (width - 40) * 9 / 16 + 40;
        int height1 = (width - 40) * 112 / 718;
        favCover1.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        favCover2.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(width - 40, height1);
        ll.setMargins(20, 0, 20, 0);
        favCover11.setLayoutParams(ll);
        favCover22.setLayoutParams(ll);
        favList1 = (NoScrollListView) favView.findViewById(R.id.zhanlan_list);
        favList2 = (NoScrollListView) favView.findViewById(R.id.zhanguan_list);
        fav1Adapter = new ExhibitionAdapter(this, fav1Data);
        favList1.setAdapter(fav1Adapter);
        fav2Adapter = new FavMuseumAdapter(this, fav2Data);
        favList2.setAdapter(fav2Adapter);
        radioButton_xing = (RadioButton) findViewById(R.id.my_xingcheng);
        radioButton_acti = (RadioButton) findViewById(R.id.my_active);
        radioButton_fav = (RadioButton) findViewById(R.id.my_fav);
        radioButton_city = (RadioButton) findViewById(R.id.my_citys);
        radioButton_xing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeTab(0);
                }
            }
        });
        radioButton_acti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeTab(1);
                }
            }
        });
        radioButton_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeTab(2);
                }
            }
        });
        radioButton_city.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeTab(3);
                }

            }
        });
        changeTab(0);
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
        xingData.clear();
        activeData.clear();
        cityData.clear();
        fav1Data.clear();
        fav2Data.clear();
        if (isLoginStatusChange) {
            getXingchengData();
            getFavData();
            getActiveData();
        } else {
            xingData.addAll(AppValue.myList.getCalendarModels());
            cityData.addAll(AppValue.allCitys.getUsers());
            handler.sendEmptyMessage(1);
            handler.sendEmptyMessage(5);
        }
    }

    private void getFavData() {
        if (userModel == null) return;
        apiController.getLikeList(this, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof RecommandModel) {
                    RecommandModel r = (RecommandModel) entry;
                    fav1Data.addAll(r.getCalendarModels());
                    Collections.sort(fav1Data);
                    fav2Data.addAll(r.getMuseumModels());
                    Collections.sort(fav2Data);
                    handler.sendEmptyMessage(3);
                }
            }
        });
    }


    private void getActiveData() {
        if (userModel == null) return;
        apiController.getActives(this, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof ActiveListModel) {
                    ActiveListModel a = (ActiveListModel) entry;
                    activeData.addAll(a.getActiveModels());
                    handler.sendEmptyMessage(2);
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
                    startActivity(new Intent(UserCenterActivity.this, LoginActivity.class));
                } else startActivity(new Intent(UserCenterActivity.this, UserInfoActivity.class));
                break;

            case R.id.my_vip_level:
                if (userModel == null) {
                    startActivity(new Intent(UserCenterActivity.this, LoginActivity.class));
                } else startActivity(new Intent(UserCenterActivity.this, MyVipActivity.class));
                break;
            case R.id.zhanlan_cover:
                if (fav1Data.size() > 0) {
                    favList1.setVisibility(View.VISIBLE);
                    favCover11.setVisibility(View.VISIBLE);
                    favCover1.setVisibility(View.GONE);
                }
                break;
            case R.id.zhanguan_cover:
                if (fav2Data.size() > 0) {
                    favList2.setVisibility(View.VISIBLE);
                    favCover22.setVisibility(View.VISIBLE);
                    favCover2.setVisibility(View.GONE);
                }
                break;
            case R.id.zhanlan_cover1:
                favCover1.setVisibility(View.VISIBLE);
                favList1.setVisibility(View.GONE);
                favCover11.setVisibility(View.GONE);

                break;
            case R.id.zhanguan_cover1:
                favCover2.setVisibility(View.VISIBLE);
                favList2.setVisibility(View.GONE);
                favCover22.setVisibility(View.GONE);
                break;
        }
    }

    private void getXingchengData() {
        if (userModel == null) {
            apiController.getEventList(this, "1", new FetchEntryListener() {
                @Override
                public void setData(Entry entry) {
                    if (entry != null && entry instanceof CalendarListModel) {

                        handler.sendEmptyMessage(1);
                    }
                }
            });
        }

    }

    public void changeTab(int position) {
        containView.removeAllViews();
        switch (position) {
            case 0:
                containView.addView(xingListView);
                radioButton_xing.setChecked(true);
                radioButton_acti.setChecked(false);
                radioButton_fav.setChecked(false);
                radioButton_city.setChecked(false);
                break;
            case 1:
                containView.addView(actiListView);
                radioButton_xing.setChecked(false);
                radioButton_acti.setChecked(true);
                radioButton_fav.setChecked(false);
                radioButton_city.setChecked(false);
                break;
            case 2:
                containView.addView(favView);
                radioButton_xing.setChecked(false);
                radioButton_acti.setChecked(false);
                radioButton_fav.setChecked(true);
                radioButton_city.setChecked(false);
                break;
            case 3:
                containView.addView(cityView);
                radioButton_xing.setChecked(false);
                radioButton_acti.setChecked(false);
                radioButton_fav.setChecked(false);
                radioButton_city.setChecked(true);
                break;

        }
    }
}
