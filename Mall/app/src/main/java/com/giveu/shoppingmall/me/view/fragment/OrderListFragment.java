package com.giveu.shoppingmall.me.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.cash.view.activity.VerifyActivity;
import com.giveu.shoppingmall.event.RefreshEvent;
import com.giveu.shoppingmall.me.adapter.OrderListAdapter;
import com.giveu.shoppingmall.me.presenter.OrderHandlePresenter;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.view.activity.OrderInfoActivity;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.OrderDetailResponse;
import com.giveu.shoppingmall.model.bean.response.OrderListResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.EventBusUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.NetWorkUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 101912 on 2017/8/25.
 */

public class OrderListFragment extends BaseFragment implements IOrderInfoView<OrderDetailResponse> {

    PullToRefreshListView ptrlv;
    LinearLayout ll_emptyView;

    private View fragmentView;
    private String channelName = "";//渠道名称

    private int pageNum = 1;//当前页数
    private final int pageSize = 20;//每页的item数
    private int orderState;

    private OrderHandlePresenter presenter;
    private OrderListAdapter adapter;
    private List<OrderListResponse.SkuInfoBean> mDatas;
    FrameLayout emptyFrameLayout;
    boolean isFragmentViewInit = false;

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderState = bundle.getInt(OrderState.ORDER_TYPE);
        }
        baseLayout.setTitleBarAndStatusBar(false, false);
        View fragment_empty = View.inflate(mBaseContext, R.layout.fragment_empty, null);
        emptyFrameLayout = (FrameLayout) fragment_empty.findViewById(R.id.frame);

        return fragment_empty;

    }

    @Override
    protected void setListener() {}

    private synchronized void initFragmentView() {
        if (!isFragmentViewInit){
            fragmentView = View.inflate(mBaseContext, R.layout.fragment_order_list, null);
            emptyFrameLayout.removeAllViews();
            emptyFrameLayout.addView(fragmentView);

            ll_emptyView = (LinearLayout) fragmentView.findViewById(R.id.ll_emptyView);
            ptrlv = (PullToRefreshListView) fragmentView.findViewById(R.id.ptrlv);
            mDatas = new ArrayList<>();

            presenter = new OrderHandlePresenter(this);

            adapter = new OrderListAdapter(mBaseContext, mDatas, presenter);
            ptrlv.setAdapter(adapter);
            ptrlv.setPullRefreshEnable(true);
            ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
            ptrlv.setPullLoadEnable(false);
            ptrlv.setScrollingWhileRefreshingEnabled(true);
            ptrlv.getFooter().setOnClickListener(null);

            registerEventBus();
            setViewListener();

            isFragmentViewInit = true;
        }
    }

    private void setViewListener() {
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //下拉刷新的时候相当于在ListView的最上方又添加一个item，所以对应item的点击事件需position-1
                if (position - 1 >= 0 && position - 1 < adapter.getData().size()) {
                    String orderNo = adapter.getData().get(position - 1).orderNo;
                    if (StringUtils.isNotNull(orderNo))
                        OrderInfoActivity.startIt(mBaseContext, orderNo);
                }
            }
        });

        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                ptrlv.setPullLoadEnable(false);
                onRefresh();
                if (!NetWorkUtils.isNetWorkConnected()) {
                    ptrlv.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ptrlv.setPullRefreshEnable(false);
                initData();
            }
        });

        ptrlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //滑动时停止图片加载，滑动停止时恢复
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    ImageLoader.getInstance().resume();
                } else {
                    ImageLoader.getInstance().pause();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(RefreshEvent refreshEvent) {
        //接受到通知时，如果是当前页立即做刷新，否则重置加载的标识，在页面可见时做刷新
        if (getUserVisibleHint() && orderState == refreshEvent.orderState) {
            onRefresh();
        } else if (orderState == refreshEvent.orderState) {
            setDataInit(true);
        }
    }

    //下拉刷新
    private void onRefresh() {
        //刷新时pageNum重新设置为1
        pageNum = 1;
        initData();
    }

    @Override
    public void initDataDelay() {
        onRefresh();
    }

    private void initData() {
        ApiImpl.getOrderList(mBaseContext, Const.CHANNEL, LoginHelper.getInstance().getIdPerson(), pageNum + "", pageSize + "", orderState + "", new BaseRequestAgent.ExpandResponseListener<OrderListResponse>() {
            @Override
            public void beforeSuccessAndError() {
                initFragmentView();
                ptrlv.setPullRefreshEnable(true);
                ptrlv.onRefreshComplete();
            }

            @Override
            public void onSuccess(OrderListResponse response) {
                ll_emptyView.setVisibility(View.GONE);
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
                if (response.data != null) {
                    if (StringUtils.isNotNull(response.data.channelName)) {
                        channelName = response.data.channelName;
                        adapter.setChannelName(channelName);
                    }
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
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }


    //订单删除成功
    @Override
    public void deleteOrderSuccess(String orderNo) {
        removeData(orderNo);
        switch (orderState) {
            case OrderState.ALLRESPONSE:
                EventBusUtils.poseEvent(new RefreshEvent(OrderState.CLOSED));
                break;
            case OrderState.CLOSED:
                EventBusUtils.poseEvent(new RefreshEvent(OrderState.ALLRESPONSE));
                break;
        }
        ToastUtils.showLongToast("订单删除成功");
    }

    //取消订单成功
    @Override
    public void cancelOrderSuccess(String orderNo) {
        if (orderState == OrderState.ALLRESPONSE) {
            //刷新待支付状态列表
            EventBusUtils.poseEvent(new RefreshEvent(OrderState.WAITINGPAY));
        } else {
            removeData(orderNo);
        }
        //刷新所有，已关闭订单列表
        EventBusUtils.poseEvent(new RefreshEvent(OrderState.ALLRESPONSE));
        EventBusUtils.poseEvent(new RefreshEvent(OrderState.CLOSED));
        ToastUtils.showLongToast("订单取消成功");
    }

    //确认收货成功
    @Override
    public void confirmReceiveSuccess(String orderNo) {
        if (orderState == OrderState.ALLRESPONSE) {
            //刷新待支付
            EventBusUtils.poseEvent(new RefreshEvent(OrderState.WAITINGRECEIVE));
        } else {
            removeData(orderNo);
        }
        //刷新所有，已完成
        EventBusUtils.poseEvent(new RefreshEvent(OrderState.ALLRESPONSE));
        EventBusUtils.poseEvent(new RefreshEvent(OrderState.FINISHED));
        ToastUtils.showLongToast("确认收货成功");
    }

    //申请退款成功
    @Override
    public void applyToRefundSuccess() {
        EventBusUtils.poseEvent(new RefreshEvent(OrderState.ALLRESPONSE));
    }

    //验证交易密码成功
    @Override
    public void verifyPayPwdSuccess(String orderNo, boolean isWalletPay, String payment) {
        adapter.dismissDealPwdDialog();
        CommonUtils.closeSoftKeyBoard(mBaseContext);
        VerifyActivity.startItForShopping(mBaseContext, orderNo, isWalletPay, payment);
    }

    //验证交易密码失败
    @Override
    public void verifyPayPwdFailure(int remainTimes) {
        CommonUtils.closeSoftKeyBoard(mBaseContext);
        adapter.showDealPwdDialogError(remainTimes);
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

    /**
     * 此回调在于判断点击支付的订单是否有效
     *
     * @param response
     */
    @Override
    public void showOrderDetail(OrderDetailResponse response) {
        if (response != null && response.status == 1) {
            adapter.onPay(response.orderNo, response.payType + "", StringUtils.string2Double(response.totalPrice));
        } else {
            if (orderState == OrderState.ALLRESPONSE) {
                //刷新待支付
                EventBusUtils.poseEvent(new RefreshEvent(OrderState.WAITINGPAY));
            } else {
                removeData(response.orderNo);
            }
            //刷新所有，已关闭
            EventBusUtils.poseEvent(new RefreshEvent(OrderState.ALLRESPONSE));
            EventBusUtils.poseEvent(new RefreshEvent(OrderState.CLOSED));
            ToastUtils.showLongToast("订单已失效");
        }
    }
}
