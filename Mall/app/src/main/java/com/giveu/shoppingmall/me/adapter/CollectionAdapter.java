package com.giveu.shoppingmall.me.adapter;

import android.content.Context;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.CollectionResponse;

import java.util.List;

/**
 * Created by 101900 on 2017/9/4.
 */

public class CollectionAdapter extends LvCommonAdapter<CollectionResponse> {
    public CollectionAdapter(Context context, List<CollectionResponse> datas) {
        super(context, R.layout.collection_item, datas);
    }


    @Override
    protected void convert(ViewHolder holder, CollectionResponse item, int position) {
        if (item.isShowCb) {
            //显示
            holder.setVisible(R.id.cb_choose, true);
        } else {
            holder.setVisible(R.id.cb_choose, false);
        }
    }
}
