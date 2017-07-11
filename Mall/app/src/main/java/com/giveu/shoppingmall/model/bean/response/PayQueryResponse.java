package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 513419 on 2017/7/11.
 */

public class PayQueryResponse extends BaseBean<PayQueryResponse> {
    /**
     * paySuccess : false
     * orderAmt :
     * payId : 08201707100000034333
     */
    public boolean paySuccess;
    public String orderAmt;
    public String payId;
}
