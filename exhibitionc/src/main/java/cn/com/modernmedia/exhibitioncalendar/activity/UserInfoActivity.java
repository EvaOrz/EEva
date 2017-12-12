package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.util.FetchPhotoManager;
import cn.com.modernmedia.corelib.util.ImgFileManager;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.widget.RoundImageView;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.CalendarListModel;
import cn.com.modernmedia.exhibitioncalendar.model.UploadAvatarResult;
import cn.com.modernmedia.exhibitioncalendar.util.AppValue;
import cn.com.modernmedia.exhibitioncalendar.view.AddPopView;
import cn.com.modernmedia.exhibitioncalendar.view.SignDialog;

/**
 * 个人中心页面
 * Created by Eva. on 17/4/1.
 */

public class UserInfoActivity extends BaseActivity {

    private static final String KEY_IMAGE = "data";
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
                    Tools.setAvatar(UserInfoActivity.this, mUser.getUserName(), avatar);
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
                startActivity(new Intent(UserInfoActivity.this, ModifyPwdActivity.class));
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.avatar:

                FetchPhotoManager fetchPhotoManager = new FetchPhotoManager(this, picturePath);
                fetchPhotoManager.doFecthPicture();
                break;
            case R.id.nickname:
                new SignDialog(UserInfoActivity.this, 1, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setNickName(text);
                        handler.sendEmptyMessage(1);
                    }
                });
                break;
            case R.id.real_name:
                new SignDialog(UserInfoActivity.this, 2, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setRealname(text);
                        handler.sendEmptyMessage(1);
                    }
                });
                break;
            case R.id.telephone:
                new SignDialog(UserInfoActivity.this, 4, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setPhone(text);
                        handler.sendEmptyMessage(1);
                    }
                });
                break;
            case R.id.address:
                new SignDialog(UserInfoActivity.this, 3, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setAddress(text);
                        handler.sendEmptyMessage(1);
                    }
                });
                break;

            case R.id.email_ch:
                new SignDialog(UserInfoActivity.this, 6, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setEmail(text);
                        handler.sendEmptyMessage(1);
                    }
                });
                break;
            case R.id.sign:
                new SignDialog(UserInfoActivity.this, 5, new SignDialog.SignChangeListener() {
                    @Override
                    public void setText(String text) {
                        mUser.setDesc(text);
                        handler.sendEmptyMessage(1);
                    }
                });
                break;
            case R.id.birthday:
                new AddPopView(UserInfoActivity.this, 3, mUser.getBirth());
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
        mController.modifyUserInfo(this, mUser.getUid(), mUser.getToken(), mUser.getRealname(), mUser.getUserName(), mUser.getNickName(), mUser.getAvatar(), null, mUser.getDesc(), false, new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                showLoadingDialog(false);
                if (entry instanceof UserModel) {
                    UserModel resUser = (UserModel) entry;
                    ErrorMsg error = resUser.getError();
                    if (error.getNo() == 0) {
                        DataHelper.saveUserLoginInfo(UserInfoActivity.this, resUser);
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
        AppValue.myList = new CalendarListModel();
        CommonApplication.loginStatusChange = 2;
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == FetchPhotoManager.REQUEST_CAMERA) {
                Tools.startPhotoZoom(this, Uri.fromFile(new File(picturePath)), picturePath);
            } else if (requestCode == FetchPhotoManager.REQUEST_GALLERY) {
                if (data != null) {
                    Tools.startPhotoZoom(this, data.getData(), picturePath);
                }
            } else if (requestCode == Tools.REQUEST_ZOOM) {
                if (data != null && data.getExtras() != null) {
                    Bitmap bitmap = data.getExtras().getParcelable(KEY_IMAGE);
                    ImgFileManager.saveImage(bitmap, picturePath);
                    if (bitmap != null) {
                        uploadAvatar(picturePath);
                        bitmap.recycle();
                        bitmap = null;
                    }
                } else {
                    showToast(R.string.upload_failed);
                }
            }
            //            else if (requestCode == BandDetailActivity.BAND_SUCCESS) {
            //                mUser.setEmail(DataHelper.getUserLoginInfo(this).getEmail());
            ////                mUser.setPhone(DataHelper.getUserPhone(this));
            //                handler.sendEmptyMessage(0);
            //            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }


    /**
     * 上传用户头像
     *
     * @param imagePath 头像存储在本地的路径
     */
    protected void uploadAvatar(String imagePath) {
        if (mUser == null || TextUtils.isEmpty(imagePath)) return;

        if (!new File(imagePath).exists()) {
            showLoadingDialog(false);
            showToast(R.string.msg_avatar_get_failed);// 头像获取失败
            return;
        }

        showLoadingDialog(true);
        mController.uploadUserAvatar(imagePath, new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                showLoadingDialog(false);
                String toast = "";
                if (entry instanceof UploadAvatarResult) {
                    UploadAvatarResult result = (UploadAvatarResult) entry;
                    //                    String status = result.getStatus();
                    //                    if (status.equals("success")) { // 头像上传成功
                    if (!TextUtils.isEmpty(result.getAvatarPath())) {
                        mUser.setAvatar(result.getAvatarPath());
                        handler.sendEmptyMessage(1);
                        modifyUserInfo();
                        return;
                        //                    }
                    } else {
                        toast = result.getMsg();
                    }
                }
                showToast(TextUtils.isEmpty(toast) ? getString(R.string.msg_avatar_upload_failed) : toast);
            }
        });
    }


}