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

import com.android.volley.mynet.ApiUrl;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.event.AddCardEvent;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AgreementApplyResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.EventBusUtils;
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
    String bankName;
    String bankPhone;
    String orderNo;
    String smsSeq;
    String bankNo;
    String sendCode;

    public static void startIt(Activity mActivity, String bankCode, String bankNo, String bankName, String bankPerson) {
        Intent intent = new Intent(mActivity, AddBankCardSecondActivity.class);
        intent.putExtra("bankCode", bankCode);
        intent.putExtra("bankNo", bankNo);
        intent.putExtra("bankName", bankName);
        intent.putExtra("bankName", bankName);
        intent.putExtra("bankPerson", bankPerson);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addcardsecond);
        baseLayout.setTitle("添加银行卡");
        tvSendCode.setSendTextColor(true);
        bankName = getIntent().getStringExtra("bankName");
        etBankName.setText(bankName);
        CommonUtils.setTextWithSpan(checkboxDesc, false, "本人同意代扣还款并出具本", "《代扣服务授权书》", R.color.black, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //代扣服务授权书
                CustomWebViewActivity.startIt(mBaseContext, ApiUrl.WebUrl.authorize, "《代扣服务授权书》");

            }
        });
    }

    @Override
    public void setListener() {
        etBankName.checkFormat(EditView.Style.BANKNAME);
        int length = StringUtils.getTextFromView(etBankName).length();
        etBankName.setSelection(length);
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
                    if (!tvSendCode.isCounting()) {
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
        bankName = StringUtils.getTextFromView(etBankName);
        bankPhone = StringUtils.getTextFromView(etBankPhone);
        if (StringUtils.isNull(bankName)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入银行类别！");
            }
            return;
        }
        if (!bankName.matches("[\\u4e00-\\u9fa5]+$")) {
            if (showToast) {
                ToastUtils.showShortToast("请输入正确的银行类别！");
            }
            return;
        }
        if (!StringUtils.checkPhoneNumberAndTipError(bankPhone, showToast)) {
            return;
        }
        if (llSendCode != null) {
            sendCode = StringUtils.getTextFromView(etSendCode);
            if (StringUtils.isNull(sendCode)) {
                if (showToast) {
                    ToastUtils.showShortToast("请输入6位验证码！");
                }
                return;
            } else if (sendCode.length() != 6) {
                if (showToast) {
                    ToastUtils.showShortToast("请输入6位验证码！");
                }
                return;
            }
        }

        if (!checkbox.isChecked()) {
            if (showToast) {
                ToastUtils.showShortToast("请勾选代扣服务授权书！");
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
                String bankCode = getIntent().getStringExtra("bankCode");
                bankNo = getIntent().getStringExtra("bankNo");
                bankCode = StringUtils.nullToEmptyString(bankCode);
                bankNo = StringUtils.nullToEmptyString(bankNo);
                ApiImpl.agreementApply(mBaseContext, bankCode, bankNo, LoginHelper.getInstance().getIdPerson(), bankPhone, new BaseRequestAgent.ResponseListener<AgreementApplyResponse>() {
                    @Override
                    public void onSuccess(AgreementApplyResponse response) {
                        if (response != null) {
                            if (response.data != null) {
                                smsSeq = response.data.smsSeq;
                                orderNo = response.data.orderNo;
                            }
                        }
                        tvSendCode.startCount(new SendCodeTextView.CountEndListener() {
                            @Override
                            public void onEnd() {
                                String bankPhone = StringUtils.getTextFromView(etBankPhone);
                                if (StringUtils.checkPhoneNumberAndTipError(bankPhone, false)) {
                                    tvSendCode.setBackgroundResource(R.color.title_color);
                                    tvSendCode.setEnabled(true);
                                } else {
                                    tvSendCode.setBackgroundResource(R.color.color_d8d8d8);
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
            case R.id.tv_commit:
                if (tvCommit.isClickEnabled()) {
                    String bankPerson = getIntent().getStringExtra("bankPerson");
                    bankPerson = StringUtils.nullToEmptyString(bankPerson);
//                     String isDefault;
//                    if (LoginHelper.getInstance().hasDefaultCard()) {
//                        //有默认卡
//                        isDefault = "0";//当前设置其他卡
//                    } else {
//                        //无默认卡
//                        isDefault = "1";//当前设置默认卡
//                    }

                    ApiImpl.addBankCard(mBaseContext, bankPhone, bankName, bankNo, bankPerson, sendCode, LoginHelper.getInstance().getIdPerson(), LoginHelper.getInstance().getIdent(), "1", orderNo, "1", smsSeq, new BaseRequestAgent.ResponseListener<AgreementApplyResponse>() {
                        NormalHintDialog dialog;

                        @Override
                        public void onSuccess(AgreementApplyResponse response) {
                            dialog = new NormalHintDialog(mBaseContext, "银行卡添加成功，\n已自动设置为默认卡");
                            dialog.showDialog();
                            dialog.setOnDialogDismissListener(new NormalHintDialog.OnDialogDismissListener() {
                                @Override
                                public void onDismiss() {
                                    finish();
                                }
                            });
                            //添加银行卡成功，hasDefault设为true，表示有默认卡
                            LoginHelper.getInstance().setHasDefaultCard(true);
                            LoginHelper.getInstance().setBankName(bankName);
                            LoginHelper.getInstance().setDefaultCard(bankNo);
                            //通知我的银行卡列表刷新界面
                            EventBusUtils.poseEvent(new AddCardEvent());
                            BaseApplication.getInstance().fetchUserInfo();
                            BaseApplication.getInstance().finishActivity(AddBankCardFirstActivity.class);
                        }

                        @Override
                        public void onError(BaseBean errorBean) {
                            if (errorBean != null) {
                                dialog = new NormalHintDialog(mBaseContext, errorBean.message);
                                dialog.showDialog();
                            }
                        }
                    });
                } else {
                    buttonCanClick(true);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvSendCode.onDestory();
    }

}
