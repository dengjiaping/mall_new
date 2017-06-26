package com.giveu.shoppingmall.me.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.me.view.activity.BillListActivity;
import com.giveu.shoppingmall.me.view.activity.ContactUsActivity;
import com.giveu.shoppingmall.me.view.activity.LoginActivity;
import com.giveu.shoppingmall.me.view.activity.QuotaActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 我的模块
 * Created by 508632 on 2016/12/13.
 */


public class MainMeFragment extends BaseFragment {

    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.ll_quota)
    LinearLayout llQuota;
    @BindView(R.id.ll_bill)
    LinearLayout llBill;
    @BindView(R.id.ll_account_manage)
    LinearLayout llAcountManage;
    @BindView(R.id.ll_help)
    LinearLayout llHelp;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_main_me, null);
        baseLayout.setTitle("个人中心");
        baseLayout.hideBack();
        baseLayout.setBlueWhiteStyle();
        baseLayout.setRightImageAndListener(R.drawable.ic_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected boolean translateStatusBar() {
        return true;
    }

    @Override
    public void initWithDataDelay() {

    }

    @OnClick({R.id.tv_login, R.id.ll_bill, R.id.ll_help, R.id.ll_account_manage,R.id.ll_quota})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_login:
                LoginActivity.startIt(mBaseContext);
                break;

            case R.id.ll_bill:
                BillListActivity.startIt(mBaseContext);
                break;

            case R.id.ll_help:
                ContactUsActivity.startIt(mBaseContext);
                break;

            case R.id.ll_account_manage:
                break;

            case R.id.ll_quota:
                QuotaActivity.startIt(mBaseContext);
                break;

            default:
                break;

        }
    }
}
