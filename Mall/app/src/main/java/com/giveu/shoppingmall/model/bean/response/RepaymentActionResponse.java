package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 513419 on 2017/7/10.
 */

public class RepaymentActionResponse extends BaseBean<RepaymentActionResponse> {

    public WxPayParamsResponse wechatresponse;
    public String channelType;
    public String payId;
}
