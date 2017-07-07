package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * 分期取现月供明细
 * Created by 101900 on 2017/7/7.
 */

public class RpmDetailResponse extends BaseBean<List<RpmDetailResponse>> {
    public double monthPay;
    public int repayNum;
    public String repayDate;
}
