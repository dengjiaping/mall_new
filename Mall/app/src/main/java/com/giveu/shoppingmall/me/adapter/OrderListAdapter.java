package com.giveu.shoppingmall.me.adapter;

import android.content.Context;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.me.presenter.OrderHandlePresenter;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.model.bean.response.OrderListResponse;

import java.util.List;

/**
 * Created by 101912 on 2017/9/2.
 */

public class OrderListAdapter extends LvCommonAdapter<OrderListResponse.SkuInfoBean> {

    private OrderHandlePresenter presenter;
    private String channelName;

    public OrderListAdapter(Context context, List<OrderListResponse.SkuInfoBean> datas, String channelName, OrderHandlePresenter presenter) {
        super(context, R.layout.lv_order_item, datas);
        this.channelName = channelName;
        this.presenter = presenter;
    }

    @Override
    protected void convert(ViewHolder viewHolder, OrderListResponse.SkuInfoBean item, int position) {
        viewHolder.setText(R.id.tv_channel_name, channelName);
        viewHolder.setText(R.id.tv_name, item.name);
        viewHolder.setText(R.id.tv_sale_price, String.valueOf(item.salePrice));
        if (item.orderType == 0) {
            viewHolder.setText(R.id.tv_down_payment, String.valueOf(item.downPayment));
            viewHolder.setText(R.id.tv_month_payment, String.valueOf(item.monthPayment));
            viewHolder.setVisible(R.id.ll_payment, true);
        } else {
            viewHolder.setVisible(R.id.ll_payment, false);
        }
        switch (item.status) {

            //待支付
            case OrderState.WAITINGPAY:
                viewHolder.setVisible(R.id.tv_button_left, true);
                viewHolder.setVisible(R.id.tv_button_right, true);
                viewHolder.setText(R.id.tv_status, "订单待付款");
                viewHolder.setText(R.id.tv_button_left, "订单取消");
                viewHolder.setText(R.id.tv_button_right, "去支付");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onCancelOrder();
                    }
                });
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onPay();
                    }
                });
                break;

            //待首付
            case OrderState.DOWNPAYMENT:
                viewHolder.setVisible(R.id.tv_button_left, true);
                viewHolder.setVisible(R.id.tv_button_right, true);
                viewHolder.setText(R.id.tv_status, "订单待首付");
                viewHolder.setText(R.id.tv_button_left, "订单跟踪");
                viewHolder.setText(R.id.tv_button_right, "去首付");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace();
                    }
                });
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onDownPayment();
                    }
                });
                break;

            //待发货
            case OrderState.WAITINGDELIVERY:
                /*viewHolder.setText(R.id.tv_status, "订单待付款");
                viewHolder.setText(R.id.tv_button_left, "订单取消");
                viewHolder.setText(R.id.tv_button_right, "去支付");*/
                break;

            //待收货
            case OrderState.WAITINGRECEIVE:
                viewHolder.setVisible(R.id.tv_button_left, true);
                viewHolder.setVisible(R.id.tv_button_right, true);
                viewHolder.setText(R.id.tv_status, "订单已发货，待签收");
                viewHolder.setText(R.id.tv_button_left, "订单跟踪");
                viewHolder.setText(R.id.tv_button_right, "确认收货");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace();
                    }
                });
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onConfirmReceive();
                    }
                });
                break;

            //订单已完成
            case OrderState.FINISHED:
                viewHolder.setVisible(R.id.tv_button_left, false);
                viewHolder.setVisible(R.id.tv_button_right, true);
                viewHolder.setText(R.id.tv_status, "订单已完成");
                viewHolder.setText(R.id.tv_button_right, "订单跟踪");
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace();
                    }
                });
                break;

            //订单已关闭
            case OrderState.CLOSED:
                viewHolder.setVisible(R.id.tv_button_left, true);
                viewHolder.setVisible(R.id.tv_button_right, true);
                viewHolder.setText(R.id.tv_status, "已完成");
                viewHolder.setText(R.id.tv_button_left, "订单跟踪");
                viewHolder.setText(R.id.tv_button_right, "删除订单");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace();
                    }
                });
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onDeleteOrder();
                    }
                });
                break;

            default:
                break;
        }
    }
}
