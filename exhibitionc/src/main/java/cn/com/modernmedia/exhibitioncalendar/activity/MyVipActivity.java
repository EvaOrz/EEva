package cn.com.modernmedia.exhibitioncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import cn.com.modernmedia.corelib.model.VipInfoModel;
import cn.com.modernmedia.corelib.util.Tools;
import cn.com.modernmedia.corelib.widget.RoundImageView;
import cn.com.modernmedia.exhibitioncalendar.MyApplication;
import cn.com.modernmedia.exhibitioncalendar.R;
import cn.com.modernmedia.exhibitioncalendar.adapter.VipIconAdapter;
import cn.com.modernmedia.exhibitioncalendar.api.ApiController;
import cn.com.modernmedia.exhibitioncalendar.model.ProductListModel;
import cn.com.modernmedia.exhibitioncalendar.model.ProductListModel.ProductModel;
import cn.com.modernmedia.exhibitioncalendar.util.UriParse;

/**
 * 我的vip页面
 * Created by Eva. on 2017/10/27.
 */

public class MyVipActivity extends BaseActivity {

    private List<ProductModel> productModels = new ArrayList<>();
    private List<ProductListModel> vip1Datas = new ArrayList<>(), vip2Datas = new ArrayList<>();
    private ListView vip1View, vip2View;
    private VipIconAdapter vip1Adapter, vip2Adapter;
    private RadioGroup radioGroup;
    private RadioButton vip1B, vip2B;
    private LinearLayout container;
    private ImageView vip_icon;// 用户vip 标志
    private ProductModel payModel;// 选中的支付套餐
    private TextView priceText, deadLine, nickname, goPay;
    private RoundImageView avatar;

    private VipInfoModel vipInfoModel;
    private boolean isGoLogin = false, isGoDuihuan = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vip);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserInfo();
        // 初始化
        vipInfoModel = DataHelper.getVipInfo(this);
        handler.sendEmptyMessage(3);// 显示对应套餐
        handler.sendEmptyMessage(4);// 显示有效期
        // 付费状态变化之后再初始化
        if (MyApplication.loginStatusChange == 4) {
            getIssueLevel();
        }
    }

    /**
     * 取用户的付费权限
     */
    private void getIssueLevel() {
        ApiController.getInstance(this).getUserPermission(new FetchDataListener() {
            @Override
            public void fetchData(boolean isSuccess, String data, boolean fromHttp) {
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject != null) {
                            vipInfoModel = VipInfoModel.parseVipInfoModel(jsonObject);
                            DataHelper.saveVipInfo(MyVipActivity.this, vipInfoModel);
                            handler.sendEmptyMessage(3);// 显示对应套餐
                            handler.sendEmptyMessage(4);// 显示有效期
                        }
                    } catch (JSONException e) {

                    }
                }
            }
        });


    }


    private void setUserInfo() {
        if (DataHelper.getUserLoginInfo(this) != null) {
            Tools.setAvatar(this, DataHelper.getUserLoginInfo(this).getAvatar(), avatar);
            nickname.setText(DataHelper.getUserLoginInfo(this).getNickName());
        } else {
            avatar.setImageResource(R.mipmap.avatar_bg);
            nickname.setText("未登录");
        }
    }

    private void initView() {
        findViewById(R.id.vip_back).setOnClickListener(this);
        priceText = (TextView) findViewById(R.id.vip_price);
        avatar = (RoundImageView) findViewById(R.id.avatar);
        nickname = (TextView) findViewById(R.id.nickname);
        deadLine = (TextView) findViewById(R.id.deadline);
        findViewById(R.id.vip_xieyi).setOnClickListener(this);
        findViewById(R.id.vip_kefu).setOnClickListener(this);
        findViewById(R.id.vip_duihuan).setOnClickListener(this);
        avatar.setOnClickListener(this);
        goPay = (TextView) findViewById(R.id.go_pay);
        goPay.setOnClickListener(this);
        vip_icon = (ImageView) findViewById(R.id.vip_icon);
        vip1View = new ListView(this);
        vip1View.setDivider(null);
        vip2View = new ListView(this);
        vip2View.setDivider(null);
        container = (LinearLayout) findViewById(R.id.vip_layout);
        vip1Adapter = new VipIconAdapter(this, vip1Datas);
        vip2Adapter = new VipIconAdapter(this, vip2Datas);
        vip1View.setAdapter(vip1Adapter);
        vip2View.setAdapter(vip2Adapter);

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
                            handler.sendEmptyMessage(3);
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

    private List<ProductListModel> data(List<ProductModel> indexList) {
        List<ProductListModel> ll = new ArrayList<>();

        for (int i = 0; i < indexList.size(); i += 3) {
            ProductListModel p = new ProductListModel();
            List<ProductModel> l = new ArrayList<>();
            l.add(indexList.get(i));
            if (indexList.size() > i + 1) {
                l.add(indexList.get(i + 1));
            } else l.add(new ProductModel());
            if (indexList.size() > i + 2) {
                l.add(indexList.get(i + 2));
            } else l.add(new ProductModel());
            p.setList(l);
            ll.add(p);
        }

        return ll;

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (productModels.size() > 0) {
                        vip1B.setText(productModels.get(0).getGoodName());
                        vip1Datas.clear();
                        vip1Datas.addAll(data(productModels.get(0).getChildList()));
                        if (productModels.size() > 1) {
                            vip2B.setText(productModels.get(1).getGoodName());
                            vip2Datas.clear();
                            vip2Datas.addAll(data(productModels.get(1).getChildList()));
                        }
                    }
                case 1:// 选中第一tab
                    if (productModels.size() > 0) {
                        container.removeAllViews();
                        vip1Adapter.notifyDataSetChanged();
                        container.addView(vip1View, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        showPrice(productModels.get(0));
                    }
                    break;
                case 2:// 选中第二tab
                    if (productModels.size() > 1) {
                        container.removeAllViews();
                        vip2Adapter.notifyDataSetChanged();
                        container.addView(vip2View);
                        showPrice(productModels.get(1));
                    }
                    break;

                case 3:// 显示vip 对应套餐
                    if (vipInfoModel == null || vipInfoModel.getLevel() == 0 || productModels.size() < 2) {
                        vip1B.setVisibility(View.VISIBLE);
                        vip2B.setVisibility(View.VISIBLE);
                        handler.sendEmptyMessage(1);
                    } else {
                        if (vipInfoModel.getGoods_id().equals(productModels.get(0).getPid())) {
                            vip1B.setVisibility(View.VISIBLE);
                            vip2B.setVisibility(View.GONE);
                            handler.sendEmptyMessage(1);
                        } else if (vipInfoModel.getGoods_id().equals(productModels.get(1).getPid())) {
                            vip1B.setVisibility(View.GONE);
                            vip2B.setVisibility(View.VISIBLE);
                            handler.sendEmptyMessage(2);
                        }
                    }
                    break;
                case 4:// 显示会员有效期
                    if (!TextUtils.isEmpty(vipInfoModel.getVip_endtime())) {
                        deadLine.setVisibility(View.VISIBLE);
                        deadLine.setText("会员有效期：" + Tools.format(Long.valueOf(vipInfoModel.getVip_endtime()) * 1000, "yyyy.MM.dd"));
                    } else {
                        deadLine.setVisibility(View.GONE);
                    }
                    if (vipInfoModel != null) {
                        if (vipInfoModel.getLevel() == 0) {
                            goPay.setText(R.string.my_vip_btn1);
                        } else {
                            goPay.setText(R.string.my_vip_btn2);
                        }
                        if (!TextUtils.isEmpty(vipInfoModel.getGoods_id())) {
                            vip_icon.setVisibility(View.VISIBLE);
                            Tools.setImage(vip_icon, vipInfoModel.getGoods_id());
                        }
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
            case R.id.vip_xieyi:// 服务协议
                Intent intent = new Intent(MyVipActivity.this, AboutActivity.class);
                intent.putExtra("browser_type", 3);
                startActivity(intent);
                break;
            case R.id.vip_kefu:// 客服链接
                UriParse.doLinkWeb(MyVipActivity.this, "https://artcalendar.bbwc.cn/html/artCalendar/html/crm.html");
                break;

            case R.id.vip_duihuan:// 激活兑换码
                Intent ii = new Intent(MyVipActivity.this, AboutActivity.class);
                ii.putExtra("browser_type", 4);
                startActivity(ii);
                break;
        }

    }

    /**
     * 是否登录
     *
     * @return
     */
    private boolean isLogin() {
        if (DataHelper.getUserLoginInfo(this) == null) {
            isGoLogin = true;
            startActivity(new Intent(MyVipActivity.this, LoginActivity.class));
            return false;
        } else return true;
    }


}
