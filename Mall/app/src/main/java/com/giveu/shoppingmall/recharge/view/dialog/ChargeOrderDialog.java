package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;


/**
 * 充值订单详情的弹框
 */
public class ChargeOrderDialog {
    // private CallsParam callsParam;
    private CustomDialog mDialog;
    private ImageView iv_dialog_close;
    //sumPrice 实际付款
    private TextView tv_recharge_amount, tv_recharge_phone, tv_price, tv_preferential_price, tv_sum, tv_payment_type;
    private ClickEnabledTextView tv_payment;
    private LinearLayout ll_payment_type;
    private CheckBox cb_agreement;
    //充值显示的价格（没使用优惠券的时候）
    public String OldPrice;
    //套餐的面值
    private String denomination;
    Activity mActivity;
    String paymentType = "即有钱包";//支付方式，默认
    PwdDialog pwdDialog = null;
    public ChargeOrderDialog(Activity activity) {
        this.mActivity = activity;

        //   callsParam = new CallsParam();
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_recharge, null);
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.BOTTOM, true);
        initView(contentView);
    }

    public ChargeOrderDialog(Activity activity, String phoneArea, final String rechargeAmount, final String phone, String price, String inputDenomination) {
        this.mActivity = activity;
        denomination = inputDenomination;

        //   callsParam = new CallsParam();
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_recharge, null);
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.BOTTOM, true);
        initView(contentView);
        OldPrice = price.replace("￥", "");
        tv_recharge_amount.setText(rechargeAmount);
        tv_recharge_phone.setText(phone.replace(" ", "-") + "【" + phoneArea + "】");
        tv_price.setText(price);
        tv_sum.setText(price);

        pwdDialog = new PwdDialog(mActivity);
    }

    public void showDialog() {
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void initView(final View contentView) {


        iv_dialog_close = (ImageView) contentView.findViewById(R.id.iv_dialog_close);
        tv_recharge_amount = (TextView) contentView.findViewById(R.id.tv_recharge_amount);
        tv_payment_type = (TextView) contentView.findViewById(R.id.tv_payment_type);
        tv_recharge_phone = (TextView) contentView.findViewById(R.id.tv_recharge_phone);
        tv_price = (TextView) contentView.findViewById(R.id.tv_price);
        ll_payment_type = (LinearLayout) contentView.findViewById(R.id.ll_payment_type);
        tv_payment = (ClickEnabledTextView) contentView.findViewById(R.id.tv_payment);
        cb_agreement = (CheckBox) contentView.findViewById(R.id.cb_agreement);
        tv_payment.setClickEnabled(true);
//
        tv_sum = (TextView) contentView.findViewById(R.id.tv_sum);
//        isCheckBoxChecked = cb_agreement.isChecked();
//        ll_recharge_preferential = (LinearLayout) contentView.findViewById(R.id.ll_recharge_preferential);

        setListener();

    }

    private void setListener() {

        //勾选协议
        cb_agreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_payment.setClickEnabled(true);
                } else {
                    tv_payment.setClickEnabled(false);
                }
            }
        });

        //关闭弹窗
        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });
        final PaymentTypeDialog paymentTypeDialog = new PaymentTypeDialog(mActivity,  paymentType);
        ll_payment_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择支付方式
                paymentTypeDialog.showDialog();
                paymentTypeDialog.setdismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        tv_payment_type.setText(paymentTypeDialog.paymentType + "");
                        CommonUtils.closeSoftKeyBoard(pwdDialog.inputView.getWindowToken(),mActivity);
                    }
                });


            }
        });
        //付款按钮
        tv_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_payment.isClickEnabled()) {

                    pwdDialog.showDialog();
                    CommonUtils.openSoftKeyBoard(mActivity);
                    pwdDialog.setdismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            CommonUtils.closeSoftKeyBoard(pwdDialog.inputView.getWindowToken(),mActivity);
                        }
                    });
                } else {
                    ToastUtils.showShortToast("请勾选用户协议");
                }
            }
        });
    }
}