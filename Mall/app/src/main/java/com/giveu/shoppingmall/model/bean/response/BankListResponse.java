package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101900 on 2017/7/10.
 */

public class BankListResponse extends BaseBean<List<BankListResponse>> {
    public String dicName;
    public String dicOrder;
    public String dicValue;
    public String url;
}
