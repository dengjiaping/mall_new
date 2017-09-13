package com.giveu.shoppingmall.model.bean.response;

/**
 * Created by 524202 on 2017/9/13.
 */

public class SkuInfo {
    private int quantity;
    private String skuCode;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public SkuInfo() {
    }

    public SkuInfo(int quantity, String skuCode) {
        this.quantity = quantity;
        this.skuCode = skuCode;
    }
}
