package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 101912 on 2017/9/5.
 */

public class OrderDetailResponse extends BaseBean<OrderDetailResponse> {

    /**
     * courtesyCardJo : {"courtesyCardId":1,"courtesyCardName":"满699减30元全品类","isSelected":1}
     * downPayment : 1000
     * giftPackPrice : 1000
     * monthPayment : 24210
     * orderNo : JD0000002
     * orderType : 1
     * payType : 1
     * receiverJo : {"address":"地铁大厦17楼","city":"深圳市","county":"福田区","mobile":"18118751982","province":"广东省","receiverName":"张超"}
     * rechargeDenomination : 50元/50m
     * rechargePhone : 13540602715
     * remainingTime : 1000
     * selDownPaymentRate : 10
     * selStagingNumberRate : 12
     * skuInfo : {"name":"努比亚(nubia)【4+64GB】Z17mini 黑金色 移动联通电信4G手机 双卡双待","quantity":1,"salePrice":199900,"skuCode":"4538000","src":"https://img13.360buyimg.com/n1/s450x450_jfs/t4558/150/3164446244/101650/34a04d56/58f6ceb4N64cd94bd.jpg","srcIp":"192.168.1.102","totalPrice":1999000}
     * status : 1
     * timeLeft : 2天22小时
     * totalPrice : 10000001
     * userComments : 尽快发货
     */

    public CourtesyCardJoBean courtesyCardJo;
    public String downPayment;
    public int giftPackPrice;
    public String monthPayment;
    public String orderNo;
    public int orderType;
    public int payType;
    public ReceiverJoBean receiverJo;
    public String rechargeDenomination;
    public String rechargePhone;
    public int remainingTime;
    public String selDownPaymentRate;
    public String selStagingNumberRate;
    public SkuInfoBean skuInfo;
    public int status;
    public String timeLeft;
    public String totalPrice;
    public String userComments;

    public static class CourtesyCardJoBean {
        /**
         * courtesyCardId : 1
         * courtesyCardName : 满699减30元全品类
         * isSelected : 1
         */

        public int courtesyCardId;
        public String courtesyCardName;
        public int isSelected;
    }

    public static class ReceiverJoBean {
        /**
         * address : 地铁大厦17楼
         * city : 深圳市
         * county : 福田区
         * mobile : 18118751982
         * province : 广东省
         * receiverName : 张超
         */

        public String address;
        public String city;
        public String county;
        public String mobile;
        public String province;
        public String receiverName;
    }

    public static class SkuInfoBean {
        /**
         * name : 努比亚(nubia)【4+64GB】Z17mini 黑金色 移动联通电信4G手机 双卡双待
         * quantity : 1
         * salePrice : 199900
         * skuCode : 4538000
         * src : https://img13.360buyimg.com/n1/s450x450_jfs/t4558/150/3164446244/101650/34a04d56/58f6ceb4N64cd94bd.jpg
         * srcIp : 192.168.1.102
         * totalPrice : 1999000
         */

        public String name;
        public String quantity;
        public String salePrice;
        public String skuCode;
        public String src;
        public String srcIp;
        public String totalPrice;
    }
}
