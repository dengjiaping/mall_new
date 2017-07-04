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
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.me.presenter.RequestPwdPresenter;
import com.giveu.shoppingmall.me.view.agent.IRequestPwdView;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.SendCodeTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/20.
 * 找回登录密码
 */

public class RequestPasswordActivity extends BaseActivity implements IRequestPwdView {

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
    private RequestPwdPresenter presenter;
    private boolean isForTrade;//是否找回交易密码,false为找回登录密码

    public static void startIt(Activity activity) {
        startIt(activity, false);
    }

    public static void startIt(Activity activity, boolean isForTrade) {
        Intent intent = new Intent(activity, RequestPasswordActivity.class);
        intent.putExtra("isForTrade", isForTrade);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_request_password);
        isForTrade = getIntent().getBooleanExtra("isForTrade", false);
        if (isForTrade) {
            baseLayout.setTitle("找回交易密码");
        } else {
            baseLayout.setTitle("找回登录密码");
            presenter = new RequestPwdPresenter(this);
            //已登录用户自动填充手机号
            if (LoginHelper.getInstance().hasLogin() && StringUtils.isNotNull(LoginHelper.getInstance().getMobile())) {
                etPhone.setText(LoginHelper.getInstance().getMobile());
                etPhone.setSelection(LoginHelper.getInstance().getMobile().length());
                tvSendCode.performClick();
                etVertificationCode.requestFocus();
            }
        }
        initCallDialog();
        etPhone.checkFormat(11);
        tvSendCode.setSendTextColor(false);
    }

    private void initCallDialog() {
        callDialog = new CustomDialog(mBaseContext, R.layout.dialg_dial_phone, R.style.customerDialog, Gravity.CENTER, false);
        tvDial = (TextView) callDialog.findViewById(R.id.tv_dial);
        tvDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.dismiss();
                CommonUtils.callPhone(mBaseContext, "40088888888");
            }
        });
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

        etVertificationCode.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                canClick(false);
            }
        });
    }

    @OnClick({R.id.tv_send_code, R.id.tv_next, R.id.tv_unreceived})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send_code:
                if (etPhone.length() == 11) {
                    CommonUtils.closeSoftKeyBoard(mBaseContext);
                    presenter.sendSMSCode(etPhone.getText().toString(), "findLoginPwd");
                    canClick(false);
                }
                break;
            case R.id.tv_next:
                if (canClick(true)) {
                    presenter.checkSms(etPhone.getText().toString(), etVertificationCode.getText().toString());
                }
                break;
            case R.id.tv_unreceived:
                callDialog.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvSendCode != null) {
            tvSendCode.onDestory();
        }
    }

    private boolean canClick(boolean showToast) {
        tvNext.setClickEnabled(false);
        if (etPhone.getText().toString().length() != 11) {
            if (showToast) {
                ToastUtils.showShortToast("请输入11位的手机号");
            }
            return false;
        }

        if (etVertificationCode.getText().toString().length() == 0) {
            if (showToast) {
                ToastUtils.showShortToast("请输入验证码");
            }
            return false;
        }
        tvNext.setClickEnabled(true);
        return true;
    }

    @Override
    public void skipToIdentify(String randCode) {
        //找回登录密码，钱包资质用户跳转至身份证填写
        IdentifyActivity.startIt(mBaseContext, randCode, etPhone.getText().toString(), isForTrade);
    }

    @Override
    public void skipToChangePassword(String randCode) {
        //找回登录密码，非钱包资质用户跳转至密码重置
        if (!isForTrade) {
            SetPasswordActivity.startItWithRandCode(mBaseContext, false, etPhone.getText().toString(), randCode);
        } else {
            IdentifyActivity.startIt(mBaseContext, randCode, etPhone.getText().toString(), true);
        }
    }

    @Override
    public void sendSMSSuccess() {
        tvSendCode.startCount(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SetPasswordActivity.REQUEST_FINISH && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
