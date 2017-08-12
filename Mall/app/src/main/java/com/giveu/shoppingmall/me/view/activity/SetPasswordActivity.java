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
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.MD5;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.giveu.shoppingmall.utils.LoginHelper.REMEBER_ACCOUNT;

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
    private String randomCode;
    private String randCode;
    public static final int REQUEST_FINISH = 10000;

    public static void startIt(Activity activity, boolean isSetPassword, String mobile, String randomCode) {
        Intent intent = new Intent(activity, SetPasswordActivity.class);
        intent.putExtra("isSetPassword", isSetPassword);
        intent.putExtra("mobile", mobile);
        intent.putExtra("randomCode", randomCode);
        activity.startActivityForResult(intent, REQUEST_FINISH);
    }

    public static void startItWithRandCode(Activity activity, boolean isSetPassword, String mobile, String randCode) {
        Intent intent = new Intent(activity, SetPasswordActivity.class);
        intent.putExtra("isSetPassword", isSetPassword);
        intent.putExtra("randCode", randCode);
        intent.putExtra("mobile", mobile);
        activity.startActivityForResult(intent, REQUEST_FINISH);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_password);
        isSetPassword = getIntent().getBooleanExtra("isSetPassword", false);
        mobile = getIntent().getStringExtra("mobile");
        randomCode = getIntent().getStringExtra("randomCode");
        randCode = getIntent().getStringExtra("randCode");
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
//        etPwd.setPasswordInputStyle();
//        etConfirmPwd.setPasswordInputStyle();
        presenter = new SetPasswordPresenter(this);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {

    }

    @OnClick({R.id.tv_complete})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_complete:
                if (canClick(true)) {
                    if (StringUtils.checkLoginPwdAndTipError(etPwd.getText().toString(),true)) {
                        if (isSetPassword) {
                            //设置密码
                            presenter.register(mobile, MD5.MD5Encode(etPwd.getText().toString()), randomCode);
                        } else {
                            //重置密码
                            presenter.resetPassword(mobile, MD5.MD5Encode(etPwd.getText().toString()), randCode, LoginHelper.getInstance().getUserName());
                        }
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void setListener() {
        super.setListener();
        etPwd.addTextChangedListener(new TextChangeListener() {
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

        if (!StringUtils.checkLoginPwdAndTipError(etPwd.getText().toString(), showToast)) {
            return false;
        }
        if (!etConfirmPwd.getText().toString().equals(etPwd.getText().toString())) {
            if (showToast) {
                ToastUtils.showShortToast("登录密码不一致，请重新确认");
            }
            return false;
        }
        tvComplete.setClickEnabled(true);
        return true;
    }

    @Override
    public void registerSuccess(RegisterResponse response) {
        ToastUtils.showShortToast("注册成功");
        //只保存账号，以便在登录界面显示注册的账号
        LoginHelper.getInstance().putString(REMEBER_ACCOUNT, mobile);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void changePwdSuccess() {
        ToastUtils.showShortToast("重置密码成功");
        setResult(RESULT_OK);
        finish();
    }
}
