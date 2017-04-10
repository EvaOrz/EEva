package modernmedia.com.cn.exhibitioncalendar.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import modernmedia.com.cn.corelib.BaseActivity;
import modernmedia.com.cn.corelib.db.DataHelper;
import modernmedia.com.cn.corelib.listener.FetchEntryListener;
import modernmedia.com.cn.corelib.model.Entry;
import modernmedia.com.cn.corelib.model.ErrorMsg;
import modernmedia.com.cn.corelib.model.UserModel;
import modernmedia.com.cn.corelib.model.VerifyCode;
import modernmedia.com.cn.corelib.util.Tools;
import modernmedia.com.cn.exhibitioncalendar.R;
import modernmedia.com.cn.exhibitioncalendar.api.ApiController;

/**
 * 手机注册忘记密码
 *
 * @author lusiyuan
 */
public class ForgetPwdActivity extends BaseActivity {

    private EditText phoneEdit, codeEdit, passwordEdit;
    private TextView getCode, announcation;
    private String phoneNum;
    private ImageView pwdImg;
    private boolean canGetVerify = true;// 是否可获取验证码
    private ApiController mController;
    private Animation shakeAnim;
    private LinearLayout ifshow;

    private boolean isShowPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
        phoneNum = getIntent().getStringExtra("phone_number");
        mController = ApiController.getInstance(this);
        initView();
    }

    private void initView() {
        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
        phoneEdit = (EditText) findViewById(R.id.forget_account);
        codeEdit = (EditText) findViewById(R.id.forget_verify);
        passwordEdit = (EditText) findViewById(R.id.forget_password);
        ifshow = (LinearLayout) findViewById(R.id.forget_ifshow);
        announcation = (TextView) findViewById(R.id.code_register_announcation);

        phoneEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Tools.checkIsPhone(ForgetPwdActivity.this, s.toString())) {
                    ifshow.setVisibility(View.VISIBLE);
                } else ifshow.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.forget_account_clear).setOnClickListener(this);
        pwdImg = (ImageView) findViewById(R.id.forget_pwd_clear);
        pwdImg.setOnClickListener(this);
        findViewById(R.id.forget_confirm).setOnClickListener(this);
        getCode = (TextView) findViewById(R.id.forget_verify_get);

        findViewById(R.id.forget_close).setOnClickListener(this);
        getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String newPass = "", code = "";
        phoneNum = phoneEdit.getText().toString();
        code = codeEdit.getText().toString();
        newPass = passwordEdit.getText().toString();
        if (v.getId() == R.id.forget_close) {// 关闭
            finish();
        } else if (v.getId() == R.id.forget_account_clear) {// 清除账号
            doClear(phoneEdit);
        } else if (v.getId() == R.id.forget_pwd_clear) {// 清除密码
            if (isShowPassword) {// 隐藏
                passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                pwdImg.setImageResource(R.mipmap.password_unshow);
            } else {//选择状态 显示明文--设置为可见的密码
                passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                pwdImg.setImageResource(R.mipmap.password_show);
            }
            isShowPassword = !isShowPassword;
            return;
        } else if (v.getId() == R.id.forget_confirm) {// 完成
            checkStyle(phoneNum, newPass, code);
        } else if (v.getId() == R.id.forget_verify_get) {// 获取验证码
            doGetVerifyCode();
        }
    }

    /**
     * 获取手机验证码
     */
    protected void doGetVerifyCode() {
        if (canGetVerify) {
            canGetVerify = false;
            // 开启倒计时器
            new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    getCode.setText(millisUntilFinished / 1000 + "s重新获取");
                }

                public void onFinish() {
                    getCode.setText(R.string.get_verify_code);
                    canGetVerify = true;
                }
            }.start();
            mController.getVerifyCode(phoneNum, new FetchEntryListener() {

                @Override
                public void setData(Entry entry) {
                    if (entry instanceof VerifyCode) {
                        showToast(entry.toString());
                        handler.sendEmptyMessage(0);
                    }
                }
            });
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            announcation.setVisibility(View.VISIBLE);
        }
    };

    /**
     * 忘记密码
     */
    protected void checkStyle(final String userName, final String newPwd, String code) {
        /**
         * 账号密码不能为空
         */
        if (Tools.checkString(userName, phoneEdit, shakeAnim)) {
            /**
             * 手机\Email状态
             */
            if (Tools.checkIsEmailOrPhone(this, userName)) {
                if (Tools.checkIsPhone(this, userName)) {
                    if (Tools.checkString(code, codeEdit, shakeAnim) && Tools.checkString(newPwd, passwordEdit, shakeAnim))
                        doForgetPwd(1, userName, newPwd, code);
                } else {
                    new AlertDialog.Builder(ForgetPwdActivity.this).setTitle(R.string.find_back_pwd).setMessage(String.format(getString(R.string.find_back_pwd1), userName)).setNegativeButton(R.string.cancel, null).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doForgetPwd(2, userName, newPwd, null);
                            dialog.dismiss();
                            new AlertDialog.Builder(ForgetPwdActivity.this).setTitle(R.string.forget_email_text1).setMessage(R.string.forget_email_text2).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                        }
                    }).show();

                }

            } else {
                showToast(R.string.msg_login_email_error);
            }
        }

    }

    private void doForgetPwd(final int type, final String userName, final String newPwd, String code) {
        showLoadingDialog(true);
        mController.getPassword(userName, code, newPwd, new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                showLoadingDialog(false);
                String toast = "";
                if (entry instanceof UserModel) {
                    UserModel resUser = (UserModel) entry;
                    ErrorMsg error = resUser.getError();
                    if (error.getNo() == 0) {
                        if (type == 1) {// 电话
                            toast = getString(R.string.modify_success);
                            // 手机登录不自动登录
                            //                            doLogin(userName, newPwd);
                        } else if (type == 2) {// email
                            return;
                        }
                    } else {
                        toast = error.getDesc();
                    }
                }
                showToast(TextUtils.isEmpty(toast) ? getString(R.string.msg_find_pwd_failed) : toast);
            }
        });
    }

    /**
     * 用户登录
     *
     * @param userName 用户名称
     * @param password 密码
     */
    protected void doLogin(final String userName, final String password) {
        showLoadingDialog(true);
        mController.login(userName, password, new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                showLoadingDialog(false);
                String toast = "";
                if (entry instanceof UserModel) {
                    UserModel user = (UserModel) entry;
                    ErrorMsg error = user.getError();
                    if (error.getNo() == 0 && !TextUtils.isEmpty(user.getUid())) {
                        // 登录成功
                        user.setPassword(password);
                        user.setLogined(true);
                        user.setPhone(userName);
                        // 将相关信息用SharedPreferences存储
                        DataHelper.saveUserLoginInfo(ForgetPwdActivity.this, user);
                        DataHelper.saveAvatarUrl(ForgetPwdActivity.this, user.getUserName(), user.getAvatar());
                        finish();
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
     * 清除用户名
     */
    protected void doClear(EditText e) {
        if (e != null) e.setText("");
    }


}
