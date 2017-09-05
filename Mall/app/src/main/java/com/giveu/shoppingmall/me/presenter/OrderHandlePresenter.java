package com.giveu.shoppingmall.me.presenter;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.view.activity.OrderInfoActivity;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.OrderDetailResponse;
import com.giveu.shoppingmall.model.bean.response.OrderListResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.ArrayList;

/**
 * Created by 101912 on 2017/8/30.
 */

public class OrderHandlePresenter extends BasePresenter<IOrderInfoView> {

    public OrderHandlePresenter(IOrderInfoView view) {
        super(view);
    }


    public void getOrderDetail(String orderNo) {
        //获取订单详情
        ApiImpl.getOrderDetail(OrderState.CHANNEL, LoginHelper.getInstance().getIdPerson(), orderNo, new BaseRequestAgent.ResponseListener<OrderDetailResponse>() {
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
    public void onTrace() {

    }

    //确认收货
    public void onConfirmReceive() {

    }

    //申请退款
    public void onApplyToRefund() {

    }

    //上传手机串码
    public void onUploadMobileIMEI() {

    }

    //取消订单
    public void onCancelOrder() {

    }

    //删除订单
    public void onDeleteOrder() {

    }

}
