package com.giveu.shoppingmall.me.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.index.view.activity.PayChannelActivity;
import com.giveu.shoppingmall.index.view.activity.TransactionPwdActivity;
import com.giveu.shoppingmall.me.presenter.OrderHandlePresenter;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.relative.OrderStatus;
import com.giveu.shoppingmall.me.view.dialog.BalanceDeficientDialog;
import com.giveu.shoppingmall.me.view.dialog.DealPwdDialog;
import com.giveu.shoppingmall.me.view.dialog.NotActiveDialog;
import com.giveu.shoppingmall.model.bean.response.OrderListResponse;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
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
    private DealPwdDialog dealPwdDialog;// 输入交易密码的弹框
    private NotActiveDialog notActiveDialog;//未开通钱包的弹窗
    private BalanceDeficientDialog balanceDeficientDialog;//钱包余额不足的弹框
    private String payType;//支付方式
    private double finalPayment;//最终支付金额
    private boolean isWalletPay;//是否钱包支付

    public OrderListAdapter(Context context, List<OrderListResponse.SkuInfoBean> datas, OrderHandlePresenter presenter) {
        super(context, R.layout.lv_order_item, datas);
        this.presenter = presenter;
        notActiveDialog = new NotActiveDialog((Activity) mContext);
        dealPwdDialog = new DealPwdDialog((Activity) mContext);
        balanceDeficientDialog = new BalanceDeficientDialog((Activity) mContext);
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
        if (StringUtils.isNotNull(item.payType)) {
            payType = item.payType;
            if ("0".equals(payType)) {
                isWalletPay = true;
            } else {
                isWalletPay = false;
            }
        }
        if (StringUtils.isNotNull(item.name))
            viewHolder.setText(R.id.tv_name, item.name);
        if (StringUtils.isNotNull(item.salePrice))
            viewHolder.setText(R.id.tv_sale_price, "¥" + StringUtils.format2(item.salePrice));

        //图片icon
        if (StringUtils.isNotNull(item.srcIp)) {
            if (StringUtils.isNotNull(item.src)) {
                src = item.srcIp + item.src;
                ImageView imageView = viewHolder.getView(R.id.iv_icon);
                ImageUtils.loadImage(src, imageView);
            }
        }
        /**
         * 分期产品显示月供和首付
         * 一次性产品和借记卡消费不显示
         */
        if (item.orderType == 0) {
            viewHolder.setVisible(R.id.rl_payment, true);
            if (StringUtils.isNotNull(item.monthPayment) && StringUtils.isNotNull(item.periods)) {
                viewHolder.setVisible(R.id.ll_payment, true);
                viewHolder.setVisible(R.id.ll_total, false);
                viewHolder.setText(R.id.tv_down_payment, "¥" + StringUtils.format2(item.downPayment));
                viewHolder.setText(R.id.tv_month_payment, "¥" + StringUtils.format2(item.monthPayment) + " * " + item.periods);
            } else {
                viewHolder.setVisible(R.id.ll_payment, false);
                viewHolder.setVisible(R.id.ll_total, true);
                viewHolder.setText(R.id.tv_total, "¥" + StringUtils.format2(item.payPrice));
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
         * 充值中		    ——
         * 充值成功	        ——
         * 充值失败	        申请退款
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
                        onPay();
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
                        onPay();
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

    //点击去支付、去首付后的流程处理
    private void onPay() {
        //是否有开通钱包
        if (LoginHelper.getInstance().hasQualifications()) {
            //是否设置了交易密码
            if (LoginHelper.getInstance().hasSetPwd()) {
                //钱包可消费余额是否足够
                if (Integer.parseInt(LoginHelper.getInstance().getAvailablePoslimit()) < finalPayment && "0".equals(payType)) {
                    balanceDeficientDialog.setBalance(LoginHelper.getInstance().getAvailablePoslimit());
                    balanceDeficientDialog.show();
                    return;
                }
                dealPwdDialog.setPrice("¥" + StringUtils.format2(finalPayment + ""));
                dealPwdDialog.setOnCheckPwdListener(new DealPwdDialog.OnCheckPwdListener() {
                    @Override
                    public void checkPwd(String payPwd) {
                        presenter.onVerifyPayPwd(payPwd, orderNo, isWalletPay, finalPayment + "");
                    }
                });
                dealPwdDialog.showDialog();
            } else {
                TransactionPwdActivity.startIt((Activity) mContext, LoginHelper.getInstance().getIdPerson());
            }
        } else {
            notActiveDialog.showDialog();
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
