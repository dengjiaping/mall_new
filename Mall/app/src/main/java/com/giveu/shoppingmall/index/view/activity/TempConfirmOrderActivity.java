package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.view.agent.IConfirmOrderListener;
import com.giveu.shoppingmall.index.view.fragment.ConfirmOrderFragment;
import com.giveu.shoppingmall.index.widget.StableEditText;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.InsuranceFee;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;

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
    //消费者分期合同
    @BindView(R.id.confirm_order_agreement_layout)
    LinearLayout llAgreementLayout;
    @BindView(R.id.confirm_order_agreement)
    TextView tvAgreement;
    @BindView(R.id.confirm_order_agreement_checkbox)
    CheckBox cbAgreement;

    private int downPaymentRate; //首付比例
    private int quantity; //商品数量
    private String skuCode;//商品skuCode
    private long idProduct; //分期id
    private int payType; //付款方式
    private int paymentNum;
    private String customerName = null; //客户姓名
    private String customerPhone = null;//客户电话
    private CreateOrderResponse.ReceiverJoBean receiverJoBean = null; //客户收货地址

    private ConfirmOrderFragment fragment = null;
    private String paymentPrice = "0";
    private String totalPrice = "0";
    private String cardPrice = "0";
    private long cardId = 0; //优惠券Id

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
            paymentNum = getIntent().getIntExtra("paymentNum", 0);
        }

        customerName = LoginHelper.getInstance().getName();
        customerPhone = LoginHelper.getInstance().getPhone();

        initMsgEditText();

        showLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        fragment = ConfirmOrderFragment.newInstance(skuCode, downPaymentRate, quantity, payType, idProduct, paymentNum);
        getSupportFragmentManager().beginTransaction().replace(R.id.confirm_order_content, fragment).commitNow();
        fragment.initDataDelay();
    }

    public static void startIt(Context context, int downPaymentRate, long idProduct, int paymentNum, int quantity, String skuCode, int paymentType) {
        Intent intent = new Intent(context, TempConfirmOrderActivity.class);
        intent.putExtra("downPaymentRate", downPaymentRate);
        intent.putExtra("idProduct", idProduct);
        intent.putExtra("quantity", quantity);
        intent.putExtra("paymentType", paymentType);
        intent.putExtra("skuCode", skuCode);
        intent.putExtra("paymentNum", paymentNum);
        context.startActivity(intent);
    }

    @Override
    public void onPayTypeChanged(int payType) {
        this.payType = payType;
        if (payType == 0) {
            llAgreementLayout.setVisibility(View.VISIBLE);
            setTotalPrice(paymentPrice);
        } else {
            llAgreementLayout.setVisibility(View.GONE);
            setTotalPrice();
        }
    }

    @Override
    public void onDownPaymentRateChanged(int downPaymentRate, String price) {
        this.downPaymentRate = downPaymentRate;
        this.paymentPrice = price;
        setTotalPrice(price);
    }

    @Override
    public void onTotalPriceChanged(String totalPrice) {
        this.totalPrice = totalPrice;
        setTotalPrice();
    }

    @Override
    public void onDownPaymentChanged(long idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public void onCustomerAddressChanged(CreateOrderResponse.ReceiverJoBean receiverJoBean) {
        this.receiverJoBean = receiverJoBean;
    }

    @Override
    public void onCourtesyCardIdChanged(long cardId, String cardPrice) {
        this.cardPrice = cardPrice;
        this.cardId = cardId;
        setTotalPrice();
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
        hideLoding();
        if (rlEmptyView.getVisibility() != View.GONE) {
            rlEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onInitFailed() {
        hideLoding();
        if (rlEmptyView.getVisibility() != View.VISIBLE) {
            rlEmptyView.setVisibility(View.VISIBLE);
            tvEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener reTryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (fragment != null) {
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

    private void setTotalPrice(String price) {
        double tPrice = StringUtils.string2Double(totalPrice);
        double cPrice = StringUtils.string2Double(price);
        double result = Math.max(tPrice - cPrice, 0);
        CommonUtils.setTextWithSpanSizeAndColor(tvTotalPrice, "¥ ", StringUtils.format2(result + ""), "",
                16, 11, R.color.title_color, R.color.black);
    }


    private void setTotalPrice() {
        setTotalPrice(cardPrice);
    }

    /**
     * 初始化买家留言编辑框
     */
    private void initMsgEditText() {
        //第一次进入界面EditText未获取焦点,设置显示内容
        final String stableText = "买家留言（选填）：";
        String hintText = "对本次交易的说明";
        SpannableString desc = new SpannableString(stableText + hintText);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_4a4a4a)), 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_color)), 4, 8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_4a4a4a)), 8, 9, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_cccccc)), 9, desc.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        msgEditText.setText(desc);
        //限制输入字符长度为100
        msgEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100 + stableText.length())});
        //第一次点击EditText获取焦点，重置显示内容
        msgEditText.setOnFirstFocusedListener(new StableEditText.OnFirstFocusedListener() {
            @Override
            public void firstFocused() {
                msgEditText.setSpannerStableText(stableText, 4, 8, getResources().getColor(R.color.title_color));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
