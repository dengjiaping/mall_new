package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.OrderHandlePresenter;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.relative.OrderStatus;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;
import com.giveu.shoppingmall.model.bean.response.OrderDetailResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.CountDownTextView;
import com.giveu.shoppingmall.widget.DetailView;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 101912 on 2017/8/29.
 */

public class OrderInfoActivity extends BaseActivity implements IOrderInfoView<OrderDetailResponse> {

    @BindView(R.id.ll_time_left)
    LinearLayout llTimeLeft;
    @BindView(R.id.tv_time_left)
    CountDownTextView tvTimeLeft;
    @BindView(R.id.tv_orderNo)
    TextView tvOrderNo;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_receiver_name)
    TextView tvReceiverName;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sale_price)
    TextView tvSalePrice;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.rl_down_payment)
    RelativeLayout rlDownPayment;
    @BindView(R.id.tv_down_payment)
    TextView tvDownPayment;
    @BindView(R.id.rl_staging_num)
    RelativeLayout rlStagingNum;
    @BindView(R.id.tv_staging_num)
    TextView tvStagingNum;
    @BindView(R.id.rl_month_payment)
    RelativeLayout rlMonthPayment;
    @BindView(R.id.tv_month_payment)
    TextView tvMonthPayment;
    @BindView(R.id.dv_coupon_name)
    DetailView dvCouponName;
    @BindView(R.id.tv_user_comments)
    TextView tvUserComments;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.rl_pay)
    RelativeLayout rlPay;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.ll_trace_and_receice)
    LinearLayout llTraceAndReceive;
    @BindView(R.id.tv_trace)
    TextView tvTrace;
    @BindView(R.id.ll_trace)
    LinearLayout llTrace;
    @BindView(R.id.tv_order_trace)
    TextView tvOrderTrace;
    @BindView(R.id.tv_contract)
    TextView tvContract;
    @BindView(R.id.cb_contract)
    CheckBox cbContract;
    @BindView(R.id.rl_footer)
    RelativeLayout rlFooter;
    @BindView(R.id.ll_entity_goods)
    LinearLayout llEntityGoods;
    @BindView(R.id.ll_virtual_goods)
    LinearLayout llVirtualGoods;
    @BindView(R.id.ll_user_comments)
    LinearLayout llUserComments;
    @BindView(R.id.tv_recharge_phone)
    TextView tvRechargePhone;
    @BindView(R.id.tv_recharge_denomination)
    TextView tvRechargeDenomination;
    @BindView(R.id.rl_deliver_and_install)
    RelativeLayout rlDeliverAndInstall;
    @BindView(R.id.tv_deliver)
    TextView tvDeliver;
    @BindView(R.id.tv_install)
    TextView tvInstall;
    @BindView(R.id.ll_service)
    LinearLayout llService;
    @BindView(R.id.cb_service0)
    CheckBox cbService0;
    @BindView(R.id.tv_service0)
    TextView tvService0;
    @BindView(R.id.tv_service0_cost)
    TextView tvService0Cost;
    @BindView(R.id.ll_apply_refund)
    LinearLayout llApplyRefund;


    private ConfirmDialog dialog;
    private OrderHandlePresenter presenter;
    private String orderNo;
    private String src;

    public static void startIt(Activity activity, String orderNo, String src) {
        Intent intent = new Intent(activity, OrderInfoActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("src", src);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_info);
        baseLayout.setTitle("订单详情");
        baseLayout.showLoading();
        baseLayout.ll_baselayout_content.setVisibility(View.GONE);
        presenter = new OrderHandlePresenter(this);
        orderNo = getIntent().getStringExtra("orderNo");
        src = getIntent().getStringExtra("src");
        //调用接口获取订单详情
        presenter.getOrderDetail(orderNo);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {

    }


    /**
     * 支付方式、首付、分期数、月供金额、增值服务、优惠券按照订单实际信息显示，有则显示，无则隐藏
     * 如果用户购买的是大家电，展示家电配送和安装时间
     */
    @Override
    public void showOrderDetail(OrderDetailResponse response) {
        //区分京东商品和话费流量商品
        showUIByOrderType(response.orderType);
        //底部按钮
        dealFooterView(response.status);
        //倒计时
        long timeLeft = 0;
        if (StringUtils.isNotNull(response.remainingTime))
            timeLeft = Long.parseLong(response.remainingTime);
        //status为待首付和待付款时，则采用倒计时
        if (response.status == OrderState.DOWNPAYMENT || response.status == OrderState.WAITINGPAY) {
            llTimeLeft.setVisibility(View.VISIBLE);
            llTimeLeft.setBackgroundColor(getResources().getColor(R.color.color_00bbc0));
            tvTimeLeft.setRestTime(timeLeft);
            tvTimeLeft.startCount(new CountDownTextView.CountEndListener() {
                @Override
                public void onEnd() {
                    tvPay.setBackgroundColor(getResources().getColor(R.color.color_d8d8d8));
                    llTimeLeft.setBackgroundColor(getResources().getColor(R.color.color_d8d8d8));
                    ToastUtils.showLongToast("订单已失效，请重新下单");
                    tvTimeLeft.setText("订单已失效，请重新下单");
                }
            });
        }
        //status为待收货时
        else if (response.status == OrderState.WAITINGRECEIVE) {
            llTimeLeft.setVisibility(View.VISIBLE);
            tvTimeLeft.setText("剩" + response.timeLeft + "自动确认收货");
        } else {
            llTimeLeft.setVisibility(View.GONE);
        }
        //订单号
        if (StringUtils.isNotNull(response.orderNo)) {
            tvOrderNo.setText("订单号：" + response.orderNo);
        }
        //订单status
        String status = OrderStatus.getOrderStatus(response.status);
        if (StringUtils.isNotNull(status)) {
            tvStatus.setText(status);
        }
        //收货人
        if (StringUtils.isNotNull(response.receiverJo.receiverName)) {
            tvReceiverName.setText(response.receiverJo.receiverName);
        }
        //手机号
        if (StringUtils.isNotNull(response.receiverJo.mobile)) {
            tvMobile.setText(response.receiverJo.mobile);
        }
        //收货地址
        String adress = response.receiverJo.province + response.receiverJo.city + response.receiverJo.county + response.receiverJo.address;
        if (StringUtils.isNotNull(adress)) {
            tvAddress.setText(adress);
        }
        //商品icon
        ImageUtils.loadImage(src, ivPicture);

        //商品标题
        if (StringUtils.isNotNull(response.skuInfo.name)) {
            tvName.setText(response.skuInfo.name);
        }
        //商品单价
        if (StringUtils.isNotNull(response.skuInfo.salePrice)) {
            tvSalePrice.setText("¥" + response.skuInfo.salePrice);
        }
        //商品数量
        if (StringUtils.isNotNull(response.skuInfo.quantity)) {
            tvQuantity.setVisibility(View.VISIBLE);
            tvQuantity.setText("×" + response.skuInfo.quantity);
        } else {
            tvQuantity.setVisibility(View.GONE);
        }
        //商品合计
        if (StringUtils.isNotNull(response.skuInfo.totalPrice)) {
            tvTotalPrice.setText("¥" + response.skuInfo.totalPrice);
        }
        //支付方式
        String orderPayType = OrderStatus.getOrderPayType(response.payType);
        if (StringUtils.isNotNull(orderPayType)) {
            tvPayType.setText(orderPayType);
        }
        //首付
        if (StringUtils.isNotNull(response.downPayment) && StringUtils.isNotNull(response.selDownPaymentRate)) {
            rlDownPayment.setVisibility(View.VISIBLE);
            tvDownPayment.setText(response.selDownPaymentRate + "%(¥" + response.downPayment + ")");
        } else {
            rlDownPayment.setVisibility(View.GONE);
        }
        //分期数
        if (StringUtils.isNotNull(response.selStagingNumberRate)) {
            rlStagingNum.setVisibility(View.VISIBLE);
            tvStagingNum.setText(response.selStagingNumberRate + "个月");
        } else {
            rlStagingNum.setVisibility(View.GONE);
        }
        //月供金额
        if (StringUtils.isNotNull(response.monthPayment)) {
            rlMonthPayment.setVisibility(View.VISIBLE);
            tvMonthPayment.setText("¥" + response.monthPayment);
        } else {
            rlMonthPayment.setVisibility(View.GONE);
        }
        //大家电
        if (StringUtils.isNotNull(response.deliverGoods) && StringUtils.isNotNull(response.installGoods)) {
            rlDeliverAndInstall.setVisibility(View.VISIBLE);
            tvDeliver.setText("送货：" + response.deliverGoods);
            tvInstall.setText("安装：" + response.installGoods);
        } else {
            rlDeliverAndInstall.setVisibility(View.GONE);
        }
        //增值服务
        if (CommonUtils.isNotNullOrEmpty(response.addValueService)) {
            llService.setVisibility(View.VISIBLE);
            tvService0.setText(response.addValueService.get(0).serviceName);
            tvService0Cost.setText("¥" + response.addValueService.get(0).servicePrice +"/月");
            if (response.addValueService.get(0).isSelected == 0) {
                cbService0.setChecked(true);
            } else {
                cbService0.setChecked(false);
            }
        } else {
            llService.setVisibility(View.GONE);
        }

        //优惠券
        if (StringUtils.isNotNull(response.courtesyCardName)) {
            dvCouponName.setVisibility(View.VISIBLE);
            dvCouponName.setRightText(response.courtesyCardName);
        } else
            dvCouponName.setVisibility(View.GONE);

        //买家留言（文字前景色不一样）
        if (StringUtils.isNotNull(response.userComments)) {
            tvUserComments.setText(response.userComments);
            SpannableString userComments = new SpannableString("买家留言：" + response.userComments);
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_00bbc0));
            ForegroundColorSpan blackSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_4a4a4a));
            userComments.setSpan(blackSpan, 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//从起始下标到终了下标，包括起始下标
            userComments.setSpan(blueSpan, 5, userComments.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvUserComments.setText(userComments);
        }
        //支付金额
        if (StringUtils.isNotNull(response.totalPrice)) {
            tvTotal.setText("¥" + response.totalPrice);
        }

        //充值号码
        if (StringUtils.isNotNull(response.rechargePhone)) {
            tvRechargePhone.setText(response.rechargePhone);
        }

        //充值面额
        if (StringUtils.isNotNull(response.rechargeDenomination)) {
            tvRechargeDenomination.setText(response.rechargeDenomination);
        }
        baseLayout.ll_baselayout_content.setVisibility(View.VISIBLE);
        baseLayout.disLoading();
    }

    //确认收货成功
    @Override
    public void confirmReceiveSuccess(String orderNo) {
        ToastUtils.showLongToast("成功确认收货");
        //再次调取接口更新数据
        presenter.getOrderDetail(orderNo);
    }


    @OnClick({R.id.tv_pay, R.id.tv_contract, R.id.tv_order_trace, R.id.tv_trace, R.id.tv_confirm_receive, R.id.tv_apply_refund})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            //去支付
            case R.id.tv_pay:
                presenter.onPay();
                break;

            //消费分期合同
            case R.id.tv_contract:

                break;

            //订单追踪
            case R.id.tv_order_trace:
                presenter.onTrace(orderNo, src);
                break;

            //订单追踪
            case R.id.tv_trace:
                presenter.onTrace(orderNo, src);
                break;

            //确认收货
            case R.id.tv_confirm_receive:
                showConfirmDialog();
                break;

            //申请退款
            case R.id.tv_apply_refund:
                break;

            default:
                break;

        }
    }

    /**
     * 订单类型:0-京东商品，1-流量，2-话费
     * 实体商品和虚拟商品显示界面不同
     * 这是处理方法
     */
    private void showUIByOrderType(int orderType) {
        if (orderType == 0) {
            llEntityGoods.setVisibility(View.VISIBLE);
            llUserComments.setVisibility(View.VISIBLE);
            llVirtualGoods.setVisibility(View.GONE);
        } else {
            llEntityGoods.setVisibility(View.GONE);
            llUserComments.setVisibility(View.GONE);
            llVirtualGoods.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 底部有四种样式，这是处理方法
     */
    private void dealFooterView(int status) {
        switch (status) {
            //待付款
            case OrderState.WAITINGPAY:
                rlPay.setVisibility(View.VISIBLE);
                llTrace.setVisibility(View.GONE);
                llTraceAndReceive.setVisibility(View.GONE);
                llApplyRefund.setVisibility(View.GONE);
                tvPay.setText("去支付");
                break;
            //待首付
            case OrderState.DOWNPAYMENT:
                rlPay.setVisibility(View.VISIBLE);
                llTrace.setVisibility(View.GONE);
                llTraceAndReceive.setVisibility(View.GONE);
                llApplyRefund.setVisibility(View.GONE);
                tvPay.setText("去首付");
                break;
            //订单已发货
            case OrderState.WAITINGRECEIVE:
                rlPay.setVisibility(View.GONE);
                llTrace.setVisibility(View.GONE);
                llTraceAndReceive.setVisibility(View.VISIBLE);
                llApplyRefund.setVisibility(View.GONE);
                break;
            //订单已完成
            case OrderState.FINISHED:
                rlPay.setVisibility(View.GONE);
                llTrace.setVisibility(View.VISIBLE);
                llTraceAndReceive.setVisibility(View.GONE);
                llApplyRefund.setVisibility(View.GONE);
                break;
            //订单已关闭
            case OrderState.CLOSED:
                rlPay.setVisibility(View.GONE);
                llTrace.setVisibility(View.VISIBLE);
                llTraceAndReceive.setVisibility(View.GONE);
                llApplyRefund.setVisibility(View.GONE);
                break;
            //充值中
            case OrderState.ONREGHARGE:
                rlPay.setVisibility(View.GONE);
                llTrace.setVisibility(View.GONE);
                llTraceAndReceive.setVisibility(View.GONE);
                llApplyRefund.setVisibility(View.GONE);
                break;
            //充值成功
            case OrderState.RECHARGESUCCESS:
                rlPay.setVisibility(View.GONE);
                llTrace.setVisibility(View.GONE);
                llTraceAndReceive.setVisibility(View.GONE);
                llApplyRefund.setVisibility(View.GONE);
                break;
            //充值失败
            case OrderState.RECHARGEFAIL:
                rlPay.setVisibility(View.GONE);
                llTrace.setVisibility(View.GONE);
                llTraceAndReceive.setVisibility(View.GONE);
                llApplyRefund.setVisibility(View.VISIBLE);
                break;
        }
    }

    //确认收货dialog
    private void showConfirmDialog() {
        dialog = new ConfirmDialog(mBaseContext);
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


    //不用做处理
    @Override
    public void deleteOrderSuccess(String orderNo) {
    }

    //不用做处理
    @Override
    public void cancelOrderSuccess(String orderNo) {
    }

}

