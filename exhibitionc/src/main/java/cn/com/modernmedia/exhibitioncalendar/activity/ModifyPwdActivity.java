package cn.com.modernmedia.exhibitioncalendar.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.ErrorMsg;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;

import static cn.com.modernmedia.corelib.CommonApplication.mContext;

/**
 * 默认修改密码页面
 *
 * @author ZhuQiao
 */
public class ModifyPwdActivity extends BaseActivity {
    private EditText mOldPwdEdit;
    private EditText mNewPwdEdit;
    private ApiController mController;
    private Animation shakeAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        initView();
    }


    private void initView() {
        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
        mController = ApiController.getInstance(this);
        mOldPwdEdit = (EditText) findViewById(R.id.modify_pwd_old_edit);
        mNewPwdEdit = (EditText) findViewById(R.id.modify_pwd_new_edit);

        findViewById(R.id.modify_old_clear).setOnClickListener(this);
        findViewById(R.id.modify_new_clear).setOnClickListener(this);
        findViewById(R.id.modify_sure).setOnClickListener(this);
        findViewById(R.id.modify_pwd_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.modify_old_clear) {
            doClear(1);
        } else if (id == R.id.modify_new_clear) {
            doClear(2);

        } else if (id == R.id.modify_pwd_back) {
            finish();
        } else if (id == R.id.modify_sure) {
            if (mOldPwdEdit != null && mNewPwdEdit != null) {
                String old = mOldPwdEdit.getText().toString();
                String ne = mNewPwdEdit.getText().toString();
                //UserTools.checkString(old, mOldPwdEdit, shakeAnim) &&
                if (Tools.checkString(ne, mNewPwdEdit, shakeAnim)) {

                    if (ne.length() > 3 && ne.length() < 17) {
                        doModifyPwd(old, ne);
                    } else showToast(R.string.password_length_error);// 密码长度错误
                }
            }
        }
    }

    /**
     * 清除旧密码
     */
    protected void doClear(int type) {
        if (type == 1) {
            if (mOldPwdEdit != null) mOldPwdEdit.setText("");
        } else if (type == 2) {
            if (mNewPwdEdit != null) mNewPwdEdit.setText("");
        }

    }


    /**
     * 修改用户密码
     *
     * @param oldPwd      旧密码
     * @param newPassword 新密码
     */
    protected void doModifyPwd(String oldPwd, String newPassword) {
        UserModel user = DataHelper.getUserLoginInfo(mContext);
        if (Tools.checkPasswordFormat(mContext, newPassword) && user != null) {
            showLoadingDialog(true);
            mController.modifyUserPassword(this, oldPwd, newPassword, new FetchEntryListener() {

                @Override
                public void setData(final Entry entry) {
                    showLoadingDialog(false);
                    String toast = "";
                    if (entry instanceof UserModel) {
                        UserModel resUser = (UserModel) entry;
                        ErrorMsg error = resUser.getError();
                        // 修改成功
                        if (error.getNo() == 0) {
                            showToast(R.string.msg_modify_success);
                            finish();
                            return;
                        } else {
                            toast = error.getDesc();
                        }
                    }
                    showToast(TextUtils.isEmpty(toast) ? getString(R.string.msg_modify_pwd_failed) : toast);
                }
            });
        }
    }


}
