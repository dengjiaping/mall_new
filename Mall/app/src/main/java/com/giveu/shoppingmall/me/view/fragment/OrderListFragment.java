package com.giveu.shoppingmall.me.view.fragment;

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
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.view.activity.OrderInfoActivity;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.OrderListResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
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

public class OrderListFragment extends BaseFragment implements IOrderInfoView {

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
            orderState = bundle.getString(OrderState.ORDER_TYPE, "");
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
                //下拉刷新的时候相当于在ListView的最上方又添加一个item，所以对应item的点击事件需position-1
                String orderNo = mDatas.get(position - 1).orderNo;
                String src = mDatas.get(position - 1).srcIp + mDatas.get(position - 1).src;
                if (StringUtils.isNotNull(orderNo) && StringUtils.isNotNull(src))
                    OrderInfoActivity.startIt(mBaseContext, orderNo, src);
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
        ApiImpl.getOrderList(mBaseContext, OrderState.CHANNEL, LoginHelper.getInstance().getIdPerson(), pageNum + "", pageSize + "", orderState, new BaseRequestAgent.ResponseListener<OrderListResponse>() {
            @Override
            public void onSuccess(OrderListResponse response) {
                ll_emptyView.setVisibility(View.GONE);
                ptrlv.setPullRefreshEnable(true);
                ptrlv.setPullLoadEnable(false);
                //onRefresh时数据为空，显示空界面
                if (pageNum == 1 && (response.data == null || CommonUtils.isNullOrEmpty(response.data.skuInfo))) {
                    mDatas.clear();
                    ll_emptyView.setVisibility(View.VISIBLE);
                }
                //加载下一页刚好没数据，显示FootView
                if (pageNum != 1 && (response.data == null || CommonUtils.isNullOrEmpty(response.data.skuInfo))) {
                    ptrlv.showEnd("没有更多数据了");
                }
                if (StringUtils.isNotNull(response.channelName)) {
                    channelName = response.channelName;
                }
                if (response.data != null) {
                    if (CommonUtils.isNotNullOrEmpty(response.data.skuInfo)) {
                        if (pageNum == 1) {
                            mDatas.clear();
                        }
                        //当订单列表个数大于或等于pageSize时，则显示加载更多，否则显示没有更多数据了
                        if (response.data.skuInfo.size() >= pageSize) {
                            ptrlv.setPullLoadEnable(true);
                        } else {
                            ptrlv.showEnd("没有更多数据了");
                        }
                        pageNum++;
                        mDatas.addAll(response.data.skuInfo);
                    }
                }
                adapter.notifyDataSetChanged();
                ptrlv.onRefreshComplete();
            }

            @Override
            public void onError(BaseBean errorBean) {
                ptrlv.onRefreshComplete();
                ptrlv.setPullRefreshEnable(false);
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    //不需要做处理
    @Override
    public void showOrderDetail(BaseBean response) {

    }

    //订单删除成功
    @Override
    public void deleteOrderSuccess(String orderNo) {
        removeData(orderNo);
        ToastUtils.showLongToast("订单删除成功");
    }

    //取消订单成功
    @Override
    public void cancelOrderSuccess(String orderNo) {
        removeData(orderNo);
        ToastUtils.showLongToast("订单取消成功");
    }

    //确认收货成功
    @Override
    public void confirmReceiveSuccess(String orderNo) {
        removeData(orderNo);
        ToastUtils.showLongToast("确认收货成功");
    }

    //在mDatas中移除订单号为orderNo的订单
    private void removeData(String orderNo) {
        for (OrderListResponse.SkuInfoBean data : mDatas) {
            if (orderNo.equals(data.orderNo)) {
                OrderListResponse.SkuInfoBean skuInfoBean = data;
                mDatas.remove(skuInfoBean);
                break;
            }
        }
        adapter.notifyDataSetChanged();
        //当数据为空时，显示空界面
        if (mDatas.size() == 0) {
            ptrlv.setPullLoadEnable(false);
            ll_emptyView.setVisibility(View.VISIBLE);
        }
    }
}
