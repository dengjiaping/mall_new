package com.giveu.shoppingmall.repay.view.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.me.view.activity.BillListActivity;
import com.giveu.shoppingmall.repay.adpter.RepayAdapter;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainRepayFragment extends BaseFragment {
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    private RepayAdapter repayAdapter;
    private ArrayList<String> repayList;
    private ViewHolder headerHolder;
    private CustomDialog totalDialog;
    private TextView tvWithdrawals;
    private TextView tvLargeWithdrawals;
    private TextView tvHint;
    private TextView tvKnow;
    private int pageIndex = 1;
    private final int pageSize = 10;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_main_repay, null);
        ButterKnife.bind(this, view);
        baseLayout.setTitle("我的额度");
        baseLayout.hideBack();
        baseLayout.setBlueWhiteStyle();
        View headerView = inflater.inflate(R.layout.activity_quota, null);
        headerHolder = new ViewHolder(headerView);
        repayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            repayList.add(i + "");
        }
        repayAdapter = new RepayAdapter(mBaseContext, repayList);
        ptrlv.setAdapter(repayAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setPullLoadEnable(false);
        ptrlv.getRefreshableView().addHeaderView(headerView);
        return view;
    }


    @Override
    protected void setListener() {
        headerHolder.tvAvailableCredit.setOnClickListener(this);
        headerHolder.tvAvaiableWithdrawals.setOnClickListener(this);
        headerHolder.tvConsumable.setOnClickListener(this);

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
                if (position - 2 >= 0 && position - 2 < repayList.size()) {
                    BillListActivity.startIt(mBaseContext);
                }

            }
        });

    }

    @Override
    public void initDataDelay() {

    }

    /**
     * 1为总授信额度，2为取现总额度，3为消费总额度
     *
     * @param flag
     */
    private void showTotalDialog(int flag) {

        if (totalDialog == null) {
            totalDialog = new CustomDialog(mBaseContext, R.layout.dialog_avaialbe_credit
                    , R.style.customerDialog, Gravity.CENTER, false);
            totalDialog.setCanceledOnTouchOutside(false);
            tvWithdrawals = (TextView) totalDialog.findViewById(R.id.tv_withdrawals);
            tvLargeWithdrawals = (TextView) totalDialog.findViewById(R.id.tv_large_withdrawals);
            tvHint = (TextView) totalDialog.findViewById(R.id.tv_hint);
            tvKnow = (TextView) totalDialog.findViewById(R.id.tv_confirm);
        }
        switch (flag) {
            case 1:
                tvWithdrawals.setText("可用额度：" + "¥4000.00" + "\n总授信额度：" + " ¥8000.00");
                tvLargeWithdrawals.setVisibility(View.GONE);
                tvHint.setVisibility(View.GONE);
                break;

            case 2:
                tvWithdrawals.setText("取现可用额度：" + "¥4000.00" + "\n取现总额度：" + " ¥8000.00");
                tvLargeWithdrawals.setVisibility(View.VISIBLE);
                tvLargeWithdrawals.setText("大额现金分期可用额度：" + "¥4000.00" + "\n大额现金分期总额度：" + " ¥8000.00");
                tvHint.setVisibility(View.GONE);
                break;

            case 3:
                tvWithdrawals.setText("消费可用额度：" + "¥4000.00" + "\n消费总额度：" + " ¥8000.00");
                tvLargeWithdrawals.setVisibility(View.GONE);
                tvHint.setVisibility(View.GONE);
                break;
        }
        tvKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalDialog.dismiss();
            }
        });
        totalDialog.show();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_available_credit:
                showTotalDialog(1);
                break;

            case R.id.tv_avaiable_withdrawals:
                showTotalDialog(2);
                break;

            case R.id.tv_consumable:
                showTotalDialog(3);
                break;
        }
    }

    @Override
    protected boolean translateStatusBar() {
        return true;
    }

    static class ViewHolder {
        @BindView(R.id.tv_available_credit)
        TextView tvAvailableCredit;
        @BindView(R.id.tv_available_money)
        TextView tvAvailableMoney;
        @BindView(R.id.tv_avaiable_withdrawals)
        TextView tvAvaiableWithdrawals;
        @BindView(R.id.tv_withdrawals_money)
        TextView tvWithdrawalsMoney;
        @BindView(R.id.tv_consumable)
        TextView tvConsumable;
        @BindView(R.id.tv_consumable_money)
        TextView tvConsumableMoney;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
