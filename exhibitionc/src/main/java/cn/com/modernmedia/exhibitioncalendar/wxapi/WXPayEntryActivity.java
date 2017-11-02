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
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchDataListener;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;

/**
 * 微信支付回调activity
 *
 * @author lusiyuan
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private ApiController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = ApiController.getInstance(this);
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
                controller.notifyServer(ApiController.SUCCESS, ApiController.NEW_WEIXIN_KEY, new FetchDataListener() {
                    @Override
                    public void fetchData(boolean isSuccess, String data, boolean fromHttp) {
                        if (isSuccess) {
                            DataHelper.clearOrder(WXPayEntryActivity.this, ApiController.NEW_WEIXIN_KEY);
                            controller.saveLevel(data);
                        }
                    }
                });
                //跳转交易详情界面
                goToPayResult(resp.errCode);

            } else if (resp.errCode == -2) {// 取消支付
                controller.notifyServer(ApiController.CANCEL, ApiController.NEW_WEIXIN_KEY);
                goToPayResult(resp.errCode);
            } else {
                controller.notifyServer(ApiController.ERROR, ApiController.NEW_WEIXIN_KEY);
                goToPayResult(resp.errCode);
            }

        }
    }

    private void goToPayResult(int code) {
        MyApplication.weixinPayStatus = code;
        WXPayEntryActivity.this.finish();
    }


}