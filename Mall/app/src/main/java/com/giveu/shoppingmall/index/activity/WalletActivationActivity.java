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
    @BindView(R.id.cb_check)
    CheckBox cbCheck;
    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_activation);
    }

    @Override
    public void setData() {
        EditListener(etName, ivName, 2, 18, 2);
        EditListener(etIdent, ivIdent, 15, 18, 3);
        EditListener(etPhone, ivPhone, 11, 0, 1);
        EditListener(etCode, ivCode, 6, 0, 1);
        EditListener(etBankNo, ivBankNo, 19, 0, 1);
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


    public void EditListener(final EditText editText, final ImageView imageView, final int flag1, final int flag2, final int style) {
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

                switch (style) {
                    case 1:
                        //银行卡、手机号类只有1个位数限制
                        if (s.length() == flag1) {
                            editText.setTextColor(getResources().getColor(R.color.color_9b9b9b));
                        } else {
                            editText.setTextColor(getResources().getColor(R.color.red));
                        }
                        break;
                    case 2:
                        //姓名范围的位数限制
                        if (s.length() >= flag1 && s.length() <= flag2) {
                            editText.setTextColor(getResources().getColor(R.color.color_9b9b9b));
                        } else {
                            editText.setTextColor(getResources().getColor(R.color.red));
                        }
                        break;
                    case 3:
                        //身份证类2种位数限制
                        if (s.length() == flag1 || s.length() == flag2) {
                            editText.setTextColor(getResources().getColor(R.color.color_9b9b9b));
                        } else {
                            editText.setTextColor(getResources().getColor(R.color.red));
                        }
                        break;
                }
            }
        });
    }

    @OnClick({R.id.tv_send_code, R.id.tv_activation})
    public void onViewClicked(View view) {
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
                        ActivationStatusActivity.startIt(mBaseContext);
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
