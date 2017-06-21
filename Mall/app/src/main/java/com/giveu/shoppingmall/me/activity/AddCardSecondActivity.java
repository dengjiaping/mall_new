package com.giveu.shoppingmall.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.view.EditView;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.view.SendCodeTextView;
import com.giveu.shoppingmall.view.dialog.NormalHintDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 添加银行卡二级页
 * Created by 101900 on 2017/2/22.
 */

public class AddCardSecondActivity extends BaseActivity {
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.checkbox_desc)
    TextView checkboxDesc;
    @BindView(R.id.tv_afresh)
    SendCodeTextView tvAfresh;
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
    private boolean isCheckBoxChecked = true;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addcardsecond);
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
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isCheckBoxChecked = isChecked;
            }
        });
    }

    @Override
    public void setData() {
        etBankName.checkFormat(EditView.Style.BANKNAME);
        etSendCode.checkFormat(6);
        etBankPhone.checkFormat(11);
        etBankName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if("工商银行".equals(s.toString())){
                    llSendCode.setVisibility(View.GONE);
                    etSendCode.setText("");
                }else{
                    llSendCode.setVisibility(View.VISIBLE);
                }
            }
        });
        etBankPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 11){
                    etSendCode.setEnabled(true);
                }else{
                    etSendCode.setEnabled(false);
                }
            }
        });
    }

    //是否输入相关信息的判断
    private boolean checkEditTextExceptCode() {
        String bankName = StringUtils.getTextFromView(etBankName);
        String bankPhone = StringUtils.getTextFromView(etBankPhone);
        if (StringUtils.isNull(bankName)) {
            ToastUtils.showShortToast("请输入银行类别");
            return false;
        }
        if (!StringUtils.checkPhoneNumberAndTipError(bankPhone,true)) {
            return false;
        }

        if (!isCheckBoxChecked) {
            ToastUtils.showShortToast("请勾选代扣服务协议");
            return false;
        }
        return true;
    }


    @OnClick({R.id.tv_afresh, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_afresh:
                if (checkEditTextExceptCode()) {
                    tvAfresh.startCount();
                }
                break;
            case R.id.tv_commit:
                if (checkEditTextExceptCode()) {
                    String bankCode = StringUtils.getTextFromView(etSendCode);
                    if (StringUtils.isNull(bankCode)) {
                        ToastUtils.showShortToast("请输入验证码");
                        return;
                    }
                    NormalHintDialog dialog = new NormalHintDialog(mBaseContext, "您已经绑定了该银行卡，请确认后重试");
                    dialog.showDialog();

                }
                break;
        }
    }

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, AddCardSecondActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvAfresh.stopCount();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
