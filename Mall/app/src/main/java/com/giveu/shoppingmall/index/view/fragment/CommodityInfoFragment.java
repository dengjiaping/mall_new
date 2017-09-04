package com.giveu.shoppingmall.index.view.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
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
import com.giveu.shoppingmall.index.adapter.BannerImageLoader;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;
import com.giveu.shoppingmall.index.view.dialog.BuyCommodityDialog;
import com.giveu.shoppingmall.index.view.dialog.CreditCommodityDialog;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.widget.DetailView;
import com.giveu.shoppingmall.widget.PullDetailLayout;
import com.giveu.shoppingmall.widget.dialog.ChooseCityDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

public class CommodityInfoFragment extends BaseFragment implements PullDetailLayout.OnSlideDetailsListener {
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
    private View view;
    private CommodityDetailFragment commodityDetailFragment;
    private CommodityDetailActivity activity;
    private BuyCommodityDialog buyDialog;//商品属性对话框
    private ChooseCityDialog chooseCityDialog;
    private boolean isCredit;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_info, null);
        baseLayout.setTitleBarAndStatusBar(false, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        activity = (CommodityDetailActivity) mBaseContext;
        return view;
    }

    @Override
    protected void setListener() {

    }


    private void initView() {
        commodityDetailFragment = new CommodityDetailFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.mContainer, commodityDetailFragment).commitAllowingStateLoss();
        banner = (Banner) view.findViewById(R.id.banner);
        banner.getLayoutParams().height = DensityUtils.getWidth();
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        final List<String> images = new ArrayList<String>();
        images.add("http://pic28.nipic.com/20130422/2547764_110759716145_2.jpg");
        images.add("http://pic27.nipic.com/20130319/10415779_103704478000_2.jpg");
        images.add("http://img5.imgtn.bdimg.com/it/u=2062816722,137475371&fm=26&gp=0.jpg");
        images.add("http://img.taopic.com/uploads/allimg/120222/34250-12022209414087.jpg");
        images.add("http://sc.jb51.net/uploads/allimg/131031/2-13103115593HY.jpg");
        banner.setImages(images);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                LogUtil.e(images.get(position));
            }
        });
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(images);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        isCredit = getArguments().getBoolean("isCredit", false);
        buyDialog = new BuyCommodityDialog(mBaseContext);
        chooseCityDialog = new ChooseCityDialog(mBaseContext);
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
                    creditDialog.show();
                }
                String attrStr = "";
                for (Map.Entry<String, String> entry : attrHashMap.entrySet()) {
                    attrStr += entry.getValue()+" ";
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public class ImageAdapter extends PagerAdapter {

        private ArrayList<String> imageList;

        public ImageAdapter(ArrayList<String> imageList) {
            this.imageList = imageList;
        }

        @Override
        public int getCount() {
            if (imageList == null) {
                return 0;
            }
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(R.mipmap.ic_launcher);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
            imageView.setLayoutParams(params);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
