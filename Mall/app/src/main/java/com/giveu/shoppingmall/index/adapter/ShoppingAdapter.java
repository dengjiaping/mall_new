package com.giveu.shoppingmall.index.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;
import com.giveu.shoppingmall.model.bean.response.ShoppingResponse;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;

import java.util.List;

/**
 * Created by 513419 on 2017/8/30.
 */

public class ShoppingAdapter extends LvCommonAdapter<ShoppingResponse.ResultListBean> {
    private String srcIp;

    public ShoppingAdapter(Context context, List<ShoppingResponse.ResultListBean> datas) {
        super(context, R.layout.lv_shopping_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ShoppingResponse.ResultListBean item, int position) {
        ImageView ivCommodity = holder.getView(R.id.iv_commodity);
        ImageUtils.loadImageWithCorner(srcIp + item.src, R.drawable.ic_defalut_pic_corner, ivCommodity, DensityUtils.dip2px(4));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //K00002691 K00002713可以分期    K00002912可以一次
                CommodityDetailActivity.startIt(mContext, true, "K00002713");
            }
        });
        holder.setText(R.id.tv_commodity_name, item.name);
        holder.setText(R.id.tv_price, "¥"+StringUtils.format2(item.salePrice));
        holder.setText(R.id.tv_monthly_price, "月供：¥"+StringUtils.format2(item.monthAmount));
    }

    public void setDataAndSrcIp(List<ShoppingResponse.ResultListBean> data, String srcIp) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
        this.srcIp = srcIp + "/";
    }
}
