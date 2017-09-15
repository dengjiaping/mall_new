package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.io.Serializable;

/**
 * Created by 101912 on 2017/9/12.
 */

public class ConfirmPayResponse extends BaseBean<ConfirmPayResponse> implements Serializable{

    /**
     * iniypay : 0
     * latesRepayDate : 2017年08月23日
     * payPrice : 100.36
     * paymentNum : 6
     */

    public String iniypay;
    public String latesRepayDate;
    public String payPrice;
    public String paymentNum;
    public String alipay;
    public String payId;
    public int payType;
}
