package com.giveu.shoppingmall.me.presenter;

import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.view.activity.OrderInfoActivity;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;

/**
 * Created by 101912 on 2017/8/30.
 */

public class OrderHandlePresenter extends BasePresenter<IOrderInfoView> {

    public OrderHandlePresenter(IOrderInfoView view) {
        super(view);
    }

    public void getOrderInfo() {
        //处理订单详情
        if (getView().getAct() instanceof OrderInfoActivity) {

        }
        else {

        }
        getView().showOrderInfo();
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
