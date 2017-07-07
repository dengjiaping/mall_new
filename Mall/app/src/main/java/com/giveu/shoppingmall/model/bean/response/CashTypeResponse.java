package com.giveu.shoppingmall.model.bean.response;

/**
 * Created by 101900 on 2017/6/29.
 */

public class CashTypeResponse {
    public String idProduct;//期数
    public String month;//期数
    public boolean isChecked = false;//是否选中
    public boolean isShow = false;//是否显示费率

    public CashTypeResponse(String month, boolean isChecked, boolean isShow) {
        this.month = month;
        this.isChecked = isChecked;
        this.isShow = isShow;
    }
}
