package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.CreditAdapter;
import com.giveu.shoppingmall.view.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by 513419 on 2017/6/23.
 */

public class CreditDetailActivity extends BaseActivity {
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    private CreditAdapter creditAdapter;
    private ArrayList<String> creditList;
    private int pageIndex = 1;
    private final int pageSize = 10;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, CreditDetailActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_credit_detail);
        baseLayout.setTitle("分期明细");
        creditList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            creditList.add(i + "");
        }
        creditAdapter = new CreditAdapter(mBaseContext, creditList);
        ptrlv.setAdapter(creditAdapter);
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
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 >= 0 && position - 1 < creditList.size()) {

                }

            }
        });
    }
}
