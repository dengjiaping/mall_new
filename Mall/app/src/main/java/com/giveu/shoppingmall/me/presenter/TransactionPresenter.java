package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.agent.ITransactionView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ContractResponse;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/6/26.
 */

public class TransactionPresenter extends BasePresenter<ITransactionView> {
    public TransactionPresenter(ITransactionView view) {
        super(view);
    }

    public void searchContract(String creditStatus, String creditType, String idPerson, String loanDate, int page, int pageSize, String timeType) {
        if (!LoginHelper.getInstance().hasAverageUser()) {
            //不是假数据的激活用户，才去调交易查询数据
            ApiImpl.searchContract(getView().getAct(), creditStatus, creditType, idPerson, loanDate, page, pageSize, timeType, new BaseRequestAgent.ResponseListener<ContractResponse>() {
                @Override
                public void onSuccess(ContractResponse response) {
                    if (getView() != null) {
                        getView().showContractResult(response.data.list);
                    }
                }

                @Override
                public void onError(BaseBean errorBean) {
                    CommonLoadingView.showErrorToast(errorBean);
                }
            });
        }
    }
}
