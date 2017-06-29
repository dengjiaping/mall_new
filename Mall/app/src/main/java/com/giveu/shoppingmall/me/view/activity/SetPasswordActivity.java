package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.SetPasswordPresenter;
import com.giveu.shoppingmall.me.view.agent.ISetPasswordView;
import com.giveu.shoppingmall.model.bean.response.RegisterResponse;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;

import butterknife.BindView;

/**
 * Created by 513419 on 2017/6/20.
 */

public class SetPasswordActivity extends BaseActivity implements ISetPasswordView {

    @BindView(R.id.et_pwd)
    EditView etPwd;
    @BindView(R.id.et_confirm_pwd)
    EditView etConfirmPwd;
    @BindView(R.id.tv_complete)
    ClickEnabledTextView tvComplete;
    private boolean isSetPassword;
    private SetPasswordPresenter presenter;
    private String mobile;
    private String smsCode;

    public static void startIt(Activity activity, boolean isSetPassword, String mobile, String smsCode) {
        Intent intent = new Intent(activity, SetPasswordActivity.class);
        intent.putExtra("isSetPassword", isSetPassword);
        intent.putExtra("mobile", mobile);
        intent.putExtra("smsCode", smsCode);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_password);
        isSetPassword = getIntent().getBooleanExtra("isSetPassword", false);
        mobile = getIntent().getStringExtra("mobile");
        smsCode = getIntent().getStringExtra("smsCode");
        //区分是设置密码还是重置密码
        if (isSetPassword) {
            baseLayout.setTitle("设置登录密码");
        } else {
            baseLayout.setTitle("重置登录密码");
        }
        etPwd.setMaxLength(16);
        etConfirmPwd.setMaxLength(16);
        etPwd.checkFormat(8);
        etConfirmPwd.checkFormat(8);
        etPwd.setPasswordInputStyle();
        etConfirmPwd.setPasswordInputStyle();
        presenter = new SetPasswordPresenter(this);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_complete:
                if (canClick(true)) {
                    presenter.register(mobile, etPwd.getText().toString(), smsCode);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void setListener() {
        super.setListener();
        etConfirmPwd.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick(false);
            }
        });
        etConfirmPwd.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick(false);
            }
        });
    }

    private boolean canClick(boolean showToast) {
        tvComplete.setClickEnabled(false);
        if (etPwd.getText().toString().length() < 8) {
            if (showToast) {
                ToastUtils.showShortToast("请设置8-16位字母数字组合密码");
            }
            return false;
        }
        if (!etConfirmPwd.getText().toString().equals(etPwd.getText().toString())) {
            if (showToast) {
                ToastUtils.showShortToast("输入密码不一致");
            }
            return false;
        }
        tvComplete.setClickEnabled(true);
        return true;
    }

    @Override
    public void registerSuccess(RegisterResponse response) {
        ToastUtils.showShortToast("注册成功");
        finish();
    }
}
