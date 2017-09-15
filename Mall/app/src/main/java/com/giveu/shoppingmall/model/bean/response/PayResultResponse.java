package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 513419 on 2017/9/15.
 */

public class PayResultResponse extends BaseBean<PayResultResponse> {

    /**
     * orderAmt : 123
     * payId : 08201707150000036381
     * paySuccess : true
     */

    public String orderAmt;
    public String payId;
    public boolean paySuccess;
}
