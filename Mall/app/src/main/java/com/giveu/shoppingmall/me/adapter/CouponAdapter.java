package com.giveu.shoppingmall.me.adapter;

import android.content.Context;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.ItemViewDelegate;
import com.giveu.shoppingmall.base.lvadapter.MultiItemTypeAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.CouponBean;

import java.util.List;

/**
 * Created by 101912 on 2017/8/16.
 */

public class CouponAdapter extends MultiItemTypeAdapter<CouponBean> {

    private boolean isInvalidCoupon;//是否有失效优惠券

    public CouponAdapter(Context context, List<CouponBean> datas) {
        super(context, datas);
        addItemViewDelegate(new ItemViewDelegate<CouponBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_coupon_item;
            }

            @Override
            public boolean isForViewType(CouponBean item, int position) {
                return item.isNotLine;
            }

            @Override
            public void convert(ViewHolder holder, CouponBean couponBean, int position) {

            }
        });

        addItemViewDelegate(new ItemViewDelegate<CouponBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.lv_invalid_item;
            }

            @Override
            public boolean isForViewType(CouponBean item, int position) {
                return !item.isNotLine;
            }

            @Override
            public void convert(ViewHolder holder, CouponBean couponBean, int position) {

            }
        });
    }


}
