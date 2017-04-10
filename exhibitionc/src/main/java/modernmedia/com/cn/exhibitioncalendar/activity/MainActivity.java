package modernmedia.com.cn.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.corelib.model.WeatherModel;
import modernmedia.com.cn.exhibitioncalendar.MyApplication;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.api.ApiController;


/**
 * Created by Eva. on 17/3/27.
 */

public class MainActivity extends BaseActivity {
    private ImageView weatherImg;
    private TextView weatherTxt;
    private ApiController apiController;
    private WeatherModel weatherModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        apiController = ApiController.getInstance(this);
        apiController.getWeather(this, "beijing", new FetchEntryListener() {
            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof WeatherModel) {
                    weatherModel = (WeatherModel) entry;
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!TextUtils.isEmpty(weatherModel.getIcon()))
                        MyApplication.finalBitmap.display(weatherImg, weatherModel.getIcon());
                    weatherTxt.setText(weatherModel.getDesc());
                    break;
            }
        }
    };

    private void initView() {
        findViewById(R.id.main_left).setOnClickListener(this);
        findViewById(R.id.main_right).setOnClickListener(this);

        weatherImg = (ImageView) findViewById(R.id.main_weather_img);
        weatherTxt = (TextView) findViewById(R.id.main_weather_txt);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_left:
                startActivity(new Intent(MainActivity.this, LeftActivity.class));
                break;
            case R.id.main_right:
                startActivity(new Intent(MainActivity.this, MyListActivity.class));
                break;
        }

    }
}
