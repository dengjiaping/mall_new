package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.recharge.view.activity.RechargeStatusActivity;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;

import java.util.Random;

/**
 * 充值订单详情的弹框
 */
public class ChargeOrderDialog {
    // private CallsParam callsParam;
    private CustomDialog mDialog;
    private ImageView iv_dialog_close;
    //sumPrice 实际付款
    private TextView tv_recharge_amount, tv_recharge_phone, tv_price, tv_preferential_price, sumPrice;
    private ClickEnabledTextView tv_payment;
    private CheckBox cb_dialog;
    //充值显示的价格（没使用优惠券的时候）
    public String OldPrice;

    //套餐的面值
    private String denomination;
    Activity mActivity;

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
        sumPrice.setText(price);
    }

    public void showDialog() {
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void initView(final View contentView) {
        iv_dialog_close = (ImageView) contentView.findViewById(R.id.iv_dialog_close);
        tv_recharge_amount = (TextView) contentView.findViewById(R.id.tv_recharge_amount);
        tv_recharge_phone = (TextView) contentView.findViewById(R.id.tv_recharge_phone);
        tv_price = (TextView) contentView.findViewById(R.id.tv_price);
//        tv_preferential_price = (TextView) contentView.findViewById(R.id.tv_preferential_price);
        tv_payment = (ClickEnabledTextView) contentView.findViewById(R.id.tv_payment);
        cb_dialog = (CheckBox) contentView.findViewById(R.id.cb_dialog);
        tv_payment.setClickEnabled(true);
//
        sumPrice = (TextView) contentView.findViewById(R.id.tv_sum);
//        isCheckBoxChecked = cb_dialog.isChecked();
//        ll_recharge_preferential = (LinearLayout) contentView.findViewById(R.id.ll_recharge_preferential);

        setListener();

    }

    private void setListener() {

        //勾选协议
        cb_dialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tv_payment.setClickEnabled(true);
                }else{
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


        //付款按钮
        tv_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(tv_payment.isClickEnabled()){
                   Random random = new Random();
                   int randomNum = random.nextInt(2)+1;
                   switch (randomNum){
                       case 1:
                           RechargeStatusActivity.startIt(mActivity,"fail","很抱歉，本次支付失败，请重新发起支付","100.00元","98.00元",null);
                           break;
                       case 2:
                           RechargeStatusActivity.startIt(mActivity,"success",null,"100.00元","98.00元","温馨提示：预计10分钟到账，充值高峰可能会有延迟，可在个人中心-我的订单查看充值订单状态");
                           break;
                   }

                   mDialog.dismiss();
                }else{
                    ToastUtils.showShortToast("请勾选用户协议");
                }
//                final PwdDialog dialog = new PwdDialog();
//                dialog.showDialog(callsParam, mActivity, null);
//                //dialog关闭时收起键盘
//                dialog.setdismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        CommonUtils.closeSoftKeyBoard(mActivity);
//                    }
//                });
//                callsParam.tabIndex = tabIndex;
//                callsParam.mobile = number.replace(" ", "");
//                callsParam.name = username;
//                callsParam.oper = oper;
//                callsParam.pid = pid;
//                callsParam.price = denomination;
//                callsParam.product = product;
//                if (callsParam.discountPrice == null) {
//                    callsParam.discountPrice = OldPrice;//默认传的折扣价格
//                }

            }
        });
    }
}