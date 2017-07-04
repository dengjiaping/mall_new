package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.BillListPresenter;
import com.giveu.shoppingmall.me.view.agent.IBillIistView;
import com.giveu.shoppingmall.me.view.dialog.RepaymentDetailDialog;
import com.giveu.shoppingmall.me.view.dialog.RepaymentDialog;
import com.giveu.shoppingmall.me.view.fragment.BillFragment;
import com.giveu.shoppingmall.model.bean.response.BillBean;
import com.giveu.shoppingmall.model.bean.response.BillListResponse;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.NoScrollViewPager;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 513419 on 2017/6/22.
 */

public class BillListActivity extends BaseActivity implements IBillIistView {
    @BindView(R.id.vp_bill)
    NoScrollViewPager vpBill;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_confirm)
    ClickEnabledTextView tvConfirm;
    private BillFragment currentMonthFragment;
    private BillFragment nextMonthFragment;
    private BillFragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> fragmentList;
    private RepaymentDialog repaymentDialog;
    private double payMoney;
    private RepaymentDetailDialog repaymentDetailDialog;
    private BillListPresenter presenter;
    private DecimalFormat format = new DecimalFormat("0.00");

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, BillListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bill_list);
        baseLayout.setTitle("账单");
        baseLayout.setRightTextColor(R.color.color_00bbc0);
        baseLayout.setRightTextAndListener("交易查询", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionSearchActivity.startIt(mBaseContext);
            }
        });
        fragmentList = new ArrayList<>();
        currentMonthFragment = new BillFragment();
        //传递参数
        Bundle currentBundle = new Bundle();
        currentBundle.putBoolean("isCurrentMonth", true);
        Bundle nextBundle = new Bundle();
        nextBundle.putBoolean("isCurrentMonth", false);
        currentMonthFragment.setArguments(currentBundle);
        nextMonthFragment = new BillFragment();
        nextMonthFragment.setArguments(nextBundle);
        fragmentList.add(currentMonthFragment);
        fragmentList.add(nextMonthFragment);
        fragmentAdapter = new BillFragmentAdapter(getSupportFragmentManager(), fragmentList);
        vpBill.setAdapter(fragmentAdapter);
        repaymentDialog = new RepaymentDialog(mBaseContext);
        repaymentDetailDialog = new RepaymentDetailDialog(mBaseContext);
        presenter = new BillListPresenter(this);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {
        //调一次接口可获取当月和下月的数据，并进行拆分
        // TODO: 2017/7/3 写死的数据需更换
        presenter.getBillList("15124638");
    }

    @Override
    public void setListener() {
        super.setListener();
        repaymentDialog.setOnConfirmListener(new RepaymentDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String money) {
                tvMoney.setText("还款金额：¥" + format.format(Double.parseDouble(money)));
                payMoney = Double.parseDouble(money);
                if (vpBill.getCurrentItem() == 0) {
                    //更新fragment的还款金额，以便下次切换时显示的是已更新的数据
                    currentMonthFragment.setPayMoney(payMoney);
                } else if (vpBill.getCurrentItem() == 1) {
                    nextMonthFragment.setPayMoney(payMoney);
                }
                canClick();
            }

        });

    }

    @OnClick({R.id.iv_change_money, R.id.tv_confirm})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_change_money:
                if (vpBill.getCurrentItem() == 0) {
                    if (currentMonthFragment.getPayMoney() <= 0) {
                        ToastUtils.showShortToast("请先勾选要还的款项");
                    } else {
                        repaymentDialog.show();
                    }
                } else if (vpBill.getCurrentItem() == 1) {
                    if (nextMonthFragment.getPayMoney() <= 0) {
                        ToastUtils.showShortToast("请先勾选要还的款项");
                    } else {
                        repaymentDialog.show();
                    }
                }
                break;

            case R.id.tv_confirm:
                if (canClick()) {
                    repaymentDetailDialog.show();
                } else {
                    ToastUtils.showShortToast("请先勾选要还的款项");
                }
                break;

            default:
                break;
        }
    }

    public void setCurrentItem(int currentItem) {
        //来回切换时需重新设置该列表下还款金额
        if (currentItem == 0) {
            tvMoney.setText("还款金额：¥" + format.format(currentMonthFragment.getPayMoney()));
            payMoney = currentMonthFragment.getPayMoney();
            canClick();

        } else if (currentItem == 1) {
            tvMoney.setText("还款金额：¥" + format.format(nextMonthFragment.getPayMoney()));
            payMoney = nextMonthFragment.getPayMoney();
            canClick();
        }
        vpBill.setCurrentItem(currentItem, false);
    }

    public boolean canClick() {
        if (payMoney <= 0) {
            tvConfirm.setClickEnabled(false);
            return false;
        } else {
            tvConfirm.setClickEnabled(true);
            return true;
        }
    }

    @Override
    public void showBillList(BillListResponse.HeaderBean headerBean, ArrayList<BillBean> currentMonthList, ArrayList<BillBean> nextMonthList) {
        //获取账单列表后刷新fragment数据
        currentMonthFragment.notifyDataSetChange(headerBean, currentMonthList);
        nextMonthFragment.notifyDataSetChange(headerBean, nextMonthList);
    }

    public void setPayMoney(double payMoney) {
        tvMoney.setText("还款金额：¥" + format.format(payMoney));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private class BillFragmentAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragments;

        public BillFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
