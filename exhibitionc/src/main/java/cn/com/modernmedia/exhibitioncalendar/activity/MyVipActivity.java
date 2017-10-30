package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.BaseActivity;
import cn.com.modernmedia.corelib.db.DataHelper;
import cn.com.modernmedia.corelib.listener.FetchDataListener;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.widget.NoScrollGridView;
import cn.com.modernmedia.corelib.widget.RoundImageView;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.adapter.VipIconAdapter;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.ProductListModel;
import cn.com.modernmedia.exhibitioncalendar.model.ProductListModel.ProductModel;

/**
 * 我的vip页面
 * Created by Eva. on 2017/10/27.
 */

public class MyVipActivity extends BaseActivity {

    private List<ProductModel> productModels = new ArrayList<>(), vip1Datas = new ArrayList<>(), vip2Datas = new ArrayList<>();
    private NoScrollGridView vip1GridView, vip2GridView;
    private VipIconAdapter vip1Adapter, vip2Adapter;
    private RadioGroup radioGroup;
    private RadioButton vip1B, vip2B;
    private LinearLayout container;
    private ProductModel payModel;// 选中的支付套餐
    private TextView priceText, deadLine, nickname;
    private RoundImageView avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vip);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        setUserInfo();
    }

    private void setUserInfo() {
        if (DataHelper.getUserLoginInfo(this) != null) {
            Tools.setAvatar(this, DataHelper.getUserLoginInfo(this).getAvatar(), avatar);
            nickname.setText(DataHelper.getUserLoginInfo(this).getNickName());
        }
    }

    private void initView() {
        findViewById(R.id.vip_back).setOnClickListener(this);
        priceText = (TextView) findViewById(R.id.vip_price);
        avatar = (RoundImageView) findViewById(R.id.avatar);
        nickname = (TextView) findViewById(R.id.nickname);
        avatar.setOnClickListener(this);
        findViewById(R.id.go_pay).setOnClickListener(this);
        vip1GridView = new NoScrollGridView(this);
        vip2GridView = new NoScrollGridView(this);
        container = (LinearLayout) findViewById(R.id.vip_layout);
        vip1Adapter = new VipIconAdapter(this, vip1Datas);
        vip2Adapter = new VipIconAdapter(this, vip2Datas);
        vip1GridView.setAdapter(vip1Adapter);
        vip2GridView.setAdapter(vip2Adapter);
        vip1GridView.setNumColumns(3);
        vip2GridView.setNumColumns(3);

        radioGroup = (RadioGroup) findViewById(R.id.index_special_rg);
        vip1B = (RadioButton) findViewById(R.id.vip_type1);
        vip2B = (RadioButton) findViewById(R.id.vip_type2);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.vip_type1) {

                    handler.sendEmptyMessage(1);
                } else if (checkedId == R.id.vip_type2) {
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }

    private void initData() {
        ApiController.getInstance(this).getProducts(new FetchDataListener() {
            @Override
            public void fetchData(boolean isSuccess, String data, boolean fromHttp) {
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        ProductListModel p = ProductListModel.parseProductListModel(jsonObject);
                        if (p != null) {
                            productModels.clear();
                            productModels.addAll(p.getList());

                            handler.sendEmptyMessage(0);
                        }


                    } catch (JSONException e) {

                    }

                }
            }
        });

    }

    private void showPrice(ProductModel p) {

        payModel = p;
        priceText.setText(payModel.getSalesPrice());
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (productModels.size() > 0) {
                        showPrice(productModels.get(0));
                        vip1B.setText(productModels.get(0).getGoodName());
                        Drawable dd = getResources().getDrawable(R.mipmap.app61_vip_1);
                        vip1B.setCompoundDrawables(dd, null, null, null);
                        vip1B.setVisibility(View.VISIBLE);
                        vip1Datas.clear();
                        vip1Datas.addAll(productModels.get(0).getChildList());
                        if (productModels.size() > 1) {
                            vip2B.setVisibility(View.VISIBLE);
                            vip2B.setText(productModels.get(1).getGoodName());
                            Drawable dd2 = getResources().getDrawable(R.mipmap.app61_vip_2);
                            vip2B.setCompoundDrawables(dd2, null, null, null);
                            vip2Datas.clear();
                            vip2Datas.addAll(productModels.get(1).getChildList());
                        } else {
                            vip2B.setVisibility(View.GONE);
                        }
                    } else {
                        vip1B.setVisibility(View.GONE);
                        vip2B.setVisibility(View.GONE);
                    }

                    handler.sendEmptyMessage(1);
                    break;

                case 1:
                    if (productModels.size() > 0) {
                        container.removeAllViews();
                        vip1Adapter.notifyDataSetChanged();
                        container.addView(vip1GridView);
                        showPrice(productModels.get(0));
                    }
                    break;
                case 2:
                    if (productModels.size() > 1) {
                        container.removeAllViews();
                        vip2Adapter.notifyDataSetChanged();
                        container.addView(vip2GridView);
                        showPrice(productModels.get(1));
                    }
                    break;

            }
        }
    };


    private void goPay(ProductModel productModel) {
        Intent i = new Intent(MyVipActivity.this, PayActivity.class);
        i.putExtra("pay_product", productModel);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vip_back:
                finish();
                break;
            case R.id.go_pay:
                if (isLogin()) {
                    goPay(payModel);
                }
                break;
            case R.id.avatar:
                if (isLogin()) {
                    startActivity(new Intent(MyVipActivity.this, UserCenterActivity.class));
                }
                break;
        }

    }

    private boolean isLogin() {
        if (DataHelper.getUserLoginInfo(this) == null) {
            startActivity(new Intent(MyVipActivity.this, LoginActivity.class));
            return false;
        } else return true;
    }
}
