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
import com.giveu.shoppingmall.index.view.activity.TransactionPwdActivity;
import com.giveu.shoppingmall.me.view.dialog.NotActiveDialog;
import com.giveu.shoppingmall.utils.FingerPrintHelper;
import com.giveu.shoppingmall.utils.LoginHelper;
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
    NotActiveDialog notActiveDialog;//未开通钱包的弹窗

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, SecurityCenterActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_security_center);
        baseLayout.setTitle("安全中心");
        notActiveDialog = new NotActiveDialog(mBaseContext);
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
            tvLockType.setText("指纹解锁");
        } else {
            tvLockType.setText("图案解锁");
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
                if (isWallActivationAndPwd()) {//激活并设置了交易密码
                    TransactionInputActivity.startIt(mBaseContext);
                }
                break;
            case R.id.ll_change_login_pwd:
                //修改登录密码
                RequestPasswordActivity.startIt(mBaseContext,RequestPasswordActivity.CHANGE_LOGIN_PWD);
                break;
            case R.id.ll_change_transaction_pwd:
                //修改交易密码
                if (isWallActivationAndPwd()) {//激活并设置了交易密码
                    RequestPasswordActivity.startIt(mBaseContext, RequestPasswordActivity.CHANGE_TRADE_PWD);
                }
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

    /**
     * 是否激活钱包并且设置了交易密码，没有激活跳转激活，激活没有设置密码跳转设置交易密码
     */
    public boolean isWallActivationAndPwd() {
        if (LoginHelper.getInstance().hasQualifications()) {
            //判断是否设置了交易密码
            if (!LoginHelper.getInstance().hasSetPwd()) {//没有设置交易密码
                TransactionPwdActivity.startIt(mBaseContext, LoginHelper.getInstance().getIdPerson());
                return false;
            }
            return true;
        } else {
            notActiveDialog.showDialog();
            return false;
        }
    }
}
