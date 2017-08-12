package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.SendCodeTextView;
import com.giveu.shoppingmall.widget.dialog.NormalHintDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改手机号
 * Created by 101900 on 2017/6/29.
 */

public class ChangePhoneNumberActivity extends BaseActivity {


    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;
    @BindView(R.id.et_phone_number)
    EditView etPhoneNumber;
    @BindView(R.id.et_send_code)
    EditView etSendCode;
    @BindView(R.id.tv_finish)
    ClickEnabledTextView tvFinish;
    @BindView(R.id.tv_unreceived)
    TextView tvUnreceived;
    private CustomDialog callDialog;
    private TextView tvDial;
    String randCode;//校验交易密码返回的随机码
    String phoneNumber;
    NormalHintDialog changeSuccessDialog;

    public static void startIt(Activity mActivity, String randCode) {
        Intent intent = new Intent(mActivity, ChangePhoneNumberActivity.class);
        intent.putExtra("randCode", randCode);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_phone_number);
        baseLayout.setTitle("修改手机号");
        tvSendCode.setSendTextColor(false);
        randCode = getIntent().getStringExtra("randCode");
        initCallDialog();
        changeSuccessDialog = new NormalHintDialog(mBaseContext, "绑定手机修改成功！\n\n", "登录手机号已同步，请通过绑定手机+登录密码登录");
        changeSuccessDialog.setOnDialogDismissListener(new NormalHintDialog.OnDialogDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
    }

    @Override
    public void setListener() {
        super.setListener();
        etPhoneNumber.checkFormat(11);
        etSendCode.checkFormat(6);
        etPhoneNumber.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                buttonCanClick(false);
            }
        });

        etSendCode.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                buttonCanClick(false);
            }
        });
    }

    private void initCallDialog() {
        callDialog = new CustomDialog(mBaseContext, R.layout.dialg_dial_phone, R.style.customerDialog, Gravity.CENTER, false);
        tvDial = (TextView) callDialog.findViewById(R.id.tv_dial);
        tvDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.dismiss();
                CommonUtils.callPhone(mBaseContext, "4001868888");
            }
        });
    }

    @Override
    public void setData() {

    }

    private void buttonCanClick(boolean showToast) {
        tvFinish.setClickEnabled(false);

        String phoneNumber = StringUtils.getTextFromView(etPhoneNumber);
        String sendCode = StringUtils.getTextFromView(etSendCode);
        if (!StringUtils.checkPhoneNumberAndTipError(phoneNumber, showToast)) {
            return;
        }
        if (StringUtils.isNull(sendCode)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入6位验证码");
            }
            return;
        } else if (sendCode.length() != 6) {
            if (showToast) {
                ToastUtils.showShortToast("请输入6位验证码");
            }
            return;
        }
        if (!showToast) {
            tvFinish.setClickEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvSendCode != null) {
            tvSendCode.onDestory();
        }
    }


    @OnClick({R.id.tv_send_code, R.id.tv_finish, R.id.tv_unreceived})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        phoneNumber = StringUtils.getTextFromView(etPhoneNumber);
        String sendCode = StringUtils.getTextFromView(etSendCode);
        switch (view.getId()) {
            case R.id.tv_send_code:
                if (phoneNumber.length() == 11) {
                    ApiImpl.sendSMSCode(mBaseContext, phoneNumber, "updatephone", new BaseRequestAgent.ResponseListener<BaseBean>() {
                        @Override
                        public void onSuccess(BaseBean response) {
                            tvSendCode.startCount(null);
                        }

                        @Override
                        public void onError(BaseBean errorBean) {
                            CommonLoadingView.showErrorToast(errorBean);
                        }
                    });

                } else {
                    ToastUtils.showShortToast("请输入11位的手机号");
                }
                break;
            case R.id.tv_finish:
                if (tvFinish.isClickEnabled()) {
                    ApiImpl.updatePhone(mBaseContext, LoginHelper.getInstance().getIdPerson(), phoneNumber, randCode, sendCode, new BaseRequestAgent.ResponseListener<BaseBean>() {
                        @Override
                        public void onSuccess(BaseBean response) {
                            LoginHelper.getInstance().setPhone(phoneNumber);
                            BaseApplication.getInstance().fetchUserInfo();
                            changeSuccessDialog.showDialog();
                        }

                        @Override
                        public void onError(BaseBean errorBean) {
                            NormalHintDialog normalHintDialog = new NormalHintDialog(mBaseContext, errorBean.message);
                            normalHintDialog.showDialog();
                        }
                    });

                } else {
                    buttonCanClick(true);
                }
                break;
            case R.id.tv_unreceived:
                callDialog.show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
