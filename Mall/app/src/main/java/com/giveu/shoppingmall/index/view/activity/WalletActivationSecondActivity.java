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

import com.amap.api.location.AMapLocation;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.SmsCodeResponse;
import com.giveu.shoppingmall.model.bean.response.WalletActivationResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LocationUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.LocationListener;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.SendCodeTextView;
import com.giveu.shoppingmall.widget.dialog.NormalHintDialog;
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
    String orderNo;
    String sendSouce;
    String smsSeq;
    String idPerson;
    String ident;
    String name;
    String bankNo;
    String phone;
    private String latitude;
    private String longitude;
    LocationUtils locationUtils;//定位方法
    NormalHintDialog walletActivationDialog;
    NormalHintDialog changePhoneDialog;

    public static void startIt(Activity mActivity, String name, String ident, String idPerson, String bankNo, String phone) {
        Intent intent = new Intent(mActivity, WalletActivationSecondActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("ident", ident);
        intent.putExtra("idPerson", idPerson);
        intent.putExtra("bankNo", bankNo);
        intent.putExtra("phone", phone);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_activation_second);
        baseLayout.setTitle("钱包激活");
        CommonUtils.openSoftKeyBoard(mBaseContext);
        tvSendCode.setSendTextColor(false);
        idPerson = getIntent().getStringExtra("idPerson");
        ident = getIntent().getStringExtra("ident");
        name = getIntent().getStringExtra("name");
        bankNo = getIntent().getStringExtra("bankNo");
        phone = getIntent().getStringExtra("phone");

        if (StringUtils.isNotNull(bankNo)) {
            etBankNo.setText(bankNo);
        }
        if (StringUtils.isNotNull(phone)) {
            etPhone.setText(phone);
        }
        locationUtils = new LocationUtils(mBaseContext);
        locationUtils.startLocation();
        walletActivationDialog = new NormalHintDialog(mBaseContext, "你的激活绑定手机与注册号码不一致！\n", "激活成功后，请通过绑定手机+登陆密码登陆");
    }

    @Override
    public void setListener() {
        super.setListener();
        etPhone.checkFormat(11);
        etCode.checkFormat(6);
        editTextListener(etPhone, ivPhone);
        editTextListener(etCode, ivCode);
        editTextListener(etBankNo, ivBankNo);
        showPhoneTextColor(ivPhone, etPhone, StringUtils.getTextFromView(etPhone));
        showPhoneTextColor(ivBankNo, etBankNo, StringUtils.getTextFromView(etBankNo));

        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonCanClick(false);
            }
        });

        locationUtils.setOnLocationListener(new LocationListener() {
            @Override
            public void onSuccess(AMapLocation locationResult) {
                latitude = locationResult.getLatitude() + "";
                longitude = locationResult.getLongitude() + "";
            }

            @Override
            public void onFail(Object o) {
                ToastUtils.showShortToast("定位失败！");
            }
        });
    }

    @Override
    public void setData() {

        if (StringUtils.isNotNull(StringUtils.getTextFromView(etBankNo))) {
            etBankNo.setSelection(etBankNo.length());
        }
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
                showPhoneTextColor(imageView, editText, s.toString());
            }
        });
    }

    /**
     * 银行卡、手机号从上一个页面带过来的颜色逻辑处理
     *
     * @param phone
     */
    public void showPhoneTextColor(ImageView iv, final EditText editText, String phone) {
        if (phone.length() > 0) {
            iv.setImageResource(R.drawable.ic_pen);
        } else {
            iv.setImageResource(R.drawable.ic_add);
        }
        if (etPhone == editText) {
            //如果是手机号的EditText还需判断验证码是否可以点击
            if (StringUtils.checkPhoneNumberAndTipError(phone, false)) {
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

    @OnClick({R.id.tv_send_code, R.id.tv_activation})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        final String phone = StringUtils.nullToEmptyString(StringUtils.getTextFromView(etPhone));
        String bankNo = StringUtils.nullToEmptyString(StringUtils.getTextFromView(etBankNo));
        String code = StringUtils.nullToEmptyString(StringUtils.getTextFromView(etCode));

        switch (view.getId()) {
            case R.id.tv_send_code:
                ApiImpl.sendActivateSmsCode(mBaseContext, bankNo, idPerson, ident, name, phone, new BaseRequestAgent.ResponseListener<SmsCodeResponse>() {
                    @Override
                    public void onSuccess(SmsCodeResponse response) {
                        SmsCodeResponse smsCodeResponse = response.data;
                        if (smsCodeResponse != null) {
                            orderNo = StringUtils.nullToEmptyString(smsCodeResponse.orderNo);
                            sendSouce = StringUtils.nullToEmptyString(smsCodeResponse.sendSouce);
                            smsSeq = StringUtils.nullToEmptyString(smsCodeResponse.smsSeq);
                        }
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
                    locationUtils.startLocation();//定位获取经纬度
                    ApiImpl.activateWallet(mBaseContext, bankNo, idPerson, ident, latitude, longitude, orderNo, phone, name, sendSouce, code, smsSeq, new BaseRequestAgent.ResponseListener<WalletActivationResponse>() {
                        @Override
                        public void onSuccess(final WalletActivationResponse response) {
                            if (!phone.equals(LoginHelper.getInstance().getPhone())) {
                                //激活填写手机号与注册不一致
                                walletActivationDialog.setOnDialogDismissListener(new NormalHintDialog.OnDialogDismissListener() {
                                    @Override
                                    public void onDismiss() {
                                        ActivationStatusActivity.startShowResultSuccess(mBaseContext, response.data, idPerson);
                                    }
                                });
                                walletActivationDialog.showDialog();
                            } else {
                                ActivationStatusActivity.startShowResultSuccess(mBaseContext, response.data, idPerson);
                            }
                        }

                        @Override
                        public void onError(BaseBean errorBean) {
                            ActivationStatusActivity.startShowResultFail(mBaseContext, errorBean.message, errorBean.result);
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

        if (StringUtils.isNull(bankNo)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入银行卡号！");
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
        if (locationUtils != null) {
            locationUtils.destory();
        }
    }

}
