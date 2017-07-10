package com.giveu.shoppingmall.me.view.dialog;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
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
                if (StringUtils.isNotNull(etMoney.getText().toString()) && Double.parseDouble(etMoney.getText().toString()) > 0) {
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

        //限制最多输入两位小数，并且如果是小数点开头自动补全0
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etMoney.setText(s);
                        etMoney.setSelection(s.length());
                    }
                }
                if (s.toString().startsWith(".")) {
                    s = "0" + s;
                    etMoney.setText(s);
                    etMoney.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etMoney.setText(s.subSequence(0, 1));
                        etMoney.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
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
