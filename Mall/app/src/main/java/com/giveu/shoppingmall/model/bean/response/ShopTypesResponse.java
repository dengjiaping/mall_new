package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101912 on 2017/9/14.
 */

public class ShopTypesResponse extends BaseBean<List<ShopTypesResponse>> {


    /**
     * name : 手机
     * shopTypeId : 1
     */

    public String name;
    public int shopTypeId;
}
