package modernmedia.com.cn.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.exhibitioncalendar.R;

/**
 * Created by Eva. on 17/4/4.
 * <p>
 * 我的展览 页面
 */

public class MyListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
    }
}
