package com.giveu.shoppingmall.me.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.BulletinListResponse;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;

import java.util.List;


/**
 * 消息列表
 * Created by 101900 on 2016/12/22.
 */

public class MessageAdapter extends LvCommonAdapter<BulletinListResponse> {

    public MessageAdapter(Activity context, List<BulletinListResponse> datas) {
        super(context, R.layout.lv_message_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, BulletinListResponse item, int position) {
        holder.setText(R.id.tv_title, item.title);
        holder.setText(R.id.tv_content, item.digest);
        holder.setText(R.id.tv_date, item.createTimeToString);
        ImageView ivPhoto = holder.getView(R.id.iv_photo);
        ViewGroup.LayoutParams params = ivPhoto.getLayoutParams();
        if (StringUtils.isNull(item.imgUrl)) {
            ivPhoto.setVisibility(View.GONE);
        } else {
            ivPhoto.setVisibility(View.VISIBLE);
        }
        if (item.readFlag == 0) {
            //未读状态
            holder.setTextColor(R.id.tv_title, ContextCompat.getColor(mContext, R.color.black));
            holder.setTextColor(R.id.tv_read_detail, ContextCompat.getColor(mContext, R.color.black));
        } else {
            //已读状态
            holder.setTextColor(R.id.tv_title, ContextCompat.getColor(mContext, R.color.color_9b9b9b));
            holder.setTextColor(R.id.tv_read_detail, ContextCompat.getColor(mContext, R.color.color_9b9b9b));
        }
        //减去边距,宽高比8比17
        params.height = (int) ((DensityUtils.getWidth() - DensityUtils.dip2px(38)) * (8.0 / 17));
        params.width = DensityUtils.getWidth() - DensityUtils.dip2px(38);
        ImageUtils.loadImage(item.imgUrl, ivPhoto);
    }
}
