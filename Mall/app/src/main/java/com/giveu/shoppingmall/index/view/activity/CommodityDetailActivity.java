package com.giveu.shoppingmall.index.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.adapter.CommodityFragmentAdapter;
import com.giveu.shoppingmall.index.view.fragment.CommodityDetailFragment;
import com.giveu.shoppingmall.index.view.fragment.CommodityInfoFragment;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.giveu.shoppingmall.R.id.vp_content;

/**
 * Created by 513419 on 2017/8/30.
 * 商品详情
 */

public class CommodityDetailActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(vp_content)
    public NoScrollViewPager vpContent;
    @BindView(R.id.ll_credit)
    LinearLayout llCredit;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.view_divider)
    View viewDivider;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    private List<Fragment> fragmentList = new ArrayList<>();
    private CommodityInfoFragment commodityInfoFragment;
    private CommodityDetailFragment commodityDetailFragment;
    private String[] tabTitles = new String[]{"商品", "详情"};
    private boolean isCredit;//是否分期产品
    private String skuCode;

    public static void startIt(Context context, boolean isCredit, String skuCode) {
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        intent.putExtra("isCredit", isCredit);
        intent.putExtra("skuCode", skuCode);
        context.startActivity(intent);
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_commodity_detail);
        baseLayout.setTitleBarAndStatusBar(false, false);
        isCredit = getIntent().getBooleanExtra("isCredit", false);
        skuCode = getIntent().getStringExtra("skuCode");
        //商品介绍
        commodityInfoFragment = new CommodityInfoFragment();
        Bundle infoBundle = new Bundle();
        infoBundle.putBoolean("isCredit", isCredit);
        infoBundle.putString("skuCode", skuCode);
        commodityInfoFragment.setArguments(infoBundle);
        //商品详情
        commodityDetailFragment = new CommodityDetailFragment();
        Bundle detailBundle = new Bundle();
        detailBundle.putString("skuCode", skuCode);
        commodityDetailFragment.setArguments(detailBundle);
        //为了解决滑动冲突，在商品详情页时（是左右滑动的详情页而不是上下滑动产生的详情页），webview左右滑动禁止，以便让viewpager能够滑动
        commodityDetailFragment.setFromCommodityDetail(true);
        fragmentList.add(commodityInfoFragment);
        fragmentList.add(commodityDetailFragment);
        tabLayout.setupWithViewPager(vpContent);
        //vpContent设置为可滑动
        vpContent.setScrollDisabled(false);
        vpContent.setAdapter(new CommodityFragmentAdapter(getSupportFragmentManager(), fragmentList, tabTitles));
        ivCollect.setTag(false);
        //是否分期产品，分期产品显示月供
        if (isCredit) {
            llCollect.getLayoutParams().width = DensityUtils.dip2px(57);
            llCredit.setVisibility(View.VISIBLE);
            viewDivider.setVisibility(View.VISIBLE);
        } else {
            llCollect.getLayoutParams().width = DensityUtils.dip2px(140);
            llCredit.setVisibility(View.GONE);
            viewDivider.setVisibility(View.GONE);
        }
    }

    @Override
    public void setData() {

    }

    /**
     * 收藏商品
     */
    private void collectCommodity() {
        setCollectStatus(1);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(ivCollect, "scaleY", 1f, 1.5f, 1f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(ivCollect, "scaleX", 1f, 1.5f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleXAnimator).with(scaleYAnimator);
        animSet.setDuration(500);
        animSet.start();
    }

    /**
     * 0 未收藏， 1已收藏
     * @param collectStatus
     */
    public void setCollectStatus(int collectStatus) {
        if(collectStatus == 1) {
            ivCollect.setImageResource(R.drawable.ic_collect_select);
            tvCollect.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_ff2a2a));
        }else {
            cancelCollect();
        }
    }

    /**
     * 取消收藏
     */
    private void cancelCollect() {
        tvCollect.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_9b9b9b));
        ivCollect.setImageResource(R.drawable.ic_collect_unselect);
    }


    @OnClick({R.id.tv_buy, R.id.ll_collect, R.id.ll_back})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_buy:
                commodityInfoFragment.showBuyDialog();
                break;
            case R.id.ll_collect:
                boolean isCheck = (boolean) ivCollect.getTag();
                //已收藏则取消收藏
                if (isCheck) {
                    cancelCollect();
                } else {
                    collectCommodity();
                }
                ivCollect.setTag(!isCheck);
                break;
        }
    }

    public void showCommodityDetail() {
        vpContent.setCurrentItem(1, false);
        commodityDetailFragment.showCommodityIntroduce();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
