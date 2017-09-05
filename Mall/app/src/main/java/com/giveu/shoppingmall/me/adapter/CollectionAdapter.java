package com.giveu.shoppingmall.me.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.CollectionResponse;
import com.giveu.shoppingmall.utils.ImageUtils;

import java.util.List;

/**
 * Created by 101900 on 2017/9/4.
 */

public class CollectionAdapter extends LvCommonAdapter<CollectionResponse.ResultListBean> {
    public CbItemCheckListener mListener;

    public CollectionAdapter(Context context, List<CollectionResponse.ResultListBean> datas, CbItemCheckListener listener) {
        super(context, R.layout.collection_item, datas);
        mListener = listener;
    }

    @Override
    protected void convert(final ViewHolder holder, final CollectionResponse.ResultListBean item, final int position) {
        final CheckBox cbChoose = holder.getView(R.id.cb_choose);
        ImageView ivGoods = holder.getView(R.id.iv_goods);
        ImageUtils.loadImage("http://img5.imgtn.bdimg.com/it/u=1730776793,842511342&fm=200&gp=0.jpg", R.drawable.defalut_img_88_88, ivGoods);
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
        holder.setText(R.id.tv_price,item.salePrice);
        holder.setText(R.id.tv_name,item.name);
        holder.setText(R.id.tv_month_amount,item.monthAmount);
    }

    public interface CbItemCheckListener {
        void itemClick();
    }
}
