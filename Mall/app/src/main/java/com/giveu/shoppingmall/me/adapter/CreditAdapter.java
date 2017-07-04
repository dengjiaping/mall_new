package com.giveu.shoppingmall.me.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.ListInstalmentResponse;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.TypeUtlis;

import java.util.List;

/**
 * Created by 513419 on 2017/6/23.
 */

public class CreditAdapter extends LvCommonAdapter<ListInstalmentResponse.Instalment> {

    public CreditAdapter(Context context, List<ListInstalmentResponse.Instalment> datas) {
        super(context, R.layout.lv_credit_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ListInstalmentResponse.Instalment item, int position) {
        //未结清状体字体颜色较浅，结清字体颜色较深
        if ("a".equals(item.payStatus)) {
            holder.setTextColor(R.id.tv_instalment, ContextCompat.getColor(mContext, R.color.color_9b9b9b));
            holder.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.color_9b9b9b));
            holder.setTextColor(R.id.tv_date, ContextCompat.getColor(mContext, R.color.color_9b9b9b));
            holder.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext, R.color.color_9b9b9b));
        } else {
            holder.setTextColor(R.id.tv_instalment, ContextCompat.getColor(mContext, R.color.color_4a4a4a));
            holder.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext, R.color.color_4a4a4a));
            holder.setTextColor(R.id.tv_date, ContextCompat.getColor(mContext, R.color.color_4a4a4a));
            holder.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext, R.color.color_00adb2));
        }
        holder.setText(R.id.tv_instalment, item.num + "");
        holder.setText(R.id.tv_amount, StringUtils.format2(item.amount));
        holder.setText(R.id.tv_date, item.dueDate);
        holder.setText(R.id.tv_status, TypeUtlis.getCreditStatusText(item.payStatus));
    }
}
