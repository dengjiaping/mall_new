package com.giveu.shoppingmall.index.presenter;

import android.app.Activity;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.agent.IConfirmOrderView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.MonthSupplyResponse;
import com.giveu.shoppingmall.model.bean.response.SkuInfo;

/**
 * Created by 524202 on 2017/10/11.
 */

public class ConfirmOrderPresenter extends BasePresenter<IConfirmOrderView> {
    public ConfirmOrderPresenter(IConfirmOrderView view) {
        super(view);
    }

    public void getAppDownPayAndMonthPay(Activity context, String channel, String idPerson, int downPaymentRate, String skuCode, int quantity) {
        ApiImpl.getAppDownPayAndMonthPay(context, channel, idPerson, downPaymentRate, skuCode, quantity, new BaseRequestAgent.ResponseListener<DownPayMonthPayResponse>() {
            @Override
            public void onSuccess(DownPayMonthPayResponse response) {
                getView().getAppDownPayAndMonthPaySuccess(response);
            }

            @Override
            public void onError(BaseBean errorBean) {
                getView().getAppDownPayAndMonthPayFailed();
            }
        });
    }

    public void getCreateOrderSc(Activity context, String channel, String idPerson, int downPaymentRate, long idProduct, SkuInfo skuInfo) {
        ApiImpl.createOrderSc(context, channel, idPerson, downPaymentRate, idProduct, skuInfo, new BaseRequestAgent.ResponseListener<CreateOrderResponse>() {
            @Override
            public void onSuccess(CreateOrderResponse response) {
                getView().getCreateOrderScSuccess(response);
            }

            @Override
            public void onError(BaseBean errorBean) {
                getView().getCreateOrderScFailed();
            }
        });
    }

    public void getAppMonthlySupply(Activity context, String channel, String idPerson, int downPaymentRate, long idProduct, int insuranceFee, int quantity, String skuCode) {
        ApiImpl.getAppMonthlySupply(context, channel, idPerson, downPaymentRate, idProduct, insuranceFee, quantity, skuCode, new BaseRequestAgent.ResponseListener<MonthSupplyResponse>() {
            @Override
            public void onSuccess(MonthSupplyResponse response) {
                getView().getAppMonthlySupplySuccess(response);
            }

            @Override
            public void onError(BaseBean errorBean) {
                getView().getAppMonthlySupplyFailed();
            }
        });
    }
}
