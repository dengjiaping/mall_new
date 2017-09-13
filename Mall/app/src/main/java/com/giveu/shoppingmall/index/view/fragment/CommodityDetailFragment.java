package com.giveu.shoppingmall.index.view.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.model.bean.response.CommodityDetailResponse;


/**
 * Created by 513419 on 2017/8/30.
 * 商品介绍
 */

public class CommodityDetailFragment extends BaseFragment{
    private View view;
    private RadioGroup rgCommodity;
    private WebCommodityFragment introduceFragment;
    private WebCommodityFragment paramsFragment;
    private FragmentManager fragmentManager;
    private boolean fromCommodityDetail;
    private CommodityDetailResponse data;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_detail, null);
        baseLayout.setTitleBarAndStatusBar(false, false);
        rgCommodity = (RadioGroup) view.findViewById(R.id.rg_commodity);
        fragmentManager = getChildFragmentManager();
        introduceFragment = new WebCommodityFragment();
        introduceFragment.setFromCommodityDetail(fromCommodityDetail);
        if (data != null) {
//            introduceFragment.setHtmlStr(data.intruction);
        }
        if (!introduceFragment.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.mContainer, introduceFragment).commitAllowingStateLoss();
        }
//        showFragment(0);
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
    public void showFragment(int index) {
        hideFagment();
        switch (index) {
            case 0:

                if (introduceFragment == null) {
                    introduceFragment = new WebCommodityFragment();
                    introduceFragment.setFromCommodityDetail(fromCommodityDetail);
                    if (data != null) {
//                        introduceFragment.setHtmlStr(data.intruction);
                    }
                    if (!introduceFragment.isAdded()) {
                        fragmentManager.beginTransaction().add(R.id.mContainer, introduceFragment).commitAllowingStateLoss();
                    }
                } else {
                    fragmentManager.beginTransaction().show(introduceFragment).commitAllowingStateLoss();
                }
                break;
            case 1:
          /*      if (paramsFragment == null) {
                    paramsFragment = new WebCommodityFragment();
                    paramsFragment.setFromCommodityDetail(fromCommodityDetail);
                    if (data != null) {
                        paramsFragment.setHtmlStr(data.param);
                    }

                    if (!paramsFragment.isAdded()) {
                        fragmentManager.beginTransaction().add(R.id.mContainer, paramsFragment).commitAllowingStateLoss();
                    }
                } else {
                    fragmentManager.beginTransaction().show(paramsFragment).commitAllowingStateLoss();
                }*/
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
    }

    public void setFromCommodityDetail(boolean fromCommodityDetail) {
        this.fromCommodityDetail = fromCommodityDetail;
    }

    public void refreshCommodityDetail(String url) {
//        this.data = url;
        if (introduceFragment != null) {
//            introduceFragment.setHtmlStr(data.intruction);
            if (introduceFragment.isAdded()) {
                introduceFragment.loadHtml(url);
            }
        }
     /*   if (paramsFragment != null) {
            paramsFragment.setHtmlStr(data.param);
            if (paramsFragment.isAdded()) {
                paramsFragment.loadHtml(data.param);
            }
        }*/
    }
}
