package com.giveu.shoppingmall.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.view.SendCodeTextView;

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
    @BindView(R.id.et_bank_name)
    EditText etBankName;
    @BindView(R.id.et_bank_phone)
    EditText etBankPhone;
    @BindView(R.id.et_bank_code)
    EditText etBankCode;
    @BindView(R.id.tv_afresh)
    SendCodeTextView tvAfresh;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
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
    }

    //是否输入相关信息的判断
    private boolean checkEditTextExceptCode() {
        String bankName = StringUtils.getTextFromView(etBankName);
        String bankPhone = StringUtils.getTextFromView(etBankPhone);
        if (StringUtils.isNull(bankName)) {
            ToastUtils.showShortToast("请输入银行类别");
            return false;
        }
        if (!StringUtils.checkPhoneNumberAndTipError(bankPhone)) {
            return false;
        }
        if (!isCheckBoxChecked) {
            ToastUtils.showShortToast("请勾选代扣服务协议");
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
                    String bankCode = StringUtils.getTextFromView(etBankCode);
                    if (StringUtils.isNull(bankCode)) {
                        ToastUtils.showShortToast("请输入验证码");
                        return;
                    }
                    BaseApplication.getInstance().finishActivity(AddCardFirstActivity.class);
                    finish();
                }
                break;
        }
    }

    public static void startIt(Activity mActivity){
        Intent intent = new Intent(mActivity, AddCardSecondActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvAfresh.stopCount();
    }
}
