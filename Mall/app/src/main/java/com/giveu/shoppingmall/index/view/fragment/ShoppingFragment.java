package com.giveu.shoppingmall.index.view.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.index.adapter.BannerImageLoader;
import com.giveu.shoppingmall.index.adapter.ShoppingAdapter;
import com.giveu.shoppingmall.index.presenter.ShoppingPresenter;
import com.giveu.shoppingmall.index.view.activity.ShoppingListActivity;
import com.giveu.shoppingmall.index.view.activity.ShoppingSearchActivity;
import com.giveu.shoppingmall.index.view.agent.IShoppingView;
import com.giveu.shoppingmall.model.bean.response.IndexResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.widget.NoScrollGridView;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.DefaultTransformer;
import com.youth.banner.transformer.FlipHorizontalTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 513419 on 2017/8/30.
 */

public class ShoppingFragment extends BaseFragment implements IShoppingView {
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    @BindView(R.id.statusView)
    View statusView;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    private ShoppingAdapter shoppingAdapter;
    private ShoppingPresenter presenter;
    private int bannerHeight;
    private HeaderViewHolder viewHolder;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_shopping, null);
        baseLayout.setTitleBarAndStatusBar(false, false);
        baseLayout.setTopBarBackgroundColor(R.color.red);
        ButterKnife.bind(this, view);
        View headerView = View.inflate(mBaseContext, R.layout.lv_shopping_banner, null);
        viewHolder = new HeaderViewHolder(headerView);
        initHeaderView();
        ptrlv.getRefreshableView().addHeaderView(headerView);
        ArrayList<String> shopList = new ArrayList<>();
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shopList.add("item");
        shoppingAdapter = new ShoppingAdapter(mBaseContext, shopList);
        ptrlv.setAdapter(shoppingAdapter);
        ViewGroup.LayoutParams layoutParams = statusView.getLayoutParams();
        layoutParams.height = DensityUtils.getStatusBarHeight();
        statusView.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            statusView.setVisibility(View.GONE);
        }
        presenter = new ShoppingPresenter(this);
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingSearchActivity.startIt(mBaseContext);
            }
        });
        presenter.getIndexContent();
        return view;
    }


    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    /**
     * 头布局
     */
    private void initHeaderView() {
        initBanner();
        initHot();
        initCategory();
        initMore();
    }

    /**
     * 轮播图
     */
    private void initBanner() {
        bannerHeight = (int) (DensityUtils.getWidth() / (750 / 410.f));
        viewHolder.banner.getLayoutParams().height = bannerHeight;
        //设置banner样式
        viewHolder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        viewHolder.banner.setPageTransformer(false, new FlipHorizontalTransformer());
        //设置图片加载器
        viewHolder.banner.setImageLoader(new BannerImageLoader());
        viewHolder.banner.setPageTransformer(false, new DefaultTransformer());
        //设置图片集合
        final List<String> images = new ArrayList<String>();
        images.add("http://pic28.nipic.com/20130422/2547764_110759716145_2.jpg");
        images.add("http://pic27.nipic.com/20130319/10415779_103704478000_2.jpg");
        images.add("http://img5.imgtn.bdimg.com/it/u=2062816722,137475371&fm=26&gp=0.jpg");
        images.add("http://img.taopic.com/uploads/allimg/120222/34250-12022209414087.jpg");
        images.add("http://sc.jb51.net/uploads/allimg/131031/2-13103115593HY.jpg");
        viewHolder.banner.setImages(images);
        viewHolder.banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                LogUtil.e(images.get(position));
            }
        });
        //设置标题集合（当banner样式有显示title时）
        viewHolder.banner.setBannerTitles(images);
        //设置轮播时间
        viewHolder.banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        viewHolder.banner.setIndicatorGravity(BannerConfig.CENTER);
        viewHolder.banner.start();
    }

    /**
     * 热门品类
     */
    private void initHot() {
//        NoScrollGridView gvHot = (NoScrollGridView) headerView.findViewById(R.id.gv_hot);
       /* headerView.findViewById(R.id.shopping_hot_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingClassifyActivity.startIt(mBaseContext);
            }
        });*/

        ArrayList<String> hotList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            hotList.add(i + "");
        }
        LvCommonAdapter<String> commonAdapter = new LvCommonAdapter<String>(mBaseContext, R.layout.rv_hot_item, hotList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                LinearLayout llHot = holder.getView(R.id.ll_hot);
                llHot.getLayoutParams().width = (DensityUtils.getWidth() - DensityUtils.dip2px(15) * 3) / 2;
                llHot.getLayoutParams().height = (int) (llHot.getLayoutParams().width * (120 / 169f));
                ImageView ivCommodity = holder.getView(R.id.iv_commodity);
                ivCommodity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShoppingListActivity.startIt(mContext);
                    }
                });
            }
        };
        viewHolder.gvHot.setAdapter(commonAdapter);
    }

    /**
     * 分类
     */
    private void initCategory() {
        viewHolder.gvCategory.getLayoutParams().height = DensityUtils.dip2px(200);
        ArrayList<String> hotList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            hotList.add(i + "");
        }
        LvCommonAdapter<String> commonAdapter = new LvCommonAdapter<String>(mBaseContext, R.layout.gv_category_item, hotList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                LinearLayout llHot = holder.getView(R.id.ll_category);
                llHot.getLayoutParams().width = (DensityUtils.getWidth() - DensityUtils.dip2px(15) * 5) / 4;
                llHot.getLayoutParams().height = (int) (llHot.getLayoutParams().width * (240 / 159f));
                ImageView ivCommodity = holder.getView(R.id.iv_commodity);
                ivCommodity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShoppingListActivity.startIt(mContext);
                    }
                });
            }
        };
        viewHolder.gvCategory.setAdapter(commonAdapter);
    }


    /**
     * 更多类目
     */
    private void initMore() {
        viewHolder.ivCategoryMore.getLayoutParams().width = (DensityUtils.getWidth() - DensityUtils.dip2px(15) * 5) / 4;
        viewHolder.ivCategoryMore.getLayoutParams().height = (int) (viewHolder.ivCategoryMore.getLayoutParams().width * (240 / 159f));
        viewHolder.ivCategoryMore.getLayoutParams().height = viewHolder.ivCategoryMore.getLayoutParams().height + DensityUtils.dip2px(15 + 61);
    }


    @Override
    public void onStart() {
        super.onStart();
        viewHolder.banner.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewHolder.banner.stopAutoPlay();
    }


    @Override
    protected void setListener() {
        ptrlv.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            private HashMap<Integer, ItemRecod> recordSp = new HashMap<>();
            private int mCurrentfirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //滑动时停止图片加载，停止轮播，停止滑动时恢复
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    ImageLoader.getInstance().resume();
                    viewHolder.banner.startAutoPlay();
                } else {
                    ImageLoader.getInstance().pause();
                    viewHolder.banner.stopAutoPlay();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);
                if (firstView != null) {
                    ItemRecod itemRecord = recordSp.get(firstVisibleItem);
                    if (itemRecord == null) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();//获取最顶部Item的高度
                    itemRecord.top = firstView.getTop();//获取距离顶部的距离
                    recordSp.put(firstVisibleItem, itemRecord);//设置值
                }
                int scrollY = getScrollY();
                float rate = (float) (scrollY * 1.0 / bannerHeight);
                if (rate > 1) {
                    rate = 1;
                }
                if (rate > 0.5) {
                    ivMessage.setImageResource(R.drawable.classify_msg);
                } else {
                    ivMessage.setImageResource(R.drawable.ic_message);
                }
                ColorDrawable drawable = new ColorDrawable(getResources().getColor(R.color.white));
                int alpha = (int) (255 * (rate));
                drawable.setAlpha(alpha);
                llSearch.setBackgroundDrawable(drawable);
            }

            private int getScrollY() {
                int height = 0;
                for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
                    ItemRecod itemRecod = recordSp.get(i);
                    //快速滑动会为空，判断一下，发现的bug
                    if (itemRecod != null) {
                        height += itemRecod.height;
                    }
                }
                ItemRecod itemRecod = recordSp.get(mCurrentfirstVisibleItem);
                if (null == itemRecod) {
                    itemRecod = new ItemRecod();
                }
                return height - itemRecod.top;
            }

            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });
        ptrlv.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                if (state == PullToRefreshBase.State.PULL_TO_REFRESH || state == PullToRefreshBase.State.RELEASE_TO_REFRESH) {
                    llSearch.setVisibility(View.GONE);
                } else {
                    llSearch.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void initDataDelay() {

    }

    @Override
    public void getIndexContent(ArrayList<IndexResponse> contentList) {
        if (CommonUtils.isNotNullOrEmpty(contentList)) {
            for (IndexResponse indexResponse : contentList) {
                switch (indexResponse.typeValue){
                    case 0+"":
                        break;
                    case 1+"":
                        break;
                    case 2+"":
                        break;
                    case 3+"":
                        break;
                    case 4+"":
                        break;
                }
            }
        } else {
            LogUtil.e("is null");
        }
    }

    public static class HeaderViewHolder {
        @BindView(R.id.banner)
        Banner banner;
        @BindView(R.id.gv_hot)
        NoScrollGridView gvHot;
        @BindView(R.id.gv_category)
        NoScrollGridView gvCategory;
        @BindView(R.id.iv_category_more)
        ImageView ivCategoryMore;

        public HeaderViewHolder(View headerView) {
            ButterKnife.bind(this,headerView);
        }
    }
}
