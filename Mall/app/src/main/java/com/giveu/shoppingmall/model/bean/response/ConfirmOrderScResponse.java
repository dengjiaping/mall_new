package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 524202 on 2017/9/13.
 */

public class ConfirmOrderScResponse extends BaseBean<ConfirmOrderScResponse> {

    /**
     * downPayment : 10.21
     * orderNo : success
     * payMoney : 10.21
     * payType : 1
     * remainingTime : 600
     */

    private double downPayment;
    private String orderNo;
    private double payMoney;
    private int payType;
    private int remainingTime;

}
