package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.view.agent.IConfirmOrderListener;
import com.giveu.shoppingmall.index.view.fragment.ConfirmOrderFragment;
import com.giveu.shoppingmall.index.widget.StableEditText;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.InsuranceFee;

import butterknife.BindView;

/**
 * Created by 524202 on 2017/10/12.
 */

public class TempConfirmOrderActivity extends BaseActivity implements IConfirmOrderListener {

    //加载时的空白界面
    @BindView(R.id.confirm_order_empty)
    RelativeLayout rlEmptyView;
    @BindView(R.id.confirm_order_empty_text)
    TextView tvEmptyTextView;
    //买家留言
    @BindView(R.id.confirm_order_msg_edit)
    StableEditText msgEditText;
    //支付按钮和价格显示
    @BindView(R.id.confirm_order_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_ok)
    TextView tvOK;

    private int downPaymentRate; //首付比例
    private int quantity; //商品数量
    private String skuCode;//商品skuCode
    private long idProduct; //分期id
    private int payType; //付款方式

    private ConfirmOrderFragment fragment = null;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temp_confirm_order_layout);
        baseLayout.setTitle("订单信息确认");
        if (getIntent() != null) {
            downPaymentRate = getIntent().getIntExtra("downPaymentRate", 0);
            idProduct = getIntent().getLongExtra("idProduct", 0);
            quantity = getIntent().getIntExtra("quantity", 0);
            skuCode = getIntent().getStringExtra("skuCode");
            payType = getIntent().getIntExtra("paymentType", 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragment = ConfirmOrderFragment.newInstance(skuCode, downPaymentRate, quantity, payType, idProduct);
        getSupportFragmentManager().beginTransaction().replace(R.id.confirm_order_content, fragment).commitNow();
        fragment.initDataDelay();
    }

    @Override
    public void setListener() {
        super.setListener();
        tvEmptyTextView.setOnClickListener(reTryListener);
        tvOK.setOnClickListener(confirmOrderListener);
        baseLayout.setBackClickListener(backListener);
    }

    @Override
    public void setData() {

    }

    public static void startIt(Context context, int downPaymentRate, long idProduct, int quantity, String skuCode, int paymentType) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra("downPaymentRate", downPaymentRate);
        intent.putExtra("idProduct", idProduct);
        intent.putExtra("quantity", quantity);
        intent.putExtra("paymentType", paymentType);
        intent.putExtra("skuCode", skuCode);
        context.startActivity(intent);
    }

    @Override
    public void onPayTypeChanged(int payType) {

    }

    @Override
    public void onDownPaymentRateChanged(int downPaymentRate) {

    }

    @Override
    public void onTotalPriceChanged(String totalPrice) {

    }

    @Override
    public void onDownPaymentChanged(long idProduct) {

    }

    @Override
    public void onCustomerAddressChanged(CreateOrderResponse.ReceiverJoBean receiverJoBean) {

    }

    @Override
    public void onCourtesyCardIdChanged(long cardId) {

    }

    @Override
    public void onInsuranceFeeChanged(InsuranceFee insuranceFee) {

    }

    @Override
    public void onInstallDateChanged(int installDateId) {

    }

    @Override
    public void onReservingDateChanged(int reservingDateId) {

    }

    @Override
    public void onInitSuccess() {
        if (rlEmptyView.getVisibility() != View.GONE) {
            rlEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onInitFailed() {
        if (rlEmptyView.getVisibility() != View.VISIBLE) {
            rlEmptyView.setVisibility(View.VISIBLE);
            tvEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener reTryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (fragment != null){
                fragment.initDataDelay();
            }
        }
    };

    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener confirmOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ConfirmOrderSc();
        }
    };

    private void ConfirmOrderSc() {

    }
}
