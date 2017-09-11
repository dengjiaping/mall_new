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
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;

import java.util.List;

/**
 * Created by 101912 on 2017/9/2.
 */

public class OrderListAdapter extends LvCommonAdapter<OrderListResponse.SkuInfoBean> {

    private OrderHandlePresenter presenter;
    private String channelName = "";//渠道名称
    private ConfirmDialog dialog;//确认弹框
    private String orderNo;
    private String src;

    public OrderListAdapter(Context context, List<OrderListResponse.SkuInfoBean> datas, OrderHandlePresenter presenter) {
        super(context, R.layout.lv_order_item, datas);
        if (StringUtils.isNotNull(channelName))
            this.channelName = channelName;
        this.presenter = presenter;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

        //图片icon
        if (StringUtils.isNotNull(item.srcIp))
            if (StringUtils.isNotNull(item.src)) {
                src = item.srcIp + item.src;
                ImageView imageView = viewHolder.getView(R.id.iv_icon);
                ImageUtils.loadImage(src, imageView);
            }

        /**
         * 分期产品显示月供和首付
         * 一次性产品和借记卡消费不显示
         */
        if (item.orderType == 0) {
            viewHolder.setVisible(R.id.rl_payment, true);
            if (StringUtils.isNotNull(item.monthPayment)) {
                viewHolder.setVisible(R.id.ll_payment, true);
                viewHolder.setVisible(R.id.ll_total, false);
                viewHolder.setText(R.id.tv_down_payment, "¥" + item.downPayment);
                viewHolder.setText(R.id.tv_month_payment, "¥" + item.monthPayment);
            }
            else {
                viewHolder.setVisible(R.id.ll_payment, false);
                viewHolder.setVisible(R.id.ll_total, true);
                viewHolder.setText(R.id.tv_total, "¥" + item.payPrice);
            }

        } else {
            viewHolder.setVisible(R.id.rl_payment, false);
        }


        /**
         * 根据不同订单status显示不同按钮
         * 待付款           取消订单、去支付
         * 待首付           订单跟踪、去首付
         * 待收货           订单跟踪、确认收货
         * 已完成           订单跟踪
         * 已关闭           订单跟踪、删除订单
         */
        String status = OrderStatus.getOrderStatus(item.status);
        viewHolder.setText(R.id.tv_status, status);
        switch (item.status) {

            //待支付
            case OrderState.WAITINGPAY:
                viewHolder.setVisible(R.id.tv_button_left, true);
                viewHolder.setVisible(R.id.tv_button_right, true);
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
                viewHolder.setText(R.id.tv_button_left, "订单追踪");
                viewHolder.setText(R.id.tv_button_right, "去首付");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace(orderNo, src);
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
                viewHolder.setText(R.id.tv_button_left, "订单追踪");
                viewHolder.setText(R.id.tv_button_right, "确认收货");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace(orderNo, src);
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
                viewHolder.setText(R.id.tv_button_right, "订单追踪");
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace(orderNo, src);
                    }
                });
                break;

            //订单已关闭
            case OrderState.CLOSED:
                viewHolder.setVisible(R.id.tv_button_left, true);
                viewHolder.setVisible(R.id.tv_button_right, true);
                viewHolder.setText(R.id.tv_button_left, "订单追踪");
                viewHolder.setText(R.id.tv_button_right, "删除订单");
                viewHolder.setOnClickListener(R.id.tv_button_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onTrace(orderNo, src);
                    }
                });
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteDialog();
                    }
                });
                break;

            //充值中
            case OrderState.ONREGHARGE:
                viewHolder.setVisible(R.id.tv_button_left, false);
                viewHolder.setVisible(R.id.tv_button_right, false);
                break;

            //充值成功
            case OrderState.RECHARGESUCCESS:
                viewHolder.setVisible(R.id.tv_button_left, false);
                viewHolder.setVisible(R.id.tv_button_right, false);
                break;

            //充值失败
            case OrderState.RECHARGEFAIL:
                viewHolder.setVisible(R.id.tv_button_left, false);
                viewHolder.setVisible(R.id.tv_button_right, true);
                viewHolder.setText(R.id.tv_button_right, "申请退款");
                viewHolder.setOnClickListener(R.id.tv_button_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRefundDialog();
                    }
                });
                break;
            default:
                break;
        }
    }

    //取消订单dialog
    private void showCancelDialog() {
        dialog = new ConfirmDialog((Activity) mContext);
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
        dialog = new ConfirmDialog((Activity) mContext);
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
        dialog = new ConfirmDialog((Activity) mContext);
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


    //申请退款diaolog
    private void showRefundDialog() {
        dialog = new ConfirmDialog((Activity) mContext);
        dialog.setContent("确认申请退款？");
        dialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                presenter.onApplyToRefund(orderNo);
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
