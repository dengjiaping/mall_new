package com.giveu.shoppingmall.index.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.adapter.BannerImageLoader;
import com.giveu.shoppingmall.index.presenter.CommodityInfoPresenter;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;
import com.giveu.shoppingmall.index.view.activity.ConfirmOrderActivity;
import com.giveu.shoppingmall.index.view.agent.ICommodityInfoView;
import com.giveu.shoppingmall.index.view.dialog.BuyCommodityDialog;
import com.giveu.shoppingmall.index.view.dialog.CreditCommodityDialog;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.SkuIntroductionResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.LocationUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.listener.LocationListener;
import com.giveu.shoppingmall.widget.DetailView;
import com.giveu.shoppingmall.widget.PullDetailLayout;
import com.giveu.shoppingmall.widget.dialog.ChooseCityDialog;
import com.giveu.shoppingmall.widget.flowlayout.FlowLayout;
import com.giveu.shoppingmall.widget.flowlayout.TagAdapter;
import com.giveu.shoppingmall.widget.flowlayout.TagFlowLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;

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
    @BindView(R.id.dv_supply)
    DetailView dvSupply;
    @BindView(R.id.fl_server)
    TagFlowLayout flServer;
    @BindView(R.id.ll_server)
    LinearLayout llServer;
    private View view;
    private CommodityDetailFragment commodityDetailFragment;
    private CommodityDetailActivity activity;
    private BuyCommodityDialog buyDialog;//商品属性对话框
    private ChooseCityDialog chooseCityDialog;
    private boolean isCredit;//分期商品还是一次性商品
    private String skuCode;
    private CommodityInfoPresenter presenter;
    private LocationUtils locationUtils;//定位
    private TagAdapter<String> serverAdapter;//商品服务对应的流式布局
    private String provinceStr;
    private String cityStr;
    private String regionStr;
    private CreditCommodityDialog creditDialog;
    private String smallIconStr;
    private String commodityPrice;
    private int commodityAmounts;
    private String commodityName;


    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_info, null);
        baseLayout.setTitleBarAndStatusBar(false, false);
        ButterKnife.bind(this, view);

        activity = (CommodityDetailActivity) mBaseContext;
        presenter = new CommodityInfoPresenter(this);
        isCredit = getArguments().getBoolean("isCredit", false);
        skuCode = getArguments().getString("skuCode");
        //购买对话框
        buyDialog = new BuyCommodityDialog(mBaseContext);
        //地址选择对话框
        chooseCityDialog = new ChooseCityDialog(mBaseContext);
        creditDialog = new CreditCommodityDialog(mBaseContext);
        //不用选择街道，只选择省市区即可
        chooseCityDialog.setNeedStreet(false);
        initBanner();
        //服务的流式布局对应的adapter
        serverAdapter = new TagAdapter<String>(new ArrayList<String>()) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvTag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flowlayout_server_item, flServer, false);
                tvTag.setText(s);
                return tvTag;
            }
        };
        flServer.setAdapter(serverAdapter);
        //上拉对应的view
        commodityDetailFragment = new CommodityDetailFragment();
        Bundle detailBundle = new Bundle();
        detailBundle.putString("skuCode", skuCode);
        commodityDetailFragment.setArguments(detailBundle);
        commodityDetailFragment.setFromCommodityDetail(false);
        getChildFragmentManager().beginTransaction().replace(R.id.mContainer, commodityDetailFragment).commitAllowingStateLoss();
        return view;
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }


    @Override
    protected void setListener() {
        initListener();
    }


    /**
     * 初始化商品轮播图
     */
    private void initBanner() {
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
                provinceStr = province;
                cityStr = city;
                regionStr = region;
                llChooseAddress.setMiddleText(province + " " + city + " " + region + " " + street);
                //选择完地址后查询该商品在该地区是否有货
                presenter.queryCommodityStock(provinceStr, cityStr, regionStr, skuCode);
            }
        });
        buyDialog.setBuyEnable(false);

        buyDialog.setOnChooseCompleteListener(new BuyCommodityDialog.OnChooseCompleteListener() {
            @Override
            public void onComplete(String skuCode) {
                CommodityInfoFragment.this.skuCode = skuCode;
                //选完属性后再次查询商品的相关信息，并检查库存
                getCommodityInfo();
                presenter.queryCommodityStock(provinceStr, cityStr, regionStr, skuCode);
            }
        });
        buyDialog.setOnConfirmListener(new BuyCommodityDialog.OnConfirmListener() {
            @Override
            public void confirm(int amounts) {
                //如果是分期产品，那么需要选择分期数，首付等
                if (isCredit) {
                    commodityAmounts = amounts;
                    presenter.getAppDownPayAndMonthPay(Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), 0, skuCode);
                } else {
                    ConfirmOrderActivity.startIt(mBaseContext);
                }
            }

            @Override
            public void cancle() {

            }
        });
        creditDialog.setOnDownPayChangeListener(new CreditCommodityDialog.OnDownPayChangeListener() {
            @Override
            public void onChange(int downPayRate) {
                presenter.getAppDownPayAndMonthPay(Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), downPayRate, skuCode);
            }
        });

        creditDialog.setOnConfirmListener(new CreditCommodityDialog.OnConfirmListener() {
            @Override
            public void confirm() {
                ConfirmOrderActivity.startIt(mBaseContext);
            }

            @Override
            public void cancle() {

            }
        });
    }

    public void showChooseCityDialog() {
        if (chooseCityDialog != null) {
            chooseCityDialog.show();
        }
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
        getCommodityInfo();
        //获取默认的地址，没有的话获取收货地址的第一个，或定位的位置
        if (StringUtils.isNotNull(LoginHelper.getInstance().getReceiveProvince())) {
            provinceStr = LoginHelper.getInstance().getReceiveProvince();
            cityStr = LoginHelper.getInstance().getReceiveCity();
            regionStr = LoginHelper.getInstance().getReceiveRegion();
            llChooseAddress.setMiddleText(provinceStr + " " + cityStr + " " + regionStr);
            //检查库存
            presenter.queryCommodityStock(provinceStr, cityStr, regionStr, skuCode);
        } else {
            //获取收货地址，并取收货地址的第一个地址
            if (LoginHelper.getInstance().hasQualifications()) {
                presenter.getAddressList(LoginHelper.getInstance().getIdPerson(), "5");
            } else {
                //获取定位的位置
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.applyGpsPermission();
                } else {
                    startLocation();
                }
            }
        }
    }

    /**
     * 获取商品信息
     */
    public void getCommodityInfo() {
        presenter.getSkuIntroduce(LoginHelper.getInstance().getIdPerson(), "SC", skuCode);
    }

    @Override
    public void onStatucChanged(PullDetailLayout.Status status) {
        if (status == PullDetailLayout.Status.OPEN) {
            //当前为图文详情页
            fabUpSlide.setVisibility(View.VISIBLE);
            activity.vpContent.setScrollDisabled(true);
            tvPull.setText("下拉收起商品详情");
            activity.hideTabLayout();
            ivArrow.setImageResource(R.drawable.ic_arrow_down);
        } else {
            //当前为商品详情页
            fabUpSlide.setVisibility(View.GONE);
            activity.vpContent.setScrollDisabled(false);
            ivArrow.setImageResource(R.drawable.ic_arrow_up);
            tvPull.setText("上拉查看商品详情");
            activity.showTabLayout();
        }
    }

    public void showBuyDialog() {
        if (buyDialog != null) {
            buyDialog.showDialog(isCredit);
        }
    }

    @Override
    public void showSkuIntroduction(SkuIntroductionResponse skuResponse) {
        if (skuResponse.skuInfo != null) {
            //更新轮播图
            banner.update(skuResponse.skuInfo.srcs);
            tvCommoditName.setText(skuResponse.skuInfo.name);
            CommonUtils.setTextWithSpanSizeAndColor(tvPrice, "¥", StringUtils.format2(skuResponse.skuInfo.salePrice),
                    "", 19, 15, R.color.color_ff2a2a, R.color.color_999999);

            tvIntroduction.setText(skuResponse.skuInfo.adwords);
            dvSupply.setMiddleText(skuResponse.skuInfo.supplier);
            //给购买对话框初始化数据
            buyDialog.setData(isCredit, skuCode, skuResponse);
            //服务承诺对应的数据
            if (CommonUtils.isNotNullOrEmpty(skuResponse.skuInfo.serviceSafeguards)) {
                ArrayList<String> serverList = new ArrayList<>();
                for (SkuIntroductionResponse.ServiceSafeguardsBean safeguard : skuResponse.skuInfo.serviceSafeguards) {
                    serverList.add(safeguard.name);
                }
                serverAdapter.setDatas(serverList);
                llServer.setVisibility(View.VISIBLE);
            } else {
                llServer.setVisibility(View.GONE);
            }
            //获取购买对话框选择属性对应的skuCode，更新skuCode
            skuCode = buyDialog.getSkuCode();
            llChooseAttr.setMiddleText(buyDialog.getAttrStr());
            commodityDetailFragment.refreshCommodityDetail(skuCode);
            //刷新商品详情activity的数据
            activity.initData(skuCode, skuResponse.collectStatus, skuResponse.skuInfo.monthAmount);
            smallIconStr = skuResponse.skuInfo.srcIp + "/" + skuResponse.skuInfo.src;
            commodityName = skuResponse.skuInfo.name;
            commodityPrice = skuResponse.skuInfo.salePrice;
        }
    }


    @Override
    public void showStockState(int state) {
        switch (state) {
            case 0:
                dvStock.setMiddleText("有货");
                //可点击购买
                buyDialog.setBuyEnable(true);
                activity.setBuyEnable(true);
                break;

            case 1:
                dvStock.setMiddleText("无货");
                //不可点击立即购买或在购买对话框中不可点击下一步
                buyDialog.setBuyEnable(false);
                activity.setBuyEnable(false);

                break;

            case 2:
                dvStock.setMiddleText("商品售罄");
                //不可点击立即购买或在购买对话框中不可点击下一步
                buyDialog.setBuyEnable(false);
                activity.setBuyEnable(false);
                break;
        }
        activity.refreshStock(state);
    }

    @Override
    public void getAddressList(boolean hasAddress, String province, String city, String region) {
        //有收货地址
        if (hasAddress) {
            this.provinceStr = province;
            this.cityStr = city;
            this.regionStr = region;
            llChooseAddress.setMiddleText(provinceStr + " " + cityStr + " " + regionStr);
            presenter.queryCommodityStock(provinceStr, cityStr, regionStr, skuCode);
        } else {
            //没有收货地址直接获取定位位置
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.applyGpsPermission();
            } else {
                startLocation();
            }
        }
    }

    @Override
    public void showDownPayMonthPay(boolean success, ArrayList<DownPayMonthPayResponse> data) {
        if (success) {
            if (CommonUtils.isNotNullOrEmpty(data)) {
                creditDialog.initData(commodityAmounts, smallIconStr, commodityName, commodityPrice, data);
                creditDialog.show();
            }
        }
    }

    public void rejectGpsPermission() {
        if (llChooseAddress != null) {
            //GPS定位失败，直接设置为北京市
            llChooseAddress.setMiddleText("北京市");
            provinceStr = "北京市";
            cityStr = "";
            regionStr = "";
            //GPS获取省市区后查询该商品是否有货
            presenter.queryCommodityStock(provinceStr, cityStr, regionStr, skuCode);
        }
    }

    public void startLocation() {
        if (locationUtils == null) {
            locationUtils = new LocationUtils(BaseApplication.getInstance());
            locationUtils.setOnLocationListener(new LocationListener() {
                @Override
                public void onSuccess(AMapLocation location) {
                    provinceStr = location.getProvince();
                    cityStr = location.getCity();
                    regionStr = location.getDistrict();
                    llChooseAddress.setMiddleText(provinceStr + " " + cityStr + " " + regionStr);
                    //GPS获取省市区后查询该商品是否有货
                    presenter.queryCommodityStock(provinceStr, cityStr, regionStr, skuCode);
                    locationUtils.stopLocation();
                }

                @Override
                public void onFail(Object o) {
                    //GPS定位失败，直接设置为北京市
                    llChooseAddress.setMiddleText("北京市");
                    provinceStr = "北京市";
                    cityStr = "";
                    regionStr = "";
                    //GPS获取省市区后查询该商品是否有货
                    presenter.queryCommodityStock(provinceStr, cityStr, regionStr, skuCode);
                    locationUtils.stopLocation();
                }
            });
        }
        locationUtils.startLocation();
    }

    public String getSkuCode() {
        return skuCode;
    }

    @Override
    public void onDestroy() {
        if (locationUtils != null) {
            locationUtils.destory();
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
