package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/21.
 */

public class FingerPrintActivity extends BaseActivity {
    @BindView(R.id.tv_change_login)
    TextView tvChangeLogin;
    private FingerprintIdentify mFingerprintIdentify;
    private boolean isForSetting;
    private ConfirmDialog settingDialog;
    private boolean hasEnterSetting;

    /**
     * @param activity
     * @param isForSetting 是否从设置界面过来的，用来区分解锁和设置指纹
     */
    public static void startIt(Activity activity, boolean isForSetting) {
        Intent intent = new Intent(activity, FingerPrintActivity.class);
        intent.putExtra("isForSetting", isForSetting);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_finger_print);
        baseLayout.setTitle("解锁");
        baseLayout.setRightTextColor(R.color.color_00adb2);
        isForSetting = getIntent().getBooleanExtra("isForSetting", false);
        //设置指纹是没有切换账号按钮的
        if (isForSetting) {
            tvChangeLogin.setVisibility(View.GONE);
        }
        baseLayout.setRightTextAndListener("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isForSetting) {
                    VerifyPwdActivity.startIt(mBaseContext, false);
                }
                finish();
            }
        });
        initDialog();
    }

    private void initDialog() {
        settingDialog = new ConfirmDialog(mBaseContext);
        settingDialog.setContent("您还未录入指纹，请在设置中录入指纹");
        settingDialog.setConfirmStr("去设置");
        settingDialog.setCancleStr("取消");
        settingDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                hasEnterSetting = true;
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                settingDialog.dismiss();
            }

            @Override
            public void cancle() {
                settingDialog.dismiss();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (hasEnterSetting) {
            //j系统设置后返回需重新更新指纹是否可用的状态
            mFingerprintIdentify = new FingerprintIdentify(mBaseContext, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
                @Override
                public void onCatchException(Throwable exception) {

                }
            });
            //指纹可用时开始录入指纹，否则弹出设置框
            if (mFingerprintIdentify.isFingerprintEnable()) {
                mFingerprintIdentify.resumeIdentify();
            } else {
                settingDialog.show();
            }
            hasEnterSetting = false;
        } else {
            mFingerprintIdentify.resumeIdentify();
        }
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
                        if (isForSetting) {
                            ToastUtils.showShortToast("指纹设置成功");
                        }
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
            if (!isForSetting) {
                VerifyPwdActivity.startIt(mBaseContext, false);
            }
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
