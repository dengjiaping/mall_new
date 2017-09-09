package com.giveu.shoppingmall.me.relative;

/**
 * Created by 101912 on 2017/9/4.
 */

/**
 * 状态:0-全部 1-待付款
 * 2-待首付 3-待发货
 * 4-待收货 5-已完成
 * 6-已关闭
 */
public interface OrderState {

    String ORDER_TYPE = "orderState";

    //渠道名称：商城
    String CHANNEL = "sc";

    //全部
    String ALL_RESPONSE = "0";

    //待付款
    String WAITING_PAY = "1";
    int WAITINGPAY = 1;

    //待首付
    String DOWN_PAYMENT = "2";
    int DOWNPAYMENT = 2;

    //待发货
    String WAITING_DELIVERY = "3";
    int WAITINGDELIVERY = 3;

    //待收货
    String WAITING_RECEIVE = "4";
    int WAITINGRECEIVE = 4;

    //已完成
    String Finished_RESPONSE = "5";
    int FINISHED = 5;

    //已关闭
    String CLOSED_RESPONSE = "6";
    int CLOSED = 6;

    //充值中
    String ON_RECHARGE = "20";
    int ONREGHARGE = 20;

    //充值成功
    String RECHARGE_SUCCESS = "21";
    int RECHARGESUCCESS = 21;

    //充值失败
    String RECHARGE_FAIL = "22";
    int RECHARGEFAIL = 22;
}