package com.giveu.shoppingmall.index.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AgreementBean;
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
import com.giveu.shoppingmall.widget.dialog.PermissionDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;


    private String latitude;
    private String longitude;
    LocationUtils locationUtils;//定位方法
    NormalHintDialog walletActivationDialog;
    private PermissionDialog permissionDialog;
    private int hasLocationTimes = 0;

    public static void startIt(Activity mActivity, String name, String ident, String idPerson, String bankNo, String phone) {
        Intent intent = new Intent(mActivity, WalletActivationSecondActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("ident", ident);
        intent.putExtra("idPerson", idPerson);
        intent.putExtra("bankNo", bankNo);
        intent.putExtra("phone", phone);
        mActivity.startActivityForResult(intent, 100);
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
        walletActivationDialog = new NormalHintDialog(mBaseContext, "你的激活绑定手机与注册号码不一致,激活成功后，请通过绑定手机+登陆密码登陆");

        AgreementBean agreementBean1 = new AgreementBean("已阅读并同意", "《即有钱包激活协议》", "你要传的url");
        AgreementBean agreementBean2 = new AgreementBean("及代扣还款并出具本", "《代扣服务授权书》", "你要传的url");
        List<AgreementBean> agreementList = new ArrayList<>();
        agreementList.add(agreementBean1);
        agreementList.add(agreementBean2);
        //添加合同详情的下方动态的合同，个数会变化
        String str = "";
        for (int i = 0; i < agreementList.size(); i++) {
            str += agreementList.get(i).startStr + agreementList.get(i).endStr;
        }
        SpannableString msp = new SpannableString(str);
        addMsp(agreementList, msp);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        tvAgreement.setText(msp);

        initPermissionDialog();
        locationUtils.startLocation();
//这里以ACCESS_COARSE_LOCATION为例
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1001);//自定义的code
        }
    }

    //根据合同的数量来设置可点击的合同个数
    public void addMsp(final List<AgreementBean> agreementList, final SpannableString msp) {
        final List<AgreementBean> mAgreementList = agreementList;

        int total = 0;
        int start = 0;
        for (int i = 0; i < mAgreementList.size(); i++) {
            final int dataPosition = i;
            start = total + mAgreementList.get(i).startStr.length();
            total = start + mAgreementList.get(i).endStr.length();

            msp.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View widget) {
                    //跳转
                    ToastUtils.showShortToast(dataPosition + "");
                }
            }, start, total, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
        if(requestCode == 1001){
            ToastUtils.showLongToast("成功获取");
        }
    }
    private void initPermissionDialog() {
        permissionDialog = new PermissionDialog(mBaseContext);
        permissionDialog.setPermissionStr("请在系统设置中开启GPS和定位权限");
        permissionDialog.setNeedFinish(false);
        permissionDialog.setConfirmStr("去设置");
        permissionDialog.setCancleStr("暂不");
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
                hasLocationTimes++;
                if (hasLocationTimes == 2) {
                    int errorCode = ((AMapLocation) o).getErrorCode();
                    switch (errorCode) {
                        case 10:
                        case 12:
                        case 13:
                            permissionDialog.show();
                            break;
                        default:
                            break;
                    }
                    locationUtils.stopLocation();
                } else {
                    locationUtils.startLocation();
                    ToastUtils.showLongToast("正在定位");
                }
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
                    ApiImpl.activateWallet(mBaseContext, bankNo, idPerson, ident, latitude, longitude, orderNo, phone, name, sendSouce, code, smsSeq, new BaseRequestAgent.ResponseListener<WalletActivationResponse>() {
                        @Override
                        public void onSuccess(final WalletActivationResponse response) {
                            if (response != null) {
                                if (response.data != null) {
                                    WalletActivationResponse wallResponse = response.data;
                                    if (wallResponse.isPhoneChage) {
                                        //修改了手机号
                                        walletActivationDialog.showDialog();
                                        walletActivationDialog.setOnDialogDismissListener(new NormalHintDialog.OnDialogDismissListener() {
                                            @Override
                                            public void onDismiss() {
                                                ActivationStatusActivity.startShowResultSuccess(mBaseContext, response, idPerson);
                                            }
                                        });
                                    } else {
                                        ActivationStatusActivity.startShowResultSuccess(mBaseContext, response, idPerson);
                                    }
                                    setResult(RESULT_OK);
                                    LoginHelper.getInstance().setIdPerson(idPerson);
                                    BaseApplication.getInstance().fetchUserInfo();//刷新状态
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onError(BaseBean errorBean) {
                            ActivationStatusActivity.startShowResultFail(mBaseContext, errorBean.message, errorBean.result);
                            finish();
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
