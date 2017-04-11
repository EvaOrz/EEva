package modernmedia.com.cn.exhibitioncalendar.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.CommonApplication;
import modernmedia.com.cn.corelib.db.DataHelper;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.corelib.model.ErrorMsg;
import modernmedia.com.cn.corelib.model.UserModel;
import modernmedia.com.cn.corelib.util.FetchPhotoManager;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.corelib.widget.RoundImageView;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.api.ApiController;

import static modernmedia.com.cn.corelib.CommonApplication.mContext;

/**
 * Created by Eva. on 17/4/1.
 */

public class UserCenterActivity extends BaseActivity {

    private RoundImageView avatar;
    private TextView nickname, realname, telephone, address, birthday;
    private static final String AVATAR_PIC = "avatar.jpg";

    private String picturePath;// 头像
    private UserModel mUser;
    private ApiController mController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        picturePath = Environment.getExternalStorageDirectory().getPath() + "/" + AVATAR_PIC;
        mUser = DataHelper.getUserLoginInfo(this);
        initView();
    }

    private void initView() {

        findViewById(R.id.user_back).setOnClickListener(this);
        findViewById(R.id.edit_pwd).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        avatar = (RoundImageView) findViewById(R.id.avatar);
        nickname = (TextView) findViewById(R.id.nickname);
        realname = (TextView) findViewById(R.id.real_name);
        telephone = (TextView) findViewById(R.id.telephone);
        address = (TextView) findViewById(R.id.address);
        birthday = (TextView) findViewById(R.id.birthday);

        if (mUser != null) {
            Tools.setAvatar(UserCenterActivity.this, mUser.getAvatar(), avatar);
            nickname.setText(mUser.getNickName());
            realname.setText(mUser.getRealname());
            telephone.setText(mUser.getPhone());
            address.setText(mUser.getAddress());
            birthday.setText(mUser.getBirth());

        }
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
                FetchPhotoManager fetchPhotoManager = new FetchPhotoManager(this, picturePath);
                fetchPhotoManager.doFecthPicture();

                break;


        }
    }


    /**
     * 更新用户信息
     *
     * @param user
     * @param url        图片的相对地址(通过上传头像获得)
     * @param avatar_url 头像的绝对地址
     */
    public void modifyUserInfo(UserModel user, String url, final String avatar_url, final boolean isPushEmail) {
        if (user == null) return;
        showLoadingDialog(true);
        // 只更新头像、昵称信息
        mController.modifyUserInfo(user.getUid(), user.getToken(), user.getUserName(), user.getNickName(), url, null, user.getDesc(), isPushEmail, new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                showLoadingDialog(false);
                if (entry instanceof UserModel) {
                    UserModel resUser = (UserModel) entry;
                    ErrorMsg error = resUser.getError();
                    if (error.getNo() == 0) {

                        DataHelper.setNickname(mContext, resUser.getNickName());
                        DataHelper.setDesc(mContext, resUser.getDesc());
                        mUser.setNickName(resUser.getNickName());
                        mUser.setAvatar(resUser.getAvatar());
                        mUser.setDesc(resUser.getDesc());
                        handler.sendEmptyMessage(1);
                    }// 修改失败
                    else {
                        showToast(error.getDesc());
                    }
                }

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {// 绑定信息变更
                //                if (mUser == null )return;
                //                if (mUser.isBandQQ()) qq.setImageResource(R.drawable.login_qq);
                //                if (mUser.isBandWeibo()) sina.setImageResource(R.drawable.login_sina);
                //                if (mUser.isBandWeixin()) weixin.setImageResource(R.drawable.login_weixin);
                //                if (mUser.isBandPhone())
                //                    phoneText.setText(mUser.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                //                else phoneText.setText(R.string.band_yet);// 未绑定


            } else if (msg.what == 1) {// 昵称、头像、签名变更
                nickname.setText(DataHelper.getNickname(mContext));
                // 设置头像
                Tools.setAvatar(UserCenterActivity.this, mUser.getAvatar(), avatar);
            }

        }
    };

    private void logout() {
        DataHelper.clearLoginInfo(this);
        CommonApplication.loginStatusChange = true;
        finish();
    }
}
