package com.giveu.shoppingmall.me.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.ItemViewDelegate;
import com.giveu.shoppingmall.base.lvadapter.MultiItemTypeAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.BillResponse;

import java.util.List;

/**
 * Created by 513419 on 2017/6/22.
 */

public class BillAdapter extends MultiItemTypeAdapter<BillResponse> {

    public BillAdapter(Context context, List<BillResponse> datas) {
        super(context, datas);
        addItemViewDelegate(new ItemViewDelegate<BillResponse>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_bill_title;
            }

            @Override
            public boolean isForViewType(BillResponse item, int position) {
                return item.isTitle;
            }

            @Override
            public void convert(ViewHolder holder, final BillResponse item, int position) {
                final CheckBox cbChoose = holder.getView(R.id.cb_choose);
                cbChoose.setChecked(item.isChoose);
                holder.setOnClickListener(R.id.ll_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cbChoose.setChecked(!cbChoose.isChecked());
                        item.isChoose = cbChoose.isChecked();
                    }
                });
            }
        });

        addItemViewDelegate(new ItemViewDelegate<BillResponse>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_bill_item;
            }

            @Override
            public boolean isForViewType(BillResponse item, int position) {
                return !item.isTitle;
            }

            @Override
            public void convert(ViewHolder holder, BillResponse item, int position) {
            }
        });
    }
}
