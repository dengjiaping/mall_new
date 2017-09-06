package com.giveu.shoppingmall.me.presenter;


import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.OrderDetailResponse;
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
        ApiImpl.getOrderDetail(getView().getAct(), "qq", "10056737", orderNo, new BaseRequestAgent.ResponseListener<OrderDetailResponse>() {
            @Override
            public void onSuccess(OrderDetailResponse response) {
                getView().showOrderDetail(response.data);
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    //去支付
    public void onPay() {

    }

    //去首付
    public void onDownPayment() {

    }

    //订单追踪
    public void onTrace(String orderNo) {

    }

    //确认收货
    public void onConfirmReceive(String orderNo) {

    }

    //申请退款
    public void onApplyToRefund() {

    }

    //上传手机串码
    public void onUploadMobileIMEI() {

    }

    //取消订单
    public void onCancelOrder(final String orderNo) {
        ApiImpl.cancelOrder(getView().getAct(), OrderState.CHANNEL, "10056737", orderNo, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                getView().cancelOrderSuccess(orderNo);
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    //删除订单
    public void onDeleteOrder(final String orderNo) {
        ApiImpl.deleteOrder(getView().getAct(), OrderState.CHANNEL, "10056737", orderNo, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                getView().deleteOrderSuccess(orderNo);
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

}
