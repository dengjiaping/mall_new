package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101900 on 2017/7/6.
 */

public class ProductResponse extends BaseBean<List<ProductResponse>> {


    /**
     * creditFrom : 0
     * creditTo : 3000
     * idProduct : 2644874
     * paymentNum : 3
     * prodCode : JM1_Da
     * prodName : JM0_3_Da
     */

    public String prodCode;
    public int paymentNum;
    public int creditTo;
    public int creditFrom;
    public String prodName;
    public int idProduct;
    public int productType;
    public boolean isChecked = false;//false 没有选中

    public ProductResponse(int paymentNum, int idProduct, boolean isChecked) {
        this.paymentNum = paymentNum;
        this.idProduct = idProduct;
        this.isChecked = isChecked;
    }
}
