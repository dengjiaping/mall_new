package com.giveu.shoppingmall.index.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.ItemViewDelegate;
import com.giveu.shoppingmall.base.lvadapter.MultiItemTypeAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;

import java.util.List;

/**
 * Created by 513419 on 2017/8/30.
 */

public class ShoppingAdapter extends MultiItemTypeAdapter<String> {

    public ShoppingAdapter(Context context, List<String> datas) {
        super(context, datas);



        addItemViewDelegate(new ItemViewDelegate<String>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_shopping_item;
            }

            @Override
            public boolean isForViewType(String item, int position) {
                return item.equals("item");
            }

            @Override
            public void convert(ViewHolder holder, String t, int position) {
                ImageView ivCommodity = holder.getView(R.id.iv_commodity);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //K00002691可以分期   K00002713 K00002912可以一次
                        CommodityDetailActivity.startIt(mContext,false,"K00002713");
                    }
                });
            }
        });
    }

}
