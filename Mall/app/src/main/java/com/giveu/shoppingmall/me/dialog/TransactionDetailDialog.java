package com.giveu.shoppingmall.me.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;

/**
 * Created by 513419 on 2017/6/23.
 */

public class TransactionDetailDialog extends CustomDialog {
    private TextView tvKnow;

    public TransactionDetailDialog(Activity context) {
        super(context, R.layout.dialog_transaction_detail, R.style.customerDialog, Gravity.CENTER, false);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tvKnow = (TextView) contentView.findViewById(R.id.tv_know);
        tvKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        setCanceledOnTouchOutside(false);
    }
}
