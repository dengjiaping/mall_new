package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.adapter.CreditAdapter;
import com.giveu.shoppingmall.me.presenter.CreditDetailPresenter;
import com.giveu.shoppingmall.me.view.agent.ICreditDetailView;
import com.giveu.shoppingmall.model.bean.response.ListInstalmentResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 513419 on 2017/6/23.
 */

public class CreditDetailActivity extends BaseActivity implements ICreditDetailView {
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    private CreditAdapter creditAdapter;
    private ArrayList<ListInstalmentResponse.Instalment> creditList;
    private int pageIndex = 1;
    private final int pageSize = 10;
    private String idCredit;
    private CreditDetailPresenter presenter;

    public static void startIt(Activity activity, String idCredit) {
        Intent intent = new Intent(activity, CreditDetailActivity.class);
        intent.putExtra("idCredit", idCredit);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_credit_detail);
        baseLayout.setTitle("分期明细");
        creditList = new ArrayList<>();
        creditAdapter = new CreditAdapter(mBaseContext, creditList);
        ptrlv.setAdapter(creditAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.DISABLED);
        ptrlv.setPullLoadEnable(false);
        presenter = new CreditDetailPresenter(this);
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {
        idCredit = getIntent().getStringExtra("idCredit");
        presenter.getCreditDetail(idCredit);
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

    @Override
    public void showCreditDetail(List<ListInstalmentResponse.Instalment> instalmentList) {
        if (CommonUtils.isNotNullOrEmpty(instalmentList)) {
            creditList.addAll(instalmentList);
            creditAdapter.notifyDataSetChanged();
        }
    }
}
