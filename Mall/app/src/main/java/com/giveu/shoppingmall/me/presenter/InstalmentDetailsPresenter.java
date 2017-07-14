package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IInstalmentDetailsView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ConfirmOrderResponse;
import com.giveu.shoppingmall.model.bean.response.InstalmentDetailResponse;
import com.giveu.shoppingmall.model.bean.response.PayQueryResponse;
import com.giveu.shoppingmall.model.bean.response.RepaymentActionResponse;
import com.giveu.shoppingmall.utils.TypeUtlis;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/7/3.
 */

public class InstalmentDetailsPresenter extends BasePresenter<IInstalmentDetailsView> {
    public InstalmentDetailsPresenter(IInstalmentDetailsView view) {
        super(view);
    }

    public void getInstalmentDetails(String idCredit, boolean isCurrent, String numInstalment, final String productType, final String creditType) {
        ApiImpl.getInstalmentDetails(getView().getAct(), idCredit, isCurrent, numInstalment, TypeUtlis.convertProductType(productType), new BaseRequestAgent.ResponseListener<InstalmentDetailResponse>() {
            @Override
            public void onSuccess(InstalmentDetailResponse response) {
                if (getView() != null) {
                    getView().showInstalmentDetails(response.data, creditType);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    public void createRepaymentOrder(String idPerson, long amount, String clientIp, String payChannel, String productType) {
        ApiImpl.createRepaymentOrder(getView().getAct(), idPerson, amount, clientIp, payChannel, productType, new BaseRequestAgent.ResponseListener<RepaymentActionResponse>() {
            @Override
            public void onSuccess(RepaymentActionResponse response) {
                if (getView() != null) {
                    getView().createOrderSuccess(response.data.wechatresponse,response.data.payId);
                }

            }

            @Override
            public void onError(BaseBean errorBean) {
                if (getView() != null) {
                    getView().createOrderFailed(errorBean.message);
                }
            }
        });
    }

    public void payQuery(String payId) {
        ApiImpl.payQuery(getView().getAct(), payId, new BaseRequestAgent.ResponseListener<PayQueryResponse>() {
            @Override
            public void onSuccess(PayQueryResponse response) {
                if (getView() != null) {
                    getView().paySuccess();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }


}
