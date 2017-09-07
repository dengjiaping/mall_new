package com.giveu.shoppingmall.index.adapter;

import android.content.Context;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;

import java.util.List;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShopListItemAdapter extends LvCommonAdapter {
    private Context mContext;

    public ShopListItemAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    protected void convert(com.giveu.shoppingmall.base.lvadapter.ViewHolder viewHolder, Object item, int position) {
        viewHolder.setOnClickListener(R.id.item_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //K00002691可以分期   K00002713可以一次
                CommodityDetailActivity.startIt(mContext, true,"K00002691");
            }
        });
    }
}
