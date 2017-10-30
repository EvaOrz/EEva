package cn.com.modernmedia.exhibitioncalendar.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.modernmedia.corelib.model.Entry;

/**
 * 产品Model
 * Created by Eva. on 2017/10/27.
 */

public class ProductListModel extends Entry {
    private static final long serialVersionUID = 1L;
    private List<ProductModel> list = new ArrayList<>();

    public List<ProductModel> getList() {
        return list;
    }

    public void setList(List<ProductModel> list) {
        this.list = list;
    }

    public static ProductListModel parseProductListModel(JSONObject jsonObject) {
        ProductListModel productListModel = new ProductListModel();
        List<ProductModel> productListModels = new ArrayList<>();
        JSONArray jsonArray = jsonObject.optJSONArray("app61_vip");
        if (jsonArray == null) return null;
        for (int i = 0; i < jsonArray.length(); i++) {
            productListModels.add(ProductModel.parseProductModel( jsonArray.optJSONObject(i)));
        }
        productListModel.setList(productListModels);
        return productListModel;
    }

    public static class ProductModel extends Entry {
        private static final long serialVersionUID = 1L;
        private String pid = "";      //商品ID
        private String goodName = "";    //商品名称
        private int goodType;    //商品类型：订阅、消耗、永久
        private int goodPrice;       //商品价格：单位:分(人民币)
        private String salesPrice = "";   //显示价格。例如：1元/年
        private String goodDesc = "";        //商品描述
        private int goodNum;         //商品数量
        private String goodUnit = "";        //商品单位
        private String expireTime = "";  //有效期。商品类型为订阅时，表示有效截止时间点
        private String needAddress = ""; //是否必须填写配送信息 1需要，0不需要
        private String goodIcon1 = "";
        private String goodIcon2 = "";
        private int bright;// 0：不点亮 1：点亮

        private List<VipPayType> payTypeLists = new ArrayList<>();
        private List<ProductModel> childList = new ArrayList<>();


        public static ProductModel parseProductModel(JSONObject jsonObject) {
            ProductModel productModel = new ProductModel();
            productModel.setPid(jsonObject.optString("pid"));
            productModel.setGoodName(jsonObject.optString("good_name"));
            productModel.setGoodPrice(jsonObject.optInt("good_price"));
            productModel.setGoodType(jsonObject.optInt("good_type"));
            productModel.setGoodUnit(jsonObject.optString("good_unit"));
            productModel.setGoodNum(jsonObject.optInt("good_num"));
            productModel.setNeedAddress(jsonObject.optString("good_needaddress"));
            productModel.setGoodDesc(jsonObject.optString("good_desc"));
            productModel.setGoodIcon1(jsonObject.optString("good_icon_1"));
            productModel.setGoodIcon2(jsonObject.optString("good_icon_2"));
            productModel.setBright(jsonObject.optInt("bright"));
            productModel.setSalesPrice(jsonObject.optString("sales_price"));
            productModel.setPayTypeLists(parsePayType(jsonObject.optJSONArray("pay_list")));
            if (jsonObject.optJSONArray("child") != null)
                productModel.setChildList(parseProductModel(jsonObject.optJSONArray("child")));

            return productModel;
        }

        public static List<ProductModel> parseProductModel(JSONArray array) {
            List<ProductModel> list = new ArrayList<>();
            if (array == null || array.length() == 0) return list;
            for (int i = 0; i < array.length(); i++) {
                list.add(parseProductModel(array.optJSONObject(i)));
            }
            return list;
        }


        public static List<VipPayType> parsePayType(JSONArray array) {
            List<VipPayType> list = new ArrayList<>();
            if (array == null || array.length() == 0) return list;
            for (int i = 0; i < array.length(); i++) {
                list.add(VipPayType.parseVipPayType(array.optJSONObject(i)));
            }
            return list;
        }

        public int getBright() {
            return bright;
        }

        public void setBright(int bright) {
            this.bright = bright;
        }

        public String getGoodDesc() {
            return goodDesc;
        }

        public void setGoodDesc(String goodDesc) {
            this.goodDesc = goodDesc;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getGoodName() {
            return goodName;
        }

        public void setGoodName(String goodName) {
            this.goodName = goodName;
        }

        public int getGoodType() {
            return goodType;
        }

        public void setGoodType(int goodType) {
            this.goodType = goodType;
        }

        public int getGoodPrice() {
            return goodPrice;
        }

        public void setGoodPrice(int goodPrice) {
            this.goodPrice = goodPrice;
        }

        public String getSalesPrice() {
            return salesPrice;
        }

        public void setSalesPrice(String showPrice) {
            this.salesPrice = showPrice;
        }


        public int getGoodNum() {
            return goodNum;
        }

        public void setGoodNum(int goodNum) {
            this.goodNum = goodNum;
        }

        public String getGoodUnit() {
            return goodUnit;
        }

        public void setGoodUnit(String goodUnit) {
            this.goodUnit = goodUnit;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public String getNeedAddress() {
            return needAddress;
        }

        public void setNeedAddress(String needAddress) {
            this.needAddress = needAddress;
        }

        public String getGoodIcon1() {
            return goodIcon1;
        }

        public void setGoodIcon1(String goodIcon1) {
            this.goodIcon1 = goodIcon1;
        }

        public String getGoodIcon2() {
            return goodIcon2;
        }

        public void setGoodIcon2(String goodIcon2) {
            this.goodIcon2 = goodIcon2;
        }

        public List<VipPayType> getPayTypeLists() {
            return payTypeLists;
        }

        public void setPayTypeLists(List<VipPayType> payTypeLists) {
            this.payTypeLists = payTypeLists;
        }

        public List<ProductModel> getChildList() {
            return childList;
        }

        public void setChildList(List<ProductModel> childList) {
            this.childList = childList;
        }
    }

    public static class VipPayType extends Entry {
        private static final long serialVersionUID = 1L;
        private int payTypeId;           //支付方式ID
        private String payTypeName = "";         //支付方式名称
        private int isRecommend;         //是否为推荐支付方式
        private String recommendIconUrl = "";    //支付方式推送的ICON。http://s1.cdn.bbwc.cn/statics/images/pay/tuijian@3x.png

        public static VipPayType parseVipPayType(JSONObject jsonObject) {
            VipPayType vipPayType = new VipPayType();
            if (jsonObject == null) return vipPayType;
            vipPayType.setPayTypeId(jsonObject.optInt("pay_id"));
            vipPayType.setPayTypeName(jsonObject.optString("pay_name"));
            vipPayType.setRecommendIconUrl(jsonObject.optString("recommend_icon_url"));
            vipPayType.setIsRecommend(jsonObject.optInt("is_recommend"));
            return vipPayType;
        }

        public int getPayTypeId() {
            return payTypeId;
        }

        public void setPayTypeId(int payTypeId) {
            this.payTypeId = payTypeId;
        }

        public String getRecommendIconUrl() {
            return recommendIconUrl;
        }

        public void setRecommendIconUrl(String recommendIconUrl) {
            this.recommendIconUrl = recommendIconUrl;
        }

        public int getIsRecommend() {
            return isRecommend;
        }

        public void setIsRecommend(int isRecommend) {
            this.isRecommend = isRecommend;
        }

        public String getPayTypeName() {
            return payTypeName;
        }

        public void setPayTypeName(String payTypeName) {
            this.payTypeName = payTypeName;
        }
    }


}
