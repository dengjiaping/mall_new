package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 *  用户信息
 * Created by 101900 on 2017/7/3.
 */

public class UserInfoResponse extends BaseBean{

    /**
     * activeDate : 2017/6/23
     * availableCyLimit : 50000
     * availablePosLimit : 50000
     * availableRechargeLimit : 500
     * creditCount : null
     * cyLimit : 50000
     * endDate : 2020/6/1
     * globleLimit : 100000
     * idPerson : 10000923
     * ident : 371082199201058672
     * name : 唐兴
     * phone : 18109491314
     * posLimit : 0
     * repayAmount : null
     * repayDate : null
     * status : true
     */

    public String activeDate;
    public int availableCyLimit;
    public int availablePosLimit;
    public int availableRechargeLimit;
    public Object creditCount;
    public int cyLimit;
    public String endDate;
    public int globleLimit;
    public int idPerson;
    public String ident;
    public String name;
    public String phone;
    public int posLimit;
    public String repayAmount;
    public String repayDate;
    public boolean status;
}
