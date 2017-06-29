package com.giveu.shoppingmall.model.bean.response;

/**
 * Created by 101900 on 2017/6/29.
 */

public class CashTypeResponse {
    public String month;//期数
    public boolean isChecked = false;//是否选中

    public CashTypeResponse(String month, boolean isChecked) {
        this.month = month;
        this.isChecked = isChecked;
    }
}
