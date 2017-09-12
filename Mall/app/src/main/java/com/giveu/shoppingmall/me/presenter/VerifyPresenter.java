package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.me.view.agent.IVerifyView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ConfirmOrderResponse;
import com.giveu.shoppingmall.model.bean.response.ConfirmPayResponse;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;

/**
 * Created by 513419 on 2017/7/10.
 */

public class VerifyPresenter extends SendSmsPresenter<IVerifyView> {
    public VerifyPresenter(IVerifyView view) {
        super(view);
    }

    public void confirmRechargeOrder(String idPerson, String mobile, long productId, String orderNo, int payType, String smsCode, String phone) {
        ApiImpl.confirmRechargeOrder(getView().getAct(), idPerson, mobile, productId, orderNo, payType, smsCode, phone, new BaseRequestAgent.ResponseListener<ConfirmOrderResponse>() {
            @Override
            public void onSuccess(ConfirmOrderResponse response) {
                if (getView() != null) {
                    getView().confirmOrderSuccess(response.data);
                }

            }

            @Override
            public void onError(BaseBean errorBean) {
                if (getView() != null) {
                    getView().confirmOrderFail();
                }
            }
        });
    }

    //商城订单支付
    public void confirmPayForShop(String orderNo, String randomCode, String smsCode, String smsMobile) {
        ApiImpl.confirmPay(getView().getAct(), Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), orderNo, randomCode, smsCode, smsMobile, new BaseRequestAgent.ResponseListener<ConfirmPayResponse>() {
            @Override
            public void onSuccess(ConfirmPayResponse response) {
                if (getView() != null) {
                    getView().confirmPaySuccess(response);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {

            }
        });
    }
}
