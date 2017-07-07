package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 101900 on 2017/7/7.
 */

public class PayPwdResponse extends BaseBean<PayPwdResponse>{

    /**
     * code : 038013
     * remainTimes : 3
     * status : true
     * tips : 您今天已三次校验密码错误，账户已冻结
     */

    public int remainTimes;
    public boolean status;
    public String tips;
}
