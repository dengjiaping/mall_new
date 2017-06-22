package com.giveu.shoppingmall.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.me.view.activity.LoginActivity;
import com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity;
import com.giveu.shoppingmall.me.view.activity.VerifyPwdActivity;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/21.
 */

public class FingerPrintActivity extends BaseActivity {
    private FingerprintIdentify mFingerprintIdentify;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, FingerPrintActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_finger_print);
        baseLayout.setTitle("解锁");
        baseLayout.setRightTextColor(R.color.color_00adb2);
        baseLayout.setRightTextAndListener("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyPwdActivity.startIt(mBaseContext, false);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFingerprintIdentify.resumeIdentify();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFingerprintIdentify.cancelIdentify();
    }


    @Override
    public void setData() {
        mFingerprintIdentify = new FingerprintIdentify(this, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
            }
        });
        if (mFingerprintIdentify.isFingerprintEnable() && SharePrefUtil.hasFingerPrint()) {
            try {
                mFingerprintIdentify.resumeIdentify();
                //最多指纹解锁10次
                mFingerprintIdentify.startIdentify(10, new BaseFingerprint.FingerprintIdentifyListener() {
                    @Override
                    public void onSucceed() {
                        BaseApplication.getInstance().setLastestStopMillis(System.currentTimeMillis());
                        mFingerprintIdentify.cancelIdentify();
                        finish();
                    }

                    @Override
                    public void onNotMatch(int availableTimes) {
                        if (availableTimes > 1) {
                        } else {
                        }
                    }

                    @Override
                    public void onFailed() {
                        ToastUtils.showShortToast("指纹识别失败");
                    }
                });
            } catch (Exception e) {
            }

        } else {
            ToastUtils.showShortToast("指纹识别不可用");
            VerifyPwdActivity.startIt(mBaseContext, false);
            finish();
        }
    }

    @OnClick(R.id.tv_change_login)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        LoginActivity.startIt(mBaseContext);
    }
}
