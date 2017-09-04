package com.giveu.shoppingmall.index.adapter;

import android.content.Context;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.rvadapter.RvCommonAdapter;
import com.giveu.shoppingmall.base.rvadapter.ViewHolder;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;

import java.util.List;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShopListItemAdapter extends RvCommonAdapter {
    private Context mContext;

    public ShopListItemAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        holder.setOnClickListener(R.id.item_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommodityDetailActivity.startIt(mContext);
            }
        });
    }

}
