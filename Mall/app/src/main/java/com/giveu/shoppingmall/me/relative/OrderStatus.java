package com.giveu.shoppingmall.me.relative;

/**
 * Created by 101912 on 2017/9/5.
 */

public class OrderStatus {

    /**
     *
     * 订单状态:
     * 1-待付款 2-待首付
     * 3-待发货 4-待收货
     * 5-已完成 6-已关闭
     */
    public static String getOrderStatus(int orderStatus) {
        switch (orderStatus) {
            case OrderState.WAITINGPAY:
                return "订单待付款";
            case OrderState.DOWNPAYMENT:
                return "订单待首付";
            case OrderState.WAITINGDELIVERY:
                return "订单待发货";
            case OrderState.WAITINGRECEIVE:
                return "订单待收货";
            case OrderState.FINISHED:
                return "订单已完成";
            case OrderState.CLOSED:
                return "订单已关闭";
            default:
                return "";
        }
    }

    /**
     *
     * 支付方式:
     * 0-即有钱包，1-微信, 2-支付宝
     */
    public static String getOrderPayType(int payType) {
        switch (payType) {
            case 0:
                return "即有钱包";
            case 1:
                return "微信";
            case 2:
                return "支付宝";
            default:
                return "";
        }
    }

    /**
     *订单状态             可用按钮功能
     * 待付款              取消订单、去支付
     * 待首付              订单跟踪、去首付
     * 待收货              订单跟踪、确认收货
     * 一完成              订单跟踪
     * 已关闭              订单跟踪、删除订单
     */
    public static String getButtonType(int type) {
        /*switch (type) {
            case
        }*/
        return "";
    }

}
