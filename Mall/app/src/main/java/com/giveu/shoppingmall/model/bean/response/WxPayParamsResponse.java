package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 513419 on 2017/7/10.
 */

public class WxPayParamsResponse<T extends WxPayParamsResponse> extends BaseBean<T> {
    public String appid;
    public String noncestr;
    public String packageValue;
    public String partnerid;
    public String prepayid;
    public String sign;
    public String timestamp;
}
