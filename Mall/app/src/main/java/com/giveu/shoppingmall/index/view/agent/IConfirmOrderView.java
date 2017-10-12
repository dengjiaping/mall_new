package com.giveu.shoppingmall.index.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.MonthSupplyResponse;

/**
 * Created by 524202 on 2017/10/11.
 */

public interface IConfirmOrderView extends IView {
    /**
     * 查询首付和分期信息成功回调
     *
     * @param response
     */
    void getAppDownPayAndMonthPaySuccess(DownPayMonthPayResponse response);

    /**
     * 查询首付和分期信息失败回调
     */
    void getAppDownPayAndMonthPayFailed();

    /**
     * 创建订单成功回调
     *
     * @param response
     */
    void getCreateOrderScSuccess(CreateOrderResponse response);

    /**
     * 创建订单失败回调
     */
    void getCreateOrderScFailed();

    /**
     * 查询月供信息成功回调
     *
     * @param response
     */
    void getAppMonthlySupplySuccess(MonthSupplyResponse response);

    /**
     * 查询月供信息失败回调
     */
    void getAppMonthlySupplyFailed();
}
