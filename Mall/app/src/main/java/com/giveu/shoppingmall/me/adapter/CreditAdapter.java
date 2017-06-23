package com.giveu.shoppingmall.me.adapter;

import android.content.Context;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;

import java.util.List;

/**
 * Created by 513419 on 2017/6/23.
 */

public class CreditAdapter extends LvCommonAdapter<String> {

    public CreditAdapter(Context context, List<String> datas) {
        super(context, R.layout.lv_credit_item, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {

    }
}
