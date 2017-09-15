package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.me.view.activity.OrderInfoActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ConfirmPayResponse;
import com.giveu.shoppingmall.model.bean.response.OrderDetailResponse;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.PayUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.CountDownTextView;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/9/11.
 * 支付方式
 */

public class PayChannelActivity extends BaseActivity {
    @BindView(R.id.tv_remain_time)
    CountDownTextView tvRemainTime;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.ll_pay_fail)
    LinearLayout llPayFail;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.ll_pay_status)
    LinearLayout llPayStatus;
    private ConfirmDialog cancelDialog;
    private boolean isOrderValid = true;//订单是否有效，有效的话返回上个页面的时候给予提示
    private String orderNo;
    private String paymentNum;
    private String alipayStr;
    private String timeLeft;

    public static void startIt(Activity activity, String orderNo, String paymentNum, String alipayStr) {
        Intent intent = new Intent(activity, PayChannelActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("paymentNum", paymentNum);
        intent.putExtra("alipayStr", alipayStr);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay_channel);
        baseLayout.setTitle("支付首付金额");
        orderNo = getIntent().getStringExtra("orderNo");
        alipayStr = getIntent().getStringExtra("alipayStr");
        paymentNum = StringUtils.format2(getIntent().getStringExtra("paymentNum"));
        tvMoney.setText("¥" + paymentNum);
        getRestTime();
        cancelDialog = new ConfirmDialog(mBaseContext);
        cancelDialog.setContent("是否放弃支付?");
        cancelDialog.setConfirmStr("取消");
        cancelDialog.setCancleStr("确定");
        baseLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOrderValid) {
                    cancelDialog.show();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void setListener() {
        super.setListener();
        cancelDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                cancelDialog.dismiss();
            }

            @Override
            public void cancle() {
                cancelDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void setData() {

    }

    @OnClick({R.id.tv_back, R.id.tv_confirm, R.id.tv_order})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;

            case R.id.tv_confirm:
                final ConfirmPayResponse confirmPayResponse = new ConfirmPayResponse();
                confirmPayResponse.payPrice = paymentNum;
                PayUtils.AliPay(mBaseContext, alipayStr, new PayUtils.AliPayThread.OnPayListener() {
                    @Override
                    public void onSuccess() {
                        BaseApplication.getInstance().finishActivity(ConfirmOrderActivity.class);
                        OrderPayResultActivity.startIt(mBaseContext, confirmPayResponse, orderNo, true);
                        finish();
                    }

                    @Override
                    public void onFail() {
                        BaseApplication.getInstance().finishActivity(ConfirmOrderActivity.class);
                        OrderPayResultActivity.startIt(mBaseContext, confirmPayResponse, orderNo, false);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showShortToast("取消支付");
                    }
                });
                break;

            case R.id.tv_order:
                OrderInfoActivity.startIt(mBaseContext, orderNo);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isOrderValid) {
            cancelDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    //获取剩余时间
    private void getRestTime() {
        ApiImpl.getOrderDetail(mBaseContext, Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), orderNo, new BaseRequestAgent.ResponseListener<OrderDetailResponse>() {
            @Override
            public void onSuccess(OrderDetailResponse response) {
                if (response != null) {
                    if (StringUtils.isNotNull(response.data.remainingTime)) {
                        timeLeft = response.data.remainingTime;
                        tvRemainTime.setRestTime(StringUtils.string2Long(timeLeft));
                        tvRemainTime.startCount(new CountDownTextView.CountEndListener() {
                            @Override
                            public void onEnd() {
                                isOrderValid = false;
                                llPayStatus.setVisibility(View.GONE);
                                llPayFail.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        BaseApplication.getInstance().finishActivity(ConfirmOrderActivity.class);
                        isOrderValid = false;
                        llPayStatus.setVisibility(View.GONE);
                        llPayFail.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) {

            }
        });
    }

}
