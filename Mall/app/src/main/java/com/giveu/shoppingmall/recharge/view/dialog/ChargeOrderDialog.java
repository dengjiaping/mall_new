package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
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
    Activity mActivity;
    private TextView tvProduct;
    String paymentTypeStr = "即有钱包";//支付方式，默认
    int paymentType = 0;//支付方式，默认即有钱包

    public ChargeOrderDialog(Activity activity) {
        this.mActivity = activity;

        //   callsParam = new CallsParam();
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_recharge, null);
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.BOTTOM, true);
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        }
        initView(contentView);
    }

    public ChargeOrderDialog(Activity activity, String phoneArea, final String rechargeAmount, final String phone, String price) {
        this.mActivity = activity;

        //   callsParam = new CallsParam();
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_recharge, null);
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.BOTTOM, true);
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        }
        initView(contentView);
        OldPrice = price.replace("￥", "");
        tv_recharge_amount.setText(rechargeAmount);
        tv_recharge_phone.setText(phone.replace(" ", "-") + "【" + phoneArea + "】");
        tv_price.setText(price);
        tv_sum.setText(price);

//        pwdDialog = new PwdDialog(mActivity, PwdDialog.statusType.RECHARGE);
    }

    public void showDialog() {
        mDialog.setCancelable(false);
        if (mActivity != null && !mActivity.isFinishing() && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dissmissDialog() {
        if (mActivity != null && !mActivity.isFinishing() && mDialog.isShowing())
            mDialog.dismiss();
    }

    private void initView(final View contentView) {


        iv_dialog_close = (ImageView) contentView.findViewById(R.id.iv_dialog_close);
        tv_recharge_amount = (TextView) contentView.findViewById(R.id.tv_date_top);
        tv_payment_type = (TextView) contentView.findViewById(R.id.tv_payment_type);
        tv_recharge_phone = (TextView) contentView.findViewById(R.id.tv_recharge_phone);
        tv_price = (TextView) contentView.findViewById(R.id.tv_price);
        ll_payment_type = (LinearLayout) contentView.findViewById(R.id.ll_payment_type);
        tv_payment = (ClickEnabledTextView) contentView.findViewById(R.id.tv_payment);
        cb_agreement = (CheckBox) contentView.findViewById(R.id.cb_agreement);
        tv_payment.setClickEnabled(true);
        tvProduct = (TextView) contentView.findViewById(R.id.tv_product);
//
        tv_sum = (TextView) contentView.findViewById(R.id.tv_sum);
//        isCheckBoxChecked = cb_agreement.isChecked();
//        ll_recharge_preferential = (LinearLayout) contentView.findViewById(R.id.ll_recharge_preferential);

        setListener();

    }

    public void setProductType(int productType) {
        switch (productType) {
            case 0:
                tvProduct.setText("充值话费");
                break;

            case 1:
                tvProduct.setText("充值流量");
                break;

            default:
                tvProduct.setText("充值话费");
                break;
        }
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
        final PaymentTypeDialog paymentTypeDialog = new PaymentTypeDialog(mActivity, paymentTypeStr);
        ll_payment_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择支付方式
                paymentTypeDialog.showDialog();
                paymentTypeDialog.setOnChoosePayTypeListener(new PaymentTypeDialog.OnChoosePayTypeListener() {
                    @Override
                    public void onChooseType(int type) {
                        paymentType = type;
                    }
                });
                paymentTypeDialog.setdismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        tv_payment_type.setText(paymentTypeDialog.paymentType + "");
//                        CommonUtils.closeSoftKeyBoard(pwdDialog.inputView.getWindowToken(), mActivity);
                    }
                });


            }
        });
        //付款按钮
        tv_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_payment.isClickEnabled()) {
                    if (listener != null) {
                        listener.onConfirm(paymentType);
                    }
/*                    pwdDialog.showDialog();
                    CommonUtils.openSoftKeyBoard(mActivity);
                    pwdDialog.setdismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            CommonUtils.closeSoftKeyBoard(pwdDialog.inputView.getWindowToken(),mActivity);
                        }
                    });*/
                } else {
                    ToastUtils.showShortToast("请勾选用户协议");
                }
            }
        });
    }

    public interface OnConfirmListener {
        void onConfirm(int paymentType);
    }

    private OnConfirmListener listener;

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }
}