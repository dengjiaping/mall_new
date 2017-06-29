package com.giveu.shoppingmall.model.bean.response;

/**
 * 月供数据
 * Created by 101900 on 2017/6/29.
 */

public class MonthlyDetailsResponse {
    public String stageNumber;//期数
    public String price;//月供金额
    public String date;//月供时间

    public MonthlyDetailsResponse(String stageNumber, String price, String date) {
        this.stageNumber = stageNumber;
        this.price = price;
        this.date = date;
    }
}
