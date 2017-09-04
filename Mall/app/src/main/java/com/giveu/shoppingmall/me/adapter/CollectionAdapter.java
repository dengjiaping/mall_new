package com.giveu.shoppingmall.me.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.CollectionResponse;

import java.util.List;

/**
 * Created by 101900 on 2017/9/4.
 */

public class CollectionAdapter extends LvCommonAdapter<CollectionResponse> {
    public CbItemCheckListener mListener;

    public CollectionAdapter(Context context, List<CollectionResponse> datas, CbItemCheckListener listener) {
        super(context, R.layout.collection_item, datas);
        mListener = listener;
    }


    @Override
    protected void convert(final ViewHolder holder, final CollectionResponse item, final int position) {
        final CheckBox cbChoose = holder.getView(R.id.cb_choose);
        if (item.isShowCb) {
            //显示
            holder.setVisible(R.id.cb_choose, true);
        } else {
            holder.setVisible(R.id.cb_choose, false);
        }

        if (item.isCheck) {
            //选择
            holder.setChecked(R.id.cb_choose, true);
        } else {
            holder.setChecked(R.id.cb_choose, false);
        }

        holder.setOnClickListener(R.id.cb_choose, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isCheck = cbChoose.isChecked();
                if (mListener != null) {
                    mListener.itemClick();
                }
            }
        });

        holder.setText(R.id.tv_price,item.test);
    }

    public interface CbItemCheckListener {
        void itemClick();
    }
}
