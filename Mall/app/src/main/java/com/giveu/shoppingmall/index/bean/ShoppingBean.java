package com.giveu.shoppingmall.index.bean;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingBean {
    //0-title,1-content
    private int type;
    private String title;
    private String tag;

    public ShoppingBean(int type, String title, String tag) {
        this.type = type;
        this.title = title;
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }
}
