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
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private FingerprintIdentify mFingerprintIdentify;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, SecurityCenterActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_security_center);
        baseLayout.setTitle("安全中心");
//        ApiImpl.getUserInfo(mBaseContext, "10000923", new BaseRequestAgent.ResponseListener<UserInfoResponse>() {
//            @Override
//            public void onSuccess(UserInfoResponse response) {
//                ToastUtils.showShortToast("成功");
//            }
//
//            @Override
//            public void onError(BaseBean errorBean) {
//                CommonLoadingView.showErrorToast(errorBean);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StringUtils.isNotNull(SharePrefUtil.getPatternPwd())) {
            switchGesture.setImageResource(R.drawable.ios_switch_checked);
        } else {
            switchGesture.setImageResource(R.drawable.ios_switch_unchecked);
        }
    }

    @Override
    public void setData() {
        mFingerprintIdentify = new FingerprintIdentify(mBaseContext);
        if (mFingerprintIdentify.isHardwareEnable()) {
            tvLockType.setText("手势与指纹");
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
                RequestPasswordActivity.startIt(mBaseContext);
                //修改登录密码

                break;
            case R.id.ll_change_transaction_pwd:
//                if(StringUtils.isNull(LoginHelper.getInstance().getIdPerson())){
//                    ToastUtils.showShortToast("没有钱包资质");
//                    return;
//                }
                RequestPasswordActivity.startIt(mBaseContext, true);
                //修改交易密码
                break;
            case R.id.iv_switch:
                if (StringUtils.isNotNull(SharePrefUtil.getPatternPwd())) {
                    //去关闭
                    VerifyPwdActivity.startIt(mBaseContext, true);
                } else {
                    //去开启
                    VerifyPwdActivity.startItForSetting(mBaseContext, true);
                }
                //手势与指纹
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
