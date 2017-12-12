package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.util.ParseUtil;
import cn.com.modernmedia.corelib.widget.NoScrollGridView;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.adapter.CityGridAdapter;
import cn.com.modernmedia.exhibitioncalendar.adapter.DragCityAdapter;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel;
import cn.com.modernmedia.exhibitioncalendar.model.TagListModel.TagInfo;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;
import cn.com.modernmedia.exhibitioncalendar.view.DragGridView;

/**
 * 添加收藏城市页面
 * Created by Eva. on 2017/6/28.
 */

public class CityPickActivity extends BaseActivity {
    private List<TagInfo> areas = new ArrayList<>();
    private List<TagInfo> citys = new ArrayList<>();
    private List<TagInfo> users = new ArrayList<>();

    private DragGridView myView;
    private DragCityAdapter myCityAdapter;
    private TextView editButton;
    private LinearLayout conTain;

    public boolean ischeck = false;// 删除状态

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citylist);
        initView();
        initData();
    }

    /**
     * 如果已登录，则排序状态 未登录，则登陆
     */
    public void isLogined() {
        if (DataHelper.getUserLoginInfo(this) == null) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else {
            if (ischeck) {// 提交订阅
                book();
            } else {
                editButton.setText(R.string.complete);
                for (int i = 0; i < users.size(); i++) {
                    users.get(i).setCheck(true);
                }
                myCityAdapter.notifyDataSetChanged();
            }
            ischeck = !ischeck;
        }
    }

    private void book() {
        String sss = "";
        for (TagInfo s : users) {
            sss += s.getTagId() + ",";
        }
        Log.e("保存收藏", sss);
        showLoadingDialog(true);
        ApiController.getInstance(this).saveMyCitys(this, sss, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                showLoadingDialog(false);
                if (entry != null && entry instanceof ErrorMsg) {
                    if (((ErrorMsg) entry).getNo() == 200) {// 成功
                        for (int i = 0; i < users.size(); i++) {
                            users.get(i).setCheck(false);
                        }
                        for (int i = 0; i < citys.size(); i++) {
                            citys.get(i).setCheck(false);
                        }
                        ischeck = false;
                        handler.sendEmptyMessage(0);
                        handler.sendEmptyMessage(1);
                        AppValue.allCitys.setUsers(users);
                    } else {
                        showToast(((ErrorMsg) entry).getDesc());
                    }


                }
            }
        });
    }


    private void initView() {
        findViewById(R.id.citylist_back).setOnClickListener(this);

        myView = (DragGridView) findViewById(R.id.my_lists);
        myView.setNumColumns(5);
        myCityAdapter = new DragCityAdapter(CityPickActivity.this, users);
        myView.setAdapter(myCityAdapter);
        myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ischeck) {
                    TagInfo tagInfo = users.get(position);
                    Intent i = new Intent(CityPickActivity.this, CalendarListActivity.class);
                    i.putExtra("list_tagid", tagInfo.getTagId());
                    i.putExtra("list_tagname", tagInfo.getTagName());
                    startActivity(i);
                }
            }
        });
        conTain = (LinearLayout) findViewById(R.id.city_container);
        editButton = (TextView) findViewById(R.id.city_action);
        editButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.citylist_back:
                finish();
                break;
            case R.id.city_action:
                isLogined();
                break;
        }
    }

    /***
     * 生成子GridView
     *
     * @param ll
     */
    private void getItem(final List<TagInfo> ll, String title) {
        LinearLayout con = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_city_gridview, null);
        if (!ParseUtil.listNotNull(ll)) return;

        NoScrollGridView grid = (NoScrollGridView) con.findViewById(R.id.my_lists);
        TextView tt = (TextView) con.findViewById(R.id.zhou_name);
        tt.setText(title);
        final CityGridAdapter adapter = new CityGridAdapter(this, ll);
        grid.setAdapter(adapter);
        grid.setNumColumns(5);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                TagInfo tagInfo = ll.get(position);
                if (ischeck) {
                    tagInfo.setCheck(true);
                    users.add(tagInfo);
                    removeItem(tagInfo, citys);
                    handler.sendEmptyMessage(0);
                    handler.sendEmptyMessage(1);
                } else {// 非编辑状态，点击跳转栏目详情页面

                    Intent i = new Intent(CityPickActivity.this, CalendarListActivity.class);
                    i.putExtra("list_tagid", tagInfo.getTagId());
                    i.putExtra("list_tagname", tagInfo.getTagName());
                    startActivity(i);

                }
            }
        });
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ischeck) {// 编辑状态待选区长按先判断登陆
                    isLogined();
                }
                return false;
            }
        });
        adapter.notifyDataSetChanged();
        conTain.addView(con);
    }

    private void removeItem(TagInfo t, List<TagInfo> ll) {

        Iterator<TagInfo> iterator = ll.iterator();
        while (iterator.hasNext()) {
            TagInfo f = iterator.next();
            if (f.getTagId().equals(t.getTagId())) {
                iterator.remove();
            }
        }
    }

    /**
     * 删除某条订阅
     *
     * @param position
     */
    public void deleteBook(int position) {
        if (ischeck) {// 编辑状态，可删除排序
            TagInfo t = users.get(position);
            t.setCheck(false);
            removeItem(t, users);
            citys.add(t);
            handler.sendEmptyMessage(0);
            handler.sendEmptyMessage(1);
        }
    }


    private void initData() {
        ApiController.getInstance(this).getCitys(this, -1, new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof TagListModel) {
                    TagListModel tagListModel = (TagListModel) entry;
                    users.clear();
                    users.addAll(tagListModel.getUsers());
                    areas.clear();
                    areas.addAll(tagListModel.getAreas());
                    citys.clear();
                    citys.addAll(tagListModel.getCitys());

                    handler.sendEmptyMessage(0);
                    handler.sendEmptyMessage(1);
                }
            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    myCityAdapter.notifyDataSetChanged();

                    break;
                case 1:
                    conTain.removeAllViews();
                    for (TagInfo uu : users) {
                        for (int i = 0; i < citys.size(); i++) {
                            if (uu.getTagId().equals(citys.get(i).getTagId())) {
                                citys.remove(i);
                            }
                        }
                    }

                    for (TagInfo tt : areas) {
                        List<TagInfo> llls = new ArrayList<>();
                        for (TagInfo city1 : citys) {
                            if (city1.getScopeTagId().equals(tt.getTagId())) {
                                llls.add(city1);
                            }

                        }
                        getItem(llls, tt.getTagName() + " " + tt.getTagNameEn());
                    }

                    break;
            }
        }
    };


}
