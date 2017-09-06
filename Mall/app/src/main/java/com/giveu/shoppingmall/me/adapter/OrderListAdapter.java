package com.giveu.shoppingmall.me.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.me.presenter.OrderHandlePresenter;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.relative.OrderStatus;
import com.giveu.shoppingmall.model.bean.response.OrderListResponse;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;

import java.util.List;

/**
 * Created by 101912 on 2017/9/2.
 */

public class OrderListAdapter extends LvCommonAdapter<OrderListResponse.SkuInfoBean> {

    private OrderHandlePresenter presenter;
    private String channelName;//渠道名称
    private ConfirmDialog dialog;//确认弹框
    private String orderNo;

    public OrderListAdapter(Context context, List<OrderListResponse.SkuInfoBean> datas, String channelName, OrderHandlePresenter presenter) {
        super(context, R.layout.lv_order_item, datas);
        if (StringUtils.isNotNull(channelName))
            this.channelName = channelName;
        this.presenter = presenter;
        dialog = new ConfirmDialog((Activity) mContext);
    }

    @Override
    protected void convert(ViewHolder viewHolder, OrderListResponse.SkuInfoBean item, int position) {
        viewHolder.setText(R.id.tv_channel_name, channelName);
        if (StringUtils.isNotNull(item.orderNo)) {
            orderNo = item.orderNo;
        }
        if (StringUtils.isNotNull(item.name))
            viewHolder.setText(R.id.tv_name, item.name);
        if (StringUtils.isNotNull(item.salePrice))
            viewHolder.setText(R.id.tv_sale_price, "¥" + item.salePrice);
        if (item.orderType == 0) {
            viewHolder.setText(R.id.tv_down_payment, "¥" + item.downPayment);
            viewHolder.setText(R.id.tv_month_payment, "¥" + item.monthPayment);
            viewHolder.setVisible(R.id.ll_payment, true);
        } else {
            viewHolder.setVisible(R.id.ll_payment, false);
        }
        String iconUrl = "";
        if (StringUtils.isNotNull(item.srcIp))
            if (StringUtils.isNotNull(item.src)) {
                iconUrl = item.srcIp + item.src;
                ImageView imageView = viewHolder.getView(R.id.iv_icon);
                ImageUtils.loadImage(iconUrl, imageView);
            }
        String status = OrderStatus.getOrderStatus(item.status);
        switch (item.status) {

            //待支付
            case OrderState.WAITINGPAY:
                viewHolder.setVisible(R.id.tv_button_left, true);
                viewHolder.setVisible(R.id.tv_button_right, true);
                viewHolder.setText(R.id.tv_status, status);
                viewHolder.setText(R.id.tv_button_left, "订单取消");
                viewHolder.setText(R.id.tv_button_right, "去支付");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCancelDialog();
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
                viewHolder.setText(R.id.tv_status, status);
                viewHolder.setText(R.id.tv_button_left, "订单跟踪");
                viewHolder.setText(R.id.tv_button_right, "去首付");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace(orderNo);
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
                viewHolder.setText(R.id.tv_status, status + "，待签收");
                viewHolder.setText(R.id.tv_button_left, "订单跟踪");
                viewHolder.setText(R.id.tv_button_right, "确认收货");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace(orderNo);
                    }
                });
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showReceiveDialog();
                    }
                });
                break;

            //订单已完成
            case OrderState.FINISHED:
                viewHolder.setVisible(R.id.tv_button_left, false);
                viewHolder.setVisible(R.id.tv_button_right, true);
                viewHolder.setText(R.id.tv_status, status);
                viewHolder.setText(R.id.tv_button_right, "订单跟踪");
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace(orderNo);
                    }
                });
                break;

            //订单已关闭
            case OrderState.CLOSED:
                viewHolder.setVisible(R.id.tv_button_left, true);
                viewHolder.setVisible(R.id.tv_button_right, true);
                viewHolder.setText(R.id.tv_status, status);
                viewHolder.setText(R.id.tv_button_left, "订单跟踪");
                viewHolder.setText(R.id.tv_button_right, "删除订单");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace(orderNo);
                    }
                });
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteDialog();
                    }
                });
                break;

            default:
                break;
        }
    }

    //取消订单dialog
    private void showCancelDialog() {
        dialog.setContent("确认取消订单？");
        dialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                presenter.onCancelOrder(orderNo);
                dialog.dismiss();
            }

            @Override
            public void cancle() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //确认收货dialog
    private void showReceiveDialog() {
        dialog.setContent("是否确认收货？");
        dialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                presenter.onConfirmReceive(orderNo);
                dialog.dismiss();
            }

            @Override
            public void cancle() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //删除订单diaolog
    private void showDeleteDialog() {
        dialog.setContent("是否删除订单？");
        dialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                presenter.onDeleteOrder(orderNo);
                dialog.dismiss();
            }

            @Override
            public void cancle() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
