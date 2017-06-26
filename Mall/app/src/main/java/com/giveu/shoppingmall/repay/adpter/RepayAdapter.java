package com.giveu.shoppingmall.repay.adpter;

import android.content.Context;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;

import java.util.List;

/**
 * Created by 513419 on 2017/6/26.
 */

public class RepayAdapter extends LvCommonAdapter<String> {
    public RepayAdapter(Context context, List<String> datas) {
        super(context, R.layout.lv_repay_item, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {

    }
}
