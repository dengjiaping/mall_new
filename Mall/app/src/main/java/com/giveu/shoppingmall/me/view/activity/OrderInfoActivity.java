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

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.OrderHandlePresenter;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.relative.OrderStatus;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.OrderDetailResponse;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.CountDownTextView;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;

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
    @BindView(R.id.tv_coupon_name)
    TextView tvCouponName;
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

    private ConfirmDialog dialog;
    private OrderHandlePresenter presenter;
    private String orderNo;

    public static void startIt(Activity activity, String orderNo) {
        Intent intent = new Intent(activity, OrderInfoActivity.class);
        intent.putExtra("orderNo", orderNo);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_info);
        baseLayout.setTitle("订单详情");
        presenter = new OrderHandlePresenter(this);
        orderNo = getIntent().getStringExtra("orderNo");
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

    @Override
    public void showOrderDetail(OrderDetailResponse response) {
        //底部按钮
        dealFooterView(response.status);
        //倒计时
        long timeLeft = 0;
        if (StringUtils.isNotNull(response.timeLeft))
            timeLeft = Long.parseLong(response.timeLeft);
        //status为待首付和待付款时，则采用倒计时
        if (response.status == OrderState.DOWNPAYMENT || response.status == OrderState.WAITINGPAY) {
            llTimeLeft.setVisibility(View.VISIBLE);
            tvTimeLeft.setRestTime(timeLeft);
            tvTimeLeft.startCount(new CountDownTextView.CountEndListener() {
                @Override
                public void onEnd() {
                    tvPay.setBackgroundColor(getResources().getColor(R.color.color_d8d8d8));
                    ToastUtils.showLongToast("订单已失效，请重新下单");
                    tvTimeLeft.setText("订单已失效，请重新下单");
                }
            });
        }
        //status为待收货时
        else if (response.status == OrderState.WAITINGRECEIVE) {
            llTimeLeft.setVisibility(View.VISIBLE);
            tvTimeLeft.setText("剩" + StringUtils.formatRestTimeToDay(timeLeft) + "自动确认收货");
        } else
            llTimeLeft.setVisibility(View.GONE);
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
        String iconUrl = "";
        if (StringUtils.isNotNull(response.skuInfo.srcIp))
            if (StringUtils.isNotNull(response.skuInfo.src)) {
                iconUrl = response.skuInfo.srcIp + response.skuInfo.src;
                ImageUtils.loadImage(iconUrl, ivPicture);
            }

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
            tvQuantity.setText("×" + response.skuInfo.quantity);
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
            tvDownPayment.setText(response.selDownPaymentRate + "(¥" + response.downPayment + ")");
        }
        //分期数
        if (StringUtils.isNotNull(response.selStagingNumberRate)) {
            tvStagingNum.setText(response.selStagingNumberRate + "个月");
        }
        //月供金额
        if (StringUtils.isNotNull(response.monthPayment)) {
            tvMonthPayment.setText("¥" + response.monthPayment);
        }
        //大家电


        //增值服务


        //优惠券


        //买家留言（文字前景色不一样）
        if (StringUtils.isNotNull(response.userComments)) {
            tvUserComments.setText(response.userComments);
            SpannableString userComments = new SpannableString("买家留言：" + response.userComments);
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_00bbc0));
            ForegroundColorSpan blackSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_4a4a4a));
            userComments.setSpan(blackSpan, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//从起始下标到终了下标，包括起始下标
            userComments.setSpan(blueSpan, 5, userComments.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvUserComments.setText(userComments);
        }
        //支付金额
        if (StringUtils.isNotNull(response.totalPrice)) {
            tvTotal.setText("¥" + response.totalPrice);
        }

    }

    @Override
    public void deleteOrderSuccess(String orderNo) {

    }

    @Override
    public void cancelOrderSuccess(String orderNo) {

    }

    @OnClick({R.id.tv_pay, R.id.tv_contract, R.id.tv_order_trace, R.id.tv_trace, R.id.tv_confirm_receive})
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
                presenter.onTrace(orderNo);
                break;

            //订单追踪
            case R.id.tv_trace:
                presenter.onTrace(orderNo);
                break;

            //确认收货
            case R.id.tv_confirm_receive:
                showConfirmDialog();
                break;

            default:
                break;

        }
    }

    /**
     * 底部有三种样式，这是处理方法
     */
    private void dealFooterView(int status) {
        switch (status) {
            //待付款
            case OrderState.WAITINGPAY:
                rlPay.setVisibility(View.VISIBLE);
                llTrace.setVisibility(View.GONE);
                llTraceAndReceive.setVisibility(View.GONE);
                tvPay.setText("去支付");
                break;
            //待首付
            case OrderState.DOWNPAYMENT:
                rlPay.setVisibility(View.VISIBLE);
                llTrace.setVisibility(View.GONE);
                llTraceAndReceive.setVisibility(View.GONE);
                tvPay.setText("去首付");
                break;
            //订单已发货
            case OrderState.WAITINGRECEIVE:
                rlPay.setVisibility(View.GONE);
                llTrace.setVisibility(View.GONE);
                llTraceAndReceive.setVisibility(View.VISIBLE);
                break;
            //订单已完成
            case OrderState.FINISHED:
                rlPay.setVisibility(View.GONE);
                llTrace.setVisibility(View.VISIBLE);
                llTraceAndReceive.setVisibility(View.GONE);
                break;
            //订单已关闭
            case OrderState.CLOSED:
                rlFooter.setVisibility(View.GONE);
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

}

