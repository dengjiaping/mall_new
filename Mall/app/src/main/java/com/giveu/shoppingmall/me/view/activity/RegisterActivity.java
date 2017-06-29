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
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.RegisterPresenter;
import com.giveu.shoppingmall.me.view.agent.IRegisterView;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.SendCodeTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/19.
 */

public class RegisterActivity extends BaseActivity implements IRegisterView {


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
    private RegisterPresenter presenter;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        baseLayout.setTitle("注册");
        etPhone.checkFormat(11);
        presenter = new RegisterPresenter(this);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
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
                canClick(false);
            }
        });

        etCode.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick(false);
            }
        });
    }

    @OnClick({R.id.tv_next, R.id.tv_send_code, R.id.cb_agreement})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.cb_agreement:
                canClick(false);
                break;

            case R.id.tv_next:
                if (canClick(true)) {
                    presenter.checkSMSCode(etPhone.getText().toString(), etCode.getText().toString());
                }
                break;

            case R.id.tv_send_code:
                if (etPhone.getText().toString().length() != 11) {
                    ToastUtils.showShortToast("请输入11位的手机号");
                } else {
                    CommonUtils.closeSoftKeyBoard(mBaseContext);
                    presenter.sendSMSCode(etPhone.getText().toString());
                }
                break;

            default:
                break;
        }
    }

    /**
     * 是否满足条件，是的话，按钮状态变为可点击
     */
    private boolean canClick(boolean showToast) {
        tvNext.setClickEnabled(false);
        if (etPhone.getText().toString().length() != 11) {
            if (showToast) {
                ToastUtils.showShortToast("请输入11位的手机号");
            }
            return false;
        }

        if ("获取验证码".equals(tvSendCode.getText().toString()) && tvSendCode.isEnabled()) {
            if (showToast) {
                ToastUtils.showShortToast("请获取验证码");
            }
            return false;
        }

        if (etCode.getText().toString().length() == 0) {
            if (showToast) {
                ToastUtils.showShortToast("请输入验证码");
            }
            return false;
        }

        if (!cbAgreement.isChecked()) {
            if (showToast) {
                ToastUtils.showShortToast("请输入验证码");
            }

            return false;
        }
        tvNext.setClickEnabled(true);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvSendCode != null) {
            tvSendCode.onDestory();
        }
    }

    @Override
    public void checkSMSSuccess() {
        SetPasswordActivity.startIt(mBaseContext, true, etPhone.getText().toString(), etCode.getText().toString());
    }

    @Override
    public void sendSMSSuccess() {
        tvSendCode.startCount(null);
        canClick(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SetPasswordActivity.REQUEST_FINISH && resultCode == RESULT_OK) {
            finish();
        }
    }
}
