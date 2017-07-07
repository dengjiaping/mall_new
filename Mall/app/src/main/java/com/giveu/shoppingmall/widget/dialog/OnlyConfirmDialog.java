package com.giveu.shoppingmall.widget.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;

/**
 * Created by 513419 on 2017/7/3.
 */

public class OnlyConfirmDialog extends CustomDialog {

    private TextView tvContent;

    public OnlyConfirmDialog(Activity context) {
        super(context, R.layout.dialog_only_confirm_button, R.style.customerDialog, Gravity.CENTER, false);
    }

    public OnlyConfirmDialog(Activity context, int style) {
        super(context, R.layout.dialog_only_confirm_button, style, Gravity.CENTER, false);
    }

    public OnlyConfirmDialog(Activity context, int layout, int style, int windowGravity, boolean isFullScreen) {
        super(context, layout, style, windowGravity, isFullScreen);
    }

    public OnlyConfirmDialog(Activity context, View contentView, int style, int windowGravity, boolean isFullScreen) {
        super(context, contentView, style, windowGravity, isFullScreen);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        View confrimView = contentView.findViewById(R.id.ll_confirm);
        confrimView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setContent(CharSequence content) {
        tvContent.setText(content);
    }
}
