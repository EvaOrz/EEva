package modernmedia.com.cn.exhibitioncalendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.adapter.SearchAdapter;
import modernmedia.com.cn.exhibitioncalendar.api.ApiController;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel;
import modernmedia.com.cn.exhibitioncalendar.model.CalendarListModel.CalendarModel;


/**
 * Created by Eva. on 17/4/4.
 */

public class SearchActivity extends BaseActivity {
    private EditText sEdit;
    private ListView listView;
    private List<CalendarModel> calendarModels = new ArrayList<>();
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        findViewById(R.id.search_back).setOnClickListener(this);
        findViewById(R.id.search_tag).setOnClickListener(this);
        sEdit = (EditText) findViewById(R.id.search_edit);
        listView = (ListView) findViewById(R.id.search_listview);
        sEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                doSearch();
                return false;
            }
        });
        searchAdapter = new SearchAdapter(this);
        listView.setAdapter(searchAdapter);
    }

    private void doSearch() {
        String key = sEdit.getText().toString();
        if (!TextUtils.isEmpty(key)) {
            ApiController.getInstance(SearchActivity.this).search(SearchActivity.this, key, new FetchEntryListener() {
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
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.search_tag:
                doSearch();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            searchAdapter.setData(calendarModels);
            searchAdapter.notifyDataSetChanged();
        }
    };
}
