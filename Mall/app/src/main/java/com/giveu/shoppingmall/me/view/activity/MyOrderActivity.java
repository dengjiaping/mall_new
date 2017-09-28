package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

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
    private String[] tabsText = {"全部", "待付款", "待收货", "已完成", "已关闭"};
    private OrderListFragment allFragment;
    private OrderListFragment waitingPayFragment;
    private OrderListFragment downPaymentFragment;
    private OrderListFragment waitingReceiveFragment;
    private OrderListFragment finishedFragment;
    private OrderListFragment closedFragment;
    private ArrayList<Fragment> fragments;



    public static void startIt(Activity activity, String orderState) {
        Intent intent = new Intent(activity, MyOrderActivity.class);
        intent.putExtra(OrderState.ORDER_TYPE, orderState);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order);
        baseLayout.setTitle("我的订单");
    }

    @Override
    public void setData() {
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                initFragment();
                mTabLayout.setVisibility(View.VISIBLE);
                return false; //false 表示只监听一次IDLE事件,之后就不会再执行这个函数了.
            }
        });
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        currentTab = Integer.parseInt(getIntent().getStringExtra(OrderState.ORDER_TYPE));

        //全部订单
        allFragment = new OrderListFragment();
        Bundle allBundle = new Bundle();
        allBundle.putInt(OrderState.ORDER_TYPE, OrderState.ALLRESPONSE);
        allFragment.setArguments(allBundle);
        fragments.add(allFragment);

        //待付款
        waitingPayFragment = new OrderListFragment();
        Bundle waitingPayBundle = new Bundle();
        waitingPayBundle.putInt(OrderState.ORDER_TYPE, OrderState.WAITINGPAY);
        waitingPayFragment.setArguments(waitingPayBundle);
        fragments.add(waitingPayFragment);

        /*//待首付
        downPaymentFragment = new OrderListFragment();
        Bundle downPaymentBundle = new Bundle();
        downPaymentBundle.putInt(OrderState.ORDER_TYPE, OrderState.DOWNPAYMENT);
        downPaymentFragment.setArguments(downPaymentBundle);
        fragments.add(downPaymentFragment);*/

        //待收货
        waitingReceiveFragment = new OrderListFragment();
        Bundle waitingReceiveBundle = new Bundle();
        waitingReceiveBundle.putInt(OrderState.ORDER_TYPE, OrderState.WAITINGRECEIVE);
        waitingReceiveFragment.setArguments(waitingReceiveBundle);
        fragments.add(waitingReceiveFragment);

        //已完成
        finishedFragment = new OrderListFragment();
        Bundle finishedBundle = new Bundle();
        finishedBundle.putInt(OrderState.ORDER_TYPE, OrderState.FINISHED);
        finishedFragment.setArguments(finishedBundle);
        fragments.add(finishedFragment);

        //已关闭
        closedFragment = new OrderListFragment();
        Bundle closedBundle = new Bundle();
        closedBundle.putInt(OrderState.ORDER_TYPE, OrderState.CLOSED);
        closedFragment.setArguments(closedBundle);
        fragments.add(closedFragment);

        adapter = new OrderFragmentAdapter(getSupportFragmentManager(), fragments, tabsText);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.color_00bbc0));
        //显示特定的tab，viewpager和tablayout需同步
        mViewPager.setCurrentItem(currentTab);
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
