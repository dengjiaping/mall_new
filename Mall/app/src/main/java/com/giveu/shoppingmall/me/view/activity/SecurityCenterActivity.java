package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.FingerPrintHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 安全中心
 * Created by 101900 on 2017/6/29.
 */

public class SecurityCenterActivity extends BaseActivity {

    @BindView(R.id.ll_change_phone_number)
    LinearLayout llChangePhoneNumber;
    @BindView(R.id.ll_change_login_pwd)
    LinearLayout llChangeLoginPwd;
    @BindView(R.id.ll_change_transaction_pwd)
    LinearLayout llChangeTransactionPwd;
    @BindView(R.id.iv_switch)
    ImageView switchGesture;
    @BindView(R.id.tv_lock_type)
    TextView tvLockType;
    private FingerPrintHelper fingerHelper;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, SecurityCenterActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_security_center);
        baseLayout.setTitle("安全中心");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StringUtils.isNotNull(SharePrefUtil.getPatternPwd()) || SharePrefUtil.hasFingerPrint()) {
            switchGesture.setImageResource(R.drawable.ios_switch_checked);
        } else {
            switchGesture.setImageResource(R.drawable.ios_switch_unchecked);
        }
    }

    @Override
    public void setData() {
        fingerHelper = new FingerPrintHelper(mBaseContext);
        if (fingerHelper.isHardwareEnable()) {
            tvLockType.setText("指纹");
        } else {
            tvLockType.setText("手势");
        }
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    @OnClick({R.id.ll_change_phone_number, R.id.ll_change_login_pwd, R.id.ll_change_transaction_pwd, R.id.iv_switch})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_change_phone_number:
                //修改手机号
//                if (true) {
//                    //有资质的
//                    TransactionInputActivity.startIt(mBaseContext);
//                } else {
//                    //没有资质的
//                    ChangePhoneNumberActivity.startIt(mBaseContext);
//                }
//                if(StringUtils.isNull(LoginHelper.getInstance().getIdPerson())){
//                    WalletActivationFirstActivity.startIt(mBaseContext);
//                    return;
//                }
                TransactionInputActivity.startIt(mBaseContext);
                break;
            case R.id.ll_change_login_pwd:
                //修改登录密码
                RequestPasswordActivity.startIt(mBaseContext);
                break;
            case R.id.ll_change_transaction_pwd:
//                if(StringUtils.isNull(LoginHelper.getInstance().getIdPerson())){
//                    ToastUtils.showShortToast("没有钱包资质");
//                    return;
//                }
                //修改交易密码
                RequestPasswordActivity.startIt(mBaseContext, true);
                break;
            case R.id.iv_switch:
                if (StringUtils.isNotNull(SharePrefUtil.getPatternPwd()) || SharePrefUtil.hasFingerPrint()) {
                    //去关闭
                    VerifyPwdActivity.startItForSetting(mBaseContext, true, true);
                } else {
                    //去开启
                    VerifyPwdActivity.startItForSetting(mBaseContext, true, false);
                }
                break;
        }
    }
}
