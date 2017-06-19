package com.giveu.shoppingmall.index.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 钱包激活页面
 * Created by 101900 on 2017/6/19.
 */

public class WalletActivationActivity extends BaseActivity {
    @BindView(R.id.tv_activation)
    TextView tvActivation;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_ident)
    EditText etIdent;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_bank_no)
    EditText etBankNo;
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

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_activation);
    }

    @Override
    public void setData() {
        EditListener(etName, ivName);
        EditListener(etIdent, ivIdent);
        EditListener(etPhone, ivPhone);
        EditListener(etCode, ivCode);
        EditListener(etBankNo, ivBankNo);
    }

    //true 信息正确 false 信息错误
    public boolean ErrorCheck() {
        String name = StringUtils.getTextFromView(etName);
        String ident = StringUtils.getTextFromView(etIdent);
        String phone = StringUtils.getTextFromView(etPhone);
        String code = StringUtils.getTextFromView(etCode);
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

        if (StringUtils.isNull(code)) {
            ToastUtils.showShortToast("请输入验证码！");
            return false;
        }
        return true;
    }


    @OnClick(R.id.tv_activation)
    public void onViewClicked() {
        if (ErrorCheck()) {
            CommonUtils.startActivity(mBaseContext, ActivationStatusActivity.class);
        }
    }

    public void EditListener(EditText editText, final ImageView imageView) {
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
}
