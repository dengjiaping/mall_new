package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.view.activity.MyOrderActivity;
import com.giveu.shoppingmall.me.view.activity.OrderInfoActivity;
import com.giveu.shoppingmall.model.bean.response.ConfirmPayResponse;
import com.giveu.shoppingmall.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 101912 on 2017/9/12.
 */

public class OrderPayResultActivity extends BaseActivity {

    @BindView(R.id.ll_pay_success)
    LinearLayout llPaySuccess;
    @BindView(R.id.ll_pay_fail)
    LinearLayout llPayFail;
    @BindView(R.id.ll_pay_price)
    LinearLayout llPayPrice;
    @BindView(R.id.tv_pay_price)
    TextView tvPayPrice;
    @BindView(R.id.ll_iniypay)
    LinearLayout llIniypay;
    @BindView(R.id.tv_iniypay)
    TextView tvIniypay;
    @BindView(R.id.ll_payment_num)
    LinearLayout llPaymentNum;
    @BindView(R.id.tv_payment_num)
    TextView tvPaymentNum;
    @BindView(R.id.ll_latest_date)
    LinearLayout llLatestDate;
    @BindView(R.id.tv_latest_date)
    TextView tvLatestDate;

    private boolean isSuccess;//是否支付成功
    private ConfirmPayResponse response;
    private String orderNo;

    public static void startIt(Activity activity, ConfirmPayResponse response, String orderNo, boolean isSuccess) {
        Intent intent = new Intent(activity, OrderPayResultActivity.class);
        intent.putExtra("ConfirmPayResponse", response);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("isSuccess", isSuccess);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_pay_result);
        baseLayout.setTitle("订单支付");
        orderNo = getIntent().getStringExtra("orderNo");
        isSuccess = getIntent().getBooleanExtra("isSuccess", false);
        response = (ConfirmPayResponse) getIntent().getSerializableExtra("ConfirmPayResponse");
        if (isSuccess) {
            llPaySuccess.setVisibility(View.VISIBLE);
            llPayFail.setVisibility(View.GONE);
        } else {
            llPaySuccess.setVisibility(View.GONE);
            llPayFail.setVisibility(View.VISIBLE);
        }
        //首付金额
        if (StringUtils.isNotNull(response.iniypay)) {
            tvIniypay.setText(StringUtils.format2(response.iniypay) + "元");
        } else {
            llIniypay.setVisibility(View.GONE);
        }
        //最迟还款日
        if (StringUtils.isNotNull(response.latesRepayDate)) {
            tvLatestDate.setText(response.latesRepayDate);
        } else {
            llLatestDate.setVisibility(View.GONE);
        }
        //订单金额
        if (StringUtils.isNotNull(response.payPrice)) {
            tvPayPrice.setText(StringUtils.format2(response.payPrice) + "元");
        } else {
            llPayPrice.setVisibility(View.GONE);
        }
        //分期数
        if (StringUtils.isNotNull(response.paymentNum)) {
            tvPaymentNum.setText(response.paymentNum + "期");
        } else {
            llPaymentNum.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.tv_check_order, R.id.tv_repay})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_check_order:
                MyOrderActivity.startIt(mBaseContext, OrderState.ALL_RESPONSE);
                finish();
                break;

            case R.id.tv_repay:
                OrderInfoActivity.startIt(mBaseContext, orderNo);
                finish();
                break;
        }
    }

    @Override
    public void setData() {

    }
}
