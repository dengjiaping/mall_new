package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.view.EditView;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.view.ClickEnabledTextView;
import com.giveu.shoppingmall.view.SendCodeTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/19.
 */

public class RegisterActivity extends BaseActivity {


    @BindView(R.id.et_phone)
    EditView etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.tv_next)
    ClickEnabledTextView tvNext;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        baseLayout.setTitle("注册");
        etPhone.checkFormat(11);
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

        etCode.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick();
            }
        });
    }

    @OnClick({R.id.tv_next, R.id.tv_send_code, R.id.cb_agreement})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.cb_agreement:
                canClick();
                break;

            case R.id.tv_next:
                if (tvNext.isClickEnabled()) {
                    SetPasswordActivity.startIt(mBaseContext, true);
                } else {
                    if (etPhone.getText().toString().length() != 11) {
                        ToastUtils.showShortToast("请输入11位的手机号");
                    } else if (etCode.getText().toString().length() == 0) {
                        ToastUtils.showShortToast("请输入验证码");
                    }
                }
                break;

            case R.id.tv_send_code:
                if (etPhone.length() != 11) {
                    ToastUtils.showShortToast("请输入11位的手机号");
                } else {
                    tvSendCode.startCount(null);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 是否满足条件，是的话，按钮状态变为可点击
     */
    private void canClick() {
        if (cbAgreement.isChecked() && etCode.getText().toString().length() != 0 && etPhone.getText().toString().length() == 11) {
            tvNext.setClickEnabled(true);
        } else {
            tvNext.setClickEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvSendCode != null) {
          tvSendCode.onDestory();
        }
    }
}
