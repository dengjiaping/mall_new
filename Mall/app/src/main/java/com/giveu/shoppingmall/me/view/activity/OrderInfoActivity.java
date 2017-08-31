package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.OrderHandlePresenter;
import com.giveu.shoppingmall.me.view.agent.IOrderInfoView;

/**
 * Created by 101912 on 2017/8/29.
 */

public class OrderInfoActivity extends BaseActivity implements IOrderInfoView{

    private OrderHandlePresenter presenter;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, OrderInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_info);
        baseLayout.setTitle("订单详情");
        presenter = new OrderHandlePresenter(this);
        presenter.getOrderInfo();
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {

    }

    @Override
    public void showOrderInfo() {

    }
}
