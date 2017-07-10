package com.giveu.shoppingmall.me.view.agent;

import com.giveu.shoppingmall.model.bean.response.ConfirmOrderResponse;

/**
 * Created by 513419 on 2017/7/10.
 */

public interface IVerifyView extends ISendSmsView{
    void confirmOrderSuccess(ConfirmOrderResponse data);
    void thirdPayOrderFailed();
    void thirdPaySuccess();
}
