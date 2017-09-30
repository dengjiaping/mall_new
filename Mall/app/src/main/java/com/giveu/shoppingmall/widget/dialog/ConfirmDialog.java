package com.giveu.shoppingmall.widget.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;

/**
 * Created by 513419 on 2017/3/6.
 * 确认或取消对话框
 */

public class ConfirmDialog extends CustomDialog implements View.OnClickListener {

    private TextView tvContent;
    private TextView tvCancle;
    private TextView tvConfirm;
    private EditText editText;
    protected Activity context;

    public ConfirmDialog(Activity context) {
        super(context, R.layout.dialog_confirm, R.style.customerDialog, Gravity.CENTER, false);
        this.context = context;
    }

    public ConfirmDialog(Activity context, int style) {
        super(context, R.layout.dialog_confirm, style, Gravity.CENTER, false);
        this.context = context;
    }

    public ConfirmDialog(Activity context, int layout, int style, int windowGravity, boolean isFullScreen) {
        super(context, layout, style, windowGravity, isFullScreen);
        this.context = context;
    }

    public ConfirmDialog(Activity context, View contentView, int style, int windowGravity, boolean isFullScreen) {
        super(context, contentView, style, windowGravity, isFullScreen);
        this.context = context;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        tvCancle = (TextView) contentView.findViewById(R.id.tv_firstBtn);
        tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        editText = (EditText) contentView.findViewById(R.id.editText);
        tvCancle.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                if (listener != null) {
                    listener.confirm();
                }
                break;

            case R.id.tv_firstBtn:
                if (listener != null) {
                    listener.cancel();
                }
                break;

            default:
                break;
        }
    }

    public void setContent(CharSequence content) {
        if (content == null) {
            return;
        }
        tvContent.setText(content);
    }

    public void setEditEnable(boolean editEnable) {
        if (editEnable) {
            editText.setVisibility(View.VISIBLE);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        } else {
            editText.setVisibility(View.GONE);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }
    }

    //获取输入框内容
    public String getEditContent() {
        return editText.getText().toString();
    }

    public void setConfirmStr(CharSequence str) {
        tvConfirm.setText(str);
    }

    public void setCancleStr(CharSequence str) {
        tvCancle.setText(str);
    }

    private OnChooseListener listener;

    public void setOnChooseListener(OnChooseListener listener) {
        this.listener = listener;
    }

    public interface OnChooseListener {
        void confirm();

        void cancel();
    }

}
