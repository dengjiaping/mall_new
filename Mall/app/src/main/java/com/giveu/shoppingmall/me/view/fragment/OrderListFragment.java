package com.giveu.shoppingmall.me.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.adapter.OrderListAdapter;
import com.giveu.shoppingmall.me.presenter.OrderHandlePresenter;
import com.giveu.shoppingmall.me.view.activity.OrderInfoActivity;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.OrderListResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 101912 on 2017/8/25.
 */

public class OrderListFragment extends BaseFragment implements IOrderInfoView{

    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    @BindView(R.id.ll_emptyView)
    LinearLayout ll_emptyView;

    private View fragmentView;
    private String channelName = "";//渠道名称

    private int pageNum = 1;//当前页数
    private final int pageSize = 10;//每页的item数
    private String orderState;

    private OrderHandlePresenter presenter;
    private OrderListAdapter adapter;
    private List<OrderListResponse.SkuInfoBean> mDatas;

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderState = bundle.getString("orderState", "");
        }
        baseLayout.setTitleBarAndStatusBar(false, false);
        fragmentView = View.inflate(mBaseContext, R.layout.fragment_order_list, null);
        ButterKnife.bind(this, fragmentView);
        mDatas = new ArrayList<>();

        presenter = new OrderHandlePresenter(this);

        adapter = new OrderListAdapter(mBaseContext, mDatas, channelName, presenter);
        ptrlv.setAdapter(adapter);
        ptrlv.setPullRefreshEnable(true);
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setPullLoadEnable(false);
        return fragmentView;

    }


    @Override
    protected void setListener() {
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderInfoActivity.startIt(mBaseContext);
            }
        });

        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                ptrlv.setPullLoadEnable(false);
                onRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ptrlv.setPullRefreshEnable(false);
                initDataDelay();
            }
        });

    }

    //下拉刷新
    private void onRefresh() {
        //刷新时pageNum重新设置为1
        pageNum = 1;
        initDataDelay();
    }

    @Override
    public void initDataDelay() {
        initData();
    }


    private void initData() {
        ApiImpl.getOrderList(mBaseContext, "sc", LoginHelper.getInstance().getIdPerson(), pageNum + "", pageSize + "", orderState, new BaseRequestAgent.ResponseListener<OrderListResponse>() {
            @Override
            public void onSuccess(OrderListResponse response) {
                ptrlv.setPullRefreshEnable(true);
                if (StringUtils.isNotNull(response.channelName)) {
                    channelName = response.channelName;
                }
                if (CommonUtils.isNotNullOrEmpty(response.data.skuInfo)) {
                    ll_emptyView.setVisibility(View.GONE);
                    if (pageNum == 1) {
                        mDatas.clear();
                        //当订单列表个数大于或等于pageSize时，则显示加载更多，否则显示没有更多数据了
                        if (response.data.skuInfo.size() >= pageSize) {
                            ptrlv.setPullLoadEnable(true);
                        } else {
                            ptrlv.setPullLoadEnable(false);
                            ptrlv.showEnd("没有更多数据了");
                        }
                        ptrlv.onRefreshComplete();
                    }
                    pageNum++;
                    mDatas.addAll(response.data.skuInfo);
                    adapter.notifyDataSetChanged();
                } else {
                    //表示一点数据都没有，显示空icon
                    if (pageNum == 1) {
                        mDatas.clear();
                        adapter.notifyDataSetChanged();
                        ptrlv.onRefreshComplete();
                        ptrlv.setPullLoadEnable(false);
                        ll_emptyView.setVisibility(View.VISIBLE);
                    }
                    //刚好下一页没数据
                    else {
                        ptrlv.setPullLoadEnable(false);
                        ptrlv.showEnd("没有更多数据了");
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                ptrlv.onRefreshComplete();
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }



}
