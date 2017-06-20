package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * 钱包激活状态的相关信息
 * Created by 101900 on 2017/6/20.
 */

public class ActivationResponse extends BaseBean<ActivationResponse> {
    public String status;//激活状态
    public String date1;//额度1
    public String date2;//额度2
    public String date3;//额度3
    public String bottomHint;//底部提示
    public String midHint;//中间提示语

    public ActivationResponse(String status, String date1, String date2, String date3, String bottomHint, String midHint) {
        this.status = status;
        this.date1 = date1;
        this.date2 = date2;
        this.date3 = date3;
        this.bottomHint = bottomHint;
        this.midHint = midHint;
    }
}
