package cn.com.modernmedia.exhibitioncalendar.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.com.modernmedia.corelib.CommonApplication;

/**
 * 微信支付回调activity
 *
 * @author lusiyuan
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, CommonApplication.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(final BaseResp resp) {
        if (resp != null && resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.e("微信支付code:", resp.errCode + "" + resp.errStr);
            // resp.errCode == 0为支付成功，为-2未取消支付，-1出错
            if (resp.errCode == 0) {
                // 存储支付成功后用户的level
                //                            SlateDataHelper.setIssueLevel(WXPayEntryActivity.this, "1");
                //跳转交易详情界面
                goToPayResult(resp.errCode);

            } else if (resp.errCode == -2) {// 取消支付
                goToPayResult(resp.errCode);
            } else {
                goToPayResult(resp.errCode);
            }

        }
    }

    private void goToPayResult(int code) {
        //        VipProductPayActivity.payintent(this,"微信",code+"");
        WXPayEntryActivity.this.finish();
    }


}