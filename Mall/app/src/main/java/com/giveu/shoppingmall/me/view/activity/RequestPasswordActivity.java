package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * 找回或更改登录密码
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
    private ImageView ivCancel;
    private TextView tvPhone;
    private LinearLayout llPhone;
    private RequestPwdPresenter presenter;
    public static final int FIND_LOGIN_PWD = 1;
    public static final int CHANGE_LOGIN_PWD = 2;
    public static final int FIND_TRADE_PWD = 3;
    public static final int CHANGE_TRADE_PWD = 4;
    private int type;


    public static void startIt(Activity activity, int type) {
        Intent intent = new Intent(activity, RequestPasswordActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_request_password);
        type = getIntent().getIntExtra("type", 1);
        switch (type) {
            case FIND_LOGIN_PWD:
                baseLayout.setTitle("找回登录密码");
                break;
            case CHANGE_LOGIN_PWD:
                baseLayout.setTitle("修改登录密码");
                break;
            case FIND_TRADE_PWD:
                baseLayout.setTitle("找回交易密码");
                break;
            case CHANGE_TRADE_PWD:
                baseLayout.setTitle("修改交易密码");
                break;
        }
        presenter = new RequestPwdPresenter(this);
        initCallDialog();
        etPhone.checkFormat(11);
        tvSendCode.setSendTextColor(false);
        if (StringUtils.isNotNull(LoginHelper.getInstance().getPhone())) {
            etPhone.setText(LoginHelper.getInstance().getPhone());
            etPhone.setSelection(etPhone.getText().toString().length());
            etVertificationCode.requestFocus();
            // tvSendCode.performClick();
        }
    }

    private void initCallDialog() {
        callDialog = new CustomDialog(mBaseContext, R.layout.dialog_dial, R.style.customerDialog, Gravity.CENTER, false);
        tvDial = (TextView) callDialog.findViewById(R.id.tv_dial);
        ivCancel = (ImageView) callDialog.findViewById(R.id.iv_cancel);
        tvPhone = (TextView) callDialog.findViewById(R.id.tv_phone);
        llPhone = (LinearLayout) callDialog.findViewById(R.id.ll_phone);
        llPhone.setVisibility(View.GONE);
        tvDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.dismiss();
                CommonUtils.callPhone(mBaseContext, "4001868888");
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.dismiss();
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
                    if (type == FIND_TRADE_PWD || type == CHANGE_TRADE_PWD) {
                        //交易密码
                        presenter.sendSMSCode(etPhone.getText().toString(), "resetPayPwd");
                    } else {
                        //登录密码
                        presenter.sendSMSCode(etPhone.getText().toString(), "findLoginPwd");
                    }

                    canClick(false);
                }
                break;
            case R.id.tv_next:
                if (canClick(true)) {
                    if (type == FIND_TRADE_PWD || type == CHANGE_TRADE_PWD) {
                        //找回交易密码, 直接跳到下一个页面，下一个页面接口会一起校验短信验证码
                        IdentifyActivity.startIt(mBaseContext, "", etPhone.getText().toString(), etVertificationCode.getText().toString(), type);
                    } else {
                        //登录密码
                        presenter.checkSms(etPhone.getText().toString(), etVertificationCode.getText().toString());
                    }
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
        if ("获取验证码".equals(tvSendCode.getText().toString()) && tvSendCode.isEnabled()) {
            if (showToast) {
                ToastUtils.showShortToast("请获取验证码");
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
        IdentifyActivity.startIt(mBaseContext, randCode, etPhone.getText().toString(), etVertificationCode.getText().toString(), type);
    }

    @Override
    public void skipToChangePassword(String randCode) {
        //找回登录密码，非钱包资质用户跳转至密码重置
        SetPasswordActivity.startItWithRandCode(mBaseContext, false, etPhone.getText().toString(), randCode);
    }

    @Override
    public void sendSMSSuccess() {
        tvSendCode.startCount(null);
        canClick(false);
        //验证码发送成功，则在弹出框显示手机号码
        llPhone.setVisibility(View.VISIBLE);
        tvPhone.setText(etPhone.getText().toString());
    }

    @Override
    public void checkSMSSuccess() {

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
