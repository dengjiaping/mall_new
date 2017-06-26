package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.SendCodeTextView;
import com.giveu.shoppingmall.widget.dialog.NormalHintDialog;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 添加银行卡二级页
 * Created by 101900 on 2017/2/22.
 */

public class AddBankCardSecondActivity extends BaseActivity {
    @BindView(R.id.checkbox_desc)
    TextView checkboxDesc;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.et_bank_name)
    EditView etBankName;
    @BindView(R.id.et_bank_phone)
    EditView etBankPhone;
    @BindView(R.id.et_send_code)
    EditView etSendCode;
    @BindView(R.id.ll_send_code)
    LinearLayout llSendCode;
    @BindView(R.id.tv_send_code)
    SendCodeTextView tvSendCode;
    @BindView(R.id.tv_commit)
    ClickEnabledTextView tvCommit;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addcardsecond);
        CommonUtils.openSoftKeyBoard(mBaseContext);
        baseLayout.setTitle("添加银行卡");
        CommonUtils.setTextWithSpan(checkboxDesc, false, "我同意代扣还款并遵守合同中相关约定", "《代扣服务协议》", R.color.black, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //代扣协议
            }
        });
    }

    @Override
    public void setListener() {
        etBankName.checkFormat(EditView.Style.BANKNAME);
        etSendCode.checkFormat(6);
        etBankPhone.checkFormat(11);
        etBankName.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if ("工商银行".equals(s.toString())) {
                    llSendCode.setVisibility(View.GONE);
                    etSendCode.setText("");
                } else {
                    llSendCode.setVisibility(View.VISIBLE);
                    tvCommit.setClickEnabled(false);
                    buttonCanClick(false);
                }

            }
        });
        etBankPhone.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.checkPhoneNumberAndTipError(s.toString(), false)) {
                    tvSendCode.setBackgroundResource(R.color.title_color);
                    if(!tvSendCode.isCounting()){
                        tvSendCode.setEnabled(true);
                    }
                    tvCommit.setClickEnabled(false);
                    buttonCanClick(false);
                } else {
                    tvSendCode.setEnabled(false);
                    tvSendCode.setBackgroundResource(R.color.color_d8d8d8);
                }
                tvCommit.setClickEnabled(false);
                buttonCanClick(false);
            }
        });
        etSendCode.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                tvCommit.setClickEnabled(false);
                buttonCanClick(false);
            }
        });
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                tvCommit.setClickEnabled(false);
                buttonCanClick(false);
            }
        });
    }

    @Override
    public void setData() {

    }


    public void buttonCanClick(boolean showToast) {
        String bankName = StringUtils.getTextFromView(etBankName);
        String bankPhone = StringUtils.getTextFromView(etBankPhone);
        if (StringUtils.isNull(bankName)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入银行类别！");
            }
            return;
        }
        if (!bankName.matches("[\\u4e00-\\u9fa5]+$")) {
            if(showToast){
                ToastUtils.showShortToast("请输入正确的银行类别！");
            }
            return ;
        }
        if (!StringUtils.checkPhoneNumberAndTipError(bankPhone, showToast)) {
            return;
        }
        if (llSendCode != null) {
            String sendCode = StringUtils.getTextFromView(etSendCode);
            if(StringUtils.isNull(sendCode)){
                if (showToast) {
                    ToastUtils.showShortToast("请输入6位验证码！");
                }
                return;
            }else if(sendCode.length() != 6){
                if (showToast) {
                    ToastUtils.showShortToast("请输入6位验证码！");
                }
                return;
            }
        }

        if (!checkbox.isChecked()) {
            if (showToast) {
                ToastUtils.showShortToast("请勾选代扣服务协议！");
            }
            return;
        }
        if (!showToast) {//只在输入时判断按钮颜色，点击按钮不操作按钮颜色
            tvCommit.setClickEnabled(true);
        }
    }

    @OnClick({R.id.tv_send_code, R.id.tv_commit})
    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.tv_send_code:
                    tvSendCode.startCount(new SendCodeTextView.CountEndListener() {
                        @Override
                        public void onEnd() {
                             String bankPhone = StringUtils.getTextFromView(etBankPhone);
                            if(StringUtils.checkPhoneNumberAndTipError(bankPhone,false)){
                                tvSendCode.setBackgroundResource(R.color.title_color);
                                tvSendCode.setEnabled(true);
                            }else{
                                tvSendCode.setBackgroundResource(R.color.color_d8d8d8);
                                tvSendCode.setEnabled(false);
                            }

                        }
                    });
                break;
            case R.id.tv_commit:
                if (tvCommit.isClickEnabled()) {
                    NormalHintDialog dialog = new NormalHintDialog(mBaseContext, "您已经绑定了该银行卡，请确认后重试");
                    dialog.showDialog();

                } else {
                    buttonCanClick(true);
                }
                break;
        }
    }

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, AddBankCardSecondActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvSendCode.onDestory();
    }

}
