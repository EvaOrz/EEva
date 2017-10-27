package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.model.VerifyCode;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;

/**
 * 注册页面
 *
 * @author Eva. 密码长度为4~16位 密码有安全性等级划分，根据各项指标进行加分，最后根据总分数定级 密码字符类型分为四种
 *         数字（Unicode:30~39） 小写字母（Unicode：61~7A） 大写字母（Unicode：41~5A）
 *         符号（Unicode：除以上外，20~7E） 密码长度分数 4~6字符：0分 7~8字符：5分 9~16字符：10分 单类型字符总数分数
 *         1~2字符：0分 3~16字符：5分 总类型数目分数 1种：0分 2种：2分 3种：3分 4种：5分 每有一个字符与上一个字符相同：-1分
 *         分数定级 -∞~15分：弱 16~25分：中 26~35分：强
 */
public class RegisterActivity extends BaseActivity {
    private ApiController mController;
    private Animation shakeAnim;
    private EditText mAcountEdit, mNickEdit, mPwdEdit, mVerifyEdit;
    private TextView getVerify, announcation1, announcation2;
    private LinearLayout ifShow;
    private ImageView pwdImg;

    private boolean canGetVerify = true;// 是否可获取验证码
    private boolean isShowPassword = false;// 是否显示密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mController = ApiController.getInstance(this);
        initView();
    }

    private void initView() {
        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
        findViewById(R.id.register_close).setOnClickListener(this);
        findViewById(R.id.register_login).setOnClickListener(this);
        announcation1 = (TextView) findViewById(R.id.register_announcation1);
        announcation2 = (TextView) findViewById(R.id.register_announcation2);
        setPhone(announcation1, announcation2);
        announcation1.setOnClickListener(this);
        announcation2.setOnClickListener(this);

        mAcountEdit = (EditText) findViewById(R.id.register_account);

        ifShow = (LinearLayout) findViewById(R.id.register_ifshow);
        mAcountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Tools.checkIsPhone(RegisterActivity.this, s.toString())) {
                    ifShow.setVisibility(View.VISIBLE);
                } else ifShow.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mNickEdit = (EditText) findViewById(R.id.register_nickname);
        mPwdEdit = (EditText) findViewById(R.id.register_password);
        pwdImg = (ImageView) findViewById(R.id.register_pwd_clear);
        mVerifyEdit = (EditText) findViewById(R.id.register_verify);
        getVerify = (TextView) findViewById(R.id.register_verify_get);
        getVerify.setOnClickListener(this);
        findViewById(R.id.register_account_clear).setOnClickListener(this);
        findViewById(R.id.register_nickname_clear).setOnClickListener(this);
        pwdImg.setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
    }


    /*设置电话颜色*/
    private void setPhone(TextView t1, TextView t2) {
        SpannableStringBuilder mBuilder = new SpannableStringBuilder(getString(R.string.register_announcation1));
        mBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), 12, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        t1.setText(mBuilder);
        SpannableStringBuilder mBuilder2 = new SpannableStringBuilder(getString(R.string.register_announcation2));
        mBuilder2.setSpan(new ForegroundColorSpan(Color.BLUE), 2, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        t2.setText(mBuilder2);

    }

    @Override
    public void onClick(View v) {
        String account = "", nick = "", pwd = "", verifyCode = "";// 账号、昵称、密码、验证码
        account = mAcountEdit.getText().toString();
        nick = mNickEdit.getText().toString();
        pwd = mPwdEdit.getText().toString();

        // 点击处理
        if (mVerifyEdit != null) verifyCode = mVerifyEdit.getText().toString();
        if (v.getId() == R.id.register_close) {// 关闭
            finish();
        } else if (v.getId() == R.id.register) {// 注册
            if (Tools.checkIsEmailOrPhone(this, account)) {
                if (Tools.checkString(nick, mNickEdit, shakeAnim) && Tools.checkString(pwd, mPwdEdit, shakeAnim)) {
                    if (pwd.length() > 3 && pwd.length() < 17) {
                        if (Tools.checkIsPhone(this, account)) {
                            if (Tools.checkString(verifyCode, mVerifyEdit, shakeAnim))
                                doRegister(account, nick, pwd, account, verifyCode);
                        } else doRegister(account, nick, pwd, null, null);
                    } else showToast(R.string.password_length_error);// 密码长度错误
                }
            } else showToast(R.string.get_account_error);// 账号格式错误

        } else if (v.getId() == R.id.register_verify_get) {// 获取验证码
            if (Tools.checkIsPhone(this, account)) doGetVerifyCode(account);
            else showToast(R.string.get_account_error);// 手机号码格式错误

        } else if (v.getId() == R.id.register_account_clear) {// 清除账号输入
            mAcountEdit.setText("");
        } else if (v.getId() == R.id.register_nickname_clear) {// 清除昵称输入
            mNickEdit.setText("");
        } else if (v.getId() == R.id.register_pwd_clear) {// 清除密码输入
            if (isShowPassword) {// 隐藏
                mPwdEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                pwdImg.setImageResource(R.mipmap.password_unshow);
            } else {//选择状态 显示明文--设置为可见的密码
                mPwdEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                pwdImg.setImageResource(R.mipmap.password_show);
            }
            isShowPassword = !isShowPassword;
            return;
        } else if (v.getId() == R.id.register_login) {// 去登录
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        } else if (v.getId() == R.id.register_announcation1) {// 用户协议
            UriParse.doLinkWeb(RegisterActivity.this, "https://buy.bbwc.cn/html/terms.html");
        } else if (v.getId() == R.id.register_announcation2) {
            UriParse.doLinkWeb(RegisterActivity.this, "https://buy.bbwc.cn/html/privacy.html");
        }
    }

    /**
     * 用户注册
     */
    protected void doRegister(final String account, String nick, final String pwd, String phone, final String code) {
        showLoadingDialog(true);
        // username \ password \ code \ phone \ nick
        mController.register(account, pwd, code, phone, nick, new FetchEntryListener() {

            @Override
            public void setData(final Entry entry) {
                showLoadingDialog(false);
                String toast = "";
                if (entry instanceof UserModel) {
                    UserModel user = (UserModel) entry;
                    ErrorMsg error = user.getError();
                    if (error.getNo() == 0) {
                        // 注册成功
                        if (code != null && code.length() > 0) user.setPhone(account);
                        else user.setEmail(account);

                        user.setPassword(pwd);
                        user.setLogined(true);
                        // 将相关信息用SharedPreferences存储
                        DataHelper.saveUserLoginInfo(RegisterActivity.this, user);
                        // 返回上一级界面
                        afterRegister(user);
                        return;
                    } else {
                        toast = error.getDesc();
                    }
                }

                showToast(TextUtils.isEmpty(toast) ? getString(R.string.msg_register_failed) : toast);
            }
        });
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

    /**
     * 注册成功，默认进入设置用户信息界面
     *
     * @param user
     */
    protected void afterRegister(UserModel user) {
        showToast(R.string.msg_register_success);
        CommonApplication.loginStatusChange = 2;
        finish();
    }


}
