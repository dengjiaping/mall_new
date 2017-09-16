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
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;
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
    //综合搜索
    private final static String SORT_BY_SIZE = "synthesize";
    //销量搜索
    private final static String SORT_BY_VOLUME = "salesVolume";
    //价格降序搜索
    private final static String SORT_PRICE_DESC = "salePriceDesc";
    //价格升序搜索
    private final static String SORT_PRICE_ASC = "salePriceAsc";
    //价格排序按钮未处于check状态
    private final static String STATUS_UNCHECK = "0";
    //价格排序按钮已check,且升序搜索
    private final static String STATUS_CHECK_UP = "1";
    //价格排序按钮已check,且降序搜索
    private final static String STATUS_CHECK_DOWN = "2";

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
    private String channel = Const.CHANNEL;
    private String idPerson = LoginHelper.getInstance().getIdPerson();
    private String keyword = "";
    private String orderSort = SORT_BY_SIZE;
    private int pageNum = 1;
    private int pageSize = 10;
    private int shopTypeId = 0;
    private String code;
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
        rbPrice.setTag(0);
        return view;
    }

    /**
     * 商品搜索——通过关键字搜索
     * 通常用于搜索界面主动搜索
     *
     * @param keyword 待搜索的关键字
     */
    public void searchByKeyWord(String keyword) {
        this.keyword = keyword;
        this.code = null;
        this.shopTypeId = 0;
        initDataForFragment();
    }

    /**
     * 商品搜索——通过ShopTypeId(商品三级类目Id)搜索
     * 通常用于分类界面点击商品搜索
     *
     * @param shopTypeId 待搜索的商品三级类目Id
     */
    public void searchByShopTypeId(int shopTypeId) {
        this.shopTypeId = shopTypeId;
        this.keyword = null;
        this.code = null;
        initDataForFragment();
    }

    /**
     * 商品搜索——通过code搜索
     * 通常用于首页搜索
     *
     * @param code 待搜索的code
     */
    public void searchByCode(String code) {
        this.code = code;
        this.keyword = null;
        this.shopTypeId = 0;
        initDataForFragment();
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
                        if (!STATUS_UNCHECK.equals(rbPrice.getTag().toString())) {
                            setPriceButtonStatus(STATUS_UNCHECK);
                        }
                        orderSort = SORT_BY_SIZE;
                        initDataForFragment();
                        break;
                    case R.id.shopping_list_radio_sale:
                        if (!STATUS_UNCHECK.equals(rbPrice.getTag().toString())) {
                            setPriceButtonStatus(STATUS_UNCHECK);
                        }
                        initDataForFragment();
                        orderSort = SORT_BY_VOLUME;
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
                String status = rbPrice.getTag().toString();
                if (STATUS_UNCHECK.equals(status) || STATUS_CHECK_DOWN.equals(status)) {
                    setPriceButtonStatus(STATUS_CHECK_UP);
                    orderSort = SORT_PRICE_ASC;
                } else if (STATUS_CHECK_UP.equals(status)) {
                    setPriceButtonStatus(STATUS_CHECK_DOWN);
                    orderSort = SORT_PRICE_DESC;
                }
                initDataForFragment();
            }
        });

    }

    private void setPriceButtonStatus(String status) {
        Drawable drawable;
        if (STATUS_UNCHECK.equals(status)) {
            drawable = getResources().getDrawable(R.drawable.sort_disable);
        } else if (STATUS_CHECK_UP.equals(status)) {
            drawable = getResources().getDrawable(R.drawable.sort_up);
        } else {
            drawable = getResources().getDrawable(R.drawable.sort_down);
        }
        Rect bounds = rbPrice.getCompoundDrawables()[0].getBounds();
        drawable.setBounds(bounds);
        rbPrice.setCompoundDrawables(drawable, null, null, null);
        rbPrice.setTag(status);
    }

    public static ShoppingListFragment newInstance() {
        Bundle args = new Bundle();
        ShoppingListFragment fragment = new ShoppingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initDataForFragment() {
        pageIndex = 1;
        ApiImpl.getGoodsSearch(mBaseContext, channel, idPerson, keyword, orderSort, pageNum, pageSize, shopTypeId, code, new BaseRequestAgent.ResponseListener<GoodsSearchResponse>() {
                    @Override
                    public void onSuccess(GoodsSearchResponse response) {
                        mRefreshView.setPullRefreshEnable(true);

                        if (response.data != null && CommonUtils.isNotNullOrEmpty(response.data.resultList)) {

                            if (pageIndex == 1) {
                                shoppingList.clear();
                                if (response.data.resultList.size() >= pageSize) {
                                    mRefreshView.setPullLoadEnable(true);
                                } else {
                                    mRefreshView.setPullLoadEnable(false);
                                    mRefreshView.showEnd("没有更多数据");
                                }

                                emptyView.setVisibility(View.GONE);
                                mRefreshView.onRefreshComplete();
                            }

                            pageIndex++;
                            shoppingList.addAll(response.data.resultList);
                            mAdapter.setSrcIp(response.data.srcIp);
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
                }

        );
    }

    @Override
    public void initDataDelay() {
        initDataForFragment();
    }
}
