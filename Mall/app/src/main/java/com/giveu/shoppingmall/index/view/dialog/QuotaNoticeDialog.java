package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;


/**
 * Created by 524202 on 2017/10/10.
 */

public class QuotaNoticeDialog {

    private final CustomDialog mDialog;
    private TextView titleView;
    private TextView contentView;
    private TextView confirmView;

    public QuotaNoticeDialog(Activity mActivity) {
        View view = View.inflate(mActivity, R.layout.dialog_quota_notice_layout, null);
        mDialog = new CustomDialog(mActivity, view, R.style.customerDialog, Gravity.CENTER, false);

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        titleView = (TextView) view.findViewById(R.id.tv_title);
        contentView = (TextView) view.findViewById(R.id.tv_content);
        confirmView = (TextView) view.findViewById(R.id.tv_confirm);

        confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public void setTitleText(CharSequence title) {
        titleView.setText(title);
    }

    public void setContentText(CharSequence content) {
        contentView.setText(content);
    }

    public void setConfirmText(CharSequence confirm) {
        confirmView.setText(confirm);
    }

    public void show() {
        mDialog.show();
    }


}
