package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.widget.IosSwitch;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 安全中心
 * Created by 101900 on 2017/6/29.
 */

public class SecurityCenterActivity extends BaseActivity {

    @BindView(R.id.ll_change_phone_number)
    LinearLayout llChangePhoneNumber;
    @BindView(R.id.ll_change_login_pwd)
    LinearLayout llChangeLoginPwd;
    @BindView(R.id.ll_change_transaction_pwd)
    LinearLayout llChangeTransactionPwd;
    @BindView(R.id.switch_gesture)
    IosSwitch switchGesture;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, SecurityCenterActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_security_center);
        baseLayout.setTitle("安全中心");
    }

    @Override
    public void setData() {

    }

    @OnClick({R.id.ll_change_phone_number, R.id.ll_change_login_pwd, R.id.ll_change_transaction_pwd, R.id.switch_gesture})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_change_phone_number:
                //修改手机号
                if (true) {
                    //有资质的
                    TransactionInputActivity.startIt(mBaseContext);
                } else {
                    //没有资质的
                    ChangePhoneNumberActivity.startIt(mBaseContext);
                }
                break;
            case R.id.ll_change_login_pwd:
                //修改登录密码

                break;
            case R.id.ll_change_transaction_pwd:
                //修改交易密码
                break;
            case R.id.switch_gesture:
                //手势与指纹
                break;
        }
    }

}
