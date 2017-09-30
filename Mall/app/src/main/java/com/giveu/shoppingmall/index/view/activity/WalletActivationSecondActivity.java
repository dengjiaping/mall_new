package com.giveu.shoppingmall.index.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import com.android.volley.mynet.ApiUrl;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.fastaccess.permission.base.PermissionHelper;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BasePermissionActivity;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.me.view.activity.BankActivity;
import com.giveu.shoppingmall.me.view.activity.CustomWebViewActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AgreementBean;
import com.giveu.shoppingmall.model.bean.response.SmsCodeResponse;
import com.giveu.shoppingmall.model.bean.response.WalletActivationResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LocationUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.LocationListener;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.SendCodeTextView;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;
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

public class WalletActivationSecondActivity extends BasePermissionActivity {

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
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.tv_banklist)
    TextView tvBanklist;
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
    private PermissionDialog permissionDialog;
    private boolean isPermissionReallyDeclined;//该boolean的意义参考SplashActivity
    boolean isGPSConfirm = false;//GPS权限弹窗确认按钮标记，true 点击去开启回来继续弹窗
    boolean isGPS = false;//是否是GPS权限弹窗去开启回来的标记
    CustomDialog dialog;
    LocationManager locationManager;

    public static void startIt(Activity mActivity, String name, String ident, String idPerson, String bankNo, String phone) {
        Intent intent = new Intent(mActivity, WalletActivationSecondActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("ident", ident);
        intent.putExtra("idPerson", idPerson);
        intent.putExtra("bankNo", bankNo);
        intent.putExtra("phone", phone);
        mActivity.startActivityForResult(intent, Const.ACTIVATION_CODE);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_activation_second);
        baseLayout.setTitle("钱包激活");
        tvSendCode.setSendTextColor(false);
        idPerson = getIntent().getStringExtra("idPerson");
        ident = getIntent().getStringExtra("ident");
        name = getIntent().getStringExtra("name");
        bankNo = getIntent().getStringExtra("bankNo");
        phone = getIntent().getStringExtra("phone");
        initGPS();
        locationManager = (LocationManager) mBaseContext.getSystemService(Context.LOCATION_SERVICE);
        //查看支持银行列表
        CommonUtils.setTextWithSpan(tvBanklist, false, "查看", "支持银行", R.color.color_9b9b9b, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开银行列表
                CommonUtils.closeSoftKeyBoard(mBaseContext);
                BankActivity.startIt(mBaseContext);
            }
        });

        if (StringUtils.isNotNull(bankNo)) {
            etBankNo.setText(bankNo);
        }
        if (StringUtils.isNotNull(phone)) {
            etPhone.setText(phone);
        }
        locationUtils = new LocationUtils(mBaseContext);

        walletActivationDialog = new NormalHintDialog(mBaseContext, "你的激活绑定手机与注册号码不一致,激活成功后，请通过绑定手机+登录密码登录");
        //设置协议
        setTvAgreement(tvAgreement);
        initPermissionDialog();
        //6.0以下直接调取位置信息，6.0以上先申请权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
          //  locationUtils.startLocation(mBaseContext);
        }
    }

    /**
     * 设置协议
     */
    public void setTvAgreement(TextView tv) {
        AgreementBean agreementBean1 = new AgreementBean("已阅读并同意", "《即有钱包激活协议》", ApiUrl.WebUrl.pAProtocol);
        AgreementBean agreementBean2 = new AgreementBean("及代扣还款并出具本", "《代扣服务授权书》", ApiUrl.WebUrl.authorize);
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
        tv.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        tv.setText(msp);
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
                    CustomWebViewActivity.startIt(mBaseContext, mAgreementList.get(dataPosition).url, mAgreementList.get(dataPosition).endStr);
                }
            }, start, total, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGPS) {
            //是否是gps没开，去开启回来的状态
            if (StringUtils.isNull(longitude) || StringUtils.isNull(latitude)) {
                //没有获取到经纬度
                if (permissionDialog.isConfirm() || isGPSConfirm) {
                    //位置权限点击去开启或GPS权限点击去开启回来继续定位
                    locationUtils.startLocation(mBaseContext);
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0以上
            if (!isPermissionReallyDeclined) {
                if (!PermissionHelper.getInstance(this).isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    setPermissionHelper(true, new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
                }
            }
        } else {
            //6.0以下
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        super.onPermissionReallyDeclined(permissionName);
        permissionDialog.setPermissionStr("钱包激活需要位置权限才可正常使用");
        permissionDialog.show();
        isPermissionReallyDeclined = true;
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        super.onPermissionGranted(permissionName);
     //   locationUtils.startLocation(mBaseContext);
    }

    private void initPermissionDialog() {
        permissionDialog = new PermissionDialog(mBaseContext);
        permissionDialog.setPermissionStr(getResources().getString(R.string.app_name) + "需要位置权限才可正常使用");
        permissionDialog.setConfirmStr("去开启");
        permissionDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                permissionDialog.dismiss();
                isPermissionReallyDeclined = false;
            }

            @Override
            public void cancel() {
                permissionDialog.dismiss();
            }
        });
    }


    @Override
    public void setListener() {
        super.setListener();
        etPhone.checkFormat(11);
        etCode.checkFormat(6);
        editTextListener(etPhone, ivPhone);
        editTextListener(etCode, ivCode);
        editTextListener(etBankNo, ivBankNo);
        showPhoneTextColor(etPhone, StringUtils.getTextFromView(etPhone));
        showPhoneTextColor(etBankNo, StringUtils.getTextFromView(etBankNo));

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
                locationUtils.stopLocation(mBaseContext);
            }

            @Override
            public void onFail(Object o) {
                hideLoding();
                if (!isNetworkConnected(mBaseContext)) {
                    ToastUtils.showShortToast("网络不可用");
                    return;
                }
                if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    //没有GPS权限，显示弹窗，并附上标记
                    isGPS = true;
                    dialog.show();
                } else {
                    isGPSConfirm = false;
                    if (!PermissionHelper.getInstance(mBaseContext).isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        setPermissionHelper(true, new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
                    }
                }
            }
        });
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private void initGPS() {
        // 判断GPS模块是否开启，如果没有则开启
        CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
        dialog = customDialogUtil.getDialogModeOneHint("钱包激活需要开启GPS，是否开启？", "取消", "去开启", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }
                , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 转到手机设置界面，用户设置GPS
                        isGPSConfirm = true;
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                    }
                });
        dialog.setCancelable(false);
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
                showPhoneTextColor(editText, s.toString());
            }
        });
    }

    /**
     * 银行卡、手机号从上一个页面带过来的颜色逻辑处理
     *
     * @param phone
     */
    public void showPhoneTextColor(final EditText editText, String phone) {
//        if (phone.length() > 0) {
//            iv.setImageResource(R.drawable.ic_pen);
//        } else {
//            iv.setImageResource(R.drawable.ic_add);
//        }
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
                //发送验证码
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
                //点击激活
                //没有经纬度，并开启定位
                if (StringUtils.isNull(latitude) || StringUtils.isNull(longitude)) {
                    locationUtils.startLocation(mBaseContext);
                }
                if (StringUtils.isNotNull(latitude) && StringUtils.isNotNull(longitude)) {
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
                                                    //显示成功页
                                                    activationSuccess(mBaseContext, response, idPerson, "success");
                                                }
                                            });
                                        } else {
                                            //显示成功页
                                            activationSuccess(mBaseContext, response, idPerson, "success");
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onError(BaseBean errorBean) {
                                ActivationStatusActivity.startShowResultFail(mBaseContext, errorBean, "fail");
                            }
                        });
                    } else {
                        buttonCanClick(true);
                    }
                }

                break;
        }
    }

    /**
     * 激活成功显示状态页面
     */
    public void activationSuccess(Activity activity, WalletActivationResponse wallResponse, String idPerson, String status) {
        ActivationStatusActivity.startShowResultSuccess(activity, wallResponse, idPerson, status, wallResponse.data.coupon, wallResponse.data.hasShowCouponDialog());
        setResult(RESULT_OK);
        finish();
        LoginHelper.getInstance().setIdPerson(idPerson);
        BaseApplication.getInstance().fetchUserInfo();//刷新状态
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
                ToastUtils.showShortToast("请勾选即有钱包激活协议和代扣服务授权书！");
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
