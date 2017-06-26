package com.giveu.shoppingmall.me.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.me.adapter.BillAdapter;
import com.giveu.shoppingmall.me.view.dialog.TransactionDetailDialog;
import com.giveu.shoppingmall.me.view.activity.BillListActivity;
import com.giveu.shoppingmall.model.bean.response.BillResponse;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 513419 on 2017/6/22.
 */

public class BillFragment extends BaseFragment {

    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    private BillAdapter billAdapter;
    private ArrayList<BillResponse> billList;
    private int pageIndex = 1;
    private final int pageSize = 10;
    private boolean isCurrentMonth;
    private BillListActivity mActivity;
    private ViewHolder headerHolder;
    private TransactionDetailDialog transactionDetailDialog;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_list, null);
        ButterKnife.bind(this, view);
        baseLayout.setTitleBarAndStatusBar(false, false);
        mActivity = (BillListActivity) mBaseContext;
        View headerView = inflater.inflate(R.layout.lv_bill_header, null);
        headerHolder = new ViewHolder(headerView);
        isCurrentMonth = getArguments().getBoolean("isCurrentMonth", true);
        //根据是否当前月份，显示状态
        if (isCurrentMonth) {
            headerHolder.tvCurrentMonth.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_00bbc0));
            headerHolder.viewCurrent.setVisibility(View.VISIBLE);
            headerHolder.tvNextMonth.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_9b9b9b));
            headerHolder.viewNext.setVisibility(View.GONE);
        } else {
            headerHolder.tvCurrentMonth.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_9b9b9b));
            headerHolder.viewCurrent.setVisibility(View.GONE);
            headerHolder.tvNextMonth.setTextColor(ContextCompat.getColor(mBaseContext, R.color.color_00bbc0));
            headerHolder.viewNext.setVisibility(View.VISIBLE);
        }
        billList = new ArrayList<>();
        billAdapter = new BillAdapter(mBaseContext, billList);
        ptrlv.setAdapter(billAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setPullLoadEnable(false);
        ptrlv.getRefreshableView().addHeaderView(headerView);
        transactionDetailDialog = new TransactionDetailDialog(mBaseContext);
        return view;
    }

    private void setData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 30; i++) {
                    BillResponse bill = new BillResponse();
                    if (i % 4 == 0) {
                        bill.isTitle = true;
                    } else {
                        bill.isTitle = false;
                    }
                    billList.add(bill);
                }
                if (pageIndex == 1) {
                    ptrlv.onRefreshComplete();
                    ptrlv.setPullRefreshEnable(true);
                }
                billAdapter.notifyDataSetChanged();
         /*       if (CommonUtils.isNotNullOrEmpty(billList)) {
                    if (pageIndex == 1) {
                        billList.clear();
                        if (billList.size() >= pageSize) {
                            ptrlv.setPullLoadEnable(true);
                        } else {
                            ptrlv.setPullLoadEnable(false);
                            ptrlv.showEnd("没有更多数据");
                        }
//                emptyView.setVisibility(View.GONE);
                    }
//            billList.addAll(response.data.entities);
                    billAdapter.notifyDataSetChanged();
                    pageIndex++;
                } else {
                    if (pageIndex == 1) {
//                emptyView.setVisibility(View.VISIBLE);
                        ptrlv.setPullLoadEnable(false);
                    } else {
                        ptrlv.setPullLoadEnable(false);
                        ptrlv.showEnd("没有更多数据");
                    }
                }*/
            }
        }, 1000);
    }

    @Override
    protected void setListener() {

        headerHolder.tvCurrentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setCurrentItem(0);
            }
        });

        headerHolder.tvNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setCurrentItem(1);
            }
        });


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
                if (position - 2 >= 0 && position - 2 < billList.size()) {
                    if (!billList.get(position - 2).isTitle) {
                        transactionDetailDialog.show();
                    }
                }

            }
        });

    }

    @Override
    public void initWithDataDelay() {
        setData();
    }


    static class ViewHolder {
        @BindView(R.id.tv_total)
        TextView tvTotal;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_current_month)
        TextView tvCurrentMonth;
        @BindView(R.id.view_current)
        View viewCurrent;
        @BindView(R.id.tv_next_month)
        TextView tvNextMonth;
        @BindView(R.id.view_next)
        View viewNext;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}