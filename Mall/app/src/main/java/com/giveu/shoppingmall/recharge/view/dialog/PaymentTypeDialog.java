package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.utils.LoginHelper;

/**
 * 支付方式弹窗
 * Created by 101900 on 2017/6/28.
 */

public class PaymentTypeDialog {
    private CustomDialog mDialog;
    private Activity mActivity;
    private ImageView iv_back;
    private LinearLayout llWallet;
    private LinearLayout llWeChat;
    private LinearLayout llALi;
    private CheckBox cbWalletCheck;
    private CheckBox cbWechatCheck;
    private CheckBox cbALiCheck;
    public String paymentType;//支付类型

    public PaymentTypeDialog(Activity mActivity) {
        this.mActivity = mActivity;
        View contentView = View.inflate(mActivity, R.layout.dialog_payment_type, null);
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.BOTTOM, true);
        initView(contentView);
        mDialog.setCancelable(false);
    }

    public void showDialog(int defaulChoose) {
        resetCheck();
        switch (defaulChoose) {
            case 0:
                cbWalletCheck.setChecked(true);
                break;
            case 1:
                cbWechatCheck.setChecked(true);
                break;
            case 2:
                cbALiCheck.setChecked(true);
                break;
        }
        mDialog.show();
    }

    private void initView(View contentView) {
        iv_back = (ImageView) contentView.findViewById(R.id.iv_back);
        llWallet = (LinearLayout) contentView.findViewById(R.id.ll_wallet);
        cbWalletCheck = (CheckBox) contentView.findViewById(R.id.cb_wallet_check);
        llWeChat = (LinearLayout) contentView.findViewById(R.id.ll_wechat);
        cbWechatCheck = (CheckBox) contentView.findViewById(R.id.cb_wechat_check);
        llALi = (LinearLayout) contentView.findViewById(R.id.ll_ali);
        cbALiCheck = (CheckBox) contentView.findViewById(R.id.cb_ali_check);
        if (LoginHelper.getInstance().hasAverageUser()) {
            llWallet.setVisibility(View.GONE);
            cbALiCheck.setChecked(true);
        }
        setListener();
    }

    private void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        llWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbWalletCheck.performClick();
            }
        });
        llWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbWechatCheck.performClick();
            }
        });

        llALi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbALiCheck.performClick();
            }
        });
        cbWalletCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCheck();
                cbWalletCheck.setChecked(true);
                onChoosePaymentType(0, "即有钱包");
            }
        });
        cbWechatCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCheck();
                cbWechatCheck.setChecked(true);
                onChoosePaymentType(1, "微信支付");
            }
        });

        cbALiCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCheck();
                cbALiCheck.setChecked(true);
                onChoosePaymentType(2, "支付宝");
            }
        });
    }

    private void resetCheck() {
        cbWalletCheck.setChecked(false);
        cbWechatCheck.setChecked(false);
        cbALiCheck.setChecked(false);
    }

    /**
     * 点击改变选中的支付类型，获取的支付类型名称，并关闭支付弹窗
     *
     * @param paymentType 支付方式
     */
    public void onChoosePaymentType(int paymentType, String paymentTypeStr) {
        if (listener != null) {
            listener.onChooseType(paymentType, paymentTypeStr);
        }
        mDialog.dismiss();
    }

    public interface OnChoosePayTypeListener {
        //选中的支付方式，0为钱包，1为微信，2为支付宝
        void onChooseType(int type, String paymentTypeStr);
    }

    private OnChoosePayTypeListener listener;

    public void setOnChoosePayTypeListener(OnChoosePayTypeListener listener) {
        this.listener = listener;
    }
}
