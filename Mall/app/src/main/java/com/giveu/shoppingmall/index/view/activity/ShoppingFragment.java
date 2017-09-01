package com.giveu.shoppingmall.index.view.activity;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.index.adapter.ShoppingAdapter;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 513419 on 2017/8/30.
 */

public class ShoppingFragment extends BaseFragment {
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    @BindView(R.id.statusView)
    View statusView;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    private ShoppingAdapter shoppingAdapter;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_shopping, null);
        baseLayout.setTitleBarAndStatusBar(false, false);
        ButterKnife.bind(this, view);
        ArrayList<String> shopList = new ArrayList<>();
        shopList.add("banner");
        shopList.add("hot");
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        shoppingAdapter.startBanner();
    }

    @Override
    public void onPause() {
        super.onPause();
        shoppingAdapter.stopBanner();
    }

    @Override
    protected void setListener() {
        ptrlv.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            private SparseArray<ItemRecod> recordSp = new SparseArray<>(0);
            private int mCurrentfirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //滑动时停止图片加载，停止轮播，停止滑动时恢复
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    ImageLoader.getInstance().resume();
                    shoppingAdapter.startBanner();
                } else {
                    ImageLoader.getInstance().pause();
                    shoppingAdapter.stopBanner();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(1);
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
                if (scrollY >= 0 && scrollY <= DensityUtils.dip2px(150)) {
                    //设置标题栏透明度0~255
                    llSearch.getBackground().setAlpha((int) (scrollY * 1.0 / DensityUtils.dip2px(150) * 255));
                } else if (scrollY > DensityUtils.dip2px(150)) {
                    //滑动距离大于255就设置为不透明
                    llSearch.getBackground().setAlpha(255);
                }
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
                if (state == PullToRefreshBase.State.PULL_TO_REFRESH||state == PullToRefreshBase.State.RELEASE_TO_REFRESH) {
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
}
