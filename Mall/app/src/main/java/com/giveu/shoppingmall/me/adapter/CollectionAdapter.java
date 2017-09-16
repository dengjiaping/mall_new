package com.giveu.shoppingmall.me.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.CollectionResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;

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
        TextView tvMonthAmount = holder.getView(R.id.tv_month_amount);
        TextView tvPrice = holder.getView(R.id.tv_price);
        ImageUtils.loadImage(item.srcIp + ImageUtils.ImageSize.img_size_240_240 + item.src, R.drawable.default_img_240_240, ivGoods);
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
        holder.setText(R.id.tv_name, StringUtils.ToAllFullWidthString(item.name));
        if (item.hasShowMonthAmount()) {
            //true就不显示月供,实际上隐藏售价view，月供view变成售价
            holder.setVisible(R.id.tv_price, false);
            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "¥", item.salePrice, "", 16, 13, R.color.red, R.color.color_999999);
        } else {
            holder.setVisible(R.id.tv_price, true);
            //textView,str1,str2,str3,tvSize1,tvSize2,tvColor1,tvColor2
            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount, "月供:¥", item.monthAmount, "起", 16, 13, R.color.red, R.color.color_999999);
            tvPrice.setText("¥" + StringUtils.format2(item.salePrice));
        }

        holder.setVisible(R.id.tv_invalid, item.hasInvalid());
    }

    public interface CbItemCheckListener {
        void itemClick();
    }
}
