package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 513419 on 2017/7/8.
 */

public class ConfirmOrderResponse extends BaseBean<ConfirmOrderResponse> {
    public String appid;
    public String noncestr;
    public long orderDetailId;
    public String orderNo;
    public String packageValue;
    public String partnerid;
    public String prepayid;
    public String sign;
    public String timestamp;
    public String payId;

}
