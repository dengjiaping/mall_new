package com.giveu.shoppingmall.me.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;

/**
 * Created by 513419 on 2017/6/23.
 */

public class RepaymentDetailDialog extends CustomDialog {
    private ImageView ivDismiss;
    private TextView tvConfirm;

    public RepaymentDetailDialog(Activity context) {
        super(context, R.layout.dialog_repayment_detail, R.style.customerDialog, Gravity.CENTER, false);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        ivDismiss = (ImageView) contentView.findViewById(R.id.iv_dismiss);
        tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        ivDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirm();
                }
                dismiss();
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
        void onConfirm();
    }
}
