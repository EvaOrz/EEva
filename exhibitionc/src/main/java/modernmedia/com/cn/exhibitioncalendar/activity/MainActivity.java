package modernmedia.com.cn.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.exhibitioncalendar.R;


/**
 * Created by Eva. on 17/3/27.
 */

public class MainActivity extends BaseActivity {
    private ImageView weatherImg;
    private TextView weatherTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

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
                startActivity(new Intent(MainActivity.this, UserCenterActivity.class));
                break;
        }

    }
}
