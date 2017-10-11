package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.event.OrderDialogEvent;
import com.giveu.shoppingmall.index.view.activity.OrderPayResultActivity;
import com.giveu.shoppingmall.index.view.activity.PayChannelActivity;
import com.giveu.shoppingmall.me.presenter.VerifyPresenter;
import com.giveu.shoppingmall.me.view.agent.IVerifyView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ConfirmOrderResponse;
import com.giveu.shoppingmall.model.bean.response.ConfirmPayResponse;
import com.giveu.shoppingmall.model.bean.response.EnchashmentCreditResponse;
import com.giveu.shoppingmall.recharge.view.activity.RechargeStatusActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.EventBusUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.PassWordInputView;
import com.giveu.shoppingmall.widget.SendCodeTextView;

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
    @BindView(R.id.tv_divider)
    TextView tvDivider;
    @BindView(R.id.tv_unreceived)
    TextView tvUnreceived;
    private VerifyPresenter presenter;
    private String statusType;
    private String codeType = "";
    private String smsCode;
    private String mobile;
    private long productId;
    private String orderNo;
    private int paymentType;
    private boolean isForShopping;//是否从商城过来的
    private boolean isWalletPay;//是否钱包支付，true：验证成功跳转到支付成功界面，false：验证成功跳转支付宝微信支付界面

    public static final String CASH = "cash";
    public static final String RECHARGE = "recharge";
    public static final String BANKCARD = "bankCard";
    public static final String SHOPPING = "shopping";
    private String salePrice;
    private String randomCode;
    private String paymentNum;//商城订单支付金额

    public static void startIt(Activity activity, String insuranceFee, String statusType, String creditAmount, String creditType, String idProduct, String randCode, String chooseBankName, String chooseBankNo) {
        Intent intent = new Intent(activity, VerifyActivity.class);
        intent.putExtra("insuranceFee", insuranceFee);
        intent.putExtra("statusType", statusType);
        intent.putExtra("creditAmount", creditAmount);
        intent.putExtra("creditType", creditType);
        intent.putExtra("idProduct", idProduct);
        intent.putExtra("randCode", randCode);
        intent.putExtra("chooseBankName", chooseBankName);
        intent.putExtra("chooseBankNo", chooseBankNo);
        activity.startActivity(intent);
    }

    public static void startItForShopping(Activity activity, String orderNo, boolean isWalletPay, String paymentNum) {
        Intent intent = new Intent(activity, VerifyActivity.class);
        intent.putExtra("isForShopping", true);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("statusType", SHOPPING);
        intent.putExtra("isWalletPay", isWalletPay);//是否钱包支付
        intent.putExtra("paymentNum", paymentNum);//支付金额

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
    public static void startItForRecharge(Activity activity, String mobile, long productId, String orderNo, int paymentType, String salePrice) {
        Intent intent = new Intent(activity, VerifyActivity.class);
        intent.putExtra("statusType", RECHARGE);
        intent.putExtra("mobile", mobile);
        intent.putExtra("productId", productId);
        intent.putExtra("paymentType", paymentType);
        intent.putExtra("salePrice", salePrice);
        intent.putExtra("orderNo", orderNo);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_verify);
        baseLayout.setTitle("验证");
        //   tvSendCode.startCount(null);
        inputViewPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
        tvSendCode.setSendTextColor(false);
        presenter = new VerifyPresenter(this);
        statusType = getIntent().getStringExtra("statusType");
        //是否从商城过来的
        isForShopping = getIntent().getBooleanExtra("isForShopping", false);
        if (isForShopping) {
            tvDivider.setVisibility(View.VISIBLE);
            tvUnreceived.setVisibility(View.VISIBLE);
            tvSendCode.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_767876));
            paymentNum = getIntent().getStringExtra("paymentNum");
            isWalletPay = getIntent().getBooleanExtra("isWalletPay", false);
        } else {
            tvDivider.setVisibility(View.GONE);
            tvUnreceived.setVisibility(View.GONE);
            tvSendCode.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_00bbc0));
        }
        //充值所需参数
        mobile = getIntent().getStringExtra("mobile");
        productId = getIntent().getLongExtra("productId", 0);
        paymentType = getIntent().getIntExtra("paymentType", 0);
        orderNo = getIntent().getStringExtra("orderNo");
        salePrice = getIntent().getStringExtra("salePrice");

        tvPhone.setText(hintPhone(LoginHelper.getInstance().getPhone()));
        //弹出键盘，需求
        CommonUtils.openSoftKeyBoard(mBaseContext);
        switch (statusType) {
            case CASH:
                codeType = "enchashment";
                break;

            case RECHARGE:
                codeType = "recharge";
                break;
            case SHOPPING:
                codeType = "shop";
                break;
        }
        baseLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当前界面是充值过来的，点击返回键退出时，让充值界面的订单框弹出来
                if (RECHARGE.equals(statusType)) {
                    ToastUtils.showShortToast("支付取消");
                    EventBusUtils.poseEvent(new OrderDialogEvent());
                }
                finish();
            }
        });
        presenter.sendSMSCode(LoginHelper.getInstance().getPhone(), codeType);
    }

    /**
     * 隐藏中间四位数，如13786614023
     */
    public String hintPhone(String phone) {
        if (StringUtils.isNotNull(phone)) {
            if (phone.length() > 4) {
                String strEnd = phone.substring(phone.length() - 4, phone.length());//4023
                strEnd = "****" + strEnd;//****4023
                String strStart = phone.substring(0, 3);//137
                return strStart + strEnd;
            }
        }
        return phone;
    }


    @Override
    public void setListener() {
        super.setListener();
        inputViewPwd.setInputCallBack(new PassWordInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                if (6 == result.length()) {
                    smsCode = result;
                    switch (statusType) {
                        case CASH:
                            presenter.checkSMSCode(LoginHelper.getInstance().getPhone(), result, codeType);
                            break;

                        case RECHARGE:
                            presenter.confirmRechargeOrder(LoginHelper.getInstance().getIdPerson(), mobile, productId, orderNo,
                                    paymentType, result, LoginHelper.getInstance().getPhone());
                            break;
                        case SHOPPING:
                            int payType;
                            if (isWalletPay) {
                                payType = 0;
                            } else {
                                payType = 2;
                            }
                            presenter.confirmPayForShop(Const.CHANNEL, orderNo, LoginHelper.getInstance().getIdPerson(), payType, smsCode, LoginHelper.getInstance().getPhone());
                            break;
                    }
                }
            }
        });
    }

    @OnClick({R.id.tv_send_code, R.id.tv_unreceived})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send_code:
                presenter.sendSMSCode(LoginHelper.getInstance().getPhone(), codeType);
                break;

            case R.id.tv_unreceived:
                View contentView = View.inflate(mBaseContext, R.layout.dialog_code_hint, null);
                ImageView ivDiss = (ImageView) contentView.findViewById(R.id.iv_dismiss);
                final CustomDialog unReceivedDialog = new CustomDialog(mBaseContext, contentView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
                ivDiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unReceivedDialog.dismiss();
                    }
                });
                unReceivedDialog.show();
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
        switch (statusType) {
            case CASH:
                String insuranceFee = getIntent().getStringExtra("insuranceFee");
                String creditAmount = getIntent().getStringExtra("creditAmount");
                String idProduct = getIntent().getStringExtra("idProduct");
                final String creditType = getIntent().getStringExtra("creditType");
                String randcode = getIntent().getStringExtra("randCode");
                String chooseBankName = getIntent().getStringExtra("chooseBankName");
                String chooseBankNo = getIntent().getStringExtra("chooseBankNo");

                ApiImpl.addEnchashmentCredit(mBaseContext, insuranceFee, chooseBankName, chooseBankNo, creditAmount, creditType, LoginHelper.getInstance().getIdPerson(), idProduct, LoginHelper.getInstance().getPhone(), randcode, smsCode, new BaseRequestAgent.ResponseListener<EnchashmentCreditResponse>() {
                    @Override
                    public void onSuccess(EnchashmentCreditResponse response) {
                        if (response.data != null) {
                            EnchashmentCreditResponse ecResponse = response.data;
                            CashFinishStatusActivity.startIt(mBaseContext, "success", response.message, ecResponse.creditAmount, ecResponse.repayNum, ecResponse.deductDate, creditType);
                            //更新取现可用额度
                            LoginHelper.getInstance().setAvailablePoslimit(ecResponse.creditAmount);
                            BaseApplication.getInstance().finishActivity(CashTypeActivity.class);
                            finish();
                            BaseApplication.getInstance().fetchUserInfo();
                        }
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        CashFinishStatusActivity.startIt(mBaseContext, "fail", errorBean.message);
                        BaseApplication.getInstance().finishActivity(CashTypeActivity.class);
                        finish();
                    }
                });
                break;
            case RECHARGE:
                break;
            case SHOPPING:
                break;

        }

    }

    @Override
    public void onBackPressed() {
        //当前界面是充值过来的，点击返回键退出时，让充值界面的订单框弹出来
        if (RECHARGE.equals(statusType)) {
            ToastUtils.showShortToast("支付取消");
            EventBusUtils.poseEvent(new OrderDialogEvent());
        }
        super.onBackPressed();
    }

    @Override
    public void confirmOrderSuccess(ConfirmOrderResponse data) {
        //充值成功后，需更新额度，因此更新个人信息
        BaseApplication.getInstance().fetchUserInfo();
        RechargeStatusActivity.startIt(mBaseContext, "success", null, salePrice + "元", salePrice + "元");
        finish();
    }

    @Override
    public void confirmOrderFail(String message) {
        RechargeStatusActivity.startIt(mBaseContext, "fail", StringUtils.isNotNull(message) ? message : "很抱歉，本次支付失败，请重新发起支付", salePrice + "元", salePrice + "元");
        finish();
    }


    /**
     * 商城订单短信验证成功的回调方法
     * 如果是钱包支付，则跳转到支付成功界面
     * 如果是其他方式支付，则跳转支付宝或微信支付界面
     */
    @Override
    public void confirmPaySuccess(ConfirmPayResponse data) {
        if (isWalletPay) {
            if (data != null) {
                BaseApplication.getInstance().finishAllExceptMainActivity();
                //钱包支付后刷新额度
                BaseApplication.getInstance().fetchUserInfo();
                OrderPayResultActivity.startIt(mBaseContext, data, orderNo, true);
            }
        } else {
            PayChannelActivity.startIt(mBaseContext, orderNo, paymentNum, data.alipay, data.payId);
        }
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

}
