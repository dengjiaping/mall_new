package com.giveu.shoppingmall.recharge.view.agent;

import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.bean.response.RechargeResponse;
import com.giveu.shoppingmall.model.bean.response.SegmentResponse;

/**
 * Created by 513419 on 2017/7/8.
 */

public interface IRechargeView extends IView {
    void showProducts(RechargeResponse data);
    void showPhoneInfo(SegmentResponse data);
    void showErrorInfo();
    void createOrderSuccess(String orderNo);
    void pwdSuccess();
    void pwdError(int remainTimes);
}
