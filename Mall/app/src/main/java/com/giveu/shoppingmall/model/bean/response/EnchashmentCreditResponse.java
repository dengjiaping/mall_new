package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * 取现生成订单
 * Created by 101900 on 2017/7/10.
 */

public class EnchashmentCreditResponse extends BaseBean<EnchashmentCreditResponse>{

    /**
     * contractNo : 990020431
     * creditAmount : 200
     * deductDate : 2017-07-21T00:00:00+08:00
     * idCredit : 20431
     * repayNum : 0
     */

    public long contractNo;
    public int creditAmount;
    public String deductDate;
    public String idCredit;
    public int repayNum;
}
