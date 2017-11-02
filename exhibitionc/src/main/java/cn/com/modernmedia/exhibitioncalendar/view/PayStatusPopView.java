package cn.com.modernmedia.exhibitioncalendar.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.activity.PayActivity;
import cn.com.modernmedia.exhibitioncalendar.model.ProductListModel.ProductModel;

/**
 * 支付状态
 * Created by Eva. on 2017/11/2.
 */

public class PayStatusPopView {

    private Context mContext;
    private PopupWindow window;
    private View view;
    private ImageView payResultImg;
    private TextView payResult, payMoney, payStyle, payProduct, payOutId, payTime;
    private ProductModel productModel;
    private int type, payStatus;
    private String time, outNo;


    public PayStatusPopView(Context context, ProductModel productModel, int type, String time, int payStatus, String outNo) {
        this.mContext = context;
        this.productModel = productModel;
        this.type = type;
        this.time = time;
        this.outNo = outNo;
        this.payStatus = payStatus;
        init();

    }


    public boolean ifShow() {
        return window.isShowing();
    }

    public void show() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                window.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

    }

    private void init() {
        view = LayoutInflater.from(mContext).inflate(R.layout.view_pay_status, null);
        window = new PopupWindow(view, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        window.setFocusable(true);
        window.setAnimationStyle(R.style.fetch_image_popup_anim);
        window.update();
        window.setBackgroundDrawable(new BitmapDrawable());
        view.findViewById(R.id.pay_status_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                if (mContext instanceof PayActivity){
                    ((PayActivity)mContext).finish();
                }
            }
        });
        payResultImg = (ImageView) view.findViewById(R.id.vip_paycallback_img);//交易结果图片
        payResult = (TextView) view.findViewById(R.id.vip_paycallback_result);//交易结果
        payMoney = (TextView) view.findViewById(R.id.vip_paycallback_money);//交易金额
        payStyle = (TextView) view.findViewById(R.id.vip_paycallback_style);//付款方式
        payProduct = (TextView) view.findViewById(R.id.vip_paycallback_product);//商品名称
        payOutId = (TextView) view.findViewById(R.id.vip_paycallback_id);//订单编号
        payTime = (TextView) view.findViewById(R.id.vip_paycallback_time);//创建时间

        if (type == PayActivity.WEIXIN_PAY) {
            payStyle.setText("微信支付");
        } else {
            payStyle.setText("支付宝支付");
        }
        payProduct.setText(productModel.getGoodName());
        payOutId.setText(outNo);
        payTime.setText(time);
        Double mm = Double.valueOf(productModel.getGoodPrice());
        payMoney.setText(String.format("%.2f", mm/100));
        //0为支付成功，-2取消支付，-1出错 -3 支付结果确认中
        if (payStatus == 0) {
            showPaySuccess();
        } else if (payStatus == -1) {
            showPayFail();
        } else if (payStatus == -2) {
            showPayCancel();
        }
    }

    private void showPaySuccess() {
        payResultImg.setImageResource(R.drawable.vippaysuccess);
        payResult.setText(mContext.getResources().getString(R.string.vip_pay_success));
    }

    private void showPayFail() {
        payResultImg.setImageResource(R.drawable.vippayfail);
        payResult.setText(mContext.getResources().getString(R.string.vip_pay_fail));
    }

    private void showPayCancel() {
        payResultImg.setImageResource(R.drawable.vippayfail);
        payResult.setText(mContext.getResources().getString(R.string.vip_pay_cancel));
    }
}
