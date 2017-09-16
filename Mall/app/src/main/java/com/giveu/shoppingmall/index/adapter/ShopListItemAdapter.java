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
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;

import java.util.List;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShopListItemAdapter extends LvCommonAdapter<GoodsSearchResponse.GoodsBean> {
    private Context mContext;
    private String srcIp = "";

    public ShopListItemAdapter(Context context, List lists) {
        super(context, R.layout.adapter_shopping_list_item, lists);
        mContext = context;
    }

    public void setSrcIp(String ip) {
        srcIp = ip;
    }

    @Override
    protected void convert(ViewHolder holder, final GoodsSearchResponse.GoodsBean item, int position) {
        holder.setOnClickListener(R.id.item_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //K00002691可以分期   K00002713可以一次
                if (item.isInstallments == 1) {
                    CommodityDetailActivity.startIt(mContext, true, item.skuCode);
                } else {
                    CommodityDetailActivity.startIt(mContext, false, item.skuCode);
                }
            }
        });

        ImageView image = holder.getView(R.id.item_left_image);
        ImageUtils.loadImage(srcIp + "/" + item.src, R.drawable.ic_defalut_pic_corner, image);

        holder.setText(R.id.item_right_name, StringUtils.ToAllFullWidthString(item.name));
        holder.setText(R.id.item_right_desc, StringUtils.ToAllFullWidthString(item.keywords));

        TextView tvMonthAmount = holder.getView(R.id.item_right_month_mount);
        TextView tvPrice = holder.getView(R.id.item_right_price);
        //true就不显示月供,实际上隐藏售价view，月供view变成售价
        if (StringUtils.isNotNull(item.monthAmount)) {
            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "月供:¥", item.monthAmount, "起", 14, 11, R.color.red, R.color.color_999999);
            holder.setText(R.id.item_right_price, "¥" + StringUtils.format2(item.salePrice));
            tvPrice.setVisibility(View.VISIBLE);
        } else {
            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "¥", item.salePrice, "", 14, 11, R.color.red, R.color.color_999999);
            tvPrice.setVisibility(View.INVISIBLE);
        }
    }
}
