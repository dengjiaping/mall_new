package com.giveu.shoppingmall.me.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.model.bean.response.InstalmentDetailResponse;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.TypeUtlis;

/**
 * Created by 513419 on 2017/6/23.
 * 还款明细
 */

public class IntalmentDetailsDialog extends CustomDialog {
    private TextView tvOverdue;
    private TextView tvCreditNo;
    private TextView tvGoodsName;
    private TextView tvLoanDate;
    private TextView tvLoanDays;
    private TextView tvOverDays;
    private TextView tvCreditAmount;
    private TextView tvOverAmount;
    private TextView tvInterestAmount;
    private TextView tvPayAmount;
    private TextView tvRepayAmount;
    private TextView tvKnow;

    public IntalmentDetailsDialog(Activity context) {
        super(context, R.layout.dialog_transaction_detail, R.style.customerDialog, Gravity.CENTER, false);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tvOverdue = (TextView) contentView.findViewById(R.id.tv_overdue);
        tvCreditNo = (TextView) contentView.findViewById(R.id.tv_creditNo);
        tvGoodsName = (TextView) contentView.findViewById(R.id.tv_goodsName);
        tvLoanDate = (TextView) contentView.findViewById(R.id.tv_loanDate);
        tvLoanDays = (TextView) contentView.findViewById(R.id.tv_loanDays);
        tvOverDays = (TextView) contentView.findViewById(R.id.tv_overDays);
        tvCreditAmount = (TextView) contentView.findViewById(R.id.tv_creditAmount);
        tvOverAmount = (TextView) contentView.findViewById(R.id.tv_overAmount);
        tvInterestAmount = (TextView) contentView.findViewById(R.id.tv_interestAmount);
        tvPayAmount = (TextView) contentView.findViewById(R.id.tv_payAmount);
        tvRepayAmount = (TextView) contentView.findViewById(R.id.tv_repayAmount);
        tvKnow = (TextView) contentView.findViewById(R.id.tv_confirm);
        tvKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setInstalmentDetailsData(InstalmentDetailResponse details, String creditType) {
        //数据为空不作任何处理
        if (details == null) {
            return;
        }
        tvCreditNo.setText("合同号：" + details.contractNo);
        //分期产品
        if (TypeUtlis.getConsumType(creditType) == 1 || TypeUtlis.getConsumType(creditType) == 2) {
            if (StringUtils.isNotNull(details.goodsName)) {
                tvGoodsName.setText("商品名称：" + details.goodsName);
            } else {
                tvGoodsName.setText("商品名称：--");
            }
            tvGoodsName.setVisibility(View.VISIBLE);
            tvCreditAmount.setVisibility(View.GONE);
            if (TypeUtlis.getConsumType(creditType) == 1) {
                tvLoanDays.setText("分期总额：" + StringUtils.format2(details.creditAmount) + "元");
            } else {
                tvLoanDays.setText("交易金额：" + StringUtils.format2(details.creditAmount) + "元");
            }
            tvInterestAmount.setVisibility(View.VISIBLE);
            tvInterestAmount.setText("当前金额：" + StringUtils.format2(details.instalmentAmount) + "元");

        } else if (TypeUtlis.getConsumType(creditType) == 3) {
            //取现随借随还
            tvGoodsName.setVisibility(View.GONE);
            tvLoanDays.setText("已借天数：" + details.loanDays + "天");
            tvLoanDays.setVisibility(View.VISIBLE);
            tvCreditAmount.setVisibility(View.VISIBLE);
            tvCreditAmount.setText("取现金额：" + StringUtils.format2(details.creditAmount) + "元");
            if (StringUtils.isNotNull(details.interestAmount)) {
                tvInterestAmount.setVisibility(View.VISIBLE);
                tvInterestAmount.setText("当前息费：" + StringUtils.format2(details.interestAmount) + "元");
            } else {
                tvInterestAmount.setVisibility(View.GONE);
            }
        }
        if (StringUtils.isNotNull(details.loanDate)) {
            tvLoanDate.setText("交易时间：" + details.loanDate);
        } else {
            tvLoanDate.setText("交易时间：--");
        }
        if (details.overDays > 0) {
            tvOverAmount.setText("逾期费用：" + StringUtils.format2(details.overAmount) + "元");
            tvOverAmount.setVisibility(View.VISIBLE);
            tvOverDays.setText("逾期天数：" + details.overDays + "天");
            tvOverDays.setVisibility(View.VISIBLE);
            tvOverdue.setVisibility(View.VISIBLE);
        } else {
            tvOverAmount.setVisibility(View.GONE);
            tvOverDays.setVisibility(View.GONE);
            tvOverdue.setVisibility(View.GONE);
        }
        tvPayAmount.setText("当期已还：" + StringUtils.format2(details.payAmount) + "元");
        tvRepayAmount.setText("当前应还：" + StringUtils.format2(details.repayAmount) + "元");
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        setCanceledOnTouchOutside(false);
    }
}
