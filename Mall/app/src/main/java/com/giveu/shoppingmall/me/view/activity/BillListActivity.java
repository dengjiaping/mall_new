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
import com.giveu.shoppingmall.me.view.dialog.RepaymentDetailDialog;
import com.giveu.shoppingmall.me.view.dialog.RepaymentDialog;
import com.giveu.shoppingmall.me.view.fragment.BillFragment;
import com.giveu.shoppingmall.widget.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 513419 on 2017/6/22.
 */

public class BillListActivity extends BaseActivity {
    @BindView(R.id.vp_bill)
    NoScrollViewPager vpBill;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    private BillFragment currentMonthFragment;
    private BillFragment nextMonthFragment;
    private BillFragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> fragmentList;
    private RepaymentDialog repaymentDialog;
    private int payMoney;
    private RepaymentDetailDialog repaymentDetailDialog;

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
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        super.setListener();
        repaymentDialog.setOnConfirmListener(new RepaymentDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String money) {
                tvMoney.setText(String.format(tvMoney.getText().toString(), money));
                payMoney = Integer.parseInt(money);
            }
        });

    }

    @OnClick({R.id.iv_change_money, R.id.tv_confirm})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_change_money:
                repaymentDialog.show();
                break;

            case R.id.tv_confirm:
                repaymentDetailDialog.show();
                break;

            default:
                break;
        }
    }

    public void setCurrentItem(int currentItem) {
        vpBill.setCurrentItem(currentItem, false);
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
