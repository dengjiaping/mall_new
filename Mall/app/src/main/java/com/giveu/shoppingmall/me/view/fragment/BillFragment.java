package com.giveu.shoppingmall.me.view.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.adapter.BillAdapter;
import com.giveu.shoppingmall.me.presenter.InstalmentDetailsPresenter;
import com.giveu.shoppingmall.me.view.activity.BillListActivity;
import com.giveu.shoppingmall.me.view.agent.IInstalmentDetailsView;
import com.giveu.shoppingmall.me.view.dialog.IntalmentDetailsDialog;
import com.giveu.shoppingmall.model.bean.response.BillBean;
import com.giveu.shoppingmall.model.bean.response.BillListResponse;
import com.giveu.shoppingmall.model.bean.response.InstalmentDetailResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 513419 on 2017/6/22.
 */

public class BillFragment extends BaseFragment implements IInstalmentDetailsView {

    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    private BillAdapter billAdapter;
    private ArrayList<BillBean> billList;
    private int pageIndex = 1;
    private final int pageSize = 10;
    private boolean isCurrentMonth;
    private BillListActivity mActivity;
    private ViewHolder headerHolder;
    private double payMoney;
    private InstalmentDetailsPresenter presenter;
    private IntalmentDetailsDialog intalmentDetailsDialog; //还款明细对话框


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
        ptrlv.setMode(PullToRefreshBase.Mode.DISABLED);
        ptrlv.setPullLoadEnable(false);
        ptrlv.getRefreshableView().addHeaderView(headerView);
        intalmentDetailsDialog = new IntalmentDetailsDialog(mBaseContext);
        presenter = new InstalmentDetailsPresenter(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
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

        billAdapter.setOnMoneyChangetListener(new BillAdapter.OnMoneyChangeListener() {
            @Override
            public void moneyChange(double money) {
                payMoney = payMoney + money;
                mActivity.setPayMoney(payMoney);
            }
        });


        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                ptrlv.setPullLoadEnable(false);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ptrlv.setPullRefreshEnable(true);
            }
        });
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 2 >= 0 && position - 2 < billList.size()) {
                    if (!billList.get(position - 2).isTitle) {
                        BillBean billBean = billList.get(position - 2);
                        presenter.getInstalmentDetails(billBean.contractId, isCurrentMonth, billBean.numInstalment, billBean.productType);
                    }
                }

            }
        });

    }

    public void notifyDataSetChange(BillListResponse.HeaderBean headerBean, ArrayList<BillBean> billBeenList) {
        if (headerBean != null) {
            headerHolder.tvTotal.setText("¥" + headerBean.repayAmount);
            if(StringUtils.isNotNull(headerBean.endDate)) {
                headerHolder.tvDate.setText("最后还款日：" + headerBean.endDate);
            }else {
                headerHolder.tvDate.setText("最后还款日：--");
            }
            if (headerBean.isOverduce) {
                headerHolder.ivOverDue.setVisibility(View.VISIBLE);
            } else {
                headerHolder.ivOverDue.setVisibility(View.GONE);
            }
        }
        if (CommonUtils.isNotNullOrEmpty(billBeenList)) {
            billList.clear();
            billList.addAll(billBeenList);
            billAdapter.notifyDataSetChanged();
        } else {
            baseLayout.showEmpty(144, 0, "抱歉，没有账单哦");
        }
    }

    @Override
    public void initWithDataDelay() {
        //刚进来这个fragment时，还款金额为0
        mActivity.setPayMoney(0d);
    }

    @Override
    public void showInstalmentDetails(InstalmentDetailResponse data) {
        intalmentDetailsDialog.setInstalmentDetailsData(data);
        intalmentDetailsDialog.show();
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
        @BindView(R.id.iv_overdue)
        ImageView ivOverDue;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
