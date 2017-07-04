package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.WalletActivationResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.SendCodeTextView;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 钱包激活二级页面
 * Created by 101900 on 2017/6/19.
 */

public class WalletActivationSecondActivity extends BaseActivity {

    @BindView(R.id.iv_bank_no)
    ImageView ivBankNo;
    @BindView(R.id.et_bank_no)
    EditView etBankNo;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    @BindView(R.id.et_phone)
    EditView etPhone;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.et_code)
    EditView etCode;
    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;
    @BindView(R.id.cb_check)
    CheckBox cbCheck;
    @BindView(R.id.tv_activation)
    ClickEnabledTextView tvActivation;


    public static void startIt(Activity mActivity, String name, String ident) {
        Intent intent = new Intent(mActivity, WalletActivationSecondActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("ident", ident);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_activation_second);
        baseLayout.setTitle("钱包激活");
        CommonUtils.openSoftKeyBoard(mBaseContext);
        tvSendCode.setSendTextColor(false);
    }

    @Override
    public void setListener() {
        super.setListener();
        etPhone.checkFormat(11);
        etCode.checkFormat(6);
        etBankNo.checkFormat(19);
        editTextListener(etPhone, ivPhone);
        editTextListener(etCode, ivCode);
        editTextListener(etBankNo, ivBankNo);
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonCanClick(false);
            }
        });
    }

    @Override
    public void setData() {

    }

    /**
     * 每个输入框监听 输入字符后图标改变，未输时还原图标
     *
     * @param editText
     * @param imageView
     */
    public void editTextListener(final EditText editText, final ImageView imageView) {
        editText.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    imageView.setImageResource(R.drawable.ic_pen);
                } else {
                    imageView.setImageResource(R.drawable.ic_add);
                }
                if (etPhone == editText) {
                    //如果是手机号的EditText还需判断验证码是否可以点击
                    if (StringUtils.checkPhoneNumberAndTipError(s.toString(), false)) {
                        tvSendCode.setTextColor(getResources().getColor(R.color.title_color));
                        if (!tvSendCode.isCounting()) {
                            tvSendCode.setEnabled(true);
                        }
                        tvActivation.setClickEnabled(false);
                    } else {
                        tvSendCode.setTextColor(getResources().getColor(R.color.color_d8d8d8));
                        tvSendCode.setEnabled(false);
                    }
                    tvActivation.setClickEnabled(false);
                }
                buttonCanClick(false);
            }
        });
    }

    @OnClick({R.id.tv_send_code, R.id.tv_activation})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        final String phone = StringUtils.nullToEmptyString(StringUtils.getTextFromView(etPhone));
        switch (view.getId()) {
            case R.id.tv_send_code:
                ApiImpl.sendSMSCode(mBaseContext, phone, "activateWallet", new BaseRequestAgent.ResponseListener<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean response) {
                        tvSendCode.startCount(new SendCodeTextView.CountEndListener() {
                            @Override
                            public void onEnd() {
                                if (StringUtils.checkPhoneNumberAndTipError(phone, false)) {
                                    tvSendCode.setTextColor(getResources().getColor(R.color.title_color));
                                    tvSendCode.setEnabled(true);
                                } else {
                                    tvSendCode.setTextColor(getResources().getColor(R.color.color_d8d8d8));
                                    tvSendCode.setEnabled(false);
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                });

                break;
            case R.id.tv_activation:
                if (tvActivation.isClickEnabled()) {
                    String name = StringUtils.nullToEmptyString(getIntent().getStringExtra("name"));
                    String ident = StringUtils.nullToEmptyString(getIntent().getStringExtra("ident"));
                    String bankNo = StringUtils.nullToEmptyString(StringUtils.getTextFromView(etBankNo));
                    String code = StringUtils.nullToEmptyString(StringUtils.getTextFromView(etCode));
                    ApiImpl.activateWallet(mBaseContext, bankNo, "10000923", ident, "106.72", "26.57", phone, name, code, LoginHelper.getInstance().getUserId(), new BaseRequestAgent.ResponseListener<WalletActivationResponse>() {
                        @Override
                        public void onSuccess(WalletActivationResponse response) {
                            ToastUtils.showShortToast("激活成功！");
                            //TODO: 错误状态码未确定 ，先不跳转
//                            if (response != null) {
//                                ActivationStatusActivity.startIt(mBaseContext,"", response.globleLimit+"", response.cyLimit+"", response.posLimit+"", response.lab);
//                            }
                        }

                        @Override
                        public void onError(BaseBean errorBean) {
                            CommonLoadingView.showErrorToast(errorBean);
                        }
                    });
                } else {
                    buttonCanClick(true);
                }
                break;
        }
    }

    /**
     * 立即激活按钮的颜色控制
     *
     * @param showToast
     * @return
     */
    private void buttonCanClick(boolean showToast) {
        tvActivation.setClickEnabled(false);

        String phone = StringUtils.getTextFromView(etPhone);
        String bankNo = StringUtils.getTextFromView(etBankNo);

        if (!StringUtils.isCardNum(bankNo)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入19位的银行卡号！");
            }
            return;
        }
        if (!StringUtils.checkPhoneNumberAndTipError(phone, showToast)) {
            return;
        }
        String code = StringUtils.getTextFromView(etCode);
        if (StringUtils.isNull(code)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入6位验证码");
            }
            return;
        } else if (code.length() != 6) {
            if (showToast) {
                ToastUtils.showShortToast("请输入6位验证码");
            }
            return;
        }
        if (!cbCheck.isChecked()) {
            if (showToast) {
                ToastUtils.showShortToast("请勾选协议！");
            }
            return;
        }
        if (!showToast) {
            tvActivation.setClickEnabled(true);
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
