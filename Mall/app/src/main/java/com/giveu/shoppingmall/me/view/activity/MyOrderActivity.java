package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.view.fragment.OrderListFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by 101912 on 2017/8/25.
 */

public class MyOrderActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    private int currentTab;//当前tablayout需显示的tab

    private OrderFragmentAdapter adapter;
    private String[] tabsText = {"全部", "待付款", "待首付", "待收货", "已完成", "已关闭"};
    private OrderListFragment allFragment;
    private OrderListFragment waitingPayFragment;
    private OrderListFragment downPaymentFragment;
    private OrderListFragment waitingReceiveFragment;
    private OrderListFragment finishedFragment;
    private OrderListFragment closedFragment;
    private ArrayList<Fragment> fragments;



    public static void startIt(Activity activity, String orderState) {
        Intent intent = new Intent(activity, MyOrderActivity.class);
        intent.putExtra("orderState", orderState);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order);
        baseLayout.setTitle("我的订单");
    }

    @Override
    public void setData() {
        fragments = new ArrayList<>();
        currentTab = Integer.parseInt(getIntent().getStringExtra("orderState"));

        allFragment = new OrderListFragment();
        Bundle allBundle = new Bundle();
        allBundle.putString("orderState", OrderState.ALL_RESPONSE);
        allFragment.setArguments(allBundle);
        fragments.add(allFragment);

        waitingPayFragment = new OrderListFragment();
        Bundle waitingPayBundle = new Bundle();
        waitingPayBundle.putString("orderState", OrderState.WAITING_PAY);
        waitingPayFragment.setArguments(waitingPayBundle);
        fragments.add(waitingPayFragment);

        downPaymentFragment = new OrderListFragment();
        Bundle downPaymentBundle = new Bundle();
        downPaymentBundle.putString("orderState", OrderState.DOWN_PAYMENT);
        downPaymentFragment.setArguments(downPaymentBundle);
        fragments.add(downPaymentFragment);

        waitingReceiveFragment = new OrderListFragment();
        Bundle waitingReceiveBundle = new Bundle();
        waitingReceiveBundle.putString("orderState", OrderState.WAITING_RECEIVE);
        waitingReceiveFragment.setArguments(waitingReceiveBundle);
        fragments.add(waitingReceiveFragment);

        finishedFragment = new OrderListFragment();
        Bundle finishedBundle = new Bundle();
        finishedBundle.putString("orderState", OrderState.Finished_RESPONSE);
        finishedFragment.setArguments(finishedBundle);
        fragments.add(finishedFragment);

        closedFragment = new OrderListFragment();
        Bundle closedBundle = new Bundle();
        closedBundle.putString("orderState", OrderState.CLOSED_RESPONSE);
        closedFragment.setArguments(closedBundle);
        fragments.add(closedFragment);

        adapter = new OrderFragmentAdapter(getSupportFragmentManager(), fragments, tabsText);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(currentTab);
        mTabLayout.getTabAt(currentTab).select();
    }

    private class OrderFragmentAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments;
        String[] tabsText;

        public OrderFragmentAdapter(FragmentManager manager, ArrayList<Fragment> fragments, String[] tabsText) {
            super(manager);
            this.fragments = fragments;
            this.tabsText = tabsText;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabsText[position];
        }
    }




}
