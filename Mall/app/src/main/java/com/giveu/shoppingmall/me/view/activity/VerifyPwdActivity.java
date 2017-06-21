package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.view.emptyview.CommonLoadingView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 508632 on 2016/12/22.
 * 登录
 */

public class VerifyPwdActivity extends BaseActivity {

    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_userId)
    TextView tv_userId;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    private String pwd;
    boolean isForClosePattern;


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_verify_pwd);
//        isForClosePattern = getIntent().getBooleanExtra("isForClose", false);

        baseLayout.hideBack();
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

//        tv_userName.setText(LoginHelper.getInstance().getUserName());
//        tv_userId.setText(LoginHelper.getInstance().getUserId());
//        if (StringUtils.isNotNull(LoginHelper.getInstance().getAvatar())) {
//            ImageUtils.loadImageWithCorner(LoginHelper.getInstance().getAvatar(), R.drawable.verify_head_default, R.drawable.verify_head_default, ivAvatar, DensityUtils.dip2px(30));
//        }
    }

    @OnClick(R.id.tv_change_account)
    public void changeAccount() {
        LoginActivity.startIt(mBaseContext);
    }


    @OnClick({R.id.tv_login})
    @Override
    public void onClick(View view) {
        //防止快速点击
        if (CommonUtils.isFastDoubleClick(view.getId())) {
            return;
        }

        String pwd = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShortToast("请输入密码");
            return;
        }

        CommonUtils.closeSoftKeyBoard(this);
        ApiImpl.checkPwd(mBaseContext, pwd, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                onVerifySuccess();
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }


    @Override
    public void setData() {

    }

    private void onVerifySuccess() {
        //登录密码解锁成功，清除手势密码
        SharePrefUtil.setPatternPwd("");
        SharePrefUtil.setFingerPrint(false);
        setResult(RESULT_OK);
        finish();
    }


    /**
     * 是否从手势解锁页面过来的
     */
    public static void startIt(Activity context, boolean isForClosePattern) {
        Intent intent = new Intent(context, VerifyPwdActivity.class);
        intent.putExtra("isForClose", isForClosePattern);
        context.startActivityForResult(intent, 10);
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
