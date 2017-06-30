package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 101900 on 2017/6/30.
 */

public class WalletActivationResponse extends BaseBean<WalletActivationResponse> {

    /**
     * cyLimit : 50000
     * globleLimit : 100000
     * isPhoneChage : true
     * lab : 通过个人资金进行商城消费可积累信用提升额度哦
     * posLimit : 0
     */

    public int cyLimit;
    public int globleLimit;
    public boolean isPhoneChage;
    public String lab;
    public int posLimit;
}
