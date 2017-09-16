package com.giveu.shoppingmall.model.bean.response;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingBean {
    //0-title,1-content
    private int type;
    private ShopTypesBean typesBean;

    public ShoppingBean(int type, ShopTypesBean bean) {
        this.type = type;
        typesBean = bean;
    }

    public int getType() {
        return type;
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
