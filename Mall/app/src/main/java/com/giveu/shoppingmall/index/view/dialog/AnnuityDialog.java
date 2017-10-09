package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.MonthSupplyResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.widget.MaxHeightListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 524202 on 2017/9/30.
 */

public class AnnuityDialog extends CustomDialog {

    private TextView tvPrice;
    private TextView tvNum;
    private MaxHeightListView listView;
    private LvCommonAdapter<MonthSupplyResponse.PaymentListBean> mAdapter;
    private List<MonthSupplyResponse.PaymentListBean> lists;
    private TextView tvConfirm;

    public AnnuityDialog(Activity context, MonthSupplyResponse data) {
        super(context, R.layout.dialog_annuity_layout, R.style.customerDialog, Gravity.CENTER, false);
    }

    @Override
    protected void initView(View contentView) {
        tvPrice = (TextView) contentView.findViewById(R.id.dialog_annuity_price);
        tvNum = (TextView) contentView.findViewById(R.id.dialog_annuity_num);
        listView = (MaxHeightListView) contentView.findViewById(R.id.dialog_annuity_list);
        tvConfirm = (TextView) contentView.findViewById(R.id.dialog_annuity_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (lists == null) {
            lists = new ArrayList<>();
        }

        mAdapter = new LvCommonAdapter<MonthSupplyResponse.PaymentListBean>(mAttachActivity, R.layout.dialog_annuity_item, lists) {
            @Override
            protected void convert(ViewHolder viewHolder, MonthSupplyResponse.PaymentListBean item, int position) {
                viewHolder.setText(R.id.annuity_item_1, "第" + String.format("%02d", item.paymentNum) + "期")
                        .setText(R.id.annuity_item_2, "¥" + item.monthPay)
                        .setText(R.id.annuity_item_3, item.repayDate);
            }
        };
        listView.setAdapter(mAdapter);
    }

    public void refreshData(MonthSupplyResponse data, boolean isDataChange) {
        CommonUtils.setTextWithSpan(tvPrice, "首付： ", "¥" + data.initPay, R.color.color_4a4a4a, R.color.red);
        CommonUtils.setTextWithSpan(tvNum, "分期数： ", data.paymentNum + "期", R.color.color_4a4a4a, R.color.red);
        if (isDataChange) {
            lists.clear();
            if (data.paymentList != null) {
                lists.addAll(data.paymentList);
            }
            mAdapter.notifyDataSetChanged();
            if (lists.size() > 0) {
                listView.smoothScrollToPosition(0);
            }
        }
    }

}
