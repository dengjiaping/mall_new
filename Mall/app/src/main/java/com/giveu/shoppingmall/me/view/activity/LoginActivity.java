package com.giveu.shoppingmall.me.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.index.activity.MainActivity;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by 513419 on 2017/6/19.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.iv_delete_account)
    ImageView ivDeleteAccount;
    @BindView(R.id.iv_delete_pwd)
    ImageView ivDeletePwd;

    private String lat;
    private String lng;
    private String userId;
    private String pwd;
    //标记定位次数，如果2次定位未成功，那么提示定位失败，并不再进行登录操作
    private int initLocCounts;
    private String deviceNumber;


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        baseLayout.setTitle("登录");
        baseLayout.setWhiteBlueStyle();
        dealLogoutByServer();
    }

    private void dealLogoutByServer() {
        boolean isLogoutByServer = getIntent().getBooleanExtra("isLogoutByServer", false);
        String logoutMsg = getIntent().getStringExtra("logoutMsg");
        if (isLogoutByServer && !TextUtils.isEmpty(logoutMsg)) {
           /* CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
            customDialogUtil.getDialogMode1("提示", logoutMsg, "确定", "", null, null).show();*/
        }
    }


    @OnClick({R.id.tv_login, R.id.iv_delete_account, R.id.iv_delete_pwd, R.id.tv_register,R.id.tv_forget_pwd})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.tv_login:
                //每次点击按钮重置定位次数
                initLocCounts = 1;
                turnToLogin();
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
                RequestPasswordActivity.startIt(mBaseContext);
                break;

            default:
                break;
        }
    }

    private void turnToLogin() {
        userId = etAccount.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        if (StringUtils.isNull(userId)) {
            ToastUtils.showShortToast("请输入账号");
            return;
        }
        if (StringUtils.isNull(pwd)) {
            ToastUtils.showShortToast("请输入密码");
            return;
        }
    }

    private void onLoginSuccess() {
        ToastUtils.showLongToast("登录成功");
//        LoginHelper.getInstance().saveLoginStatus(loginBean);
        //统计登录次数
        MobclickAgent.onEvent(mBaseContext, "Forward");
        //如果此时deviceNumber不为空那么设备号上传服务器已成功
        if (StringUtils.isNotNull(deviceNumber)) {
//            LoginHelper.getInstance().setHasUploadDeviceNumber(true);
        }
        //退出登录后，极光推送已关闭，此时登录成功后需恢复极光推送
        if (JPushInterface.isPushStopped(BaseApplication.getInstance())) {
            JPushInterface.resumePush(BaseApplication.getInstance());
        }
        MainActivity.startItDealLock(0, mBaseContext, LoginActivity.class.getName(), false);
        finish();
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
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    ivDeleteAccount.setVisibility(View.VISIBLE);
                } else {
                    ivDeleteAccount.setVisibility(View.GONE);
                }
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

        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    ivDeletePwd.setVisibility(View.VISIBLE);
                } else {
                    ivDeletePwd.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void setData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void logoutByServerAndStartIt(String msg) {
        LoginHelper.getInstance().logout();
        BaseApplication.getInstance().finishAllActivity();
        //停止极光推送并清除所有通知
        JPushInterface.stopPush(BaseApplication.getInstance());
        JPushInterface.clearAllNotifications(BaseApplication.getInstance());
        Context context = BaseApplication.getInstance().getApplicationContext();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isLogoutByServer", true);
        intent.putExtra("logoutMsg", msg);
        context.startActivity(intent);
    }

    // 返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}