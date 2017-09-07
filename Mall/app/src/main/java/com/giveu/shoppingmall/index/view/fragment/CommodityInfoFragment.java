package com.giveu.shoppingmall.index.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.adapter.BannerImageLoader;
import com.giveu.shoppingmall.index.presenter.CommodityInfoPresenter;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;
import com.giveu.shoppingmall.index.view.activity.ConfirmOrderActivity;
import com.giveu.shoppingmall.index.view.agent.ICommodityInfoView;
import com.giveu.shoppingmall.index.view.dialog.BuyCommodityDialog;
import com.giveu.shoppingmall.index.view.dialog.CreditCommodityDialog;
import com.giveu.shoppingmall.model.bean.response.SkuIntroductionResponse;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.widget.DetailView;
import com.giveu.shoppingmall.widget.PullDetailLayout;
import com.giveu.shoppingmall.widget.dialog.ChooseCityDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.giveu.shoppingmall.R.id.fab_up_slide;
import static com.giveu.shoppingmall.R.id.sv_goods_info;
import static com.giveu.shoppingmall.R.id.sv_switch;

/**
 * Created by 513419 on 2017/8/30.
 * 商品信息
 */

public class CommodityInfoFragment extends BaseFragment implements ICommodityInfoView, PullDetailLayout.OnSlideDetailsListener {
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.ll_choose_attr)
    DetailView llChooseAttr;
    @BindView(R.id.ll_choose_address)
    DetailView llChooseAddress;
    @BindView(R.id.tv_pull)
    TextView tvPull;
    @BindView(R.id.ll_pull_up)
    LinearLayout llPullUp;
    @BindView(sv_goods_info)
    ScrollView svGoodsInfo;
    @BindView(R.id.mContainer)
    RelativeLayout mContainer;
    @BindView(sv_switch)
    PullDetailLayout svSwitch;
    @BindView(fab_up_slide)
    ImageView fabUpSlide;
    @BindView(R.id.tv_commodit_name)
    TextView tvCommoditName;
    @BindView(R.id.tv_introduction)
    TextView tvIntroduction;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.dv_stock)
    DetailView dvStock;
    private View view;
    private CommodityDetailFragment commodityDetailFragment;
    private CommodityDetailActivity activity;
    private BuyCommodityDialog buyDialog;//商品属性对话框
    private ChooseCityDialog chooseCityDialog;
    private boolean isCredit;//分期商品还是一次性商品
    private String skuCode;
    private CommodityInfoPresenter presenter;

    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_info, null);
        baseLayout.setTitleBarAndStatusBar(false, false);
        ButterKnife.bind(this, view);

        activity = (CommodityDetailActivity) mBaseContext;
        presenter = new CommodityInfoPresenter(this);
        isCredit = getArguments().getBoolean("isCredit", false);
        skuCode = getArguments().getString("skuCode");
        buyDialog = new BuyCommodityDialog(mBaseContext);
        chooseCityDialog = new ChooseCityDialog(mBaseContext);
        initListener();
        initBanner();
        return view;
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }


    @Override
    protected void setListener() {

    }


    private void initBanner() {
        commodityDetailFragment = new CommodityDetailFragment();
        Bundle detailBundle = new Bundle();
        detailBundle.putString("skuCode", skuCode);
        commodityDetailFragment.setArguments(detailBundle);
        commodityDetailFragment.setFromCommodityDetail(false);
        getChildFragmentManager().beginTransaction().replace(R.id.mContainer, commodityDetailFragment).commitAllowingStateLoss();
        banner = (Banner) view.findViewById(R.id.banner);
        banner.getLayoutParams().height = DensityUtils.getWidth();
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void initListener() {
        fabUpSlide.setOnClickListener(this);
        svSwitch.setOnSlideDetailsListener(this);
        llPullUp.setOnClickListener(this);
        chooseCityDialog.setOnConfirmListener(new ChooseCityDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String province, String city, String region, String street) {
                chooseCityDialog.dismiss();
                llChooseAddress.setMiddleText(province + " " + city + " " + region + " " + street);
            }
        });
        buyDialog.setOnConfirmListener(new BuyCommodityDialog.OnConfirmListener() {
            @Override
            public void confirm(LinkedHashMap<String, String> attrHashMap) {
                if (isCredit) {
                    CreditCommodityDialog creditDialog = new CreditCommodityDialog(mBaseContext);
                    creditDialog.setOnConfirmListener(new CreditCommodityDialog.OnConfirmListener() {
                        @Override
                        public void confirm() {
                            ConfirmOrderActivity.startIt(mBaseContext);
                        }

                        @Override
                        public void cancle() {

                        }
                    });
                    creditDialog.show();
                }
                String attrStr = "";
                for (Map.Entry<String, String> entry : attrHashMap.entrySet()) {
                    attrStr += entry.getValue() + " ";
                }
                llChooseAttr.setMiddleText(attrStr);
            }

            @Override
            public void cancle() {

            }
        });
    }

    @OnClick({R.id.ll_pull_up, R.id.fab_up_slide, R.id.ll_choose_address, R.id.ll_choose_attr})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_choose_attr:
                buyDialog.showDialog(isCredit);
                break;

            case R.id.ll_choose_address:
                chooseCityDialog.show();
                break;

            case R.id.ll_pull_up:
                //上拉查看图文详情
                activity.showCommodityDetail();
                break;

            case R.id.fab_up_slide:
                //点击滑动到顶部
                svGoodsInfo.smoothScrollTo(0, 0);
                svSwitch.smoothClose(true);
                break;

        }
    }

    @Override
    public void initDataDelay() {
        //K00002691可以分期   K00002713可以一次
        presenter.getSkuIntroduce(LoginHelper.getInstance().getIdPerson(), "qq", "K00002713");

    }

    @Override
    public void onStatucChanged(PullDetailLayout.Status status) {
        if (status == PullDetailLayout.Status.OPEN) {
            svSwitch.smoothClose(true);
            activity.showCommodityDetail();
            //当前为图文详情页
            fabUpSlide.setVisibility(View.VISIBLE);
            activity.vpContent.setScrollDisabled(true);
            tvPull.setText("下拉收起商品详情");
            ivArrow.setImageResource(R.drawable.ic_arrow_down);
        } else {
            //当前为商品详情页
            fabUpSlide.setVisibility(View.GONE);
            activity.vpContent.setScrollDisabled(false);
            ivArrow.setImageResource(R.drawable.ic_arrow_up);
            tvPull.setText("上拉查看商品详情");
        }
    }

    public void showBuyDialog() {
        if (buyDialog != null) {
            buyDialog.show();
        }
    }

    @Override
    public void showSkuIntroduction(SkuIntroductionResponse skuResponse) {
        if (skuResponse.skuInfo != null) {
            banner.update(skuResponse.skuInfo.srcs);
            tvCommoditName.setText(skuResponse.skuInfo.name);
            tvPrice.setText("¥" + skuResponse.skuInfo.salePrice);
            tvIntroduction.setText(skuResponse.skuInfo.adwords);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
