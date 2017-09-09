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

import java.util.List;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShopListItemAdapter extends LvCommonAdapter<GoodsSearchResponse.GoodsBean> {
    private Context mContext;

    public ShopListItemAdapter(Context context, List lists) {
        super(context, R.layout.adapter_shopping_list_item, lists);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final GoodsSearchResponse.GoodsBean item, int position) {
        holder.setOnClickListener(R.id.item_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //K00002691可以分期   K00002713可以一次
                if(item.getIsInstallments()==1){
                    CommodityDetailActivity.startIt(mContext, true, item.getSkuCode());
                }else {
                    CommodityDetailActivity.startIt(mContext, false, item.getSkuCode());
                }
            }
        });

        ImageView image = holder.getView(R.id.item_left_image);
        ImageUtils.loadImage(item.getSrcIp() + item.getSrc(), R.drawable.defalut_img_88_88, image);

        holder.setText(R.id.item_right_name, item.getName());

        TextView tvMonthAmount = holder.getView(R.id.item_right_month_mount);
        //true就不显示月供,实际上隐藏售价view，月供view变成售价
        CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "月供:¥", item.getMonthAmount() + "", "", 14, 11, R.color.red, R.color.color_999999);

        holder.setText(R.id.item_right_price, "¥" + item.getSalePrice());
    }
}
