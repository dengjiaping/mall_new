package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.me.view.activity.IdentifyActivity;
import com.giveu.shoppingmall.me.view.activity.RequestPasswordActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置交易密码成功页
 * Created by 101900 on 2017/6/19.
 */

public class SetPwdStatusActivity extends BaseActivity {

    String flag;//找回交易密码的标记
    @BindView(R.id.tv_back)
    TextView tvBack;

    //钱包激活设置交易密码
    public static void startSetPwd(Activity mActivity) {
        Intent intent = new Intent(mActivity, SetPwdStatusActivity.class);
        mActivity.startActivity(intent);
    }

    //用于finish之前页面（找回交易密码）
    public static void startSetPwd(Activity mActivity, String flag) {
        Intent intent = new Intent(mActivity, SetPwdStatusActivity.class);
        intent.putExtra("flag", flag);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_pwd_status);
        baseLayout.setTitle("设置成功");
    }


    @Override
    public void setListener() {
        super.setListener();
    }

    @OnClick(R.id.tv_back)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if ("transaction".equals(flag)) {
            BaseApplication.getInstance().finishActivity(IdentifyActivity.class);
            BaseApplication.getInstance().finishActivity(TransactionPwdActivity.class);
            BaseApplication.getInstance().finishActivity(RequestPasswordActivity.class);
        }else{
            BaseApplication.getInstance().finishActivity(ActivationStatusActivity.class);
        }
        finish();
    }

    @Override
    public void setData() {
        flag = getIntent().getStringExtra("flag");
    }
}
