package com.giveu.shoppingmall.me.adapter;

import android.content.Context;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.ContractResponse;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.TypeUtlis;

import java.util.List;

/**
 * Created by 513419 on 2017/6/23.
 */

public class TransactionAdapter extends LvCommonAdapter<ContractResponse.Contract> {
    public TransactionAdapter(Context context, List<ContractResponse.Contract> datas) {
        super(context, R.layout.lv_transaction_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ContractResponse.Contract item, int position) {
        if (StringUtils.isNotNull(item.paymentNum)) {
            holder.setText(R.id.tv_date, item.currentInstalment + "/" + item.paymentNum + "期");
        } else {
            holder.setText(R.id.tv_date, item.loanDate);
        }

        holder.setText(R.id.tv_type, TypeUtlis.getCreditTypeText(item.creditType));
        holder.setText(R.id.tv_creditAmount, item.creditAmount);
        holder.setText(R.id.tv_state, TypeUtlis.getCreditStatusText(item.creditStatus));
    }
}
