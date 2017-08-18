package com.giveu.shoppingmall.model.bean.response;

/**
 * Created by 101912 on 2017/8/16.
 */

public class CouponBean {

    public CouponBean(String date, boolean isNotLine) {
        this.date = date;
        this.isNotLine = isNotLine;
    }

    public String date;
    public boolean isNotLine;   //true表示是优惠券item，false表示是分隔线item
}
