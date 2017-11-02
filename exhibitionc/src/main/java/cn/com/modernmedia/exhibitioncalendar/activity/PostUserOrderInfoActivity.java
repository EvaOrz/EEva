package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchDataListener;
import cn.com.modernmedia.corelib.listener.FetchEntryListener;
import cn.com.modernmedia.corelib.model.Entry;
import cn.com.modernmedia.corelib.model.VerifyCode;
import cn.com.modernmedia.corelib.model.VipInfoModel;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.view.ChangeDistrictDialog;


/**
 * 新增vip 收货地址页面
 */
public class PostUserOrderInfoActivity extends BaseActivity {
    private EditText name, phone, address, code;
    private TextView city, getverify;
    private ApiController apiController;
    private VipInfoModel vipInfoModel;
    private boolean canGetVerify = true;// 是否可获取验证码
    private String pp, cc;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    city.setText(pp + " " + cc);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_info);

        apiController = ApiController.getInstance(this);

        initView();

    }

    private void initView() {
        name = (EditText) findViewById(R.id.order_name_edit);
        phone = (EditText) findViewById(R.id.order_phone_edit);
        city = (TextView) findViewById(R.id.order_city_edit);
        code = (EditText) findViewById(R.id.verify_code_edit);
        address = (EditText) findViewById(R.id.order_address_edit);
        getverify = (TextView) findViewById(R.id.verify_get);
        getverify.setOnClickListener(this);
        findViewById(R.id.order_post).setOnClickListener(this);
        findViewById(R.id.order_back).setOnClickListener(this);
        city.setOnClickListener(this);

        vipInfoModel = DataHelper.getVipInfo(this);
        if (vipInfoModel != null) {
            address.setText(vipInfoModel.getAddress());
            name.setText(vipInfoModel.getRealname());
            phone.setText(vipInfoModel.getPhone());
            pp = vipInfoModel.getProvince();
            cc = vipInfoModel.getCity();
            city.setText(pp + " " + cc);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //延迟100毫秒，弹出键盘
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) address.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(address, 0);
            }
        };
        handler.postDelayed(runnable, 100);
    }


    @Override
    public void onClick(View v) {
        String order_name = name.getText().toString();
        String order_phone = phone.getText().toString();
        String order_code = code.getText().toString();
        String order_address = address.getText().toString();

        if (v.getId() == R.id.order_post) {//提交
            String str = order_name.replaceAll(" ", "");//去掉所有空格
            if (TextUtils.isEmpty(str) || TextUtils.isEmpty(order_address)) {
                showToast(R.string.order_error_null);
                return;
            }
            if (!(order_phone.length() == 11) || !checkIsPhone(order_phone)) {
                showToast(R.string.order_error_phone_number);
                return;
            }
            if (TextUtils.isEmpty(order_code)) {
                showToast(R.string.msg_has_no_code);
                return;
            }
            commit(str, order_address, order_phone, order_code);
        } else if (v.getId() == R.id.order_back) {
            finish();
        } else if (v.getId() == R.id.order_city_edit) {
            changeDistrict();
        } else if (v.getId() == R.id.verify_get) {// 获取验证码
            if (Tools.checkIsPhone(this, order_phone)) doGetVerifyCode(order_phone);
            else showToast(R.string.get_account_error);// 手机号码格式错误
        }
    }


    private void commit(String realname, String address, String phone, String code) {
        showLoadingDialog(true);
        apiController.addVipInfo(realname, "", pp, cc, "", address, phone, code, new FetchDataListener() {
            @Override
            public void fetchData(boolean isSuccess, String data, boolean fromHttp) {
                Log.e("addVipInfo", data);
                showLoadingDialog(false);
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject != null) {
                            VipInfoModel vipInfoModel = VipInfoModel.parseVipInfoModel(jsonObject);
                            DataHelper.saveVipInfo(PostUserOrderInfoActivity.this, vipInfoModel);
                            finish();
                        }
                    } catch (JSONException e) {

                    }
                }
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
                    getverify.setText(millisUntilFinished / 1000 + "s重新获取");
                }

                public void onFinish() {
                    getverify.setText(R.string.get_verify_code);
                    canGetVerify = true;
                }
            }.start();
            apiController.getVerifyCode(phone, new FetchEntryListener() {

                @Override
                public void setData(Entry entry) {
                    if (entry instanceof VerifyCode) {
                        showToast(entry.toString());
                    }
                }
            });
        }
    }

    private void changeDistrict() {
        ChangeDistrictDialog mChangeAddressDialog = new ChangeDistrictDialog(this);
        mChangeAddressDialog.setAddress("北京", "朝阳");
        mChangeAddressDialog.show();
        mChangeAddressDialog.setAddresskListener(new ChangeDistrictDialog.OnAddressCListener() {

            @Override
            public void onClick(String province, String city) {
                pp = province;
                cc = city;
                mHandler.sendEmptyMessage(1);
            }
        });
    }

    //手机号数字正则匹配
    public static boolean checkIsPhone(String data) {
        String str1 = "^\\d{11}$";
        Pattern p = Pattern.compile(str1);
        Matcher m = p.matcher(data);
        return m.matches();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
