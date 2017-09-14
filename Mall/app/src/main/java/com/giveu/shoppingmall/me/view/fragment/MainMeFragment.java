package com.giveu.shoppingmall.me.view.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.index.view.activity.WalletActivationFirstActivity;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.view.activity.AccountManagementActivity;
import com.giveu.shoppingmall.me.view.activity.CollectionActivity;
import com.giveu.shoppingmall.me.view.activity.ContactUsActivity;
import com.giveu.shoppingmall.me.view.activity.MyCouponActivity;
import com.giveu.shoppingmall.me.view.activity.MyOrderActivity;
import com.giveu.shoppingmall.me.view.activity.QuotaActivity;
import com.giveu.shoppingmall.me.view.activity.RepaymentActivity;
import com.giveu.shoppingmall.me.view.dialog.NotActiveDialog;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.NetWorkUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshScrollView;

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
    @BindView(R.id.ll_my_coupon)
    LinearLayout llMyCoupon;
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
    NotActiveDialog notActiveDialog;//未开通钱包的弹窗
    @BindView(R.id.view_divider)
    View viewDivider;
    @BindView(R.id.ll_my_collection)
    LinearLayout llMyCollection;
    @BindView(R.id.ll_order_module)
    LinearLayout llOrderModule;
    @BindView(R.id.tv_waiting_pay)
    TextView tvWaitingPay;
    @BindView(R.id.tv_down_payment)
    TextView tvDownPayment;
    @BindView(R.id.tv_waiting_receive)
    TextView tvWaitingReceive;
    @BindView(R.id.ptrsv)
    PullToRefreshScrollView ptrsv;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_main_me, null);
        baseLayout.setTitle("个人中心");
        baseLayout.hideBack();
        baseLayout.setBlueWhiteStyle();
        baseLayout.setTopBarBgDrawble(R.color.color_00c9cd);
        notActiveDialog = new NotActiveDialog(mBaseContext);
//        baseLayout.setRightImageAndListener(R.drawable.ic_message, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MessageActivity.startIt(mBaseContext);
//            }
//        });
        registerEventBus();
        ButterKnife.bind(this, view);
        ptrsv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ptrsv.setScrollingWhileRefreshingEnabled(true);
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
            //用户有钱包资质
            if (LoginHelper.getInstance().hasQualifications()) {
                tvStatus.setVisibility(View.GONE);
                tvLogin.setText(LoginHelper.getInstance().getName());
                tvWithdrawals.setText("可用额度" + StringUtils.format2(LoginHelper.getInstance().getTotalCost()) + "元");
                tvSee.setVisibility(View.VISIBLE);
                String remainDays = LoginHelper.getInstance().getRemainDays();
                if (StringUtils.isNotNull(remainDays)) {
                    if (StringUtils.string2Int(remainDays) < 0) {
                        //剩余天数为负数
                        tvDays.setText("已逾期");
                    } else {
                        SpannableString msp = new SpannableString("剩余" + remainDays + "天");
                        msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mBaseContext, R.color.color_4a4a4a)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mBaseContext, R.color.color_00adb2)), 2, remainDays.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mBaseContext, R.color.color_4a4a4a)), remainDays.length() + 2, remainDays.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvDays.setText(msp);
                    }
                }
            } else {
                tvLogin.setText(LoginHelper.getInstance().getUserName());
                tvWithdrawals.setText("查看信用钱包额度");
                tvStatus.setVisibility(View.VISIBLE);
                tvSee.setVisibility(View.GONE);
                tvDays.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_4a4a4a));
                tvDays.setText("- -");

            }
            tvWaittingPay.setText(StringUtils.format2(LoginHelper.getInstance().getRepayAmount()));
            tvPayAmounts.setText(LoginHelper.getInstance().getCreditCount());
            viewDivider.setVisibility(View.VISIBLE);
            llPayStatus.setVisibility(View.VISIBLE);
            //订单数量显示个数处理逻辑
            if (LoginHelper.getInstance().getOrderDownpaymentNum() > 0) {
                tvDownPayment.setVisibility(View.VISIBLE);
                tvDownPayment.setText(LoginHelper.getInstance().getOrderDownpaymentNum() + "");
            } else {
                tvDownPayment.setVisibility(View.GONE);
            }
            if (LoginHelper.getInstance().getOrderPayNum() > 0) {
                tvWaitingPay.setVisibility(View.VISIBLE);
                tvWaitingPay.setText(LoginHelper.getInstance().getOrderPayNum() + "");
            } else {
                tvWaitingPay.setVisibility(View.GONE);
            }
            if (LoginHelper.getInstance().getOrderReceiveNum() > 0) {
                tvWaitingReceive.setVisibility(View.VISIBLE);
                tvWaitingReceive.setText(LoginHelper.getInstance().getOrderReceiveNum() + "");
            } else {
                tvWaitingReceive.setVisibility(View.GONE);
            }
        } else {
            //未登录状态
            tvWaitingPay.setVisibility(View.GONE);
            tvDownPayment.setVisibility(View.GONE);
            tvWaitingReceive.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
            tvWithdrawals.setText("查看信用钱包额度");
            tvLogin.setText("立即登录");
            tvSee.setVisibility(View.GONE);
            ivAvatar.setImageResource(R.drawable.ic_default_avatar);
            tvDays.setText("--");
            viewDivider.setVisibility(View.VISIBLE);
            llPayStatus.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(LoginResponse response) {
        //调用个人信息接口后，收到通知，更新个人信息的显示
        updateUserUi();
        ptrsv.onRefreshComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void stopRefresh(BaseBean response) {
        //调用个人信息接口失败后，收到通知，完成刷新
        ptrsv.onRefreshComplete();
        CommonLoadingView.showErrorToast(response);
    }

    @Override
    protected void setListener() {
        ptrsv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (LoginHelper.getInstance().loginPersonInfo != null) {
                    BaseApplication.getInstance().fetchUserInfo();
                    if (!NetWorkUtils.isNetWorkConnected()) {
                        ptrsv.onRefreshComplete();
                    }
                } else {
                    ptrsv.onRefreshComplete();
                }
            }
        });

    }

    @Override
    protected boolean translateStatusBar() {
        return true;
    }

    @Override
    public void initDataDelay() {

    }


    @OnClick({R.id.iv_avatar, R.id.tv_status, R.id.tv_login, R.id.ll_bill, R.id.ll_help, R.id.ll_account_manage, R.id.ll_my_coupon, R.id.ll_quota, R.id.ll_my_collection, R.id.ll_my_order, R.id.ll_waiting_pay, R.id.ll_waiting_receive, R.id.ll_down_payment})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_login:
            case R.id.iv_avatar:
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    AccountManagementActivity.startIt(mBaseContext);
                }
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
                        notActiveDialog.showDialog();
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
            case R.id.ll_my_collection:
                //我的收藏
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    if (LoginHelper.getInstance().hasQualifications()) {
                        CollectionActivity.startIt(mBaseContext);
                    } else {
                        notActiveDialog.showDialog();
                    }
                }
                break;
            case R.id.ll_my_coupon:
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    if (LoginHelper.getInstance().hasQualifications()) {
                        MyCouponActivity.startIt(mBaseContext);
                    } else {
                        notActiveDialog.showDialog();
                    }
                }
                break;

            case R.id.ll_my_order:
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext))
                    MyOrderActivity.startIt(mBaseContext, OrderState.ALL_RESPONSE);
                break;

            case R.id.ll_quota:
                //激活钱包用户直接查看额度，否则先去激活
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    if (LoginHelper.getInstance().hasQualifications()) {
                        QuotaActivity.startIt(mBaseContext);
                    } else {
                        notActiveDialog.showDialog();
                    }
                }
                break;

            case R.id.ll_waiting_pay:
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    MyOrderActivity.startIt(mBaseContext, OrderState.WAITING_PAY);
                }
                break;

            case R.id.ll_down_payment:
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    MyOrderActivity.startIt(mBaseContext, OrderState.DOWN_PAYMENT);
                }
                break;

            case R.id.ll_waiting_receive:
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    MyOrderActivity.startIt(mBaseContext, OrderState.WAITING_RECEIVE);
                }
                break;

            default:
                break;

        }
    }
}
