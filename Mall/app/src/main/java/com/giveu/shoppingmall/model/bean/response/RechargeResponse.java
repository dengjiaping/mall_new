package com.giveu.shoppingmall.model.bean.response;


import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101900 on 2017/1/20.
 */

public class RechargeResponse extends BaseBean<RechargeResponse> {


    /**
     * call : {"cmccs":[{"name":"30话费","operator":0,"productType":0,"salePrice":29.99}],"ctcs":[{"name":"400话费","operator":0,"productType":0,"salePrice":399.99}],"cuccs":[{"name":"500话费","operator":0,"productType":0,"salePrice":499.99}]}
     * traffic : {"cmccs":[{"name":"30M流量","operator":0,"productType":0,"salePrice":4.99}],"ctcs":[{"name":"1G流量","operator":0,"productType":0,"salePrice":99.99}],"cuccs":[{"name":"300M流量","operator":0,"productType":0,"salePrice":9.99}]}
     */

    public CallBean call;
    public TrafficBean traffic;

    public static class CallBean {
        public List<PackageBean> cmccs;
        public List<PackageBean> ctcs;
        public List<PackageBean> cuccs;
    }

    public static class PackageBean {
        /**
         * name : 30话费
         * operator : 0
         * productType : 0
         * salePrice : 29.99
         */

        public String name;
        public int operator;
        public long callTrafficId;
        public int productType;
        public double salePrice;
        public boolean hasPhone;
    }

    public static class TrafficBean {
        public List<PackageBean> cmccs;
        public List<PackageBean> ctcs;
        public List<PackageBean> cuccs;
    }
}
