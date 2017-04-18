package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.util.FetchPhotoManager;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.widget.RoundImageView;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.view.AddPopView;
import cn.com.modernmedia.exhibitioncalendar.view.SignDialog;

/**
 * Created by Eva. on 17/4/1.
 */

public class UserCenterActivity extends BaseActivity {

    private static final String AVATAR_PIC = "avatar.jpg";
    private RoundImageView avatar;
    private TextView nickname, realname, telephone, address, birthday, email, sign;
    private String picturePath;// 头像
    private UserModel mUser;
    private ApiController mController;


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
                if (mUser != null) {
                    Tools.setAvatar(UserCenterActivity.this, mUser.getAvatar(), avatar);
                    nickname.setText(mUser.getNickName());
                    realname.setText(mUser.getRealname());
                    sign.setText(mUser.getDesc());
                    telephone.setText(mUser.getPhone());
                    address.setText(mUser.getAddress());
                    birthday.setText(mUser.getBirth());
                    email.setText(mUser.getEmail());
                }
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        picturePath = Environment.getExternalStorageDirectory().getPath() + "/" + AVATAR_PIC;
        mUser = DataHelper.getUserLoginInfo(this);
        mController = ApiController.getInstance(this);
        initView();
    }

    private void initView() {

        findViewById(R.id.user_back).setOnClickListener(this);
        findViewById(R.id.edit_pwd).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.user_ok).setOnClickListener(this);
        avatar = (RoundImageView) findViewById(R.id.avatar);
        nickname = (TextView) findViewById(R.id.nickname);
        realname = (TextView) findViewById(R.id.real_name);
        telephone = (TextView) findViewById(R.id.telephone);
        address = (TextView) findViewById(R.id.address);
        birthday = (TextView) findViewById(R.id.birthday);
        email = (TextView) findViewById(R.id.email_ch);
        sign = (TextView) findViewById(R.id.sign);

        avatar.setOnClickListener(this);
        nickname.setOnClickListener(this);
        realname.setOnClickListener(this);
        telephone.setOnClickListener(this);
        address.setOnClickListener(this);
        birthday.setOnClickListener(this);
        email.setOnClickListener(this);
        sign.setOnClickListener(this);

        handler.sendEmptyMessage(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_back:
                finish();
                break;
            case R.id.edit_pwd:
                startActivity(new Intent(UserCenterActivity.this, ForgetPwdActivity.class));
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.avatar:
                FetchPhotoManager fetchPhotoManager = new FetchPhotoManager(this, picturePath);
                fetchPhotoManager.doFecthPicture();

                break;
            case R.id.nickname:
                new SignDialog(UserCenterActivity.this, 1, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setNickName(text);
                        handler.sendEmptyMessage(1);
                    }
                });
                break;
            case R.id.real_name:
                new SignDialog(UserCenterActivity.this, 2, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setRealname(text);
                        handler.sendEmptyMessage(1);
                    }
                });
                break;
            case R.id.telephone:
                new SignDialog(UserCenterActivity.this, 4, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setPhone(text);
                        handler.sendEmptyMessage(1);
                    }
                });
                break;
            case R.id.address:
                new SignDialog(UserCenterActivity.this, 3, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setAddress(text);
                        handler.sendEmptyMessage(1);
                    }
                });
                break;

            case R.id.email_ch:
                new SignDialog(UserCenterActivity.this, 6, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setEmail(text);
                        handler.sendEmptyMessage(1);
                    }
                });

            case R.id.sign:
                new SignDialog(UserCenterActivity.this, 5, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setDesc(text);
                        handler.sendEmptyMessage(1);
                    }
                });
            case R.id.birthday:
                new AddPopView(UserCenterActivity.this, 3,mUser.getBirth());
                break;

            case R.id.user_ok:
                showLoadingDialog(true);
                modifyUserInfo();
                break;
        }
    }

    /**
     * 更新用户信息
     */
    public void modifyUserInfo() {
        showLoadingDialog(true);
        // 只更新头像、昵称信息
        mController.modifyUserInfo(mUser.getUid(), mUser.getToken(), mUser.getUserName(), mUser.getNickName(), mUser.getAvatar(), null, mUser.getDesc(), false, new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                showLoadingDialog(false);
                if (entry instanceof UserModel) {
                    UserModel resUser = (UserModel) entry;
                    ErrorMsg error = resUser.getError();
                    if (error.getNo() == 0) {
                        DataHelper.saveUserLoginInfo(UserCenterActivity.this, resUser);
                        showToast("修改成功");
                    }// 修改失败
                    else {
                        showToast(error.getDesc());
                    }
                }

            }
        });
    }

    public void setBirth(int year, int month, int day) {
        mUser.setBirth(year + "-" + month + "-" + day);
        handler.sendEmptyMessage(1);
    }

    private void logout() {
        DataHelper.clearLoginInfo(this);
        CommonApplication.loginStatusChange = true;
        finish();
    }


//    /**
//     * 上传用户头像
//     *
//     * @param imagePath 头像存储在本地的路径
//     */
//    protected void uploadAvatar(String imagePath) {
//        if (user == null || TextUtils.isEmpty(imagePath)) return;
//
//        if (!new File(imagePath).exists()) {
//            showLoadingDialog(false);
//            showToast(R.string.msg_avatar_get_failed);// 头像获取失败
//            return;
//        }
//
//        showLoadingDialog(true);
//        mController.uploadAvatar(imagePath, new FetchEntryListener() {
//
//                    @Override
//                    public void setData(final Entry entry) {
//                        showLoadingDialog(false);
//                        String toast = "";
//                        if (entry instanceof UploadAvaterApi.) {
//                            UploadAvatarApi.UploadAvatarResult result = (UploadAvatarApi.UploadAvatarResult) entry;
//                            //                    String status = result.getStatus();
//                            //                    if (status.equals("success")) { // 头像上传成功
//                            if (!TextUtils.isEmpty(result.getAvatarPath())) {
//
//                                doModify(result.getAvatarPath());
//                                return;
//                                //                    }
//                            } else {
//                                toast = result.getMsg();
//                            }
//                        }
//                        showToast(TextUtils.isEmpty(toast) ? getString(R.string.msg_avatar_upload_failed) : toast);
//                    }
//                }
//
//        );
//    }
}
