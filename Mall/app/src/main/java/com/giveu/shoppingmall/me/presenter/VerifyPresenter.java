package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.me.view.agent.IVerifyView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ConfirmOrderResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/7/10.
 */

public class VerifyPresenter extends SendSmsPresenter<IVerifyView> {
    public VerifyPresenter(IVerifyView view) {
        super(view);
    }

    public void confirmRechargeOrder(String idPerson, String mobile, long productId, String orderNo, int payType, String smsCode, String phone) {
        ApiImpl.confirmRechargeOrder(getView().getAct(), idPerson, mobile, productId, orderNo, payType, smsCode,phone, new BaseRequestAgent.ResponseListener<ConfirmOrderResponse>() {
            @Override
            public void onSuccess(ConfirmOrderResponse response) {
                if (getView() != null) {
                    getView().confirmOrderSuccess(response.data);
                }

            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    public void thirdPayRecharge(String idPerson, long orderDetailId, String orderNo) {
        ApiImpl.thirdPayRecharge(getView().getAct(), idPerson, orderDetailId, orderNo, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().thirdPaySuccess();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                if (getView() != null) {
                    getView().thirdPayOrderFailed();
                }
            }
        });

    }
}
