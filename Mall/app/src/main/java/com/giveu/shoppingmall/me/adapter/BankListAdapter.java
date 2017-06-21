package com.giveu.shoppingmall.me.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.mynet.ApiUrl;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.ParentAdapter;
import com.giveu.shoppingmall.model.bean.response.BankListBean;
import com.giveu.shoppingmall.utils.ImageUtils;


/**
 * 银行列表
 * Created by 101900 on 2016/12/22.
 */

public class BankListAdapter extends ParentAdapter<BankListBean.ListBean> {

    public BankListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(getContext(), R.layout.bank_item, null);
        }
        TextView tv = getViewFromViewHolder(convertView, R.id.tv_management);
        ImageView iv = getViewFromViewHolder(convertView,R.id.iv_management);
        BankListBean.ListBean item = getItem(position);
        tv.setText(item.val);

        ImageUtils.loadImage(ApiUrl.BASE_URL+item.smallIco,R.drawable.defalut_img_88_88,iv);
        return convertView;
    }
}
