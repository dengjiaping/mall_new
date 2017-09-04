package com.giveu.shoppingmall.index.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.ItemViewDelegate;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.MultiItemTypeAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.widget.NoScrollGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 513419 on 2017/8/30.
 */

public class ShoppingAdapter extends MultiItemTypeAdapter<String> {
    private Banner banner;

    public ShoppingAdapter(Context context, List<String> datas) {
        super(context, datas);
        addItemViewDelegate(new ItemViewDelegate<String>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_shopping_banner;
            }

            @Override
            public boolean isForViewType(String item, int position) {
                return item.equals("banner");
            }

            @Override
            public void convert(ViewHolder holder, String t, int position) {
                banner = holder.getView(R.id.banner);
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
            }
        });

        addItemViewDelegate(new ItemViewDelegate<String>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_shopping_hot;
            }

            @Override
            public boolean isForViewType(String item, int position) {
                return item.equals("hot");
            }

            @Override
            public void convert(ViewHolder holder, String t, int position) {
                NoScrollGridView rvHot = holder.getView(R.id.rv_hot);
                ArrayList<String> hotList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    hotList.add(i + "");
                }
                LvCommonAdapter<String> rvCommonAdapter = new LvCommonAdapter<String>(mContext, R.layout.rv_hot_item, hotList) {
                    @Override
                    protected void convert(ViewHolder holder, String s, int position) {
                        ImageView ivCommodity = holder.getView(R.id.iv_commodity);
                        ivCommodity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CommodityDetailActivity.startIt(mContext,false);
                            }
                        });
                        ImageUtils.loadImage("http://img5.imgtn.bdimg.com/it/u=1730776793,842511342&fm=200&gp=0.jpg", R.drawable.defalut_img_88_88, ivCommodity);
                    }
                };
                rvHot.setAdapter(rvCommonAdapter);
            }
        });

        addItemViewDelegate(new ItemViewDelegate<String>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_shopping_item;
            }

            @Override
            public boolean isForViewType(String item, int position) {
                return item.equals("item");
            }

            @Override
            public void convert(ViewHolder holder, String t, int position) {
                ImageView ivCommodity = holder.getView(R.id.iv_commodity);
                ivCommodity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommodityDetailActivity.startIt(mContext,true);
                    }
                });
                ImageUtils.loadImage("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=438802142,1975222280&fm=26&gp=0.jpg", R.drawable.defalut_img_88_88, ivCommodity);
            }
        });
    }

    public void stopBanner() {
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }

    public void startBanner() {
        if (banner != null) {
            banner.startAutoPlay();
        }
    }
}
