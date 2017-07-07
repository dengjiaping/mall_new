package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.TransactionDetailPresenter;
import com.giveu.shoppingmall.me.view.agent.ITransactionDetailView;
import com.giveu.shoppingmall.model.bean.response.TransactionDetailResponse;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.TypeUtlis;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/23.
 */

public class TransactionDetailActivity extends BaseActivity implements ITransactionDetailView {

    @BindView(R.id.tv_creditNo)
    TextView tvCreditNo;
    @BindView(R.id.tv_goodsName)
    TextView tvGoodsName;
    @BindView(R.id.ll_goos_name)
    LinearLayout llGoosName;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.ll_credit_detail)
    LinearLayout llCreditDetail;
    @BindView(R.id.tv_transaction_type)
    TextView tvTransactionType;
    @BindView(R.id.tv_pay_date)
    TextView tvPayDate;
    @BindView(R.id.tv_pay_status)
    TextView tvPayStatus;
    private String idCredit;
    private String creditType;

    public static void startIt(Activity activity, String idCredit, String creditType) {
        Intent intent = new Intent(activity, TransactionDetailActivity.class);
        intent.putExtra("idCredit", idCredit);
        intent.putExtra("creditType", creditType);
        activity.startActivity(intent);
    }

    private TransactionDetailPresenter presenter;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_transaction_detail);
        baseLayout.setTitle("交易详情");
        presenter = new TransactionDetailPresenter(this);
        idCredit = getIntent().getStringExtra("idCredit");
        creditType = getIntent().getStringExtra("creditType");
        presenter.getContractDetails(idCredit, creditType);
        initTextView(new TransactionDetailResponse());
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void setData() {

    }

    @OnClick(R.id.ll_credit_detail)
    public void onCreditDetail() {
        CreditDetailActivity.startIt(mBaseContext, idCredit);
    }

    @Override
    public void showTransactionDetail(TransactionDetailResponse data) {
        initTextView(data);
    }

    private void initTextView(TransactionDetailResponse data) {
        tvCreditNo.setText(data.contractNo);
        //分期产品
        if (TypeUtlis.getConsumType(creditType) == 1) {
            tvTransactionType.setText("分期总额：");
            tvMoney.setText(data.creditAmount);
            tvPayDate.setText("分期数：");
            tvDate.setText(data.paymentNum + "期");
            tvPayStatus.setText("剩余应还本金：");
            tvStatus.setText(data.lastPrincipal + "元");
            llCreditDetail.setVisibility(View.VISIBLE);
            llGoosName.setVisibility(View.VISIBLE);
            if (StringUtils.isNotNull(data.goodsName)) {
                tvGoodsName.setText(data.goodsName);
            } else {
                tvGoodsName.setText("--");
            }
        } else if (TypeUtlis.getConsumType(creditType) == 2 || TypeUtlis.getConsumType(creditType) == 3) {
            if (TypeUtlis.getConsumType(creditType) == 3) {
                //随借随还取现交易详情
                tvTransactionType.setText("取现金额：");
            } else {
                //一次性交易详情
                llGoosName.setVisibility(View.VISIBLE);
                if (StringUtils.isNotNull(data.goodsName)) {
                    tvGoodsName.setText(data.goodsName);
                } else {
                    tvGoodsName.setText("--");
                }
            }
            tvMoney.setText(data.creditAmount);
            tvDate.setText(data.dueDate);
            tvStatus.setText(TypeUtlis.getCreditStatusText(data.creditStatus));
        }
    }
}
