package com.giveu.shoppingmall.model.bean.response;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingBean {
    //0-title,1-content
    private int type;
    private int tag;
    private ShopTypesBean typesBean;
    public int shopTypeId;

    public ShoppingBean(int type, int tag, ShopTypesBean bean, int shopTypeId) {
        this.type = type;
        this.tag = tag;
        typesBean = bean;
        this.shopTypeId = shopTypeId;
    }

    public int getType() {
        return type;
    }

    public int getTag() {
        return tag;
    }


    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTypesBean(ShopTypesBean typesBean) {
        this.typesBean = typesBean;
    }

    public ShopTypesBean getTypesBean() {
        return typesBean;
    }
}
