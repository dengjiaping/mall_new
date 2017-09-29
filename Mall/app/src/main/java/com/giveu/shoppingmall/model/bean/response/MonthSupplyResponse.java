package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 524202 on 2017/9/29.
 */

public class MonthSupplyResponse extends BaseBean<MonthSupplyResponse> {


    /**
     * paymentNum : 3
     * initPay : 359.8
     * paymentList : [{"paymentNum":1,"monthPay":1174,"repayDate":"2017/10/29"},{"paymentNum":2,"monthPay":1174,"repayDate":"2017/11/29"},{"paymentNum":3,"monthPay":1174,"repayDate":"2017/12/29"}]
     */

    public int paymentNum;
    public String initPay;
    public List<PaymentListBean> paymentList;

    public static class PaymentListBean {
        /**
         * paymentNum : 1
         * monthPay : 1174
         * repayDate : 2017/10/29
         */

        public int paymentNum;
        public String monthPay;
        public String repayDate;
    }
}
