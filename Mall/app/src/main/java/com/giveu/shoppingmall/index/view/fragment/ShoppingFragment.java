package com.giveu.shoppingmall.index.view.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.index.adapter.BannerImageLoader;
import com.giveu.shoppingmall.index.adapter.ShoppingAdapter;
import com.giveu.shoppingmall.index.presenter.ShoppingPresenter;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;
import com.giveu.shoppingmall.index.view.activity.ShoppingClassifyActivity;
import com.giveu.shoppingmall.index.view.activity.ShoppingSearchActivity;
import com.giveu.shoppingmall.index.view.agent.IShoppingView;
import com.giveu.shoppingmall.me.view.activity.CustomWebViewActivity;
import com.giveu.shoppingmall.model.bean.response.GoodsSearchResponse;
import com.giveu.shoppingmall.model.bean.response.IndexResponse;
import com.giveu.shoppingmall.recharge.view.fragment.RechargeActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.fab_up_slide)
    ImageView fabUpSlide;
    @BindView(R.id.ll_emptyView)
    LinearLayout llEmptyView;
    private ShoppingAdapter shoppingAdapter;
    private ShoppingPresenter presenter;
    private int bannerHeight;
    private HeaderViewHolder viewHolder;
    private int pageIndex = 1;
    private final int pageSize = 20;
    private View headerView;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_shopping, null);
        baseLayout.setTitleBarAndStatusBar(false, false);
        baseLayout.setTopBarBackgroundColor(R.color.red);
        ButterKnife.bind(this, view);
        headerView = View.inflate(mBaseContext, R.layout.lv_shopping_header_view, null);
        viewHolder = new HeaderViewHolder(headerView);
        shoppingAdapter = new ShoppingAdapter(mBaseContext, new ArrayList<GoodsSearchResponse.GoodsBean>());

        ViewGroup.LayoutParams layoutParams = statusView.getLayoutParams();
        layoutParams.height = DensityUtils.getStatusBarHeight();
        statusView.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            statusView.setVisibility(View.GONE);
        }
        ptrlv.setAdapter(shoppingAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setPullLoadEnable(false);
        ptrlv.getRefreshableView().addHeaderView(headerView);
        ptrlv.setVisibility(View.GONE);
        presenter = new ShoppingPresenter(this);
        //刚开始隐藏头布局的所有内容
      /*  initBanner(null);
        initHot(null);
        initMore(null);
        initCategory(null);*/
//        skipToActivity(0,null);
        presenter.getHeadContent();
        return view;
    }


    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    /**
     * 轮播图
     *
     * @param indexResponse
     */
    private void initBanner(final IndexResponse indexResponse) {
        if (indexResponse == null || CommonUtils.isNullOrEmpty(indexResponse.decorations)) {
            return;
        }
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
        for (IndexResponse.DecorationsBean decoration : indexResponse.decorations) {
            images.add(indexResponse.srcIp + "/" + decoration.picSrc);
        }
        viewHolder.banner.setImages(images);
        viewHolder.banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                skipToActivity(indexResponse.srcIp, indexResponse.decorations.get(position));
                ShoppingClassifyActivity.startIt(mBaseContext);
            }
        });
        //设置标题集合（当banner样式有显示title时）
        viewHolder.banner.setBannerTitles(images);
        //设置轮播时间
        viewHolder.banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        viewHolder.banner.setIndicatorGravity(BannerConfig.CENTER);
        viewHolder.banner.start();
    }

    /**
     * 0跳转至h5,1跳转搜索界面，2跳转商品详情，3跳转分类
     *
     * @param decorationsBean
     */
    private void skipToActivity(String srcIp, IndexResponse.DecorationsBean decorationsBean) {
        switch (decorationsBean.urlTypeValue) {
            case 0:
                CustomWebViewActivity.startIt(mBaseContext, srcIp + "/" + decorationsBean.url, "");
//                CustomWebViewActivity.startIt(mBaseContext, "http://wx.dafycredit.cn/dafy-qq-store-detail/#/details/productArg?skuCode=K00002702", "");
                break;
            case 1:
                ShoppingSearchActivity.startIt(mBaseContext);
                break;
            case 2:
                CommodityDetailActivity.startIt(mBaseContext, false, decorationsBean.code);
                break;
            case 3:
                ShoppingClassifyActivity.startIt(mBaseContext);
                break;
        }
    }

    /**
     * 热门品类
     *
     * @param indexResponse
     */
    private void initHot(final IndexResponse indexResponse) {
        if (indexResponse == null || CommonUtils.isNullOrEmpty(indexResponse.decorations)) {
            return;
        }
        viewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingClassifyActivity.startIt(mBaseContext);
            }
        });
        LvCommonAdapter<IndexResponse.DecorationsBean> commonAdapter = new LvCommonAdapter<IndexResponse.DecorationsBean>(mBaseContext, R.layout.rv_hot_item, indexResponse.decorations) {
            @Override
            protected void convert(ViewHolder holder, final IndexResponse.DecorationsBean item, int position) {
                LinearLayout llHot = holder.getView(R.id.ll_hot);
                llHot.getLayoutParams().width = (DensityUtils.getWidth() - DensityUtils.dip2px(15) * 3) / 2;
                llHot.getLayoutParams().height = (int) (llHot.getLayoutParams().width * (120 / 169f));
                ImageView ivCommodity = holder.getView(R.id.iv_commodity);
                ImageView ivSmall = holder.getView(R.id.iv_small);
                ImageUtils.loadImageWithCorner(indexResponse.srcIp + ImageUtils.ImageSize.img_size_200_200
                        + item.picSrc, R.drawable.ic_defalut_pic_corner, ivCommodity, DensityUtils.dip2px(4));
                ImageUtils.loadImageWithCorner(indexResponse.srcIp + ImageUtils.ImageSize.img_size_200_200
                        + item.iconSrc, R.drawable.ic_defalut_pic_corner, ivSmall, DensityUtils.dip2px(4));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipToActivity(indexResponse.srcIp, item);
//                        ShoppingListActivity.startIt(mContext);
                    }
                });
                TextView tvTitle = holder.getView(R.id.tv_name);
                holder.setText(R.id.tv_title, item.title);
                holder.setText(R.id.tv_name, item.name);
                switch (position) {
                    case 0:
                        tvTitle.setTextColor(Color.parseColor("#FF1516"));
                        break;
                    case 1:
                        tvTitle.setTextColor(Color.parseColor("#01B0FF"));
                        break;
                    case 2:
                        tvTitle.setTextColor(Color.parseColor("#FF7A45"));
                        break;
                    case 3:
                        tvTitle.setTextColor(Color.parseColor("#FF4466"));
                        break;
                }
            }
        };
        viewHolder.gvHot.setAdapter(commonAdapter);
    }

    /**
     * 分类
     *
     * @param indexResponse
     */
    private void initCategory(final IndexResponse indexResponse) {
        if (indexResponse == null || CommonUtils.isNullOrEmpty(indexResponse.decorations)) {
            return;
        }
        LvCommonAdapter<IndexResponse.DecorationsBean> commonAdapter = new LvCommonAdapter<IndexResponse.DecorationsBean>(mBaseContext, R.layout.gv_category_item, indexResponse.decorations) {
            @Override
            protected void convert(ViewHolder holder, final IndexResponse.DecorationsBean item, int position) {
                LinearLayout llHot = holder.getView(R.id.ll_category);
                llHot.getLayoutParams().width = (DensityUtils.getWidth() - DensityUtils.dip2px(15) * 5) / 4;
                llHot.getLayoutParams().height = (int) (llHot.getLayoutParams().width * (240 / 159f));
                ImageView ivCommodity = holder.getView(R.id.iv_commodity);
                ImageUtils.loadImageWithCorner(indexResponse.srcIp + ImageUtils.ImageSize.img_size_200_200
                        + item.picSrc, R.drawable.ic_defalut_pic_corner, ivCommodity, DensityUtils.dip2px(4));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipToActivity(indexResponse.srcIp, item);
//                        ShoppingListActivity.startIt(mContext);
                    }
                });
                holder.setText(R.id.tv_title, item.title);
                holder.setText(R.id.tv_name, item.name);
            }
        };
        viewHolder.gvCategory.setAdapter(commonAdapter);
    }


    /**
     * 更多类目
     *
     * @param indexResponse
     */
    private void initMore(IndexResponse indexResponse) {
        if (indexResponse == null || CommonUtils.isNullOrEmpty(indexResponse.decorations)) {
            return;
        }
        viewHolder.llPhoneRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RechargeActivity.startIt(mBaseContext);
            }
        });
        viewHolder.ivCategoryMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingClassifyActivity.startIt(mBaseContext);
            }
        });
        viewHolder.ivCategoryMore.getLayoutParams().width = (DensityUtils.getWidth() - DensityUtils.dip2px(15) * 5) / 4;
        viewHolder.ivCategoryMore.getLayoutParams().height = (int) (viewHolder.ivCategoryMore.getLayoutParams().width * (240 / 159f));
        IndexResponse.DecorationsBean decorationsBean = indexResponse.decorations.get(0);
        ImageUtils.loadImageWithCorner(indexResponse.srcIp + ImageUtils.ImageSize.img_size_200_200
                + decorationsBean.picSrc, R.drawable.ic_defalut_pic_corner, viewHolder.ivCommodity, DensityUtils.dip2px(4));
        viewHolder.tvTitle.setText(decorationsBean.name);
        viewHolder.tvIntroduction.setText(decorationsBean.title);
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

    @OnClick({R.id.ll_search, R.id.fab_up_slide, R.id.ll_emptyView})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_search:
                ShoppingSearchActivity.startIt(mBaseContext);
                break;
            case R.id.fab_up_slide:
                ptrlv.getRefreshableView().smoothScrollToPosition(0);
                break;
            case R.id.ll_emptyView:
                presenter.getHeadContent();
                llEmptyView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void setListener() {

        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                presenter.getHeadContent();
                ptrlv.setPullLoadEnable(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ptrlv.setPullRefreshEnable(false);
            }
        });
        ptrlv.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            private SparseArray<ItemRecod> recordSp = new SparseArray<>();
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
                    recordSp.append(firstVisibleItem, itemRecord);//设置值
                }
                int scrollY = getScrollY();
                //滚动半个屏幕时显示回到顶部icon
                if (scrollY >= DensityUtils.getHeight()) {
                    fabUpSlide.setVisibility(View.VISIBLE);
                } else {
                    fabUpSlide.setVisibility(View.GONE);
                }
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
    public void getIndexContent(List<GoodsSearchResponse.GoodsBean> contentList, String srcIp) {
        //显示查询结果
        if (pageIndex == 1) {
            ptrlv.onRefreshComplete();
        }
        ptrlv.setPullRefreshEnable(true);
        if (CommonUtils.isNotNullOrEmpty(contentList)) {
            if (pageIndex == 1) {
                shoppingAdapter.setDataAndSrcIp(contentList, srcIp);
                if (contentList.size() >= pageSize) {
                    ptrlv.setPullLoadEnable(true);
                } else {
                    ptrlv.setPullLoadEnable(false);
                    ptrlv.showEnd("没有更多数据");
                }
            } else {
                shoppingAdapter.getData().addAll(contentList);
                shoppingAdapter.notifyDataSetChanged();
            }
            pageIndex++;
            llEmptyView.setVisibility(View.GONE);
            ptrlv.setVisibility(View.VISIBLE);
        } else {
            if (pageIndex == 1) {
                shoppingAdapter.getData().clear();
                shoppingAdapter.notifyDataSetChanged();
                llEmptyView.setVisibility(View.VISIBLE);
                ptrlv.setVisibility(View.GONE);
                ptrlv.setPullLoadEnable(false);
            } else {
                ptrlv.setPullLoadEnable(false);
                ptrlv.showEnd("没有更多数据");
            }
        }

    }

    @Override
    public void getDataFail() {
        ptrlv.setVisibility(View.GONE);
        llEmptyView.setVisibility(View.VISIBLE);
        pageIndex = 1;
    }

    @Override
    public void getHeadContent(ArrayList<IndexResponse> data) {
        if (CommonUtils.isNotNullOrEmpty(data)) {
            for (IndexResponse indexResponse : data) {
                switch (indexResponse.typeValue) {
                    case 0 + "":
                        initBanner(indexResponse);
                        break;
                    case 1 + "":
                        initCategory(indexResponse);
                        break;
                    case 2 + "":
                        initHot(indexResponse);
                        break;
                    case 3 + "":
                        initMore(indexResponse);
                        break;
                    case 4 + "":
                        presenter.getIndexContent("SC", LoginHelper.getInstance().getIdPerson(), pageIndex, pageSize, indexResponse.code);
                        break;
                }
            }
        } else {
            llEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
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
        @BindView(R.id.fill_view)
        View fillView;
        @BindView(R.id.iv_commodity)
        ImageView ivCommodity;
        @BindView(R.id.rl_hot)
        RelativeLayout rlHot;
        @BindView(R.id.ll_category_more)
        LinearLayout llCategoryMore;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_introduction)
        TextView tvIntroduction;
        @BindView(R.id.ll_phone_recharge)
        LinearLayout llPhoneRecharge;
        @BindView(R.id.shopping_hot_more)
        TextView tvMore;

        public HeaderViewHolder(View headerView) {
            ButterKnife.bind(this, headerView);
        }
    }
}
