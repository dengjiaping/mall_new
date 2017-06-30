package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101900 on 2017/6/30.
 */

public class BankCardListResponse extends BaseBean<List<BankCardListResponse>> {

    /**
     * bankBindPhone : 13578945632
     * bankName : 中国建设银行
     * bankNo : 6236681260000565242
     * bankPerson : 潘梦娟
     * id : 1
     * idPerson : 184735
     * isDefault : 0
     * payType : 1
     */

    public String bankBindPhone;
    public String bankName;
    public String bankNo;
    public String bankPerson;
    public int id;
    public int idPerson;
    public int isDefault;
    public int payType;
}
