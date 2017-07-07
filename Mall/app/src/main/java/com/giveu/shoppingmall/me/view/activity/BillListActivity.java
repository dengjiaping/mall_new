package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.BillListPresenter;
import com.giveu.shoppingmall.me.view.agent.IBillIistView;
import com.giveu.shoppingmall.me.view.fragment.BillFragment;
import com.giveu.shoppingmall.model.bean.response.BillBean;
import com.giveu.shoppingmall.model.bean.response.BillListResponse;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.widget.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by 513419 on 2017/6/22.
 */

public class BillListActivity extends BaseActivity implements IBillIistView {
    @BindView(R.id.vp_bill)
    NoScrollViewPager vpBill;
    private BillFragment currentMonthFragment;
    private BillFragment nextMonthFragment;
    private BillFragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> fragmentList;
    private BillListPresenter presenter;

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
        presenter = new BillListPresenter(this);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {
        //调一次接口可获取当月和下月的数据，并进行拆分
        presenter.getBillList(LoginHelper.getInstance().getIdPerson());
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    public void setCurrentItem(int currentItem) {
        vpBill.setCurrentItem(currentItem, false);
    }

    @Override
    public void showBillList(BillListResponse.HeaderBean headerBean, ArrayList<BillBean> currentMonthList, ArrayList<BillBean> nextMonthList) {
        //获取账单列表后刷新fragment数据
        currentMonthFragment.notifyDataSetChange(headerBean, currentMonthList);
        nextMonthFragment.notifyDataSetChange(headerBean, nextMonthList);
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
