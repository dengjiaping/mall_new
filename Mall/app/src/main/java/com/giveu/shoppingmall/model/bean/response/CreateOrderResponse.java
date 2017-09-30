package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 524202 on 2017/9/13.
 */

public class CreateOrderResponse extends BaseBean<CreateOrderResponse> {

    /**
     * avsList : [{"serviceId":7,"serviceName":"\u2018人身意外保险保障服务\u2019","servicePrice":13,"serviceUrl":"http://wx.dafycredit.cn/h5/sales/index.html#/XFFQ/XFFQInsuredNotice"}]
     * cardList : [{"id":74064,"name":"测试内容0x3l","price":"测试内容840n"}]
     * fsList : [{"annuity":606,"idProduct":31311,"paymentNum":3},{"annuity":319,"idProduct":31312,"paymentNum":6},{"annuity":224,"idProduct":31313,"paymentNum":9},{"annuity":176,"idProduct":31314,"paymentNum":12}]
     * initList : [{"id":0,"name":"零首付","price":"0"},{"id":5,"name":"5%","price":"41.45"},{"id":10,"name":"10%","price":"82.9"},{"id":20,"name":"20%","price":"165.8"},{"id":30,"name":"30%","price":"248.7"},{"id":40,"name":"40%","price":"331.6"},{"id":50,"name":"50%","price":"414.5"}]
     * install : true
     * installList : []
     * loadPlan : {"initPay":"189.9","paymentList":[{"monthPay":"319.0","paymentNum":1,"repayDate":"2017/09/28"},{"monthPay":"319.0","paymentNum":2,"repayDate":"2017/10/28"},{"monthPay":"319.0","paymentNum":3,"repayDate":"2017/11/28"},{"monthPay":"319.0","paymentNum":4,"repayDate":"2017/12/28"},{"monthPay":"319.0","paymentNum":5,"repayDate":"2018/01/28"},{"monthPay":"319.0","paymentNum":6,"repayDate":"2018/02/28"}],"paymentNum":6}
     * payPrice : 829.0
     * receiverJo : {"address":"福中地铁大厦20楼2018室","city":"深圳市","cityCode":"1607","custName":"翁生","phone":"13530402714","province":"广东","provinceCode":"19","region":"福田区","regionCode":"3639","street":"","streetCode":"测试内容2v87"}
     * reserving : true
     * reservingList : [{"date":"2017-09-13","id":2,"week":"星期三"},{"date":"2017-09-13","id":3,"week":"星期三"},{"date":"2017-09-13","id":4,"week":"星期三"},{"date":"2017-09-13","id":5,"week":"星期三"},{"date":"2017-09-13","id":6,"week":"星期三"},{"date":"2017-09-13","id":7,"week":"星期三"},{"date":"2017-09-13","id":8,"week":"星期三"},{"date":"2017-09-13","id":9,"week":"星期三"},{"date":"2017-09-13","id":10,"week":"星期三"},{"date":"2017-09-13","id":11,"week":"星期三"}]
     * skuInfo : {"name":"海尔（Haier） XQB55-M1269 5.5公斤全自动波轮洗衣机 3年质保","quantity":1,"salePrice":"829.0","skuCode":"K00002727","src":"group1/M07/64/81/CgsLw1mSa1SAN4-3AAH3At2dryQ762.jpg","srcIp":"http://10.10.11.136","totalPrice":"829.0"}
     */

    public boolean install;
    public LoadPlanBean loadPlan;
    public String payPrice;
    public ReceiverJoBean receiverJo;
    public boolean reserving;
    public SkuInfoBean skuInfo;
    public List<AvsListBean> avsList;
    public List<CardListBean> cardList;
    public List<FsListBean> fsList;
    public List<InitListBean> initList;
    public List<ReservingListBean> installList;
    public List<ReservingListBean> reservingList;


    public static class LoadPlanBean {
        /**
         * initPay : 189.9
         * paymentList : [{"monthPay":"319.0","paymentNum":1,"repayDate":"2017/09/28"},{"monthPay":"319.0","paymentNum":2,"repayDate":"2017/10/28"},{"monthPay":"319.0","paymentNum":3,"repayDate":"2017/11/28"},{"monthPay":"319.0","paymentNum":4,"repayDate":"2017/12/28"},{"monthPay":"319.0","paymentNum":5,"repayDate":"2018/01/28"},{"monthPay":"319.0","paymentNum":6,"repayDate":"2018/02/28"}]
         * paymentNum : 6
         */

        public String initPay;
        public int paymentNum;
        public List<PaymentListBean> paymentList;

        public static class PaymentListBean {
            /**
             * monthPay : 319.0
             * paymentNum : 1   分期顺序
             * repayDate : 2017/09/28
             */

            public String monthPay;
            public int paymentNum;
            public String repayDate;

        }
    }

    public static class ReceiverJoBean {
        /**
         * address : 福中地铁大厦20楼2018室
         * city : 深圳市
         * cityCode : 1607
         * custName : 翁生
         * phone : 13530402714
         * province : 广东
         * provinceCode : 19
         * region : 福田区
         * regionCode : 3639
         * street :
         * streetCode : 测试内容2v87
         */

        public String address;
        public String city;
        public String cityCode;
        public String custName;
        public String phone;
        public String province;
        public String provinceCode;
        public String region;
        public String regionCode;
        public String street;
        public String streetCode;

    }

    public static class SkuInfoBean {
        /**
         * name : 海尔（Haier） XQB55-M1269 5.5公斤全自动波轮洗衣机 3年质保
         * quantity : 1
         * salePrice : 829.0
         * skuCode : K00002727
         * src : group1/M07/64/81/CgsLw1mSa1SAN4-3AAH3At2dryQ762.jpg
         * srcIp : http://10.10.11.136
         * totalPrice : 829.0
         */

        public String name;
        public int quantity;
        public String salePrice;
        public String skuCode;
        public String src;
        public String srcIp;
        public String totalPrice;

    }

    public static class AvsListBean {
        /**
         * serviceId : 7
         * serviceName : ‘人身意外保险保障服务’
         * servicePrice : 13
         * serviceUrl : http://wx.dafycredit.cn/h5/sales/index.html#/XFFQ/XFFQInsuredNotice
         */

        public long serviceId;
        public String serviceName;
        public String servicePrice;
        public String serviceUrl;

    }

    public static class CardListBean {
        /**
         * id : 74064 优惠券id
         * name : 优惠券名称
         * price : 优惠价格
         */

        public long id;
        public String name;
        public String price;

    }

    public static class FsListBean {
        /**
         * 金融分期
         * annuity : 606 月供
         * idProduct : 31311 产品id
         * paymentNum : 3 分期数
         */

        public String annuity;
        public long idProduct;
        public int paymentNum;
    }

    public static class InitListBean {
        /**
         * 首付信息
         * id : 0
         * name : 零首付
         * price : 0
         */

        public int id;
        public String name;
        public String price;
    }

    public static class ReservingListBean {
        /**
         * date : 2017-09-13
         * id : 2
         * week : 星期三
         */

        public String date;
        public int id;
        public String week;

    }

}
