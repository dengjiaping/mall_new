package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.ICreditDetailView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ListInstalmentResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/7/4.
 */

public class CreditDetailPresenter extends BasePresenter<ICreditDetailView> {
    public CreditDetailPresenter(ICreditDetailView view) {
        super(view);
    }

    public void getCreditDetail(String idCredit) {
        ApiImpl.getListInstalment(getView().getAct(), idCredit, new BaseRequestAgent.ResponseListener<ListInstalmentResponse>() {
            @Override
            public void onSuccess(ListInstalmentResponse response) {
                if (getView() != null) {
                    getView().showCreditDetail(response.data.instalmentList);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
