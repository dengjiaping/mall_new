package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/10/12.
 */

public class GoodsInfoResponse extends BaseBean<ArrayList<GoodsInfoResponse>> {

    /**
     * id : 0
     * name : 零首付
     */

    public int id;
    public String name;
    public int isSelect;
}
