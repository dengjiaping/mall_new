package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101912 on 2017/9/2.
 */

public class OrderListResponse extends BaseBean<OrderListResponse>{


    /**
     * channelName : 即有商城
     * pageNum : 1
     * pageSize : 10
     * skuInfo : [{"downPayment":24210,"mobile":"13563987556","monthPayment":24210,"name":"努比亚(nubia)【4+64GB】Z17mini 黑金色 移动联通电信4G手机 双卡双待","orderNo":"JD0000002","orderType":1,"payPrice":24210,"periods":24,"quantity":1,"remainingTime":10000000,"salePrice":199900,"skuCode":"4538000","src":"https://img13.360buyimg.com/n1/s450x450_jfs/t4558/150/3164446244/101650/34a04d56/58f6ceb4N64cd94bd.jpg","srcIp":"http://10.10.11.139","status":0,"timeLeft":"1天30分"},{"downPayment":24210,"mobile":"13563987556","monthPayment":24210,"name":"努比亚(nubia)【4+64GB】Z17mini 黑金色 移动联通电信4G手机 双卡双待","orderNo":"JD0000002","orderType":1,"payPrice":24210,"periods":24,"quantity":1,"remainingTime":10000000,"salePrice":199900,"skuCode":"4538000","src":"https://img13.360buyimg.com/n1/s450x450_jfs/t4558/150/3164446244/101650/34a04d56/58f6ceb4N64cd94bd.jpg","srcIp":"http://10.10.11.139","status":0,"timeLeft":"1天30分"},{"downPayment":24210,"mobile":"13563987556","monthPayment":24210,"name":"努比亚(nubia)【4+64GB】Z17mini 黑金色 移动联通电信4G手机 双卡双待","orderNo":"JD0000002","orderType":1,"payPrice":24210,"periods":24,"quantity":1,"remainingTime":10000000,"salePrice":199900,"skuCode":"4538000","src":"https://img13.360buyimg.com/n1/s450x450_jfs/t4558/150/3164446244/101650/34a04d56/58f6ceb4N64cd94bd.jpg","srcIp":"http://10.10.11.139","status":0,"timeLeft":"1天30分"}]
     */

    public String channelName;
    public int pageNum;
    public int pageSize;
    public List<SkuInfoBean> skuInfo;

    public static class SkuInfoBean {
        /**
         * downPayment : 24210
         * mobile : 13563987556
         * monthPayment : 24210
         * name : 努比亚(nubia)【4+64GB】Z17mini 黑金色 移动联通电信4G手机 双卡双待
         * orderNo : JD0000002
         * orderType : 1
         * payPrice : 24210
         * periods : 24
         * quantity : 1
         * remainingTime : 10000000
         * salePrice : 199900
         * skuCode : 4538000
         * src : https://img13.360buyimg.com/n1/s450x450_jfs/t4558/150/3164446244/101650/34a04d56/58f6ceb4N64cd94bd.jpg
         * srcIp : http://10.10.11.139
         * status : 0
         * timeLeft : 1天30分
         */

        public String downPayment;
        public String mobile;
        public String monthPayment;
        public String name;
        public String orderNo;
        public int orderType;
        public String payPrice;
        public int periods;
        public int quantity;
        public String remainingTime;
        public String salePrice;
        public String skuCode;
        public String src;
        public String srcIp;
        public int status;
        public String timeLeft;
    }
}
