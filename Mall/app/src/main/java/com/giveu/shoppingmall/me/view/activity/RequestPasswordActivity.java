package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.me.view.EditView;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.view.ClickEnabledTextView;
import com.giveu.shoppingmall.view.SendCodeTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/20.
 * 找回登录密码
 */

public class RequestPasswordActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditView etPhone;
    @BindView(R.id.et_vertification_code)
    EditText etVertificationCode;
    @BindView(R.id.tv_next)
    ClickEnabledTextView tvNext;
    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;
    private CustomDialog callDialog;
    private TextView tvDial;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, RequestPasswordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_request_password);
        baseLayout.setTitle("找回登录密码");
        initCallDialog();
        etPhone.checkFormat(11);
    }

    private void initCallDialog() {
        callDialog = new CustomDialog(mBaseContext, R.layout.dialg_dial_phone, R.style.customerDialog, Gravity.CENTER, false);
        tvDial = (TextView) callDialog.findViewById(R.id.tv_dial);
        tvDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.dismiss();
                CommonUtils.callPhone(mBaseContext,"40088888888");
            }
        });
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        super.setListener();
        etPhone.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick();
            }
        });

        etVertificationCode.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick();
            }
        });
    }

    @OnClick({R.id.tv_send_code, R.id.tv_next, R.id.tv_unreceived})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send_code:
                if (etPhone.length() != 11) {
                    ToastUtils.showShortToast("请输入11位的手机号");
                } else {
                    tvSendCode.startCount(null);
                }
                break;
            case R.id.tv_next:
                if (tvNext.isClickEnabled()) {
                    SetPasswordActivity.startIt(mBaseContext, false);
                } else {
                    if (etPhone.getText().toString().length() != 11) {
                        ToastUtils.showShortToast("请输入11位的手机号");
                    } else if (etVertificationCode.getText().toString().length() == 0) {
                        ToastUtils.showShortToast("请输入验证码");
                    }
                }
                break;
            case R.id.tv_unreceived:
                callDialog.show();
                break;
        }
    }

    private void canClick() {
        if (etVertificationCode.getText().toString().length() != 0 && etPhone.getText().toString().length() == 11) {
            tvNext.setClickEnabled(true);
        } else {
            tvNext.setClickEnabled(false);
        }
    }
}
