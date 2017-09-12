package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.ArrayList;

/**
 * Created by 513419 on 2017/9/12.
 */

public class DownPayMonthPayResponse extends BaseBean<ArrayList<DownPayMonthPayResponse>> {

    /**
     * monthRatioEir : 35.483
     * prodCode : JM1_Da
     * paymentNum : 3
     * effectiveinterestrate : 38.299
     * creditTo : 3000.0
     * creditFrom : 200.0
     * prodName : JM0_3_Da
     * idProduct : 31311
     * annuity : 674.0
     * insuranceFeeMax : 15.0
     */

    public String monthRatioEir;
    public String prodCode;
    public int paymentNum;
    public String effectiveinterestrate;
    public String creditTo;
    public String creditFrom;
    public String initPay;
    public String prodName;
    public String idProduct;
    public String annuity;
    public String insuranceFeeMax;
}
