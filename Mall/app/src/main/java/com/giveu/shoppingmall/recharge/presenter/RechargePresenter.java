package com.giveu.shoppingmall.recharge.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.PayPwdResponse;
import com.giveu.shoppingmall.model.bean.response.RechargeResponse;
import com.giveu.shoppingmall.model.bean.response.SegmentResponse;
import com.giveu.shoppingmall.recharge.view.agent.IRechargeView;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

/**
 * Created by 513419 on 2017/7/8.
 */

public class RechargePresenter extends BasePresenter<IRechargeView> {
    public RechargePresenter(IRechargeView view) {
        super(view);
    }

    public void getProducts() {
        ApiImpl.goodsCallTraffics(getView().getAct(), new BaseRequestAgent.ResponseListener<RechargeResponse>() {
            @Override
            public void onSuccess(RechargeResponse response) {
                if (getView() != null) {
                    getView().showProducts(response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    public void getPhoneInfo(String phone) {
        ApiImpl.goodsCallTrafficsSegment(getView().getAct(), phone, new BaseRequestAgent.ResponseListener<SegmentResponse>() {
            @Override
            public void onSuccess(SegmentResponse response) {
                if (getView() != null) {
                    getView().showPhoneInfo(response.data);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                if (getView() != null&&errorBean!=null) {
                    getView().showErrorInfo(errorBean.message);
                }
            }
        });
    }

    public void createRechargeOrder(final String idPerson, final String mobile, final long productId) {
        ApiImpl.createRechargeOrder(getView().getAct(), idPerson, mobile, productId, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null && response.data != null && StringUtils.isNotNull(response.data.toString())) {
                    getView().createOrderSuccess(response.data.toString());
                }

            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    public void checkPwd(String idPerson, String tradPwd) {
        ApiImpl.verifyPayPwd(getView().getAct(), idPerson, tradPwd, new BaseRequestAgent.ResponseListener<PayPwdResponse>() {
            @Override
            public void onSuccess(PayPwdResponse response) {
                if (getView() != null) {
                    //密码校验成功
                    if (response.data.status) {
                        getView().pwdSuccess();
                    } else {
                        getView().pwdError(response.data.remainTimes);
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
