package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 101900 on 2017/7/10.
 */

public class CashRecordsResponse extends BaseBean<List<CashRecordsResponse>> {
    public String appDate;
    public String loan;
    public String source;
    public String status;
}
