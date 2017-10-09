package com.giveu.shoppingmall.model.bean.response;

/**
 * Created by 524202 on 2017/10/9.
 */

public class InsuranceFee {
    int isSelected;
    long serviceId;

    public InsuranceFee() {

    }

    public InsuranceFee(int isSelected, long serviceId) {
        this.isSelected = isSelected;
        this.serviceId = serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public void setSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}
