package com.giveu.shoppingmall.me.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.me.presenter.OrderHandlePresenter;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import butterknife.BindView;

/**
 * Created by 101912 on 2017/8/25.
 */

public class OrderListFragment extends BaseFragment implements IOrderInfoView {

    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    private LvCommonAdapter<String> adapter;
    private OrderHandlePresenter presenter;


    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseLayout.setTitleBarAndStatusBar(false, false);
        presenter = new OrderHandlePresenter(this);
        presenter.getOrderInfo();
        return inflater.inflate(R.layout.fragment_order_list, container, false);
    }

    @Override
    protected void setListener() {
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });


    }

    @Override
    public void initDataDelay() {

    }

    @Override
    public void showOrderInfo() {

    }
}
