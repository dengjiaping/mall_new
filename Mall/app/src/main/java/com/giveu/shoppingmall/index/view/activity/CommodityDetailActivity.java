package com.giveu.shoppingmall.index.view.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.adapter.CommodityFragmentAdapter;
import com.giveu.shoppingmall.index.view.dialog.BuyCommodityDialog;
import com.giveu.shoppingmall.index.view.dialog.CreditCommodityDialog;
import com.giveu.shoppingmall.index.view.fragment.CommodityDetailFragment;
import com.giveu.shoppingmall.index.view.fragment.CommodityInfoFragment;
import com.giveu.shoppingmall.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 513419 on 2017/8/30.
 * 商品详情
 */

public class CommodityDetailActivity extends BaseActivity {
    public NoScrollViewPager vp_content;
    private List<Fragment> fragmentList = new ArrayList<>();
    private BuyCommodityDialog buyCommodityDialog;
    private TextView tvBuy;
    private TextView tvCommodityDetail;
    private TabLayout tabLayout;
    private CommodityInfoFragment commodityInfoFragment;
    private CommodityDetailFragment commodityDetailFragment;
    private String[] tabTitles = new String[]{"商品", "详情"};

    public static void startIt(Context context){
        Intent intent = new Intent(context,CommodityDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_commodity_detail);
        baseLayout.setTitleBarAndStatusBar(false,false);
        vp_content = (NoScrollViewPager) findViewById(R.id.vp_content);
        commodityInfoFragment = new CommodityInfoFragment();
        commodityDetailFragment = new CommodityDetailFragment();
        //为了解决滑动冲突，在商品详情页时（是左右滑动的详情页而不是上下滑动产生的详情页），webview左右滑动禁止，以便让viewpager能够滑动
        commodityDetailFragment.setFromCommodityDetail(true);
        fragmentList.add(commodityInfoFragment);
        fragmentList.add(commodityDetailFragment);
        tvBuy = (TextView) findViewById(R.id.tv_buy);
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyCommodityDialog.show();
            }
        });
        buyCommodityDialog = new BuyCommodityDialog(this, R.style.customerDialog);
        tvCommodityDetail = (TextView) findViewById(R.id.tv_commodity_detail);
        tvCommodityDetail.setAlpha(0);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vp_content);
        vp_content.setAdapter(new CommodityFragmentAdapter(getSupportFragmentManager(), fragmentList, tabTitles));
        CreditCommodityDialog creditCommodityDialog = new CreditCommodityDialog(mBaseContext);
        creditCommodityDialog.show();
    }

    @Override
    public void setData() {

    }

    /**
     * 透明度变化，坐标y变化
     *
     * @param targetView
     * @param beginY
     * @param endY
     * @param beginAlpha
     * @param endAlpha
     */
    public void startAnimation(View targetView, float beginY, float endY, float beginAlpha, float endAlpha) {
        PropertyValuesHolder translateYHolder = PropertyValuesHolder.ofFloat("translationY", beginY,
                endY);
        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", beginAlpha,
                endAlpha);
        ObjectAnimator.ofPropertyValuesHolder(targetView, translateYHolder, alphaHolder).setDuration(200).start();
    }


    public void showTabLayout() {
        startAnimation(tvCommodityDetail, 0, 200, 1, 0);
        startAnimation(tabLayout, -200, 0, 0, 1);
    }

    public void hideTabLayout() {
        startAnimation(tvCommodityDetail, 200, 0, 0, 1);
        startAnimation(tabLayout, 0, -200, 1, 0);
    }

    public void showCommodityDetail() {
        vp_content.setCurrentItem(1);
    }
}
