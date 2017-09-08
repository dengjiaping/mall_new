package com.giveu.shoppingmall.index.view.fragment;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.index.adapter.ShopListItemAdapter;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.GoodsSearchResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 524202 on 2017/9/7.
 */

public class ShoppingListFragment extends BaseFragment {

    @BindView(R.id.shopping_list_listview)
    PullToRefreshListView mRefreshView;
    @BindView(R.id.shopping_list_radiogroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.shopping_list_radio_all)
    RadioButton rbAll;
    @BindView(R.id.shopping_list_radio_sale)
    RadioButton rbSale;
    @BindView(R.id.shopping_list_radio_price)
    RadioButton rbPrice;
    @BindView(R.id.shopping_empty_view)
    TextView emptyView;

    private List<GoodsSearchResponse.GoodsBean> shoppingList;
    private ShopListItemAdapter mAdapter;
    private String channel = "SC";
    private String idPerson = "1234567";
    private String keyword = "iphone";
    private String orderSort = "synthesize";
    private int pageNum = 0;
    private int pageSize = 10;
    private int shopTypeId = 1;
    private int pageIndex = 1;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        ButterKnife.bind(this, view);

        baseLayout.setTitleBarAndStatusBar(false, false);

        shoppingList = new ArrayList<>();

        mAdapter = new ShopListItemAdapter(mBaseContext, shoppingList);
        mRefreshView.setAdapter(mAdapter);
        mRefreshView.setPullLoadEnable(false);
        rbAll.setChecked(true);
        rbPrice.setTag(true);

        return view;
    }

    @Override
    protected void setListener() {

        mRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mRefreshView.setPullLoadEnable(false);
                initDataForFragment();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initDataForFragment();
                mRefreshView.setPullRefreshEnable(false);
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.shopping_list_radio_all:
                        break;
                    case R.id.shopping_list_radio_sale:
                        break;
                    case R.id.shopping_list_radio_price:
                        break;
                    default:
                        break;
                }
            }
        });

        rbPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbPrice.isChecked()) {
                    boolean isUp = (boolean) rbPrice.getTag();
                    Drawable drawable;
                    if (isUp) {
                        drawable = getResources().getDrawable(R.drawable.sort_down);
                    } else {
                        drawable = getResources().getDrawable(R.drawable.sort_up);
                    }
                    Rect bounds = rbPrice.getCompoundDrawables()[0].getBounds();
                    drawable.setBounds(bounds);
                    rbPrice.setCompoundDrawables(drawable, null, null, null);
                    rbPrice.setTag(!isUp);
                }
            }
        });

    }

    public static ShoppingListFragment newInstance() {

        Bundle args = new Bundle();

        ShoppingListFragment fragment = new ShoppingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void initDataForFragment() {
        pageIndex = 1;
        ApiImpl.getGoodsSearch(mBaseContext, channel, idPerson, keyword, orderSort, pageNum, pageSize, shopTypeId, new BaseRequestAgent.ResponseListener<GoodsSearchResponse>() {
            @Override
            public void onSuccess(GoodsSearchResponse response) {
                mRefreshView.setPullRefreshEnable(true);
                if (CommonUtils.isNotNullOrEmpty(response.data.getGoodsList())) {

                    if (pageIndex == 1) {
                        shoppingList.clear();
                        if (response.data.getGoodsList().size() >= pageSize) {
                            mRefreshView.setPullLoadEnable(true);
                        } else {
                            mRefreshView.setPullLoadEnable(false);
                            mRefreshView.showEnd("没有更多数据");
                        }

                        emptyView.setVisibility(View.GONE);
                        mRefreshView.onRefreshComplete();
                    }

                    pageIndex++;
                    shoppingList.addAll(response.data.getGoodsList());
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (pageIndex == 1) {
                        mRefreshView.onRefreshComplete();
                        mRefreshView.setPullLoadEnable(false);
                        emptyView.setVisibility(View.VISIBLE);
                        shoppingList.clear();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mRefreshView.setPullLoadEnable(false);
                        mRefreshView.showEnd("没有更多数据");
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                mRefreshView.onRefreshComplete();
            }
        });
    }

    @Override
    public void initDataDelay() {
        initDataForFragment();
    }
}