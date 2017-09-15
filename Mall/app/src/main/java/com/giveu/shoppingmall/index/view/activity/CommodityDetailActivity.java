package com.giveu.shoppingmall.index.view.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fastaccess.permission.base.PermissionHelper;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BasePermissionActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.adapter.CommodityFragmentAdapter;
import com.giveu.shoppingmall.index.presenter.CommodityPresenter;
import com.giveu.shoppingmall.index.view.agent.ICommodityView;
import com.giveu.shoppingmall.index.view.fragment.CommodityInfoFragment;
import com.giveu.shoppingmall.index.view.fragment.WebCommodityFragment;
import com.giveu.shoppingmall.model.bean.response.SkuIntroductionResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.giveu.shoppingmall.R.id.vp_content;

/**
 * Created by 513419 on 2017/8/30.
 * 商品详情
 */

public class CommodityDetailActivity extends BasePermissionActivity implements ICommodityView {
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
    @BindView(R.id.tv_monthAmount)
    TextView tvMonthAmount;
    @BindView(R.id.tv_commodity_detail)
    TextView tvCommodityDetail;
    @BindView(R.id.ll_choose_address)
    LinearLayout llChooseAddress;
    private List<Fragment> fragmentList = new ArrayList<>();
    private CommodityInfoFragment commodityInfoFragment;
    private WebCommodityFragment commodityDetailFragment;
    private String[] tabTitles = new String[]{"商品", "详情"};
    private boolean isCredit;//是否分期产品
    private String skuCode;
    private CommodityPresenter presenter;

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
        isCredit = false;
        skuCode = getIntent().getStringExtra("skuCode");
        //商品介绍
        commodityInfoFragment = new CommodityInfoFragment();
        Bundle infoBundle = new Bundle();
        infoBundle.putBoolean("isCredit", isCredit);
        infoBundle.putString("skuCode", skuCode);
        commodityInfoFragment.setArguments(infoBundle);
        tvCommodityDetail.setAlpha(0);
        //商品详情
        commodityDetailFragment = new WebCommodityFragment();
        Bundle detailBundle = new Bundle();
        detailBundle.putString("skuCode", skuCode);
        commodityDetailFragment.setArguments(detailBundle);
        //为了解决滑动冲突，在商品详情页时（是左右滑动的详情页而不是上下滑动产生的详情页），webview左右滑动禁止，以便让viewpager能够滑动
        commodityDetailFragment.setFromCommodityDetail(true);
        fragmentList.add(commodityInfoFragment);
        fragmentList.add(commodityDetailFragment);
        tabLayout.setupWithViewPager(vpContent);
        vpContent.setOffscreenPageLimit(2);
        //vpContent设置为可滑动
        vpContent.setScrollDisabled(false);
        vpContent.setAdapter(new CommodityFragmentAdapter(getSupportFragmentManager(), fragmentList, tabTitles));
        ivCollect.setTag(false);//设置tag标识是否已收藏
        //目前只有一次性产品
        isCredit = false;
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
        //接口返回是否有货前购买按钮都不能点击
        setBuyEnable(false);
        presenter = new CommodityPresenter(this);
//        presenter.getCommodityDetail(Const.CHANNEL, skuCode);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {

    }

    /**
     * 收藏商品
     */
    private void collectCommodity() {
        setCollectStatus(0);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(ivCollect, "scaleY", 1f, 1.5f, 1f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(ivCollect, "scaleX", 1f, 1.5f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleXAnimator).with(scaleYAnimator);
        animSet.setDuration(500);
        animSet.start();
    }

    /**
     * 0 未收藏， 1已收藏
     *
     * @param collectStatus
     */
    public void setCollectStatus(int collectStatus) {
        if (collectStatus == 0) {
            ivCollect.setTag(true);
            ivCollect.setImageResource(R.drawable.ic_collect_select);
            tvCollect.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_ff2a2a));
        } else {
            ivCollect.setTag(false);
            cancelCollect();
        }
    }

    /**
     * {@link CommodityInfoFragment#showSkuIntroduction(SkuIntroductionResponse)}}
     * CommodityInfoFragment切换商品参数后调用，刷新收藏状态并更新商品详情信息
     *
     * @param skuCode
     * @param collectStatus
     * @param monthAmount
     */
    public void initData(String skuCode, int collectStatus, String monthAmount) {
        setCollectStatus(collectStatus);
        if (isCredit) {
            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "¥", StringUtils.format2(monthAmount), " 起",
                    13, 11, R.color.color_00bbc0, R.color.color_4a4a4a);
        }
        presenter.getCommodityDetail(Const.CHANNEL, skuCode);
    }

    /**
     * 取消收藏
     */
    private void cancelCollect() {
        tvCollect.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_9b9b9b));
        ivCollect.setImageResource(R.drawable.ic_collect_unselect);
    }


    @OnClick({R.id.tv_buy, R.id.ll_collect, R.id.ll_back, R.id.ll_choose_address})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_back:
                if (vpContent.getCurrentItem() == 1) {
                    vpContent.setCurrentItem(0);
                } else {
                    finish();
                }
                break;

            case R.id.tv_buy:
                commodityInfoFragment.showBuyDialog();
                break;
            case R.id.ll_collect:
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    if (LoginHelper.getInstance().hasQualifications()) {
                        //收藏商品
                        boolean isCheck = (boolean) ivCollect.getTag();
                        String collectSkuCode = commodityInfoFragment.getSkuCode();
                        //已收藏则取消收藏
                        if (isCheck) {
                            //取消收藏
                            presenter.collectCommodity(LoginHelper.getInstance().getIdPerson(), collectSkuCode, 1);
                        } else {
                            //收藏
                            presenter.collectCommodity(LoginHelper.getInstance().getIdPerson(), collectSkuCode, 0);
                        }
                    } else {
                        commodityInfoFragment.showNotActiveDialog();
                    }
                }

                break;
            case R.id.ll_choose_address:
                commodityInfoFragment.showChooseCityDialog();
                break;
        }
    }

    /**
     * 6.0以上系统申请通讯录权限
     */
    public void applyGpsPermission() {
        if (!PermissionHelper.getInstance(this).isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            setPermissionHelper(false, new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
        } else {
            commodityInfoFragment.startLocation();
        }
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        super.onPermissionGranted(permissionName);
        commodityInfoFragment.startLocation();
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        super.onPermissionDeclined(permissionName);
        commodityInfoFragment.rejectGpsPermission();
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        super.onPermissionReallyDeclined(permissionName);
        commodityInfoFragment.rejectGpsPermission();
    }

    /**
     * 是否可点击购买
     *
     * @param enable {@link CommodityInfoFragment#showStockState(int)} ()}
     */
    public void setBuyEnable(boolean enable) {
        tvBuy.setEnabled(enable);
        if (enable) {
            tvBuy.setBackgroundColor(ContextCompat.getColor(mBaseContext, R.color.color_00bbc0));
        } else {
            tvBuy.setBackgroundColor(ContextCompat.getColor(mBaseContext, R.color.color_d8d8d8));
        }
    }

    /**
     * 根据状态显示是否有货，是否可点击{@link CommodityInfoFragment#showStockState(int) 检查完库存后调用，
     * 无货时立即购买按钮不可点击}
     *
     * @param state
     */
    public void refreshStock(int state) {
        switch (state) {
            case 0:
                tvBuy.setEnabled(true);
                tvBuy.setText("立即购买");
                tvBuy.setBackgroundResource(R.color.color_00bbc0);
                llChooseAddress.setVisibility(View.GONE);
                break;

            case 1:
                tvBuy.setEnabled(false);
                tvBuy.setText("所在地区暂时无货");
                llChooseAddress.setVisibility(View.VISIBLE);
                tvBuy.setBackgroundResource(R.color.color_d8d8d8);

                break;

            case 2:
                tvBuy.setEnabled(false);
                tvBuy.setText("商品售罄");
                llChooseAddress.setVisibility(View.GONE);
                tvBuy.setBackgroundResource(R.color.color_d8d8d8);
                break;
        }
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

    /**
     * 在商品信息中点击上拉查看商品详情，直接跳至第二个tab
     */
    public void showCommodityDetail() {
        vpContent.setCurrentItem(1, false);
    }

    @Override
    public void onBackPressed() {
        //返回键可返回上一个fragment
        if (vpContent != null && vpContent.getCurrentItem() == 1) {
            vpContent.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 向服务器发出收藏请求后的回调
     */
    @Override
    public void collectOperator() {
        boolean isCheck = (boolean) ivCollect.getTag();
        //已收藏则取消收藏
        if (isCheck) {
            cancelCollect();
        } else {
            collectCommodity();
        }
        ivCollect.setTag(!isCheck);
    }

    /**
     * 刷新商品详情的相关信息（网页）
     *
     */
    @Override
    public void showCommodity(String url) {
        commodityDetailFragment.refreshCommodityDetail(url);
        commodityInfoFragment.refreshCommodityDetail(url);
    }
}
