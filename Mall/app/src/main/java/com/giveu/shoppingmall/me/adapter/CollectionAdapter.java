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
        ImageUtils.loadImage(item.srcIp+"/s240x240fdfs/"+item.src, R.drawable.defalut_img_88_88, ivGoods);
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
        holder.setText(R.id.tv_name,item.name);
        if(item.hasShowMonthAmount()){
            //true就不显示月供,实际上隐藏售价view，月供view变成售价
            holder.setVisible(R.id.tv_price,false);
            String text = StringUtils.format2(item.salePrice);
            String startStr = text.substring(0,text.indexOf("."));
            String endStr = text.substring(text.indexOf("."),text.length());

            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount,"售价：¥",startStr,endStr,"",14,11,R.color.red,R.color.color_999999);
        }else{
            holder.setVisible(R.id.tv_price,true);
            String text = StringUtils.format2(item.monthAmount);
            String startStr = text.substring(0,text.indexOf("."));
            String endStr = text.substring(text.indexOf("."),text.length());
            CommonUtils.setTextWithSpanSizeAndColor(tvMonthAmount,"月供：¥",startStr,endStr,"起",14,11,R.color.red,R.color.color_999999);
            holder.setText(R.id.tv_price,"¥"+item.salePrice);
        }

        holder.setVisible(R.id.tv_invalid,item.hasInvalid());
    }

    public interface CbItemCheckListener {
        void itemClick();
    }
}
