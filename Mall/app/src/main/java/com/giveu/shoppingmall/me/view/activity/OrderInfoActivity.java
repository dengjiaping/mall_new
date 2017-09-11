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
import com.giveu.shoppingmall.index.view.activity.PayChannelActivity;
import com.giveu.shoppingmall.index.view.activity.TransactionPwdActivity;
import com.giveu.shoppingmall.me.presenter.OrderHandlePresenter;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.relative.OrderStatus;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;
import com.giveu.shoppingmall.me.view.dialog.BalanceDeficientDialog;
import com.giveu.shoppingmall.me.view.dialog.DealPwdDialog;
import com.giveu.shoppingmall.me.view.dialog.NotActiveDialog;
import com.giveu.shoppingmall.model.bean.response.OrderDetailResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.CountDownTextView;
import com.giveu.shoppingmall.widget.DetailView;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;

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
    @BindView(R.id.ll_contract)
    LinearLayout llContract;


    private ConfirmDialog dialog;
    private OrderHandlePresenter presenter;
    private String orderNo;
    private String src;
    private DealPwdDialog dealPwdDialog;// 输入交易密码的弹框
    private NotActiveDialog notActiveDialog;//未开通钱包的弹窗
    private BalanceDeficientDialog balanceDeficientDialog;//钱包余额不足的弹框
    int orderPayType;
    double downPayment;//首付金额
    boolean isHaveDownPayment;//是否有首付金额
    String serviceUrl;//服务详情
    String serviceName;

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
        baseLayout.ll_baselayout_content.setVisibility(View.GONE);
        notActiveDialog = new NotActiveDialog(mBaseContext);
        dealPwdDialog = new DealPwdDialog(mBaseContext);
        balanceDeficientDialog = new BalanceDeficientDialog(mBaseContext);
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
            tvPay.setClickable(true);
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
                    tvPay.setClickable(false);
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
        if (response.receiverJo != null) {
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
        }
        //商品icon
        ImageUtils.loadImage(src, ivPicture);

        //商品标题
        if (StringUtils.isNotNull(response.skuInfo.name)) {
            tvName.setText(response.skuInfo.name);
        }
        //商品单价
        if (StringUtils.isNotNull(response.skuInfo.salePrice)) {
            tvSalePrice.setText("¥" + StringUtils.format2(response.skuInfo.salePrice));
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
            tvTotalPrice.setText("¥" + StringUtils.format2(response.skuInfo.totalPrice));
        }
        //支付方式
        orderPayType = response.payType;
        if (StringUtils.isNotNull(OrderStatus.getOrderPayType(response.payType))) {
            tvPayType.setText(OrderStatus.getOrderPayType(response.payType));
        }
        //首付
        if (StringUtils.isNotNull(response.downPayment)) {
            isHaveDownPayment = true;
            downPayment = Double.parseDouble(response.downPayment);
            if (StringUtils.isNotNull(response.selDownPaymentRate)) {
                rlDownPayment.setVisibility(View.VISIBLE);
                tvDownPayment.setText(response.selDownPaymentRate + "%(¥" + StringUtils.format2(response.downPayment) + ")");
            } else {
                rlDownPayment.setVisibility(View.GONE);
            }
        } else {
            isHaveDownPayment = false;
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
            tvMonthPayment.setText("¥" + StringUtils.format2(response.monthPayment));
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
        //增值服务（目前只有一种）
        if (CommonUtils.isNotNullOrEmpty(response.addValueService)) {
            llService.setVisibility(View.VISIBLE);
            tvService0.setText(response.addValueService.get(0).serviceName);
            serviceName = response.addValueService.get(0).serviceName;
            tvService0Cost.setText("¥" + response.addValueService.get(0).servicePrice + "/月");
            if (response.addValueService.get(0).isSelected == 0) {
                cbService0.setChecked(true);
            } else {
                cbService0.setChecked(false);
            }
        } else {
            llService.setVisibility(View.GONE);
        }

        //服务详情
        if (response.addValueService != null) {
            if (StringUtils.isNotNull(response.addValueService.get(0).serviceUrl)) {
                serviceUrl = response.addValueService.get(0).serviceUrl;
            }
        }
        //优惠券
        if (StringUtils.isNotNull(response.courtesyCardName)) {
            dvCouponName.setVisibility(View.VISIBLE);
            dvCouponName.setRightText(response.courtesyCardName);
        } else {
            dvCouponName.setVisibility(View.GONE);
        }

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
//            tvTotal.setText("¥" + StringUtils.format2(response.totalPrice));
        }

        //充值号码
        if (StringUtils.isNotNull(response.rechargePhone)) {
            tvRechargePhone.setText(response.rechargePhone);
        }

        //充值面额
        if (StringUtils.isNotNull(response.rechargeDenomination)) {
            tvRechargeDenomination.setText(response.rechargeDenomination);
        }
        //消费分期合同
        if (response.orderType == 0) {
            llContract.setVisibility(View.VISIBLE);
        } else {
            llContract.setVisibility(View.GONE);
        }
        baseLayout.ll_baselayout_content.setVisibility(View.VISIBLE);
    }

    //确认收货成功
    @Override
    public void confirmReceiveSuccess(String orderNo) {
        ToastUtils.showLongToast("确认收货成功");
        //再次调取接口更新数据
        presenter.getOrderDetail(orderNo);
    }

    //申请退款成功
    @Override
    public void applyToRefundSuccess() {
        ToastUtils.showLongToast("申请成功！会在1~3个工作日处理。如果使用钱包额度支付，我们会将合同取消并恢复您的额度；如果使用其他支付方式，将会退款到您原支付账户，请注意查收");
    }

    //验证交易密码成功
    @Override
    public void verifyPayPwdSuccess() {
        CommonUtils.closeSoftKeyBoard(mBaseContext);
    }

    //验证交易密码失败
    @Override
    public void verifyPayPwdFailure(int remainTimes) {
        CommonUtils.closeSoftKeyBoard(mBaseContext);
        dealPwdDialog.showPwdError(remainTimes);
    }

    @OnClick({R.id.tv_pay, R.id.tv_contract, R.id.tv_order_trace, R.id.tv_trace, R.id.tv_confirm_receive, R.id.tv_apply_refund, R.id.iv_service_detail})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            //去支付、去首付
            case R.id.tv_pay:
                //是否开通钱包
                if (LoginHelper.getInstance().hasQualifications()) {
                    //是否设置了交易密码
                    if (LoginHelper.getInstance().hasSetPwd()) {
                        //钱包可消费余额是否足够
                        if (Integer.parseInt(LoginHelper.getInstance().getAvailablePoslimit()) < downPayment && "0".equals(orderPayType)) {
                            balanceDeficientDialog.setBalance(LoginHelper.getInstance().getAvailablePoslimit());
                            balanceDeficientDialog.show();
                            return;
                        }
                        dealPwdDialog.setPrice("¥" + StringUtils.format2(downPayment + ""));
                        dealPwdDialog.setOnCheckPwdListener(new DealPwdDialog.OnCheckPwdListener() {
                            @Override
                            public void checkPwd(String payPwd) {
                                presenter.onVerifyPayPwd(payPwd);
                            }
                        });
                        dealPwdDialog.showDialog();
                    } else {
                        TransactionPwdActivity.startIt(mBaseContext, LoginHelper.getInstance().getIdPerson());
                    }
                } else {
                    notActiveDialog.showDialog();
                }
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
                presenter.onApplyToRefund(orderNo);
                break;

            case R.id.iv_service_detail:
                CustomWebViewActivity.startIt(mBaseContext, serviceUrl, serviceName);
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

