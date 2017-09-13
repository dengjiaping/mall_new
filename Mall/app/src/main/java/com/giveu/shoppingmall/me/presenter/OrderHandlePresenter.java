package com.giveu.shoppingmall.me.presenter;


import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.activity.PayChannelActivity;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.view.activity.OrderTraceActivity;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.OrderDetailResponse;
import com.giveu.shoppingmall.model.bean.response.PayPwdResponse;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;


/**
 * Created by 101912 on 2017/8/30.
 */

public class OrderHandlePresenter extends BasePresenter<IOrderInfoView> {


    public OrderHandlePresenter(IOrderInfoView view) {
        super(view);
    }

    //获取订单详情
    public void getOrderDetail(String orderNo) {

        ApiImpl.getOrderDetail(getView().getAct(), Const.CHANNEL, "10056737", orderNo, new BaseRequestAgent.ResponseListener<OrderDetailResponse>() {
            @Override
            public void onSuccess(OrderDetailResponse response) {
                if (getView() != null) {
                    getView().showOrderDetail(response.data);
                }

            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }



    //验证交易密码
    public void onVerifyPayPwd(String payPwd, final String orderNo, final boolean isWalletPay, final String payment) {
        ApiImpl.verifyPayPwd(getView().getAct(), LoginHelper.getInstance().getIdPerson(), payPwd, new BaseRequestAgent.ResponseListener<PayPwdResponse>() {
            @Override
            public void onSuccess(PayPwdResponse response) {
                if (getView() != null) {
                    if (response.data != null) {
                        PayPwdResponse pwdResponse = response.data;
                        if (pwdResponse.status) {
                            getView().verifyPayPwdSuccess(orderNo, isWalletPay, payment);
                        } else {
                            getView().verifyPayPwdFailure(pwdResponse.remainTimes);
                        }
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    //订单追踪
    public void onTrace(String orderNo, String src) {
        if (StringUtils.isNotNull(orderNo)) {
            OrderTraceActivity.startIt(getView().getAct(), orderNo, src);
        }
    }

    //确认收货
    public void onConfirmReceive(final String orderNo) {
        ApiImpl.confirmReceive(getView().getAct(), Const.CHANNEL, "10056737", orderNo, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().confirmReceiveSuccess(orderNo);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    //申请退款
    public void onApplyToRefund(String orderNo) {
        ApiImpl.applyToRefundForRecharge(getView().getAct(), Const.CHANNEL, "10056737", orderNo, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().applyToRefundSuccess();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    //上传手机串码
    public void onUploadMobileIMEI() {

    }

    //取消订单
    public void onCancelOrder(final String orderNo) {
        ApiImpl.cancelOrder(getView().getAct(), Const.CHANNEL, "10056737", orderNo, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().cancelOrderSuccess(orderNo);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    //删除订单
    public void onDeleteOrder(final String orderNo) {
        ApiImpl.deleteOrder(getView().getAct(), Const.CHANNEL, "10056737", orderNo, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (getView() != null) {
                    getView().deleteOrderSuccess(orderNo);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}