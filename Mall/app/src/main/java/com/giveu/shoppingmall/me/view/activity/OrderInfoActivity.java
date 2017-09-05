package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 101912 on 2017/8/29.
 */

public class OrderInfoActivity extends BaseActivity implements IOrderInfoView<OrderDetailResponse> {

    @BindView(R.id.tv_time_left)
    TextView tvTimeLeft;
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
    @BindView(R.id.tv_comfirm_receive)
    TextView tvConfirmReceive;
    @BindView(R.id.ll_trace)
    LinearLayout llTrace;
    @BindView(R.id.tv_order_trace)
    TextView tvOrderTrace;
    @BindView(R.id.tv_contract)
    TextView tvContract;
    @BindView(R.id.cb_contract)
    CheckBox cbContract;


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
        //订单号
        if (StringUtils.isNotNull(response.orderNo)) {
            tvOrderNo.setText(response.orderNo);
        }
        //订单status


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
            tvQuantity.setText(response.skuInfo.quantity);
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
            tvDownPayment.setText(response.selDownPaymentRate + "(¥+" + response.downPayment + ")");
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
            tvTotal.setText(response.totalPrice);
        }

    }
}
