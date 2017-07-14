package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * 分期费率
 * Created by 101900 on 2017/7/4.
 */

public class RepayCostResponse extends BaseBean<RepayCostResponse> {

    /**
     * cost : 0
     * drawMoney : 3000
     * monthPay : 1064.49
     */

    public double cost;
    public double drawMoney;
    public double monthPay;
}
