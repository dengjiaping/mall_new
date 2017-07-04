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

    public int idPerson;
    public String endDate;
    public int cyLimit;
    public String ident;
    public int repayAmount;
    public Object creditCount;
    public int availableCyLimit;
    public double availablePosLimit;
    public int globleLimit;
    public int posLimit;
    public String phone;
    public String activeDate;
    public String name;
    public Object repayDate;
    public Object availableRechargeLimit;
    public boolean status;
}
