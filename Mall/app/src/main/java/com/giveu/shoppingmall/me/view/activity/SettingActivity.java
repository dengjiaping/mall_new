package com.giveu.shoppingmall.me.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by 513419 on 2017/5/18.
 * 系统设置
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.iv_gesture)
    ImageView iv_gesture;
    @BindView(R.id.iv_finger)
    ImageView ivFinger;
    @BindView(R.id.ll_change_finger)
    LinearLayout llChangeFinger;
    private boolean hasEnterSetting;
    private ConfirmDialog settingDialog;
    private FingerprintIdentify mFingerprintIdentify;
    private CustomDialog fingerDialog;
    private TextView tvIdentifyResult;
    private BaseFingerprint.FingerprintIdentifyListener identifyListener;
    private boolean hasIdentifyFail;//是否验证失败，避免频繁操作指纹

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        baseLayout.setTitle("系统设置");
        baseLayout.setTitleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDevSecret();
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
        fingerDialog = new CustomDialog(mBaseContext, R.layout.dialog_finger, R.style.customerDialog, Gravity.CENTER, false);
        fingerDialog.setCanceledOnTouchOutside(false);
        fingerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //弹框消失时指纹验证取消
                mFingerprintIdentify.cancelIdentify();
            }
        });
        tvIdentifyResult = (TextView) fingerDialog.findViewById(R.id.tv_identify_result);
    }

    @OnClick({R.id.ll_change_finger, R.id.iv_gesture, R.id.ll_feedback, R.id.ll_change_pwd, R.id.ll_about_us, R.id.ll_new_version, R.id.tv_logout})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_feedback:
                break;

            case R.id.ll_change_pwd:
                break;

            case R.id.ll_about_us:
                break;

            case R.id.tv_logout:
                logout();
                break;
            case R.id.iv_gesture:
                onClickGesture();
                break;
            case R.id.ll_change_finger:
                onFingerIdentify();
                break;

            default:
                break;
        }

    }

    private void onFingerIdentify() {
        boolean isChecked = (boolean) iv_gesture.getTag();
        //开启指纹的前提条件是已开启手势解锁
        if (isChecked) {
            //指纹已录入，弹出验证框，指纹可用但未录入，弹出设置框
            if (mFingerprintIdentify.isFingerprintEnable()) {
                mFingerprintIdentify.resumeIdentify();
                //开始验证指纹
                fingerDialog.show();
                tvIdentifyResult.setText("");
                mFingerprintIdentify.startIdentify(10, identifyListener);
            } else if (mFingerprintIdentify.isHardwareEnable() && !mFingerprintIdentify.isFingerprintEnable()) {
                settingDialog.show();
            }

        } else {
            ToastUtils.showShortToast("请先开启手势解锁");
        }


    }

    private void onClickGesture() {
        boolean isChecked = (boolean) iv_gesture.getTag();
        if (isChecked) {
            CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
            customDialogUtil.getDialogMode1("确认关闭手势密码", "关闭手势密码后只能进行密码登录", "暂不", "解除", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //关闭手势
                    GestureLoginActivity.startItForClosePattern(mBaseContext);
                }
            }).show();
        } else {
            //新建手势
            CommonUtils.startActivity(mBaseContext, CreateGestureActivity.class);
        }
    }

    private void logout() {
        CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
        customDialogUtil.getDialogMode1("提示", "是否要退出登录？", "确定", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要重新读取通讯录
                LoginHelper.getInstance().logout();
                finish();
                //停止极光推送并清除所有通知
                JPushInterface.stopPush(BaseApplication.getInstance());
                JPushInterface.clearAllNotifications(BaseApplication.getInstance());
                BaseApplication.getInstance().finishAllActivity();
            }
        }, null).show();
    }

    @Override
    public void setListener() {
        super.setListener();

    }

    @Override
    public void setData() {
        mFingerprintIdentify = new FingerprintIdentify(this, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
            }
        });
        identifyListener = new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                hasIdentifyFail = false;
                if (!SharePrefUtil.hasFingerPrint()) {
                    tvIdentifyResult.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_9b9b9b));
                    tvIdentifyResult.setText("验证成功,指纹密码已可用");
                    ToastUtils.showShortToast("指纹解锁已可用");
                    SharePrefUtil.setFingerPrint(true);
                    setFingerIdentify(true);
                } else {
                    tvIdentifyResult.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_9b9b9b));
                    tvIdentifyResult.setText("验证成功,指纹密码已关闭");
                    ToastUtils.showShortToast("指纹密码已关闭");
                    SharePrefUtil.setFingerPrint(false);
                    setFingerIdentify(false);
                }
                fingerDialog.dismiss();
            }

            @Override
            public void onNotMatch(int availableTimes) {
                hasIdentifyFail = false;
                tvIdentifyResult.setTextColor(ContextCompat.getColor(mBaseContext, R.color.red_f4333c));
                tvIdentifyResult.setText("指纹不匹配");
            }

            @Override
            public void onFailed() {
                if (!hasIdentifyFail) {
                    tvIdentifyResult.setTextColor(ContextCompat.getColor(mBaseContext, R.color.red_f4333c));
                    tvIdentifyResult.setText("添加指纹密码失败");
                    ToastUtils.showShortToast("添加指纹密码失败");
                    hasIdentifyFail = true;
                } else {
                    ToastUtils.showShortToast("请勿频繁操作，请稍后重试");
                }
                mFingerprintIdentify.resumeIdentify();
                fingerDialog.dismiss();
            }
        };
        if (!mFingerprintIdentify.isHardwareEnable()) {
            llChangeFinger.setVisibility(View.GONE);
        }else {
            llChangeFinger.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //改变toggleSwitch的ui
        setIvGestureChecked(!TextUtils.isEmpty(SharePrefUtil.getPatternPwd()));
        //设置指纹解锁开关的开始状态
        setFingerIdentify(SharePrefUtil.hasFingerPrint());
        //是否从系统设置返回
        if (hasEnterSetting) {
            //重新更新指纹是否可用的状态
            mFingerprintIdentify = new FingerprintIdentify(mBaseContext, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
                @Override
                public void onCatchException(Throwable exception) {

                }
            });
            if (mFingerprintIdentify.isFingerprintEnable()) {
                if (!SharePrefUtil.hasFingerPrint()) {
                    mFingerprintIdentify.startIdentify(10, identifyListener);
                    fingerDialog.show();
                }
            } else {
                settingDialog.show();
            }
        }
    }

    public void setFingerIdentify(boolean checked) {
        if (checked) {
            ivFinger.setImageResource(R.drawable.ios_switch_checked);
        } else {
            ivFinger.setImageResource(R.drawable.ios_switch_unchecked);
        }
    }

    public void setIvGestureChecked(boolean checked) {
        if (checked) {
            iv_gesture.setImageResource(R.drawable.ios_switch_checked);
        } else {
            iv_gesture.setImageResource(R.drawable.ios_switch_unchecked);
        }
        iv_gesture.setTag(checked);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK) {
            //关闭手势， 验证手势密码成功
            ToastUtils.showShortToast("手势密码已关闭");
            setIvGestureChecked(false);
            SharePrefUtil.setPatternPwd("");
            setFingerIdentify(false);
            SharePrefUtil.setFingerPrint(false);
        }
    }

    private int devSecretClickCount = 0;
    private final long deltaTime = 10000;
    private long lastTime;

	/**
     * 点击顶部文字出现开发模式
     */
    public void clickDevSecret() {
        if (devSecretClickCount == 0) {
            lastTime = System.currentTimeMillis();
        }
        devSecretClickCount++;
        if (devSecretClickCount > 10) {
            devSecretClickCount = 0;
            //10s内连续点击才有效
            if (System.currentTimeMillis() - lastTime <= deltaTime) {
                CommonUtils.startActivity(mBaseContext, DevSettingActivity.class);
            }
        }
    }


}
