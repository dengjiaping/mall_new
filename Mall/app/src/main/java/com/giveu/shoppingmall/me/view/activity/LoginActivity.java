package com.giveu.shoppingmall.me.view.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.event.LoginSuccessEvent;
import com.giveu.shoppingmall.event.LotteryEvent;
import com.giveu.shoppingmall.index.view.activity.MainActivity;
import com.giveu.shoppingmall.index.view.activity.WalletActivationFirstActivity;
import com.giveu.shoppingmall.me.presenter.LoginPresenter;
import com.giveu.shoppingmall.me.view.agent.ILoginView;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.EventBusUtils;
import com.giveu.shoppingmall.utils.FingerPrintHelper;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.MD5;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by 513419 on 2017/6/19.
 */

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.et_account)
    EditView etAccount;
    @BindView(R.id.et_pwd)
    EditView etPwd;
    @BindView(R.id.iv_delete_account)
    ImageView ivDeleteAccount;
    @BindView(R.id.iv_delete_pwd)
    ImageView ivDeletePwd;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.ll_third_login)
    LinearLayout llThirdLogin;
    @BindView(R.id.tv_login)
    ClickEnabledTextView tvLogin;
    private boolean needActive;

    private int keyHeight = 0; //软件盘弹起后所占高度

    private LoginPresenter presenter;
    private CustomDialog accountDialog;
    private TextView tvConfirm;

    public static void startIt(Activity activity) {
        startIt(activity, false);
    }

    public static void startIt(Activity activity, boolean needActive) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("needActive", needActive);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        baseLayout.setTitle("登录");
        baseLayout.setWhiteBlueStyle();
        dealLogoutByServer();
        etAccount.checkFormat(11);
        etPwd.setMaxLength(16);
        etPwd.checkFormat(8);
//        etPwd.setPasswordInputStyle();
        keyHeight = DensityUtils.getHeight() / 3;//弹起高度为屏幕高度的1/3
        presenter = new LoginPresenter(this);
        initAccount();
        needActive = getIntent().getBooleanExtra("needActive", false);
    }

    private void initAccount() {
        if (StringUtils.isNotNull(LoginHelper.getInstance().getRemeberAccount())) {
            etAccount.setText(LoginHelper.getInstance().getRemeberAccount());
            etAccount.setSelection(LoginHelper.getInstance().getRemeberAccount().length());
        }
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    private void dealLogoutByServer() {
        boolean isLogoutByServer = getIntent().getBooleanExtra("isLogoutByServer", false);
        String logoutMsg = getIntent().getStringExtra("logoutMsg");
        if (isLogoutByServer && !TextUtils.isEmpty(logoutMsg)) {
            CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
            customDialogUtil.getDialogMode1("提示", logoutMsg, "确定", "", null, null).show();
        }
    }


    @OnClick({R.id.tv_login, R.id.iv_delete_account, R.id.iv_delete_pwd,
            R.id.tv_register, R.id.tv_forget_pwd, R.id.iv_wechat_login, R.id.iv_qq_login, R.id.iv_weibo_login, R.id.tv_forget_account})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.tv_login:
                //每次点击按钮重置定位次数
                if (canClick(true)) {
                    presenter.login(etAccount.getText().toString(), MD5.MD5Encode(etPwd.getText().toString()));
                }
                break;

            case R.id.tv_register:
                RegisterActivity.startIt(mBaseContext);
                break;

            case R.id.iv_delete_account:
                etAccount.setText("");
                break;

            case R.id.iv_delete_pwd:
                etPwd.setText("");
                break;

            case R.id.tv_forget_pwd:
                RequestPasswordActivity.startIt(mBaseContext, RequestPasswordActivity.FIND_LOGIN_PWD);
                break;

            case R.id.iv_wechat_login:
                showLoading();
                presenter.WechatLogin();
                break;

            case R.id.iv_qq_login:
                showLoading();
                presenter.QQLogin();
                break;

            case R.id.iv_weibo_login:
                showLoading();
                presenter.SinaWeiboLogin();
                break;

            case R.id.tv_forget_account:
                initAccountDialog();
                accountDialog.show();
                break;

            default:
                break;
        }
    }

    private void initAccountDialog() {
        accountDialog = new CustomDialog(mBaseContext, R.layout.dialog_login_hint, R.style.customerDialog, Gravity.CENTER, false);
        tvConfirm = (TextView) accountDialog.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountDialog.dismiss();
            }
        });
    }

    private boolean canClick(boolean showToast) {
        tvLogin.setClickEnabled(false);
        if (etAccount.getText().toString().length() != 11) {
            if (showToast) {
                ToastUtils.showShortToast("请输入11位的手机号");
            }
            return false;
        }
        if (!StringUtils.checkLoginPwdAndTipError(etPwd.getText().toString(), showToast)) {
            return false;
        }
        tvLogin.setClickEnabled(true);
        return true;
    }

    private void onLoginSuccessV() {
/*        ToastUtils.showLongToast("登录成功");
        LoginHelper.getInstance().saveLoginStatus(loginBean);
        //统计登录次数
        MobclickAgent.onEvent(mBaseContext, "Forward");
        //如果此时deviceNumber不为空那么设备号上传服务器已成功
*//*        if (StringUtils.isNotNull(deviceNumber)) {
//            LoginHelper.getInstance().setHasUploadDeviceNumber(true);
        }
        //退出登录后，极光推送已关闭，此时登录成功后需恢复极光推送
        if (JPushInterface.isPushStopped(BaseApplication.getInstance())) {
            JPushInterface.resumePush(BaseApplication.getInstance());
        }*//*
        MainActivity.startItDealLock(0, mBaseContext, LoginActivity.class.getName(), false);
        finish();*/
    }

    @Override
    public void onLoginSuccess(LoginResponse data) {
        //重新登录后要先清空之前的登录信息，使用户有两次设置指纹或手势的机会,再重新保存用户信息
        LoginHelper.getInstance().logout();
        ToastUtils.showLongToast("登录成功");
        LoginHelper.getInstance().saveLoginStatus(data, true);
        BaseApplication.getInstance().setLastestStopMillis(System.currentTimeMillis());
        //统计登录次数
        MobclickAgent.onEvent(mBaseContext, "Forward");
        LotteryEvent lotteryEvent = new LotteryEvent();
        if (needActive) {
            Intent intent = new Intent(mBaseContext, MainActivity.class);
            startActivity(intent);
            if (!LoginHelper.getInstance().hasQualifications()) {
                WalletActivationFirstActivity.startIt(mBaseContext);
            } else {
                lotteryEvent.skip2H5 = true;
            }
        } else {
            //之前的逻辑是登录成功时调回首页的，当二级页面内进行登录操作后是不应该回到主页的，如果删除下面的注释代码
            //那么在二级页面返回时是会锁住屏幕，这是错误的，所以需要发通知告诉MainActivity，我已经登陆过了，你onStart的时候
            //不能锁住屏幕
//            MainActivity.startItDealLock(0, mBaseContext, LoginActivity.class.getName(), false);
            EventBusUtils.poseEvent(new LoginSuccessEvent());
        }
        EventBusUtils.poseEvent(lotteryEvent);
        settingPatternOrFingerPrint();
        finish();
    }

    /**
     * 设置手势密码的提示
     */
    public void settingPatternOrFingerPrint() {
        if (LoginHelper.getInstance().shouldShowSetting()
                && !SharePrefUtil.hasFingerPrint() && TextUtils.isEmpty(SharePrefUtil.getPatternPwd())) {
            FingerPrintHelper fingerHelper = new FingerPrintHelper(mBaseContext);
            if (fingerHelper.isHardwareEnable()) {
                FingerPrintActivity.startIt(mBaseContext, true);
            } else {
                CreateGestureActivity.startIt(mBaseContext);
            }
            LoginHelper.getInstance().reduceRemingTimes();
        }
    }

    @Override
    public void onLoginFail() {

    }

    @Override
    public void afterThirdLogin() {
        hideLoding();
    }

    @Override
    public void setListener() {
        super.setListener();
        etAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && ivDeleteAccount.getVisibility() == View.VISIBLE) {
                    ivDeleteAccount.setVisibility(View.GONE);
                } else if (hasFocus && etAccount.getText().toString().length() > 0) {
                    ivDeleteAccount.setVisibility(View.VISIBLE);
                }
            }
        });
        etAccount.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    ivDeleteAccount.setVisibility(View.VISIBLE);
                } else {
                    ivDeleteAccount.setVisibility(View.GONE);
                }
                canClick(false);
            }
        });

        etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && ivDeletePwd.getVisibility() == View.VISIBLE) {
                    ivDeletePwd.setVisibility(View.GONE);
                } else if (hasFocus && etPwd.getText().toString().length() > 0) {
                    ivDeletePwd.setVisibility(View.VISIBLE);
                }
            }
        });

        etPwd.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    ivDeletePwd.setVisibility(View.VISIBLE);
                } else {
                    ivDeletePwd.setVisibility(View.GONE);
                }
                canClick(false);
            }
        });


        /**
         * 禁止键盘弹起的时候可以滚动
         */
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        scrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                                       int oldTop, int oldRight, int oldBottom) {
              /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    int dist = llContent.getBottom() - bottom;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //如果系统版本大于等于6.0应该加上状态栏高度，否则弹起高度不够
                        dist += DensityUtils.getStatusBarHeight();
                    }
                    if (dist > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(llContent, "translationY", 0.0f, -dist);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                    }
                    llThirdLogin.setVisibility(View.INVISIBLE);

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    if ((llContent.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(llContent, "translationY", llContent.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                    }
                    llThirdLogin.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterActivity.REQUEST_PHONE && resultCode == RESULT_OK) {
            initAccount();
        }
    }

    @Override
    public void setData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void logoutByServerAndStartIt(String msg) {
        //清空本地登录信息
        LoginHelper.getInstance().logout();
        BaseApplication.getInstance().finishAllExceptMainActivity();
        //停止极光推送并清除所有通知
        JPushInterface.stopPush(BaseApplication.getInstance());
        JPushInterface.clearAllNotifications(BaseApplication.getInstance());
        Context context = BaseApplication.getInstance().getApplicationContext();
        //先开启首页，再跳转至登录界面
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isLogoutByServer", true);
        intent.putExtra("logoutMsg", msg);
        context.startActivity(intent);
    }
}