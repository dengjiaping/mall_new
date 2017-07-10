package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.index.view.activity.MainActivity;
import com.giveu.shoppingmall.me.presenter.VerifyPresenter;
import com.giveu.shoppingmall.me.view.agent.IVerifyView;
import com.giveu.shoppingmall.model.bean.response.ConfirmOrderResponse;
import com.giveu.shoppingmall.recharge.view.activity.RechargeStatusActivity;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.PayUtils;
import com.giveu.shoppingmall.widget.PassWordInputView;
import com.giveu.shoppingmall.widget.SendCodeTextView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/26.
 */

public class VerifyActivity extends BaseActivity implements IVerifyView {

    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;
    @BindView(R.id.input_view_pwd)
    PassWordInputView inputViewPwd;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    private VerifyPresenter presenter;
    public static final int REUQEST_FINISH = 10000;
    private String statusType;
    private String codeType = "";
    private String mobile;
    private long productId;
    private String orderNo;
    private int paymentType;

    public static final String CASH = "cash";
    public static final String RECHARGE = "recharge";
    public static final String BANKCARD = "bankCard";
    private long orderDetailId;
    private String salePrice;

    public static void startIt(Activity activity, String statusType) {
        Intent intent = new Intent(activity, VerifyActivity.class);
        intent.putExtra("statusType", statusType);
        activity.startActivity(intent);
    }

    /**
     * 充值短信校验
     *
     * @param activity
     * @param mobile
     * @param productId
     * @param orderNo
     * @param paymentType
     */
    public static void startItForRecharge(Activity activity, String mobile, long productId, String orderNo, int paymentType, long orderDetailId, String salePrice) {
        Intent intent = new Intent(activity, VerifyActivity.class);
        intent.putExtra("statusType", RECHARGE);
        intent.putExtra("mobile", mobile);
        intent.putExtra("productId", productId);
        intent.putExtra("paymentType", paymentType);
        intent.putExtra("orderDetailId", orderDetailId);
        intent.putExtra("salePrice", salePrice);
        intent.putExtra("orderNo", orderNo);
        activity.startActivity(intent);
    }

    /**
     * 微信支付完成
     *
     * @param mContext
     * @param payType
     */
    public static void startItAfterPay(Context mContext, String payType) {
        Intent i = new Intent(mContext, MainActivity.class);
        i.putExtra(Const.whichFragmentInActMain, 0);
        i.putExtra("payType", payType);
        mContext.startActivity(i);
    }

//    public static void startIt(Fragment fragment, String statusType) {
//        Intent intent = new Intent(fragment.getActivity(), VerifyActivity.class);
//        intent.putExtra("statusType", statusType);
//        fragment.startActivityForResult(intent, REUQEST_FINISH);
//    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_verify);
        baseLayout.setTitle("验证");
        //   tvSendCode.startCount(null);
        tvSendCode.setSendTextColor(false);
        presenter = new VerifyPresenter(this);
        statusType = getIntent().getStringExtra("statusType");
        //充值所需参数
        mobile = getIntent().getStringExtra("mobile");
        productId = getIntent().getLongExtra("productId", 0);
        paymentType = getIntent().getIntExtra("paymentType", 0);
        orderNo = getIntent().getStringExtra("orderNo");
        salePrice = getIntent().getStringExtra("salePrice");

        tvPhone.setText(LoginHelper.getInstance().getPhone());
        switch (statusType) {
            case CASH:
                break;

            case RECHARGE:
                codeType = "recharge";
                break;
        }
        presenter.sendSMSCode(LoginHelper.getInstance().getPhone(), codeType);
    }


    @Override
    public void setListener() {
        super.setListener();
        inputViewPwd.setInputCallBack(new PassWordInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                if (6 == result.length()) {
                    switch (statusType) {
                        case CASH:
                            break;

                        case RECHARGE:
                            presenter.confirmRechargeOrder(LoginHelper.getInstance().getIdPerson(), mobile, productId, orderNo,
                                    paymentType, result,LoginHelper.getInstance().getPhone());
                            break;
                    }
//                    presenter.checkSMSCode(LoginHelper.getInstance().getPhone(), result, codeType);
       /*             if ("123456".equals(result)) {
                        Random random = new Random();
                        int randomNum = random.nextInt(2) + 1;
                        switch (statusType) {
                            case PwdDialog.statusType.CASH:
                                //跳转取现状态页
                                if (randomNum == 1) {
                                    CashFinishStatusActivity.startIt(mBaseContext, "fail", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", null, null, null);
                                } else {
                                    CashFinishStatusActivity.startIt(mBaseContext, "success", null, "3000.00元", "9期", "2017/05/18");
                                }
                                break;
                            case PwdDialog.statusType.RECHARGE:
                                //跳转充值状态页
                                if (randomNum == 1) {
                                    RechargeStatusActivity.startIt(mBaseContext, "fail", "很抱歉，本次支付失败，请重新发起支付", "100.00元", "98.00元", null);
                                } else {
                                    RechargeStatusActivity.startIt(mBaseContext, "success", null, "100.00元", "98.00元", "温馨提示：预计10分钟到账，充值高峰可能会有延迟，可在个人中心-我的订单查看充值订单状态");
                                }
                                break;
                        }
                        finish();
                    } else {
                        ToastUtils.showShortToast("验证码错误");
                    }*/
                }
            }
        });
    }

    @OnClick({R.id.tv_send_code})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send_code:
                presenter.sendSMSCode(LoginHelper.getInstance().getPhone(), codeType);
                break;
        }
    }

    @Override
    public void setData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvSendCode != null) {
            tvSendCode.onDestory();
        }
    }

    @Override
    public void sendSMSSuccess() {
        tvSendCode.startCount(null);
        switch (statusType) {
            case CASH:
                break;

            case RECHARGE:
                break;
        }
    }

    @Override
    public void checkSMSSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void confirmOrderSuccess(ConfirmOrderResponse data) {
        if (data != null) {
            BaseApplication.getInstance().setBeforePayActivity(mBaseContext.getClass().getSimpleName());
            IWXAPI iWxapi = PayUtils.getWxApi();
            PayReq payReq = PayUtils.getRayReq(data.partnerid, data.prepayid, data.packageValue, data.noncestr, data.timestamp, data.sign);
            iWxapi.sendReq(payReq);
            orderDetailId = data.orderDetailId;
        } else {
            RechargeStatusActivity.startIt(mBaseContext, "success", null, salePrice + "元", salePrice + "元", "温馨提示：预计10分钟到账，充值高峰可能会有延迟，可在个人中心-我的订单查看充值订单状态");
            finish();
        }
    }

    @Override
    public void thirdPayOrderFailed() {
        RechargeStatusActivity.startIt(mBaseContext, "fail", "很抱歉，本次支付失败，请重新发起支付", salePrice + "元", salePrice + "元", null);
        finish();
    }

    @Override
    public void thirdPaySuccess() {
        RechargeStatusActivity.startIt(mBaseContext, "success", null, salePrice + "元", salePrice + "元", "温馨提示：预计10分钟到账，充值高峰可能会有延迟，可在个人中心-我的订单查看充值订单状态");
        finish();
    }

    /**
     * 手机充值微信支付结果
     */
    public void rechargeAfterWxPay() {
        presenter.thirdPayRecharge(LoginHelper.getInstance().getIdPerson(), orderDetailId, orderNo);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String payType = intent.getStringExtra("payType");
        if (RECHARGE.equals(payType)) {
            rechargeAfterWxPay();
        }
    }
}
