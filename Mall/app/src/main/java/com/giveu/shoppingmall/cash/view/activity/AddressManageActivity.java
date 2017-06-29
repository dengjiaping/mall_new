package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.cash.adpter.AddressManageAdapter;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by 513419 on 2017/6/26.
 */

public class AddressManageActivity extends BaseActivity {
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    @BindView(R.id.tv_add_address)
    ClickEnabledTextView tvAddAddress;
    private AddressManageAdapter addressManageAdapter;
    private ArrayList<String> addressList;
    private int pageIndex = 1;
    private final int pageSize = 10;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, AddressManageActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address_manage);
        baseLayout.setTitle("选择收货地址");
        addressList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            addressList.add(i + "");
        }
        addressManageAdapter = new AddressManageAdapter(mBaseContext, addressList);
        ptrlv.setAdapter(addressManageAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setPullLoadEnable(false);
    }

    @Override
    public void setData() {

    }


    @Override
    public void setListener() {
        super.setListener();
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                setData();
                ptrlv.setPullLoadEnable(false);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setData();
                ptrlv.setPullRefreshEnable(true);
            }
        });
        tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddressActivity.startIt(mBaseContext);
            }
        });
    }

}
