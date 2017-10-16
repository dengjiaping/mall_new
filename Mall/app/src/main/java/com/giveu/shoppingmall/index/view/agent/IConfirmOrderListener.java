package com.giveu.shoppingmall.index.view.agent;

import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.InsuranceFee;

/**
 * Created by 524202 on 2017/10/12.
 */

public interface IConfirmOrderListener {
    /**
     * 支付当时发生变化变化
     *
     * @param payType
     */
    void onPayTypeChanged(int payType);

    /**
     * 首付比例发生变化
     *
     * @param downPaymentRate 首付比例
     * @param price 首付价格
     */
    void onDownPaymentRateChanged(int downPaymentRate, String price);

    /**
     * 支付总价发生变化,由首付变化引起
     *
     * @param totalPrice 支付总价
     */
    void onTotalPriceChanged(String totalPrice);

    /**
     * 分期数发生变化
     *
     * @param idProduct 分期id
     */
    void onDownPaymentChanged(long idProduct);

    /**
     * 客户收货地址发生变化
     *
     * @param receiverJoBean 客户收货地址
     */
    void onCustomerAddressChanged(CreateOrderResponse.ReceiverJoBean receiverJoBean);

    /**
     * 优惠券Id发生变化
     *
     * @param cardId
     * @param cardPrice
     */
    void onCourtesyCardIdChanged(long cardId,String cardPrice);

    /**
     * 增值服务发生变化
     *
     * @param insuranceFee
     */
    void onInsuranceFeeChanged(InsuranceFee insuranceFee);

    /**
     * 大家电安装时间发生变化
     *
     * @param installDateId 大家电安装时间id
     */
    void onInstallDateChanged(int installDateId);

    /**
     * 大家电送货时间发生变化
     *
     * @param reservingDateId 大家电送货时间id
     */
    void onReservingDateChanged(int reservingDateId);

    /**
     * 界面数据初始化成功
     */
    void onInitSuccess();

    /**
     * 界面数据初始化失败
     */
    void onInitFailed();
}

