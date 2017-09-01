package com.giveu.shoppingmall.index.view.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;


/**
 * Created by 513419 on 2017/8/30.
 * 商品介绍
 */

public class CommodityDetailFragment extends BaseFragment {
    private View view;
    private RadioGroup rgCommodity;
    private WebCommodityFragment introduceFragment;
    private WebCommodityFragment paramsFragment;
    private WebCommodityFragment afterSaleFragment;
    private FragmentManager fragmentManager;
    private boolean fromCommodityDetail;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_detail, null);
        baseLayout.setTitleBarAndStatusBar(false, false);
        rgCommodity = (RadioGroup) view.findViewById(R.id.rg_commodity);
        fragmentManager = getChildFragmentManager();
        showFragment(0);
        return view;
    }


    @Override
    public void initDataDelay() {

    }

    @Override
    protected void setListener() {
        rgCommodity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_introduce:
                        showFragment(0);
                        break;

                    case R.id.rb_params:
                        showFragment(1);
                        break;

                    case R.id.rb_after_sale:
                        showFragment(2);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    /**
     * 显示fragment
     *
     * @param index
     */
    private void showFragment(int index) {
        hideFagment();
        switch (index) {
            case 0:

                if (introduceFragment == null) {
                    introduceFragment = new WebCommodityFragment();
                    introduceFragment.setFromCommodityDetail(fromCommodityDetail);
                    if (!introduceFragment.isAdded()) {
                        fragmentManager.beginTransaction().add(R.id.mContainer, introduceFragment).commitAllowingStateLoss();
                    }
                } else {
                    fragmentManager.beginTransaction().show(introduceFragment).commitAllowingStateLoss();
                }
                break;
            case 1:
                if (paramsFragment == null) {
                    paramsFragment = new WebCommodityFragment();
                    paramsFragment.setFromCommodityDetail(fromCommodityDetail);
                    if (!paramsFragment.isAdded()) {
                        fragmentManager.beginTransaction().add(R.id.mContainer, paramsFragment).commitAllowingStateLoss();
                    }
                } else {
                    fragmentManager.beginTransaction().show(paramsFragment).commitAllowingStateLoss();
                }
                break;
            case 2:
                if (afterSaleFragment == null) {
                    afterSaleFragment = new WebCommodityFragment();
                    afterSaleFragment.setFromCommodityDetail(fromCommodityDetail);
                    if (!afterSaleFragment.isAdded()) {
                        fragmentManager.beginTransaction().add(R.id.mContainer, afterSaleFragment).commitAllowingStateLoss();
                    }
                } else {
                    fragmentManager.beginTransaction().show(afterSaleFragment).commitAllowingStateLoss();
                }
                break;
        }
    }

    /**
     * 隐藏fragment:
     */
    private void hideFagment() {
        if (introduceFragment != null && introduceFragment.isAdded()) {
            fragmentManager.beginTransaction().hide(introduceFragment).commitAllowingStateLoss();
        }
        if (paramsFragment != null && paramsFragment.isAdded()) {
            fragmentManager.beginTransaction().hide(paramsFragment).commitAllowingStateLoss();
        }
        if (afterSaleFragment != null && afterSaleFragment.isAdded()) {
            fragmentManager.beginTransaction().hide(afterSaleFragment).commitAllowingStateLoss();
        }
    }

    public void setFromCommodityDetail(boolean fromCommodityDetail) {
        this.fromCommodityDetail = fromCommodityDetail;
    }
}
