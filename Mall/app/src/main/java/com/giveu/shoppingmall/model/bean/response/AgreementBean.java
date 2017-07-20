package com.giveu.shoppingmall.model.bean.response;

/**
 * Created by 101900 on 2017/7/20.
 */

public class AgreementBean {
    public String startStr;
    public String endStr;
    public String url;

    public AgreementBean(String startStr, String endStr, String url) {
        this.startStr = startStr;
        this.endStr = endStr;
        this.url = url;
    }
}
