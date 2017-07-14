package com.giveu.shoppingmall.me.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.index.view.activity.WalletActivationFirstActivity;
import com.giveu.shoppingmall.me.view.activity.AccountManagementActivity;
import com.giveu.shoppingmall.me.view.activity.ContactUsActivity;
import com.giveu.shoppingmall.me.view.activity.LoginActivity;
import com.giveu.shoppingmall.me.view.activity.QuotaActivity;
import com.giveu.shoppingmall.me.view.activity.RepaymentActivity;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_withdrawals)
    TextView tvWithdrawals;
    @BindView(R.id.tv_waitting_pay)
    TextView tvWaittingPay;
    @BindView(R.id.tv_pay_amounts)
    TextView tvPayAmounts;
    @BindView(R.id.tv_days)
    TextView tvDays;
    @BindView(R.id.ll_pay_status)
    LinearLayout llPayStatus;
    @BindView(R.id.tv_see)
    TextView tvSee;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_main_me, null);
        baseLayout.setTitle("个人中心");
        baseLayout.hideBack();
        baseLayout.setBlueWhiteStyle();
       /* baseLayout.setRightImageAndListener(R.drawable.ic_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        registerEventBus();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserUi();
    }

    /**
     * 更新用户信息UI
     */
    private void updateUserUi() {
        //用户已登录
        if (LoginHelper.getInstance().hasLogin()) {
            if (StringUtils.isNotNull(LoginHelper.getInstance().getUserPic())) {
                ImageUtils.loadImageWithCorner(LoginHelper.getInstance().getUserPic(), R.drawable.ic_default_avatar, ivAvatar, DensityUtils.dip2px(25));
            }
            tvLogin.setEnabled(false);
            //用户有钱包资质
            if (LoginHelper.getInstance().hasQualifications()) {
                tvStatus.setVisibility(View.GONE);
                tvLogin.setText(LoginHelper.getInstance().getName());
                tvWithdrawals.setText("可用额度" + StringUtils.format2(LoginHelper.getInstance().getTotalCost()) + "元");
                tvSee.setVisibility(View.VISIBLE);
            } else {
                tvLogin.setText(LoginHelper.getInstance().getUserName());
                tvWithdrawals.setText("查看信用钱包额度");
                tvStatus.setVisibility(View.VISIBLE);
                tvSee.setVisibility(View.GONE);
            }
            tvWaittingPay.setText(StringUtils.format2(LoginHelper.getInstance().getRepayAmount()));
            tvPayAmounts.setText(LoginHelper.getInstance().getCreditCount());
            tvDays.setText(LoginHelper.getInstance().getRepayDate());
            llPayStatus.setVisibility(View.VISIBLE);
        } else {
            //未登录状态
            tvStatus.setVisibility(View.GONE);
            tvWithdrawals.setText("查看信用钱包额度");
            tvLogin.setText("立即登录");
            tvSee.setVisibility(View.GONE);
            ivAvatar.setImageResource(R.drawable.ic_default_avatar);
            tvLogin.setEnabled(true);
            llPayStatus.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(LoginResponse response) {
        //调用个人信息接口后，收到通知，更新个人信息的显示
        updateUserUi();
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected boolean translateStatusBar() {
        return true;
    }

    @Override
    public void initDataDelay() {

    }

    @OnClick({R.id.tv_status, R.id.tv_login, R.id.ll_bill, R.id.ll_help, R.id.ll_account_manage, R.id.ll_quota})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_login:
                LoginActivity.startIt(mBaseContext);
                break;

            case R.id.tv_status:
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    WalletActivationFirstActivity.startIt(mBaseContext);
                }
                break;

            case R.id.ll_bill:
                //登录并且有资质才可查看，否则先登录或先激活钱包
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    if (LoginHelper.getInstance().hasQualifications()) {
                        RepaymentActivity.startIt(mBaseContext);
                    } else {
                        WalletActivationFirstActivity.startIt(mBaseContext);
                    }
                }
                break;

            case R.id.ll_help:
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    ContactUsActivity.startIt(mBaseContext);
                }
                break;

            case R.id.ll_account_manage:
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    AccountManagementActivity.startIt(mBaseContext);
                }
                break;

            case R.id.ll_quota:
                //激活钱包用户直接查看额度，否则先去激活
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    if (LoginHelper.getInstance().hasQualifications()) {
                        QuotaActivity.startIt(mBaseContext);
                    } else {
                        WalletActivationFirstActivity.startIt(mBaseContext);
                    }
                }
                break;

            default:
                break;

        }
    }
}
