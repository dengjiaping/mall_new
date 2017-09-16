package com.giveu.shoppingmall.index.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;
import com.giveu.shoppingmall.model.bean.response.GoodsSearchResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;

import java.util.List;

/**
 * Created by 513419 on 2017/8/30.
 */

public class ShoppingAdapter extends LvCommonAdapter<GoodsSearchResponse.GoodsBean> {
    private String srcIp;

    public ShoppingAdapter(Context context, List<GoodsSearchResponse.GoodsBean> datas) {
        super(context, R.layout.lv_shopping_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final GoodsSearchResponse.GoodsBean item, int position) {
        ImageView ivCommodity = holder.getView(R.id.iv_commodity);
        TextView tvPrice = holder.getView(R.id.tv_price);
        TextView tvMonthAmount = holder.getView(R.id.tv_monthly_price);
        if (StringUtils.isNull(item.skuCode)) {
            holder.getConvertView().setVisibility(View.INVISIBLE);
            return;
        } else {
            holder.getConvertView().setVisibility(View.VISIBLE);
        }
        ImageUtils.loadImageWithCorner(srcIp + item.src, R.drawable.ic_defalut_pic_corner, ivCommodity, DensityUtils.dip2px(4));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommodityDetailActivity.startIt(mContext, false, item.skuCode);
            }
        });
        holder.setText(R.id.tv_commodity_name, StringUtils.ToAllFullWidthString(item.name));

        CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "¥", item.salePrice, "", 16, 13, R.color.red, R.color.color_4a4a4a);
//        if (item.hasShowMonthAmount()) {
//            //true就不显示月供,实际上隐藏售价view，月供view变成售价
//            holder.setVisible(R.id.tv_price, false);
//            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "售价:¥", item.salePrice, "", 16, 13, R.color.red, R.color.color_4a4a4a);
//        } else {
//            holder.setVisible(R.id.tv_price, true);
//            //textView,str1,str2,str3,tvSize1,tvSize2,tvColor1,tvColor2
//            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "月供:¥", item.monthAmount, "起", 16, 13, R.color.red, R.color.color_4a4a4a);
//            tvPrice.setText("¥" + StringUtils.format2(item.salePrice));
//        }

    }

    public void setDataAndSrcIp(List<GoodsSearchResponse.GoodsBean> data, String srcIp) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
        this.srcIp = srcIp + ImageUtils.ImageSize.img_size_200_200;
    }
}
