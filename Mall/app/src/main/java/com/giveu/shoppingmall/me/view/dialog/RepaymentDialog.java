package com.giveu.shoppingmall.me.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;


/**
 * Created by 513419 on 2017/6/23.
 */

public class RepaymentDialog extends CustomDialog {
    private EditText etMoney;
    private TextView tvCancel;
    private TextView tvConfirm;

    public RepaymentDialog(Activity context) {
        super(context, R.layout.dialog_repayment, R.style.customerDialog, Gravity.CENTER, false);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        etMoney = (EditText) contentView.findViewById(R.id.et_money);
        tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //输入金额不为空并且金额大于0
                if (StringUtils.isNotNull(etMoney.getText().toString()) && Integer.parseInt(etMoney.getText().toString()) > 0) {
                    if (listener != null) {
                        listener.onConfirm(etMoney.getText().toString());
                        CommonUtils.closeSoftKeyBoard(mAttachActivity);
                        dismiss();
                    }
                } else {
                    ToastUtils.showShortToast("请输入还款金额");
                }

            }
        });
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        setCanceledOnTouchOutside(false);
    }

    private OnConfirmListener listener;

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

   public interface OnConfirmListener {
        void onConfirm(String money);
    }
}
