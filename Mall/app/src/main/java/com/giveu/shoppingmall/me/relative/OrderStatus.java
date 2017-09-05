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
            case 1:
                return "订单待付款";
            case 2:
                return "订单待首付";
            case 3:
                return "订单待发货";
            case 4:
                return "订单待收货";
            case 5:
                return "订单已完成";
            case 6:
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
}
