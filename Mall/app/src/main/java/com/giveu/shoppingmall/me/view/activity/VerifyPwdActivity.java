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
import com.giveu.shoppingmall.me.presenter.LoginPresenter;
import com.giveu.shoppingmall.me.view.agent.ILoginView;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
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
    private boolean isForClose;

    /**
     * 是否需要关闭指纹及手势解锁
     */
    public static void startIt(Activity context, boolean isForClosePattern) {
        Intent intent = new Intent(context, VerifyPwdActivity.class);
        intent.putExtra("isForClose", isForClosePattern);
        context.startActivityForResult(intent, 10);
    }

    /**
     * 安全中心过来的
     *
     * @param context
     * @param isForSetting
     */
    public static void startItForSetting(Activity context, boolean isForSetting) {
        Intent intent = new Intent(context, VerifyPwdActivity.class);
        intent.putExtra("isForSetting", isForSetting);
        context.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_verify_pwd);
        isForClosePattern = getIntent().getBooleanExtra("isForClose", false);
        isForSetting = getIntent().getBooleanExtra("isForSetting", false);
        isForClose = getIntent().getBooleanExtra("isForClose", false);
        if (isForSetting) {
            tvChangeAccount.setVisibility(View.GONE);
        }

        baseLayout.hideBack();
        presenter = new LoginPresenter(this);
/*        SpannableString cancleText = StringUtils.getColorSpannable("", "取消", R.color.color_00adb2, R.color.color_00adb2);
        baseLayout.setRightTextAndListener(cancleText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
/*
        SpannableString titleText;
        if (isForClosePattern){
            //页面的作用是关闭手势
            titleText = StringUtils.getColorSpannable("", "关闭手势", R.color.color_4a4a4a, R.color.color_4a4a4a);
        }else{
            //页面的作用是解锁
            if (TextUtils.isEmpty(SharePrefUtil.getInstance().getPatternPwd())){
                //没有手势密码，那么这个页面的作用就是解锁不允许关闭
                SpannableString rigthText = StringUtils.getColorSpannable("", "切换账号", R.color.color_00adb2, R.color.color_00adb2);
                baseLayout.setRightTextAndListener(rigthText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        CommonUtils.startActivity(mBaseContext, LoginActivity.class);
                        finish();
                    }
                });
            }
            titleText = StringUtils.getColorSpannable("", "解锁", R.color.color_4a4a4a, R.color.color_4a4a4a);
        }*/
        baseLayout.setTitle("登录");
        tv_userId.setText(LoginHelper.getInstance().getPhone());
        if (StringUtils.isNotNull(LoginHelper.getInstance().getUserPic())) {
            ImageUtils.loadImageWithCorner(LoginHelper.getInstance().getUserPic(), R.drawable.ic_default_avatar, ivAvatar, DensityUtils.dip2px(25));
        }
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
        presenter.login(LoginHelper.getInstance().getPhone(), etPwd.getText().toString());
    }


    @Override
    public void setData() {

    }

    @Override
    public void onLoginSuccess(LoginResponse data) {
        if (isForSetting) {
            //密码验证成功后设置手势密码
            CreateGestureActivity.startIt(mBaseContext);
        } else {
            if (isForClose) {
                //清除手势密码
                SharePrefUtil.setPatternPwd("");
                SharePrefUtil.setFingerPrint(false);
                //重新计时
                BaseApplication.getInstance().setLastestStopMillis(System.currentTimeMillis());
                setResult(RESULT_OK);
            }
            finish();
        }

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
        if (isForClose || isForSetting) {
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
