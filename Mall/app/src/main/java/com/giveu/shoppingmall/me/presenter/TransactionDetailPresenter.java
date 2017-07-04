package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.ITransactionDetailView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.TransactionDetailResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/7/4.
 */

public class TransactionDetailPresenter extends BasePresenter<ITransactionDetailView> {
    public TransactionDetailPresenter(ITransactionDetailView view) {
        super(view);
    }

    public void getContractDetails(String idCredit, String creditType) {
        ApiImpl.getContractDetails(getView().getAct(), idCredit, creditType, new BaseRequestAgent.ResponseListener<TransactionDetailResponse>() {
            @Override
            public void onSuccess(TransactionDetailResponse response) {
                if (getView() != null) {
                    getView().showTransactionDetail(response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });

    }

}
