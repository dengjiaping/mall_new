package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.IInstalmentDetailsView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.InstalmentDetailResponse;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/7/3.
 */

public class InstalmentDetailsPresenter extends BasePresenter<IInstalmentDetailsView> {
    public InstalmentDetailsPresenter(IInstalmentDetailsView view) {
        super(view);
    }

    public void getInstalmentDetails(String idCredit, boolean isCurrent, String numInstalment, String productType) {
        ApiImpl.getInstalmentDetails(getView().getAct(), idCredit, isCurrent, numInstalment, productType, new BaseRequestAgent.ResponseListener<InstalmentDetailResponse>() {
            @Override
            public void onSuccess(InstalmentDetailResponse response) {
                if (getView() != null) {
                    getView().showInstalmentDetails(response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}