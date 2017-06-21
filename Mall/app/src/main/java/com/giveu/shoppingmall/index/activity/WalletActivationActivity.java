package com.giveu.shoppingmall.index.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.view.EditView;
import com.giveu.shoppingmall.model.bean.response.ActivationResponse;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.view.SendCodeTextView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 钱包激活页面
 * Created by 101900 on 2017/6/19.
 */

public class WalletActivationActivity extends BaseActivity {
    @BindView(R.id.tv_activation)
    TextView tvActivation;
    @BindView(R.id.iv_name)
    ImageView ivName;
    @BindView(R.id.iv_ident)
    ImageView ivIdent;
    @BindView(R.id.iv_bank_no)
    ImageView ivBankNo;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.cb_check)
    CheckBox cbCheck;
    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;
    ActivationResponse activationResponse;
    @BindView(R.id.et_name)
    EditView etName;
    @BindView(R.id.et_ident)
    EditView etIdent;
    @BindView(R.id.et_bank_no)
    EditView etBankNo;
    @BindView(R.id.et_phone)
    EditView etPhone;
    @BindView(R.id.et_code)
    EditView etCode;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_activation);
        baseLayout.setTitle("钱包激活");
    }

    @Override
    public void setData() {
        etName.checkFormat(EditView.Style.NAME);
        etIdent.checkFormat(EditView.Style.IDENT);
        etPhone.checkFormat(11);
        etCode.checkFormat(6);
        etBankNo.checkFormat(19);
        EditListener(etName, ivName);
        EditListener(etIdent, ivIdent);
        EditListener(etPhone, ivPhone);
        EditListener(etCode, ivCode);
        EditListener(etBankNo, ivBankNo);
        activationResponse = new ActivationResponse("1", "13000.00元", "1000.00元", "12000.00元", null, null);
    }

    //true 信息正确 false 信息错误
    public boolean ErrorCheck() {
        String name = StringUtils.getTextFromView(etName);
        String ident = StringUtils.getTextFromView(etIdent);
        String phone = StringUtils.getTextFromView(etPhone);
        String bankNo = StringUtils.getTextFromView(etBankNo);
        if (StringUtils.isNull(name)) {
            ToastUtils.showShortToast("请输入姓名！");
            return false;
        }
        if (!StringUtils.checkUserNameAndTipError(name)) {
            return false;
        }
        if (!StringUtils.checkIdCardAndTipError(ident)) {
            return false;
        }
        if (!StringUtils.isCardNum(bankNo)) {
            ToastUtils.showShortToast("请输入19位的银行卡号！");
            return false;
        }
        if (!StringUtils.checkPhoneNumberAndTipError(phone)) {
            return false;
        }
        return true;
    }


    public void EditListener(final EditText editText, final ImageView imageView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    imageView.setImageResource(R.drawable.ic_pen);
                } else {
                    imageView.setImageResource(R.drawable.ic_add);
                }
            }
        });
    }
    @OnClick({R.id.tv_send_code, R.id.tv_activation})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_send_code:
                if (ErrorCheck()) {
                    tvSendCode.startCount();
                }
                break;
            case R.id.tv_activation:
                if (ErrorCheck()) {
                    String code = StringUtils.getTextFromView(etCode);
                    if (StringUtils.isNull(code)) {
                        ToastUtils.showShortToast("请输入验证码！");
                    } else if (!cbCheck.isChecked()) {
                        ToastUtils.showShortToast("请勾选协议！");
                    } else {
                        ActivationStatusActivity.startIt(mBaseContext, activationResponse.status, activationResponse.date1, activationResponse.date2, activationResponse.date3, activationResponse.bottomHint, activationResponse.midHint);
                    }
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvSendCode != null) {
            tvSendCode.stopCount();
        }
    }


}
