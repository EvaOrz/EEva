package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.CommonApplication;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.http.HttpsController;
import cn.com.modernmedia.corelib.listener.FetchDataListener;
import cn.com.modernmedia.corelib.model.UserModel;
import cn.com.modernmedia.corelib.pay.Base64;
import cn.com.modernmedia.corelib.pay.PayResult;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.api.UrlMaker;
import cn.com.modernmedia.exhibitioncalendar.model.ProductListModel.ProductModel;

/**
 * 支付页面
 * Eva. on 2017/10/25.
 */

public class PayActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    protected static final int REQ_CODE = 102;
    private static final int WEIXIN_PAY = 1;
    private static final int ZHIFUBAO_PAY = 2;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    protected static final int ADDRESS = 3;
    private static final int PID = 4;

    // 微信参数
    public static final String APP_KEY = "b2eujfhVFiCRQhbnmYcdkGPWvv361616";// PaySignKey（API密钥）
    private PayReq weixinReq;
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    private boolean gotoPay = false;// onresume页面是否去pay
    private ApiController controller;
    private UserModel userModel;
    private JSONObject object;
    private int pay_style = 0;// 支付方式
    private ProductModel product = new ProductModel();
    private static String outNo;// 第三方交易号

    private RadioButton weixinPay, zhifubaoPay;
    private TextView payMoney, payName, payTime, payAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        if (getIntent().getSerializableExtra("pay_product") == null || !(getIntent().getSerializableExtra("pay_product") instanceof ProductModel)) {
            finish();
            return;
        }
        product = (ProductModel) getIntent().getSerializableExtra("pay_product");
        controller = ApiController.getInstance(this);
        initView();
        weixinReq = new PayReq();// 微信req请求
    }

    private void initView() {
        payMoney = (TextView) findViewById(R.id.pay_money);
        payName = (TextView) findViewById(R.id.pay_name);
        payTime = (TextView) findViewById(R.id.pay_time);
        payAddress = (TextView) findViewById(R.id.vip_address_detail);
        findViewById(R.id.peisong_style).setOnClickListener(this);
        findViewById(R.id.pay_back).setOnClickListener(this);
        findViewById(R.id.pay_btn).setOnClickListener(this);
        weixinPay = (RadioButton) findViewById(R.id.vip_pay_weixin);
        zhifubaoPay = (RadioButton) findViewById(R.id.vip_pay_zhifubao);
        weixinPay.setChecked(true);//默认微信支付
        pay_style = WEIXIN_PAY;
        weixinPay.setOnCheckedChangeListener(this);
        zhifubaoPay.setOnCheckedChangeListener(this);
    }

    private void setChecked(int ii){
        if (ii == 0){
            pay_style = WEIXIN_PAY;
            weixinPay.setChecked(true);
            zhifubaoPay.setChecked(false);
        }else if (ii == 1){
            weixinPay.setChecked(false);
            zhifubaoPay.setChecked(true);
            pay_style = ZHIFUBAO_PAY;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.vip_pay_weixin:
                if (isChecked){
                    setChecked(0);
                }else setChecked(1);
                break;
            case R.id.vip_pay_zhifubao:

                if (isChecked){
                    setChecked(1);
                }else setChecked(0);
                break;

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_back:
                finish();
                break;

            case R.id.peisong_style:
                Intent intent = new Intent(this, PostUserOrderInfoActivity.class);
                startActivityForResult(intent, REQ_CODE);
                break;
            case R.id.pay_btn:
                if (pay_style == WEIXIN_PAY) {
                    boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
                    if (!sIsWXAppInstalledAndSupported) {
                        showLoadingDialog(false);
                        Tools.showToast(this, R.string.no_weixin);
                        return;
                    }
                    dopay(WEIXIN_PAY);
                } else if (pay_style == ZHIFUBAO_PAY) {
                    check();//查询终端设备是否存在支付宝认证账户
                    dopay(ZHIFUBAO_PAY);
                }
                break;
        }
    }

    private void initData() {
        object = new JSONObject();
        try {
            object.put("uid", userModel.getUid());
            object.put("usertoken", userModel.getToken());
            object.put("pid", product.getPid());
            object.put("appid", CommonApplication.APP_ID);
            object.put("marketkey", CommonApplication.CHANNEL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        payName.setText(product.getGoodName());
        payMoney.setText(formatPrice(product.getGoodPrice()));


    }

    public static String formatPrice(int msg) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(Double.valueOf(msg) / 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userModel = DataHelper.getUserLoginInfo(this);
        if (userModel != null && !gotoPay) {
            initData();
        }
        payTime.setText(getTime("yyyy-MM-dd HH:mm:ss"));//更新下单时间

    }

    /**
     * 9000 订单支付成功
     * 8000 正在处理中
     * 4000 订单支付失败
     * 6001 用户中途取消
     * 6002 网络连接出错
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    Log.e("*******支付宝resultInfo", resultInfo);

                    final String resultStatus = payResult.getResultStatus();
                    Log.e("*******支付宝resultStatus", resultStatus);

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showLoadingDialog(true);
                        controller.notifyServer(ApiController.SUCCESS, ApiController.NEW_ALI_KEY, new FetchDataListener() {
                            @Override
                            public void fetchData(boolean isSuccess, String data, boolean fromHttp) {
                                if (isSuccess) {
                                    showLoadingDialog(false);
                                    Log.e("更新订单状态成功", "清除数据" + data);
                                    DataHelper.clearOrder(PayActivity.this, ApiController.NEW_ALI_KEY);
                                    controller.saveLevel(data);
                                    //跳转交易详情界面
                                    payintent(PayActivity.this, getString(R.string.zhifubaopay), resultStatus);
                                }
                            }
                        });
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        controller.notifyServer(ApiController.CANCEL, ApiController.NEW_ALI_KEY);
                        payintent(PayActivity.this, getString(R.string.zhifubaopay), resultStatus);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showToast("支付结果确认中");
                            controller.notifyServer(ApiController.PAYING, ApiController.NEW_ALI_KEY);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            controller.notifyServer(ApiController.ERROR, ApiController.NEW_ALI_KEY);
                            payintent(PayActivity.this, getString(R.string.zhifubaopay), resultStatus);
                        }
                        showLoadingDialog(false);
                    }
                    break;
                case SDK_CHECK_FLAG:
                    break;
                case ADDRESS:
                    payAddress.setVisibility(View.VISIBLE);
                    //                    vip_post_detail.setText(errorMsg.getRealname() + " " + errorMsg.getPhone());
                    payAddress.setText(userModel.getCity() + userModel.getAddress());
                    break;

            }
        }

    };

    /**
     * 获取订单信息
     *
     * @param type
     */
    private void doUpload(final int type) {
        try {


            Log.e("11", object.toString());
            HttpsController.getInstance(this).requestHttpPost(UrlMaker.newGetOrder(type), object.toString(), new FetchDataListener() {
                @Override
                public void fetchData(boolean isSuccess, String data, boolean fromHttp) {
                    showLoadingDialog(false);
                    if (isSuccess) {
                        try {
                            final JSONObject jsonObject = new JSONObject(data);
                            Log.e("获取订单", data);
                            if (jsonObject.optInt("error_no") == 0) {
                                JSONObject order = jsonObject.optJSONObject("data");
                                outNo = order.optString("oid");
                                JSONObject paydata = order.optJSONObject("paydata");
                                if (paydata != null) {
                                    gotoPay = true;// 跳去支付

                                    if (type == WEIXIN_PAY) {
                                        // 预支付订单
                                        weixinPay(paydata.optString("prepayid"));
                                        saveOrder(PayActivity.this, userModel, ApiController.NEW_WEIXIN_KEY, outNo, "", ApiController.PAYING, product, getString(R.string.weixin), getTime("yyyy-MM-dd HH:mm:ss"), userModel.getRealname(), userModel.getPhone(), userModel.getCity() + userModel.getAddress(), false);
                                        /**
                                         * 跳往支付结果页面，状态：订单处理中..
                                         */
                                        //                                        payintent(getString(R.string.weixinpay), "10");
                                    } else if (type == ZHIFUBAO_PAY) {
                                        // 签名
                                        String aliOrderInfo = new String(Base64.decode(paydata.optString("request_str")));
                                        Log.e("支付宝base64jiemi", aliOrderInfo);
                                        zhifubaoPay(aliOrderInfo);
                                        saveOrder(PayActivity.this, userModel, ApiController.NEW_ALI_KEY, outNo, "", ApiController.PAYING);
                                    }
                                } else {
                                    showToast(R.string.order_make_faild);
                                }
                            } else {
                                showToast(jsonObject.optString("error_desc"));
                            }
                        } catch (JSONException e) {
                            showToast(R.string.order_make_faild);
                        }
                    } else {
                        showToast(R.string.order_make_faild);
                    }
                }
            });
        } catch (Exception e) {
            showToast(R.string.order_make_faild);
            Log.e("上传订单错误信息", e.getMessage());
        }

    }

    /**
     * 调起支付
     *
     * @param type
     */
    public void dopay(final int type) {
        showLoadingDialog(true);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                doUpload(type);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 微信支付：预支付订单号
     *
     * @param prepareId
     */
    private void weixinPay(String prepareId) {
        genPayReq(prepareId);
        // 发起第一步
        msgApi.registerApp(CommonApplication.WEIXIN_APP_ID);
        msgApi.sendReq(weixinReq);
    }

    /**
     * 微信支付防重复随机数
     *
     * @return
     */
    private String genNonceStr() {
        Random random = new Random();
        return String.valueOf(random.nextInt(10000));
        // MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
        // .getBytes());
    }

    /**
     * 支付宝支付：订单信息
     */
    public void zhifubaoPay(String orderInfo) {
        // 构造PayTask 对象
        PayTask alipay = new PayTask(PayActivity.this);
        // 调用支付接口，获取支付结果
        String result = alipay.pay(orderInfo);

        Message msg = new Message();
        msg.what = SDK_PAY_FLAG;
        msg.obj = result;
        mHandler.sendMessage(msg);

    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check() {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(PayActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }

    private void genPayReq(String prepareId) {
        if (TextUtils.isEmpty(prepareId)) {
            showToast(R.string.order_make_faild);
            return;
        }
        weixinReq.appId = CommonApplication.WEIXIN_APP_ID;
        weixinReq.partnerId = CommonApplication.WEIXIN_SECRET;
        weixinReq.prepayId = prepareId;
        weixinReq.packageValue = "Sign=WXPay";
        weixinReq.nonceStr = genNonceStr();
        weixinReq.timeStamp = String.valueOf(genTimeStamp());

        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        keys.add("appid");
        keys.add("noncestr");
        keys.add("package");
        keys.add("partnerid");
        keys.add("prepayid");
        keys.add("timestamp");

        values.add(weixinReq.appId);
        values.add(weixinReq.nonceStr);
        values.add(weixinReq.packageValue);
        values.add(weixinReq.partnerId);
        values.add(weixinReq.prepayId);
        values.add(weixinReq.timeStamp);


        weixinReq.sign = genAppSign(keys, values);


    }

    /**
     * 微信app签名生成
     *
     * @return
     */
    private String genAppSign(List<String> keys, List<String> values) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            sb.append(keys.get(i));
            sb.append('=');
            sb.append(values.get(i));
            sb.append('&');
        }
        sb.append("key=");
        sb.append(APP_KEY);

        String appSign = cn.com.modernmedia.corelib.util.weixin.MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static String getTime(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(date);
        long time = System.currentTimeMillis();
        Date curDate = new Date(time);//获取当前时间
        return formatter.format(curDate);
    }

    public static void payintent(Context c, String style, String resultStatus) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_CODE == requestCode && resultCode == RESULT_OK) {
            if (data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                userModel = (UserModel) bundle.getSerializable("error");
                mHandler.sendEmptyMessage(ADDRESS);

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 存储微信支付订单vip
     *
     * @param user
     * @param type
     * @param oid
     * @param toid
     * @param status
     */
    public void saveOrder(Context context, UserModel user, String type, String oid, String toid, String status, ProductModel product, String payStyle, String time, String name, String phone, String address, Boolean postCard) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getUid());
            jsonObject.put("usertoken", user.getToken());
            jsonObject.put("oid", oid);// slate订单号
            jsonObject.put("toid", toid);// 第三方平台订单号
            jsonObject.put("status", status);
            jsonObject.put("product_name", product.getGoodName());
            jsonObject.put("product_price", product.getGoodPrice());
            jsonObject.put("pay_style", payStyle);
            jsonObject.put("time", time);
            jsonObject.put("name", name);
            jsonObject.put("phone", phone);
            jsonObject.put("address", address);
            jsonObject.put("postCard", postCard);
            DataHelper.setOrder(context, type, jsonObject.toString());
        } catch (JSONException e) {

        }
    }

    /**
     * 存储支付订单
     *
     * @param user
     * @param type
     * @param oid
     * @param toid
     * @param status
     */
    public void saveOrder(Context context, UserModel user, String type, String oid, String toid, String status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getUid());
            jsonObject.put("usertoken", user.getToken());
            jsonObject.put("oid", oid);// slate订单号
            jsonObject.put("toid", toid);// 第三方平台订单号
            jsonObject.put("status", status);
            DataHelper.setOrder(context, type, jsonObject.toString());
        } catch (JSONException e) {

        }
    }
}
