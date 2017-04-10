package modernmedia.com.cn.exhibitioncalendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.widget.RoundImageView;
import modernmedia.com.cn.exhibitioncalendar.R;

/**
 * Created by Eva. on 17/4/1.
 */

public class UserCenterActivity extends BaseActivity {

    private RoundImageView avatar;
    private TextView nickname, realname, telephone, address, birthday;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
    }

    private void initView() {

        findViewById(R.id.user_back).setOnClickListener(this);
        findViewById(R.id.edit_pwd).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        avatar = (RoundImageView) findViewById(R.id.avatar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_back:
                finish();
                break;
            case R.id.edit_pwd:
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.avatar:
                break;
        }
    }

    private void logout() {

    }
}
