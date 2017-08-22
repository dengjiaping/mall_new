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

import com.android.volley.mynet.ApiUrl;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.index.view.activity.PerfectContactsActivity;
import com.giveu.shoppingmall.index.view.activity.TransactionPwdActivity;
import com.giveu.shoppingmall.me.view.activity.CustomWebViewActivity;
import com.giveu.shoppingmall.me.view.activity.LivingAddressActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;
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
    private TextView tv_recharge_amount, tv_recharge_phone, tv_price, tv_agreement, tv_sum, tv_payment_type;
    private ClickEnabledTextView tv_payment;
    private LinearLayout ll_payment_type;
    private CheckBox cb_agreement;
    //充值显示的价格（没使用优惠券的时候）
    public String OldPrice;
    Activity mActivity;
    private TextView tvProduct;
    String paymentTypeStr = "即有钱包";//支付方式，默认
    int paymentType = 0;//支付方式，默认即有钱包
    private PaymentTypeDialog paymentTypeDialog;

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
        //假资质用户只有支付宝
        if (LoginHelper.getInstance().hasAverageUser()) {
            paymentTypeStr = "支付宝";
        }
        paymentTypeDialog = new PaymentTypeDialog(mActivity, paymentTypeStr);

//        pwdDialog = new PwdDialog(mActivity, PwdDialog.statusType.RECHARGE);
    }

    public void setDefaultPay(int defaultPay) {
        switch (defaultPay) {
            default:
            case 0:
                paymentTypeStr = "即有钱包";
                paymentType = 0;

                break;
            case 1:
                paymentTypeStr = "微信支付";
                paymentType = 1;
                break;
            case 2:
                paymentTypeStr = "支付宝";
                paymentType = 2;
                break;
        }
        tv_payment_type.setText(paymentTypeStr);
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
        tv_agreement = (TextView) contentView.findViewById(R.id.tv_agreement);
        ll_payment_type = (LinearLayout) contentView.findViewById(R.id.ll_payment_type);
        tv_payment = (ClickEnabledTextView) contentView.findViewById(R.id.tv_payment);
        cb_agreement = (CheckBox) contentView.findViewById(R.id.cb_agreement);
        tv_payment.setClickEnabled(true);
        tvProduct = (TextView) contentView.findViewById(R.id.tv_product);
//
        tv_sum = (TextView) contentView.findViewById(R.id.tv_sum);
//        isCheckBoxChecked = cb_agreement.isChecked();
//        ll_recharge_preferential = (LinearLayout) contentView.findViewById(R.id.ll_recharge_preferential);
        CommonUtils.setTextWithSpan(tv_agreement, false, "已阅读并同意", "《贷款及咨询服务合同标准条款》", R.color.black, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //贷款及咨询服务合同标准条款
                CustomWebViewActivity.startIt(mActivity, ApiUrl.WebUrl.oConsumeLoanStatic, "《贷款及咨询服务合同标准条款》");
            }
        });

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
        ll_payment_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择支付方式，因为关闭了微信支付，所以需处理
                if (paymentType >= 1) {
                    //假资质用户默认显示支付宝支付
                    if (LoginHelper.getInstance().hasAverageUser()) {
                        paymentTypeDialog.showDialog(0);
                    } else {
                        paymentTypeDialog.showDialog(1);
                    }
                } else {
                    paymentTypeDialog.showDialog(paymentType);
                }
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
                    //非假资质用户需要补充个人信息
                    if (!LoginHelper.getInstance().hasAverageUser()) {
                        canShowPwdDialog();
                    } else {
                        if (listener != null) {
                            listener.onConfirm(paymentType);
                        }
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
                    ToastUtils.showShortToast("请勾选贷款及咨询服务合同标准条款");
                }
            }
        });
    }


    /**
     * 资料是否完善的判断
     */
    public void canShowPwdDialog() {
        //非钱包支付不用判断，直接确认付款
        if (paymentType != 0) {
            if (listener != null) {
                listener.onConfirm(paymentType);
            }
            return;
        }
        if (LoginHelper.getInstance().hasExistOther()) {
            //添加了联系人
            if (LoginHelper.getInstance().hasExistLive()) {
                //添加了居住地址,判断是否设置了交易密码
                if (LoginHelper.getInstance().hasSetPwd()) {
                    if (listener != null) {
                        listener.onConfirm(paymentType);
                    }
                } else {
                    TransactionPwdActivity.startIt(mActivity, LoginHelper.getInstance().getIdPerson());
                }
            } else {
                //未添加地址
                LivingAddressActivity.startIt(mActivity);
            }
        } else {
            //未添加联系人
            PerfectContactsActivity.startIt(mActivity, Const.CASH);
        }
    }

    public interface OnConfirmListener {
        void onConfirm(int paymentType);
    }

    private OnConfirmListener listener;

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }
}