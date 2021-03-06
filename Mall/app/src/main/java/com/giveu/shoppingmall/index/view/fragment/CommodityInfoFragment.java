package com.giveu.shoppingmall.index.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
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
import com.giveu.shoppingmall.index.view.dialog.AnnuityDialog;
import com.giveu.shoppingmall.index.view.dialog.BuyCommodityDialog;
import com.giveu.shoppingmall.index.view.dialog.CreditCommodityDialog;
import com.giveu.shoppingmall.me.view.activity.OfftheShelfActivity;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.GoodsInfoResponse;
import com.giveu.shoppingmall.model.bean.response.MonthSupplyResponse;
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
import com.giveu.shoppingmall.widget.photoview.PreviewPhotoActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.sv_goods_info)
    ScrollView svGoodsInfo;
    @BindView(R.id.mContainer)
    RelativeLayout mContainer;
    @BindView(R.id.sv_switch)
    PullDetailLayout svSwitch;
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
    ViewStub vsCommodityInfo;
    private View view;
    private WebCommodityFragment commodityDetailFragment;
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
    private AnnuityDialog annuityDialog;


    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_info, null);
        baseLayout.setTitleBarAndStatusBar(false, false);
        activity = (CommodityDetailActivity) mBaseContext;
        presenter = new CommodityInfoPresenter(this);
        isCredit = getArguments().getBoolean("isCredit", false);
        skuCode = getArguments().getString("skuCode");
        return view;
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }


    @Override
    protected void setListener() {
    }


    /**
     * 初始化商品轮播图
     */
    private void initBanner() {
        banner.getLayoutParams().height = DensityUtils.getWidth();
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置轮播时间
//        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void initListener() {
        svSwitch.setOnSlideDetailsListener(this);
        llPullUp.setOnClickListener(this);
        chooseCityDialog.setOnConfirmListener(new ChooseCityDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String province, String city, String region, String street) {
                //   chooseCityDialog.dismiss();
                provinceStr = province;
                cityStr = city;
                regionStr = region;
                llChooseAddress.setMiddleText(province + " " + city + " " + region + " " + street);
                //选择完地址后查询该商品在该地区是否有货
                presenter.queryCommodityStock(provinceStr, cityStr, regionStr, "", skuCode, 0, "", "");
            }
        });
        buyDialog.setBuyDisable();

        buyDialog.setOnChooseCompleteListener(new BuyCommodityDialog.OnChooseCompleteListener() {
            @Override
            public void onComplete(String skuCode) {
                CommodityInfoFragment.this.skuCode = skuCode;
                buyDialog.setBuyDisable();
                //选完属性后再次查询商品的相关信息，并检查库存
                getCommodityInfo();
                presenter.queryCommodityStock(provinceStr, cityStr, regionStr, "", skuCode, 0, "", "");
            }
        });
        buyDialog.setOnConfirmListener(new BuyCommodityDialog.OnConfirmListener() {
            @Override
            public void confirm(int amounts, int paymentType) {
                commodityAmounts = amounts;
                if (LoginHelper.getInstance().hasLoginAndActivation(mBaseContext)) {
                    //如果是分期产品，那么需要选择分期数，首付等
                    if (isCredit) {
                        creditDialog.initData(commodityAmounts, smallIconStr, commodityName, commodityPrice, null);
                        creditDialog.show();
                        creditDialog.setConfirmEnable(false);
                        //如果刚进来这个页面时获取首付比例失败了，再次获取，获取成功后会自动获取分期数，否则直接获取分期数
                        if (!creditDialog.hasInitSuccess()) {
                            presenter.getGoodsInfo(Const.CHANNEL);
                        }else {
                            presenter.getAppDownPayAndMonthPay(Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), creditDialog.getDownPayRate(), skuCode, commodityAmounts);
                        }
                    } else {
                        ConfirmOrderActivity.startIt(mBaseContext, 0, 0, commodityAmounts, skuCode, paymentType);
                    }
                }

            }

            @Override
            public void cancle() {

            }
        });
        creditDialog.setOnDownPayChangeListener(new CreditCommodityDialog.OnDownPayChangeListener() {
            @Override
            public void onChange(int downPayRate) {
                presenter.getAppDownPayAndMonthPay(Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), downPayRate, skuCode, commodityAmounts);
            }

            /**
             * 展示月供详情
             * @param downPaymentRate
             * @param idProduct
             * @param quantity
             */
            @Override
            public void showAppMonthlySupply(int downPaymentRate, long idProduct, int quantity) {
                presenter.getAppMonthlySupply(Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), downPaymentRate, idProduct, 0, skuCode, quantity);
            }
        });

        creditDialog.setOnConfirmListener(new CreditCommodityDialog.OnConfirmListener() {
            @Override
            public void confirm(int downPayRate, long idProduct, int paymentType) {
                ConfirmOrderActivity.startIt(mBaseContext, downPayRate, idProduct, commodityAmounts, skuCode, paymentType);
                creditDialog.dismiss();
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

    @OnClick({R.id.ll_pull_up, R.id.ll_choose_address, R.id.ll_choose_attr})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_choose_attr:
                buyDialog.showDialog();
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

    /**
     * 关闭详情回到顶部
     *
     * @return
     */
    public boolean needCloseDetail() {
        if (svSwitch != null && svSwitch.getStatus() == PullDetailLayout.Status.OPEN) {
            svSwitch.smoothClose(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void initDataDelay() {
        getCommodityInfo();
    }

    private void initAndLoadData() {
        //已经初始化过了，不再初始化
        if (svSwitch != null) {
            return;
        }
        vsCommodityInfo = (ViewStub) view.findViewById(R.id.vs_commodity_info);
        vsCommodityInfo.inflate();
        ButterKnife.bind(this, view);
        initBanner();

        //购买对话框
        buyDialog = new BuyCommodityDialog(mBaseContext);
        //地址选择对话框
        chooseCityDialog = new ChooseCityDialog(mBaseContext);
        creditDialog = new CreditCommodityDialog(mBaseContext);
        //不用选择街道，只选择省市区即可
        chooseCityDialog.setNeedStreet(false);
        //服务承诺的流式布局对应的adapter
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
        commodityDetailFragment = new WebCommodityFragment();
        Bundle detailBundle = new Bundle();
        detailBundle.putString("skuCode", skuCode);
        commodityDetailFragment.setArguments(detailBundle);
        commodityDetailFragment.setFromCommodityDetail(false);
        getChildFragmentManager().beginTransaction().replace(R.id.mContainer, commodityDetailFragment).commitAllowingStateLoss();
        initListener();
        //获取默认的地址，没有的话获取收货地址的第一个，或定位的位置
        if (StringUtils.isNotNull(LoginHelper.getInstance().getReceiveProvince())) {
            provinceStr = LoginHelper.getInstance().getReceiveProvince();
            cityStr = LoginHelper.getInstance().getReceiveCity();
            regionStr = LoginHelper.getInstance().getReceiveRegion();
            llChooseAddress.setMiddleText(provinceStr + " " + cityStr + " " + regionStr);
            //检查库存
            presenter.queryCommodityStock(provinceStr, cityStr, regionStr, "", skuCode, 0, "", "");
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
        //分期产品获取首付比例
        if (isCredit) {
            presenter.getGoodsInfo(Const.CHANNEL);
        }
        presenter.getSkuIntroduce(LoginHelper.getInstance().getIdPerson(), Const.CHANNEL, skuCode);
    }

    @Override
    public void onStatucChanged(PullDetailLayout.Status status) {
        if (status == PullDetailLayout.Status.OPEN) {
            //当前为图文详情页
            activity.vpContent.setScrollDisabled(true);
            tvPull.setText("下拉收起商品详情");
            activity.hideTabLayout();
            ivArrow.setImageResource(R.drawable.ic_arrow_down);
        } else {
            //当前为商品详情页
            activity.vpContent.setScrollDisabled(false);
            ivArrow.setImageResource(R.drawable.ic_arrow_up);
            tvPull.setText("上拉查看商品详情");
            activity.showTabLayout();
        }
    }

    public void showBuyDialog() {
        if (buyDialog != null) {
            buyDialog.showDialog();
        }
    }

    @Override
    public void showSkuIntroduction(boolean success, final SkuIntroductionResponse skuResponse) {
        //获取数据失败
        if (!success) {
            //商品已下架，关闭当前页，跳转至下架页面，sc100200为商品下架code
            if ("sc100200".equals(skuResponse.code)) {
                if (mBaseContext != null) {
                    mBaseContext.finish();
                    OfftheShelfActivity.startIt(mBaseContext);
                }
            }
            return;
        }

        initAndLoadData();
        if (skuResponse.skuInfo != null) {
            isCredit = skuResponse.skuInfo.isCredit();
            activity.initBuyView(isCredit);
            //图片放大
            final ArrayList<String> imageList = new ArrayList<>();
            if (CommonUtils.isNotNullOrEmpty(skuResponse.skuSpecs)) {
                imageList.addAll(skuResponse.skuInfo.srcs);
            }
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    PreviewPhotoActivity.startIt(mBaseContext, "图片浏览", imageList, position, true);
                }
            });
            //更新轮播图
            banner.update(skuResponse.skuInfo.srcs);
            tvCommoditName.setText(StringUtils.ToAllFullWidthString(skuResponse.skuInfo.name));
            CommonUtils.setTextWithSpanSizeAndColor(tvPrice, "¥", StringUtils.format2(skuResponse.skuInfo.salePrice),
                    "", 19, 13, R.color.color_ff2a2a, R.color.color_999999);

            if (StringUtils.isNotNull(skuResponse.skuInfo.adwords)) {
                tvIntroduction.setText(skuResponse.skuInfo.adwords);
            } else {
                tvIntroduction.setText("");
            }
            dvSupply.setMiddleText(skuResponse.skuInfo.supplier);
            //给购买对话框初始化数据
            buyDialog.setData(isCredit, skuCode, skuResponse);
            //服务承诺对应的数据
            if (CommonUtils.isNotNullOrEmpty(skuResponse.skuInfo.serviceSafeguards)) {
                ArrayList<String> serverList = new ArrayList<>();
                for (String serviceSafeguard : skuResponse.skuInfo.serviceSafeguards) {
                    serverList.add(serviceSafeguard);
                }
              /*  for (SkuIntroductionResponse.ServiceSafeguardsBean safeguard : skuResponse.skuInfo.serviceSafeguards) {
                }*/
                serverAdapter.setDatas(serverList);
                llServer.setVisibility(View.VISIBLE);
            } else {
                llServer.setVisibility(View.GONE);
            }
            //获取购买对话框选择属性对应的skuCode，更新skuCode
            skuCode = buyDialog.getSkuCode();
            llChooseAttr.setMiddleText(buyDialog.getAttrStr());
            //刷新商品详情activity的数据
            activity.initData(skuCode, skuResponse.collectStatus, skuResponse.skuInfo.monthAmount);
            smallIconStr = skuResponse.skuInfo.srcIp + "/" + skuResponse.skuInfo.src;
            commodityName = skuResponse.skuInfo.name;
            commodityPrice = skuResponse.skuInfo.salePrice;
        }
    }

    public void refreshCommodityDetail(String url) {
        initAndLoadData();
        commodityDetailFragment.refreshCommodityDetail(url);
    }


    @Override
    public void showStockState(boolean isSuccess, int state) {
        initAndLoadData();
        //查询失败，默认显示福田区，并且是有货的
        if (!isSuccess) {
            llChooseAddress.setMiddleText("广东 深圳市 福田区");
        }
        switch (state) {
            case 0:
                dvStock.setMiddleText("有货");
                //可点击购买
                buyDialog.setBuyEnable(state);
                //activity的购买按键是否可点击
                activity.setBuyEnable(true);
                break;

            case 1:
                dvStock.setMiddleText("无货");
                //不可点击立即购买或在购买对话框中不可点击下一步
                buyDialog.setBuyEnable(state);
                activity.setBuyEnable(false);

                break;

            case 2:
                dvStock.setMiddleText("商品售罄");
                //不可点击立即购买或在购买对话框中不可点击下一步
                buyDialog.setBuyEnable(state);
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
            presenter.queryCommodityStock(provinceStr, cityStr, regionStr, "", skuCode, 0, "", "");
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
            }
        }
    }

    /**
     * 更新月供相关UI
     *
     * @param response 月供数据
     */
    @Override
    public void showAppMonthlySupply(MonthSupplyResponse response) {
        if (CommonUtils.isNotNullOrEmpty(response.paymentList)) {
            if (annuityDialog == null) {
                annuityDialog = new AnnuityDialog(mBaseContext);
            }
            annuityDialog.refreshData(response, true);
            annuityDialog.show();
        }
    }

    @Override
    public void initGoodsInfo(ArrayList<GoodsInfoResponse> data) {
        if (CommonUtils.isNullOrEmpty(data)) {
            return;
        }
        initAndLoadData();
        creditDialog.initDownPaymentRate(data);
        //获取首付比例后获取分期数
        presenter.getAppDownPayAndMonthPay(Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), creditDialog.getDownPayRate(), skuCode, commodityAmounts);
    }

    public void rejectGpsPermission() {
        if (llChooseAddress != null) {
            //GPS定位失败，直接设置为广东 深圳市 福田区
            llChooseAddress.setMiddleText("广东 深圳市 福田区");
            provinceStr = "广东";
            cityStr = "深圳市";
            regionStr = "福田区";
            llChooseAddress.setMiddleText(provinceStr + " " + cityStr + " " + regionStr);
            //GPS获取省市区后查询该商品是否有货
            presenter.queryCommodityStock(provinceStr, cityStr, regionStr, "", skuCode, 0, "", "");
        }
    }

    public void startLocation() {
        llChooseAddress.setMiddleText("正在定位...");
        if (locationUtils == null) {
            locationUtils = new LocationUtils(BaseApplication.getInstance());
            locationUtils.setOnLocationListener(new LocationListener() {
                @Override
                public void onSuccess(AMapLocation location) {
                    provinceStr = location.getProvince();
                    cityStr = location.getCity();
                    regionStr = location.getDistrict();
                    locationUtils.stopLocation();
                    llChooseAddress.setMiddleText(provinceStr + " " + cityStr + " " + regionStr);
                    presenter.queryCommodityStock(provinceStr, cityStr, regionStr,
                            location.getStreet(), skuCode, 1, location.getLatitude() + "", location.getLongitude() + "");
//                    chooseCityDialog.setOriginalAddress(provinceStr, cityStr, regionStr, "");
                }

                @Override
                public void onFail(Object o) {
                    //GPS定位失败，直接设置为广东 深圳市 福田区
                    llChooseAddress.setMiddleText("广东 深圳市 福田区");
                    provinceStr = "广东";
                    cityStr = "深圳市";
                    regionStr = "福田区";
                    //GPS获取省市区后查询该商品是否有货
                    presenter.queryCommodityStock(provinceStr, cityStr, regionStr, "", skuCode, 0, "", "");
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

}
