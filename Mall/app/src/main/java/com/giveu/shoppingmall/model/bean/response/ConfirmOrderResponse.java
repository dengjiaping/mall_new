package com.giveu.shoppingmall.model.bean.response;

/**
 * Created by 513419 on 2017/7/8.
 */

public class ConfirmOrderResponse extends WxPayParamsResponse<ConfirmOrderResponse>{
    public long orderDetailId;
    public String orderNo;
}
