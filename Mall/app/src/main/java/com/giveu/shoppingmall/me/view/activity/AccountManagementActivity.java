package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.cash.view.activity.AddressManageActivity;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账号管理主页
 * Created by 101900 on 2017/6/27.
 */

public class AccountManagementActivity extends BaseActivity {

    @BindView(R.id.ll_delivery_address)
    LinearLayout llDeliveryAddress;
    @BindView(R.id.ll_bank_card)
    LinearLayout llBankCard;
    @BindView(R.id.ll_security_center)
    LinearLayout llSecurityCenter;
    @BindView(R.id.ll_version_update)
    LinearLayout llVersionUpdate;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, AccountManagementActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_management);
        baseLayout.setTitle("账号管理");
        tvPhone.setText(LoginHelper.getInstance().getPhone());
        //优先级别：真实姓名-用户名-手机号
        if (StringUtils.isNotNull(LoginHelper.getInstance().getName())) {
            tvUserName.setText(LoginHelper.getInstance().getName());
        } else {
            if (StringUtils.isNotNull(LoginHelper.getInstance().getUserName())) {
                tvUserName.setText(LoginHelper.getInstance().getUserName());
            } else {
                tvUserName.setText(LoginHelper.getInstance().getPhone());
            }
        }
    }

    @Override
    public void setData() {

    }

    @OnClick({R.id.ll_delivery_address, R.id.ll_bank_card, R.id.ll_security_center, R.id.ll_version_update, R.id.tv_finish})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_delivery_address:
                //地址管理
                AddressManageActivity.startIt(mBaseContext);
                break;
            case R.id.ll_bank_card:
                //我的银行卡
                MyBankCardActivity.startIt(mBaseContext,false);
                break;
            case R.id.ll_security_center:
                //安全中心
                SecurityCenterActivity.startIt(mBaseContext);
                break;
            case R.id.ll_version_update:
                //版本更新
                break;
            case R.id.tv_finish:
                //退出登录
                logout();
                break;
        }
    }

    private void logout() {
        CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
        customDialogUtil.getDialogMode1("提示", "是否要退出登录？", "确定", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginHelper.getInstance().logout();
                finish();
            }
        }, null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
