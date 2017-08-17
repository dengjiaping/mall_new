package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.activity.MainActivity;
import com.giveu.shoppingmall.me.presenter.LoginPresenter;
import com.giveu.shoppingmall.me.view.agent.ILoginView;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.FingerPrintHelper;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.MD5;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 508632 on 2016/12/22.
 * 登录
 */

public class VerifyPwdActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_userId)
    TextView tv_userId;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_login)
    ClickEnabledTextView tvLogin;
    @BindView(R.id.tv_change_account)
    TextView tvChangeAccount;
    boolean isForClosePattern;
    private LoginPresenter presenter;
    private boolean isForSetting;
    private boolean needFinishAll;
    private FingerPrintHelper fingerHelper;

    /**
     * 是否需要关闭指纹及手势解锁
     *
     * @param needFinishAll 是否需要关闭所有的activity，用于解锁时的判断，避免返回界面不关闭所有界面，以致绕过解锁
     */
    public static void startIt(Activity context, boolean isForClosePattern, boolean needFinishAll) {
        Intent intent = new Intent(context, VerifyPwdActivity.class);
        intent.putExtra("isForClosePattern", isForClosePattern);
        intent.putExtra("needFinishAll", needFinishAll);
        context.startActivityForResult(intent, 10);
    }

    /**
     * 安全中心过来的
     *
     * @param context
     * @param isForSetting
     * @param isForClosePattern 关闭还是开启
     */
    public static void startItForSetting(Activity context, boolean isForSetting, boolean isForClosePattern) {
        Intent intent = new Intent(context, VerifyPwdActivity.class);
        intent.putExtra("isForSetting", isForSetting);
        intent.putExtra("isForClosePattern", isForClosePattern);
        context.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_verify_pwd);
        isForClosePattern = getIntent().getBooleanExtra("isForClosePattern", false);
        isForSetting = getIntent().getBooleanExtra("isForSetting", false);
        needFinishAll = getIntent().getBooleanExtra("needFinishAll", false);
        if (isForSetting) {
            tvChangeAccount.setVisibility(View.GONE);
        }
        presenter = new LoginPresenter(this);
        if (isForSetting) {
            baseLayout.setTitle("验证登录密码");
        } else {
            baseLayout.hideBack();
            baseLayout.setTitle("解锁");
        }
        tv_userId.setText(LoginHelper.getInstance().getPhone());
        if (StringUtils.isNotNull(LoginHelper.getInstance().getUserPic())) {
            ImageUtils.loadImageWithCorner(LoginHelper.getInstance().getUserPic(), R.drawable.ic_default_avatar, ivAvatar, DensityUtils.dip2px(25));
        }
        fingerHelper = new FingerPrintHelper(mBaseContext);

    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @OnClick(R.id.tv_change_account)
    public void changeAccount() {
        LoginActivity.startIt(mBaseContext);
    }


    @Override
    public void setListener() {
        super.setListener();
        etPwd.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tvLogin.setClickEnabled(true);
                } else {
                    tvLogin.setClickEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.tv_login})
    @Override
    public void onClick(View view) {
        //防止快速点击
        if (CommonUtils.isFastDoubleClick(view.getId())) {
            return;
        }

        //不可点击什么也不操作
        if (!tvLogin.isClickEnabled()) {
            return;
        }
        CommonUtils.closeSoftKeyBoard(this);
        presenter.login(LoginHelper.getInstance().getPhone(), MD5.MD5Encode(etPwd.getText().toString()));
    }


    @Override
    public void setData() {

    }

    @Override
    public void onLoginSuccess(LoginResponse data) {
        LoginHelper.getInstance().saveLoginStatus(data, true);
        if (isForSetting) {
            //关闭手势或指纹
            if (isForClosePattern) {
                SharePrefUtil.setPatternPwd("");
                SharePrefUtil.setFingerPrint(false);
                //重新计时
                BaseApplication.getInstance().setLastestStopMillis(System.currentTimeMillis());
                setResult(RESULT_OK);
            } else {
                //指纹可用，只是设置指纹，不可用则使用手势
                if (fingerHelper.isHardwareEnable()) {
                    FingerPrintActivity.startIt(mBaseContext, true);
                } else {
                    CreateGestureActivity.startIt(mBaseContext);
                }
            }
        } else {
            //重新计时
            BaseApplication.getInstance().setLastestStopMillis(System.currentTimeMillis());
            MainActivity.startItDealLock(0, mBaseContext, VerifyPwdActivity.class.getName(), false);
        }
        finish();
    }

    @Override
    public void onLoginFail() {

    }

    @Override
    public void afterThirdLogin() {

    }

    @Override
    public void onBackPressed() {
        //解锁界面的返回，那么需要关闭所有页面，不然会返回到解锁前的前一个页面
        if (isForSetting || (isForClosePattern && !needFinishAll)) {
            super.onBackPressed();
        } else {
            BaseApplication.getInstance().finishAllActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CreateGestureActivity.REQUEST_FINISH && resultCode == RESULT_OK) {
            finish();
        }
    }
}
