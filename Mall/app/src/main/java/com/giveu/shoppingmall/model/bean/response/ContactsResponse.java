package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * 联系人类型
 * Created by 101900 on 2017/8/9.
 */

public class ContactsResponse extends BaseBean<List<ContactsResponse>> {

    /**
     * personType : 5A
     * typeName : 亲戚
     */

    public String personType;
    public String typeName;

}
