package com.giveu.shoppingmall.index.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;
import com.giveu.shoppingmall.widget.PullDetailLayout;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/8/30.
 * 商品信息
 */

public class CommodityInfoFragment extends Fragment implements View.OnClickListener, PullDetailLayout.OnSlideDetailsListener {
    private View view;
    private ViewPager vpImages;
    private ArrayList<String> imageUrlList;
    private TextView tvPageNo;
    private PullDetailLayout sv_switch;
    private ScrollView sv_goods_info;
    private FloatingActionButton fab_up_slide;
    private TextView tvPull;
    private View llPullUp;
    private CommodityDetailFragment commodityDetailFragment;
    private CommodityDetailActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (CommodityDetailActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_infol, null);
        initView();
        initListener();
        return view;
    }

    private void initView() {
        vpImages = (ViewPager) view.findViewById(R.id.vp_images);
        tvPageNo = (TextView) view.findViewById(R.id.tv_pageNo);
        tvPull = (TextView) view.findViewById(R.id.tv_pull);
        llPullUp = view.findViewById(R.id.ll_pull_up);
        fab_up_slide = (FloatingActionButton) view.findViewById(R.id.fab_up_slide);
        sv_switch = (PullDetailLayout) view.findViewById(R.id.sv_switch);
        sv_goods_info = (ScrollView) view.findViewById(R.id.sv_goods_info);
        commodityDetailFragment = new CommodityDetailFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.mContainer, commodityDetailFragment).commitAllowingStateLoss();
        imageUrlList = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            imageUrlList.add(j + "");
        }
        vpImages.setAdapter(new ImageAdapter(imageUrlList));
        tvPageNo.setText((vpImages.getCurrentItem() + 1) + "/" + imageUrlList.size());
        vpImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPageNo.setText((vpImages.getCurrentItem() + 1) + "/" + imageUrlList.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initListener() {
        fab_up_slide.setOnClickListener(this);
        sv_switch.setOnSlideDetailsListener(this);
        llPullUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pull_up:
                //上拉查看图文详情
                activity.showCommodityDetail();
                break;

            case R.id.fab_up_slide:
                //点击滑动到顶部
                sv_goods_info.smoothScrollTo(0, 0);
                sv_switch.smoothClose(true);
                break;
        }
    }

    @Override
    public void onStatucChanged(PullDetailLayout.Status status) {
        if (status == PullDetailLayout.Status.OPEN) {
            //当前为图文详情页
            fab_up_slide.show();
            activity.vp_content.setScrollDisabled(true);
            tvPull.setText("下拉收起商品详情");
            activity.hideTabLayout();
        } else {
            //当前为商品详情页
            fab_up_slide.hide();
            activity.vp_content.setScrollDisabled(false);
            tvPull.setText("上拉查看商品详情");
            activity.showTabLayout();
        }
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
