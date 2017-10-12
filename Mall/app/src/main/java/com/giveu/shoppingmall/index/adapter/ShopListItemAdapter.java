package com.giveu.shoppingmall.index.adapter;

import android.app.Activity;
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
    private List<GoodsSearchResponse.GoodsBean> lists;

    public ShopListItemAdapter(Context context, List lists) {
        super(context, R.layout.adapter_shopping_list_item, lists);
        this.lists = lists;
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
                CommodityDetailActivity.startIt((Activity) mContext, item.hasShowMonthAmount(), item.skuCode, 0, false);
//                ConfirmOrderActivity.startIt(mContext, 20, 1, "K00003030");
            }
        });

        ImageView image = holder.getView(R.id.item_left_image);
        ImageUtils.loadImage(srcIp + "/" + item.src, R.drawable.default_img_240_240, image);

        holder.setText(R.id.item_right_name, StringUtils.ToAllFullWidthString(item.name));
        holder.setText(R.id.item_right_desc, StringUtils.ToAllFullWidthString(item.keywords));

        TextView tvMonthAmount = holder.getView(R.id.item_right_month_mount);
        TextView tvPrice = holder.getView(R.id.item_right_price);
        if (1 == item.isInstallments) {
            //true分期显示月供
            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "月供:¥", item.monthAmount, "起", 14, 10, R.color.red, R.color.color_999999);
            holder.setText(R.id.item_right_price, "¥" + StringUtils.format2(item.salePrice));
            tvPrice.setVisibility(View.VISIBLE);
        } else {
            //false就不显示月供,实际上隐藏售价view，月供view变成售价
            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "¥", item.salePrice, "", 14, 10, R.color.red, R.color.color_999999);
            tvPrice.setVisibility(View.INVISIBLE);
        }

        //最后一个Item隐藏分割线
        holder.setVisible(R.id.item_right_bg_line, position != (lists.size() - 1));
    }
}
