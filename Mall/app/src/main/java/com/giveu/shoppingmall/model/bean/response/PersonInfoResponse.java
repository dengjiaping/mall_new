package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 101900 on 2017/7/4.
 */

public class PersonInfoResponse extends BaseBean<PersonInfoResponse> {

    /**
     * idPerson : 10000923
     * endDate : 2020/6/1
     * cyLimit : 50000
     * ident : 371082199201058672
     * repayAmount : 0
     * creditCount : null
     * availableCyLimit : 50000
     * availablePosLimit : 49772.05
     * globleLimit : 100000
     * posLimit : 0
     * phone : 18109491314
     * activeDate : 2017/6/23
     * name : 唐兴
     * repayDate : null
     * availableRechargeLimit : null
     * status : true
     */

    public String activeDate;
    public String availableCyLimit;
    public String availablePosLimit;
    public String availableRechargeLimit;
    public String creditCount;
    public String cyLimit;
    public String endDate;
    public String globleLimit;
    public int idPerson;
    public String ident;
    public String name;
    public String phone;
    public String posLimit;
    public String repayAmount;
    public String repayDate;
    public boolean status;
}
