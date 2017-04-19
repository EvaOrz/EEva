package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.File;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.listener.ImageDownloadStateListener;
import cn.com.modernmedia.corelib.listener.OpenAuthListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.model.VerifyCode;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.util.sina.SinaAPI;
import cn.com.modernmedia.corelib.util.sina.SinaAuth;
import cn.com.modernmedia.corelib.util.sina.SinaRequestListener;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.UploadAvatarResult;
import cn.com.modernmedia.exhibitioncalendar.view.OpenLoginPopwindow;

/**
 * 登录页面
 *
 * @author Eva.
 */
public class LoginActivity extends BaseActivity {
    public static OnWXLoginCallback sWXLoginCallback;
    public static int weixin_login = 0;
    private Context mContext;
    private ApiController mController;
    private Animation shakeAnim;
    private SinaAuth weiboAuth;
    private Button mLoginBtn;
    private EditText mAcountEdit, mPasswordEdit, mPhoneEdit, mCodeEdit;
    private LinearLayout nomalLoginLayout, phoneLoginLayout;

    private TextView getVerify, forgetTextView, phoneTextView, lastTextView;
    private ImageView pwdImg;
    private boolean canGetVerify = true;// 是否可获取验证码
    private String shareData = "";// 分享的内容
    private boolean shouldFinish = false;// 当直接跳到发笔记页的时候，不会立即执行destory，所以延迟500ms
    // 第三方app与微信通信的openapi接口
    private IWXAPI api;
    private boolean isNomalLogin = true;// 是否是正常登录方式
    private boolean isShowPassword = false;// 是否显示密码
    private String lastUserName = "";// 记录登录方式（邮箱、手机号）
    /**
     * 从网页跳转支付，需要先验证登录
     */
    private String fromSplashBundle;

    public static Class<LoginActivity> getLoginClass() {
        return LoginActivity.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mController = ApiController.getInstance(this);
        setContentView(R.layout.activity_login);
        initView();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (shouldFinish) {
                    finish();
                }
            }
        }, 500);

        fromSplashBundle = getIntent().getStringExtra("pid");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle bud = getIntent().getExtras();
        if (bud != null && bud.containsKey("pid")) {
            fromSplashBundle = getIntent().getStringExtra("pid");
        }

    }

    private void initView() {
        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
        mAcountEdit = (EditText) findViewById(R.id.login_account);
        mPasswordEdit = (EditText) findViewById(R.id.login_password);
        mPhoneEdit = (EditText) findViewById(R.id.login_phonenumber);
        mCodeEdit = (EditText) findViewById(R.id.login_code);
        nomalLoginLayout = (LinearLayout) findViewById(R.id.nomal_login);
        phoneLoginLayout = (LinearLayout) findViewById(R.id.phone_login);
        getVerify = (TextView) findViewById(R.id.login_verify_get);
        mLoginBtn = (Button) findViewById(R.id.login_btn_login);
        forgetTextView = (TextView) findViewById(R.id.login_forget_pwd);
        phoneTextView = (TextView) findViewById(R.id.login_phone);
        pwdImg = (ImageView) findViewById(R.id.login_img_pass_show);
        lastTextView = (TextView) findViewById(R.id.login_login_username);

        String last = DataHelper.getLastLoginUsername(this);
        if (last.equals("qq")) {
            lastTextView.setText("上次登录方式：QQ登录");
        } else if (last.equals("sina")) {
            lastTextView.setText("上次登录方式：新浪微博登录");
        } else if (last.equals("weixin")) {
            lastTextView.setText("上次登录方式：微信登录");
        } else if (Tools.checkIsPhone(this, last)) {
            lastTextView.setText("上次登录方式：手机登录");
        } else {
            lastTextView.setText("上次登录方式：邮箱登录");
        }

        findViewById(R.id.login_img_close).setOnClickListener(this);
        findViewById(R.id.login_img_clear).setOnClickListener(this);
        findViewById(R.id.login_phone_clear).setOnClickListener(this);
        forgetTextView.setOnClickListener(this);
        pwdImg.setOnClickListener(this);
        findViewById(R.id.login_registers).setOnClickListener(this);
        phoneTextView.setOnClickListener(this);
        findViewById(R.id.login_open).setOnClickListener(this);

        mLoginBtn.setOnClickListener(this);
        getVerify.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String psd = "";
        String phone = "", verifyCode = "";// 账号、昵称、密码、验证码

        if (mPhoneEdit != null) phone = mPhoneEdit.getText().toString();
        if (mCodeEdit != null) verifyCode = mCodeEdit.getText().toString();
        if (mAcountEdit != null) {
            lastUserName = mAcountEdit.getText().toString();
        }
        if (mPasswordEdit != null) {
            psd = mPasswordEdit.getText().toString();
        }

        if (v.getId() == R.id.login_img_clear) {
            if (mAcountEdit != null) mAcountEdit.setText("");
            return;
        } else if (v.getId() == R.id.login_img_pass_show) {
            if (isShowPassword) {// 隐藏
                mPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                //                mPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                pwdImg.setImageResource(R.mipmap.password_unshow);
            } else {//选择状态 显示明文--设置为可见的密码
                mPasswordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                //                mPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                pwdImg.setImageResource(R.mipmap.password_show);
            }
            isShowPassword = !isShowPassword;
            return;


        } else if (v.getId() == R.id.login_phone_clear) {
            if (mPhoneEdit != null) mPhoneEdit.setText("");
            return;

        } else if (v.getId() == R.id.login_forget_pwd) {
            Intent i = new Intent(mContext, ForgetPwdActivity.class);
            startActivity(i);
            return;

        } else if (v.getId() == R.id.login_img_close) {
            finish();
        } else if (v.getId() == R.id.login_registers) {// 注册
            Intent i = new Intent(mContext, RegisterActivity.class);
            startActivity(i);
            return;

        } else if (v.getId() == R.id.login_btn_login) {
            if (isNomalLogin) {
                if (Tools.checkString(lastUserName, mAcountEdit, shakeAnim) && Tools.checkString(psd, mPasswordEdit, shakeAnim))
                    doLogin(lastUserName, psd);
            } else {
                if (Tools.checkIsPhone(LoginActivity.this, phone)) {
                    if (Tools.checkString(verifyCode, mCodeEdit, shakeAnim)) {
                        UserModel uP = new UserModel();
                        uP.setPhone(phone);
                        openLogin(uP, 5, verifyCode);
                    }

                } else showToast(R.string.get_account_error);// 手机号码格式错误

            }
            return;

        } else if (v.getId() == R.id.login_verify_get) {// 获取验证码
            if (Tools.checkIsPhone(this, phone)) doGetVerifyCode(phone);
            else showToast(R.string.get_account_error);// 手机号码格式错误
            return;


        } else if (v.getId() == R.id.login_phone) {// 切换登录方式
            if (isNomalLogin) { // 切换成phone登录
                nomalLoginLayout.setVisibility(View.GONE);
                phoneLoginLayout.setVisibility(View.VISIBLE);
                forgetTextView.setVisibility(View.GONE);
                phoneTextView.setText(R.string.pwd_login);
            } else {// 切换成nomal登录
                nomalLoginLayout.setVisibility(View.VISIBLE);
                phoneLoginLayout.setVisibility(View.GONE);
                forgetTextView.setVisibility(View.VISIBLE);
                phoneTextView.setText(R.string.phone_login);
            }
            isNomalLogin = !isNomalLogin;
            return;

        } else if (v.getId() == R.id.login_open) {
            new OpenLoginPopwindow(LoginActivity.this);
            return;

        }


    }

    /**
     * 获取手机验证码
     */
    protected void doGetVerifyCode(String phone) {
        if (canGetVerify) {
            canGetVerify = false;
            // 开启倒计时器
            new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    getVerify.setText(millisUntilFinished / 1000 + "s重新获取");
                }

                public void onFinish() {
                    getVerify.setText(R.string.get_verify_code);
                    canGetVerify = true;
                }
            }.start();
            mController.getVerifyCode(phone, new FetchEntryListener() {

                @Override
                public void setData(Entry entry) {
                    if (entry instanceof VerifyCode) {
                        showToast(entry.toString());
                    }
                }
            });
        }
    }

    public void doWeixinlogin() {
        weixin_login = 1;
        sWXLoginCallback = new OnWXLoginCallback() {
            @Override
            public void onLogin(boolean isFirstLogin, UserModel user) {
                if (isFirstLogin) {
                    DataHelper.saveHasSync(mContext, true);
                    openLogin(user, 3, null);
                } else {// 登录过
                    lastUserName = "weixin";
                    afterLogin(user);
                    showLoadingDialog(false);
                }
            }
        };

        if (api == null) {
            api = WXAPIFactory.createWXAPI(mContext, MyApplication.WEIXIN_APP_ID, true);
        }

        if (!api.isWXAppInstalled()) {
            Tools.showToast(mContext, R.string.no_weixin);
            return;
        }
        api.registerApp(MyApplication.WEIXIN_APP_ID);

        SendAuth.Req req = new SendAuth.Req();
        // post_timeline
        req.scope = "snsapi_userinfo";
        req.state = "weixin_login";
        System.out.print("***********" + req.toString());
        api.sendReq(req);
    }

    /**
     * 登录成功
     *
     * @param user
     */
    protected void afterLogin(UserModel user) {
        DataHelper.saveUserLoginInfo(this, user);
        // 记录上次登录username
        DataHelper.setLastLoginUsername(this, lastUserName);
        // 返回上一级界面

        showToast(R.string.msg_login_success);
        finish();

    }


    /**
     * 开放平台(新浪微博、QQ等)账号登录
     *
     * @param user 用户信息
     * @param type 平台类型,目前0:普通登录；1：新浪微博；2：腾讯qq；3：微信；5：phone
     */
    public void openLogin(final UserModel user, final int type, String code) {
        mController.openLogin(this, user, "", code, type, new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                String toast = "";
                if (entry instanceof UserModel) {
                    UserModel mUser = (UserModel) entry;
                    ErrorMsg error = mUser.getError();
                    if (error.getNo() == 0) {
                        // 上传头像
                        if (type == 1) {
                            lastUserName = "sina";
                        } else if (type == 2) {
                            lastUserName = "qq";
                        } else if (type == 3) {
                            lastUserName = "weixin";
                        } else if (type == 5) {
                            lastUserName = user.getPhone();
                        }
                        getOpenUserAvatar(user);
                        afterLogin(mUser);
                        return;
                    } else {
                        toast = error.getDesc();
                    }
                }
                showToast(TextUtils.isEmpty(toast) ? getString(R.string.msg_login_fail) : toast);
            }
        });
    }

    /**
     * 获取开放平台用户头像
     */
    private void getOpenUserAvatar(final UserModel mUser) {
        CommonApplication.finalBitmap.display(mUser.getAvatar(), new ImageDownloadStateListener() {

            @Override
            public void loading() {
            }

            @Override
            public void loadError() {
            }

            @Override
            public void loadOk(Bitmap bitmap, NinePatchDrawable drawable, byte[] gifByte) {

            }
        });
    }

    /**
     * 上传用户头像
     *
     * @param imagePath 头像存储在本地的路径
     */
    protected void uploadAvatar(final UserModel mUser, String imagePath) {
        if (mUser == null || TextUtils.isEmpty(imagePath)) return;

        if (!new File(imagePath).exists()) {
            showToast(R.string.msg_avatar_get_failed);// 头像获取失败
            return;
        }

        mController.uploadUserAvatar(imagePath, new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                String toast = "";
                if (entry instanceof UploadAvatarResult) {
                    UploadAvatarResult result = (UploadAvatarResult) entry;
                    String status = result.getStatus();
                    if (status.equals("success")) { // 头像上传成功
                        if (!TextUtils.isEmpty(result.getImagePath())) {
                            modifyUserInfo(mUser, result.getImagePath());
                            return;
                        }
                    } else toast = result.getMsg();
                }
                showToast(TextUtils.isEmpty(toast) ? getString(R.string.msg_avatar_upload_failed) : toast);
            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @param url  图片的相对地址(通过上传头像获得)
     */
    public void modifyUserInfo(UserModel user, String url) {
        if (user == null) return;
        // 只更新头像
        mController.modifyUserInfo(this, user.getUid(), user.getToken(), user.getUserName(), user.getNickName(), url, null, user.getDesc(), false, new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                if (entry instanceof UserModel) {
                    UserModel resUser = (UserModel) entry;
                    ErrorMsg error = resUser.getError();
                    if (error.getNo() == 0) {
                        DataHelper.saveAvatarUrl(mContext, resUser.getUserName(), resUser.getAvatar());
                    }
                }
            }
        });
    }

    public void doQQLogin() {
        //        QQLoginUtil qqLoginUtil = QQLoginUtil.getInstance(mContext);
        //        qqLoginUtil.login();
        //        qqLoginUtil.setLoginListener(new UserModelAuthListener() {
        //
        //            @Override
        //            public void onCallBack(boolean isSuccess) {
        //                if (isSuccess) {
        //                    doAfterQQIsAuthed();
        //                } else {
        //                    showLoadingDialog(false);
        //                }
        //            }
        //        });
    }

    public void doSinaLogin() {
        // 新浪微博认证
        weiboAuth = new SinaAuth(mContext);
        if (!weiboAuth.checkIsOAuthed()) {
            weiboAuth.oAuth();
        } else {
            doAfterSinaIsOAuthed();
        }
        weiboAuth.setWeiboAuthListener(new OpenAuthListener() {
            @Override
            public void onCallBack(boolean isSuccess, String uid, String token) {
                if (isSuccess) doAfterSinaIsOAuthed();
            }
        });

    }

    /**
     * 简化登陆，授权之后不判定是否登陆过，直接获取用户信息
     */
    private void doAfterSinaIsOAuthed() {
        String sinaId = SinaAPI.getInstance(mContext).getSinaId();
        if (TextUtils.isEmpty(sinaId)) showToast(R.string.msg_login_fail);
        else {
            UserModel user = DataHelper.getUserLoginInfo(this);
            if (user != null && !TextUtils.isEmpty(user.getSinaId()) && user.getSinaId().equals(sinaId)) { // 已经用新浪微博账号在本应用上登录
                lastUserName = "sina";
                afterLogin(user);
            } else getSinaUserInfo();
        }
    }

    /**
     * 获取新浪用户相关信息
     */
    public void getSinaUserInfo() {
        showLoadingDialog(true);
        final SinaAPI sinaAPI = SinaAPI.getInstance(mContext);
        sinaAPI.fetchUserInfo(new SinaRequestListener() {

            @Override
            public void onSuccess(String response) {
                showLoadingDialog(false);
                JSONObject object;
                try {
                    Log.i("sina get user", response);
                    object = new JSONObject(response);
                    UserModel mUser = new UserModel();
                    mUser.setSinaId(object.optString("idstr", "")); // 新浪ID
                    mUser.setNickName(object.optString("screen_name")); // 昵称
                    mUser.setUserName(object.optString("idstr", ""));
                    mUser.setAvatar(object.optString("profile_image_url"));// 用户头像地址（中图），50×50像素
                    mUser.setOpenLoginJson(response);
                    DataHelper.saveHasSync(mContext, true);
                    openLogin(mUser, 1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String error) {
                showLoadingDialog(false);
            }
        });
    }

    private void doAfterQQIsAuthed() {

    }

    /**
     * 用户登录
     *
     * @param userName 用户名称
     * @param password 密码
     */
    protected void doLogin(final String userName, final String password) {
        // 检查格式
        if (Tools.checkIsEmailOrPhone(mContext, userName) && Tools.checkPasswordFormat(mContext, password)) {
            showLoadingDialog(true);
            mController.login(userName, password, new FetchEntryListener() {

                @Override
                public void setData(final Entry entry) {
                    String toast = "";
                    if (entry instanceof UserModel) {
                        UserModel user = (UserModel) entry;
                        ErrorMsg error = user.getError();
                        // 登录成功
                        if (error.getNo() == 0 && !TextUtils.isEmpty(user.getUid())) {
                            user.setPassword(password);
                            user.setLogined(true);
                            // 将相关信息用SharedPreferences存储
                            if (Tools.checkIsPhone(mContext, userName)) {
                                user.setPhone(userName);
                            } else {
                                user.setEmail(userName);
                            }
                            DataHelper.saveUserLoginInfo(mContext, user);
                            DataHelper.saveAvatarUrl(mContext, user.getUserName(), user.getAvatar());
                            showLoadingDialog(false);
                            afterLogin(user);
                            return;
                        } else {
                            toast = error.getDesc();
                        }
                        showLoadingDialog(false);
                        if (!TextUtils.isEmpty(toast)) showToast(toast);
                        else showToast(R.string.msg_login_fail);
                    }
                }
            });
        }
    }

    @Override
    public void finish() {
        if (DataHelper.getUserLoginInfo(this) != null) setResult(RESULT_OK);


        super.finish();
        overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onResume() {
        super.onResume();
        if (DataHelper.getUserLoginInfo(this) != null) finish();
    }

    public void onPause() {
        super.onPause();
    }

    public interface OnWXLoginCallback {
        void onLogin(boolean isFirstLogin, UserModel user);
    }


}
