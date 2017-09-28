package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.RepaymentPresenter;
import com.giveu.shoppingmall.me.view.agent.IRepaymentView;
import com.giveu.shoppingmall.me.view.fragment.RepaymentFragment;
import com.giveu.shoppingmall.model.bean.response.RepaymentResponse;
import com.giveu.shoppingmall.model.bean.response.RepaymentBean;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.widget.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by 513419 on 2017/6/22.
 */

public class RepaymentActivity extends BaseActivity implements IRepaymentView {
    @BindView(R.id.vp_bill)
    NoScrollViewPager vpBill;
    private RepaymentFragment currentMonthFragment;
    private RepaymentFragment nextMonthFragment;
    private RepaymentFragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> fragmentList;
    private RepaymentPresenter presenter;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, RepaymentActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 微信支付完成，因为launch模式为singleTask，所以不会走onCreate方法，直接onNewIntent
     *
     * @param mContext
     */
    public static void startItAfterPay(Context mContext) {
        Intent i = new Intent(mContext, RepaymentActivity.class);
        mContext.startActivity(i);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bill_list);
        baseLayout.setTitle("还款");
        baseLayout.setRightTextColor(R.color.color_00bbc0);
        baseLayout.setRightTextAndListener("交易查询", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionSearchActivity.startIt(mBaseContext);
            }
        });
        fragmentList = new ArrayList<>();
        currentMonthFragment = new RepaymentFragment();
        //传递参数,以区分当月还是下月
        Bundle currentBundle = new Bundle();
        currentBundle.putBoolean("isCurrentMonth", true);
        Bundle nextBundle = new Bundle();
        nextBundle.putBoolean("isCurrentMonth", false);
        currentMonthFragment.setArguments(currentBundle);
        nextMonthFragment = new RepaymentFragment();
        nextMonthFragment.setArguments(nextBundle);
        fragmentList.add(currentMonthFragment);
        fragmentList.add(nextMonthFragment);
        fragmentAdapter = new RepaymentFragmentAdapter(getSupportFragmentManager(), fragmentList);
        vpBill.setAdapter(fragmentAdapter);
        presenter = new RepaymentPresenter(this);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {
        //调一次接口可获取当月和下月的数据，并进行拆分
        presenter.getRepayment(LoginHelper.getInstance().getIdPerson());
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    public void setCurrentItem(int currentItem) {
        vpBill.setCurrentItem(currentItem, false);
    }

    @Override
    public void showRepayment(RepaymentResponse.HeaderBean headerBean, ArrayList<RepaymentBean> currentMonthList, ArrayList<RepaymentBean> nextMonthList) {
        //获取账单列表后刷新fragment数据
        currentMonthFragment.notifyDataSetChange(headerBean, currentMonthList);
        nextMonthFragment.notifyDataSetChange(headerBean, nextMonthList);
    }

    @Override
    public void showEmpty() {
        currentMonthFragment.initView();
        nextMonthFragment.initView();
    }


    private class RepaymentFragmentAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragments;

        public RepaymentFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (currentMonthFragment != null) {
            currentMonthFragment.payQuery();
        }
        if (nextMonthFragment != null) {
            nextMonthFragment.payQuery();
        }
    }
}
