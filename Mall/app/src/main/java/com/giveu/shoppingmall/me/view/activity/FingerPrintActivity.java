package com.giveu.shoppingmall.me.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.event.LoginSuccessEvent;
import com.giveu.shoppingmall.event.LotteryEvent;
import com.giveu.shoppingmall.utils.EventBusUtils;
import com.giveu.shoppingmall.utils.FingerPrintHelper;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/21.
 */

public class FingerPrintActivity extends BaseActivity {
    @BindView(R.id.tv_change_login)
    TextView tvChangeLogin;
    @BindView(R.id.tv_finger)
    TextView tvFinger;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    private FingerPrintHelper fingerHelper;
    private boolean isForSetting;
    private ConfirmDialog settingDialog;
    private ObjectAnimator translateAnim;
    private int failCount;

    /**
     * @param activity
     * @param isForSetting 是否从设置界面过来的，用来区分解锁和设置指纹
     */
    public static void startIt(Activity activity, boolean isForSetting) {
        Intent intent = new Intent(activity, FingerPrintActivity.class);
        intent.putExtra("isForSetting", isForSetting);
        activity.startActivityForResult(intent, CreateGestureActivity.REQUEST_FINISH);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_finger_print);
        baseLayout.setRightTextColor(R.color.color_00adb2);
        baseLayout.hideBack();
        isForSetting = getIntent().getBooleanExtra("isForSetting", false);
        //设置指纹是没有切换账号按钮的
        if (isForSetting) {
            tvChangeLogin.setVisibility(View.GONE);
            tvFinger.setText("设置指纹密码");
            baseLayout.setTitle("设置");
        } else {
            baseLayout.setTitle("解锁");
        }
        String textStr;
        if (isForSetting) {
            textStr = "取消";
        } else {
            textStr = "关闭";
        }
        baseLayout.setRightTextAndListener(textStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isForSetting) {
                    VerifyPwdActivity.startIt(mBaseContext, true, false);
                } else {
                    finish();
                }
            }
        });
        initDialog();
        fingerHelper = new FingerPrintHelper(mBaseContext);
        initAnim();
        registerEventBus();
    }

    private void initDialog() {
        settingDialog = new ConfirmDialog(mBaseContext);
        settingDialog.setContent("您还未录入指纹，请在设置中录入指纹");
        settingDialog.setConfirmStr("去设置");
        settingDialog.setCancleStr("取消");
        settingDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(LoginSuccessEvent successEvent) {
        finish();
    }

    private void initAnim() {
        translateAnim = ObjectAnimator.ofFloat(tvMessage, "translationX", 0.0f, -50, 0f, 50f, 0f);
        translateAnim.setDuration(1000);//动画时间
        translateAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fingerHelper.onResumeIdentify();
                fingerHelper.startIdentify();
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fingerHelper.onPauseIdentify();
            }
        });
        translateAnim.setInterpolator(new BounceInterpolator());//实现反复移动的效果
    }


    @Override
    protected void onResume() {
        super.onResume();
        fingerHelper.onResumeIdentify();
        if (fingerHelper.isFingerprintEnable()) {
            //最多指纹解锁10次
            fingerHelper.startIdentify();
        } else {
            settingDialog.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        fingerHelper.onPauseIdentify();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (translateAnim != null) {
            translateAnim.end();
            translateAnim = null;
        }
    }

    @Override
    public void setData() {
    }

    @Override
    public void setListener() {
        super.setListener();
        fingerHelper.setOnFingerMathchListener(new FingerPrintHelper.OnFingerMathchListener() {
            @Override
            public void onSuccess() {
                tvMessage.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_00adb2));
                BaseApplication.getInstance().setLastestStopMillis(System.currentTimeMillis());
                if (isForSetting) {
                    tvMessage.setText("指纹设置成功");
                    SharePrefUtil.setFingerPrint(true);
                    setResult(RESULT_OK);
                } else {
                    tvMessage.setText("指纹解锁成功");
                }
                fingerHelper.onPauseIdentify();
                //登录成功后需重新刷新周年庆活动状态
                EventBusUtils.poseEvent(new LotteryEvent());
                finish();
            }

            @Override
            public void onFailed() {
                //可能连续失败次数太多，下次进来会直接回调onFailed，因此需根据失败次数给出相应提示
                if (failCount == 0) {
                    tvMessage.setText("指纹识别暂不可用，请稍后重试");
                } else {
                    failCount = 0;
                    tvMessage.setTextColor(ContextCompat.getColor(mBaseContext, R.color.red_f3323b));
                    if (isForSetting) {
                        tvMessage.setText("指纹设置失败");
                    } else {
                        tvMessage.setText("指纹识别失败");
                        //指纹解锁失败，跳转至密码验证
                    }
                }
            }

            @Override
            public void onNotMatch() {
                failCount++;
                tvMessage.setTextColor(ContextCompat.getColor(mBaseContext, R.color.red_f3323b));
                tvMessage.setText("指纹密码错误");
                if (translateAnim != null) {
                    translateAnim.start();
                }
            }
        });
    }

    @OnClick(R.id.tv_change_login)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        LoginActivity.startIt(mBaseContext);
    }

    @Override
    public void onBackPressed() {
        if (isForSetting) {
            super.onBackPressed();
        } else {
            BaseApplication.getInstance().finishAllActivity();
        }
    }
}
